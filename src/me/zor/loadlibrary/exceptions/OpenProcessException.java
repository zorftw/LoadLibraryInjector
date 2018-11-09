package me.zor.loadlibrary.exceptions;

public class OpenProcessException extends Exception {
    public OpenProcessException(int pid) {
        super("Unable to open process with PID: " + pid);
    }
}
