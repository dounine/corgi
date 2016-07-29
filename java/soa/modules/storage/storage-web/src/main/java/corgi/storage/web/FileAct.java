package corgi.storage.web;

import com.alibaba.fastjson.JSON;
import com.dounine.fasthttp.receive.FileInfo;
import com.dounine.fasthttp.receive.FileInfoManager;
import com.dounine.fasthttp.receive.Progress;
import com.dounine.fasthttp.receive.StorageManager;
import corgi.commons.ResponseText;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by huanghuanlai on 16/6/13.
 */
@RestController
@RequestMapping("file")
public class FileAct {

    static {
        FileInfoManager.init("/Users/huanghuanlai/Desktop/storage");
    }

    /**
     * 分页上传文件片段
     * @param token 文件上传事件令牌
     * @param shar 上传分片索引
     * @param returnData 是否返回响应数据
     * @param multipartFile 组装好的文件信息
     * @param response 请求响应对象
     * @return 上传进度信息
     */
    @PostMapping("{token:[a-z0-9]{32}}/{shar:^[1-9]\\d{0,4}}")
    public ResponseText upload(@PathVariable String token,
                               @PathVariable int shar,
                               @RequestParam(defaultValue = "true") boolean returnData,
                               MultipartHttpServletRequest multipartFile,
                               HttpServletResponse response){
        if(!FileInfoManager.exist(token)){
            ResponseText responseText = new ResponseText();
            responseText.setErrno(2);
            responseText.setMsg("token不存在,或已失效");
            return responseText;
        }
        Map<String, MultipartFile> mfs = multipartFile.getFileMap();
        response.setCharacterEncoding("utf-8");
        for(MultipartFile mf : mfs.values()){
            try {
                FileInfo fileInfo = new FileInfo();
                fileInfo.setToken(token);
                fileInfo.setUpShar(shar);
                boolean uploadFinish = StorageManager.save(fileInfo,mf.getInputStream());
                Progress progress = StorageManager.calculateProgress(fileInfo);//计算上传进度
                progress.setToken(token);
                if(uploadFinish){//文件是否上传完整
                    FileInfoManager.removeCache(fileInfo);//删除缓存信息
                }
                if(returnData){
                    response.getWriter().print(JSON.toJSON(progress));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 取消上传
     * @param token 令牌
     */
    @DeleteMapping("{token:[a-z0-9]{32}}/cancel")
    public void cancel(@PathVariable String token){
        StorageManager.cancel(token);
    }

    /**
     * 根据停牌暂停上传
     * @param token 令牌
     */
    @PutMapping("{token:[a-z0-9]{32}}/pause")
    public void pause(@PathVariable String token){
        StorageManager.pause(token);
    }

    /**
     * 根据令牌读取上传信息
     * @param token 令牌
     * @return 上传文件信息
     */
    @GetMapping("{token:[a-z0-9]{32}}")
    public ResponseText read(@PathVariable String token){
        return new ResponseText(FileInfoManager.readCache(token));
    }

    /**
     * 创建文件上传事件
     * @param fileInfo 上传文件信息
     * @param result 错误绑定信息
     * @return 成功后的事件信息
     */
    @PostMapping("token")
    public ResponseText token(@Validated FileInfo fileInfo, BindingResult result){
        StorageManager.createTokenDIR(fileInfo);
        return new ResponseText(fileInfo);
    }

    /**
     * 读取等待上传的事件信息
     * @return 等待上传事件列表信息
     */
    @GetMapping("pending")
    public ResponseText pending(){
        return new ResponseText(StorageManager.readPendingFileInfo());
    }

    /**
     * 删除所有等待事件列表
     * @return 删除成功数量
     */
    @DeleteMapping("pending")
    public ResponseText pendingAbort(){
        return new ResponseText(StorageManager.pendingAbort());
    }

    /**
     * 根据令牌删除指定上传事件
     * @param token 令牌
     * @return 成功与否
     */
    @DeleteMapping("{token}/pending")
    public ResponseText pendingDelete(@PathVariable String token){
        return new ResponseText(StorageManager.pendingAbort(token));
    }

}
