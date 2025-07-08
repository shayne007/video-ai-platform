package com.keensense.dataconvert.api.util.es;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName：EsScrollQueryUtil
 * @Description： <p> EsScrollQueryUtil  </p>
 * @Author： - memory_fu
 * @CreatTime：2019/7/25 - 16:28
 * @Modify By：
 * @ModifyTime： 2019/7/25
 * @Modify marker：
 * @version V1.0
*/
public class EsScrollQueryUtil {

    private static final Logger logger = LoggerFactory.getLogger(EsScrollQueryUtil.class);

	private static final String defaultScrollTime = "5m";
	private static final String defaultPort = "9200";
	private String scrollId = new String();

    /**
     * 全量查询es数据
     * @param indexName 索引名称
     * @param ip  安装es地址
     * @param size 每次查询es数据条数
     * @return
     * @throws IOException
     */
	public List<JSONObject> queryData(String indexName, String ip, int size) throws IOException {
		return queryData(indexName, ip, defaultScrollTime, size);
	}

    /**
     * 全量查询es数据
     * @param indexName 索引名称
     * @param ip   安装es地址
     * @param scrollTime scrollId保存时间
     * @param size 每次查询es数据条数
     * @return
     * @throws IOException
     */
	public List<JSONObject> queryData(String indexName, String ip, String scrollTime, int size) throws IOException {
		List<JSONObject> list = new ArrayList<>();
		if (StringUtils.isEmpty(this.scrollId)) {
			List<JSONObject> scrollIdData = getScrollId(indexName, ip, size);
			list.addAll(scrollIdData);
		} else {
			List<JSONObject> scrollData = getScrollData(indexName, ip);
			list.addAll(scrollData);
		}
		return list;
	}

    /**
     * getScrollId
     * @param indexName
     * @param ip
     * @param size
     * @return
     * @throws IOException
     */
	private List<JSONObject> getScrollId(String indexName, String ip, int size) throws IOException {
        /**
         * 获取Scroll_Id和数据
         */
		String url = getUrl(indexName, ip,  null);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("size", size);
		String queryStr = jsonObject.toString();
		logger.info("=== url:[{}],queryStr:[{}] ===",url,queryStr);
		String responseStr = HttpUtil.requestMethod(HttpUtil.HTTP_POST, url, queryStr);
		List<JSONObject> analysisData = analysisData(responseStr);
		return analysisData;
	}


    /**
     * getScrollData
     * @param indexName
     * @param ip
     * @return
     * @throws IOException
     */
	private List<JSONObject> getScrollData(String indexName, String ip) throws IOException {
		String url = getUrl(indexName, ip, defaultScrollTime);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("scroll", defaultScrollTime);
		jsonObject.put("scroll_id", this.scrollId);
		String queryStr = jsonObject.toString();
        logger.info("=== url:[{}],queryStr:[{}] ===",url,queryStr);
		String responseStr = HttpUtil.requestMethod(HttpUtil.HTTP_POST, url, queryStr);
		List<JSONObject> analysisData = analysisData(responseStr);
		return analysisData;
	}


    /**
     * analysisData
     * @param responseStr
     * @return
     */
	private List<JSONObject> analysisData(String responseStr) {
		List<JSONObject> list = new ArrayList<>();
		JSONObject jsonObject = JSONObject.parseObject(responseStr);
		if (null == jsonObject) {
			return list;
		}
		this.scrollId = jsonObject.getString("_scroll_id");
		JSONObject parentHits = jsonObject.getJSONObject("hits");
		if (null == parentHits) {
			return list;
		}
		JSONArray subHits = parentHits.getJSONArray("hits");
		if (null == subHits) {
			return list;
		}
		for (int i = 0; i < subHits.size(); i++) {
			JSONObject object = subHits.getJSONObject(i);
			String id = object.getString("_id");
			JSONObject source = object.getJSONObject("_source");
			source.put("id", id);
			list.add(source);
		}
		return list;
	}

    /**
     * getUrl
     * @param indexName
     * @param ip
     * @param port
     * @param scrollTime
     * @return
     */
	private String getUrl(String indexName, String ip, String port, String scrollTime) {
		if (StringUtils.isEmpty(scrollTime)) {
			return new String("http://" + ip + ":" + port + "/" + indexName + "/_search?scroll=" + defaultScrollTime);
		}
		return new String("http://" + ip + ":" + port + "/_search/scroll");
	}

    /**
     * getUrl
     * @param indexName
     * @param ip
     * @param scrollTime
     * @return
     */
	private String getUrl(String indexName, String ip, String scrollTime) {
		return getUrl(indexName, ip, defaultPort, scrollTime);
	}

    public void clear() {
        this.scrollId = null;
    }

    /**
     * test
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String[] indexNames = new String[] { "vlpr_result", "summary_result", "objext_result", "face_result","bike_result" };
        EsScrollQueryUtil queryUtil = new EsScrollQueryUtil();
        for (String index : indexNames) {
            int count = 0;
            while (true) {
                List<JSONObject> list = queryUtil.queryData(index, "127.0.0.1", 1000);
                count += list.size();
                if (list.size() == 0) {
                    // 一个索引数据遍历完成后必须调用此方法清除scrollId
                    queryUtil.clear();
                    break;
                }

            }
        }

    }

}
