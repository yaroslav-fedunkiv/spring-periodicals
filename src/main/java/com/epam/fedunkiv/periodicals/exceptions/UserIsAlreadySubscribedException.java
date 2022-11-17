package com.epam.fedunkiv.periodicals.exceptions;

public class UserIsAlreadySubscribedException extends RuntimeException{
    public UserIsAlreadySubscribedException() {
        super();
    }

    public UserIsAlreadySubscribedException(String message) {
        super(message);
    }

    public UserIsAlreadySubscribedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserIsAlreadySubscribedException(Throwable cause) {
        super(cause);
    }
}
