package com.tt.ws.rest.hotel.services;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;

import com.tt.ws.rest.hotel.bean.HotelsFareRuleRespBean;
import com.ws.interaction.beans.ServiceRequestBean;
import com.ws.interaction.beans.ServiceResponseBean;
import com.ws.interaction.factory.ServiceResolverFactory;
import com.ws.services.hotel.bean.HotelBookRequestBean;
import com.ws.services.hotel.bean.HotelBookResponseBean;
import com.ws.services.hotel.bean.HotelBookingDetailsReqBean;
import com.ws.services.hotel.bean.HotelBookingDetailsRespBean;
import com.ws.services.hotel.bean.HotelCancelCostReqBean;
import com.ws.services.hotel.bean.HotelCancelCostRespBean;
import com.ws.services.hotel.bean.HotelCancelRequestBean;
import com.ws.services.hotel.bean.HotelCancelResponseBean;
import com.ws.services.hotel.bean.HotelCommonSearchRespBean;
import com.ws.services.hotel.bean.HotelSearchRequestBean;
import com.ws.services.hotel.bean.RoomRequestBean;
import com.ws.services.util.CallLog;
import com.ws.services.util.ProductEnum;
import com.ws.services.util.ProductServiceEnum;

@Service
public class MikiHotelService 
{
	String logStr = "[MikiHotelService]";
	ServiceResolverFactory serviceResolverFactory = null;
	RoomRequestBean roomRequestBean = null;
	ServiceRequestBean serviceRequestBean = null;
	ServiceResponseBean serviceResponseBean=null;
	String jsonString = null;
	
	
	public  String hotelBooking(HotelBookRequestBean reqBean)  
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
		//serviceRequestBean.setServiceConfigModel(HotelUtil.setServiceConfigModel()); // for rest
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
	
	public String searchHotelCostCancellation(HotelCancelCostReqBean reqBean)	
		{
			logStr = logStr+" searchHotelCancellation() ";
			
			HotelCancelCostRespBean costResponseBean;
			
			serviceRequestBean = new ServiceRequestBean();
			serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
			serviceRequestBean.setServiceName(ProductEnum.Hotel.cancelCost.toString());
			serviceRequestBean.setServiceConfigModel(reqBean.getServiceConfigModel());
			//serviceRequestBean.setServiceConfigModel(HotelUtil.setServiceConfigModel()); // for rest
			serviceRequestBean.setRequestBean(reqBean);
			serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
			serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
			costResponseBean = (HotelCancelCostRespBean) serviceResponseBean.getResponseBean();
			if(null!=costResponseBean)
			{
				ObjectMapper mapper = new ObjectMapper();
				try 
				{
					jsonString = mapper.writeValueAsString(costResponseBean);
					CallLog.info(1,logStr+" Response Json:::" + jsonString);
				} catch (Exception e) {
					CallLog.printStackTrace(1,e);
				}
			}
			return jsonString;
		}
				
	public String searchHotelBookingDetails(HotelBookingDetailsReqBean reqBean)	
				{
					logStr = logStr+" searchHotelCancellation() ";
					
					HotelBookingDetailsRespBean bookingDetailsResponceBean;
					
					serviceRequestBean = new ServiceRequestBean();
					serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
					serviceRequestBean.setServiceName(ProductEnum.Hotel.hotelBookingDetails.toString());
					serviceRequestBean.setServiceConfigModel(reqBean.getServiceConfigModel());
					//serviceRequestBean.setServiceConfigModel(HotelUtil.setServiceConfigModel()); // for rest
					serviceRequestBean.setRequestBean(reqBean);
					serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
					serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
					bookingDetailsResponceBean = (HotelBookingDetailsRespBean) serviceResponseBean.getResponseBean();
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
	
	public HotelsFareRuleRespBean getPriceAvail(HotelSearchRequestBean reqBean) 
	{
		serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
		serviceRequestBean.setServiceName(ProductEnum.Hotel.search.toString());
		serviceRequestBean.setServiceConfigModel(reqBean.getServiceConfigModel());
		serviceRequestBean.setRequestBean(reqBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		HotelsFareRuleRespBean responseBean = null;
		HotelCommonSearchRespBean hotelSearchResponseBean = (HotelCommonSearchRespBean) serviceResponseBean.getResponseBean();
		if(!hotelSearchResponseBean.getFareRuleRespList().isEmpty())
		{
			responseBean = hotelSearchResponseBean.getFareRuleRespList().get(0);
		}
		return responseBean;
	}
}
