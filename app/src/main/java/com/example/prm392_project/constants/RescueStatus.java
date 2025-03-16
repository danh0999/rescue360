package com.example.prm392_project.constants;

//public enum RescueStatusEnum
//{
//    New = 0,
//    Pending = 1,
//    Approved = 2,
//    InProgress = 3,
//    Completed = 4,
//    Cancelled = 5
//}

public enum RescueStatus {
    NEW(0),
    PENDING(1),
    APPROVED(2),
    IN_PROGRESS(3),
    COMPLETED(4),
    CANCELLED(5);

    private final int value;

    RescueStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
