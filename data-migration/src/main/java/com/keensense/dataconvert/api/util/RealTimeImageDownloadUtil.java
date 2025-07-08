package com.keensense.dataconvert.api.util;

import com.keensense.dataconvert.api.request.queue.DownloadImageQueue;
import com.keensense.dataconvert.api.request.queue.ImageDownloadClientQueue;
import com.keensense.dataconvert.biz.common.consts.CommonConst;
import com.keensense.dataconvert.biz.entity.PictureInfo;
import com.loocme.security.encrypt.Base64;
import com.loocme.sys.util.PatternUtil;
import com.loocme.sys.util.StringUtil;
import net.sf.ehcache.util.NamedThreadFactory;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * @projectName：keensense-u2s
 * @Package：com.keensense.dataconvert.api.util
 * @Description： <p> RealTimeImageDownloadUtil  </p>
 * @Author： - Jason
 * @CreatTime：2019/7/25 - 14:12
 * @Modify By：
 * @ModifyTime： 2019/7/25
 * @Modify marker：
 */
public class RealTimeImageDownloadUtil {

    private static final Logger logger = LoggerFactory.getLogger(RealTimeImageDownloadUtil.class);

    /**
     * name 线程区分
     */
    public static String REAL_TIME_DOWN_LOAD_IMAGE = "realtime-download";

    /**
     * 是否为图片url前缀
     */
    public static String PATTERN_IMG_URL_PREFFIX = "^(http|ftp).*";

    /**
     * 实时图片下载多线程
     */
    private final static ExecutorService realTimeImageDownloadImageExec = newFixedThreadPool(CommonConst.DOWNLOAD_IMAGE_THREAD_SIZE, new NamedThreadFactory(REAL_TIME_DOWN_LOAD_IMAGE));

    /**
     * downloadResource 下载资源
     * @param picInfo
     */
    public static void downloadResource(final PictureInfo picInfo) {
        if (StringUtil.isNull(picInfo.getPicUrl())) {
            picInfo.setStatus(PictureInfo.STATUS_DOWNLOAD_FAIL);
            logger.info("=== 图片Url不存在-[extendId] {} download picture fail. the picture url is empty.",picInfo.getExtendId());
            return;
        }
        if (PictureInfo.STATUS_INIT != picInfo.getStatus()) {
            return;
        }
        picInfo.setStatus(PictureInfo.STATUS_DOWNLOADING);
        if (PatternUtil.isMatch(picInfo.getPicUrl(), PATTERN_IMG_URL_PREFFIX)) {
            // ImageDownloadClientQueue ---> DownloadImageQueue
            Integer index = ImageDownloadClientQueue.takeIndex();
            realTimeImageDownloadImageExec.execute(
                    new Runnable() {
                        @Override
                        public void run() {
                            /**
                             * 下载图片数据
                             */
                            downloadImage(index, picInfo);
                            ImageDownloadClientQueue.putIndex(index);

                            try {
                                Thread.sleep(new Random().nextInt(10) * 100);
                            } catch (InterruptedException e) {
                                logger.error("=== InterruptedException:error:[{}] ===",e.getMessage());
                            }
                        }

                    }
            );
        }
    }

    /**
     * static 初始化
     */
    static {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        /**
         * 创建SSLContext对象，并使用我们指定的信任管理器初始化
         */
        TrustManager[] tm = { new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
        } };
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tm, new java.security.SecureRandom());
            /**
             * 从上述SSLContext对象中得到SSLSocketFactory对象
             */
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            HttpsURLConnection.setDefaultSSLSocketFactory(ssf);
        } catch (KeyManagementException e) {
            logger.error("=== KeyManagementException:{} ===",e);
        } catch (NoSuchAlgorithmException e) {
            logger.error("=== NoSuchAlgorithmException:{} ===",e);
        }
    }

    /**
     * 下载图片 - 然后将Base64 信息放入DownloadImageQueue
     * @param index
     * @param picInfo
     */
    private static void downloadImage(int index, PictureInfo picInfo) {
        String respStr = null;
        String errorMsg = null;
        URLConnection conn = null;
        String picUrl ="";
        try {
            picUrl = picInfo.getPicUrl();
            logger.info("=== Start download picture [{}] ===",picUrl);
            URL url = new URL(picUrl);
            conn = url.openConnection();
            /**
             * 设置超时间为3秒
             */
            conn.setConnectTimeout(CommonConst.DOWNLOAD_CONNECT_TIME_OUT * 1000);
            conn.setReadTimeout(CommonConst.DOWNLOAD_READ_TIMEOUT * 1000);
            /**
             * 防止屏蔽程序抓取而返回403错误
             */
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            InputStream inputStream = conn.getInputStream();
            if (null != inputStream) {
                byte[] by = input2byte(inputStream);
                respStr = new String(Base64.encode(by));
            }
        } catch (Exception e) {
            logger.error("=== 下载图片[Url:{}],出现异常:{} === ",picUrl,e);
            errorMsg = e.getMessage();
        } finally {
            IOUtils.close(conn);
        }

        if (StringUtil.isNotNull(respStr)) {
            picInfo.setPicBase64(respStr);
            picInfo.setStatus(PictureInfo.STATUS_DOANLOAD_SUCC);
            picInfo.setDownloadTime(new Date());
            logger.debug("=== Download Picture & Convert to Base64 Success ...");
            DownloadImageQueue.putInfo(picInfo);
        } else {
            int downloadCount = picInfo.getRetryDownloadCount() + 1;
            logger.info("=== {} download picture failed. download count is {} . continue. error message: {} === ",picInfo.getExtendId(), downloadCount, errorMsg);
            picInfo.setRetryDownloadCount(downloadCount);
        }
    }

    /**
     * input2byte
     * @param inStream
     * @return
     * @throws IOException
     */
    private static final byte[] input2byte(InputStream inStream) throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }

}
