package com.dounine.corgi.sso.utils;

/**
 * Created by huanghuanlai on 16/8/14.
 */
public class IpUtils {

    public static long ipToLong(String sip) {
        long[] ip = new long[4];
        int[] pos = new int[3];
        pos[0] = sip.indexOf(".");
        ip[0] = Long.parseLong(sip.substring(0, pos[0]));
        for (int i = 1; i < 3; i++) {
            pos[i] = sip.indexOf(".", pos[i - 1] + 1);
            ip[i] = Long.parseLong(sip.substring(pos[i - 1] + 1, pos[i]));
            if (i == 2) {
                ip[i + 1] = Long.parseLong(sip.substring(pos[i] + 1));
            }
        }
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    public static String longToIp(long longIp) {
        StringBuffer sb = new StringBuffer();
        sb.append(String.valueOf(longIp >>> 24));
        sb.append(".");
        sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
        sb.append(".");
        sb.append(String.valueOf((longIp&0x0000FFFF)>>>8));
        sb.append(".");
        sb.append(String.valueOf(longIp&0x000000FF));
        return sb.toString();
    }

    public static void main(String args[]) {
        System.out.println(ipToLong("192.168.0.1"));
        System.out.println(ipToLong("192.168.0.11"));
        System.out.println(ipToLong("192.168.0.111"));
        System.out.println(longToIp(3232235631l));
    }
}
