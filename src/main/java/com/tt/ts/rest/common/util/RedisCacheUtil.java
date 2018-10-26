package com.tt.ts.rest.common.util;

import java.util.ArrayList;

import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.tt.ts.common.modal.ResultBean;
import com.tt.ts.rest.common.RedisService;
import com.tt.ws.rest.hotel.bean.HotelSearchRespDataBean;
import com.tt.ws.rest.hotel.bean.HotelsFareRuleRespBean;
import com.tt.ws.rest.hotel.model.HotelSatguruModel;
import com.ws.services.util.CallLog;

public final class RedisCacheUtil {
	
	private RedisCacheUtil() {

	}
	
	public static ResultBean isSearchKeyInCacheFlight(String searchKey,RedisService redisService) {
		ResultBean resultBean = new ResultBean();
		Boolean isFoundKey = false;
		try {
			Long startTime = System.currentTimeMillis();
			Object element = redisService.getValueFlight(searchKey);
			if (element != null) {
				isFoundKey = true;
			}
			Long endTime = System.currentTimeMillis();
			Long timeTaken = endTime - startTime;
			CallLog.info(11,"------REST(Flight)-----Time taken in founding key in cache:::" + timeTaken);
			CallLog.info(11,"------REST(Flight)-----isSearchKeyInCache:::" + isFoundKey);
			resultBean.setResultBoolean(isFoundKey);
			resultBean.setIserror(false);
		} catch(RedisConnectionFailureException ex) {
			resultBean.setIserror(true);
			CallLog.error(11,"------REST(Flight)--=---Error in Redis Connection.No JedisConnection found."+ex);
		} catch (Exception e) {
			resultBean.setIserror(true);
			CallLog.error(11, e);
		}
		return resultBean;

	}

	public static ResultBean isSearchKeyInCacheHotel(String searchKey,RedisService redisService) {
		ResultBean resultBean = new ResultBean();
		Boolean isFoundKey = false;
		try {
			Long startTime = System.currentTimeMillis();
			Object element = redisService.getValueHotel(searchKey);
			if (element != null) {
				isFoundKey = true;
			}
			Long endTime = System.currentTimeMillis();
			Long timeTaken = endTime - startTime;
			CallLog.info(11,"------REST(HOTEL)-----Time taken in founding key in cache:::" + timeTaken);
			CallLog.info(11,"------REST(HOTEL)-----isSearchKeyInCache:::" + isFoundKey);
			resultBean.setResultBoolean(isFoundKey);
			resultBean.setIserror(false);
		} catch(RedisConnectionFailureException ex) {
			resultBean.setIserror(true);
			CallLog.error(11,"------REST(HOTEL)-----Error in Redis Connection.No JedisConnection found.");
		} catch (Exception e) {
			resultBean.setIserror(true);
			CallLog.error(11, e);
		}
		return resultBean;

	}
	
