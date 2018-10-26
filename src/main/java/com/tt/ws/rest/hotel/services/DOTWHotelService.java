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
import com.ws.services.hotel.bean.HotelBookingDetailsReqBean;
import com.ws.services.hotel.bean.HotelBookingDetailsRespBean;
import com.ws.services.hotel.bean.HotelCancelRequestBean;
import com.ws.services.hotel.bean.HotelCancelResponseBean;
import com.ws.services.hotel.bean.RoomRequestBean;
import com.ws.services.util.CallLog;
import com.ws.services.util.ProductEnum;
import com.ws.services.util.ProductServiceEnum;

@Service
public class DOTWHotelService 
{
	String logStr = "[DOTWHotelService]";
	ServiceResolverFactory serviceResolverFactory = null;
	RoomRequestBean roomRequestBean = null;
	ServiceRequestBean serviceRequestBean = null;
	ServiceResponseBean serviceResponseBean=null;
	String jsonString = null;
	
	
	public String searchHotelAvailAndPricing(HotelAvailAndPricingReqBean hotelAvailAndPricingReqBean)
	{
	    serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
		serviceRequestBean.setServiceName(ProductEnum.Hotel.hotelAvailAndPricing.toString());
		serviceRequestBean.setServiceConfigModel(hotelAvailAndPricingReqBean.getServiceConfigModel());
		serviceRequestBean.setRequestBean(hotelAvailAndPricingReqBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		HotelAvailAndPricingRespBean hotelAvailAndPricingRespBean = (HotelAvailAndPricingRespBean) serviceResponseBean.getResponseBean();
		if(null!=hotelAvailAndPricingRespBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(hotelAvailAndPricingRespBean);
				CallLog.info(1,"DOTWHotelService.searchHotelAvailAndPricing Response bean values::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}
		return jsonString;	
	}
	

	public String hotelBooking(HotelBookRequestBean requestBean)
	{
	    serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
		serviceRequestBean.setServiceName(ProductEnum.Hotel.bookhotel.toString());		
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		HotelBookResponseBean hotelResponseBean = (HotelBookResponseBean) serviceResponseBean.getResponseBean();
		if(null!=hotelResponseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(hotelResponseBean);
				CallLog.info(1,"DOTWHotelService Response bean values for hotelBookResponse::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}
		return jsonString;	
	}
	
	public String searchHotelBookingDetails(HotelBookingDetailsReqBean reqBean)	
	{
		logStr = logStr+" searchHotelCancellation() ";
		serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
		serviceRequestBean.setServiceName(ProductEnum.Hotel.hotelBookingDetails.toString());
		serviceRequestBean.setServiceConfigModel(reqBean.getServiceConfigModel());
		serviceRequestBean.setRequestBean(reqBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		HotelBookingDetailsRespBean bookingDetailsResponceBean = (HotelBookingDetailsRespBean) serviceResponseBean.getResponseBean();
		if(null!=bookingDetailsResponceBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try 
			{
				jsonString = mapper.writeValueAsString(bookingDetailsResponceBean);
				CallLog.info(1,logStr+" Response Json:::" + jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}
		return jsonString;
	}
	
	public String hotelCancel(HotelCancelRequestBean requestBean)
	{
	serviceRequestBean = new ServiceRequestBean();
	serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
	serviceRequestBean.setServiceName(ProductEnum.Hotel.cancelhotel.toString());		
	//serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
	serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
	serviceRequestBean.setRequestBean(requestBean);
	serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
	serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
	HotelCancelResponseBean hotelResponseBean = (HotelCancelResponseBean) serviceResponseBean.getResponseBean();
	if(null!=hotelResponseBean)
	{
		ObjectMapper mapper = new ObjectMapper();
		try {
			jsonString = mapper.writeValueAsString(hotelResponseBean);
			CallLog.info(1,"DOTWHotelService Response bean values for hotelCancelConfirm::: "+jsonString);
		} catch (Exception e) {
			CallLog.printStackTrace(1,e);
		}
	}
	return jsonString;	
	}
}
