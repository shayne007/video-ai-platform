package com.keensense.alarm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.alarm.dto.Disposition;
import com.keensense.alarm.dto.WrapSubImageInfo;
import com.keensense.alarm.entity.DispositionEntity;
import com.keensense.alarm.entity.DispositionNotificationEntity;
import com.keensense.alarm.entity.SubImageInfoEntity;
import com.keensense.alarm.mapper.DispositionMapper;
import com.keensense.alarm.mapper.DispositionNotificationMapper;
import com.keensense.alarm.service.IDispositionService;
import com.keensense.alarm.service.ISubImageInfoService;
import com.keensense.common.util.IDUtil;
import com.keensense.common.util.Messenger;
import com.keensense.common.util.ParamProcessUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 布控对象 服务实现类
 * </p>
 *
 * @author ycl
 * @since 2019-05-14
 */
@Service
public class DispositionServiceImpl extends ServiceImpl<DispositionMapper, DispositionEntity> implements IDispositionService {

    @Resource
    private ISubImageInfoService subImageInfoService;

    @Resource
    private DispositionNotificationMapper notificationMapper;

    @Override
    public boolean addDispositions(List<Disposition> dispositions) {
        //图片跟告警对象集合
        List<DispositionEntity> disps = new ArrayList<>();
        List<SubImageInfoEntity> subImageInfos = new ArrayList<>();
        for (Disposition dto : dispositions) {
            DispositionEntity disp = new DispositionEntity();
            BeanUtils.copyProperties(dto, disp);
            if (disp.getBeginTime() == null) {
                disp.setBeginTime(LocalDateTime.now().minusMinutes(1L));
            }
            if (disp.getEndTime() == null) {
                disp.setEndTime(LocalDateTime.now().plusDays(1L));
            }
            disps.add(disp);
            //布控图片列表
            List<SubImageInfoEntity> subImageList = Optional.ofNullable(dto.getSubImageList())
                    .map(WrapSubImageInfo::getSubImageObject)
                    .orElse(Collections.emptyList());
            subImageList.forEach(subImageInfo -> subImageInfo.setDispositionId(dto.getDispositionId()));
            subImageInfos.addAll(subImageList);
            dispStatus(disp);
        }
        boolean dis = this.saveOrUpdateBatch(disps);
        boolean img = true;
        if (!subImageInfos.isEmpty()) {
            img = subImageInfoService.saveOrUpdateBatch(subImageInfos);
        }

        return dis && img;
    }

    @Override
    public boolean removeDispositions(String ids) {
        String[] idArr = ids.split(",");
        boolean dis;
        dis = this.removeByIds(Arrays.asList(idArr));
        for (String id : idArr) {
            notificationMapper.delete(new QueryWrapper<DispositionNotificationEntity>().eq("disposition_id", id));
        }
        subImageInfoService.remove(Wrappers.<SubImageInfoEntity>lambdaQuery()
                .in(SubImageInfoEntity::getDispositionId, idArr));

        return dis;
    }

    @Override
    public List<Disposition> findDispositionByParam(String param) {

        //解析url参数
        List<ParamProcessUtil.QueryParam> queryList = ParamProcessUtil.getQueryList(param);
        Page<DispositionEntity> queryPage = ParamProcessUtil.getQueryPage(queryList);
        QueryWrapper<DispositionEntity> wrapper = ParamProcessUtil.getWrapper(queryList);
        //查询布控主表
        IPage<DispositionEntity> iPage = baseMapper.selectPage(queryPage, wrapper);

        List<DispositionEntity> records = iPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return Collections.emptyList();
        }
        Messenger.sendMsg(String.valueOf(iPage.getTotal()));
        //查询图像列表子表
        Object[] ids = records.stream().map(DispositionEntity::getDispositionId).toArray();
        List<SubImageInfoEntity> images = subImageInfoService.list(Wrappers.<SubImageInfoEntity>lambdaQuery()
                .in(SubImageInfoEntity::getDispositionId, ids));
        //dto组装
        Map<String, List<SubImageInfoEntity>> collect = images.stream().collect(
                Collectors.groupingBy(SubImageInfoEntity::getDispositionId));
        List<Disposition> dispositions = new ArrayList<>();
        for (DispositionEntity entity : records) {
            Disposition disposition = new Disposition();
            BeanUtils.copyProperties(entity, disposition);
            disposition.setSubImageList(new WrapSubImageInfo(collect.get(disposition.getDispositionId())));
            IPage<DispositionNotificationEntity> notificationPage = notificationMapper.selectPage(new Page<>(0, 1),
                    Wrappers.<DispositionNotificationEntity>lambdaQuery()
                            .eq(DispositionNotificationEntity::getDispositionId, entity.getDispositionId()));
            disposition.setNotificationCount(notificationPage.getTotal());
            dispositions.add(disposition);
        }
        return dispositions;
    }


    private void dispStatus(DispositionEntity disp) {
        if (disp.getBeginTime() == null || disp.getEndTime() == null) {
            return;
        }
        if (StringUtils.isEmpty(disp.getDispositionId())) {
            disp.setDispositionId(IDUtil.randomUUID());
        }
        LocalDateTime now = LocalDateTime.now();
        if (disp.getBeginTime().isAfter(now)) {
            //布控未开始
            disp.setDispositionStatus(9);
        } else if (disp.getEndTime().isBefore(now)) {
            //布控已结束
            disp.setDispositionStatus(2);
        } else {
            //布控当中
            disp.setDispositionStatus(1);
        }
        if (disp.getOperateType() == null) {
            disp.setOperateType(false);
        }
        //是否撤控
        if (!disp.getOperateType()) {
            disp.setDispositionStatus(0);
        }
    }

}
