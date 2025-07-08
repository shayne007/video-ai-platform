package com.keensense.admin.constants;

import java.io.File;

public final class GlobalConstant {

    public static String ROOT_DIRECTORY = System.getenv("ROOT_DIR");
    static {
        if (ROOT_DIRECTORY == null)
        {
            ROOT_DIRECTORY = System.getProperty("user.dir");
        }
    }
    //public static final String ROOT_DIRECTORY = System.getProperty("user.dir");

    public static final String CLUSTER_DIRECTORY = ROOT_DIRECTORY + File.separator + "cluster";
    public static final String DATA_DIRECTORY = CLUSTER_DIRECTORY + File.separator + "data";
    public static final String DATA_FTP = ROOT_DIRECTORY + File.separator + "ftp";
}
