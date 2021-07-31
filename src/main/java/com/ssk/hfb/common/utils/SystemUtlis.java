package com.ssk.hfb.common.utils;

public final class SystemUtlis {
    public static boolean isSystemLinux() {
        String osName = System.getProperties().getProperty("os.name");
        if(osName.equals("Linux"))
        {
            System.out.println("running in Linux");
            return true;
        }
        else
        {
            System.out.println("don't running in Linux");
            return false;
        }
    }


}

