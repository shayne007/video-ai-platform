package com.keensense.search.repository.origin;

import com.alibaba.fastjson.JSONObject;
import com.keensense.common.exception.VideoException;
import com.keensense.search.domain.Archive;
import com.keensense.search.feign.FeignToArchive;
import com.keensense.search.repository.DocumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * Created by zhanx xiaohui on 2019-05-09.
 */
@Repository
@Slf4j
public class OriginDocumentRepository implements DocumentRepository {
    @Autowired
    FeignToArchive feignToArchive;

    @Override
    public Archive getDocument(JSONObject object) {
        String result = feignToArchive.getDocument(object.toJSONString());
        if (StringUtils.isEmpty(result)) {
            throw new VideoException();
        }
        JSONObject response = JSONObject.parseObject(result);
        JSONObject archivesObject = response.getJSONObject("ArchivesObject");

        return archivesObject.toJavaObject(Archive.class);
    }
}
