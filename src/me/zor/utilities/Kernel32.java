package me.zor.utilities;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

import java.awt.*;

/* wrapper for kernel */
public interface Kernel32 extends StdCallLibrary {
    /* because interface */
    Kernel32 INSTANCE = (Kernel32) Native.loadLibrary("Kernel32", Kernel32.class);

    enum AllocationType {
        Commit(0x1000),
        Reserve(0x2000),
        Decommit(0x4000),
        Release(0x8000),
        Reset(0x80000),
        Physical(0x400000),
        TopDown(0x100000),
        WriteWatch(0x200000),
        LargePages(0x20000000);

        long value;

        AllocationType(long value)
        {
            this.value = value;
        }

        public long getValue()
        {
            return value;
        }
    }

    enum MemoryProtection {
        Execute(0x10),
        ExecuteRead(0x20),
        ExecuteReadWrite(0x40),
        ExecuteWriteCopy(0x80);

        long value;

        MemoryProtection(long value)
        {
            this.value = value;
        }

        public long getValue()
        {
            return value;
        }
    }

    /* acces information */
    int PROCESS_QUERY_INFORMATION = 0x0400;
    int PROCESS_ALL_ACCESS = 0x001F0FFF;
    int PROCESS_VM_READ = 0x0010;
    int PROCESS_VM_WRITE = 0x0020;
    int PROCESS_VM_OPERATION = 0x0008;

    /* to write to the process' memory */
    boolean WriteProcessMemory(Pointer p, Pointer address, Object buffer, int size, IntByReference written);

    /* to create thread */
    Pointer CreateRemoteThread(Pointer handle, Pointer lpThreadAttributes, int dwStackSize, Pointer lpStartAddress, Pointer lpParamater, int dwCreationFlags, int lpThreadId);

    /* to get the handle of kernel itself */
    Pointer GetModuleHandleA(String lpModuleName);

    /* to allocate memory in another process */
    Pointer VirtualAllocEx(Pointer dwHandle, Pointer address, int dwSize, long allocationType, long memoryProtection);

    /* get address of laodlibrary */
    Pointer GetProcAddress(Pointer hModule, String procName);

    /* to open process */
    Pointer OpenProcess(int dwDesiredAccess, boolean bInheritHandle, int dwProcessId);
    /* to close handle created by openprocess */
    boolean CloseHandle(Pointer hObject);
}
