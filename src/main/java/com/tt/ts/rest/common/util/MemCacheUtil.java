package com.tt.ts.rest.common.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;

import com.tt.ts.common.modal.ResultBean;
import com.ws.services.util.CallLog;

public final class MemCacheUtil {
	private MemCacheUtil() {

	}

	private static MemcachedClient instance = null;

	private static MemcachedClient getCacheClient(String server)
			throws IOException {
		
		if (instance == null) {
			List<InetSocketAddress>	address = AddrUtil.getAddresses(server);
			instance = new MemcachedClient(new BinaryConnectionFactory(),address); // server=host+":"+port	
		}
			
		return instance;
	}

	public static ResultBean isSearchKeyInCache(String searchKey, String host,
			Integer port) {
		ResultBean resultBean = new ResultBean();
		Boolean isFoundKey = false;
		MemcachedClient memcacheClient = null;
		try {
			memcacheClient = getCacheClient(host + ":" + port);
			
			Object element = memcacheClient.get(searchKey);
			if (element != null) {
				isFoundKey = true;
			}
			CallLog.info(11, "key FOUND in CACHE :::" + isFoundKey);
			resultBean.setResultBoolean(isFoundKey);
			resultBean.setIserror(false);
		} catch (Exception e) {
			resultBean.setIserror(true);
			CallLog.error(11, e);
		}
		return resultBean;

	}

	public static ResultBean getResponseFromCache(String searchKey,
			String host, Integer port) {
		ResultBean resultBean = new ResultBean();
		String responseString = "";
		String decompressedStr = "";
		MemcachedClient memcacheClient = null;
		try {
			Long startTime = System.currentTimeMillis();
			CallLog.info(11, "key in Get:::" + searchKey);
			memcacheClient = getCacheClient(host + ":" + port);
			
			Object element = memcacheClient.get(searchKey);
			if (element != null) {
				responseString = element.toString();
			}
			Long endTime = System.currentTimeMillis();
			Long timeTaken = endTime - startTime;
			CallLog.error(11,"Time taken in GET response from Cache::::"+timeTaken);
			decompressedStr = CommonUtil.decompress(responseString);
			
			
			resultBean.setResultString(decompressedStr);
			resultBean.setIserror(false);
		} catch (Exception e) {
			resultBean.setIserror(true);
			CallLog.error(11,
					"ERROR while fetching content from cache :: error cause"
							+ e);
			CallLog.printStackTrace(11, e);
		}
		return resultBean;

	}

	public static ResultBean setResponseInCache(String searchKey,
			String jsonString, String host, Integer port) {
		ResultBean resultBean = new ResultBean();
		MemcachedClient memcacheClient = null;
		try {
			String compressedJsonStr = CommonUtil.compress(jsonString);
			memcacheClient = getCacheClient(host + ":" + port);
			CallLog.info(11, "key in SET:::" + searchKey);
			Long startTime = System.currentTimeMillis();
			memcacheClient.set(searchKey,1800, compressedJsonStr);
			Long endTime = System.currentTimeMillis();
			Long timeTaken = endTime - startTime;
			CallLog.error(11,"Time taken in SET response from Cache::::"+timeTaken);
			resultBean.setIserror(false);
		} catch (Exception e) {
			resultBean.setIserror(true);
			CallLog.error(11,
					"ERROR while setting content in cache :: error cause" + e);
			CallLog.printStackTrace(11, e);
		} 
		return resultBean;

	}
}
