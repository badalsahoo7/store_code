package com.example.vivify_technocrats;

public class Score {
    private String userId;
    private int correctCount;
    private int wrongCount;

    // Constructor, getters, and setters

    public Score(String userId, int correctCount, int wrongCount) {
        this.userId = userId;
        this.correctCount = correctCount;
        this.wrongCount = wrongCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public void setCorrectCount(int correctCount) {
        this.correctCount = correctCount;
    }

    public int getWrongCount() {
        return wrongCount;
    }

    public void setWrongCount(int wrongCount) {
        this.wrongCount = wrongCount;
    }
}
