package com.krats.onlinebookstore.bean;

import java.util.Random;

public class IsbnGenerator implements NumberGenerator {
    public String generateNumber() {
        return "13-12312-" + Math.abs(new Random().nextInt());
    }
}
