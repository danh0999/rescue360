package com.example.prm392_project.constants;

public enum AuthType {
    Email(0),
    Google(1),
    Firebase(2),
    Phone(3),
    Facebook(4);

    private final int value;
    AuthType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
