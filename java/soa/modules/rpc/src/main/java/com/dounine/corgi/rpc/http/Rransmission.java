package com.dounine.corgi.rpc.http;

import com.dounine.corgi.rpc.http.medias.IMedia;
import com.dounine.corgi.rpc.http.medias.MediaFile;
import com.dounine.corgi.rpc.http.medias.MediaSharFile;
import com.dounine.corgi.rpc.http.rep.Response;
import com.dounine.corgi.rpc.http.types.HeartType;
import com.dounine.corgi.rpc.http.utils.FileUtils;
import com.dounine.corgi.rpc.http.utils.InputStreamUtils;
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.*;
import java.net.ConnectException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.dounine.corgi.rpc.http.types.HeartType.PUT;

/**
 * Created by huanghuaai on 16/7/6.
 * 传送数据
 */
public class Rransmission implements IRransmission {

    protected IFlashLight flashLight;

    protected ICoordinate coordinate;

    protected IMedia media;

    protected List<IMedia> medias;

    protected Response response;

    protected HeartType heartType;

    public Rransmission(IFlashLight flashLight){
        this.flashLight = flashLight;
    }

    @Override
    public void setFlashLight(IFlashLight flashLight) {
        this.flashLight = flashLight;
    }


    @Override
    public void heartbeat(final HeartType heartType) throws IOException {
        this.heartType = heartType;
        this.coordinate = flashLight.coordinate();
        switch (heartType){
            case GET:
                getHeartbeat();break;
            case POST:
                postHeartbeat();break;
            case PUT:
                putHeartbeat();break;
            case PATCH:
                patchHeartbeat();break;
            case DELETE:
                deleteHeartbeat();break;
            case FILE:
                fileHeartbeat();break;
            case SHARE_FILE:
                fileShareHeartbeat();break;
            case OPTIONS:
                optionsHeartbeat();break;
        }
    }

    @Override
    public void push(HeartType heartType) throws IOException {
        heartbeat(heartType);
    }

    @Override
    public void push(HeartType heartType,IMedia media) throws IOException {
        this.media = media;
        heartbeat(heartType);
    }

    @Override
    public void push(HeartType heartType,List<IMedia> medias) throws IOException {
        this.medias = medias;
        heartbeat(heartType);
    }

    @Override
    public Response response() {
        if(null==response){
            HttpURLConnection httpURLConnection = coordinate.httpPoint();
            response = new Response();
            try {
                String text = InputStreamUtils.istreamToString(httpURLConnection.getInputStream());
                response.setText(text);
                response.setCode(httpURLConnection.getResponseCode());
            }catch (ConnectException ce){
                response.setCode(500);
                response.setText(ce.getMessage());
            }catch (FileNotFoundException enfe){
                response.setCode(404);
                response.setText(enfe.getMessage());
                enfe.printStackTrace();
            }catch (IOException e) {
                response.setCode(-1);
                response.setText(e.getMessage());
                e.printStackTrace();
            }
        }
        return response;
    }

    private StringBuilder dataJoin(){
        StringBuilder sb = new StringBuilder();
        IMedia med = (IMedia) media;
        String frontStr = "";
        if(heartType.equals(HeartType.GET)||heartType.equals(HeartType.DELETE)||heartType.equals(HeartType.OPTIONS)){
            frontStr = "?";
        }
        sb.append(frontStr);
        if(null!=media){
            sb.append(med.getName());
            sb.append("=");
            sb.append(med.getValue());
        }else if(null!=medias){
            for(IMedia im : medias){
                sb.append(frontStr);
                sb.append(im.getName());
                sb.append("=");
                sb.append(im.getValue());
                frontStr = "&";
            }
        }
        return sb;
    }

    public void getHeartbeat() throws IOException {
        StringBuilder sb = dataJoin();
        coordinate.data(sb.toString());
        URLConnection connection = coordinate.point();
        RequestProperty.init(connection);
        connection.connect();
    }

