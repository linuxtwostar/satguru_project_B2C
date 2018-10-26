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
import com.ws.services.hotel.bean.HotelInformationRequestBean;
import com.ws.services.hotel.bean.HotelInformationResponseBean;
import com.ws.services.hotel.bean.HotelRateCommentReqBean;
import com.ws.services.hotel.bean.HotelRateCommentRespBean;
import com.ws.services.hotel.bean.RoomRequestBean;
import com.ws.services.util.CallLog;
import com.ws.services.util.ProductEnum;
import com.ws.services.util.ProductServiceEnum;

@Service
public class HotelBedsHotelService 
{
	String logStr = "[HotelBedsHotelService]";
	ServiceResolverFactory serviceResolverFactory = null;
	RoomRequestBean roomRequestBean = null;
	ServiceRequestBean serviceRequestBean = null;
	ServiceResponseBean serviceResponseBean=null;
	String jsonString = null;
	
	
	public String searchAvailAndPricing( HotelAvailAndPricingReqBean reqBean)
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
		
		
		public  String hotelBooking( HotelBookRequestBean requestBean)  
		{		
			HotelBookResponseBean responseBean = null;
			try
			{
				logStr = logStr+" hotelBooking() ";
				serviceRequestBean = new ServiceRequestBean();
				
				serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
				serviceRequestBean.setServiceName(ProductEnum.Hotel.bookhotel.toString());
				serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
				serviceRequestBean.setRequestBean(requestBean);
				serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
				serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
				responseBean = (HotelBookResponseBean) serviceResponseBean.getResponseBean();
				if(null!=responseBean)
				{
					ObjectMapper mapper = new ObjectMapper();
				    jsonString = mapper.writeValueAsString(responseBean);
					CallLog.info(1,logStr+" Response Json::: "+jsonString);
				}
			}
			catch (Exception e) 
			{
				CallLog.printStackTrace(1, e);
			}
			return jsonString;
		}
		
		public  String hotelBookingDetails( HotelBookingDetailsReqBean requestBean)  
		{		
			HotelBookingDetailsRespBean responseBean = null;
			try
			{
				logStr = logStr+" hotelBookingDetails() ";
				serviceRequestBean = new ServiceRequestBean();
				
				serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
				serviceRequestBean.setServiceName(ProductEnum.Hotel.hotelBookingDetails.toString());
				serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
				serviceRequestBean.setRequestBean(requestBean);
				serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
				serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
				responseBean = (HotelBookingDetailsRespBean) serviceResponseBean.getResponseBean();
				if(null!=responseBean)
				{
					ObjectMapper mapper = new ObjectMapper();
				    jsonString = mapper.writeValueAsString(responseBean);
					CallLog.info(1,logStr+" Response Json::: "+jsonString);
				}
			}
			catch (Exception e) 
			{
				CallLog.printStackTrace(1, e);
			}
			return jsonString;
		}
		
		public String searchHotelCancellation( HotelCancelRequestBean reqBean)	
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
		public HotelInformationResponseBean hotelInformation(HotelInformationRequestBean hotelInfoReqBean)
		{
			logStr = logStr+ "hotelInformation()";
		    serviceRequestBean = new ServiceRequestBean();
			serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
			serviceRequestBean.setServiceName(ProductEnum.Hotel.information.toString());		
			serviceRequestBean.setServiceConfigModel(hotelInfoReqBean.getServiceConfigModel());
			serviceRequestBean.setRequestBean(hotelInfoReqBean);
			serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
			serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
			return (HotelInformationResponseBean) serviceResponseBean.getResponseBean();	
		}
		
		public String rateCommentsDetails( HotelRateCommentReqBean reqBean)	
		{
			logStr = logStr+" searchHotelCancellation() ";
			
			HotelRateCommentRespBean responseBean;
			
			serviceRequestBean = new ServiceRequestBean();
			serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
			serviceRequestBean.setServiceName(ProductEnum.Hotel.rateComment.toString());
			serviceRequestBean.setServiceConfigModel(reqBean.getServiceConfigModel());
			serviceRequestBean.setRequestBean(reqBean);
			serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
			serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
			responseBean = (HotelRateCommentRespBean) serviceResponseBean.getResponseBean();
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
}
