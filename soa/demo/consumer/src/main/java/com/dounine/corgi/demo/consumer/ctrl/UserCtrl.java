package com.dounine.corgi.demo.consumer.ctrl;

import com.dounine.corgi.commons.ResponseText;
import com.dounine.corgi.rpc.spring.Reference;
import com.dounine.corgi.demo.service.user.IUserSer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by huanghuanlai on 16/8/17.
 */
@RestController
public class UserCtrl {

    @Reference(version = "1.0.0")
    private IUserSer userSer;

    @GetMapping("hello")
    public ResponseText hello(){
        return new ResponseText(userSer.hello());
    }

}
