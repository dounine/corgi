package com.dounine.corgi.rpc.invoke;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dounine.corgi.rpc.http.Coordinate;
import com.dounine.corgi.rpc.http.FlashLight;
import com.dounine.corgi.rpc.http.IFlashLight;
import com.dounine.corgi.rpc.http.medias.IMedia;

import java.io.IOException;
import java.io.OutputStream;

/**
 * http 请求与响应
 */
public class HttpInvoke implements Invoke{

    private static final Invoke INVOKE = new HttpInvoke();

    @Override
    public String fetch(IMedia media, String url) throws IOException {
        IFlashLight flashLight = new FlashLight(new Coordinate(url));
        flashLight.on();//打开
        if(null!=media){
            flashLight.ready(media);
        }
        flashLight.emit();
        String rep = flashLight.response().getText();
        flashLight.off();//关闭
        return rep;
    }

    @Override
    public void push(Object data, OutputStream out) {
        try {
            if(data instanceof String){
                out.write(data.toString().getBytes());
            }else{
                String json = JSON.toJSONString(data, SerializerFeature.WriteNullStringAsEmpty);
                out.write(json.getBytes());
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Invoke instanct() {
        return INVOKE;
    }
}
