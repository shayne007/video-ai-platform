package com.keensense.search.utils;


import com.keensense.search.exception.HttpRequestException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * Created by memory_fu on 2019/6/11.
 */
@Slf4j
public class HttpUtil {

    private HttpUtil() {
    }

    // post请求
    public static final String HTTP_POST = "POST";
    // get请求
    public static final String HTTP_GET = "GET";
    // utf-8字符编码
    public static final String CHARSET_UTF_8 = "UTF-8";
    // HTTP内容类型
    public static final String CONTENT_TYPE_TEXT_HTML = "text/xml";
    // HTTP内容类型
    private static final String CONTENT_TYPE = "application/json";
    // 请求超时时间
    public static final int SEND_REQUEST_TIME_OUT = 20000;
    // 将读超时时间
    public static final int READ_TIME_OUT = 20000;

    /**
     * @param requestType    请求类型
     * @param urlStr        请求地址
     * @param body        请求发送内容
     * @return 返回内容
     */
    public static String requestMethod(String requestType, String urlStr, String body, String auth)
        throws IOException {
        boolean isDoInput = false;
        if (body != null && body.length() > 0) {
            isDoInput = true;
        }
        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();
        String tempLine = null;
        URL url = new URL(urlStr);
        URLConnection urlConnection = url.openConnection();
        HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
        if (isDoInput) {
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(body.length()));
            if (!StringUtils.isEmpty(auth)) {
                httpURLConnection.setRequestProperty("Authorization", auth);
            }
        }
        httpURLConnection.setDoInput(true);
        httpURLConnection.setConnectTimeout(SEND_REQUEST_TIME_OUT);
        httpURLConnection.setReadTimeout(READ_TIME_OUT);
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setRequestProperty("Accept-Charset", CHARSET_UTF_8);
        httpURLConnection.setRequestProperty("Content-Type", CONTENT_TYPE);
        httpURLConnection.setRequestMethod(requestType);
        httpURLConnection.connect();
        if (isDoInput) {
            outputStream = httpURLConnection.getOutputStream();
            outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(body);
            outputStreamWriter.flush();// 刷新
        }
        if (httpURLConnection.getResponseCode() >= 300) {
            throw new HttpRequestException(
                "http error code is :" + httpURLConnection.getResponseCode());
        }
        if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, CHARSET_UTF_8);
            reader = new BufferedReader(inputStreamReader);

            while ((tempLine = reader.readLine()) != null) {
                stringBuilder.append(tempLine);
            }
        }
        if (outputStreamWriter != null) {
            outputStreamWriter.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
        if (reader != null) {
            reader.close();
        }
        if (inputStreamReader != null) {
            inputStreamReader.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
        return stringBuilder.toString();
    }
}
