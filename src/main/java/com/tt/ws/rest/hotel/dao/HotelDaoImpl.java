package com.tt.ws.rest.hotel.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.tt.nc.core.dao.GenericDAOImpl;
import com.tt.satguruportal.util.common.TTPortalLog;
import com.tt.ts.rest.common.util.GeoLocation;
import com.tt.ts.rest.common.util.QueryConstantRest;
import com.tt.ts.rest.ruleengine.util.RuleSimulationHelper;
import com.tt.ws.rest.hotel.model.HotelCityDetailsModel;
import com.tt.ws.rest.hotel.model.HotelCountryDetailsModel;
import com.tt.ws.rest.hotel.model.HotelSatMappingModel;
import com.tt.ws.rest.hotel.model.HotelSatguruModel;
import com.ws.services.hotel.bean.HotelSearchRequestBean;
import com.ws.services.hotel.bean.HotelWidgetElement;
import com.ws.services.util.CallLog;

@Repository
public class HotelDaoImpl extends GenericDAOImpl<Object, Long> implements HotelDao{

	private static final String DATE_FORMAT = "yyyy-MM-dd";
	
	@SuppressWarnings({ "unchecked" })
	@Override
	public List<HotelSatguruModel> getHotelCompleteDetails(int pageNo,String cityName,String countryName,String hotelName) throws Exception 
	{
		String hqlQuery = "";
		if(null != hotelName && !"".equalsIgnoreCase(hotelName))
		{
			hqlQuery = "SELECT hsm FROM HotelSatguruModel hsm, HotelCityDetailsModel hcm "
					+ "WHERE hsm.tsHotelCityId = hcm.ttHotelCityId AND hsm.hotelName='"+hotelName.trim().toLowerCase()+"' "
					+ "AND LOWER(hcm.cityName) LIKE '%"+cityName.trim().toLowerCase()+"%' AND LOWER(hcm.countryName) "
					+ "LIKE '%"+countryName.trim().toLowerCase()+"%'  AND hsm.satguruHotelId IS NULL ";
		}
		else
		{
			hqlQuery = "SELECT hsm FROM HotelSatguruModel hsm, HotelCityDetailsModel hcm WHERE hsm.tsHotelCityId = hcm.ttHotelCityId "
					+ "AND LOWER(hcm.cityName) LIKE '%"+cityName.trim().toLowerCase()+"%' AND LOWER(hcm.countryName) "
					+ "LIKE '%"+countryName.trim().toLowerCase()+"%'  AND hsm.satguruHotelId IS NULL ";
		}
		
		return getPaginatedDataWithHQL(pageNo, QueryConstantRest.HOTEL_PAGE_SIZE, hqlQuery, null);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<HotelSatguruModel> getHotelInfo(int hotelId) throws Exception 
	{
		String query = QueryConstantRest.FETCH_HOTEL_INFO;
		List parameterList = new ArrayList<>();
		parameterList.add(hotelId);
		return fetchWithHQL(query, parameterList);
	}
	
	@Override
	public List<HotelCityDetailsModel> fetchCities() throws Exception
	{
		String query = QueryConstantRest.FETCH_HOTEL_CITIES;
		return fetchWithHQL(query, null);
	}
	
	@Override
	public List<HotelCountryDetailsModel> fetchCountries() throws Exception
	{
		String query = QueryConstantRest.FETCH_HOTEL_COUNTRIES;
		return fetchWithHQL(query, null);
	}
	@Override
	public List<Object[]> getHotelMapInfo(String hotelId) throws Exception 
	{
		String query = "SELECT TSHM1.CLARIFI_ID, TSHM1.SUPPLIER_HOTEL_CODE FROM TT_SATGURU_HOTEL_SUPP_MAP TSHM1 INNER JOIN TT_SATGURU_HOTEL_SUPP_MAP TSHM2  ON TSHM1.CLARIFI_ID = TSHM2.CLARIFI_ID WHERE TSHM2.SUPPLIER_HOTEL_CODE IN ("+hotelId+") GROUP BY TSHM1.CLARIFI_ID, TSHM1.SUPPLIER_HOTEL_CODE;";;
		return fetchWithSQL(query);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<HotelSatMappingModel> getHotelAllDetailsList(int clarifiId) throws Exception 
	{
		String query = QueryConstantRest.FETCH_HOTEL_ALL_DETAILS;
		List parameterList = new ArrayList<>();
		parameterList.add(clarifiId);
		return fetchWithHQL(query, parameterList);
	}
	
	@Override
	public List<Object[]> getPInventoryPriceData(HotelSearchRequestBean hotelSearchRequestBean)throws Exception 
	{
		CallLog.info(1, "inside getPInventoryPriceData DaoImpl");
		List<Object> paramList = new ArrayList<>();
		List<Object[]> resultList = null;


		if(null!=hotelSearchRequestBean.getCityName() && null!=hotelSearchRequestBean.getCountryName())
		{
			paramList.add(hotelSearchRequestBean.getCityName());
			paramList.add(hotelSearchRequestBean.getCountryName());
			paramList.add(RuleSimulationHelper.dateFormat(hotelSearchRequestBean.getCheckInDate(),DATE_FORMAT));
			paramList.add(RuleSimulationHelper.dateFormat(hotelSearchRequestBean.getCheckOutDate(),DATE_FORMAT));
			paramList.add(hotelSearchRequestBean.getNoOfRooms());

			List<HotelWidgetElement> widgetEle = hotelSearchRequestBean.getHotelWidget().getHotelWidgetElement();
			int count=0;
			for (int i = 0; i < widgetEle.size(); i++) 
			{
				paramList.add(widgetEle.get(i).getNoOfAdults());
				paramList.add(widgetEle.get(i).getNoOfChilds());
				count++;
			}
			if(count<9)
				for (int i = count; i <9; i++) 
				{
					paramList.add(0);
					paramList.add(0);
				}
			paramList.add("-1");
			resultList = fetchWithSQL("CALL fetchPIRoomPriceByCityCountry(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", paramList);
		}

		if(null!=resultList)
			CallLog.info(1, "Hotel Private Inventory Price Result Data List Size::"+resultList.size());

		return resultList;
	}

	@Override
	public List<Object[]> getHotelStaticInfo(String cityName, String countyName,String hotelName,Double lat, Double lon,Double distance,String limit, String hotelCheckIn,String hotelCheckOut)throws Exception 
	{
		CallLog.info(1, "inside getHotelStaticInfo dao");
		List<Object> paramList = new ArrayList<>();
		List<Object[]> resultList = null;
		if(lat!=null && lon!=null)
		{
			GeoLocation location = GeoLocation.fromDegrees(lat, lon);			
			CallLog.info(1, "inside getHotelStaticInfo dao distance::"+distance);
			GeoLocation[] boundingCoordinates =location.boundingCoordinates(distance, GeoLocation.EARTH_RADIUS);
			String meridian180WithinDistance =boundingCoordinates[0].getLongitudeInRadians() >boundingCoordinates[1].getLongitudeInRadians()?"OR" : "AND";
			paramList.add(boundingCoordinates[0].getLatitudeInRadians());
			paramList.add(boundingCoordinates[1].getLatitudeInRadians());
			paramList.add(boundingCoordinates[0].getLongitudeInRadians());
			paramList.add(boundingCoordinates[1].getLongitudeInRadians());
			paramList.add(location.getLatitudeInRadians());
			paramList.add(location.getLongitudeInRadians());
			paramList.add(distance / GeoLocation.EARTH_RADIUS);
			paramList.add(meridian180WithinDistance);
			if("limit".equalsIgnoreCase(limit.trim()))
			{
				paramList.add("LIMIT");
				paramList.add(hotelCheckIn);
				paramList.add(hotelCheckOut);
				resultList = fetchWithSQL("CALL fetchHotelDataByGeoLocation(?,?,?,?,?,?,?,?,?,?,?)", paramList);
			}
			else
			{
				paramList.add("");
				paramList.add(hotelCheckIn);
				paramList.add(hotelCheckOut);
				resultList = fetchWithSQL("CALL fetchHotelDataByGeoLocation(?,?,?,?,?,?,?,?,?,?,?)", paramList);
			}
			CallLog.info(1, "paramList::"+paramList.toString());
			CallLog.info(1, "Hotel Static new Size::"+resultList.size());
		}
		else
		{
			CallLog.info(1, "inside getHotelStaticInfo dao fetchHotelDataByNameCityCountry::");
			if(null != hotelName && !"".equalsIgnoreCase(hotelName))
				paramList.add(hotelName);
			else
				paramList.add("");	

			/*paramList.add(cityName);
			paramList.add(countyName);*/
			if("limit".equalsIgnoreCase(limit.trim()) && (""==hotelName || null==hotelName))
			{
				paramList.add("LIMIT");	
				paramList.add(cityName);
				paramList.add(countyName);
				paramList.add(hotelCheckIn);
				paramList.add(hotelCheckOut);
				resultList = fetchWithSQL("CALL fetchHotelDataByNameCityCountry(?,?,?,?,?,?)", paramList);
			}
			else
			{
				paramList.add("");	
				paramList.add(cityName);
				paramList.add(countyName);
				paramList.add(hotelCheckIn);
				paramList.add(hotelCheckOut);
				resultList = fetchWithSQL("CALL fetchHotelDataByNameCityCountry(?,?,?,?,?,?)", paramList);
			}

		}
		if(null!=resultList)
			CallLog.info(1, "Hotel Static Result Data List Size::"+resultList.size());

		return resultList;
	}
	@Override
	public List<Object[]> getDirectories() throws Exception {
		String query = QueryConstantRest.GET_DIRECTORIES;
		return fetchWithSQL(query, null);
	}

	@Override
	public void saveCacheTimeOut(Map<String,String> keyValuesMap){
		try {
			String query = QueryConstantRest.INSERT_KEY_VALUE_OF_CACHE;
			StringBuilder queryBuilder = new StringBuilder("");
			if(null != keyValuesMap && !keyValuesMap.isEmpty())
			{
				Set<Entry<String, String>> entrySet = keyValuesMap.entrySet();
				Iterator<Entry<String, String>> itr = entrySet.iterator();
				int count = 0;
				while(itr.hasNext())
				{
					Entry<String, String> entry = itr.next();
					Date today = Calendar.getInstance().getTime(); 
					java.sql.Date creationTime = new java.sql.Date(today.getTime());
					if(count==0)
						queryBuilder.append("(0,'"+entry.getKey()+"','"+entry.getValue()+"',1,'"+creationTime+"','"+creationTime+"',0,0,1),");
					else
						queryBuilder.append("(0,'"+entry.getKey()+"','"+entry.getValue()+"',1,'"+creationTime+"','"+creationTime+"',0,0,1)");
					
					count++;
				}
				query += queryBuilder.toString();
				executeSQLQuery(query, null);
			}
		} 
		catch (Exception e) {
			TTPortalLog.error(103, e.getMessage());
		}
	}
}
