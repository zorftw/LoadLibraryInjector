package me.zor.loadlibrary;

public class Main {

    private static final double CURRENT_VERSION = 0.1D;
    private static void print_cool_message()
    {
        System.out.println(" _____      _           _             ");
        System.out.println("|_   _|    (_)         | |            ");
        System.out.println("  | | _ __  _  ___  ___| |_ ___  _ __ ");
        System.out.println("  | || '_ \\| |/ _ \\/ __| __/ _ \\| '__|");
        System.out.println(" _| || | | | |  __/ (__| || (_) | |   ");
        System.out.println(" \\___/_| |_| |\\___|\\___|\\__\\___/|_|   ");
        System.out.println("          _/ |                        ");
        System.out.println("         |__/                         ");
        System.out.println("        [ Using Version " + CURRENT_VERSION + " ]"); //spacing lol
    }

    /* entry point for our program */
    public static void main(String[] args) throws Exception {

        print_cool_message();

        Injector injectorInstance = new Injector();
        injectorInstance.run();
    }

}
