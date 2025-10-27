package model;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents a token bucket for a single user or client.
 * Controls how many requests can be made per second, with a burst capacity.
 */
public class TokenBucket {
    private final int capacity;                 // Max number of tokens (bucket size)
    private final int refillRatePerSecond;      // How many tokens to add per second
    private double tokens;                      // Current number of tokens available
    private long lastRefillTime;                // Last time we refilled the bucket
    private final ReentrantLock lock;           // Lock to ensure thread safety

    /**
     * Constructor to initialize the token bucket.
     * @param capacity Max burst size (max tokens that can be accumulated)
     * @param refillRatePerSecond Tokens added per second (request rate)
     */
    public TokenBucket(int capacity, int refillRatePerSecond) {
        this.capacity = capacity;
        this.refillRatePerSecond = refillRatePerSecond;
        this.tokens = capacity;  // Start full
        this.lastRefillTime = System.nanoTime();  // Track in nanoseconds for precision
        this.lock = new ReentrantLock();
    }

    /**
     * Attempt to consume 1 token.
     * @return true if allowed, false if rate-limited
     */
    public boolean allowRequest() {
        lock.lock();
        try {
            refill();  // Refill tokens based on time passed
            if (tokens >= 1) {
                tokens -= 1;  // Consume 1 token
                return true;
            } else {
                return false;  // No tokens = request denied
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Refill the bucket with new tokens based on elapsed time.
     * Keeps total tokens <= capacity.
     */
    private void refill() {
        long now = System.nanoTime();
        double secondsElapsed = (now - lastRefillTime) / 1_000_000_000.0;

        // Tokens to add = rate * time passed
        double tokensToAdd = secondsElapsed * refillRatePerSecond;

        if (tokensToAdd > 0) {
            tokens = Math.min(capacity, tokens + tokensToAdd);
            lastRefillTime = now;
        }
    }
}

