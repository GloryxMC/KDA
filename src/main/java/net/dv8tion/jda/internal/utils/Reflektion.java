package net.dv8tion.jda.internal.utils;

public class Reflektion {
    public static String getCallerCallerClassName() {
        String callerClassName = new Exception().getStackTrace()[1].getClassName();
        return new Exception().getStackTrace()[0].getClassName();
    }

    public static Class getCalleeClass() throws ClassNotFoundException {
        return Class.forName(new Exception().getStackTrace()[0].getClassName());
    }
}
