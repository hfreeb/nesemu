package com.harryfreeborough.nesemu.exceptions;

public class BusException extends RuntimeException {

    public BusException(String s) {
        super(s);
    }

    public BusException(String format, Object... args) {
        super(String.format(format, args));
    }

    public BusException(String s, Throwable throwable) {
        super(s, throwable);
    }

}
