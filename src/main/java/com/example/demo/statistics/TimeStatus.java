package com.example.demo.statistics;

public enum TimeStatus {
    ONE_MIN_TO_CURRENT("현재"),
    SECOND_MIN_TO_ONE_MIN("1분전"),
    THIRD_MIN_TO_SECOND_MIN("2분전"),
    FOURTH_MIN_TO_THIRD_MIN("3분전");
    private String timeMessage;
    TimeStatus(String timeMessage) {
        this.timeMessage = timeMessage;
    }

    public String getTimeMessage() {
        return timeMessage;
    }
}
