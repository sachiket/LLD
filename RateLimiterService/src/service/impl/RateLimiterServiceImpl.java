package service.impl;

import model.TokenBucket;
import service.RateLimiterService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimiterServiceImpl implements RateLimiterService {
    private final Map<String, TokenBucket> userBuckets = new ConcurrentHashMap<>();

    @Override
    public void registerUser(String userId, int capacity, int ratePerSec) {
        userBuckets.put(userId, new TokenBucket(capacity, ratePerSec));
    }

    @Override
    public boolean allowRequest(String userId) {
        TokenBucket bucket = userBuckets.get(userId);
        if (bucket == null) {
            throw new IllegalArgumentException("User not registered: " + userId);
        }
        return bucket.allowRequest();
    }
}
