package me.zor.loadlibrary;

import com.sun.jna.Pointer;
import me.zor.loadlibrary.exceptions.OpenProcessException;
import me.zor.loadlibrary.exceptions.ProcAddressException;
import me.zor.loadlibrary.exceptions.ProcessWriteException;
import me.zor.loadlibrary.utilities.Kernel32;

import java.util.Scanner;

public class Injector {

    public int selectedPid = -1;

    void WaitForSelect(boolean list)
    {
        if( list ) {
            Process taskProcess = null;

            /* list all processes */
            try {
                ProcessBuilder pb = new ProcessBuilder("tasklist", "/FI", "sessionname ne services");
                pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
                pb.redirectError(ProcessBuilder.Redirect.INHERIT);
                taskProcess = pb.start();
            } catch (Exception e) {
                e.printStackTrace();
            }

            while (taskProcess.isAlive()) { }
        }

        /* wait for pid input */
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("");
            System.out.print("[*] Please enter a valid PID ... ");
            selectedPid = scanner.nextInt();
        } catch (Exception e) {e.printStackTrace();}
    }

    void run() throws Exception {
        boolean list = false;
        System.out.print("[*] Would you like to have all processes listed? [Y/N] ... ");
        Scanner scanner = new Scanner(System.in);
        try {
            String message = scanner.next();
            if(message.toLowerCase().equalsIgnoreCase("Y"))
            {
             list = true;
            } else if(message.toLowerCase().equalsIgnoreCase("N"))
            {
                list = false;
            } else {
                System.out.println("[!] Error reading input...");
                Thread.sleep(2500);
                System.exit(-1);
            }
        } catch (Exception e) {e.printStackTrace();}

        synchronized (this) {
                WaitForSelect(list);
        }

        if(this.selectedPid != -1)
        {
            /* open handle to process */
            Pointer HANDLE = Kernel32.INSTANCE.OpenProcess(Kernel32.PROCESS_ALL_ACCESS, false, this.selectedPid);
            if(HANDLE == Pointer.NULL)
            {
                throw new OpenProcessException(this.selectedPid);
            }

            Scanner pathScanner = new Scanner(System.in);
            String path=null;
            try {
                System.out.println("[*] Please enter the path of the DLL:");
                System.out.print("    "); //some spacing
                path = pathScanner.next();
            } catch (Exception e) {e.printStackTrace();}

            if(path == null || path.length() < 1 || path.isEmpty())
            {
                System.out.println("[!] Error reading input...");
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {e.printStackTrace(); }
                System.exit(-1);
            }

            /* get address of loadlibrary */
            Pointer kernel = Kernel32.INSTANCE.GetModuleHandleA("kernel32.dll");
            if(kernel == Pointer.NULL)
            {
                throw new Exception("Cannot find handle to Kernel32");
            }
            Pointer m_loadlibrary = Kernel32.INSTANCE.GetProcAddress(kernel, "LoadLibraryA");
            /* allocate memory for our path to be written to */
            Pointer m_write = Kernel32.INSTANCE.VirtualAllocEx(HANDLE, null, path.length() + 1, Kernel32.AllocationType.Reserve.getValue() | Kernel32.AllocationType.Commit.getValue(), Kernel32.MemoryProtection.ExecuteReadWrite.getValue());

            if( m_loadlibrary == Pointer.NULL )
            {
                throw new ProcAddressException("kernel32.dll", "LoadLibraryA");
            }

            if( m_write == Pointer.NULL )
            {
                throw new ProcessWriteException(this.selectedPid);
            }

            /* write path to memory */
            boolean b_write = Kernel32.INSTANCE.WriteProcessMemory(HANDLE, m_write, path, path.length() + 1, null);

            if(!b_write)
            {
                throw new ProcessWriteException(this.selectedPid);
            }

            /* create remote thread */
            Kernel32.INSTANCE.CreateRemoteThread(HANDLE, null, 0, m_loadlibrary, m_write, 0, 0);

            /* after we're done close the handle */
            Kernel32.INSTANCE.CloseHandle(HANDLE);
        }
        else
        {
            System.out.println("[!] Something went wrong selecting the process.");
            try {
                Thread.sleep(2500);
            } catch (Exception e) {e.printStackTrace();}
            System.exit(-1);
        }

    }

}
