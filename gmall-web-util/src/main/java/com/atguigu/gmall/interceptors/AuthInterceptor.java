package com.atguigu.gmall.interceptors;


import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.annotations.LoginRequired;
import com.atguigu.gmall.util.CookieUtil;
import com.atguigu.gmall.util.HttpclientUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //拦截代码
        //判断被拦截的请求的访问方法的注解（是否需要拦截）
        HandlerMethod hm = (HandlerMethod) handler;
        LoginRequired methodAnnotation = hm.getMethodAnnotation(LoginRequired.class);
        //是否拦截
        StringBuffer url = request.getRequestURL();
        System.out.println(url);
        if(methodAnnotation==null){
            return true;
        }
        String token = "";
        String oldToken = CookieUtil.getCookieValue(request, "oldToken", true);
        if (StringUtils.isNotBlank(oldToken)){
            token = oldToken;
        }
        String newToken = request.getParameter("token");
        if (StringUtils.isNotBlank(newToken)){
            token  = newToken;
        }
        // 是否必须登陆
        boolean loginSuccess = methodAnnotation.loginSuccess();//获得该请求是否需要登陆成功
        String success = "fail";
        Map<String,String> successMap = new HashMap<>();
        //调用认证中心进行验证
        if (StringUtils.isNotBlank(token)){
            String ip = request.getHeader("x-forwarded-for");// 通过nginx转发的客户端ip
            if(StringUtils.isBlank(ip)){
                ip = request.getRemoteAddr();// 从request中获取ip
                if(StringUtils.isBlank(ip)||"0:0:0:0:0:0:0:1".equals(ip)){
                    ip = "127.0.0.1";
                }
            }
            String successJson = HttpclientUtil.doGet("http://localhost:8085/verify?token=" + token+"&currentIp="+ip);
            successMap = JSON.parseObject(successJson, Map.class);
            success = successMap.get("status");
        }
        if (loginSuccess){
            //必须登陆成功才能访问
           if (!success.equals("success")){
               //重定向会passport登录
               StringBuffer requestURL = request.getRequestURL();
               response.sendRedirect("http://localhost:8085/index?ReturnUrl="+requestURL);
                return false;
           }
               //验证通过 覆盖cookies中的token
               request.setAttribute("memberId",successMap.get("memberId"));
               request.setAttribute("nickname",successMap.get("nickname"));
                //验证通过，覆盖cookie中的token
                if(StringUtils.isNotBlank(token)){
                    CookieUtil.setCookie(request,response,"oldToken",token,60*60*2,true);
                }
        }else{
            //没有登陆也能用，但是必须验证
            if (success.equals("success")){
                //重定向会passport登陆
                // 需要将token携带的用户信息写入
                request.setAttribute("memberId",successMap.get("memberId"));
                request.setAttribute("nickname",successMap.get("nickname"));
                //验证通过，覆盖cookie中的token
                if(StringUtils.isNotBlank(token)){
                    CookieUtil.setCookie(request,response,"oldToken",token,60*60*2,true);
                }
            }

        }
        System.out.println("进入拦截方法");
        return true;
    }
}
