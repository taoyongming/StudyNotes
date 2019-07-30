package javaperformance;

import java.util.stream.IntStream;

public class ContextTest {

    // 上下文类
    public class Context {
        private String name;
        private long id;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    // 设置上下文名字
    public class QueryNameAction {
        public void execute(Context context) {
            try {
                Thread.sleep(1000L);
                String name = Thread.currentThread().getName();
                context.setName(name);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // 设置上下文 ID
    public class QueryIdAction {
        public void execute(Context context) {
            try {
                Thread.sleep(1000L);
                long id = Thread.currentThread().getId();
                context.setId(id);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // 执行方法
    public class ExecutionTask implements Runnable {

        private QueryNameAction queryNameAction = new QueryNameAction();
        private QueryIdAction queryIdAction = new QueryIdAction();

        @Override
        public void run() {
            final Context context = new Context();
            queryNameAction.execute(context);
            System.out.println("The name query successful");
            queryIdAction.execute(context);
            System.out.println("The id query successful");

            System.out.println("The Name is " + context.getName() + " and id " + context.getId());
        }
    }

    public static void main(String[] args) {
        IntStream.range(1, 5).forEach(i -> new Thread(new ContextTest().new ExecutionTask()).start());
    }
}
