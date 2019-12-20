package geekTime.JVM;

/**
 * ${DESCRIPTION}
 *
 * @author tym
 * @ceeate 2019/9/11
 **/
import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface Adapt {
    Class<?> value();
}