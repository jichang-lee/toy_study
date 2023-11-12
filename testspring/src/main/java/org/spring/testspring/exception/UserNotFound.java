package org.spring.testspring.exception;

import java.util.function.Supplier;

public class UserNotFound extends MasterException {

    private static final String MESSAGE = "존재하지 않는 게시글 입니다.";

    public UserNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