    public void postHeartbeat(){
        StringBuilder sb = dataJoin();
        URLConnection connection = coordinate.point();
        try {
            RequestProperty.init(connection);
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            os.write(sb.toString().getBytes());
            os.flush();
        }catch (ConnectException ce){
            ce.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void putHeartbeat(){
        StringBuilder sb = dataJoin();

        HttpURLConnection connection = coordinate.httpPoint();
        try {
            connection.setRequestMethod(PUT.toString());
            RequestProperty.init(connection);
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            os.write(sb.toString().getBytes());
            os.flush();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void patchHeartbeat(){
        StringBuilder sb = dataJoin();
        HttpURLConnection connection = coordinate.httpPoint();
        try {
            connection.setRequestMethod(HeartType.PATCH.toString());
            RequestProperty.init(connection);
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            os.write(sb.toString().getBytes());
            os.flush();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteHeartbeat(){
        StringBuilder sb = dataJoin();

        coordinate.data(sb.toString());
        HttpURLConnection connection = coordinate.httpPoint();
        try {
            connection.setRequestMethod(HeartType.DELETE.toString());
            connection.connect();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fileHeartbeat(){
        final URLConnection connection = coordinate.point();
        final MediaFile mediaFile = (MediaFile) media;
        final String boundary = Long.toHexString(System.currentTimeMillis());
        final String CRLF = "\r\n";
        final String split = "------";
        try {
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=----" + boundary);
            connection.setDoOutput(true);
            OutputStream output = connection.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(output));
            writer.append(split);
            writer.append(boundary).append(CRLF);
            writer.append("Content-Disposition:form-data;name=\"data\";filename=\"");
            writer.append(mediaFile.getFile().getName());
            writer.append("\"").append(CRLF);
            writer.append("Content-Type:application/octet-stream").append(CRLF);
            writer.append(CRLF).append(CRLF);
            writer.flush();

            FileUtils.writeFileByOutput(mediaFile.getFile(),output);

            writer.append(CRLF);
            writer.append(split);
            writer.append(boundary);
            writer.append("--");
            writer.flush();
            java.net.HttpURLConnection httpURLConnection = ((java.net.HttpURLConnection) connection);
            int responseCode = httpURLConnection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fileShareHeartbeat(){
        long begin = System.currentTimeMillis();
        final MediaSharFile mediaSharFile = (MediaSharFile) media;
        final int nThreads = Runtime.getRuntime().availableProcessors();

        ExecutorService es = Executors.newFixedThreadPool(nThreads);

        long shars = 0, size = mediaSharFile.getFile().length();
        if(size==0){//文件大小为0byte时,设置也要上传
            shars = 1;
        }else{
            shars = (int) Math.ceil(size / (1f * mediaSharFile.getSharSize()));//设置文件分片总数
        }
        List<Future<String>> futures = new ArrayList<>((int) shars);
        final long sharSize = mediaSharFile.getSharSize();
        final String fileName = mediaSharFile.getFile().getName();
        final String uri = coordinate.getUri();
        try {
            final RandomAccessFile randomAccessFile = new RandomAccessFile(mediaSharFile.getFile(),"r");
            for (long i = 0; i < shars; i++) {
                final long start = i * mediaSharFile.getSharSize();
                final long shar = i + 1;
                futures.add(es.submit(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        byte[] bytes = FileUtils.readLengthBytes(randomAccessFile, start, (int) sharSize);
                        return uploadSharFile(uri,mediaSharFile.getToken(), shar, bytes, mediaSharFile.getFile().getName());
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
                    randomAccessFile.close();
                    System.out.println("占用时间:" + (System.currentTimeMillis() - begin));
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String uploadSharFile(String uri,String token,long shar,byte[] bytes,String filename){
        String url = new StringBuffer(uri).append("/").append(token).append("/").append(shar).toString();
        coordinate.setUri(url);
        final URLConnection connection = coordinate.point();
        final String boundary = Long.toHexString(System.currentTimeMillis());
        final String CRLF = "\r\n";
        final String split = "------";
        try {
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=----" + boundary);
            connection.setDoOutput(true);
            connection.setConnectTimeout(3000);
            connection.setDefaultUseCaches(false);
            connection.setUseCaches(false);
            OutputStream output = connection.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(output));
            writer.append(split);
            writer.append(boundary).append(CRLF);
            writer.append("Content-Disposition:form-data;name=\"data\";filename=\"");
            writer.append(filename);
            writer.append("\"").append(CRLF);
            writer.append("Content-Type:application/octet-stream").append(CRLF);
            writer.append(CRLF).append(CRLF);
            writer.flush();

            output.write(bytes);

            writer.append(CRLF);
            writer.append(split);
            writer.append(boundary);
            writer.append("--");
            writer.flush();
            output.flush();
            java.net.HttpURLConnection httpURLConnection = ((java.net.HttpURLConnection) connection);
            httpURLConnection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void optionsHeartbeat(){
        StringBuilder sb = dataJoin();
        coordinate.data(sb.toString());
        HttpURLConnection connection = coordinate.httpPoint();
        try {
            connection.setRequestMethod(HeartType.OPTIONS.toString());
            connection.connect();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
