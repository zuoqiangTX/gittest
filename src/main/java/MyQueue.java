import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MyQueue {
    //1.承装元素
    private LinkedList<Object> list = new LinkedList<Object>();

    //2.计数器
    private AtomicInteger count = new AtomicInteger(0);

    //3.指定上限和下限
    private final int minSize = 0;
    private final int maxSize;

    public MyQueue(int Size) {
        this.maxSize = Size;
    }

    //4.初始化一个对象用于加锁
    private final Object lock = new Object();

    //put方法
    public void put(Object obj) {
        synchronized (lock) {
            while (count.get() == this.maxSize) {  //满了
                try {
                    lock.wait();   //阻塞到这里
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            list.add(obj);  //添加元素
            count.incrementAndGet();  //计数器递增
            System.out.println("加入了" + obj);
            lock.notifyAll(); //通知一下

        }

    }

    //take方法
    public Object take() {
        Object ret = null;
        synchronized (lock) {
            while (count.get() == this.minSize) {
                try {
                    lock.wait();  //容器为空，阻塞
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ret = list.removeFirst();
            count.decrementAndGet();
            lock.notify();
            //System.out.println("移除了" + ret);
        }
        return ret;

    }

    public int getSize() {
        return this.count.get();
    }

    public static void main(String[] args) {
        final MyQueue queue = new MyQueue(5);
        queue.put("a");
        queue.put("b");
        queue.put("c");
        queue.put("d");
        queue.put("e");
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                queue.put("f");
                queue.put("g");
            }
        }, "t1");
        t1.start();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread t2 = new Thread(new Runnable() {
            public void run() {
                Object o1 = queue.take();
                System.out.println("移除" + o1);
                Object o2 = queue.take();
                System.out.println("移除" + o2);
            }
        }, "t2");
        t2.start();


    }
}
