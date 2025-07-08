package com.keensense.admin.aop;

import com.keensense.admin.base.BaseController;
import com.keensense.admin.entity.sys.SysLog;
import com.keensense.admin.entity.sys.SysUser;
import com.keensense.admin.service.sys.ISysLogService;
import com.keensense.admin.util.DateTimeUtils;
import com.keensense.admin.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 日志记录AOP实现
 */
@Aspect
@Slf4j
@Component
public class LogAspect extends BaseController {

    @Resource
    private ISysLogService sysLogService;

    /**
     * @param pjp
     * @return
     * @throws Throwable
     * @Title：doAround
     * @Description: 环绕触发
     * @author shaojian.yu
     * @date 2014年11月3日 下午1:58:45
     */
    @Around("execution(* com.keensense.admin.controller..*.*(..))")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        /**
         * 1.获取request信息 
         * 2.根据request获取session 
         * 3.从session中取出登录用户信息 
         */
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        // 获取请求地址

        long startTimeMillis = System.currentTimeMillis();
        // result的值就是被拦截方法的返回值
        Object result = pjp.proceed();
        long endTimeMillis = System.currentTimeMillis();
        String requestPath = request.getRequestURI();
        log.info("requestPath:{},cost time:{}ms", requestPath, (endTimeMillis - startTimeMillis));

        if (AopModuleConstant.getUrlMap().containsKey(requestPath)) {
            // 从session中获取用户信息
            SysUser loginInfo = getUser();
            String userName;
            String realName;
            if (loginInfo != null) {
                userName = loginInfo.getUsername();
                realName = loginInfo.getRealName();
            } else {
                userName = "用户未登录";
                realName = "用户未登录";
            }
            SysLog sysLog = new SysLog();
            sysLog.setModuleUrl(requestPath);
            sysLog.setUserName(userName);
            String[] nameAndType = AopModuleConstant.getUrlMap().get(requestPath).split(",");
            sysLog.setModuleName(nameAndType[0]);
            sysLog.setActionType(Integer.parseInt(nameAndType[1]));
            sysLog.setRealName(realName);
            sysLog.setIpaddr(RequestUtil.getIpAddress(request));
            sysLog.setCreateTime(DateTimeUtils.now());
            sysLog.setC1((endTimeMillis - startTimeMillis) + "ms");
            sysLogService.save(sysLog);
        }

        return result;
    }
}
