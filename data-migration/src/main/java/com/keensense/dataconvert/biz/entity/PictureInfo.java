package com.keensense.dataconvert.biz.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName：PictureInfo
 * @Description： <p> PictureInfo </p>
 * @Author： - Jason
 * @CreatTime：2019/7/25 - 13:51
 * @Modify By：
 * @ModifyTime： 2019/7/25
 * @Modify marker：
 * @version V1.0
*/
public class PictureInfo implements Serializable {

    private static final long serialVersionUID = 7888375500786807284L;

    /**
     * 初始状态
     */
	public static final int STATUS_INIT = 0;

    /**
     * 下载中
     */
	public static final int STATUS_DOWNLOADING = 1;

    /**
     * 下载成功
     */
	public static final int STATUS_DOANLOAD_SUCC = 2;

    /**
     * 下载失败
     */
	public static final int STATUS_DOWNLOAD_FAIL = 3;

    /**
     * 解码中
     */
	public static final int STATUS_TRANSCODE_ING = 4;

    /**
     *解码成功
     */
	public static final int STATUS_TRANSCODE_SUCC = 5;

    /**
     *解码失败
     */
	public static final int STATUS_TRANSCODE_FAIL = 6;

    /**
     *识别中
     */
	public static final int STATUS_RECOG_ING = 7;

    /**
     * 识别成功
     */
	public static final int STATUS_RECOG_SUCC = 8;

    /**
     *识别失败
     */
	public static final int STATUS_RECOG_FAIL = 9;

    /**
     * 识别结果推送中
     */
	public static final int STATUS_RESULTPUSH_ING = 10;

    /**
     * 结果推送成功
     */
	public static final int STATUS_RESULTPUSH_SUCC = 11;

    /**
     * 结果推送失败
     */
	public static final int STATUS_RESULTPUSH_FAIL = 12;

    /**
     * 错误
     */
	public static final int STATUS_ERROR = 99;

    /**
     * id
     */
    private Long id;

    /**
     * picUrl 图片url
     */
    private String picUrl;

    /**
     * downloadTime
     */
    private Date downloadTime;

    /**
     * createTime
     */
    private Date createTime = new Date();

    /**
     * finishTime
     */
    private Date finishTime;

    /**
     * downloadCost
     */
    private int downloadCost = 0;

    /**
     * status
     */
    private int status = STATUS_INIT;

    /**
     * picBase64
     */
    private String picBase64;

    /**
     * retryDownloadCount 下载重试次数
     */
    private int retryDownloadCount = 0;

    /**
     * retryRecogCount - 重试次数 默认为 - 0
     */
    private int retryRecogCount = 0;

    /**
     * results
     */
    private List<FeatureResult> results = new ArrayList<>();

    /**
     * extendId - 扩展id = [现场设备id?]
     */
	private String extendId = "";

    /**
     * interestRegion 感兴趣区域
     */
	private String interestRegion = "";

    /**
     * returnUrl
     */
	private String returnUrl = "";

    /**
     * uuid
     */
    private String recogId;

    /**
     * serialNumber
     */
    private String serialNumber;

    /**
     * startFramePts
     */
    private String startFramePts;

    /**
     * objType 1 ＝ ⼈人 ｜ 2 ＝⾞車車 ｜ 3 ＝⼈人臉 ｜ 4 ＝⼈人騎⾞車車
     */
    private int objType;

    /**
     * creatTime - 源头数据时间
     */
    private Date sourceCreatTime;

    /**
     * 需要传递的数据
     */
    private Long frameIndex;

    public Long getFrameIndex() {
        return frameIndex;
    }

    public void setFrameIndex(Long frameIndex) {
        this.frameIndex = frameIndex;
    }

    // 需要冗余的数据
    //uuid
    //objType
    //serialNumber
    //creatTime
    //firstObj
    //startFramePts
    //features.featureData


    public int getObjType() {
        return objType;
    }

    public void setObjType(int objType) {
        this.objType = objType;
    }

    public String getRecogId() {
        return recogId;
    }

    public void setRecogId(String recogId) {
        this.recogId = recogId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getStartFramePts() {
        return startFramePts;
    }

    public void setStartFramePts(String startFramePts) {
        this.startFramePts = startFramePts;
    }

    public Date getSourceCreatTime() {
        return sourceCreatTime;
    }

    public void setSourceCreatTime(Date sourceCreatTime) {
        this.sourceCreatTime = sourceCreatTime;
    }

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public Date getDownloadTime() {
		return downloadTime;
	}

	public void setDownloadTime(Date downloadTime) {
		this.downloadTime = downloadTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getPicBase64() {
		return picBase64;
	}

	public void setPicBase64(String picBase64) {
		this.picBase64 = picBase64;
	}

    public List<FeatureResult> getResults() {
        return results;
    }

    public void setResults(List<FeatureResult> results) {
        this.results = results;
    }

    public int getRetryDownloadCount() {
		return retryDownloadCount;
	}

	public void setRetryDownloadCount(int retryDownloadCount) {
		this.retryDownloadCount = retryDownloadCount;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public int getDownloadCost() {
		return downloadCost;
	}

	public void setDownloadCost(int downloadCost) {
		this.downloadCost = downloadCost;
	}

	public int getRetryRecogCount() {
		return retryRecogCount;
	}

	public void setRetryRecogCount(int retryRecogCount) {
		this.retryRecogCount = retryRecogCount;
	}

	public String getExtendId() {
		return extendId;
	}

	public void setExtendId(String extendId) {
		this.extendId = extendId;
	}

	public String getInterestRegion() {
		return interestRegion;
	}

	public void setInterestRegion(String interestRegion) {
		this.interestRegion = interestRegion;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

}
