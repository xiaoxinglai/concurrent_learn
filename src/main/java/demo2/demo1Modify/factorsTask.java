package demo2.demo1Modify;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class factorsTask implements Callable<List<Long>> {
    private Long i = 0L;

    public factorsTask(Long i) {
        this.i = i;
    }

    @Override
    public List<Long> call() throws Exception {
        List<Long> factors = new ArrayList<Long>();
        Long j = 2L;
        while (j <= i) {
            if (i % j == 0) {
                i = i / j;
                factors.add(j);
            } else {
                j++;
            }
        }
        return  factors;
    }
}

