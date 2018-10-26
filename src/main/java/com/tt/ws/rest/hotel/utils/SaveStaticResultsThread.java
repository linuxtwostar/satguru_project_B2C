package com.tt.ws.rest.hotel.utils;


import com.tt.ts.rest.common.RedisService;
import com.tt.ts.rest.common.util.RedisCacheUtil;
import com.tt.ws.rest.hotel.bean.HotelSearchRespDataBean;
import com.ws.services.util.CallLog;

public class SaveStaticResultsThread implements Runnable
{
	private String hotelSearchKey;

	private RedisService redisService;
	
	private HotelSearchRespDataBean searchDataBean;

	public SaveStaticResultsThread(String hotelSearchKey, HotelSearchRespDataBean searchDataBean, RedisService redisService) {
		super();
		this.hotelSearchKey = hotelSearchKey;
		this.redisService = redisService;
		this.searchDataBean = searchDataBean;
	}

	@Override
	public void run()
	{
		long startingTime = System.currentTimeMillis(); 
		RedisCacheUtil.setResponseInCacheHotel(hotelSearchKey, searchDataBean,redisService);
		CallLog.info(103, "TOTAL TIME TO SET RESPONSE IN CACHE FOR STATIC RESPONSE:::: Time::" + (System.currentTimeMillis()-startingTime) + " milisecs || Time::::" + (System.currentTimeMillis()-startingTime/1000) + " secs");
		try {
			Thread.currentThread().interrupt();
		} catch (Exception e) {
			CallLog.info(103, "STATIC DATA CACHE INSERTION THREAD INTRUPTTED AFTER SAVE");
		}
	}
}
