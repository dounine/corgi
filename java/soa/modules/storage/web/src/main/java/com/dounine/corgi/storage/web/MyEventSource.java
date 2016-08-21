package com.dounine.corgi.storage.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by huanghuanlai on 16/6/29.
 */
@WebServlet("/eventSource")
public class MyEventSource extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setCharacterEncoding("UTF-8");

        resp.setContentType("text/event-stream");

        //清除缓存
        resp.setHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
        PrintWriter out = resp.getWriter();


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");

        //必须写
        out.print("data:");

        //推送到的浏览器的数据
        out.println(sdf.format(new Date()));
        //得有一个空白行，不然在浏览器无法输出
        out.println();
        out.flush();
        out.close();



    }
}
