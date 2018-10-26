package com.tt.ws.rest.carbooking.controller;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tt.ws.rest.carbooking.utils.CarBookingUtils;
import com.ws.interaction.beans.ServiceRequestBean;
import com.ws.interaction.beans.ServiceResponseBean;
import com.ws.interaction.factory.ServiceResolverFactory;
import com.ws.services.carbooking.bean.CBExtraInfoRespBean;
import com.ws.services.carbooking.bean.CBExtrasListRequestBean;
import com.ws.services.carbooking.bean.CBPaymentMethodRespBean;
import com.ws.services.carbooking.bean.CBRentalTermsRespBean;
import com.ws.services.carbooking.bean.CarBookingRequestBean;
import com.ws.services.carbooking.bean.CarBookingResponseBean;
import com.ws.services.enums.ProductServiceEnum;
import com.ws.services.util.CallLog;
import com.ws.services.util.ProductEnum;


@RestController
@RequestMapping("/carBooking")
public class CBInfoController {
	
ServiceResolverFactory serviceResolverFactory = null;
	
	ServiceRequestBean serviceRequestBean = null;
	ServiceResponseBean serviceResponseBean=null;
	String logStr = "[CBInfoController]";
	String jsonString = null;

	/*{ }*/
	
	@RequestMapping(value = "/pickUpCountryList",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String pickUpCountryList(@RequestBody CarBookingRequestBean requestBean)
	{
	    serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.CarBooking.toString());
		serviceRequestBean.setServiceName(ProductEnum.CarBooking.pickUpCountryList.toString());
		//serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		serviceRequestBean.setServiceConfigModel(CarBookingUtils.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		CarBookingResponseBean responseBean = (CarBookingResponseBean) serviceResponseBean.getResponseBean();
		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(responseBean);
				CallLog.info(1,logStr+" Response bean values::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}
		return jsonString;	
	}
	
	/* {"pickUpCountry": "UK"} */
	
	@RequestMapping(value = "/pickUpCityList",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String pickUpCityList(@RequestBody CarBookingRequestBean requestBean)
	{
	    serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.CarBooking.toString());
		serviceRequestBean.setServiceName(ProductEnum.CarBooking.pickUpCityList.toString());
		//serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		serviceRequestBean.setServiceConfigModel(CarBookingUtils.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		CarBookingResponseBean responseBean = (CarBookingResponseBean) serviceResponseBean.getResponseBean();
		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(responseBean);
				CallLog.info(1,logStr+" Response bean values::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}
		return jsonString;	
	}
	
	/*{"pickUpCountry": "UK",
     "pickUpCity": "Aberdeen"
      }*/
	
	@RequestMapping(value = "/pickUpLocationList",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String pickUpLocationList(@RequestBody CarBookingRequestBean requestBean)
	{
	    serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.CarBooking.toString());
		serviceRequestBean.setServiceName(ProductEnum.CarBooking.pickUpLocationList.toString());
		//serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		serviceRequestBean.setServiceConfigModel(CarBookingUtils.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		CarBookingResponseBean responseBean = (CarBookingResponseBean) serviceResponseBean.getResponseBean();
		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(responseBean);
				CallLog.info(1,logStr+" Response bean values::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}
		return jsonString;	
	}
	
	/*{"pickUpCountry": "UK",
    "pickUpCity": "Aberdeen",
     "pickUpLocation": "Manchester Airport"
    AND
    { "pickUpLocationid" :  "3806" }
     }*/
	
	@RequestMapping(value = "/dropOffCountryList",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String dropOffCountryList(@RequestBody CarBookingRequestBean requestBean)
	{
	    serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.CarBooking.toString());
		serviceRequestBean.setServiceName(ProductEnum.CarBooking.dropOffCountryList.toString());
		//serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		serviceRequestBean.setServiceConfigModel(CarBookingUtils.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		CarBookingResponseBean responseBean = (CarBookingResponseBean) serviceResponseBean.getResponseBean();
		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(responseBean);
				CallLog.info(1,logStr+" Response bean values::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}
		return jsonString;	
	}
	
	/*
	 {"pickUpCountry": "UK",
     "pickUpCity": "Manchester",  "pickUpLocation": "Manchester Airport", "dropOffCountry" : "UK"
      } 
      AND
      { "pickUpLocationid" :  "3806",
         "dropOffCountry" : "UK" }
     
	 */
	
	@RequestMapping(value = "/dropOffCityList",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String dropOffCityList(@RequestBody CarBookingRequestBean requestBean)
	{
	    serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.CarBooking.toString());
		serviceRequestBean.setServiceName(ProductEnum.CarBooking.dropOffCityList.toString());
		//serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		serviceRequestBean.setServiceConfigModel(CarBookingUtils.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		CarBookingResponseBean responseBean = (CarBookingResponseBean) serviceResponseBean.getResponseBean();
		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(responseBean);
				CallLog.info(1,logStr+" Response bean values::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}
		return jsonString;	
	}
	
	/*
	 { "pickUpCountry": "UK",
       "pickUpCity": "Manchester",  
       "pickUpLocation": "Manchester Airport",
       "dropOffCountry" : "UK" ,
       "dropOffCity" : "Manchester" }
       AND
       { "pickUpLocationid" :  "3806",
         "dropOffCountry" : "UK" ,
         "dropOffCity" : "Manchester" }
     
     */
	
	@RequestMapping(value = "/dropOffLocationList",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String dropOffLocationList(@RequestBody CarBookingRequestBean requestBean)
	{
	    serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.CarBooking.toString());
		serviceRequestBean.setServiceName(ProductEnum.CarBooking.dropOffLocationList.toString());
		//serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		serviceRequestBean.setServiceConfigModel(CarBookingUtils.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		CarBookingResponseBean responseBean = (CarBookingResponseBean) serviceResponseBean.getResponseBean();
		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(responseBean);
				CallLog.info(1,logStr+" Response bean values::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}
		return jsonString;	
	}
	
	/*
	  { "pickUpLocationid" :  "3806",
        "day" : "09" ,
        "month" : "08",
        "year" : "2017" }
    */
	
	@RequestMapping(value = "/pickUpOpenTime",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String pickUpOpenTime(@RequestBody CarBookingRequestBean requestBean)
	{
	    serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.CarBooking.toString());
		serviceRequestBean.setServiceName(ProductEnum.CarBooking.pickUpOpenTime.toString());
		//serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		serviceRequestBean.setServiceConfigModel(CarBookingUtils.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		CarBookingResponseBean responseBean = (CarBookingResponseBean) serviceResponseBean.getResponseBean();
		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(responseBean);
				CallLog.info(1,logStr+" Response bean values::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}
		return jsonString;	
	}
	
	/*
	  { "pickUpLocationid" :  "3806",
      "day" : "09" ,
      "month" : "08",
      "year" : "2017" }
  */
	
	@RequestMapping(value = "/dropOffOpenTime",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String dropOffOpenTime(@RequestBody CarBookingRequestBean requestBean)
	{
	    serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.CarBooking.toString());
		serviceRequestBean.setServiceName(ProductEnum.CarBooking.dropOffOpenTime.toString());
		//serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		serviceRequestBean.setServiceConfigModel(CarBookingUtils.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		CarBookingResponseBean responseBean = (CarBookingResponseBean) serviceResponseBean.getResponseBean();
		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(responseBean);
				CallLog.info(1,logStr+" Response bean values::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}
		return jsonString;	
	}
	
	/*
	  { "pickUpLocationid" :  "3806"
      }
    */
	
	@RequestMapping(value = "/paymentMethodList",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String paymentMethodList(@RequestBody CarBookingRequestBean requestBean)
	{
	    serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.CarBooking.toString());
		serviceRequestBean.setServiceName(ProductEnum.CarBooking.paymentMethodList.toString());
		//serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		serviceRequestBean.setServiceConfigModel(CarBookingUtils.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		CBPaymentMethodRespBean responseBean = (CBPaymentMethodRespBean) serviceResponseBean.getResponseBean();
		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(responseBean);
				CallLog.info(1,logStr+" Response bean values::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}
		return jsonString;	
	}
	
	/*
	  { "pickUpLocationid" :  "3806"
    }
  */
	
	@RequestMapping(value = "/rentalTerms",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String rentalTerms(@RequestBody CarBookingRequestBean requestBean)
	{
	    serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.CarBooking.toString());
		serviceRequestBean.setServiceName(ProductEnum.CarBooking.rentalTerms.toString());
		//serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		serviceRequestBean.setServiceConfigModel(CarBookingUtils.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		CBRentalTermsRespBean responseBean = (CBRentalTermsRespBean) serviceResponseBean.getResponseBean();
		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(responseBean);
				CallLog.info(1,logStr+" Response bean values::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}
		return jsonString;	
	}
	
	/*
	{
    "vehicleId": "269182058",
    "pickUpYear": "2017",
    "pickUpMonth": "07",
    "pickUpDay": "20",
    "pickUpHour": "12",
    "pickUpMinute": "0",
    "dropOffYear": "2017",
    "dropOffMonth": "07",
    "dropOffDay": "22",
    "dropOffHour": "12",
    "dropOffMinute": "0", 
    "price": "200.0"
    }
    */
	
	@RequestMapping(value = "/extrasList",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String extrasList(@RequestBody CBExtrasListRequestBean requestBean)
	{
	    serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.CarBooking.toString());
		serviceRequestBean.setServiceName(ProductEnum.CarBooking.extrasList.toString());
		//serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		serviceRequestBean.setServiceConfigModel(CarBookingUtils.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		CBExtraInfoRespBean responseBean = (CBExtraInfoRespBean) serviceResponseBean.getResponseBean();
		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(responseBean);
				CallLog.info(1,logStr+" Response bean values::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}
		return jsonString;	
	}

}
