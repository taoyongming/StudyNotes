package javaperformance;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockingQueueTest {

    private int maxInventory = 10; // 最大库存

    private BlockingQueue<String> product = new LinkedBlockingQueue<>(maxInventory);// 缓存队列

    /**
     * 新增商品库存
     * @param e
     */
    public void produce(String e) {
        try {
            product.put(e);
            System.out.println(" 放入一个商品库存，总库存为：" + product.size());
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    /**
     * 消费商品
     * @return
     */
    public String consume() {
        String result = null;
        try {
            result = product.take();
            System.out.println(" 消费一个商品，总库存为：" + product.size());
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 生产者
     * @author admin
     *
     */
    private class Producer implements Runnable {

        public void run() {
            for (int i = 0; i < 20; i++) {
                produce(" 商品 " + i);
            }
        }

    }

    /**
     * 消费者
     * @author admin
     *
     */
    private class Customer implements Runnable {

        public void run() {
            for (int i = 0; i < 20; i++) {
                consume();
            }
        }
    }

    public static void main(String[] args) {

        BlockingQueueTest lc = new BlockingQueueTest();
        new Thread(lc.new Producer()).start();
        new Thread(lc.new Customer()).start();
        new Thread(lc.new Producer()).start();
        new Thread(lc.new Customer()).start();

    }
}