	public static ResultBean isSearchKeyInCacheOthers(String searchKey,RedisService redisService) {
		ResultBean resultBean = new ResultBean();
		Boolean isFoundKey = false;
		try {
			Long startTime = System.currentTimeMillis();
			Object element = redisService.getValueOther(searchKey);
			if (element != null) {
				isFoundKey = true;
			}
			Long endTime = System.currentTimeMillis();
			Long timeTaken = endTime - startTime;
			CallLog.info(11,"------REST(INSURANCE)--=---Time taken in founding key in cache:::" + timeTaken);
			CallLog.info(11,"------REST(INSURANCE)-----isSearchKeyInCache:::" + isFoundKey);
			resultBean.setResultBoolean(isFoundKey);
			resultBean.setIserror(false);
		} catch(RedisConnectionFailureException ex) {
			resultBean.setIserror(true);
			CallLog.error(11,"------REST(INSURANCE)==-----Error in Redis Connection.No JedisConnection found.");
		} catch (Exception e) {
			resultBean.setIserror(true);
			CallLog.error(11, e);
		}
		return resultBean;

	}
	public static ResultBean getResponseFromCacheFlight(String searchKey,RedisService redisService) {
		ResultBean resultBean = new ResultBean();
		String responseString = "";
		String decompressedStr = "";
		
		try {
			CallLog.info(11, "------REST(Flight)-----key in Get:::" + searchKey);
			Long startTime = System.currentTimeMillis();
			Object element = redisService.getValueFlight(searchKey);
			if (element != null) {
				responseString = element.toString();
			}
			Long endTime = System.currentTimeMillis();
			Long timeTaken = endTime - startTime;
			CallLog.info(11,"------REST(Flight)-----Time taken in getting response from cache:::" + timeTaken);
			decompressedStr = CommonUtil.decompress(responseString);
			resultBean.setResultString(decompressedStr);
			resultBean.setIserror(false);
			CallLog.info(11,"Flight Response fetched from cache sucessfully.");
		} catch(RedisConnectionFailureException ex) {
			resultBean.setIserror(true);
			CallLog.error(11,"------REST(Flight)-----Error in Redis Connection.No JedisConnection found.");
		} catch (Exception e) {
			resultBean.setIserror(true);
			CallLog.error(11,
					"------REST(Flight)-----ERROR while fetching content from cache :: error cause"
							+ e);
			CallLog.printStackTrace(11, e);
		}
		return resultBean;

	}

