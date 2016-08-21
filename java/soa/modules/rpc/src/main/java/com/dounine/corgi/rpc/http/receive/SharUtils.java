package com.dounine.corgi.rpc.http.receive;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huanghuanlai on 16/6/30.
 */
public final class SharUtils {

    private SharUtils() {
    }

    private static final List<SharSize> SHAR_SIZES = new ArrayList<>(5);
    private static final long ONE_M = 1024 * 1024;//1M

    static {
        SHAR_SIZES.add(new SharSize(ONE_M * 1, ONE_M * 100, (int) (ONE_M * 1)));//1~20=1
        SHAR_SIZES.add(new SharSize(ONE_M * 100, ONE_M * 1024, (int) (ONE_M * 2)));//100~1024=2
        SHAR_SIZES.add(new SharSize(ONE_M * 1024, ONE_M * 1024*20, (int) (ONE_M * 3)));//1024~1024*20=4
    }

    /**
     * 计算文件要平均分片大小
     *
     * @param size
     * @return
     */
    public static int calculateAvgShar(long size) {
        int shar = 0;
        if (size > ONE_M * 1024*20) {//不允许文件上传超过20g
            return -1;
        }
        for (SharSize ss : SHAR_SIZES) {
            if (size > ss.getMinSize() && size <= ss.getMaxSize()) {
                shar = ss.getShar();
                break;
            }
        }
        if(shar==0){
            shar = (int) size;
        }
        return shar;
    }
}
