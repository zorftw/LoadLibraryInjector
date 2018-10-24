package me.zor.utilities;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

/* not used */
public interface Psapi extends StdCallLibrary {
    Psapi INSTANCE = (Psapi) Native.loadLibrary("Psapi", Psapi.class);

    boolean EnumProcesses(int[] ProcessIDsOut, int size, int[] BytesReturned);
}