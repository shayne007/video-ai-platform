package com.keensense.densecrowd.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keensense.common.platform.bo.video.CrowdDensity;
import com.keensense.common.platform.bo.video.CrowdDensityQuery;
import com.keensense.common.util.DateTime;
import com.keensense.common.util.DateUtil;
import com.keensense.densecrowd.entity.task.DensecrowdDailyMaxReport;
import com.keensense.densecrowd.entity.task.DensecrowdWarnResult;
import com.keensense.densecrowd.entity.task.VsdTaskRelation;
import com.keensense.densecrowd.service.ext.CrowdDensityService;
import com.keensense.densecrowd.service.task.IDensecrowdDailyMaxReportService;
import com.keensense.densecrowd.service.task.IDensecrowdWarnResultService;
import com.keensense.densecrowd.service.task.IVsdTaskRelationService;
import com.keensense.densecrowd.service.task.IVsdTaskService;
import com.keensense.densecrowd.util.DbPropUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author: zengyc
 * @Description: 描述该类概要功能介绍
 * @Date: Created in 19:39 2020/7/27
 * @Version v0.1
 */
@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@Slf4j
public class AlarmReportTask {

    @Autowired
    CrowdDensityService crowdDensityService;

    @Autowired
    IVsdTaskRelationService vsdTaskRelationService;

    @Autowired
    IDensecrowdDailyMaxReportService densecrowdDailyMaxReportService;

    @Autowired
    private IDensecrowdWarnResultService densecrowdWarnResultService;

    /**
     * 根据任务号查询最高人群密度
     */
    @Scheduled(cron = "0 30 0 * * ?")
    public void alarmRecord() throws Exception {
        Date date = DateTime.addTimeByDays(new Date(), -1);
        String reportDate = DateUtil.formatDate(date, "yyyyMMdd");
        List<DensecrowdDailyMaxReport> reportList = densecrowdDailyMaxReportService.list(new QueryWrapper<DensecrowdDailyMaxReport>().eq("report_date", reportDate));
        if (reportList.isEmpty()) {
            List<VsdTaskRelation> list = vsdTaskRelationService.list(new QueryWrapper<VsdTaskRelation>().eq("isvalid", "1"));
            for (VsdTaskRelation vsdTaskRelation : list) {
                CrowdDensityQuery crowdDensityQuery_0 = new CrowdDensityQuery();
                crowdDensityQuery_0.setPageNo(1);
                crowdDensityQuery_0.setPageSize(1);
                crowdDensityQuery_0.setOrderField("count");
                crowdDensityQuery_0.setOrderMethod("desc");
                crowdDensityQuery_0.setStartTime(reportDate + "000000");
                crowdDensityQuery_0.setEndTime(reportDate + "235959");
                crowdDensityQuery_0.setSerialnumber(vsdTaskRelation.getSerialnumber());
                Page<CrowdDensity> page = crowdDensityService.getDensityResultList(crowdDensityQuery_0);
                if (page.getTotal() > 0) {
                    List<CrowdDensity> records = page.getRecords();
                    DensecrowdDailyMaxReport densecrowdDailyMaxReport = new DensecrowdDailyMaxReport();
                    densecrowdDailyMaxReport.setInsertTime(new Date());
                    densecrowdDailyMaxReport.setMaxCount(records.get(0).getCount());
                    densecrowdDailyMaxReport.setReportDate(reportDate);
                    densecrowdDailyMaxReport.setSerialnumber(vsdTaskRelation.getSerialnumber());
                    densecrowdDailyMaxReport.setTotalCount(page.getTotal());
                    densecrowdDailyMaxReportService.save(densecrowdDailyMaxReport);
                }
            }
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void updateAlarm() throws Exception {
        String reportDate = DateUtil.formatDate(new Date(), "yyyyMMdd");
        int dynamicDay = DbPropUtil.getInt("task.dynamic_day", 7);
        float dynamicRate = DbPropUtil.getFloat("task.dynamic_rate", 1.1f);
        List<VsdTaskRelation> list = vsdTaskRelationService.list(new QueryWrapper<VsdTaskRelation>().eq("isvalid", "1"));
        for (VsdTaskRelation vs : list) {
            List<DensecrowdDailyMaxReport> reportList = densecrowdDailyMaxReportService.list(new QueryWrapper<DensecrowdDailyMaxReport>().lt("report_date", reportDate)
                    .eq("serialnumber", vs.getSerialnumber()).orderByDesc("report_date").last("limit " + dynamicDay));
            if (!reportList.isEmpty()) {
                int counts = 0;
                for (DensecrowdDailyMaxReport densecrowdDailyMaxReport : reportList) {
                    counts += densecrowdDailyMaxReport.getMaxCount();
                }
                int alarmThreshold = new BigDecimal(counts).multiply(new BigDecimal(dynamicRate)).divide(new BigDecimal(reportList.size())).intValue();
                vs.setAlarmThreshold(alarmThreshold);
                vsdTaskRelationService.updateById(vs);
            }
        }
    }

    //    @Scheduled(cron = "0 0 2 * * ?")
    @Scheduled(cron = "0 1/1 * * * ?")
    public void deleteAlarmRecord() throws Exception {
        int deleteAlarmRecord = DbPropUtil.getInt("task.delete_alarm_record", 30);
        if (deleteAlarmRecord <= 0) {
            deleteAlarmRecord = 30;
        }
        Date date = DateTime.addTimeByDays(new Date(), -deleteAlarmRecord);
        String reportDate = DateUtil.formatDate(date, "yyyyMMdd")+"235959";
        densecrowdWarnResultService.remove(new QueryWrapper<DensecrowdWarnResult>().lt("create_time", reportDate));
    }
}
