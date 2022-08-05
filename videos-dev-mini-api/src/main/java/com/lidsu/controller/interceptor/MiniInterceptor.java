package com.lidsu.controller.interceptor;

import com.lidsu.utils.IMoocJSONResult;
import com.lidsu.utils.JsonUtils;
import com.lidsu.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

public class MiniInterceptor implements HandlerInterceptor {
    @Autowired
    public RedisOperator redis;

    public static final String USER_REDIS_SESSION="user-redis-session";
    /**
     *在Controller调用之前
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            //返回false 请求被拦截 ，返回
            String userId=request.getHeader("userId");
            String userToken = request.getHeader("userToken");
            System.out.println(userId+"++++"+userToken);
            if(StringUtils.isNotBlank(userId)&&StringUtils.isNotBlank(userToken)) {
                String uniqueToken = redis.get(USER_REDIS_SESSION + ":" + userId);
                System.out.println(uniqueToken);
                if(StringUtils.isEmpty(uniqueToken)&&StringUtils.isBlank(uniqueToken)){
                    System.out.println("请登录。。。");
                    returnErrorResponse(response,IMoocJSONResult.errorTokenMsg("请登录。。。"));
                    return false;
                }else{
                    if(!uniqueToken.equals(userToken)){
                        System.out.println("账号被挤出...");
                        returnErrorResponse(response,IMoocJSONResult.errorTokenMsg("账号被挤出..."));
                        return false;
                    }
                }

            }else {
                System.out.println("请登录。。。");
                returnErrorResponse(response,IMoocJSONResult.errorTokenMsg("请登录。。。"));
                return false;
            }
            /**
             * 返回 false：请求被拦截，返回
             * 返回 true ：请求OK，可以继续执行，放行
             */
            return true;

        }
    public void returnErrorResponse(HttpServletResponse response, IMoocJSONResult result) throws  Exception {

        OutputStream out = null;
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json");
            out = response.getOutputStream();
            out.write(JsonUtils.objectToJson(result).getBytes("utf-8"));
            out.flush();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
