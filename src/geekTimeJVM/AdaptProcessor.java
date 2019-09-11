package geekTimeJVM;

import java.io.*;
import java.util.Set;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.JavaFileObject;

import javax.tools.Diagnostic.Kind;

@SupportedAnnotationTypes("geekTimeJVM.Adapt")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class AdaptProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            if (!"geekTimeJVM.Adapt".equals(annotation.getQualifiedName().toString())) {
                continue;
            }

            ExecutableElement targetAsKey = getExecutable(annotation, "value");

            for (ExecutableElement annotatedMethod : ElementFilter.methodsIn(roundEnv.getElementsAnnotatedWith(annotation))) {
                if (!annotatedMethod.getModifiers().contains(Modifier.PUBLIC)) {
                    processingEnv.getMessager().printMessage(Kind.ERROR, "@Adapt on non-public method");
                    continue;
                }
                if (!annotatedMethod.getModifiers().contains(Modifier.STATIC)) {
                    // TODO support non-static methods
                    continue;
                }

                TypeElement targetInterface = getAnnotationValueAsTypeElement(annotatedMethod, annotation, targetAsKey);
                if (targetInterface.getKind() != ElementKind.INTERFACE) {
                    processingEnv.getMessager().printMessage(Kind.ERROR, "@Adapt with non-interface input");
                    continue;
                }

                TypeElement enclosingType = getTopLevelEnclosingType(annotatedMethod);
                createAdapter(enclosingType, annotatedMethod, targetInterface);
            }
        }
        return true;
    }

    private void createAdapter(TypeElement enclosingClass, ExecutableElement annotatedMethod,
                               TypeElement targetInterface) {
        PackageElement packageElement = (PackageElement) enclosingClass.getEnclosingElement();
        String packageName = packageElement.getQualifiedName().toString();
        String className = enclosingClass.getSimpleName().toString();
        String methodName = annotatedMethod.getSimpleName().toString();
        String adapterName = className + "_" + methodName + "Adapter";

        ExecutableElement overriddenMethod = getFirstNonDefaultExecutable(targetInterface);

        try {
            Filer filer = processingEnv.getFiler();
            JavaFileObject sourceFile = filer.createSourceFile(packageName + "." + adapterName, new Element[0]);

            try (PrintWriter out = new PrintWriter(sourceFile.openWriter())) {
                out.println("package " + packageName + ";");
                out.println("import " + targetInterface.getQualifiedName() + ";");
                out.println();
                out.println("public class " + adapterName + " implements " + targetInterface.getSimpleName() + " {");
                out.println("  @Override");
                out.println("  public " + overriddenMethod.getReturnType() + " " + overriddenMethod.getSimpleName()
                        + formatParameter(overriddenMethod, true) + " {");
                out.println("    return " + className + "." + methodName + formatParameter(overriddenMethod, false) + ";");
                out.println("  }");
                out.println("}");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ExecutableElement getExecutable(TypeElement annotation, String methodName) {
        for (ExecutableElement method : ElementFilter.methodsIn(annotation.getEnclosedElements())) {
            if (methodName.equals(method.getSimpleName().toString())) {
                return method;
            }
        }
        processingEnv.getMessager().printMessage(Kind.ERROR, "Incompatible @Adapt.");
        return null;
    }

    private ExecutableElement getFirstNonDefaultExecutable(TypeElement annotation) {
        for (ExecutableElement method : ElementFilter.methodsIn(annotation.getEnclosedElements())) {
            if (!method.isDefault()) {
                return method;
            }
        }
        processingEnv.getMessager().printMessage(Kind.ERROR,
                "Target interface should declare at least one non-default method.");
        return null;
    }

    private TypeElement getAnnotationValueAsTypeElement(ExecutableElement annotatedMethod, TypeElement annotation,
                                                        ExecutableElement annotationFunction) {
        TypeMirror annotationType = annotation.asType();

        for (AnnotationMirror annotationMirror : annotatedMethod.getAnnotationMirrors()) {
            if (processingEnv.getTypeUtils().isSameType(annotationMirror.getAnnotationType(), annotationType)) {
                AnnotationValue value = annotationMirror.getElementValues().get(annotationFunction);
                if (value == null) {
                    processingEnv.getMessager().printMessage(Kind.ERROR, "Unknown @Adapt target");
                    continue;
                }
                TypeMirror targetInterfaceTypeMirror = (TypeMirror) value.getValue();
                return (TypeElement) processingEnv.getTypeUtils().asElement(targetInterfaceTypeMirror);
            }
        }
        processingEnv.getMessager().printMessage(Kind.ERROR, "@Adapt should contain target()");
        return null;
    }

    private TypeElement getTopLevelEnclosingType(ExecutableElement annotatedMethod) {
        TypeElement enclosingType = null;
        Element enclosing = annotatedMethod.getEnclosingElement();

        while (enclosing != null) {
            if (enclosing.getKind() == ElementKind.CLASS) {
                enclosingType = (TypeElement) enclosing;
            } else if (enclosing.getKind() == ElementKind.PACKAGE) {
                break;
            }
            enclosing = enclosing.getEnclosingElement();
        }
        return enclosingType;
    }

    private String formatParameter(ExecutableElement method, boolean includeType) {
        StringBuilder builder = new StringBuilder();
        builder.append('(');
        String separator = "";

        for (VariableElement parameter : method.getParameters()) {
            builder.append(separator);
            if (includeType) {
                builder.append(parameter.asType());
                builder.append(' ');
            }
            builder.append(parameter.getSimpleName());
            separator = ", ";
        }
        builder.append(')');
        return builder.toString();
    }
}