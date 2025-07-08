package com.keensense.admin.service.task.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.task.ObjextTrack;
import com.keensense.admin.mapper.task.ObjextTrackMapper;
import com.keensense.admin.service.task.IObjextTrackService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service("objextTrackService")
public class ObjextTrackServiceImpl extends ServiceImpl<ObjextTrackMapper, ObjextTrack> implements IObjextTrackService {
    @Override
    public Set<String> queryObjextTrackResultidsBySerialnumber(String serialnumber) {
        Set<String> idsLst = null;
        List<ObjextTrack> objextTrackList = baseMapper.selectList(new QueryWrapper<ObjextTrack>().eq("serialnumber", serialnumber));
        if (objextTrackList != null && !objextTrackList.isEmpty()) {
            idsLst = new HashSet<>();
            for (ObjextTrack objextTrack : objextTrackList) {
                idsLst.add(String.valueOf(objextTrack.getResultid()));
            }
        }
        return idsLst;
    }

    @Override
    public List<Map<String, Object>> queryObjextTrackResultListBySerialnumber(String serialnumber) {
        return baseMapper.queryObjextTrackResultListBySerialnumber(serialnumber);
    }

    @Override
    public List<ObjextTrack> queryObjextTrackBySerialnumber(String serialnumber, Integer searchStatus) {
        return baseMapper.selectList(new QueryWrapper<ObjextTrack>().eq("serialnumber", serialnumber).and(searchStatus != null, i -> i.isNull(searchStatus != null, "search_status").or().eq("search_status", 0)));
    }
}
