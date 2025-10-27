# In-Memory Token Bucket Rate Limiter

## ✅ Objective

Design and implement a **Token Bucket Rate Limiter** (in-memory version) that:
- Controls how many requests a user can make
- Supports burst traffic up to a defined limit
- Refills tokens at a constant rate
- Is thread-safe and easily extensible

---

## 🧠 Core Concepts

### Token Bucket Algorithm

Each user has a **bucket** with:
- `capacity`: max tokens it can hold
- `refillRate`: tokens added per second
- `tokens`: current available tokens
- `lastRefillTime`: when it was last updated

When a request arrives:
- Refill the bucket based on time elapsed since `lastRefillTime`
- If enough tokens exist → allow request and deduct tokens
- If not → deny request

---

## 🛠️ Implementation Details

#### 1. `TokenBucket`

```java
public class TokenBucket {
    private final int capacity;
    private final double refillRatePerSecond;
    private double tokens;
    private long lastRefillTime;

    public TokenBucket(int capacity, double refillRatePerSecond) {
        this.capacity = capacity;
        this.refillRatePerSecond = refillRatePerSecond;
        this.tokens = capacity;
        this.lastRefillTime = System.nanoTime();
    }

    public synchronized boolean allowRequest() {
        refill();
        if (tokens >= 1) {
            tokens -= 1;
            return true;
        }
        return false;
    }

    private void refill() {
        long now = System.nanoTime();
        double secondsElapsed = (now - lastRefillTime) / 1_000_000_000.0;
        double tokensToAdd = secondsElapsed * refillRatePerSecond;
        if (tokensToAdd > 0) {
            tokens = Math.min(capacity, tokens + tokensToAdd);
            lastRefillTime = now;
        }
    }
}
```
## ✅ Design Summary

### Components

1. **TokenBucket**
    - Encapsulates the rate-limiting logic for a single user
    - Manages refill and consumption of tokens

2. **TokenBucketManager**
    - Maintains a map of userId → TokenBucket
    - Registers users and routes requests to their respective buckets

3. **User**
    - Represents a system user with `id`, `name`, and `role`

4. **Entry Point / Handler**
    - Accepts requests and interacts with `TokenBucketManager`
    - Ensures proper user validation and rate limit enforcement

---

## 💡 Implementation Tips

- Use **floating-point tokens** to support partial refill rates (e.g., 0.5 tokens/sec).
- Use `System.nanoTime()` for **precise time calculation** between requests.
- Make the `allowRequest()` method **synchronized** to ensure thread safety.
- Avoid timers; **refill tokens on-demand** based on elapsed time.
- Use **ConcurrentHashMap** to safely manage TokenBuckets across threads.
- Add request logging for debugging and validation purposes.

---

## 🧪 Example Behavior

- User has a limit of 5 requests/minute with a burst capacity of 5.
- Sends 5 rapid requests → all allowed.
- 6th request → denied.
- After 60 seconds → bucket refilled → requests allowed again.

---

## ❓ Interview-Style Follow-Up Questions

### Q1: What are the advantages of the Token Bucket algorithm?
- Allows **bursty traffic**
- Easy to implement
- Refill is **lazy**, no background thread needed

### Q2: Why not use the Leaky Bucket algorithm?
- Leaky Bucket enforces a **strict request rate**, no bursts.
- Token Bucket is more appropriate for APIs that can tolerate burst traffic.

### Q3: What happens when the system restarts?
- All in-memory data is lost.
- Users will start with full tokens again (fresh state).
- Not suitable for production — hence Redis is needed for persistence.

### Q4: How can we support different user tiers (e.g., free vs premium)?
- At user registration, set different `capacity` and `refillRate` based on `role`.

### Q5: Is the current implementation thread-safe?
- Yes, via `synchronized` block inside the token-consuming method.
- This avoids race conditions for concurrent requests from the same user.

### Q6: How would you test this logic?
- Inject a **mock clock** to control time deterministically.
- Use unit tests to simulate different time intervals and request patterns.

### Q7: What would break if you removed the `refill()` method?
- Tokens would not replenish over time → rate limiter becomes a hard cap.

---

## 🧭 Next Steps

To make this rate limiter production-ready, we need:
- **Distributed state** → support across multiple instances
- **Persistence** → so limits survive restarts
- **Atomicity** → safe updates without race conditions