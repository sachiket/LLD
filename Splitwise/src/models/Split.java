package models;

import java.util.UUID;

public class Split {
    private final String id = UUID.randomUUID().toString();
    private String userId;
    private int splitPercent;

    public Split(String userId, int splitPercent) {
        this.userId = userId;
        this.splitPercent = splitPercent;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getSplitPercent() {
        return splitPercent;
    }

    public void setSplitPercent(int splitPercent) {
        this.splitPercent = splitPercent;
    }
}
