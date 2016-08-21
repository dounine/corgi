package com.dounine.corgi.rpc.http.sender;

import com.alibaba.fastjson.JSON;
import com.dounine.corgi.rpc.http.Coordinate;
import com.dounine.corgi.rpc.http.FlashLight;
import com.dounine.corgi.rpc.http.IFlashLight;
import com.dounine.corgi.rpc.http.medias.IMedia;
import com.dounine.corgi.rpc.http.medias.Media;
import com.dounine.corgi.rpc.http.receive.FileInfo;
import com.dounine.corgi.rpc.http.rep.ResponseText;
import com.dounine.corgi.rpc.http.types.HeartType;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by huanghuanlai on 16/7/6.
 */
public class StorageClient {

    public static InputStream readStartToLength(File file, long start, int length, long size) {
        byte[] bytes = new byte[length];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            fis.skip(start);
            fis.read(bytes, 0, length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new ByteArrayInputStream(bytes);
    }

    public static byte[] readStartToBytes(File file, long start, int length, long size) {
        byte[] bytes = new byte[length];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            fis.skip(start);
            fis.read(bytes, 0, length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bytes;
    }

    private static void upload(File file) {
        if (!file.exists()) {
            throw new RuntimeException(file.getName() + "文件不存在");
        }
        String fileInfoJSON = getToken(file);
        if (null != fileInfoJSON && fileInfoJSON.length() > 0) {
            long begin = System.currentTimeMillis();
            ResponseText result = JSON.parseObject(fileInfoJSON, ResponseText.class);
            FileInfo fileInfo = JSON.parseObject(result.getData().toString(), FileInfo.class);

            final int nThreads = Runtime.getRuntime().availableProcessors();
            ExecutorService es = Executors.newFixedThreadPool(nThreads);

            final long len = fileInfo.getMaxShar(), size = file.length();
            List<Future<String>> futures = new ArrayList<>((int) len);
            final String fileName = file.getName();
            for (long i = 0; i < len; i++) {
                final long start = i * fileInfo.getAvgShar();
                final long shar = i + 1;
                futures.add(es.submit(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        //InputStream is = readStartToLength(file, start, fileInfo.getAvgShar(), size);
                        //return post(fileInfo.getToken(), shar, is, fileName);
                        byte[] bytes = readStartToBytes(file, start, fileInfo.getAvgShar(), size);
                        return urlConnectionUpload(fileInfo.getToken(), shar, bytes, fileName);
                    }
                }));
            }
            es.shutdown();
            while (true) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (es.isTerminated()) {//判断线程是否已经终止
                    System.out.println("占用时间:" + (System.currentTimeMillis() - begin));
                    break;
                }
            }
        }
    }

    public static String getToken(File file) {
        IFlashLight flashLightToken = new FlashLight(new Coordinate("http://localhost:8081/file/token"));
        flashLightToken.on();
        List<IMedia> tokenMedias = new ArrayList<>();
        tokenMedias.add(new Media("name", file.getName()));
        tokenMedias.add(new Media("size", file.length()));
        flashLightToken.ready(tokenMedias);
        try {
            flashLightToken.emit(HeartType.POST);
        } catch (ConnectException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        String responseText = flashLightToken.response().toString();
        flashLightToken.off();
        return responseText;
    }

    public static String urlConnectionUpload(String token, long shar, byte[] bytes, String filename) {
        StringBuffer sb = new StringBuffer("http://localhost:8081/file/");
        sb.append(token);
        sb.append("/");
        sb.append(shar);
        String url = sb.toString();
        String charset = "utf-8";
        String param = "value";
        String boundary = Long.toHexString(System.currentTimeMillis());
        String CRLF = "\r\n";
        final String split = "------";
        try {
            URLConnection connection = new URL(url).openConnection();
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=----" + boundary);
            connection.setDoOutput(true);
            OutputStream output = connection.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(output));
            writer.append(split);
            writer.append(boundary).append(CRLF);
            writer.append("Content-Disposition:form-data;name=\"data\";filename=\"" + filename + "\"").append(CRLF);
            writer.append("Content-Type:application/octet-stream").append(CRLF);
            writer.append(CRLF).append(CRLF);
            writer.flush();

            output.write(bytes);

            writer.append(CRLF);
            writer.append(split);
            writer.append(boundary);
            writer.append("--");
            writer.flush();
            HttpURLConnection httpURLConnection = ((HttpURLConnection) connection);
            int responseCode = httpURLConnection.getResponseCode();
            System.out.println(inputStream2String(httpURLConnection.getInputStream()));
            writer.close();
            httpURLConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String inputStream2String(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[1024];
        int len = in.read(b);
        while(len!=-1){
            out.append(new String(b,0,len));
            len = in.read(b);
        }
        return out.toString();
    }

    public static void main(String args[]) {
        upload(new File("/Users/huanghuanlai/Desktop/b2b2c.zip"));
    }
}
