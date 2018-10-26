package com.tt.ws.rest.hotel.services;

import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ws.interaction.beans.ServiceRequestBean;
import com.ws.interaction.beans.ServiceResponseBean;
import com.ws.interaction.factory.ServiceResolverFactory;
import com.ws.services.hotel.bean.HotelAllocationEnquiryRequestBean;
import com.ws.services.hotel.bean.HotelAllocationEnquiryResponseBean;
import com.ws.services.hotel.bean.HotelBookRequestBean;
import com.ws.services.hotel.bean.HotelBookResponseBean;
import com.ws.services.hotel.bean.HotelCancelConfirmRequestBean;
import com.ws.services.hotel.bean.HotelCancelConfirmResponseBean;
import com.ws.services.hotel.bean.HotelCancelRequestBean;
import com.ws.services.hotel.bean.HotelCancelResponseBean;
import com.ws.services.hotel.bean.HotelCancellationDetailsRequestBean;
import com.ws.services.hotel.bean.HotelCancellationDetailsResponsetBean;
import com.ws.services.hotel.bean.HotelCityRequestBean;
import com.ws.services.hotel.bean.HotelCityResponseBean;
import com.ws.services.hotel.bean.HotelCommonSearchRespBean;
import com.ws.services.hotel.bean.HotelInformationRequestBean;
import com.ws.services.hotel.bean.HotelInformationResponseBean;
import com.ws.services.hotel.bean.HotelSearchRequestBean;
import com.ws.services.hotel.bean.HotelStarRequestBean;
import com.ws.services.hotel.bean.HotelStarResponseBean;
import com.ws.services.hotel.bean.RoomRequestBean;
import com.ws.services.util.CallLog;
import com.ws.services.util.ProductEnum;
import com.ws.services.util.ProductServiceEnum;

@Service
public class TravcoHotelService 
{
	String logStr = "[TravcoHotelService]";
	ServiceResolverFactory serviceResolverFactory = null;
	RoomRequestBean roomRequestBean = null;
	ServiceRequestBean serviceRequestBean = null;
	ServiceResponseBean serviceResponseBean=null;
	String jsonString = null;
	
