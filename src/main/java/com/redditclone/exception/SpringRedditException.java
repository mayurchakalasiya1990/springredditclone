package com.redditclone.exception;

public class SpringRedditException extends RuntimeException {
    public SpringRedditException(String s, Exception e) {
        super(s,e);
    }
    public SpringRedditException(String s) {
        super(s);
    }

}
