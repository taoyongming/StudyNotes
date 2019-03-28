##### 管道输入/输出流

管道输入/输出流和普通的文件输入/输出流或者网络输入/输出流不同之处在于，它主要
用于线程之间的数据传输，而传输的媒介为内存。
管道输入/输出流主要包括了如下4种具体实现：PipedOutputStream、PipedInputStream、
PipedReader和PipedWriter，前两种面向字节，而后两种面向字符。

```java
 public class Piped {
        public static void main(String[] args) throws Exception {
            PipedWriter out = new PipedWriter();
            PipedReader in = new PipedReader();
// 将输出流和输入流进行连接，否则在使用时会抛出IOException
            out.connect(in);
            Thread printThread = new Thread(new Print(in), "PrintThread");
            printThread.start();
            int receive = 0;
            try {
                while ((receive = System.in.read()) != -1) {
                    out.write(receive);
                }
            } finally {
                out.close();
            }
        }
        static class Print implements Runnable {
            private PipedReader in;
            public Print(PipedReader in) {
                this.in = in;
            }
            public void run() {
                int receive = 0;
                try {
                    while ((receive = in.read()) != -1) {
                        System.out.print((char) receive);
                    }
                } catch (IOException ex) {
                }
            }
        }
    }
```

运行该示例，输入一组字符串，可以看到被printThread进行了原样输出。
Repeat my words.
Repeat my words.
对于Piped类型的流，必须先要进行绑定，也就是调用connect()方法，如果没有将输入/输
出流绑定起来，对于该流的访问将会抛出异常。

##### Thread.join()的使用