	public static ResultBean getResponseObjectFromCacheFlight(String searchKey,RedisService redisService) {
		ResultBean resultBean = new ResultBean();
		try {
			CallLog.info(11, "------REST(Flight)-----key in Get:::" + searchKey);
			Long startTime = System.currentTimeMillis();
			Object element = redisService.getValueFlight(searchKey);
			
			Long endTime = System.currentTimeMillis();
			Long timeTaken = endTime - startTime;
			CallLog.info(11,"------REST(Flight)-----Time taken in getting response object from cache:::" + timeTaken);
			
			resultBean.setResultObject(element);
			resultBean.setIserror(false);
			CallLog.info(11,"Response object fetched from cache sucessfully.");
		} catch(RedisConnectionFailureException ex) {
			resultBean.setIserror(true);
			CallLog.error(11,"------REST(Flight)-----Error in Redis Connection.No JedisConnection found.");
		} catch (Exception e) {
			resultBean.setIserror(true);
			CallLog.error(11,
					"------REST(Flight)-----ERROR while fetching content from cache :: error cause"
							+ e);
			CallLog.printStackTrace(11, e);
		}
		return resultBean;

	}
	public static ResultBean getResponseFromCacheHotel(String searchKey,RedisService redisService) {
		ResultBean resultBean = new ResultBean();
		String responseString = "";
		String decompressedStr = "";
		
		try {
			CallLog.info(11, "------REST(Hotel)-----key in Get:::" + searchKey);
			Long startTime = System.currentTimeMillis();
			Object element = redisService.getValueHotel(searchKey);
			if (element != null) {
				responseString = element.toString();
			}
			Long endTime = System.currentTimeMillis();
			Long timeTaken = endTime - startTime;
			CallLog.info(11,"------REST(Hotel)-----Time taken in getting response from cache:::" + timeTaken);
			decompressedStr = CommonUtil.decompress(responseString);
			resultBean.setResultString(decompressedStr);
			resultBean.setIserror(false);
			CallLog.info(11,"Hotel Response fetched from cache sucessfully.");
		} catch(RedisConnectionFailureException ex) {
			resultBean.setIserror(true);
			CallLog.error(11,"------REST(HOTEL)-----Error in Redis Connection.No JedisConnection found.");
			CallLog.printStackTrace(11, ex);
		} catch (Exception e) {
			resultBean.setIserror(true);
			CallLog.error(11,
					"------REST(Hotel)-----ERROR while fetching content from cache :: error cause"
							+ e);
			CallLog.printStackTrace(11, e);
		}
		return resultBean;

	}
	public static ResultBean getResponseCacheHotelObject(String searchKey,RedisService redisService) {
		Object resultObj = null;
		ResultBean resultBean = new ResultBean();
		try 
		{
			CallLog.info(11, "------REST(Hotel)-----key in Get:::" + searchKey);
			Long startTime = System.currentTimeMillis();
			resultObj = redisService.getValueHotel(searchKey);
			Long endTime = System.currentTimeMillis();
			Long timeTaken = endTime - startTime;
			CallLog.info(11,
					"------REST(Hotel)-----Time taken in getting response from cache:::"
							+ timeTaken);
			//resultObj = CommonUtil.decompressHotelsObject((byte[]) resultObj);
			resultBean.setResultObject(resultObj);
			CallLog.info(11, "Hotel Response fetched from cache sucessfully.");
		} catch (RedisConnectionFailureException ex) {
			CallLog.error(11,"------REST(HOTEL)-----Error in Redis Connection.No JedisConnection found.");
			CallLog.printStackTrace(11, ex);
		} catch (Exception e) {
			CallLog.error(11,"------REST(Hotel)-----ERROR while fetching content from cache :: error cause"+ e);
			CallLog.printStackTrace(11, e);
		}
		return resultBean;

	}
	public static ResultBean setResponseInCacheHotel(String searchKey,Object dataObj,RedisService redisService) {
		ResultBean resultBean = new ResultBean();

		try 
		{
			//byte[] compressedObj = CommonUtil.compressHotelsObject(dataObj);
			CallLog.info(11, "------REST(Hotel)-----key in SET:::" + searchKey);
			Long startTime = System.currentTimeMillis();
			redisService.setValueHotelObjDB(searchKey, dataObj);
			Long endTime = System.currentTimeMillis();
			Long timeTaken = endTime - startTime;
			CallLog.info(11,"------REST(Hotel)-----Time taken in setting response in cache:::"+ timeTaken);
			resultBean.setIserror(false);
			CallLog.info(11, "Hotel Response set in cache sucessfully.");
		} catch (RedisConnectionFailureException ex) {
			resultBean.setIserror(true);
			CallLog.error(11,
					"------REST(Hotel)-----Error in Redis Connection.No JedisConnection found.");
		} catch (Exception e) {
			resultBean.setIserror(true);
			CallLog.error(11,
					"------REST(Hotel)-----ERROR while setting content in cache :: error cause"
							+ e);
			CallLog.printStackTrace(11, e);
		}
		return resultBean;

		}
	public static Object setResObjectInCacheHotelDB(String searchKey,Object jsonString,RedisService redisService) {
		ResultBean resultBean = new ResultBean();
		try 
		{
			//byte[] compressedObj = CommonUtil.compressHotelsObject(jsonString);
			CallLog.info(11, "------REST(Hotel)-----key in SET:::" + searchKey);
			Long startTime = System.currentTimeMillis();
			redisService.setValueHotelDB(searchKey, jsonString);
			Long endTime = System.currentTimeMillis();
			Long timeTaken = endTime - startTime;
			CallLog.info(11,"------REST(Hotel)-----Time taken in setting response in cache:::" + timeTaken);
			resultBean.setIserror(false);
			CallLog.info(11,"Hotel Response set in cache sucessfully.");
		} catch(RedisConnectionFailureException ex) {
			resultBean.setIserror(true);
			CallLog.error(11,"------REST(Hotel)-----Error in Redis Connection.No JedisConnection found.");
		} catch (Exception e) {
			resultBean.setIserror(true);
			CallLog.error(11,
					"------REST(Hotel)-----ERROR while setting content in cache :: error cause" + e);
			CallLog.printStackTrace(11, e);
		} 
		return resultBean;

	}
	public static ResultBean getResponseFromCacheOther(String searchKey,RedisService redisService) {
		ResultBean resultBean = new ResultBean();
		String responseString = "";
		String decompressedStr = "";
		
		try {
			CallLog.info(11, "------REST(INSURANCE)-----key in Get:::" + searchKey);
			Long startTime = System.currentTimeMillis();
			Object element = redisService.getValueOther(searchKey);
			if (element != null) {
				responseString = element.toString();
			}
			Long endTime = System.currentTimeMillis();
			Long timeTaken = endTime - startTime;
			CallLog.info(11,"------REST(INSURANCE)-----Time taken in getting response from cache:::" + timeTaken);
			decompressedStr = CommonUtil.decompress(responseString);
			resultBean.setResultString(decompressedStr);
			resultBean.setIserror(false);
			CallLog.info(11,"INSURANCE Response fetched from cache sucessfully.");
		} catch(RedisConnectionFailureException ex) {
			resultBean.setIserror(true);
			CallLog.error(11,"------REST(INSURANCE)-----Error in Redis Connection.No JedisConnection found.");
		} catch (Exception e) {
			resultBean.setIserror(true);
			CallLog.error(11,
					"------REST(INSURANCE)-----ERROR while fetching content from cache :: error cause"
							+ e);
			CallLog.printStackTrace(11, e);
		}
		return resultBean;

	}
	
