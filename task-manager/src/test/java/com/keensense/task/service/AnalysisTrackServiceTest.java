package com.keensense.task.service;

import com.keensense.task.entity.CameraAnalysisTrack;
import com.keensense.task.mapper.CameraAnalysisTrackHisMapper;
import com.keensense.task.mapper.CameraAnalysisTrackMapper;
import com.keensense.task.mapper.TbAnalysisTaskMapper;
import com.keensense.task.service.impl.AnalysisTrackServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author fengsy
 * @date 6/12/20
 * @Description
 */
@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class AnalysisTrackServiceTest {
    @Mock
    private CameraAnalysisTrackMapper cameraAnalysisTrackMapper;

    @InjectMocks
    @Spy
    private AnalysisTrackServiceImpl analysisTrackServiceImpl;

    @Test
    public void testTransferToJson() throws Exception {
        List<CameraAnalysisTrack> cameraAnalysisTracks = new ArrayList<>();
        cameraAnalysisTracks.add(new CameraAnalysisTrack());
        assertThat(analysisTrackServiceImpl.transferToJson(cameraAnalysisTracks).getBoolean("")).isNull();
    }
}
