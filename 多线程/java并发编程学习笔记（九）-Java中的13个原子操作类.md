##### 原子更新基本类型类

使用原子的方式更新基本类型，Atomic包提供了以下3个类。
**·AtomicBoolean：原子更新布尔类型。**
**·AtomicInteger：原子更新整型。**
**·AtomicLong：原子更新长整型。**
以上3个类提供的方法几乎一模一样，所以本节仅以AtomicInteger为例进行讲解，
AtomicInteger的常用方法如下。
·int addAndGet（int delta）：以原子方式将输入的数值与实例中的值（AtomicInteger里的
value）相加，并返回结果。
·boolean compareAndSet（int expect，int update）：如果输入的数值等于预期值，则以原子方
式将该值设置为输入的值。
·int getAndIncrement()：以原子方式将当前值加1，注意，这里返回的是自增前的值。
·void lazySet（int newValue）：最终会设置成newValue，使用lazySet设置值后，可能导致其他
线程在之后的一小段时间内还是可以读到旧的值。

​	那么getAndIncrement是如何实现原子操作的呢？让我们一起分析其实现原理，
getAndIncrement的源码

```java
public final int getAndIncrement() {
    for (;;) {
        int current = get();
        int next = current + 1;
        if (compareAndSet(current, next))
            return current;
    }
} public final boolean compareAndSet(int expect, int update) {
    return unsafe.compareAndSwapInt(this, valueOffset, expect, update);
}
```

​	源码中for循环体的第一步先取得AtomicInteger里存储的数值，第二步对AtomicInteger的当
前数值进行加1操作，关键的第三步调用compareAndSet方法来进行原子更新操作，该方法先检
查当前数值是否等于current，等于意味着AtomicInteger的值没有被其他线程修改过，则将
AtomicInteger的当前数值更新成next的值，如果不等compareAndSet方法会返回false，程序会进
入for循环重新进行compareAndSet操作。

##### **原子更新数组**

通过原子的方式更新数组里的某个元素，Atomic包提供了以下4个类。
·AtomicIntegerArray：原子更新整型数组里的元素。
·AtomicLongArray：原子更新长整型数组里的元素。
·AtomicReferenceArray：原子更新引用类型数组里的元素。
·AtomicIntegerArray类主要是提供原子的方式更新数组里的整型，其常用方法如下。
·int addAndGet（int i，int delta）：以原子方式将输入值与数组中索引i的元素相加。
·boolean compareAndSet（int i，int expect，int update）：如果当前值等于预期值，则以原子
方式将数组位置i的元素设置成update值。

​	以上几个类提供的方法几乎一样，所以本节仅以AtomicIntegerArray为例进行讲解，
AtomicIntegerArray的使用实例代码

```java
    public class AtomicIntegerArrayTest {
        static int[] value = new int[] { 1， 2 };
    static AtomicIntegerArray ai = new AtomicIntegerArray(value);
    public static void main(String[] args) {
        ai.getAndSet(0， 3);
        System.out.println(ai.get(0));
        System.out.println(value[0]);
    }
}
```

​	需要注意的是，数组value通过构造方法传递进去，然后AtomicIntegerArray会将当前数组
复制一份，所以当AtomicIntegerArray对内部的数组元素进行修改时，不会影响传入的数组。

##### **原子更新引用类型**

​	原子更新基本类型的AtomicInteger，只能更新一个变量，如果要原子更新多个变量，就需
要使用这个原子更新引用类型提供的类。Atomic包提供了以下3个类。
·AtomicReference：原子更新引用类型。
·AtomicReferenceFieldUpdater：原子更新引用类型里的字段。
·AtomicMarkableReference：原子更新带有标记位的引用类型。可以原子更新一个布尔类
型的标记位和引用类型。构造方法是AtomicMarkableReference（V initialRef，boolean
initialMark）。
​	以上几个类提供的方法几乎一样，所以本节仅以AtomicReference为例进行讲解，
AtomicReference的使用示例代码

```java
public class AtomicReferenceTest {
    public static AtomicReference<user> atomicUserRef = new
            AtomicReference<user>();
    public static void main(String[] args) {
        User user = new User("conan"， 15);
        atomicUserRef.set(user);
        User updateUser = new User("Shinichi"， 17);
        atomicUserRef.compareAndSet(user， updateUser);
        System.out.println(atomicUserRef.get().getName());
        System.out.println(atomicUserRef.get().getOld());
    }
    static class User {
        private String name;
        private int old;
        public User(String name， int old) {
            this.name = name;
            this.old = old;
        }
        public String getName() {
            return name;
        }
        public int getOld() {
            return old;
        }
    }
}
```

##### 原子更新字段类

​	如果需原子地更新某个类里的某个字段时，就需要使用原子更新字段类，Atomic包提供
了以下3个类进行原子字段更新。
·**AtomicIntegerFieldUpdater**：原子更新整型的字段的更新器。
·**AtomicLongFieldUpdater**：原子更新长整型字段的更新器。
·**AtomicStampedReference**：原子更新带有版本号的引用类型。该类将整数值与引用关联起
来，可用于原子的更新数据和数据的版本号，可以解决使用CAS进行原子更新时可能出现的
ABA问题。
​	要想原子地更新字段类需要两步。

第一步，因为原子更新字段类都是抽象类，每次使用的时候必须使用静态方法newUpdater()创建一个更新器，并且需要设置想要更新的类和属性。

第二步，更新类的字段（属性）必须使用public volatile修饰符。以上3个类提供的方法几乎一样，所以本节仅以AstomicIntegerFieldUpdater为例进行讲解，AstomicIntegerFieldUpdater的示例代码

```java
  public class AtomicIntegerFieldUpdaterTest {
        // 创建原子更新器，并设置需要更新的对象类和对象的属性
        private static AtomicIntegerFieldUpdater<User> a = AtomicIntegerFieldUpdater.
                newUpdater(User.class， "old");
        public static void main(String[] args) {
// 设置柯南的年龄是10岁
            User conan = new User("conan"， 10);
// 柯南长了一岁，但是仍然会输出旧的年龄
            System.out.println(a.getAndIncrement(conan));
// 输出柯南现在的年龄
            System.out.println(a.get(conan));
        }
        public static class User {
            private String name;
            public volatile int old;
            public User(String name， int old) {
                this.name = name;
                this.old = old;
            }
            public String getName() {
                return name;
            }
            public int getOld() {
                return old;
            }
        }
    }
```