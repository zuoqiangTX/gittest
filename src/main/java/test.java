public class test extends Thread{
    private volatile boolean isStarting=true;

    public void setStarting(boolean starting) {
        isStarting = starting;
    }

    @Override
    public void run() {
        System.out.println("进入线程");
        while(isStarting){

        }
        System.out.println("退出线程");

    }

    public static void main(String[] args) throws InterruptedException {
        test test=new test();
        test.start();
        Thread.sleep(3000);
        test.setStarting(false);

    }
}
