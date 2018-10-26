package com.tt.ws.rest.hotel.services;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.ws.interaction.beans.ServiceRequestBean;
import com.ws.interaction.beans.ServiceResponseBean;
import com.ws.interaction.factory.ServiceResolverFactory;
import com.ws.services.hotel.bean.HotelAmendRequestBean;
import com.ws.services.hotel.bean.HotelAmendResponseBean;
import com.ws.services.hotel.bean.HotelAvailAndPricingReqBean;
import com.ws.services.hotel.bean.HotelAvailAndPricingRespBean;
import com.ws.services.hotel.bean.HotelBookRequestBean;
import com.ws.services.hotel.bean.HotelBookResponseBean;
import com.ws.services.hotel.bean.HotelCancelRequestBean;
import com.ws.services.hotel.bean.HotelCancelResponseBean;
import com.ws.services.hotel.bean.HotelChargeConditionRequestBean;
import com.ws.services.hotel.bean.HotelChargeConditionResponseBean;
import com.ws.services.hotel.bean.HotelCommonSearchRespBean;
import com.ws.services.hotel.bean.HotelEmergencyNumberRequestBean;
import com.ws.services.hotel.bean.HotelEmergencyNumberResponseBean;
import com.ws.services.hotel.bean.HotelInformationRequestBean;
import com.ws.services.hotel.bean.HotelInformationResponseBean;
import com.ws.services.hotel.bean.HotelPriceBreakDownRequestBean;
import com.ws.services.hotel.bean.HotelPriceBreakDownResponseBean;
import com.ws.services.hotel.bean.HotelSearchRequestBean;
import com.ws.services.hotel.bean.RoomRequestBean;
import com.ws.services.util.CallLog;
import com.ws.services.util.ProductEnum;
import com.ws.services.util.ProductServiceEnum;

@Service
public class GTAHotelService 
{
	String logStr = "[GTAHotelService]";
	ServiceResolverFactory serviceResolverFactory = null;
	RoomRequestBean roomRequestBean = null;
	ServiceRequestBean serviceRequestBean = null;
	ServiceResponseBean serviceResponseBean=null;
	String jsonString = null;
	