	public static ResultBean setResponseInCacheFlight(String searchKey,String jsonString,RedisService redisService,Integer timeInMinutes) {
		ResultBean resultBean = new ResultBean();
		
		try {
			String compressedJsonStr = CommonUtil.compress(jsonString);
			CallLog.info(11, "------REST(Flight)-----key in SET:::" + searchKey);
			Long startTime = System.currentTimeMillis();
			redisService.setValueFlight(searchKey, compressedJsonStr,timeInMinutes);
			Long endTime = System.currentTimeMillis();
			Long timeTaken = endTime - startTime;
			CallLog.info(11,"------REST(Flight)-----Time taken in setting response in cache:::" + timeTaken);
			resultBean.setIserror(false);
			CallLog.info(11,"Flight Response set in cache sucessfully.");
		} catch(RedisConnectionFailureException ex) {
			resultBean.setIserror(true);
			CallLog.error(11,"------REST(Flight)-----Error in Redis Connection.No JedisConnection found.");
		} catch (Exception e) {
			resultBean.setIserror(true);
			CallLog.error(11,
					"------REST(Flight)-----ERROR while setting content in cache :: error cause" + e);
			CallLog.printStackTrace(11, e);
		} 
		return resultBean;

	}
	
	public static ResultBean setResponseObjectInCacheFlight(String searchKey,Object object,RedisService redisService,Integer timeInMinutes) {
		ResultBean resultBean = new ResultBean();
		
		try {
			CallLog.info(11, "------REST(Flight)-----key in SET:::" + searchKey);
			Long startTime = System.currentTimeMillis();
			redisService.setValueObjectFlight(searchKey, object,timeInMinutes);
			Long endTime = System.currentTimeMillis();
			Long timeTaken = endTime - startTime;
			CallLog.info(11,"------REST(Flight)-----Time taken in setting response object in cache:::" + timeTaken);
			resultBean.setIserror(false);
			CallLog.info(11,"Response Object set in cache sucessfully.");
		} catch(RedisConnectionFailureException ex) {
			resultBean.setIserror(true);
			CallLog.error(11,"------REST(Flight)-----Error in Redis Connection.No JedisConnection found.");
		} catch (Exception e) {
			resultBean.setIserror(true);
			CallLog.error(11,
					"------REST(Flight)-----ERROR while setting content in cache :: error cause" + e);
			CallLog.printStackTrace(11, e);
		} 
		return resultBean;

	}
	public static ResultBean setResponseInCacheHotel(String searchKey,String jsonString,RedisService redisService) {
		ResultBean resultBean = new ResultBean();
		
		try {
			String compressedJsonStr = CommonUtil.compress(jsonString);
			CallLog.info(11, "------REST(Hotel)-----key in SET:::" + searchKey);
			Long startTime = System.currentTimeMillis();
			redisService.setValueHotel(searchKey, compressedJsonStr);
			Long endTime = System.currentTimeMillis();
			Long timeTaken = endTime - startTime;
			CallLog.info(11,"------REST(Hotel)-----Time taken in setting response in cache:::" + timeTaken);
			resultBean.setIserror(false);
			CallLog.info(11,"Hotel Response set in cache sucessfully.");
		} catch(RedisConnectionFailureException ex) {
			resultBean.setIserror(true);
			CallLog.error(11,"------REST(Hotel)-----Error in Redis Connection.No JedisConnection found.");
		} catch (Exception e) {
			resultBean.setIserror(true);
			CallLog.error(11,
					"------REST(Hotel)-----ERROR while setting content in cache :: error cause" + e);
			CallLog.printStackTrace(11, e);
		} 
		return resultBean;

	}
	
