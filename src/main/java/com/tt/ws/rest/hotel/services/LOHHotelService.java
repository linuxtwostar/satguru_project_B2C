package com.tt.ws.rest.hotel.services;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;

import com.ws.interaction.beans.ServiceRequestBean;
import com.ws.interaction.beans.ServiceResponseBean;
import com.ws.interaction.factory.ServiceResolverFactory;
import com.ws.services.hotel.bean.HotelBookRequestBean;
import com.ws.services.hotel.bean.HotelBookResponseBean;
import com.ws.services.hotel.bean.HotelBookingRulesReqBean;
import com.ws.services.hotel.bean.HotelBookingRulesRespBean;
import com.ws.services.hotel.bean.HotelCancelRequestBean;
import com.ws.services.hotel.bean.HotelCancelResponseBean;
import com.ws.services.hotel.bean.HotelCityRequestBean;
import com.ws.services.hotel.bean.HotelCityResponseBean;
import com.ws.services.hotel.bean.HotelCountryRequestBean;
import com.ws.services.hotel.bean.HotelCountryResponseBean;
import com.ws.services.hotel.bean.HotelRoomAvailRequestBean;
import com.ws.services.hotel.bean.HotelRoomAvailResponseBean;
import com.ws.services.hotel.bean.RoomRequestBean;
import com.ws.services.util.CallLog;
import com.ws.services.util.ProductEnum;
import com.ws.services.util.ProductServiceEnum;

@Service
public class LOHHotelService 
{
	String logStr = "[LOHHotelService]";
	ServiceResolverFactory serviceResolverFactory = null;
	RoomRequestBean roomRequestBean = null;
	ServiceRequestBean serviceRequestBean = null;
	ServiceResponseBean serviceResponseBean=null;
	String jsonString = null;
	
	public String searchHotelRoomAvail(HotelRoomAvailRequestBean hotelRoomRequestBean)
	{
		logStr=	logStr+ "searchHotelRoomAvail()";
	    serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
		serviceRequestBean.setServiceName(ProductEnum.Hotel.roomavail.toString());
		serviceRequestBean.setServiceConfigModel(hotelRoomRequestBean.getServiceConfigModel());
		//serviceRequestBean.setServiceConfigModel(HotelUtil.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(hotelRoomRequestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		HotelRoomAvailResponseBean hotelSearchResponseBean = (HotelRoomAvailResponseBean) serviceResponseBean.getResponseBean();
		if(null!=hotelSearchResponseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(hotelSearchResponseBean);
				CallLog.info(1,logStr+ " Response bean values::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}
		return jsonString;	
	}
	
	public String hotelBookRules(HotelBookingRulesReqBean requestBean)
	{
		logStr=	logStr+ "hotelBookRules()";
	    serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
		serviceRequestBean.setServiceName(ProductEnum.Hotel.bookingRules.toString());
		
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		//serviceRequestBean.setServiceConfigModel(HotelUtil.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		HotelBookingRulesRespBean hotelResponseBean = (HotelBookingRulesRespBean) serviceResponseBean.getResponseBean();
		if(null!=hotelResponseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(hotelResponseBean);
				CallLog.info(1,logStr+ "  Response bean values::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}
		return jsonString;	
	}
	
	public String hotelBooking(HotelBookRequestBean requestBean)
	{
		logStr=	logStr+ "hotelBooking()";
	    serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
		serviceRequestBean.setServiceName(ProductEnum.Hotel.bookhotel.toString());		
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		//serviceRequestBean.setServiceConfigModel(HotelUtil.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		HotelBookResponseBean hotelResponseBean = (HotelBookResponseBean) serviceResponseBean.getResponseBean();
		if(null!=hotelResponseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(hotelResponseBean);
				CallLog.info(1,logStr+ " Response bean values::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}
		return jsonString;	
	}
	
	public String hotelCancel(HotelCancelRequestBean requestBean)
	{
		logStr=	logStr+ "hotelCancel()";
		
	    serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
		serviceRequestBean.setServiceName(ProductEnum.Hotel.cancelhotel.toString());		
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		//serviceRequestBean.setServiceConfigModel(HotelUtil.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		HotelCancelResponseBean hotelResponseBean = (HotelCancelResponseBean) serviceResponseBean.getResponseBean();
		if(null!=hotelResponseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(hotelResponseBean);
				CallLog.info(1,logStr+ " Response bean values::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}
		return jsonString;	
	}
	
	
	public String hotelCountryList(HotelCountryRequestBean countryRequestBean)
	{
		logStr=	logStr+ "hotelCountryList()";
	    serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
		serviceRequestBean.setServiceName(ProductEnum.Hotel.hotelcountry.toString());		
		serviceRequestBean.setServiceConfigModel(countryRequestBean.getServiceConfigModel());
		//serviceRequestBean.setServiceConfigModel(HotelUtil.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(countryRequestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		HotelCountryResponseBean hotelSearchResponseBean = (HotelCountryResponseBean) serviceResponseBean.getResponseBean();
		if(null!=hotelSearchResponseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(hotelSearchResponseBean);
				CallLog.info(1,logStr+ " Response bean values::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}
		return jsonString;	
	}
	
	
	public String hotelCityList(HotelCityRequestBean cityRequestBean)
	{
		logStr=	logStr+ "hotelCityList()";
	    serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
		serviceRequestBean.setServiceName(ProductEnum.Hotel.hotelcity.toString());		
		serviceRequestBean.setServiceConfigModel(cityRequestBean.getServiceConfigModel());
		//serviceRequestBean.setServiceConfigModel(HotelUtil.setServiceConfigModel());
		serviceRequestBean.setRequestBean(cityRequestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		HotelCityResponseBean hotelSearchResponseBean = (HotelCityResponseBean) serviceResponseBean.getResponseBean();
		if(null!=hotelSearchResponseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(hotelSearchResponseBean);
				CallLog.info(1,logStr+ " Response bean values::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}
		return jsonString;	
	}
}
