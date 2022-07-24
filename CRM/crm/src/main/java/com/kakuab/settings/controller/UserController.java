package com.kakuab.settings.controller;

import com.kakuab.commons.constant.Constants;
import com.kakuab.commons.domain.ReturnObject;
import com.kakuab.commons.utils.DateUtil;
import com.kakuab.pojo.User;
import com.kakuab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(){
        return "settings/qx/user/login";
    }

    @RequestMapping("/settings/qx/user/login.do")
    @ResponseBody//自动转换json
    public Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request,
                        HttpSession session, HttpServletResponse response){
        //封装参数
        Map<String,Object> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        //调用service方法，查询用户
        User user = userService.selectUserLoginActandLoginPwd(map);
        ReturnObject returnObject = new ReturnObject();
        if (user == null){
            //登录失败，用户名或者密码错误
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("登录失败，用户名或者密码错误");
        }else {
            if (DateUtil.formatDateTime(new Date()).compareTo(user.getExpireTime())>0){
                //compareTo方法：字符串比较，1是大于，0是等于，-1是小于
                //登录失败，用户已过期
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("登录失败，用户已过期");
            }else if("0".equals(user.getLockState())){
                //登录失败，用户状态被锁定
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("登录失败，用户状态被锁定");
            }/*else if(!user.getAllowIps().contains(request.getRemoteUser())){
                //contains方法：是否包含某个字符串
                //登录失败，ip地址受限
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("登录失败，ip地址受限");
            }*/else {
                //登录成功
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                session.setAttribute(Constants.SESSION_USER,user);
                //十天免登录
                if("true".equals(isRemPwd)){
                    Cookie c1 = new Cookie("loginAct", user.getLoginAct());
                    c1.setMaxAge(10*24*60*60);
                    Cookie c2 = new Cookie("loginPwd", user.getLoginPwd());
                    c2.setMaxAge(10*24*60*60);
                    response.addCookie(c1);
                    response.addCookie(c2);
                }
                else {
                    //如果没记住密码，直接删除cookie
                    Cookie c1 = new Cookie("loginAct", "");
                    c1.setMaxAge(0);
                    Cookie c2 = new Cookie("loginPwd", "");
                    c2.setMaxAge(0);
                    response.addCookie(c1);
                    response.addCookie(c2);
                }
            }
        }
        return returnObject;
    }

    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletResponse response,HttpSession session){
        //清除cookie
        Cookie c1 = new Cookie("loginAct", "");
        c1.setMaxAge(0);
        Cookie c2 = new Cookie("loginPwd", "");
        c2.setMaxAge(0);
        response.addCookie(c1);
        response.addCookie(c2);
        //删除Session
        session.invalidate();
        return "redirect:/";
    }
}