	public static ResultBean setResponseInCacheHotelDB(String searchKey,String jsonString,RedisService redisService) {
		ResultBean resultBean = new ResultBean();
		
		try {
			String compressedJsonStr = CommonUtil.compress(jsonString);
			CallLog.info(11, "------REST(Hotel)-----key in SET:::" + searchKey);
			Long startTime = System.currentTimeMillis();
			redisService.setValueHotelDB(searchKey, compressedJsonStr);
			Long endTime = System.currentTimeMillis();
			Long timeTaken = endTime - startTime;
			CallLog.info(11,"------REST(Hotel)-----Time taken in setting response in cache:::" + timeTaken);
			resultBean.setIserror(false);
			CallLog.info(11,"Hotel Response set in cache sucessfully.");
		} catch(RedisConnectionFailureException ex) {
			resultBean.setIserror(true);
			CallLog.error(11,"------REST(Hotel)-----Error in Redis Connection.No JedisConnection found.");
		} catch (Exception e) {
			resultBean.setIserror(true);
			CallLog.error(11,
					"------REST(Hotel)-----ERROR while setting content in cache :: error cause" + e);
			CallLog.printStackTrace(11, e);
		} 
		return resultBean;

	}
	
	public static ResultBean setResponseInCacheOther(String searchKey,String jsonString,RedisService redisService) {
		ResultBean resultBean = new ResultBean();
		
		try {
			String compressedJsonStr = CommonUtil.compress(jsonString);
			CallLog.info(11, "------REST(INSURANCE)-----key in SET:::" + searchKey);
			Long startTime = System.currentTimeMillis();
			redisService.setValueOther(searchKey, compressedJsonStr);
			Long endTime = System.currentTimeMillis();
			Long timeTaken = endTime - startTime;
			CallLog.info(11,"------REST(INSURANCE)-----Time taken in setting response in cache:::" + timeTaken);
			resultBean.setIserror(false);
			CallLog.info(11,"Insurance Response set in cache sucessfully.");
		} catch(RedisConnectionFailureException ex) {
			resultBean.setIserror(true);
			CallLog.error(11,"------REST(INSURANCE)-----Error in Redis Connection.No JedisConnection found.");
		} catch (Exception e) {
			resultBean.setIserror(true);
			CallLog.error(11,
					"------REST(INSURANCE)-----ERROR while setting content in cache :: error cause" + e);
			CallLog.printStackTrace(11, e);
		} 
		return resultBean;

	}
	public static ResultBean isSearchKeyInCacheCar(String searchKey,RedisService redisService) {
		ResultBean resultBean = new ResultBean();
		Boolean isFoundKey = false;
		try {
			Long startTime = System.currentTimeMillis();
			Object element = redisService.getValueCar(searchKey);
			if (element != null) {
				isFoundKey = true;
			}
			Long endTime = System.currentTimeMillis();
			Long timeTaken = endTime - startTime;
			CallLog.info(11,"------REST(CAR)-----Time taken in founding key in cache:::" + timeTaken);
			CallLog.info(11,"------REST(CAR)-----isSearchKeyInCache:::" + isFoundKey);
			resultBean.setResultBoolean(isFoundKey);
			resultBean.setIserror(false);
		} catch(RedisConnectionFailureException ex) {
			resultBean.setIserror(true);
			CallLog.error(11,"------REST(CAR)-----Error in Redis Connection.No JedisConnection found.");
		} catch (Exception e) {
			resultBean.setIserror(true);
			CallLog.error(11, e);
		}
		return resultBean;

	}
	
