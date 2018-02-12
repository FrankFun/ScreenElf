package com.xiangzi.screenelf.elf;

/**
 * ElfUtil
 *
 * @author wuyongxiang
 *         2018/2/5
 */
public class ElfUtil {

    public static float getV(float x) {
        return (float) (Math.sqrt(ElfView.k / ElfView.m) * x * x);
    }
}
