package com.keensense.common.config;

import com.alibaba.fastjson.JSON;
import com.keensense.common.util.DateUtil;
import com.keensense.common.util.ResponseStatus;
import com.keensense.common.util.ResponseStatusList;
import com.keensense.common.util.ResponseStatusTotal;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *   网关返回参数处理
 * @description:
 * @author: luowei
 * @createDate:2019年5月13日 下午4:47:17
 * @company:
 */
@ControllerAdvice
public class VideoResponseBodyAdvice implements ResponseBodyAdvice<Object> {

	@Override
	public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass,
			ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
		if (o instanceof ResponseStatusTotal) {
			ResponseStatusTotal total = (ResponseStatusTotal) o;
			ResponseStatusList responseStatusList = total.getResponseStatusList();
			List<ResponseStatus> responseStatus = (List<ResponseStatus>) responseStatusList.getResponseStatus();
			List<ResponseStatus> response = new ArrayList<ResponseStatus>();
			for (ResponseStatus info : responseStatus) {
				info.setRequestURL(String.valueOf(serverHttpRequest.getURI()));
				info.setLocalTime(DateUtil.formatDate(new Date(), "yyyyMMddHHmmss"));
				response.add(info);
			}
			responseStatusList.setResponseStatus(response);
			o = JSON.toJSON(total);
		} else if (o instanceof ResponseStatusList) {
			ResponseStatusList total = (ResponseStatusList) o;
			ResponseStatus responseStatus = (ResponseStatus) total.getResponseStatus();
			responseStatus.setRequestURL(String.valueOf(serverHttpRequest.getURI()));
			responseStatus.setLocalTime(DateUtil.formatDate(new Date(), "yyyyMMddHHmmss"));
			o = JSON.toJSON(total);
		}
		return o;
	}

	@Override
	public boolean supports(MethodParameter methodParameter, Class aClass) {
		if (methodParameter.getParameterType() == ResponseStatusTotal.class ||
				methodParameter.getParameterType() == ResponseStatusList.class) {
			return true;
		}
		return false;
	}
}
