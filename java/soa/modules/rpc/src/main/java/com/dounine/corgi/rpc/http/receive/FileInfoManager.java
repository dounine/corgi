package com.dounine.corgi.rpc.http.receive;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Created by huanghuanlai on 16/6/27.
 */
public final class FileInfoManager {

    private FileInfoManager(){}

    private static final String FILE_INFO_NAME = ".file_info";
    private static final Map<String, FileInfo> UPLOAD_FILEINFOS = new ConcurrentHashMap<>();
    public static String DIR_PATH;
    public static final Pattern HIDDEN_TOKEN = Pattern.compile("\\.[a-z0-9]{32}");

    public static void init(){
        queryTimeoutInfo();
        readLoadPendingFileInfo();
    }

    public static void init(String baseDir){
        if(null!=baseDir&&baseDir.trim().length()>0){
            DIR_PATH = baseDir;
            File file = new File(DIR_PATH);
            if(!file.exists()){
                file.mkdirs();
            }
            init();
        }else{
            throw new RuntimeException("baseDir 存储路径不能为空");
        }
    }

    public static final List<FileInfo> readPendingFileInfo(){
        List<FileInfo> fileInfos = new ArrayList<>(UPLOAD_FILEINFOS.size());
        for(FileInfo fileInfo : UPLOAD_FILEINFOS.values()){
            Progress progress = fileInfo.getProgress();
            if(null!=progress&&progress.getPercent()>0){
                fileInfos.add(fileInfo);
            }
        }
        return fileInfos;
    }

    private static final void readLoadPendingFileInfo(){
        System.out.println(DIR_PATH);
        File file = new File(DIR_PATH);
        File[] tokenFiles = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return dir.isDirectory()&&HIDDEN_TOKEN.matcher(name).matches();
            }
        });
        for(File f : tokenFiles){
            File[] fileInfo = f.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.equals(FILE_INFO_NAME);
                }
            });
            if(null!=fileInfo&&fileInfo.length>0){
                try {
                    FileInfo fi = JSON.parseObject(FileUtils.readFileToString(fileInfo[0],Charset.forName("utf-8")),FileInfo.class);
                    UPLOAD_FILEINFOS.put(fi.getToken(),fi);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static final void queryTimeoutInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //10分钟没操作清除
                    Iterator<FileInfo> fileInfoIterator = UPLOAD_FILEINFOS.values().iterator();
                    while(fileInfoIterator.hasNext()){
                        FileInfo fileInfo = fileInfoIterator.next();
                        if(fileInfo.getUpdateTime().plusMinutes(60).isBefore(LocalDateTime.now())){
                            String path = null;
                            if(null!=fileInfo.getSavePath()&&fileInfo.getSavePath().trim().length()>0){
                                path = fileInfo.getSavePath();
                            }else{
                                path = DIR_PATH;
                            }
                            StringBuffer tokenFP = new StringBuffer(path);
                            tokenFP.append(File.separator);
                            tokenFP.append(".");
                            tokenFP.append(fileInfo.getToken());
                            File tokenFile = new File(tokenFP.toString());
                            try {
                                FileUtils.forceDelete(tokenFile);
                                fileInfoIterator.remove();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }).start();
    }

    public static final void create(FileInfo fileInfo) {
        String fileInfoPath = assembleFileInfoPath(fileInfo);
        FileInfo readFileInfo = null;
        fileInfo.setAvgShar(SharUtils.calculateAvgShar(fileInfo.getSize()));//设置每片大小
        if(fileInfo.getSize()==0){//文件大小为0byte时,设置也要上传
            fileInfo.setMaxShar(1);
        }else{
            fileInfo.setMaxShar((int) Math.ceil(fileInfo.getSize() / (1f * fileInfo.getAvgShar())));//设置文件分片总数
        }
        LocalDateTime ldt = LocalDateTime.now();
        fileInfo.setCreateTime(ldt);
        fileInfo.setUpdateTime(ldt);
        synchronized (UPLOAD_FILEINFOS){
            com.dounine.corgi.rpc.http.utils.FileUtils.writeStringToFile(JSON.toJSONString(fileInfo),new File(fileInfoPath));
            UPLOAD_FILEINFOS.put(fileInfo.getToken(), fileInfo);
        }
    }

    public static final boolean exist(String token){
        return UPLOAD_FILEINFOS.get(token)!=null;
    }

    public static final FileInfo readCache(FileInfo fileInfo) {
        return UPLOAD_FILEINFOS.get(fileInfo.getToken());
    }

    public static final FileInfo readCache(String token) {
        return UPLOAD_FILEINFOS.get(token);
    }

    private static final FileInfo read(FileInfo fileInfo) {
        File file = new File(assembleFileInfoPath(fileInfo));
        FileInfo readFileInfo = null;
        try {
            readFileInfo = JSON.parseObject(FileUtils.readFileToString(file, Charset.forName("utf-8")), FileInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readFileInfo;
    }

    public static final String assembleFileInfoPath(FileInfo fileInfo) {
        String path = null;
        if(null!=fileInfo.getSavePath()&&fileInfo.getSavePath().trim().length()>0){
            path = fileInfo.getSavePath();
        }else{
            path = DIR_PATH;
        }
        StringBuffer sb = new StringBuffer(path);
        sb.append(File.separator);
        sb.append(".");
        sb.append(fileInfo.getToken());
        sb.append(File.separator);
        sb.append(FILE_INFO_NAME);
        return sb.toString();
    }

    public static final synchronized void removeCache(FileInfo fileInfo){
        UPLOAD_FILEINFOS.remove(fileInfo.getToken());
    }

    public static final synchronized FileInfo updateCacheTime(FileInfo fileInfo) {
        FileInfo cacheFileInfo = UPLOAD_FILEINFOS.get(fileInfo.getToken());
        cacheFileInfo.setUpdateTime(LocalDateTime.now());

        return cacheFileInfo;
    }

    public static final synchronized FileInfo updateCacheShars(FileInfo fileInfo) {
        FileInfo cacheFileInfo = UPLOAD_FILEINFOS.get(fileInfo.getToken());

        Integer[] ups = cacheFileInfo.getShars();
        boolean has = false;
        for(Integer up : ups){
            if(up.equals(fileInfo.getUpShar())){
                has = true;break;
            }
        }
        if (!has){
            Integer[] newUps = new Integer[ups.length+1];
            for(int i =0,len=ups.length;i<len;i++){
                newUps[i]=ups[i];
            }
            newUps[ups.length]=fileInfo.getUpShar();
            cacheFileInfo.setShars(newUps);//更新已经上传的块索引
        }

        return cacheFileInfo;
    }

    public static final synchronized void update(FileInfo fileInfo) {
        try {
            File file = new File(assembleFileInfoPath(fileInfo));
            FileUtils.writeStringToFile(file, JSON.toJSONString(fileInfo), Charset.forName("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final void cancel(String token) {
        FileInfo fileInfo = UPLOAD_FILEINFOS.get(token);
        if(null!=fileInfo){
            String path = null;
            if(StringUtils.isNotBlank(fileInfo.getSavePath())){
                path = fileInfo.getSavePath();
            }else{
                path = DIR_PATH;
            }
            StringBuffer tokenPath = new StringBuffer(path);
            tokenPath.append(File.separator);
            tokenPath.append(".");
            tokenPath.append(token);
            try {
                synchronized (UPLOAD_FILEINFOS){
                    File file = new File(tokenPath.toString());
                    FileUtils.forceDelete(file);
                    UPLOAD_FILEINFOS.remove(token);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
