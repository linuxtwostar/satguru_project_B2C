package com.tt.ws.rest.hotel.utils;


import com.tt.ts.rest.common.RedisService;
import com.tt.ts.rest.common.util.RedisCacheUtil;
import com.tt.ws.rest.hotel.bean.HotelSearchRespDataBean;
import com.ws.services.util.CallLog;

public class SavePriceResultsThread implements Runnable
{
	private String hotelSearchKey;

	private RedisService redisService;
	
	private HotelSearchRespDataBean ruleAppliedRespBean;

	public SavePriceResultsThread(String hotelSearchKey, HotelSearchRespDataBean ruleAppliedRespBean, RedisService redisService) {
		super();
		this.hotelSearchKey = hotelSearchKey;
		this.redisService = redisService;
		this.ruleAppliedRespBean = ruleAppliedRespBean;
	}

	@Override
	public void run()
	{
		long startingTime = System.currentTimeMillis(); 
		RedisCacheUtil.setResponseInCacheHotel(hotelSearchKey, ruleAppliedRespBean,redisService);
		CallLog.info(103, "TOTAL TIME TO SET RESPONSE IN CACHE FOR PRICE RESPONSE:::: Time::" + (System.currentTimeMillis()-startingTime) + " milisecs || Time::::" + (System.currentTimeMillis()-startingTime/1000) + " secs");
		try {
			Thread.currentThread().interrupt();
		} catch (Exception e) {
			CallLog.info(103, "CACHE INSERTION THREAD INTRUPTTED AFTER SAVE");
		}
	}
}
