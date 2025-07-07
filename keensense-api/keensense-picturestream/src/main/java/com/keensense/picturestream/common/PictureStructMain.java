package com.keensense.picturestream.common;

import cn.jiuling.plugin.extend.FaceConstant;
import cn.jiuling.plugin.extend.picrecog.FaceAppMain;
import cn.jiuling.plugin.extend.picrecog.entity.RecogFaceResult;
import com.keensense.common.config.SpringContext;
import com.keensense.picturestream.algorithm.IFaceStruct;
import com.keensense.picturestream.algorithm.IObjextStruct;
import com.keensense.picturestream.algorithm.IVlprStruct;
import com.keensense.picturestream.config.NacosConfig;
import com.keensense.picturestream.entity.PictureInfo;
import com.keensense.picturestream.util.FaceUtil;
import com.loocme.sys.datastruct.Var;
import com.loocme.sys.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

@Slf4j
public class PictureStructMain {

    private PictureStructMain() {
    }

    private static IObjextStruct objextStructImpl = null;
    private static IVlprStruct vlprStructImpl = null;
    private static IFaceStruct faceStructImpl = null;
    protected static ConcurrentLinkedQueue<PictureInfo> objextPictureQueue = new ConcurrentLinkedQueue<>();
    protected static ConcurrentLinkedQueue<PictureInfo> vlprPictureQueue = new ConcurrentLinkedQueue<>();
    protected static ConcurrentLinkedQueue<PictureInfo> facePictureQueue = new ConcurrentLinkedQueue<>();
    private static List<PictureInfo> objextPictureList = new ArrayList<>();
    private static List<PictureInfo> vlprPictureList = new ArrayList<>();
    private static List<PictureInfo> facePictureList = new ArrayList<>();

    private static NacosConfig nacosConfig = SpringContext.getBean(NacosConfig.class);

    public static void initAlgorithm(IObjextStruct objextImpl, IVlprStruct vlprImpl,
                                     IFaceStruct faceImpl) {
        objextStructImpl = objextImpl;
        vlprStructImpl = vlprImpl;
        faceStructImpl = faceImpl;
        initAlgorithm();
    }

    private static void initAlgorithm() {
        Var initVat = Var.newObject();
        initVat.set("objext_picture_recog.objext_url", nacosConfig.getObjextPictureRecogObjextUrl());
        initVat.set("objext_picture_recog.output.Face", 1);
        initVat.set("objext_picture_recog.output.SubClass", 1);
        objextStructImpl.init(initVat);
        initVat.clear();
        initVat.set("vlpr_further_recog.vlpr_url", nacosConfig.getVlprFurtherRecogVlprUrl());
        initVat.set("vlpr_further_recog.replace_feature", 2);
        vlprStructImpl.init(initVat);
        initVat.clear();
        initVat.set("face_qst_recog.face_url", nacosConfig.getFaceQstRecogFaceUrl());
        faceStructImpl.init(initVat);
    }

    public static void recogObjext(PictureInfo pictureInfo) {
        if (null == objextStructImpl || !checkPicture(pictureInfo)) {
            return;
        }

        if (PictureInfo.STATUS_DOWNLOAD_SUCC == pictureInfo.getStatus()) {
            pictureInfo.setStatus(PictureInfo.STATUS_RECOG_ING);

            if (1 == objextStructImpl.getBatchSize()) {
                objextStructImpl.recog(pictureInfo);
                return;
            } else {
                objextPictureList.add(pictureInfo);
            }

            if (objextStructImpl.getBatchSize() > objextPictureList.size() && DownloadImageQueue.isQueueNotNull(1)) {
                return;
            }
            objextStructImpl.recog(objextPictureList);
            Iterator<PictureInfo> it = objextPictureList.iterator();
            while (it.hasNext()) {
                PictureInfo pic = it.next();
                try {
                    boolean vlpr = false;
                    boolean face = false;
                    if (CollectionUtils.isEmpty(pic.getResults())) {
                        continue;
                    }
                    List<Var> results = pic.getResults();
                    for (Var var : results) {
                        int metadataType = var.getInt("Metadata.Type");
                        boolean faceFlag = metadataType - PictureInfo.OBJEXT_FACE == 0 ||
                                (metadataType - PictureInfo.OBJEXT_PERSON == 0 && null != var.get("Metadata.FaceBoundingBox"));
                        if (metadataType - PictureInfo.OBJEXT_VLPR == 0) {
                            vlpr = true;
                        } else if (faceFlag) {
                            face = true;
                        }
                    }
                    if (vlpr && pic.getRecogTypeList().contains(PictureInfo.RECOG_TYPE_THIRD_VLPR)) {
                        vlprStructImpl.recog(pic);
                    }
                    if (face && pic.getRecogTypeList().contains(PictureInfo.RECOG_TYPE_FACE)) {
                        getFaceVar(Arrays.asList(pic));
                    }
                } catch (Exception e) {
                    log.error("third recog error", e);
                } finally {
                    objextPictureQueue.offer(pic);
                    it.remove();
                }
            }
        }
    }

