package com.tt.ts.rest.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.tt.satguruportal.util.common.TTPortalLog;
import com.tt.ws.rest.hotel.dao.HotelDao;

@Service
public class RedisService {

	@Autowired
	@Qualifier(value = "redisTemplateFlight")
	private RedisTemplate<String, Object> templateFlight;

	@Autowired
	@Qualifier(value = "redisTemplateHotel")
	private RedisTemplate<String, Object> templateHotel;
	
	@Autowired
	@Qualifier(value = "redisTemplateCar")
	private RedisTemplate<String, Object> templateCar;

	@Autowired
	@Qualifier(value = "redisTemplateOther")
	private RedisTemplate<String, Object> templateOther;
	
	@Autowired
	@Qualifier(value = "redisTemplateActivities")
	private RedisTemplate<String, Object> templateActivity;
	
	@Autowired
	private HotelDao hotelDao;
	
	public Object getValueFlight(final String key) {
		return templateFlight.opsForValue().get(key);
	}

	public void setValueFlight(final String key, final String value, Integer timeInMinutes) {
		templateFlight.opsForValue().set(key, value);

		// set a expire for a message
		templateFlight.expire(key, timeInMinutes, TimeUnit.MINUTES);
	}

	public void setValueObjectFlight(final String key, final Object value , Integer timeInMinutes) {
		templateFlight.opsForValue().set(key, value);

		// set a expire for a message
		templateFlight.expire(key,timeInMinutes, TimeUnit.MINUTES);
	}
	
	public void removeValueFlight(final String key) {
		templateFlight.delete(key);
	}
	
	public Object getValueHotel(final String key) {
		return templateHotel.opsForValue().get(key);
	}

	public void setValueHotel(final String key, final Object value) {
		String priceTimeOutVal = null;
		templateHotel.opsForValue().set(key, value);

		// set a expire for a message
		
		try 
		{
			List<Object[]> listObj = hotelDao.getDirectories();
			Object[] col = null;
			
			if(null != listObj && !listObj.isEmpty())
			{
				for(int i=0;i<listObj.size();i++)
				{
					col = listObj.get(i);
					if(null != col[1] && "hotel.priceResult.redisCache.timeout".equalsIgnoreCase(col[1].toString()))
						priceTimeOutVal = col[2].toString();
				}
			}
			else
			{
				Map<String,String> keyValuesMap = new HashMap<>();
				keyValuesMap.put("hotel.priceResult.redisCache.timeout", "24-HOURS");
				keyValuesMap.put("hotel.dbResult.redisCache.timeout", "30-DAYS");
				hotelDao.saveCacheTimeOut(keyValuesMap);
			}
			
			if(null != priceTimeOutVal && !priceTimeOutVal.isEmpty())
			{
				String timeOutType = priceTimeOutVal.split("-")[1];
				if("DAYS".equalsIgnoreCase(timeOutType))
					templateHotel.expire(key, Long.parseLong(priceTimeOutVal.split("-")[0]), TimeUnit.DAYS);
				else if("HOURS".equalsIgnoreCase(timeOutType))
					templateHotel.expire(key, Long.parseLong(priceTimeOutVal.split("-")[0]), TimeUnit.HOURS);
				else if("MINUTES".equalsIgnoreCase(timeOutType))
					templateHotel.expire(key, Long.parseLong(priceTimeOutVal.split("-")[0]), TimeUnit.MINUTES);
				else if("SECONDS".equalsIgnoreCase(timeOutType))
					templateHotel.expire(key, Long.parseLong(priceTimeOutVal.split("-")[0]), TimeUnit.SECONDS);
				else if("MILLISECONDS".equalsIgnoreCase(timeOutType))
					templateHotel.expire(key, Long.parseLong(priceTimeOutVal.split("-")[0]), TimeUnit.MILLISECONDS);
				else if("MICROSECONDS".equalsIgnoreCase(timeOutType))
					templateHotel.expire(key, Long.parseLong(priceTimeOutVal.split("-")[0]), TimeUnit.MICROSECONDS);
				else if("NANOSECONDS".equalsIgnoreCase(timeOutType))
					templateHotel.expire(key, Long.parseLong(priceTimeOutVal.split("-")[0]), TimeUnit.NANOSECONDS);
			}
			else
			{
				templateHotel.expire(key, 24, TimeUnit.HOURS);
				TTPortalLog.info(103, "TimeOut value not found from DB, default 24 HOURS time out setted for Non-DB cache keys");
			}
		} catch (Exception e) {
			TTPortalLog.printStackTrace(103, e);
			TTPortalLog.info(103, "Due to exception to fetch TimeOut from DB, default 24 HOURS time out setted for Non-DB cache keys");
			templateHotel.expire(key, 24, TimeUnit.HOURS);
		}
		
	}
	
