package com.epam.fedunkiv.periodicals.exceptions;

import java.util.NoSuchElementException;

public class NoSuchUserException extends NoSuchElementException {
    public NoSuchUserException() {
        super();
    }

    public NoSuchUserException(String s) {
        super(s);
    }
}