	public String searchHotel(HotelSearchRequestBean hotelSearchRequestBean)
	{
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
				} catch (Exception e) {
					CallLog.printStackTrace(103,e);
				}
			}
			return jsonString;
	}
	
	public String searchHotelInfo(HotelInformationRequestBean requestBean)
	{
		String jsonString  = null;
		serviceRequestBean = new ServiceRequestBean();
		
		//requestBean.setHotelCode("YYL");
		
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
			} catch (Exception e) {
				CallLog.printStackTrace(103,e);
			}
		}
	return jsonString;
	}
	
	public String searchHotelAllocation(HotelAllocationEnquiryRequestBean requestBean)
	{
		String jsonString = null;
				
		serviceRequestBean = new ServiceRequestBean();
		
		serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
		serviceRequestBean.setServiceName(ProductEnum.Hotel.hotelallocationenquiry.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		serviceRequestBean.setRequestBean(requestBean);

		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		 serviceResponseBean = serviceResolverFactory.getServiceResponse();// USING
		HotelAllocationEnquiryResponseBean responseBean = (HotelAllocationEnquiryResponseBean) serviceResponseBean.getResponseBean();
			if(null!=responseBean)
			{
				ObjectMapper mapper = new ObjectMapper();
				try {
			    	jsonString = mapper.writeValueAsString(responseBean);
				} catch (Exception e) {
					CallLog.printStackTrace(103,e);
				}
			}
		return jsonString;
	}
	
	public  String hotelSingleCity(@RequestBody HotelCityRequestBean requestBean)  
	{		
			HotelCityResponseBean responseBean=null;
			String jsonString="";
			try{
			
			serviceRequestBean = new ServiceRequestBean();
			
			serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
			serviceRequestBean.setServiceName(ProductEnum.Hotel.hotelsinglecity.toString());
			serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
			serviceRequestBean.setRequestBean(requestBean);
			serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
			 serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
			responseBean = (HotelCityResponseBean) serviceResponseBean.getResponseBean();
			if(null!=responseBean)
			{
				ObjectMapper mapper = new ObjectMapper();
				try {
			    	 jsonString = mapper.writeValueAsString(responseBean);
				} catch (Exception e) {
					CallLog.printStackTrace(103,e);
				}
			}
			}
			catch (Exception e) {
				CallLog.printStackTrace(103,e);
				}
		return jsonString;
	}
	

	@RequestMapping(value = "/hotelStarRating",produces=MediaType.APPLICATION_JSON_VALUE)
	public  String hotelStarRating(HotelStarRequestBean requestBean)  
	{		
		HotelStarResponseBean responseBean=null;
			String jsonString="";
			try{
			
			serviceRequestBean = new ServiceRequestBean();
			
			serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
			serviceRequestBean.setServiceName(ProductEnum.Hotel.hotelstar.toString());
			serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
			serviceRequestBean.setRequestBean(requestBean);
			serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
			 serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
			responseBean = (HotelStarResponseBean) serviceResponseBean.getResponseBean();
			if(null!=responseBean)
			{
				ObjectMapper mapper = new ObjectMapper();
				try {
			    	 jsonString = mapper.writeValueAsString(responseBean);
				} catch (Exception e) {
					CallLog.printStackTrace(103,e);
				}
			}
			}
			catch (Exception e) {
				CallLog.printStackTrace(103,e);
				}
		return jsonString;
	}
	
	public  String hotelCancelDetails(HotelCancellationDetailsRequestBean requestBean)  
	{		
		HotelCancellationDetailsResponsetBean responseBean=null;
			String jsonString="";
			try{
			serviceRequestBean = new ServiceRequestBean();
			
			serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
			serviceRequestBean.setServiceName(ProductEnum.Hotel.hotelcanceldetails.toString());
			serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
					
			serviceRequestBean.setRequestBean(requestBean);
			serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
			 serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
			responseBean = (HotelCancellationDetailsResponsetBean) serviceResponseBean.getResponseBean();
			if(null!=responseBean)
			{
				ObjectMapper mapper = new ObjectMapper();
				try {
			    	 jsonString = mapper.writeValueAsString(responseBean);
				} catch (Exception e) {
					CallLog.printStackTrace(103,e);
				}
			}
			}
			catch (Exception e) {
				CallLog.printStackTrace(103,e);
				}
		return jsonString;
	}
	
	public  String hotelBooking(@RequestBody HotelBookRequestBean requestBean)  
	{		
		HotelBookResponseBean responseBean=null;
			String jsonString="";
			try{
			 serviceRequestBean = new ServiceRequestBean();
			 Map<String, String> shippingDetailsMap;
			serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
			serviceRequestBean.setServiceName(ProductEnum.Hotel.bookhotel.toString());
			serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
			shippingDetailsMap =requestBean.getShippingDetailMap();
		
			// String reqRoom = "none";//None
			// String reqRoom = "nsrro";//Non-Smoking Room (Request Only)
			String reqRoom = "srro";// Smoking Room (Request Only)
			String eciro = "eciro";// Early Check-in (Request Only)
			String lcoro = "lcoro";// Late Check-out (Request Only)

			StringBuilder hotelRequest = new StringBuilder();
			if (reqRoom != null)
			{
				if (reqRoom.equalsIgnoreCase("none"))
				{
					hotelRequest.append("None");
					if (eciro != null)
					{
						hotelRequest.append(", Early Check-in (Request Only)");
					}
					if (lcoro != null)
					{
						hotelRequest.append(", Late Check-out (Request Only)");
					}
				}
				else if (reqRoom.equalsIgnoreCase("nsrro"))
				{
					hotelRequest.append("Non-Smoking Room (Request Only)");
					if (eciro != null)
					{
						hotelRequest.append(", Early Check-in (Request Only)");
					}
					if (lcoro != null)
					{
						hotelRequest.append(", Late Check-out (Request Only)");
					}
				}
				else if (reqRoom.equalsIgnoreCase("srro"))
				{
					hotelRequest.append("Smoking Room (Request Only)");
					if (eciro != null)
					{
						hotelRequest.append(", Early Check-in (Request Only)");
					}
					if (lcoro != null)
					{
						hotelRequest.append(", Late Check-out (Request Only)");
					}
				}
			}

			if (!hotelRequest.toString().equals(""))
				shippingDetailsMap.put("hotelRequest", hotelRequest.toString());
			else
				shippingDetailsMap.put("hotelRequest", "");
			requestBean.setShippingDetailMap(shippingDetailsMap);
			
			serviceRequestBean.setRequestBean(requestBean);
			serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
			 serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
			responseBean = (HotelBookResponseBean) serviceResponseBean.getResponseBean();
			if(null!=responseBean)
			{
				ObjectMapper mapper = new ObjectMapper();
				try {
			    	 jsonString = mapper.writeValueAsString(responseBean);
				} catch (Exception e) {
					CallLog.printStackTrace(103,e);
				}
			}
			}
			catch (Exception e) {
				CallLog.printStackTrace(103,e);
				}
		return jsonString;
	}
	
	public String searchHotelCancellation(@RequestBody HotelCancelRequestBean requestBean)
	{
		String jsonString = null;
		
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
				} catch (Exception e) {
					CallLog.printStackTrace(103,e);
				}
			}
		return jsonString;
	}
	
	public String getHotelCancelConfirm(@RequestBody HotelCancelConfirmRequestBean requestBean)
	{
		logStr = logStr+"[getHotelCancelConfirm]";
		String jsonString = null;
		
		serviceRequestBean = new ServiceRequestBean();
		
		serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
		serviceRequestBean.setServiceName(ProductEnum.Hotel.hotelcancelconfrim.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		serviceRequestBean.setRequestBean(requestBean);

		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();// USING
		HotelCancelConfirmResponseBean responseBean = (HotelCancelConfirmResponseBean) serviceResponseBean.getResponseBean();
			if(null!=responseBean)
			{
				ObjectMapper mapper = new ObjectMapper();
				try {
			    	jsonString = mapper.writeValueAsString(responseBean);
				} catch (Exception e) {
					CallLog.printStackTrace(103,e);
				}
			}
		return jsonString;
	}
}