	public void setValueHotelDB(final String key, final Object value) {
		String dbTimeOutVal = null;
		templateHotel.opsForValue().set(key, value);
		// set a expire for a message
		try 
		{
			List<Object[]> listObj = hotelDao.getDirectories();
			Object[] col = null;
			
			if(null != listObj && !listObj.isEmpty())
			{
				for(int i=0;i<listObj.size();i++)
				{
					col = listObj.get(i);
					if(null != col[1] && "hotel.dbResult.redisCache.timeout".equalsIgnoreCase(col[1].toString()))
						dbTimeOutVal = col[2].toString();
				}
			}
			else
			{
				Map<String,String> keyValuesMap = new HashMap<>();
				keyValuesMap.put("hotel.priceResult.redisCache.timeout", "24-HOURS");
				keyValuesMap.put("hotel.dbResult.redisCache.timeout", "30-DAYS");
				hotelDao.saveCacheTimeOut(keyValuesMap);
			}
			
			if(null != dbTimeOutVal && !dbTimeOutVal.isEmpty())
			{
				String timeOutType = dbTimeOutVal.split("-")[1];
				if("DAYS".equalsIgnoreCase(timeOutType))
					templateHotel.expire(key, Long.parseLong(dbTimeOutVal.split("-")[0]), TimeUnit.DAYS);
				else if("HOURS".equalsIgnoreCase(timeOutType))
					templateHotel.expire(key, Long.parseLong(dbTimeOutVal.split("-")[0]), TimeUnit.HOURS);
				else if("MINUTES".equalsIgnoreCase(timeOutType))
					templateHotel.expire(key, Long.parseLong(dbTimeOutVal.split("-")[0]), TimeUnit.MINUTES);
				else if("SECONDS".equalsIgnoreCase(timeOutType))
					templateHotel.expire(key, Long.parseLong(dbTimeOutVal.split("-")[0]), TimeUnit.SECONDS);
				else if("MILLISECONDS".equalsIgnoreCase(timeOutType))
					templateHotel.expire(key, Long.parseLong(dbTimeOutVal.split("-")[0]), TimeUnit.MILLISECONDS);
				else if("MICROSECONDS".equalsIgnoreCase(timeOutType))
					templateHotel.expire(key, Long.parseLong(dbTimeOutVal.split("-")[0]), TimeUnit.MICROSECONDS);
				else if("NANOSECONDS".equalsIgnoreCase(timeOutType))
					templateHotel.expire(key, Long.parseLong(dbTimeOutVal.split("-")[0]), TimeUnit.NANOSECONDS);
			}
			else
			{
				templateHotel.expire(key, 30, TimeUnit.DAYS);
				TTPortalLog.info(103, "TimeOut value not found from DB, default 30 DAYS time out setted for Non-DB cache keys");
			}
		} catch (Exception e) {
			TTPortalLog.printStackTrace(103, e);
			TTPortalLog.info(103, "Due to exception to fetch TimeOut from DB, default 30 DAYS time out setted for Non-DB cache keys");
			templateHotel.expire(key, 30, TimeUnit.DAYS);
		}
	}

	public Object getValueCar(final String key) {
		return templateCar.opsForValue().get(key);
	}

