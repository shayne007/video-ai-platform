package com.keensense.task.test;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: TestSwaggerController
 * @Author: cuiss
 * @CreateDate: 2019/5/7 14:31
 * @Version: 1.0
 * @Company: 长沙千视通智能科技有限公司
 */
@RestController
@Api("测试swagger")
@RequestMapping("/test")
public class TestSwaggerController {
    @ApiOperation(value = "计算+", notes = "加法")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "a", paramType = "path", value = "数字a", required = true, dataType = "Long",example = "1"),
            @ApiImplicitParam(name = "b", paramType = "path", value = "数字b", required = true, dataType = "Long",example = "1")
    })
    @GetMapping("/{a}/{b}")
    public Integer get(@PathVariable Integer a, @PathVariable Integer b) {
        return a + b;
    }
}
