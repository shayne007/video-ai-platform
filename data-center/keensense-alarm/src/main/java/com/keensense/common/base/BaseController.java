package com.keensense.common.base;

import com.keensense.common.util.ApiResponseEnum;
import com.keensense.common.util.IDUtil;
import com.keensense.common.util.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author ycl
 * @date 2019/5/10
 */
@Slf4j
public class BaseController {

    protected HttpServletRequest getRequest() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        return ((ServletRequestAttributes) ra).getRequest();
    }

    protected HttpServletResponse getResponse(){
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        return ((ServletRequestAttributes) ra).getResponse();
    }

    protected ResponseStatus generateResponse(ApiResponseEnum apiResponseEnum) {
        return generateResponse(apiResponseEnum, IDUtil.randomUUID());
    }

    private ResponseStatus generateResponse(ApiResponseEnum apiResponseEnum,String id) {
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setId(id);
        responseStatus.setRequestURL(this.getRequest().getRequestURI());
        responseStatus.setLocalTime(LocalDateTime.now());
        responseStatus.setStatusCode(apiResponseEnum.getCode());
        responseStatus.setStatusString(apiResponseEnum.getMsg());
        return responseStatus;
    }

    protected List<ResponseStatus> generateResponseList(ApiResponseEnum apiResponseEnum) {
        return Collections.singletonList(generateResponse(apiResponseEnum));
    }

    private List<ResponseStatus> generateList(ApiResponseEnum apiResponseEnum,String[] ids) {
        List<ResponseStatus> responseStatusList = new ArrayList<>();
        for (String id : ids) {
            responseStatusList.add(this.generateResponse(apiResponseEnum, id));
        }
        return responseStatusList;
    }

    protected List<ResponseStatus> generateSuccessList(String[] ids) {
        return generateList(ApiResponseEnum.SUCCESS, ids);
    }
    protected List<ResponseStatus> generateFailList(String[] ids) {
        return generateList(ApiResponseEnum.FAIL, ids);
    }


    protected String getUrlParam() {
        try {
            String queryStr = this.getRequest().getQueryString();
            if (StringUtils.isEmpty(queryStr)) {
                return queryStr;
            }
            return URLDecoder.decode(queryStr,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("getUrlParam error",e);
            return "";
        }
    }

}
