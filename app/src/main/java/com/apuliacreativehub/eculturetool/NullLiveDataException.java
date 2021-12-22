package com.apuliacreativehub.eculturetool;

public class NullLiveDataException extends Exception {
    private final static String ERROR_MESSAGE = "The LiveData object that you are requiring is null.";

    public NullLiveDataException() {
        super(ERROR_MESSAGE);
    }
}
