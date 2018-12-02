package demo2.demo1Modify;

import java.util.concurrent.CountDownLatch;

public class factorsThread implements Runnable {
    private CountDownLatch latch;

    public factorsThread(CountDownLatch latch) {
        this.latch = latch; //初始化闭锁
    }


    demo2.demo1Modify.factorsService factorsService;

    public void setFactorsService(factorsService factorsService) {
        this.factorsService = factorsService;
    }


    Long num = 0L;

    public void setNum(Long num) {
        this.num = num;
    }


    @Override
    public void run() {
        factorsService.Service(num);
        latch.countDown();  //执行完run后减1
    }
}
