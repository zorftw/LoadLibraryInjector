package me.zor.loadlibrary.exceptions;

public class ProcAddressException extends Exception {

    public ProcAddressException(String module, String proc)
    {
        super("Exception trying to get access to " + proc + " in " + module);
    }

}
