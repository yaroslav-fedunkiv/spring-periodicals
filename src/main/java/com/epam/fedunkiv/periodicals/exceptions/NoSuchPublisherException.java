package com.epam.fedunkiv.periodicals.exceptions;

import java.util.NoSuchElementException;

public class NoSuchPublisherException extends NoSuchElementException {
    public NoSuchPublisherException() {
        super();
    }

    public NoSuchPublisherException(String s) {
        super(s);
    }
}
