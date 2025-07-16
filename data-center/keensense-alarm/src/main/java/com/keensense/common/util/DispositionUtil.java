package com.keensense.common.util;

import com.google.common.collect.Lists;
import com.keensense.alarm.entity.DispositionEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

/**
 * @author ycl
 * @date 2020/3/26
 */
public class DispositionUtil {


  public static List<DispositionEntity> data = new ArrayList<>();


  public static void emit(List<DispositionEntity> data) {
    DispositionUtil.data = data;
  }


  public static List<DispositionEntity> list(String deviceId, Integer... category) {
    return data.stream()
        .filter(disp -> Lists.newArrayList(category).contains(disp.getDispositionCategory()))
        .filter(disp -> StringUtils.isEmpty(disp.getTollgateList()) || disp.getTollgateList().contains(deviceId)).collect(Collectors.toList());
  }

  public static List<DispositionEntity> list(boolean searchByLib, Integer... category) {
    if (searchByLib) {
      return data.stream().filter(disp -> StringUtils.isNotEmpty(disp.getRegIds()))
          .filter(disp -> Lists.newArrayList(category).contains(disp.getDispositionCategory()))
          .collect(Collectors.toList());
    }
    return data.stream().filter(disp -> StringUtils.isNotEmpty(disp.getTargetImageUri()))
        .filter(disp -> Lists.newArrayList(category).contains(disp.getDispositionCategory()))
        .collect(Collectors.toList());
  }

  public static DispositionEntity getDispById(String dispId) {
    for (DispositionEntity disp : data) {
      if (disp.getDispositionId().equals(dispId)) {
        return disp;
      }
    }
    return null;

  }

}
