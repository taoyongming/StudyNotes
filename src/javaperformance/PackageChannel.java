package javaperformance;


import java.util.Arrays;

// 流水线
public class PackageChannel {
    private final static int MAX_PACKAGE_NUM = 100;

    private final Package[] packageQueue;
    private final Worker[] workerPool;
    private int head;
    private int tail;
    private int count;

    public PackageChannel(int workers) {
        this.packageQueue = new Package[MAX_PACKAGE_NUM];
        this.head = 0;
        this.tail = 0;
        this.count = 0;
        this.workerPool = new Worker[workers];
        this.init();
    }

    private void init() {
        for (int i = 0; i < workerPool.length; i++) {
            workerPool[i] = new Worker("Worker-" + i, this);
        }
    }

    /**
     * push switch to start all of worker to work
     */
    public void startWorker() {
        Arrays.asList(workerPool).forEach(Worker::start);
    }

    public synchronized void put(Package packagereq) {
        while (count >= packageQueue.length) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.packageQueue[tail] = packagereq;
        this.tail = (tail + 1) % packageQueue.length;
        this.count++;
        this.notifyAll();
    }

    public synchronized Package take() {
        while (count <= 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Package request = this.packageQueue[head];
        this.head = (this.head + 1) % this.packageQueue.length;
        this.count--;
        this.notifyAll();
        return request;
    }

}
