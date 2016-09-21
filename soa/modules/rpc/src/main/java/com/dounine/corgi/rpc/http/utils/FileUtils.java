package com.dounine.corgi.rpc.http.utils;

import java.io.*;

/**
 * Created by huanghuanlai on 16/7/7.
 */
public class FileUtils {

    private static final int EOF = -1;

    public static byte[] readFileToByteArray(File file) throws IOException {
        InputStream in = null;
        try {
            in = openInputStream(file);
            return toByteArray(in, file.length());
        } finally {
            closeQuietly(in);
        }
    }

    public static byte[] readLengthBytes(RandomAccessFile randomAccessFile, long offset, int length) {
        byte[] bytes = new byte[length];
        try {
            randomAccessFile.seek(offset);
            randomAccessFile.read(bytes,0,length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static void writeFileByOutput(File file,OutputStream output){
        try {
            InputStream input = FileUtils.openInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(input);
            BufferedOutputStream bos = new BufferedOutputStream(output);
            byte[] b = new byte[1024];
            int readed;
            while((readed=bis.read(b))!=EOF){
                bos.write(b,0,readed);
            }
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyToFile(InputStream is,File file){
        FileOutputStream fos=null;
        try {
            fos = new FileOutputStream(file);
            int len = 0;
            byte[] b = new byte[2048];
            while((len=is.read(b))!=-1){
                fos.write(b,0,len);
            }
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null!=fos){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void writeStringToFile(String str,File file){
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(str.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeQuietly(InputStream is) {
        try {
            ((Closeable) is).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static byte[] toByteArray(InputStream input, long size) throws IOException {
        if (size > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Size cannot be greater than Integer max value: " + size);
        }
        return toByteArray(input, (int) size);
    }

    public static byte[] toByteArray(InputStream input, int size) throws IOException {
        if (size < 0) {
            throw new IllegalArgumentException("Size must be equal or greater than zero: " + size);
        }
        if (size == 0) {
            return new byte[0];
        }
        byte[] data = new byte[size];
        int offset = 0;
        int readed;
        while (offset < size && (readed = input.read(data, offset, size - offset)) != EOF) {
            offset += readed;
        }
        if (offset != size) {
            throw new IOException("Unexpected readed size. current: " + offset + ", excepted: " + size);
        }
        return data;
    }

    public static FileInputStream openInputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (file.canRead() == false) {
                throw new IOException("File '" + file + "' cannot be read");
            }
        } else {
            throw new FileNotFoundException("File '" + file + "' does not exist");
        }
        return new FileInputStream(file);
    }
}
