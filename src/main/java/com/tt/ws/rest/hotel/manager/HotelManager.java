package com.tt.ws.rest.hotel.manager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.tt.ws.rest.hotel.dao.HotelDao;
import com.tt.ws.rest.hotel.model.HotelSatMappingModel;
import com.tt.ws.rest.hotel.model.HotelSatguruModel;
import com.tt.ws.rest.hotel.model.HotelCityDetailsModel;
import com.tt.ws.rest.hotel.model.HotelCountryDetailsModel;
import com.ws.services.hotel.bean.HotelSearchRequestBean;
import com.ws.services.util.CallLog;

@Repository
@Component
public class HotelManager 
{
	@Autowired
	private HotelDao hotelDao;

	public List<HotelSatguruModel> getHotelCompleteDetails(int pageNo,String cityName,String countryName,String hotelName) throws Exception {
		return hotelDao.getHotelCompleteDetails(pageNo,cityName,countryName,hotelName);
	}
	public List<HotelSatguruModel> getHotelInfo(int hotelId) throws Exception {
		return hotelDao.getHotelInfo(hotelId);
	}
	
	public List<HotelCityDetailsModel> fetchHotelCities() throws Exception
	{
		return hotelDao.fetchCities();
	}
	public List<HotelCountryDetailsModel> fetchHotelCountries() throws Exception
	{
		return hotelDao.fetchCountries();
	}
	public List<Object[]> getHotelMapInfo(String hotelId) throws Exception
	{
		return hotelDao.getHotelMapInfo(hotelId);
	}
	public List<HotelSatMappingModel> getHotelAllDetailsList(int clarifiId) throws Exception
	{
		return hotelDao.getHotelAllDetailsList(clarifiId);
	}
	public List<Object[]> getPInventoryPriceData(HotelSearchRequestBean hotelSearchRequestBean)throws Exception 
	{
		return hotelDao.getPInventoryPriceData(hotelSearchRequestBean);
	}
	
	public List<Object[]> getHotelStaticInfo(String cityName, String countyName,String hotelName,Double lat, Double lon,Double distance,String limit, String hotelCheckIn, String hotelCheckOut)throws Exception 
	{
		return hotelDao.getHotelStaticInfo(cityName, countyName,hotelName,lat,lon,distance,limit,hotelCheckIn,hotelCheckOut);
	}
}
