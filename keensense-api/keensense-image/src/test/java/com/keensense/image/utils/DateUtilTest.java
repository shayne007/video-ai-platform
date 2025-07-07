package com.keensense.image.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

/**
 * @author fengsy
 * @date 7/5/21
 * @Description
 */
@Slf4j
public class DateUtilTest {

    @Test
    public void testGeneratorDate() throws ParseException {
        Date start = DateUtil.generatorDate("20210101140000", "start");
        Date end = DateUtil.generatorDate("20210101140000", "end");
        log.info(String.valueOf(start));
        log.info(String.valueOf(end));
    }
}
