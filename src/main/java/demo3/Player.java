package demo3;

import java.util.concurrent.CountDownLatch;

public class Player extends Thread {

    private static int count = 1;
    private final int id = count++;
    private CountDownLatch latch;

    public Player(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        System.out.println("【玩家" + id + "】已入场");
        latch.countDown();//计数器减1
    }

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3); //初始化计数器
        System.out.println("牌局开始, 等待玩家入场...");
        new Player(latch).start();
        new Player(latch).start();
        new Player(latch).start();
        latch.await();//阻塞等待计数器为0
        System.out.println("玩家已到齐, 开始发牌...");
    }

}