package org.spring.testspring.exception;

public class PostNotFound extends MasterException{

    private static final String MESSAGE = "존재하지 않는 게시글 입니다.";

    public PostNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