	public static ResultBean getResponseFromCacheCar(String searchKey,RedisService redisService) {
		ResultBean resultBean = new ResultBean();
		String responseString = "";
		String decompressedStr = "";
		
		try {
			CallLog.info(11, "------REST(CAR)-----key in Get:::" + searchKey);
			Long startTime = System.currentTimeMillis();
			Object element = redisService.getValueCar(searchKey);
			if (element != null) {
				responseString = element.toString();
			}
			Long endTime = System.currentTimeMillis();
			Long timeTaken = endTime - startTime;
			CallLog.info(11,"------REST(CAR)-----Time taken in getting response from cache:::" + timeTaken);
			decompressedStr = CommonUtil.decompress(responseString);
			resultBean.setResultString(decompressedStr);
			resultBean.setIserror(false);
			CallLog.info(11,"Car Response fetched from cache sucessfully.");
		} catch(RedisConnectionFailureException ex) {
			resultBean.setIserror(true);
			CallLog.error(11,"------REST(CAR)-----Error in Redis Connection.No JedisConnection found.");
		} catch (Exception e) {
			resultBean.setIserror(true);
			CallLog.error(11,
					"------REST(CAR)-----ERROR while fetching content from cache :: error cause"
							+ e);
			CallLog.printStackTrace(11, e);
		}
		return resultBean;

	}
	
	public static ResultBean setResponseInCacheCar(String searchKey,String jsonString,RedisService redisService) {
		ResultBean resultBean = new ResultBean();
		
		try {
			String compressedJsonStr = CommonUtil.compress(jsonString);
			CallLog.info(11, "------REST(Car)-----key in SET:::" + searchKey);
			Long startTime = System.currentTimeMillis();
			redisService.setValueCar(searchKey, compressedJsonStr);
			Long endTime = System.currentTimeMillis();
			Long timeTaken = endTime - startTime;
			CallLog.info(11,"------REST(Car)-----Time taken in setting response in cache:::" + timeTaken);
			resultBean.setIserror(false);
			CallLog.info(11,"Car Response set in cache sucessfully.");
		} catch(RedisConnectionFailureException ex) {
			resultBean.setIserror(true);
			CallLog.error(11,"------REST(Car)-----Error in Redis Connection.No JedisConnection found.");
		} catch (Exception e) {
			resultBean.setIserror(true);
			CallLog.error(11,
					"------REST(Car)-----ERROR while setting content in cache :: error cause" + e);
			CallLog.printStackTrace(11, e);
		} 
		return resultBean;

	}

	public static ResultBean removeResponseFromCacheFlight(String searchKey,RedisService redisService) {
		ResultBean resultBean = new ResultBean();
		try {
			CallLog.info(11, "------REST(Flight)-----key in Remove:::" + searchKey);
			redisService.removeValueFlight(searchKey);
			
			resultBean.setIserror(false);
			CallLog.info(11,"Flight Response removed from cache sucessfully.");
		} catch(RedisConnectionFailureException ex) {
			resultBean.setIserror(true);
			CallLog.error(11,"------REST(Flight)-----Error in Redis Connection.No JedisConnection found.");
		} catch (Exception e) {
			resultBean.setIserror(true);
			CallLog.error(11,
					"------REST(Flight)-----ERROR while removing content from cache :: error cause"
							+ e);
			CallLog.printStackTrace(11, e);
		}
		return resultBean;
	}
	
	public static ResultBean isSearchKeyInCacheActivity(String searchKey,RedisService redisService) {
		ResultBean resultBean = new ResultBean();
		Boolean isFoundKey = false;
		try {
			Long startTime = System.currentTimeMillis();
			Object element = redisService.getValueActivity(searchKey);
			if (element != null) {
				isFoundKey = true;
			}
			Long endTime = System.currentTimeMillis();
			Long timeTaken = endTime - startTime;
			CallLog.info(11,"------REST(Activity)-----Time taken in founding key in cache:::" + timeTaken);
			CallLog.info(11,"------REST(Activity)-----isSearchKeyInCache:::" + isFoundKey);
			resultBean.setResultBoolean(isFoundKey);
			resultBean.setIserror(false);
		} catch(RedisConnectionFailureException ex) {
			resultBean.setIserror(true);
			CallLog.error(11,"------REST(Activity)-----Error in Redis Connection.No JedisConnection found.");
		} catch (Exception e) {
			resultBean.setIserror(true);
			CallLog.error(11, e);
		}
		return resultBean;

	}
	
