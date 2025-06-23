package com.korit.authstudy.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
public class MyAccountException extends RuntimeException {
    private Map<String, String> errerMap;

    public MyAccountException(String message) {
        super(message);
        this.errerMap = Map.of("message", message);
    }
}
//    private ErrorMessage errorMessage;
//
//    public MyAccountException(String message) {
//        super(message);
//        this.errorMessage = new ErrorMessage(message);
//    }
//
//    @Data
//    @AllArgsConstructor
//    class ErrorMessage {
//        private String message;
//    }

