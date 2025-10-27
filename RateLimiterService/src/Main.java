import service.RateLimiterService;
import service.impl.RateLimiterServiceImpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        RateLimiterService rateLimiter = new RateLimiterServiceImpl();

        rateLimiter.registerUser("userA", 5, 2); // 2 req/sec, burst up to 5
        rateLimiter.registerUser("userB", 10, 5); // 5 req/sec, burst up to 10

        ExecutorService executor = Executors.newFixedThreadPool(5);
        Runnable task = () -> {
            String user = ThreadLocalRandom.current().nextBoolean() ? "userA" : "userB";
            boolean allowed = rateLimiter.allowRequest(user);
            System.out.println("[" + Thread.currentThread().getName() + "] " +
                    "Request for " + user + ": " + (allowed ? "ALLOWED" : "BLOCKED"));
        };

        for (int i = 0; i < 20; i++) {
            executor.submit(task);
            Thread.sleep(200);  // 0.2s between requests
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }
}
