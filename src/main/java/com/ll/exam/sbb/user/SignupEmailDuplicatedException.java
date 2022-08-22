package com.ll.exam.sbb.user;

public class SignupEmailDuplicatedException extends RuntimeException {
    public SignupEmailDuplicatedException(String msg) {
        super(msg);
    }
}
