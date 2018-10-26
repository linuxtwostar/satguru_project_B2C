package com.tt.ws.rest.hotel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tt.ts.rest.common.RedisService;
import com.tt.ws.rest.hotel.bean.HotelSearchReqDataBean;
import com.tt.ws.rest.hotel.bean.HotelSearchRespDataBean;
import com.tt.ws.rest.hotel.model.HotelSatguruModel;
import com.tt.ws.rest.hotel.services.HotelServices;
import com.tt.ws.rest.hotel.utils.HotelUtil;
import com.ws.services.hotel.bean.HotelSearchRequestBean;
import com.ws.services.util.CallLog;
//import com.tt.ts.rest.common.util.MemCacheUtil;

@RestController
@RequestMapping("/hotel")
public class HotelSearchController {
   
   
    String logStr = "[HotelSearchController]";
   /* private static final String REST_IP = "tt.rest.host";
    private static final String REST_CACHE_PORT = "tt.rest.memcache.port";*/

    @Autowired
    private HotelServices hotelServices;

    @Autowired
    MessageSource messageSource;

    @Autowired
    RedisService redisService;
    
    /*
     * {
     * 
     * "customerSessionId": "0ABAAC0D-307F-F5C9-9152-A89C278E197D",
     * "guestNationality": "IN", "totalResultCount": "10", "cityCode": "LON",
     * "languageCode": "en", "checkInDate": "20/04/2017", "destType": "C",
     * "hotelName": "", "cityName": "London", "tboCityName": "London",
     * "checkOutDate": "25/04/2017", "totalPax": 1, "countryCode": "UK",
     * "lohCountryCode": "ES", "countryName": "United Kingdom", "tboCityCode":
     * "9245", "noOfRooms": 1, "numberOfAdults": "2", "numberOfChildren": "1",
     * "destinationCode": "LON", "shiftDays": "2", "paxType": ["AD", "AD",
     * "CH-8"], "currencyCode": "USD", "duration": "1", "paxAgesMap": { "1": 50
     * }, "destinationZone": "13826", "roomList": [{ "roomCount": 1,
     * "childrenAge": [], "children": "0", "adults": "1", "noCots": 0 }] }
     */

//method for redis cache
    
