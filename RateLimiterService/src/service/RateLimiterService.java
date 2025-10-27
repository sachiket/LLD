package service;

public interface RateLimiterService {
    void registerUser(String userId, int capacity, int ratePerSec);
    boolean allowRequest(String userId);
}
