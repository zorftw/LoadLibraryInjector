package me.zor.exceptions;

import sun.reflect.annotation.ExceptionProxy;

public class OpenProcessException extends Exception {
    public OpenProcessException(int pid) {
        super("Unable to open process with PID: " + pid);
    }
}
