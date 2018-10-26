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
import com.ws.services.hotel.bean.HotelBookingDetailsReqBean;
import com.ws.services.hotel.bean.HotelBookingDetailsRespBean;
import com.ws.services.hotel.bean.HotelCancelConfirmRequestBean;
import com.ws.services.hotel.bean.HotelCancelConfirmResponseBean;
import com.ws.services.hotel.bean.HotelCancellationDetailsRequestBean;
import com.ws.services.hotel.bean.HotelCancellationDetailsResponsetBean;
import com.ws.services.hotel.bean.HotelCityRequestBean;
import com.ws.services.hotel.bean.HotelCityResponseBean;
import com.ws.services.hotel.bean.HotelCountryRequestBean;
import com.ws.services.hotel.bean.HotelCountryResponseBean;
import com.ws.services.hotel.bean.HotelDetailsRequestBean;
import com.ws.services.hotel.bean.HotelDetailsResponseBean;
import com.ws.services.hotel.bean.HotelGenerateInvoiceReqBean;
import com.ws.services.hotel.bean.HotelGenerateInvoiceRespBean;
import com.ws.services.hotel.bean.HotelRoomAvailRequestBean;
import com.ws.services.hotel.bean.HotelRoomAvailResponseBean;
import com.ws.services.hotel.bean.RoomRequestBean;
import com.ws.services.util.CallLog;
import com.ws.services.util.ProductEnum;
import com.ws.services.util.ProductServiceEnum;

@Service
public class TBOHotelService 
{
	String logStr = "[TBOHotelServices]";
	ServiceResolverFactory serviceResolverFactory = null;
	RoomRequestBean roomRequestBean = null;
	ServiceRequestBean serviceRequestBean = null;
	ServiceResponseBean serviceResponseBean=null;
	String jsonString = null;
	
	public  String hotelDetails(@RequestBody HotelDetailsRequestBean requestBean)  
	{		
		HotelDetailsResponseBean responseBean = null;
			try
			{
				serviceRequestBean = new ServiceRequestBean();
				
				serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
				serviceRequestBean.setServiceName(ProductEnum.Hotel.hotelDetails.toString());
				serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
						
				serviceRequestBean.setRequestBean(requestBean);
				serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
				serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
				responseBean = (HotelDetailsResponseBean) serviceResponseBean.getResponseBean();
				if(null!=responseBean)
				{
					ObjectMapper mapper = new ObjectMapper();
					jsonString = mapper.writeValueAsString(responseBean);
					CallLog.info(1,"TBOHotelServices.java hotelDetails Response bean values::: "+jsonString);
				}
			}
			catch (Exception e) 
			{
				CallLog.printStackTrace(1, e);
			}
		return jsonString;
	}
	