    @RequestMapping(value = "/searchHotels", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String searchHotel(@RequestBody HotelSearchRequestBean reqBean) {
	String jsonString;
	/*serviceRequestBean = new ServiceRequestBean();
	reqBean = HotelUtil.prepareSearchKeyForPrice(reqBean);
	String hotelSearchKey = reqBean.getHotelSearchKey();
	CallLog.info(1, "hotelSearchKey for Price Call === " + hotelSearchKey);
	String host = messageSource.getMessage(REST_IP, null, null, null);
	String portCache = messageSource.getMessage(REST_CACHE_PORT, null, null, null);
	Integer port = Integer.parseInt(portCache);
	Boolean isSearchKeyFound = false;
	//ResultBean resultBean = MemCacheUtil.isSearchKeyInCache(hotelSearchKey, host, port);
	ResultBean resultBean = RedisCacheUtil.isSearchKeyInCacheHotel(hotelSearchKey,redisService);
	if (resultBean != null && !resultBean.isError()) {
	    isSearchKeyFound = resultBean.getResultBoolean();
	}
	if (isSearchKeyFound) {
		//ResultBean resultBeanCache = MemCacheUtil.getResponseFromCache(hotelSearchKey, host, port);
	    ResultBean resultBeanCache = RedisCacheUtil.getResponseFromCacheHotel(hotelSearchKey,redisService);
	    if (resultBeanCache != null && !resultBeanCache.isError()) {
		jsonString = resultBeanCache.getResultString();
		CallLog.info(1, logStr + "Resp JSON from RedisCacheUtil for searchHotels Price call::" + jsonString);
	    } else {
		jsonString = HotelUtil.getHotelSearchResponse(serviceRequestBean, reqBean);
		if (null != jsonString && !jsonString.isEmpty()) {
		    JSONObject updatedObj = HotelUtil.getMinPriceResp(jsonString, hotelServices);
		    if (null != updatedObj)
			jsonString = updatedObj.toString();
		    if(!resultBean.isError()) {
		    	//MemCacheUtil.setResponseInCache(hotelSearchKey, jsonString, host, port);
		    	RedisCacheUtil.setResponseInCacheHotel(hotelSearchKey, jsonString,redisService);
		    }
		    
		}
	    }

	} else {
	    jsonString = HotelUtil.getHotelSearchResponse(serviceRequestBean, reqBean);
	    if (null != jsonString && !jsonString.isEmpty()) {
		JSONObject updatedObj = HotelUtil.getMinPriceResp(jsonString, hotelServices);
		if (null != updatedObj)
		{
		    jsonString = updatedObj.toString();
		    if(!resultBean.isError()) {
		    	//MemCacheUtil.setResponseInCache(hotelSearchKey, jsonString, host, port);
		    	RedisCacheUtil.setResponseInCacheHotel(hotelSearchKey, jsonString,redisService);
		    }
		    
		}
	    }
	}*/
	//jsonString=hotelServices.searchHotel(reqBean);
	jsonString=null;
	return jsonString;
    }
     @RequestMapping(value = "/searchHotelStaticInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HotelSearchRespDataBean searchHotelStaticInfo(@RequestBody HotelSearchReqDataBean hotelSearchReqDataBean) {
	//logStr = logStr + " searchHotelStaticInfo() ";
	HotelSearchRespDataBean searchRespDataBean = null;
  /*  int pageNo = hotelSearchReqDataBean.getPageNumber();
    String cityName = hotelSearchReqDataBean.getCityName();
    String countryName = hotelSearchReqDataBean.getCountryName();
    String hotelName = hotelSearchReqDataBean.getHotelName();
    Double lat=hotelSearchReqDataBean.getLatitude()!=null?Double.parseDouble(hotelSearchReqDataBean.getLatitude()):null;
    Double lon=hotelSearchReqDataBean.getLongitude()!=null?Double.parseDouble(hotelSearchReqDataBean.getLongitude()):null;
    Double dis=hotelSearchReqDataBean.getGeoDistance()!=null?Double.parseDouble(hotelSearchReqDataBean.getGeoDistance()):null;
	List<HotelSatguruModel> resultList = null;*/
	try {
	    /////////////////////////////cache implementation////////////////////////////////////
	    
	   /* hotelSearchReqDataBean = HotelUtil.prepareKeyStaticData(hotelSearchReqDataBean);
		String hotelSearchKey = hotelSearchReqDataBean.getHotelSearchKey();
		CallLog.info(1, "hotelSearchKeyStatic for searchHotelStaticInfo=== " + hotelSearchKey);
		String host = messageSource.getMessage(REST_IP, null, null, null);
		String portCache = messageSource.getMessage(REST_CACHE_PORT, null, null, null);
		Integer port = Integer.parseInt(portCache);
		Boolean isSearchKeyFound = false;
		//ResultBean resultBean = MemCacheUtil.isSearchKeyInCache(hotelSearchKey, host, port);
		ResultBean resultBean = RedisCacheUtil.isSearchKeyInCacheHotel(hotelSearchKey,redisService);
		if (resultBean != null && !resultBean.isError())
		{
		    isSearchKeyFound = resultBean.getResultBoolean();
		}
		if (isSearchKeyFound)
        {
		    ResultBean resultBeanCache = RedisCacheUtil.getResponseFromCacheHotel(hotelSearchKey, redisService);
		    //ResultBean resultBeanCache = MemCacheUtil.getResponseFromCache(hotelSearchKey, host, port);  
		    if (resultBeanCache != null && !resultBeanCache.isError()) 
		        {
					jsonString = resultBeanCache.getResultString();
					CallLog.info(1, logStr + "Resp JSON from RedisCacheUtil for searchHotelStaticInfo::::" + jsonString);
			    }
		    else 
			    {
				    if ((null != cityName && !"".equalsIgnoreCase(cityName)) && pageNo > 0)
			         {
			    	   if (null != countryName && !"".equalsIgnoreCase(countryName))
			    	   {
			    		 //resultList = hotelServices.getHotelCompleteDetails(pageNo, cityName,countryName);
			    		 resultList = hotelServices.getHotelStaticInfo(cityName,countryName,hotelName,lat,lon,dis);

			    		 if(resultList!=null && resultList.size()>0)
	                         jsonString = HotelUtil.getJsonAsString(resultList);
			    		 
	                        if (null != jsonString && !jsonString.isEmpty() && !resultBean.isError())
	                          {
	                        	//MemCacheUtil.setResponseInCache(hotelSearchKey, jsonString, host, port);
	                        	RedisCacheUtil.setResponseInCacheHotel(hotelSearchKey, jsonString,redisService);
	                          }
			    	   }
			         }
			   } 	
		    }
		else 
		{
		//	resultList = hotelServices.getHotelCompleteDetails(pageNo, cityName,countryName);
			 resultList = hotelServices.getHotelStaticInfo(cityName,countryName,hotelName,lat,lon,dis);
			 if(resultList!=null && resultList.size()>0)
	    	    jsonString = HotelUtil.getJsonAsString(resultList);
			 
			if (null != jsonString && !jsonString.isEmpty() && !resultBean.isError())
			{
				//MemCacheUtil.setResponseInCache(hotelSearchKey, jsonString, host, port);
			    RedisCacheUtil.setResponseInCacheHotel(hotelSearchKey, jsonString,redisService);
		    }
		}*/
		searchRespDataBean=hotelServices.searchHotelStaticInfo(hotelSearchReqDataBean);
	    ////////////////////////////cache implementation ends///////////////////////////////
		
	} catch (Exception e) {
	    CallLog.printStackTrace(1, e);
	}
	return searchRespDataBean;
    }
  

    @RequestMapping(value = "/getHotelInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String getHotelInfo(@RequestBody HotelSearchReqDataBean hotelSearchReqDataBean) {

	logStr = logStr + " getHotelInfo() ";
	String jsonString = null;
	List<HotelSatguruModel> resultList = null;
	try {
	    String hotelId = hotelSearchReqDataBean.getHotelId();
	    if ((null != hotelId && !"".equalsIgnoreCase(hotelId)))
		resultList = hotelServices.getHotelInfo(Integer.parseInt(hotelId));

	    if (null != resultList && !resultList.isEmpty())
		jsonString = HotelUtil.getJsonAsString(resultList);

	    CallLog.info(1, logStr + " Json::" + jsonString);

	} catch (Exception e) {
	    CallLog.printStackTrace(1, e);
	}
	return jsonString;
    }
}
