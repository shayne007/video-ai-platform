package com.keensense.admin.util;

import cn.jiuling.plugin.config.vasclient.ClientSocket;
import cn.jiuling.plugin.config.vasclient.IVasFrameDeal;
import cn.jiuling.plugin.config.vasclient.entity.StartRealPlayInfo;
import com.loocme.sys.util.ListUtil;
import com.loocme.sys.util.PatternUtil;
import com.loocme.sys.util.StringUtil;
import org.apache.commons.codec.binary.Base64;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

public class SnapshotUtil {

    public static String catSnapshotBase64(String url) {
        return Base64.encodeBase64String(catSnapshot(url));
    }

    public static byte[] catSnapshot(String url) {
        return catSnapshot(url, 5);
    }

    public static byte[] catSnapshot(String url, int frameIdx) {
        if (PatternUtil.isMatch(url, "^rtsp://.*$", Pattern.CASE_INSENSITIVE)) {
            return catRtspSnapshot(url, frameIdx);
        } else if (PatternUtil.isMatch(url, "^vas://.*$", Pattern.CASE_INSENSITIVE)) {
            return catVasSnapshot(url, frameIdx);
        }
        return null;
    }

    private static byte[] catRtspSnapshot(String url, int frameIdx) {
        byte[] picBy = null;
        try {
            FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(url);
            grabber.start();
            int frameCnt = 0;
            while (true) {
                Frame frame = grabber.grabFrame();
                if (frame == null || frame.image ==null){
                    continue;
                }
                frameCnt++;
                if (frameCnt < frameIdx){
                    continue;
                }
                Java2DFrameConverter converter = new Java2DFrameConverter();
                BufferedImage bufferedImage = converter.getBufferedImage(frame);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                boolean flag = ImageIO.write(bufferedImage, "jpg", out);
                if (flag) {
                    picBy = out.toByteArray();
                }
                break;
            }
            grabber.stop();
            grabber.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return picBy;
    }

    private static byte[] catVasSnapshot(String url, int frameIdx) {
        byte[] picBy = null;

        String[] vasInfos = PatternUtil.getMatch(url, "vas://name=([^&]*)&psw=([^&]*)&srvip=([^&]*)&srvport=([^&]*)&devid=([^&]*)&");
        if (null == vasInfos || StringUtil.isNull(vasInfos[3]) || StringUtil.isNull(vasInfos[4]) || StringUtil.isNull(vasInfos[5]))
            return null;

        ClientSocket vasSocket = ClientSocket.getNewInstance(vasInfos[3], StringUtil.getInteger(vasInfos[4]), vasInfos[1], vasInfos[2]);
        final AtomicLong startTime = new AtomicLong(0);
        long beginTime = System.currentTimeMillis();
        final List<byte[]> dataByList = new ArrayList<byte[]>();
        StartRealPlayInfo startRealPlayInfo = vasSocket.startRealPlay(vasInfos[5], new IVasFrameDeal() {
            @Override
            public int dealFrame(byte[] bytes) {
                if (0 == startTime.get()) {
                    startTime.set(System.currentTimeMillis());
                }
                dataByList.add(bytes);
                return 0;
            }
        });
        while (true) {
            long nowTime = System.currentTimeMillis();
            if ((startTime.get()>0 && (nowTime - startTime.get()) > 2 * 1000) || (nowTime - beginTime > 20 * 1000)){
                break;
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        startRealPlayInfo.stopRealPlay();
        vasSocket.disconnect();

        if (ListUtil.isNull(dataByList)){
            return null;
        }

        int framesLen = 0;
        for (int i = 0; i < dataByList.size(); i++) {
            framesLen += dataByList.get(i).length;
        }
        byte[] framesBy = new byte[framesLen];
        framesLen = 0;
        for (int i = 0; i < dataByList.size(); i++) {
            System.arraycopy(dataByList.get(i), 0, framesBy, framesLen, dataByList.get(i).length);
            framesLen += dataByList.get(i).length;
        }

        try {
            InputStream inputStream = new ByteArrayInputStream(framesBy);
            FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputStream);
            //OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
            grabber.start();
            Frame frame = null;
            int cnt = 0, frameLen = grabber.getFrameNumber();
            while (true) {
                Frame tmpframe = grabber.grabFrame();
                if ( tmpframe ==null || tmpframe.image == null){
                    continue;
                }
                if(tmpframe.keyFrame){
                    frame = tmpframe;
                    break;
                }
            }

            if (null != frame) {
                Java2DFrameConverter converter = new Java2DFrameConverter();
                BufferedImage bufferedImage = converter.getBufferedImage(frame);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                boolean flag = ImageIO.write(bufferedImage, "jpg", out);
                if (flag) {
                    picBy = out.toByteArray();
                }
            }

            grabber.stop();
            grabber.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return picBy;
    }

    /*public static void main(String[] args) {
        byte[] picBy = catSnapshot("rtsp://admin:jiuling.cn@172.16.1.167:554/h264/ch1/main/av_stream");

        if (null != picBy) {
            FileUtil.write(new File("D:\\test\\bbb.jpg"), picBy);
        }
    }*/
}
