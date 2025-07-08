package com.keensense.densecrowd.util;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

/**
 * ehcache的缓存工具类
 * 
 * @author Administrator
 *
 */
public class EhcacheUtils {
	private EhcacheUtils(){}

    public static final String VIDEO_TYPE_CACHE = "videoTypeCache";
    
	private static final CacheManager cacheManager = CacheManager.getInstance();

	/**
	 * 创建ehcache缓存，创建之后的有效期是1小时
	 */
	private static Cache systemCache = new Cache(new CacheConfiguration(
									"systemCache", 5000)
									.memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.FIFO)
									.timeoutMillis(300).timeToLiveSeconds(60 * 60));
	
	/**
     * 一直有效
     */
    private static Cache videoTypeCache = new Cache(new CacheConfiguration(
                                    "videoTypeCache", 5000)
                                    .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.FIFO).eternal(true));
    
	static {
		cacheManager.addCache(systemCache);
		cacheManager.addCache(videoTypeCache);
	}

	
	public static void putItem(String key, Object item) {
		if (systemCache.get(key) != null) {
		    systemCache.remove(key);
		}
		Element element = new Element(key, item);
		systemCache.put(element);
	}

	public static void removeItem(String key) {
	    systemCache.remove(key);
	}

	public static void updateItem(String key, Object value) {
		putItem(key, value);
	}

	public static Object getItem(String key) {
		Element element = systemCache.get(key);
		if (null != element) {
			return element.getObjectValue();
		}
		return null;
	}

   public static void putItem(String cacheName, String key, Object item) 
   {
        Cache cache = cacheManager.getCache(cacheName);
        if(null == cache)
        {
            return;
        }
        
        if (cache.get(key) != null) {
            cache.remove(key);
        }
        Element element = new Element(key, item);
        cache.put(element);
    }
   
   public static Object getItem(String cacheName, String key) {
       Cache cache = cacheManager.getCache(cacheName);
       if(null == cache)
       {
           return null;
       }
       
       Element element = cache.get(key);
       if (null != element) {
           return element.getObjectValue();
       }
       return null;
   }
   
}