	public void setValueCar(final String key, final String value) {
		templateCar.opsForValue().set(key, value);

		// set a expire for a message
		templateCar.expire(key, 1800, TimeUnit.SECONDS);
	}
	
	
	public Object getValueOther(final String key) {
		return templateOther.opsForValue().get(key);
	}

	public void setValueOther(final String key, final String value) {
		templateOther.opsForValue().set(key, value);

		// set a expire for a message
		templateOther.expire(key, 1800, TimeUnit.SECONDS);
	}
	
	public Object getValueActivity(final String key) {
		return templateActivity.opsForValue().get(key);
	}

	public void setValueActivity(final String key, final String value) {
		templateActivity.opsForValue().set(key, value);

		// set a expire for a message
		templateActivity.expire(key, 36, TimeUnit.HOURS);
	}
	
	public RedisTemplate<String, Object> getValueActivityForFlushCache() {
		return templateActivity;
	}
	public void setValueHotelObjDB(final String key, final Object value) {
		String dbTimeOutVal = null;
		templateHotel.opsForValue().set(key, value);
		// set a expire for a message
		try {
			List<Object[]> listObj = hotelDao.getDirectories();
			Object[] col = null;

			if (null != listObj && !listObj.isEmpty()) {
				for (int i = 0; i < listObj.size(); i++) {
					col = listObj.get(i);
					if (null != col[1]
							&& "hotel.dbResult.redisCache.timeout"
									.equalsIgnoreCase(col[1].toString()))
						dbTimeOutVal = col[2].toString();
				}
			}

			if (null != dbTimeOutVal && !dbTimeOutVal.isEmpty()) {
				String timeOutType = dbTimeOutVal.split("-")[1];
				if ("DAYS".equalsIgnoreCase(timeOutType))
					templateHotel.expire(key,
							Long.parseLong(dbTimeOutVal.split("-")[0]),
							TimeUnit.DAYS);
				else if ("HOURS".equalsIgnoreCase(timeOutType))
					templateHotel.expire(key,
							Long.parseLong(dbTimeOutVal.split("-")[0]),
							TimeUnit.HOURS);
				else if ("MINUTES".equalsIgnoreCase(timeOutType))
					templateHotel.expire(key,
							Long.parseLong(dbTimeOutVal.split("-")[0]),
							TimeUnit.MINUTES);
				else if ("SECONDS".equalsIgnoreCase(timeOutType))
					templateHotel.expire(key,
							Long.parseLong(dbTimeOutVal.split("-")[0]),
							TimeUnit.SECONDS);
				else if ("MILLISECONDS".equalsIgnoreCase(timeOutType))
					templateHotel.expire(key,
							Long.parseLong(dbTimeOutVal.split("-")[0]),
							TimeUnit.MILLISECONDS);
				else if ("MICROSECONDS".equalsIgnoreCase(timeOutType))
					templateHotel.expire(key,
							Long.parseLong(dbTimeOutVal.split("-")[0]),
							TimeUnit.MICROSECONDS);
				else if ("NANOSECONDS".equalsIgnoreCase(timeOutType))
					templateHotel.expire(key,
							Long.parseLong(dbTimeOutVal.split("-")[0]),
							TimeUnit.NANOSECONDS);
			} else {
				templateHotel.expire(key, 30, TimeUnit.DAYS);
				TTPortalLog
						.info(103,
								"TimeOut value not found from DB, default 30 DAYS time out setted for Non-DB cache keys");
			}
		} catch (Exception e) {
			TTPortalLog.printStackTrace(103, e);
			TTPortalLog
					.info(103,
							"Due to exception to fetch TimeOut from DB, default 30 DAYS time out setted for Non-DB cache keys");
			templateHotel.expire(key, 30, TimeUnit.DAYS);
		}
		}
	public RedisTemplate<String, Object> getValueCarForFlushCache() {
		return templateCar;
	}
	public void setValueCarForDays(final String key, final Object value) {
		templateCar.opsForValue().set(key, value);
		templateCar.expire(key, 30, TimeUnit.DAYS);
	}

}