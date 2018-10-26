package com.tt.ws.rest.hotel.dao;

import java.util.List;
import java.util.Map;

import com.tt.ws.rest.hotel.model.HotelCityDetailsModel;
import com.tt.ws.rest.hotel.model.HotelCountryDetailsModel;
import com.tt.ws.rest.hotel.model.HotelSatMappingModel;
import com.tt.ws.rest.hotel.model.HotelSatguruModel;
import com.tt.ws.rest.hotel.model.HotelCityDetailsModel;
import com.tt.ws.rest.hotel.model.HotelCountryDetailsModel;
import com.ws.services.hotel.bean.HotelSearchRequestBean;

public interface HotelDao {
	List<HotelCityDetailsModel> fetchCities() throws Exception;
	
	List<HotelCountryDetailsModel> fetchCountries() throws Exception;

	List<HotelSatguruModel> getHotelCompleteDetails(int pageNo, String cityName,String countryName,String hotelName) throws Exception;
	
	List<HotelSatguruModel> getHotelInfo(int hotelId) throws Exception;
	
	List<Object[]> getHotelMapInfo(String hotelId) throws Exception ;
	
	List<HotelSatMappingModel> getHotelAllDetailsList(int clarifiId) throws Exception;
	
	List<Object[]> getPInventoryPriceData(HotelSearchRequestBean hotelSearchRequestBean) throws Exception;
	
	List<Object[]> getHotelStaticInfo(String cityName, String countyName,String hotelName,Double lat, Double lon, Double distance,String limit,String hotelCheckIn,String hotelCheckOut) throws Exception;
	
	List<Object[]> getDirectories() throws Exception ;
	
	void saveCacheTimeOut(Map<String,String> keyValuesMap);
	
}
