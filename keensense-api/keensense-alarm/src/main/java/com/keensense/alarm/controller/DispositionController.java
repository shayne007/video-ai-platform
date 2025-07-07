package com.keensense.alarm.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.keensense.alarm.dto.Disposition;
import com.keensense.alarm.entity.DispositionEntity;
import com.keensense.alarm.service.IDispositionService;
import com.keensense.common.annotation.VIID;
import com.keensense.common.base.BaseController;
import com.keensense.common.util.ApiResponseEnum;
import com.keensense.common.util.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 布控对象 前端控制器
 * </p>
 *
 * @author ycl
 * @since 2019-05-14
 */
@RestController
@RequestMapping("/VIID/Dispositions")
@VIID
@Slf4j
public class DispositionController extends BaseController {

    @Autowired
    private IDispositionService dispositionService;


    @PostMapping
    public List<ResponseStatus> addDispositions(@RequestBody List<Disposition> dispositions) {
        boolean result = dispositionService.addDispositions(dispositions);
        String[] dispositionsIds = extractIds(dispositions);
        log.info("addDispositions result:{}, ids:{}", result, dispositionsIds);
        return result ? generateSuccessList(dispositionsIds) : generateFailList(dispositionsIds);
    }

    private String[] extractIds(List<Disposition> dispositions) {
        return dispositions.stream().map(Disposition::getDispositionId).toArray(String[]::new);
    }

    @PutMapping
    public List<ResponseStatus> updateDispositions(@RequestBody List<Disposition> dispositions) {
        return addDispositions(dispositions);
    }


    @DeleteMapping
    public List<ResponseStatus> delDispositions(@RequestParam("IDList") String ids) {
        boolean result = dispositionService.removeDispositions(ids);
        String[] delIds = ids.split(",");
        return result ? generateSuccessList(delIds) : generateFailList(delIds);
    }

    @PutMapping("/{id}")
    public ResponseStatus updateDispositions(@RequestBody Disposition disposition, @PathVariable("id") String id) {
        DispositionEntity disp = new DispositionEntity();
        BeanUtils.copyProperties(disposition, disp);
        disp.setOperateType(false);
        disp.setDispositionStatus(0);
        boolean update = dispositionService.update(disp, Wrappers.<DispositionEntity>lambdaUpdate().eq
                (DispositionEntity::getDispositionId, id));
        return generateResponse(update ? ApiResponseEnum.SUCCESS : ApiResponseEnum.FAIL);
    }

    @GetMapping
    public List<Disposition> listDispositions() {
        return dispositionService.findDispositionByParam(super.getUrlParam());
    }

}
