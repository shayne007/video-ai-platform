package com.keensense.dataconvert.biz.controller.page;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.biz.controller.page
 * @Description： <p> PageController </p>
 * @Author： - Jason
 * @CreatTime：2019/8/27 - 9:35
 * @Modify By：
 * @ModifyTime： 2019/8/27
 * @Modify marker：
 */
@Controller
public class PageController {

    private static final String DEFAULT_PASS_WORD = "123456";
    private static final String DEFAULT_USER_NAME = "admin";

    /**
     * 路由到index
     * @return
     */
    @GetMapping(value = "/*")
    public String router(){
        return "login";
    }


    /**
     * 登录操作
     * @param username
     * @param password
     * @param map
     * @param mv
     * @return
     */
    @PostMapping(value = "/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, Map<String,Object> map, ModelAndView mv){
        if (StringUtils.isEmpty(username) && StringUtils.isEmpty(password)){
            mv.addObject("msg","账号或密码不能为空.");
            return "login";
        }else if (DEFAULT_USER_NAME.equals(username) && DEFAULT_PASS_WORD.equals(password)){
            //登录成功
            return  "views/index";
        }else{
            map.put("msg","账号或密码错误!");
            return  "login";
        }
    }

}
