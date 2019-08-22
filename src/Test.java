// v9 版本
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class Test {
    public static void target(int i) { }

    static final MethodHandle mh;
    static {
        try {
            MethodHandles.Lookup l = MethodHandles.lookup();
            MethodType t = MethodType.methodType(void.class, int.class);
            mh = l.findStatic(Test.class, "target", t);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Throwable {
        long current = System.currentTimeMillis();
        for (int i = 1; i <= 2_000_000_000; i++) {
            if (i % 100_000_000 == 0) {
                long temp = System.currentTimeMillis();
                System.out.println(temp - current);
                current = temp;
            }

            mh.invokeExact(128);
        }
    }
}