	public String searchHotelRoomAvail(HotelRoomAvailRequestBean hotelSearchRequestBean)
	{
	    serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
		serviceRequestBean.setServiceName(ProductEnum.Hotel.roomavail.toString());
		serviceRequestBean.setServiceConfigModel(hotelSearchRequestBean.getServiceConfigModel());
		serviceRequestBean.setRequestBean(hotelSearchRequestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		HotelRoomAvailResponseBean hotelSearchResponseBean = (HotelRoomAvailResponseBean) serviceResponseBean.getResponseBean();
		if(null!=hotelSearchResponseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(hotelSearchResponseBean);
				CallLog.info(1,"TBOHotelServices.java Response bean values::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}
		return jsonString;	
	}
		
	public  String hotelCancelDetails(HotelCancellationDetailsRequestBean requestBean)  
	{		
			HotelCancellationDetailsResponsetBean responseBean = null;
			try
			{
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
					jsonString = mapper.writeValueAsString(responseBean);
					CallLog.info(1,"TBOHotelServices.java hotelCancelDetails Response bean values::: "+jsonString);
				}
			}
			catch (Exception e) 
			{
				CallLog.printStackTrace(1, e);
			}
		return jsonString;
	}
	
	public  String hotelAvailAndPricing(HotelAvailAndPricingReqBean requestBean)  
	{		
		HotelAvailAndPricingRespBean responseBean = null;
			try
			{
				serviceRequestBean = new ServiceRequestBean();
				
				serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
				serviceRequestBean.setServiceName(ProductEnum.Hotel.hotelAvailAndPricing.toString());
				serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
						
				serviceRequestBean.setRequestBean(requestBean);
				serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
				serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
				responseBean = (HotelAvailAndPricingRespBean) serviceResponseBean.getResponseBean();
				if(null!=responseBean)
				{
					ObjectMapper mapper = new ObjectMapper();
					jsonString = mapper.writeValueAsString(responseBean);
					CallLog.info(1,"TBOHotelServices.java hotelAvailAndPricing Response bean values::: "+jsonString);
				}
			}
			catch (Exception e) 
			{
				CallLog.printStackTrace(1, e);
			}
		return jsonString;
	}
	
	public  String hotelBooking(HotelBookRequestBean requestBean)  
	{		
		HotelBookResponseBean responseBean = null;
		try
		{
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
			    CallLog.info(1,"TBOHotelServices.java hotelBooking Response bean values::: "+jsonString);
			}
		}
		catch (Exception e) 
		{
			CallLog.printStackTrace(1, e);
		}
		return jsonString;
	}
	public  String hotelBookingAmendment(HotelAmendRequestBean requestBean)  
	{		
		HotelAmendResponseBean responseBean = null;
		try
		{
			serviceRequestBean = new ServiceRequestBean();
			
			serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
			serviceRequestBean.setServiceName(ProductEnum.Hotel.amendhotel.toString());
			serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
			serviceRequestBean.setRequestBean(requestBean);
			serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
			serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
			responseBean = (HotelAmendResponseBean) serviceResponseBean.getResponseBean();
			if(null!=responseBean)
			{
				ObjectMapper mapper = new ObjectMapper();
			    jsonString = mapper.writeValueAsString(responseBean);
			    CallLog.info(1,"TBOHotelServices.java hotelBookingAmendment Response bean values::: "+jsonString);
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
			    CallLog.info(1,"TBOHotelServices.java hotelBookingDetails Response bean values::: "+jsonString);
			}
		}
		catch (Exception e) 
		{
			CallLog.printStackTrace(1, e);
		}
		return jsonString;
	}
	
	public  String hotelGenerateInvc( HotelGenerateInvoiceReqBean requestBean)  
	{		
		HotelGenerateInvoiceRespBean responseBean = null;
		try
		{
			serviceRequestBean = new ServiceRequestBean();
			
			serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
			serviceRequestBean.setServiceName(ProductEnum.Hotel.hotelGenerateInvoice.toString());
			serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
			serviceRequestBean.setRequestBean(requestBean);
			serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
			serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
			responseBean = (HotelGenerateInvoiceRespBean) serviceResponseBean.getResponseBean();
			if(null!=responseBean)
			{
				ObjectMapper mapper = new ObjectMapper();
			    jsonString = mapper.writeValueAsString(responseBean);
			    CallLog.info(1,"TBOHotelServices.java hotelGenerateInvc Response bean values::: "+jsonString);
			}
		}
		catch (Exception e) 
		{
			CallLog.printStackTrace(1, e);
		}
		return jsonString;
	}
	
	public  String hotelCancelConfirm(HotelCancelConfirmRequestBean requestBean)  
	{		
		HotelCancelConfirmResponseBean responseBean = null;
		try
		{
			serviceRequestBean = new ServiceRequestBean();
			
			serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
			serviceRequestBean.setServiceName(ProductEnum.Hotel.hotelcancelconfrim.toString());
			serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
			serviceRequestBean.setRequestBean(requestBean);
			serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
			serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
			responseBean = (HotelCancelConfirmResponseBean) serviceResponseBean.getResponseBean();
			if(null!=responseBean)
			{
				ObjectMapper mapper = new ObjectMapper();
			    jsonString = mapper.writeValueAsString(responseBean);
			    CallLog.info(1,"TBOHotelServices.java hotelCancelConfirm Response bean values::: "+jsonString);
			}
		}
		catch (Exception e) 
		{
			CallLog.printStackTrace(1, e);
		}
		return jsonString;
	}
	
	public  String hotelCheckStatus(HotelCancelConfirmRequestBean requestBean)  
	{		
		HotelCancelConfirmResponseBean responseBean = null;
		try
		{
			serviceRequestBean = new ServiceRequestBean();
			
			serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
			serviceRequestBean.setServiceName(ProductEnum.Hotel.hotelcancelconfrim.toString());
			serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
			serviceRequestBean.setRequestBean(requestBean);
			serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
			serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
			responseBean = (HotelCancelConfirmResponseBean) serviceResponseBean.getResponseBean();
			if(null!=responseBean)
			{
				ObjectMapper mapper = new ObjectMapper();
			    jsonString = mapper.writeValueAsString(responseBean);
			    CallLog.info(1,"TBOHotelServices.java hotelCancelConfirm Response bean values::: "+jsonString);
			}
		}
		catch (Exception e) 
		{
			CallLog.printStackTrace(1, e);
		}
		return jsonString;
	}
	
	public  String hotelCountryList(HotelCountryRequestBean requestBean)  
	{		
		HotelCountryResponseBean responseBean = null;
		try
		{
			serviceRequestBean = new ServiceRequestBean();
			
			serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
			serviceRequestBean.setServiceName(ProductEnum.Hotel.hotelcountry.toString());
			serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
			serviceRequestBean.setRequestBean(requestBean);
			serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
			serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
			responseBean = (HotelCountryResponseBean) serviceResponseBean.getResponseBean();
			if(null!=responseBean)
			{
				ObjectMapper mapper = new ObjectMapper();
			    jsonString = mapper.writeValueAsString(responseBean);
			    CallLog.info(1,"TBOHotelServices.java hotelCountryList Response bean values::: "+jsonString);
			}
		}
		catch (Exception e) 
		{
			CallLog.printStackTrace(1, e);
		}
		return jsonString;
	}
	
	public  String hotelCityList(HotelCityRequestBean requestBean)  
	{		
		HotelCityResponseBean responseBean = null;
		try
		{
			serviceRequestBean = new ServiceRequestBean();
			
			serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
			serviceRequestBean.setServiceName(ProductEnum.Hotel.hotelcity.toString());
			serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
			serviceRequestBean.setRequestBean(requestBean);
			serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
			serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
			responseBean = (HotelCityResponseBean) serviceResponseBean.getResponseBean();
			if(null!=responseBean)
			{
				ObjectMapper mapper = new ObjectMapper();
			    jsonString = mapper.writeValueAsString(responseBean);
			    CallLog.info(1,"TBOHotelServices.java hotelCityList Response bean values::: "+jsonString);
			}
		}
		catch (Exception e) 
		{
			CallLog.printStackTrace(1, e);
		}
		return jsonString;
	}
}
