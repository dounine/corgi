package com.dounine.corgi.storage.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by huanghuanlai on 16/6/29.
 */
@RestController
public class HomeAct {

    /**
     * 项目首页
     * @return 首页内容
     */
    @GetMapping("")
    public ModelAndView home(){
        return new ModelAndView("index");
    }

}
