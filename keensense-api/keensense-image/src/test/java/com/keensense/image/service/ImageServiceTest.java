package com.keensense.image.service;

import com.keensense.image.repository.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

/**
 * @author fengsy
 * @date 7/5/21
 * @Description
 */
@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class ImageServiceTest {
    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageService imageService;

    @Test
    public void testService() {
        when(imageRepository.saveToFileServer("imagebase64xxxxx", "jpg", "idxxxx"))
                .thenReturn("http://imagestore.xxx.jpg");

        String jsonstr = imageService.image("idxxxx", "imagebase64xxxxx", "jpg", "ImageUrl", "requestUrlxxx");

        log.info(jsonstr);
    }

}