    public static void recogVlpr(PictureInfo pictureInfo) {
        if (null == vlprStructImpl || !checkPicture(pictureInfo)) {
            return;
        }

        if (PictureInfo.STATUS_DOWNLOAD_SUCC == pictureInfo.getStatus()) {
            pictureInfo.setStatus(PictureInfo.STATUS_RECOG_ING);
            //判断每批次处理图片数量
            if (1 == vlprStructImpl.getBatchSize()) {
                vlprStructImpl.recog(pictureInfo);
                return;
            } else {
                vlprPictureList.add(pictureInfo);
            }

            if (vlprStructImpl.getBatchSize() > vlprPictureList.size() && DownloadImageQueue.isQueueNotNull(2)) {
                return;
            }
            vlprStructImpl.recog(vlprPictureList);
            Iterator<PictureInfo> it = vlprPictureList.iterator();
            while (it.hasNext()) {
                PictureInfo pic = it.next();
                vlprPictureQueue.offer(pic);
                it.remove();
            }
        }
    }

    public static void recogFace(PictureInfo pictureInfo) {
        if (null == faceStructImpl || !checkPicture(pictureInfo)) {
            return;
        }

        if (PictureInfo.STATUS_DOWNLOAD_SUCC == pictureInfo.getStatus()) {
            pictureInfo.setStatus(PictureInfo.STATUS_RECOG_ING);

            if (1 == faceStructImpl.getBatchSize()) {
                getFaceVar(Arrays.asList(pictureInfo));
                return;
            } else {
                facePictureList.add(pictureInfo);
            }

            if (faceStructImpl.getBatchSize() > facePictureList.size() && DownloadImageQueue.isQueueNotNull(3)) {
                return;
            }
            getFaceVar(facePictureList);
            Iterator<PictureInfo> it = facePictureList.iterator();
            while (it.hasNext()) {
                PictureInfo pic = it.next();
                facePictureQueue.offer(pic);
                it.remove();
            }
        }
    }

    private static boolean checkPicture(PictureInfo pictureInfo) {
        return pictureInfo != null && StringUtil.isNotNull(pictureInfo.getPicBase64());
    }

    private static void getFaceVar(List<PictureInfo> pictureInfos) {
        FaceAppMain faceApp = FaceUtil.getFaceAppMain();
        pictureInfos.parallelStream().forEach(pictureInfo -> {
            if (pictureInfo.getPictureType() - PictureInfo.IMAGE_SEGMENTATION == 0) {

                RecogFaceResult recogFaceResult = faceApp.recogOne(FaceConstant.TYPE_DETECT_MODE_SMALL, pictureInfo.getPicBase64());
                if (recogFaceResult != null) {
                    pictureInfo.addResult(getFaceResult(recogFaceResult));
                }
            } else {
                RecogFaceResult[] recogFaceResults = faceApp.recog(FaceConstant.TYPE_DETECT_MODE_BIG, pictureInfo.getPicBase64());
                for (RecogFaceResult recogFaceResult : recogFaceResults) {
                    if (recogFaceResult != null) {
                        pictureInfo.addResult(getFaceResult(recogFaceResult));
                    }
                }
            }
        });

    }

    private static Var getFaceResult(RecogFaceResult recogFaceResult) {
        Var var = Var.newObject();
        var.set("AlgoSource", PictureInfo.RECOG_TYPE_FACE);
        var.set("blurry", recogFaceResult.getBlurry());
        var.set("roll", recogFaceResult.getRoll());
        var.set("yaw", recogFaceResult.getYaw());
        var.set("pitch", recogFaceResult.getPitch());
        var.set("quality", recogFaceResult.getQuality());
        var.set("feature", recogFaceResult.getFeature());
        var.set("rect", recogFaceResult.getRect());
        var.set("glass", recogFaceResult.getGlass());
        var.set("box.x", recogFaceResult.getPointx());
        var.set("box.y", recogFaceResult.getPointy());
        var.set("box.w", recogFaceResult.getWidth());
        var.set("box.h", recogFaceResult.getHeight());
        return var;
    }
}
