package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadSyncSimple {
    private static final Logger logger = LoggerFactory.getLogger(ThreadSyncSimple.class);
    private int queueActionThread = 0;

    private synchronized void action(int threadNumber, int thread_count) {
        int count = 1;
        boolean isInc = true;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (count == 1) isInc = true;
                if (count == 10) isInc = false;

                // если не его очередь то ждем
                while (queueActionThread != threadNumber) {
                    this.wait();
                }

                logger.info(String.valueOf(count));

                count = isInc ? count + 1 : count - 1;
                queueActionThread = queueActionThread == thread_count - 1 ? 0 : queueActionThread + 1;
                sleep();
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        final int THREAD_COUNT = 2;
        ThreadSyncSimple threadSyncSimple = new ThreadSyncSimple();
        for (int i = 0; i < THREAD_COUNT; i++) {
            int threadNumber = i;
            new Thread(() -> threadSyncSimple.action(threadNumber, THREAD_COUNT)).start();
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