	public String searchHotel(@RequestBody HotelSearchRequestBean hotelSearchRequestBean)
	{
		logStr = logStr+" searchHotel() ";	
		String jsonString = null;
			serviceRequestBean = new ServiceRequestBean();
			
			serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
			serviceRequestBean.setServiceName(ProductEnum.Hotel.search.toString());
			serviceRequestBean.setServiceConfigModel(hotelSearchRequestBean.getServiceConfigModel());
			serviceRequestBean.setRequestBean(hotelSearchRequestBean);
			serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
			 serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
			 HotelCommonSearchRespBean hotelSearchResponseBean = (HotelCommonSearchRespBean) serviceResponseBean.getResponseBean();
	
			if(null!=hotelSearchResponseBean)
			{
				ObjectMapper mapper = new ObjectMapper();
				try {
			    	 jsonString = mapper.writeValueAsString(hotelSearchResponseBean);
			    	 CallLog.info(1,logStr+" Response Json::: "+jsonString);
				} catch (Exception e) {
					CallLog.printStackTrace(103,e);
				}
			}
			return jsonString;
	}
	
	
	public String searchHotelInfo(HotelInformationRequestBean requestBean)
	{
		String jsonString = null;
		logStr = logStr+" searchHotelInfo() ";	
		serviceRequestBean = new ServiceRequestBean();
	
		serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
		serviceRequestBean.setServiceName(ProductEnum.Hotel.information.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		
		serviceRequestBean.setRequestBean(requestBean);
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
				CallLog.printStackTrace(103,e);
			}
		}
	return jsonString;
	}
	
	public String pricebreakdown(@RequestBody HotelPriceBreakDownRequestBean requestBean)
	{
		logStr = logStr+" pricebreakdown() ";	
		String jsonString  = null;
		serviceRequestBean = new ServiceRequestBean();
	
		serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
		serviceRequestBean.setServiceName(ProductEnum.Hotel.pricebreakdown.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		
		 serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		 HotelPriceBreakDownResponseBean responseBean = (HotelPriceBreakDownResponseBean) serviceResponseBean.getResponseBean();

		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
		    	jsonString = mapper.writeValueAsString(responseBean);
		    	 CallLog.info(1,logStr+" Response Json::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(103,e);
			}
		}
	return jsonString;
	}
	
	public String amendhotel(@RequestBody HotelAmendRequestBean requestBean)
	{
		logStr = logStr+" amendhotel() ";	
		String jsonString  = null;
		serviceRequestBean = new ServiceRequestBean();
	
		serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
		serviceRequestBean.setServiceName(ProductEnum.Hotel.amendhotel.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		
		 serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		 HotelAmendResponseBean responseBean = (HotelAmendResponseBean) serviceResponseBean.getResponseBean();

		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
		    	jsonString = mapper.writeValueAsString(responseBean);
		    	 CallLog.info(1,logStr+" Response Json::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(103,e);
			}
		}
	return jsonString;
	}
	
	public String emergencynumber(@RequestBody HotelEmergencyNumberRequestBean requestBean)
	{
		String jsonString  = null;
		serviceRequestBean = new ServiceRequestBean();
		logStr = logStr+" emergencynumber() ";	
		serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
		serviceRequestBean.setServiceName(ProductEnum.Hotel.emergencynumber.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		
		 serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		 HotelEmergencyNumberResponseBean responseBean = (HotelEmergencyNumberResponseBean) serviceResponseBean.getResponseBean();

		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
		    	jsonString = mapper.writeValueAsString(responseBean);
		    	 CallLog.info(1,logStr+" Response Json::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(103,e);
			}
		}
	return jsonString;
	}
	
	public String chargeCondition(@RequestBody HotelChargeConditionRequestBean requestBean)
	{
		logStr = logStr+" chargeCondition() ";	
		String jsonString  = null;
		serviceRequestBean = new ServiceRequestBean();
	
		serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
		serviceRequestBean.setServiceName(ProductEnum.Hotel.charge.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		
		 serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		 HotelChargeConditionResponseBean responseBean = (HotelChargeConditionResponseBean) serviceResponseBean.getResponseBean();

		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
		    	jsonString = mapper.writeValueAsString(responseBean);
		    	 CallLog.info(1,logStr+" Response Json::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(103,e);
			}
		}
	return jsonString;
	}
	
	public String searchAvailAndPricing(@RequestBody HotelAvailAndPricingReqBean reqBean)
	{
		logStr = logStr+" searchAvailAndPricing() ";	
		String jsonString  = null;
		serviceRequestBean = new ServiceRequestBean();
	
		serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
		serviceRequestBean.setServiceName(ProductEnum.Hotel.hotelAvailAndPricing.toString());
		serviceRequestBean.setServiceConfigModel(reqBean.getServiceConfigModel());
		
		serviceRequestBean.setRequestBean(reqBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		
		 serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		 HotelAvailAndPricingRespBean responseBean = (HotelAvailAndPricingRespBean) serviceResponseBean.getResponseBean();

		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
		    	jsonString = mapper.writeValueAsString(responseBean);
		    	 CallLog.info(1," searchAvailAndPricing Response Json::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(103,e);
			}
		}
		return jsonString;
	}
	
	public  String hotelBooking(@RequestBody HotelBookRequestBean requestBean)  
	{		
		logStr = logStr+" hotelBooking() ";	
		serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
		serviceRequestBean.setServiceName(ProductEnum.Hotel.bookhotel.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS

		HotelBookResponseBean responseBean = (HotelBookResponseBean) serviceResponseBean.getResponseBean();
		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(responseBean);
				 CallLog.info(1,logStr+" Response Json::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(103,e);
			}
		}

		return jsonString;
	}
	
	public String searchHotelCancellation(@RequestBody HotelCancelRequestBean requestBean)
	{
		String jsonString = null;
		logStr = logStr+" searchHotelCancellation() ";	
		serviceRequestBean = new ServiceRequestBean();
		
		serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
		serviceRequestBean.setServiceName(ProductEnum.Hotel.cancelhotel.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		serviceRequestBean.setRequestBean(requestBean);

		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		 serviceResponseBean = serviceResolverFactory.getServiceResponse();// USING
		HotelCancelResponseBean responseBean = (HotelCancelResponseBean) serviceResponseBean.getResponseBean();
			if(null!=responseBean)
			{
				ObjectMapper mapper = new ObjectMapper();
				try {
			    	jsonString = mapper.writeValueAsString(responseBean);
			    	 CallLog.info(1,logStr+" Response Json::: "+jsonString);
				} catch (Exception e) {
					CallLog.printStackTrace(103,e);
				}
			}
		return jsonString;
	}
}
