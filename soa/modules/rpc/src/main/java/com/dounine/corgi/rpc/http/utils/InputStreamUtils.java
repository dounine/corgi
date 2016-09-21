package com.dounine.corgi.rpc.http.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by huanghuanlai on 16/7/6.
 */
public final class InputStreamUtils {

    private InputStreamUtils(){}

    public static String istreamToString(InputStream is){
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuffer strs = new StringBuffer();
        char[] chars = new char[100];
        try {
            int readed;
            while((readed=br.read(chars))!=-1){
                strs.append(new String(chars,0,readed));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strs.toString();
    }
}
