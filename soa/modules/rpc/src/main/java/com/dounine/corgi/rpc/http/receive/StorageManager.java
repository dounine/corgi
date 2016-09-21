package com.dounine.corgi.rpc.http.receive;


import com.dounine.corgi.rpc.http.utils.UUIDUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dounine.corgi.rpc.http.receive.FileInfoManager.DIR_PATH;

/**
 * Created by huanghuanlai on 16/6/27.
 */
public final class StorageManager {

    private StorageManager() {
    }

    private static final String TMP_BLOCK_NAME = ".part";//临时文件名
    public static final Pattern UUID_AND_SHAR = Pattern.compile("[a-z0-9]{32}/\\d+");

    /**
     * 根据token与savePath创建文件上传临时目录
     *
     * @param fileInfo token与savePath不能为空
     */
    public static void createTokenDIR(FileInfo fileInfo) {
        fileInfo.setToken(UUIDUtils.create());
        String path = null;
        if(StringUtils.isNotBlank(fileInfo.getSavePath())){
            path = fileInfo.getSavePath();
        }else{
            path = DIR_PATH;
        }
        StringBuilder sb = new StringBuilder(path);
        sb.append(File.separator);
        sb.append(".");
        sb.append(fileInfo.getToken());

        File dirTmp = new File(sb.toString());
        if (!dirTmp.exists()) {
            dirTmp.mkdirs();
        }
        FileInfoManager.create(fileInfo);
    }

    public static UploadInfo parseUrl(String url) {
        Matcher matcher = UUID_AND_SHAR.matcher(url);
        UploadInfo uploadInfo = null;
        if (matcher.find()) {
            String[] ifs = matcher.group().split("/");
            uploadInfo = new UploadInfo();
            uploadInfo.setToken(ifs[0]);
            uploadInfo.setShar(Integer.parseInt(ifs[1]));
        }
        return uploadInfo;
    }

