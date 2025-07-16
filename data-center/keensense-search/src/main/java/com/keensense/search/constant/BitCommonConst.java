package com.keensense.search.constant;

import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;

public class BitCommonConst {
    private BitCommonConst(){}
    protected static final Set<PosixFilePermission> FILE_PFP_SET = new HashSet<>(); //文件权限Set

    static {
        FILE_PFP_SET.add(PosixFilePermission.OTHERS_READ);
        FILE_PFP_SET.add(PosixFilePermission.OTHERS_EXECUTE);
        FILE_PFP_SET.add(PosixFilePermission.OWNER_EXECUTE);
        FILE_PFP_SET.add(PosixFilePermission.OWNER_READ);
        FILE_PFP_SET.add(PosixFilePermission.OWNER_WRITE);
        FILE_PFP_SET.add(PosixFilePermission.GROUP_READ);
        FILE_PFP_SET.add(PosixFilePermission.GROUP_EXECUTE);
    }

    public static Set<PosixFilePermission> getFilePfpSet() {
        return FILE_PFP_SET;
    }
}
