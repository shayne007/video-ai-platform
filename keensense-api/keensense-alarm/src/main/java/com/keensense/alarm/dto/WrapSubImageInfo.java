package com.keensense.alarm.dto;

import com.keensense.alarm.entity.SubImageInfoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author ycl
 * @date 2019/5/20
 */
@Data
@AllArgsConstructor
public class WrapSubImageInfo {
    private List<SubImageInfoEntity> subImageObject;

}
