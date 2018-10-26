package com.tt.ws.rest.hotel.services;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;

import com.ws.interaction.beans.ServiceRequestBean;
import com.ws.interaction.beans.ServiceResponseBean;
import com.ws.interaction.factory.ServiceResolverFactory;
import com.ws.services.hotel.bean.HotelAvailAndPricingReqBean;
import com.ws.services.hotel.bean.HotelAvailAndPricingRespBean;
import com.ws.services.hotel.bean.HotelBookRequestBean;
import com.ws.services.hotel.bean.HotelBookResponseBean;
import com.ws.services.hotel.bean.HotelCancelRequestBean;
import com.ws.services.hotel.bean.HotelCancelResponseBean;
import com.ws.services.hotel.bean.HotelDealsRequestBean;
import com.ws.services.hotel.bean.HotelDealsResponseBean;
import com.ws.services.hotel.bean.HotelInformationRequestBean;
import com.ws.services.hotel.bean.HotelInformationResponseBean;
import com.ws.services.hotel.bean.HotelRoomAvailRequestBean;
import com.ws.services.hotel.bean.HotelRoomAvailResponseBean;
import com.ws.services.hotel.bean.RoomRequestBean;
import com.ws.services.util.CallLog;
import com.ws.services.util.ProductEnum;
import com.ws.services.util.ProductServiceEnum;

@Service
public class ExpediaHotelService 
{
	String logStr = "[ExpediaHotelServices]";
	ServiceResolverFactory serviceResolverFactory = null;
	RoomRequestBean roomRequestBean = null;
	ServiceRequestBean serviceRequestBean = null;
	ServiceResponseBean serviceResponseBean=null;
	String jsonString = null;
	
	
	public String searchHotelInfo(HotelInformationRequestBean reqBean)
	{
		logStr = logStr+" searchHotelInfo() ";
		serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
		serviceRequestBean.setServiceName(ProductEnum.Hotel.information.toString());
		serviceRequestBean.setServiceConfigModel(reqBean.getServiceConfigModel());
		serviceRequestBean.setRequestBean(reqBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		HotelInformationResponseBean hotelInformationResponseBean = (HotelInformationResponseBean) serviceResponseBean.getResponseBean();
		if(null!=hotelInformationResponseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(hotelInformationResponseBean);
				//CallLog.info(1,logStr+" Response Json::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}
		return jsonString;
	}
	
	public String searchAvailAndPricing(HotelAvailAndPricingReqBean reqBean)
	{
		logStr = logStr+" searchAvailAndPricing() ";
	    serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
		serviceRequestBean.setServiceName(ProductEnum.Hotel.hotelAvailAndPricing.toString());
		serviceRequestBean.setServiceConfigModel(reqBean.getServiceConfigModel());
		serviceRequestBean.setRequestBean(reqBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		HotelAvailAndPricingRespBean hotelSearchResponseBean = (HotelAvailAndPricingRespBean) serviceResponseBean.getResponseBean();
		if(null!=hotelSearchResponseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(hotelSearchResponseBean);
				CallLog.info(1," searchAvailAndPricing Response Json::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}
		return jsonString;	
	}
	
	public String searchHotelRoomAvailbility( HotelRoomAvailRequestBean reqBean)
	{
		logStr = logStr+" searchHotelRoomAvailbility() ";
	    serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
		serviceRequestBean.setServiceName(ProductEnum.Hotel.roomavail.toString());
		serviceRequestBean.setServiceConfigModel(reqBean.getServiceConfigModel());
		serviceRequestBean.setRequestBean(reqBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		HotelRoomAvailResponseBean hotelSearchResponseBean = (HotelRoomAvailResponseBean) serviceResponseBean.getResponseBean();
		if(null!=hotelSearchResponseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(hotelSearchResponseBean);
				CallLog.info(1,logStr+" Response Json::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}
		return jsonString;	
	}
	public  String hotelBooking( HotelBookRequestBean reqBean)  
	{		
		logStr = logStr+" hotelBooking() ";
		
		serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
		serviceRequestBean.setServiceName(ProductEnum.Hotel.bookhotel.toString());
		serviceRequestBean.setServiceConfigModel(reqBean.getServiceConfigModel());
		serviceRequestBean.setRequestBean(reqBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS

		HotelBookResponseBean responseBean = (HotelBookResponseBean) serviceResponseBean.getResponseBean();
		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(responseBean);
				CallLog.info(1,logStr+" Response Json::: "+jsonString);
			} 
			catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}

		return jsonString;
	}
	
	public String searchHotelCancellation(HotelCancelRequestBean reqBean)	
	{
		logStr = logStr+" searchHotelCancellation() ";
		
		HotelCancelResponseBean responseBean;
		
		serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
		serviceRequestBean.setServiceName(ProductEnum.Hotel.cancelhotel.toString());
		serviceRequestBean.setServiceConfigModel(reqBean.getServiceConfigModel());
		serviceRequestBean.setRequestBean(reqBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		responseBean = (HotelCancelResponseBean) serviceResponseBean.getResponseBean();
		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try 
			{
				jsonString = mapper.writeValueAsString(responseBean);
				CallLog.info(1,logStr+" Response Json:::" + jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}
		return jsonString;
	}
	
	public String searchHotelDealsDetails(HotelDealsRequestBean reqBean)	
	{
		logStr = logStr+" searchHotelDealsDetails() ";
		serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
		serviceRequestBean.setServiceName(ProductEnum.Hotel.deals.toString());
		serviceRequestBean.setServiceConfigModel(reqBean.getServiceConfigModel());
		serviceRequestBean.setRequestBean(reqBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		HotelDealsResponseBean respBean = (HotelDealsResponseBean) serviceResponseBean.getResponseBean();
		if(null!=respBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try 
			{
				jsonString = mapper.writeValueAsString(respBean);
				CallLog.info(1,logStr+" Response Json:::" + jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}
		return jsonString;
	}
}
