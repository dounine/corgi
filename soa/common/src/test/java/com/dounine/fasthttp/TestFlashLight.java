package com.dounine.fasthttp;

import junit.framework.TestCase;
import org.apache.http.Consts;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;

public class TestFlashLight extends TestCase{

    public void testA(){
        System.out.println("nihao");
    }

    @Test
    public void testProxy(){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://issp.bjike.com/admin/login");
        httpGet.setHeader("Host","yinyuetai.musicway.cn");
        httpGet.setConfig(RequestConfig.custom().setProxy(new HttpHost("yinyuetai.musicway.cn",80)).build());
        try {
            HttpResponse response = httpClient.execute(httpGet);
            System.out.println(EntityUtils.toString(response.getEntity(), Consts.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