	public static ResultBean getResponseFromCacheActivity(String searchKey,RedisService redisService) {
		ResultBean resultBean = new ResultBean();
		String responseString = "";
		String decompressedStr = "";
		
		try {
			CallLog.info(11, "------REST(Activity)-----key in Get:::" + searchKey);
			Long startTime = System.currentTimeMillis();
			Object element = redisService.getValueActivity(searchKey);
			if (element != null) {
				responseString = element.toString();
			}
			Long endTime = System.currentTimeMillis();
			Long timeTaken = endTime - startTime;
			CallLog.info(11,"------REST(Activity)-----Time taken in getting response from cache:::" + timeTaken);
			decompressedStr = CommonUtil.decompress(responseString);
			resultBean.setResultString(decompressedStr);
			resultBean.setIserror(false);
			CallLog.info(11,"Activity Response fetched from cache sucessfully.");
		} catch(RedisConnectionFailureException ex) {
			resultBean.setIserror(true);
			CallLog.error(11,"------REST(Activity)-----Error in Redis Connection.No JedisConnection found.");
		} catch (Exception e) {
			resultBean.setIserror(true);
			CallLog.error(11,
					"------REST(Activity)-----ERROR while fetching content from cache :: error cause"
							+ e);
			CallLog.printStackTrace(11, e);
		}
		return resultBean;

	}
	public static ResultBean setResponseInCacheActivity(String searchKey,String jsonString,RedisService redisService) {
		ResultBean resultBean = new ResultBean();
		
		try {
			String compressedJsonStr = CommonUtil.compress(jsonString);
			CallLog.info(11, "------REST(Activity)-----key in SET:::" + searchKey);
			Long startTime = System.currentTimeMillis();
			redisService.setValueActivity(searchKey, compressedJsonStr);
			Long endTime = System.currentTimeMillis();
			Long timeTaken = endTime - startTime;
			CallLog.info(11,"------REST(Activity)-----Time taken in setting response in cache:::" + timeTaken);
			resultBean.setIserror(false);
			CallLog.info(11,"Activity Response set in cache sucessfully.");
		} catch(RedisConnectionFailureException ex) {
			resultBean.setIserror(true);
			CallLog.error(11,"------REST(Activity)-----Error in Redis Connection.No JedisConnection found.");
		} catch (Exception e) {
			resultBean.setIserror(true);
			CallLog.error(11,
					"------REST(Activity)-----ERROR while setting content in cache :: error cause" + e);
			CallLog.printStackTrace(11, e);
		} 
		return resultBean;

	}
	
