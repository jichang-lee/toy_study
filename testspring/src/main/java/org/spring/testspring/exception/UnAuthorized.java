package org.spring.testspring.exception;

public class UnAuthorized extends MasterException{

    private static final String MESSAGE = "인증이 필요합니다.";

    public UnAuthorized() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
