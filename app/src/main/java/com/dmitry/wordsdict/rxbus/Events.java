package com.dmitry.wordsdict.rxbus;

import android.text.format.DateFormat;

public class Events {
    private Events(){}

    public static class Message {
        private String message;
        public Message(String message) {
            this.message = DateFormat.format("MM/dd/yy h:mm:ss aa", System.currentTimeMillis()) + ": " + message;
        }
        public String getMessage() {
            return message;
        }
    }
}