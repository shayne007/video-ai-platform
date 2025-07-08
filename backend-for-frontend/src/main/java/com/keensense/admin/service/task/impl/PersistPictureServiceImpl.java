package com.keensense.admin.service.task.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keensense.admin.entity.task.PersistPicture;
import com.keensense.admin.mapper.task.PersistPictureMapper;
import com.keensense.admin.service.task.IPersistPictureService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import sun.net.www.protocol.ftp.FtpURLConnection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

@Slf4j
@Service("persistPictureService")
public class PersistPictureServiceImpl extends ServiceImpl<PersistPictureMapper, PersistPicture> implements IPersistPictureService {

    @Override
    public String savePicture(String data) throws Exception{
        String id = "";
        if (StringUtil.isNull(data)) {
            return id;
        } else {
            if (PatternUtil.isMatch(data, "^(http|ftp).*")) {
                data = downloadImage(data);
            }

            if (StringUtil.isNull(data)) {
                return "";
            } else {
                PersistPicture persistPicture = new PersistPicture();
                id = UUID.randomUUID().toString().replace("-", "");
                persistPicture.setId(id);
                persistPicture.setPictureData(data);
                baseMapper.insert(persistPicture);
            }
        }
        return id;
    }

    @Override
    public String getPictureStr(String url) throws Exception{
        if(StringUtil.isNull(url)) {
            return "";
        } else {
            String data = "";
            if(PatternUtil.isMatch(url, "^(http|ftp).*")) {
                data = downloadImage(url);
            } else {
                String[] matches = PatternUtil.getMatch(url, "^([0-9a-zA-Z-_]+)-([0-9A-Za-z]+)$");
                if(ArrayUtil.isNotNull(matches)) {
                    String id = matches[2];
                    PersistPicture map = baseMapper.selectById(id);
                    data = map.getPictureData();
                }
            }

            return data;
        }
    }

    private static String downloadImage(String path) throws Exception {
        byte[] by = downloadImageBy(path);
        return null == by ? "" : new String(Base64.encode(by));
    }

    private static byte[] downloadImageBy(String path) throws Exception {
        byte[] by = null;
        URLConnection conn = null;
        boolean var9 = false;

        HttpURLConnection conn1;
        FtpURLConnection conn2;
        label107:
        {
            try {
                var9 = true;
                URL url = new URL(path);
                conn = url.openConnection();
                conn.setConnectTimeout(3000);
                conn.setReadTimeout(10000);
                conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                InputStream inputStream = conn.getInputStream();
                if (null != inputStream) {
                    by = input2byte(inputStream);
                    var9 = false;
                } else {
                    var9 = false;
                }
                break label107;
            } catch (Exception var10) {
                var9 = false;
            } finally {
                if (var9) {
                    if (null != conn) {
                        if (conn instanceof HttpURLConnection) {

                        } else if (conn instanceof FtpURLConnection) {

                        }
                    }

                }
            }

            if (null != conn) {
                if (conn instanceof HttpURLConnection) {
                } else if (conn instanceof FtpURLConnection) {
                    conn2 = (FtpURLConnection) conn;
                    conn2.close();
                    return by;
                }

                return by;
            }

            return by;
        }

        if (null != conn) {
            if (conn instanceof HttpURLConnection) {
            } else if (conn instanceof FtpURLConnection) {
                conn2 = (FtpURLConnection) conn;
                conn2.close();
            }
        }

        return by;
    }

    private static final byte[] input2byte(InputStream inStream) throws Exception {
        try {
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[100];
            boolean var3 = false;

            int rc;
            while ((rc = inStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }

            byte[] in2b = swapStream.toByteArray();
            IOUtils.closeQuietly(swapStream);
            IOUtils.closeQuietly(inStream);
            return in2b;
        } catch (IOException var5) {
            throw var5;
        }
    }
}
