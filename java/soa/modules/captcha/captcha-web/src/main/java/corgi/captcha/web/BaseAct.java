package corgi.captcha.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * Created by huanghuanlai on 16/4/27.
 */
public class BaseAct {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseAct.class);

    @PostMapping("add")
    public String add(){
        return "add";
    }

    @DeleteMapping("{id}/del")
    public String del(@PathVariable String id){
        return "del";
    }

    @PutMapping("{id}/edit")
    public String edit(@PathVariable String id){
        return "edit";
    }

    @GetMapping("list")
    public String list(){
        return "list";
    }

    @GetMapping("find")
    public String find(){
        return "find";
    }

}
