package com.keensense.task.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Pattern;

/**
 * @ClassName: CleanDataController
 * @Description: 数据清理接口
 * @Author: cuiss
 * @CreateDate: 2020/1/10 16:35
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@Api(value = "数据清理")
@RestController
@RequestMapping("/rest/cleanData")
@Slf4j
public class CleanDataController extends  BaseController{

    private static final String CLEAN_OK = "clean success";

    private static final String CLEAN_ERROR="clean error";

    private static final String DEFAULT_START="20000101";

    private static final String PATTERN = "\\d{4}\\d{2}\\d{2}";

    private static final String BASE_URL = "/rest/cleanData";

    /**
     * 删除
     * @param end
     * @return
     */
    @ApiOperation("数据清理")
    @GetMapping("/{end}")
    public String cleanData(@PathVariable("end") String end){
        log.info("clean data end="+end);
        if(StringUtils.isEmpty(end) || !Pattern.matches(PATTERN,end)){
            return "invilid params";
        }
        return cleanData(DEFAULT_START,end);
    }
    /**
     *
     * @param start 开始时间
     * @param end  结束时间
     * @return
     */
    @ApiOperation("数据清理")
    @GetMapping("/{start}/{end}")
    public String cleanData(@PathVariable("start") String start,@PathVariable("end") String end){
        log.info("clean data start="+start+",end="+end);
        if(StringUtils.isEmpty(start) || StringUtils.isEmpty(end) || !Pattern.matches(PATTERN,start) || !Pattern.matches(PATTERN,end)){
            return "invilid params";
        }

        return CLEAN_OK;
    }

}
