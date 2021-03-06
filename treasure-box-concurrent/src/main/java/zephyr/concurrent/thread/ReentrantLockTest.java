package zephyr.concurrent.thread;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReentrantLockTest {

    public static void main(String[] args) {
        ReentrantLockTest reentrantLockTest = new ReentrantLockTest();
        reentrantLockTest.helloB();
    }

    synchronized void helloA() {
        log.info("helloA");
    }

    synchronized void helloB() {
        log.info("helloB");
        // 不会被阻塞 表示synchronized可重入
        helloA();
    }
}