	@SuppressWarnings("deprecation")
	public static ResultBean flushActivityRedisCacheWithKey(RedisService redisService,String key)
	{
		ResultBean resultBean = new ResultBean();
		JedisPool pool = null; 
		Jedis jedis = null; 
		String hostName = "";
		int port = 0 ;
		try
		{
			 RedisTemplate<String, Object>  redisEle = redisService.getValueActivityForFlushCache();
			 JedisConnectionFactory connFact =  (JedisConnectionFactory) redisEle.getConnectionFactory();
			
			 hostName = connFact.getHostName();
			 port  = connFact.getPort();
			 pool = new JedisPool(hostName, port);
			 jedis = pool.getResource();
			 jedis.del(key);
			 resultBean.setIserror(false);
		}
		catch (RedisConnectionFailureException ex)
		{
			resultBean.setIserror(true);
			CallLog.error(25, "------REST(Hotel)-----Error in Redis Connection.No JedisConnection found.");
			CallLog.printStackTrace(11, ex);
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			CallLog.error(25, "------PORTAL(HOTEL)-----ERROR while setting content in cache :: error cause" + e);
			CallLog.printStackTrace(11, e);
		}
		finally
		{ 
			 if(jedis!=null)
				 pool.returnResource(jedis); 
			 if(null != pool)
				 pool.close();
		} 
		return resultBean;
	}
	@SuppressWarnings("deprecation")
	public static ResultBean flushCarRedisCacheWithKey(RedisService redisService,String key)
	{
		ResultBean resultBean = new ResultBean();
		JedisPool pool = null; 
		Jedis jedis = null; 
		String hostName = "";
		int port = 0 ;
		try
		{
			 RedisTemplate<String, Object>  redisEle = redisService.getValueCarForFlushCache();
			 JedisConnectionFactory connFact =  (JedisConnectionFactory) redisEle.getConnectionFactory();
			
			 hostName = connFact.getHostName();
			 port  = connFact.getPort();
			 pool = new JedisPool(hostName, port);
			 jedis = pool.getResource();
			 jedis.del(key);
			 resultBean.setIserror(false);
		}
		catch (RedisConnectionFailureException ex)
		{
			resultBean.setIserror(true);
			CallLog.error(25, "------REST(Hotel)-----Error in Redis Connection.No JedisConnection found.");
			CallLog.printStackTrace(11, ex);
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			CallLog.error(25, "------PORTAL(HOTEL)-----ERROR while setting content in cache :: error cause" + e);
			CallLog.printStackTrace(11, e);
		}
		finally
		{ 
			 if(jedis!=null)
				 pool.returnResource(jedis); 
			 if(null != pool)
				 pool.close();
		} 
		return resultBean;
	}
	public static ResultBean getResponseCacheCarObject(String searchKey,RedisService redisService) {
		Object resultObj = null;
		ResultBean resultBean = new ResultBean();
		try 
		{
			CallLog.info(11, "------REST(Car)-----key in Get:::" + searchKey);
			Long startTime = System.currentTimeMillis();
			resultObj = redisService.getValueCar(searchKey);
			Long endTime = System.currentTimeMillis();
			Long timeTaken = endTime - startTime;
			CallLog.info(11,
					"------REST(Car)-----Time taken in getting response from cache:::"
							+ timeTaken);
			//resultObj = CommonUtil.decompressHotelsObject((byte[]) resultObj);
			resultBean.setResultObject(resultObj);
			CallLog.info(11, "Car Response fetched from cache sucessfully.");
		} catch (RedisConnectionFailureException ex) {
			CallLog.error(11,"------REST(Car)-----Error in Redis Connection.No JedisConnection found.");
			CallLog.printStackTrace(11, ex);
		} catch (Exception e) {
			CallLog.error(11,"------REST(Car)-----ERROR while fetching content from cache :: error cause"+ e);
			CallLog.printStackTrace(11, e);
		}
		return resultBean;

	}
	public static ResultBean setResponseInCacheCar(String searchKey,Object dataObj,RedisService redisService) {
		ResultBean resultBean = new ResultBean();

		try 
		{
			//byte[] compressedObj = CommonUtil.compressHotelsObject(dataObj);
			CallLog.info(11, "------REST(Car)-----key in SET:::" + searchKey);
			Long startTime = System.currentTimeMillis();
			redisService.setValueCarForDays(searchKey, dataObj);
			Long endTime = System.currentTimeMillis();
			Long timeTaken = endTime - startTime;
			CallLog.info(11,"------REST(Car)-----Time taken in setting response in cache:::"+ timeTaken);
			resultBean.setIserror(false);
			CallLog.info(11, "Car Response set in cache sucessfully.");
		} catch (RedisConnectionFailureException ex) {
			resultBean.setIserror(true);
			CallLog.error(11,
					"------REST(Car)-----Error in Redis Connection.No JedisConnection found.");
		} catch (Exception e) {
			resultBean.setIserror(true);
			CallLog.error(11,
					"------REST(Car)-----ERROR while setting content in cache :: error cause"
							+ e);
			CallLog.printStackTrace(11, e);
		}
		return resultBean;

		}
}