    /**
     * 文件分片保存
     *
     * @param fileInfo token与savePath不能为空,is为文件读取流
     * @param is       文件读取流
     */
    public static boolean save(FileInfo fileInfo, InputStream is) {
        boolean uploadFinish = true;
        String path = null;
        if(StringUtils.isNotBlank(fileInfo.getSavePath())){
            path = fileInfo.getSavePath();
        }else{
            path = DIR_PATH;
        }
        StringBuffer tokenFP = new StringBuffer(path);
        tokenFP.append(File.separator);
        tokenFP.append(".");
        tokenFP.append(fileInfo.getToken());
        File tokenFile = new File(tokenFP.toString());

        if (tokenFile.exists()) {
            tokenFP.append(File.separator);
            tokenFP.append(TMP_BLOCK_NAME);
            tokenFP.append(fileInfo.getUpShar());
            File distFile = new File(tokenFP.toString());
            try {
                FileUtils.copyInputStreamToFile(is, distFile);//上传文件流保存到临时文件当中
                FileInfo updateInfo = FileInfoManager.updateCacheShars(fileInfo);//更新上传的分片信息
                if (updateInfo.getMaxShar() == 1) {//单文件分片
                    moveShar(updateInfo);
                } else if (updateInfo.getShars().length == updateInfo.getMaxShar()) {//所有文件分片上传完毕,可以合并
                    mergeShars(updateInfo);
                } else {
                    FileInfoManager.updateCacheTime(updateInfo);//更新上传时间
                    uploadFinish = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return uploadFinish;
    }

    /**
     * 根据token与savePath保存文件分片
     *
     * @param fileInfo token与savePath不能为空
     * @param file     要保存的文件
     */
    public static void save(FileInfo fileInfo, File file) {
        try {
            save(fileInfo, new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 移动只一个分片文件
     *
     * @param fileInfo
     */
    public static void moveShar(FileInfo fileInfo) {
        String path = null;
        if(StringUtils.isNotBlank(fileInfo.getSavePath())){
            path = fileInfo.getSavePath();
        }else{
            path = DIR_PATH;
        }
        StringBuilder tokenPath = new StringBuilder(path);//token文件夹路径
        tokenPath.append(File.separator);
        tokenPath.append(".");
        tokenPath.append(fileInfo.getToken());

        StringBuilder targetFP = new StringBuilder(tokenPath);//目标路径
        targetFP.append(File.separator);

        StringBuilder renameFP = new StringBuilder(targetFP);//更名路径
        renameFP.append(fileInfo.getName());

        targetFP.append(".part1");
        File targetFile = new File(targetFP.toString());

        StringBuilder existOFP = new StringBuilder(path);
        existOFP.append(File.separator);
        existOFP.append(fileInfo.getName());
        File existOldFile = new File(existOFP.toString());
        try {
            File renameFile = new File(renameFP.toString());

            targetFile.renameTo(renameFile);//将.part1更名为真正的文件

            if (existOldFile.exists()) {//判断文件是否存在,存在则删除原来的文件
                FileUtils.forceDelete(existOldFile);
            }

            FileUtils.moveFileToDirectory(renameFile, new File(path), false);//移动文件到上一层目录

            FileUtils.forceDelete(new File(tokenPath.toString()));//删除创建的临时文件夹

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 文件分片信息合并成文件
     *
     * @param fileInfo token与savePath不能为空
     */
    private static void mergeShars(FileInfo fileInfo) {
        String path = null;
        if(StringUtils.isNotBlank(fileInfo.getSavePath())){
            path = fileInfo.getSavePath();
        }else{
            path = DIR_PATH;
        }
        StringBuilder tokenPath = new StringBuilder(path);
        tokenPath.append(File.separator);
        tokenPath.append(".");
        tokenPath.append(fileInfo.getToken());

        StringBuilder targetFP = new StringBuilder(tokenPath);
        targetFP.append(File.separator);
        targetFP.append(fileInfo.getName());
        File targetFile = new File(targetFP.toString());

        FileOutputStream targetFOS = null;
        try {
            targetFOS = new FileOutputStream(targetFile, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        File[] parts = new File(tokenPath.toString()).listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().startsWith(TMP_BLOCK_NAME);
            }
        });

        TreeMap<Integer, File> sortFiles = new TreeMap<>();
        for (File part : parts) {
            sortFiles.put(Integer.parseInt(part.getName().substring(5)), part);
        }
        sortFiles.descendingKeySet();//排序

        try {
            for (File part : sortFiles.values()) {//文件合并
                FileUtils.copyFile(part, targetFOS);
            }

            for (File part : parts) {//删除文件分片信息
                FileUtils.forceDelete(part);
            }

            StringBuilder existOFP = new StringBuilder(path);
            existOFP.append(File.separator);
            existOFP.append(fileInfo.getName());
            File existOldFile = new File(existOFP.toString());

            if (existOldFile.exists()) {//判断文件是否存在,存在则删除原来的文件
                FileUtils.forceDelete(existOldFile);
            }

            FileUtils.moveFileToDirectory(targetFile, new File(path), false);//移动文件到上一层目录

            FileUtils.forceDelete(new File(tokenPath.toString()));//删除创建的临时文件夹

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算文件上传进度
     *
     * @param fileInfo token必值
     * @return 进度 0~100
     */
    public static Progress calculateProgress(FileInfo fileInfo) {
        Progress progress = new Progress();
        FileInfo _fileInfo = FileInfoManager.readCache(fileInfo);
        boolean lastBlockUp = false;//最后一块文件是否已经上传
        int sumBlock = _fileInfo.getMaxShar();//分的总块数
        if (sumBlock == 1 || sumBlock == _fileInfo.getShars().length) {//文件不用分片或所有分片上传完成
            progress.setPercent(100);
        } else {
            for (Integer up : _fileInfo.getShars()) {
                if (up.equals(sumBlock)) {
                    lastBlockUp = true;
                    break;
                }
            }
            double avgSize = _fileInfo.getAvgShar();//每个分片的大小
            if (lastBlockUp) {
                long avgSumSize = (long) (avgSize * (_fileInfo.getShars().length - 1));//已经上传的平均片总大小
                long lastBlockSize = (long) (_fileInfo.getSize() - avgSize * (sumBlock - 1));//最后一片的大小
                long uploadSize = (long) (avgSumSize + lastBlockSize);//平均片总大小+最后一片大小=总上传大小
                progress.setUpSize(uploadSize);
                progress.setPercent((int) Math.ceil(1f * uploadSize / _fileInfo.getSize() * 100));
            } else {
                long uploadSize = (long) (avgSize * _fileInfo.getShars().length);//已经上传的平均片总大小
                progress.setUpSize(uploadSize);
                progress.setPercent((int) Math.ceil(1f * uploadSize / _fileInfo.getSize() * 100));
            }
        }
        _fileInfo.setProgress(progress);
        return progress;
    }

    /**
     * 停止上传
     *
     * @param token
     */
    public static void cancel(String token) {
        FileInfoManager.cancel(token);
    }

    public static void pause(String token) {
        FileInfo fileInfo = FileInfoManager.readCache(token);
        if (null != fileInfo) {
            FileInfoManager.update(fileInfo);
        }
    }

    public static List<FileInfo> readPendingFileInfo() {
        return FileInfoManager.readPendingFileInfo();
    }

    public static int pendingAbort() {
        return pendingAbort(null);
    }

    public static int pendingAbort(String token) {
        return 0;
    }
}
