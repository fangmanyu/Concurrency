package xin.stxkfzx.concurrency.test.atomic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xin.stxkfzx.concurrency.annotations.ThreadSafe;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 并发测试<br/>
 * 使用jdk提供的AtomicInteger保证自增操作的原子性
 *
 * @author fmy
 * @date 2019-01-19 11:36
 */
@ThreadSafe
public class AtomicBooleanTest {
    private static final Logger log = LoggerFactory.getLogger(AtomicBooleanTest.class);

    /** 请求总数 */
    public static int clientTotal = 5000;
    /** 同时执行的并发数 */
    public static int threadTotal = 200;
    public static AtomicInteger count = new AtomicInteger(0);
    private static AtomicBoolean isHappened = new AtomicBoolean(false);

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        // 信号量，用于阻塞线程，控制同一时间的并发量
        final Semaphore semaphore = new Semaphore(threadTotal);
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        for (int i = 0; i < clientTotal; i++) {
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    test();
                    semaphore.release();
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
                // countDownLatch线程执行计数器减1
                countDownLatch.countDown();
            });
        }

        // countDownLatch.await() 阻塞线程，直到CountDownLatch内部线程执行计数器到0
        countDownLatch.await();
        executorService.shutdown();
        log.info("count: {}", count.get());
    }

    public static void test() {
        // 确保 false -> true 过程不会被其他线程所修改
        if (isHappened.compareAndSet(false, true)) {
            count.getAndIncrement();
        }
    }
}
