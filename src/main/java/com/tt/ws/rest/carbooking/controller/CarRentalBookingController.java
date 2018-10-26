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
import com.ws.services.carbooking.bean.CBAmendRequestBean;
import com.ws.services.carbooking.bean.CBAmendResponseBean;
import com.ws.services.carbooking.bean.CBBookingInfoRespBean;
import com.ws.services.carbooking.bean.CBBookingStatusRespBean;
import com.ws.services.carbooking.bean.CarBookingRequestBean;
import com.ws.services.carbooking.bean.CarRentalBookingReqBean;
import com.ws.services.carbooking.bean.CarRentalBookingRespBean;
import com.ws.services.carbooking.bean.CarRentalSearchReqBean;
import com.ws.services.carbooking.bean.CarRentalSearchRespBean;
import com.ws.services.enums.ProductServiceEnum;
import com.ws.services.util.CallLog;
import com.ws.services.util.ProductEnum;

@RestController
@RequestMapping("/CarRentalBooking")
public class CarRentalBookingController {
	
ServiceResolverFactory serviceResolverFactory = null;
	
	ServiceRequestBean serviceRequestBean = null;
	ServiceResponseBean serviceResponseBean=null;
	String logStr = "[CarRentalBookingController]";
	String jsonString = null;
	
	
	/*{ 
	   	"pickupLocationId":"LON",
	   	"pickupDate":"05/11/2017" ,
	    "dropOffLocationId" :"LON",  
		"dropOffDate" :"10/11/2017",
		"driverAge":"32"

	}*/

	@RequestMapping(value = "/searchCars",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String searchCars(@RequestBody CarRentalSearchReqBean requestBean)
	{
		 	serviceRequestBean = new ServiceRequestBean();
			serviceRequestBean.setProductType(ProductServiceEnum.CarBooking.toString());
			serviceRequestBean.setServiceName(ProductEnum.CarBooking.search.toString());
			//serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
			serviceRequestBean.setServiceConfigModel(CarBookingUtils.setServiceConfigModel()); 
			serviceRequestBean.setRequestBean(requestBean);
			serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
			serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
			CarRentalSearchRespBean responseBean = (CarRentalSearchRespBean) serviceResponseBean.getResponseBean();
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


	/*{
		"pickupLocationId": "3372771",
		"pickupDate": "05/11/2017",
		"pickupHour": "11",
		"pickupMinutes": "30",
		"dropOffLocationId": "3372771",
		"dropOffDate": "10/11/2017",
		"dropOffHour": "2",
		"dropOffMinutes": "30",
		"extraListId": "607304703",
		"extraListAmount": "1",
		"vehicleId": "548494063",
		"driverTitle": "Mr",
		"driverFirstName": "Test",
		"driverLastName": "Abc",
		"driverCountry": "UK",
		"driverCity": "London",
		"driverStreet": "hjkk",
		"driverPostCode": "34567",
		"driverEmail": "abc@gmail.com",
		"driverTelephone": "4567890123",
		"driverAge": "32",
		"depositPayment": "false",
		"creditCardType": "4",
		"cardNumber": "5111111111111111",
		"ccv2": "123",
		"expirationYear": "2019",
		"expirationMonth": "08",
		"cardHolder": "Test Abc",
		"comments": "hfdjkfikkkl",
		"pickUpService": "test",
		"dropOffService": "test123",
		"flightNo": "abc 123",
		"AcceptedPrice": "574.94",
		"AcceptedCurrency": "GBP",
		"Adcamp": "example",
		"Adplat": "example"

	}*/
	@RequestMapping(value = "/getMakeBookingResp",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String getMakeBookingResp(@RequestBody CarRentalBookingReqBean requestBean)
	{
		serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.CarBooking.toString());
		serviceRequestBean.setServiceName(ProductEnum.CarBooking.makeBooking.toString());
		//serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		serviceRequestBean.setServiceConfigModel(CarBookingUtils.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		CarRentalBookingRespBean responseBean = (CarRentalBookingRespBean) serviceResponseBean.getResponseBean();
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

	/*{
		"pickupLocationId": "3372771",
		"pickupDate": "05/11/2017",
		"pickupHour": "11",
		"pickupMinutes": "30",
		"dropOffLocationId": "3372771",
		"dropOffDate": "10/11/2017",
		"dropOffHour": "2",
		"dropOffMinutes": "30",
		"extraListId": "607304703",
		"extraListAmount": "1",
		"vehicleId": "548494063",
		"driverTitle": "Mr",
		"driverFirstName": "Test",
		"driverLastName": "Abc",
		"driverCountry": "UK",
		"driverCity": "London",
		"driverStreet": "hjkk",
		"driverPostCode": "34567",
		"driverEmail": "abc@gmail.com",
		"driverTelephone": "4567890123",
		"driverAge": "32",
		"comments": "hfdjkfikkkl",
		"pickUpService": "test",
		"dropOffService": "test123",
		"flightNo": "abc 123",
		"AcceptedPrice": "574.94",
		"AcceptedCurrency": "GBP",
		"Adcamp": "example",
		"Adplat": "example"
	
	}*/
	//322915834
	@RequestMapping(value = "/getSaveQuoteResp",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String getSaveQuoteResp(@RequestBody CarRentalBookingReqBean requestBean)
	{
		serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.CarBooking.toString());
		serviceRequestBean.setServiceName(ProductEnum.CarBooking.saveQuote.toString());
		//serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		serviceRequestBean.setServiceConfigModel(CarBookingUtils.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		CarRentalBookingRespBean responseBean = (CarRentalBookingRespBean) serviceResponseBean.getResponseBean();
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

	/*{
		"pickupLocationId": "3372771",
		"pickupDate": "05/11/2017",
		"pickupHour": "11",
		"pickupMinutes": "30",
		"dropOffLocationId": "3372771",
		"dropOffDate": "10/11/2017",
		"dropOffHour": "2",
		"dropOffMinutes": "30",
		"extraListId": "607304703",
		"extraListAmount": "1",
		"vehicleId": "548494063",
		"driverTitle": "Mr",
		"driverFirstName": "Test",
		"driverLastName": "Abc",
		"driverCountry": "UK",
		"driverCity": "London",
		"driverStreet": "hjkk",
		"driverPostCode": "34567",
		"driverEmail": "abc@gmail.com",
		"driverTelephone": "4567890123",
		"driverAge": "32",
		"depositPayment": "false",
		"creditCardType": "4",
		"cardNumber": "5111111111111111",
		"ccv2": "123",
		"expirationYear": "2019",
		"expirationMonth": "08",
		"cardHolder": "Test Abc"
		

	}*/
	@RequestMapping(value = "/getConvertQuoteResp",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String getConvertQuoteResp(@RequestBody CarRentalBookingReqBean requestBean)
	{
		serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.CarBooking.toString());
		serviceRequestBean.setServiceName(ProductEnum.CarBooking.convertQuote.toString());
		//serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		serviceRequestBean.setServiceConfigModel(CarBookingUtils.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		CarRentalBookingRespBean responseBean = (CarRentalBookingRespBean) serviceResponseBean.getResponseBean();
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

	/*{
		"bookingId": "322915834",
		
		"email": "abc@gmail.com"
		
	
	}*/
	@RequestMapping(value = "/getBookingInfo",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String getBookingInfo(@RequestBody CarBookingRequestBean requestBean)
	{
		serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.CarBooking.toString());
		serviceRequestBean.setServiceName(ProductEnum.CarBooking.bookingInfo.toString());
		//serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		serviceRequestBean.setServiceConfigModel(CarBookingUtils.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		CBBookingInfoRespBean responseBean = (CBBookingInfoRespBean) serviceResponseBean.getResponseBean();
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
	
	/*{
		"bookingId": "322915834",
		
		"email": "abc@gmail.com"
	

	}*/
	@RequestMapping(value = "/getBookingStatus",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String getBookingStatus(@RequestBody CarBookingRequestBean requestBean)
	{
		serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.CarBooking.toString());
		serviceRequestBean.setServiceName(ProductEnum.CarBooking.bookingStatus.toString());
		//serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		serviceRequestBean.setServiceConfigModel(CarBookingUtils.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		CBBookingStatusRespBean responseBean = (CBBookingStatusRespBean) serviceResponseBean.getResponseBean();
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
	/*{
		"bookingId": "322915834",
		"email": "abc@gmail.com",
		"pickUpYear": "2017",
		"pickUpMonth": "11",
		"pickUpDay": "5",
		"pickUpHour": "11",
		"pickUpMinute": "30",
		"dropOffYear": "2017",
		"dropOffMonth": "11",
		"dropOffDay": "10",
		"dropOffHour": "2",
		"dropOffMinute": "30",
		"driverCountry": "UK",
		"driverCity": "London",
		"driverStreet": "hjkk",
		"driverPostCode": "345678",
		"driverTelephone": "9988776644",
		"addlInfoComments": "jdfkgdkljgjogt",
		"airlineInfo": "ABC12"

	}*/
	
	@RequestMapping(value = "/getBookingAmend",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String getBookingAmend(@RequestBody CBAmendRequestBean requestBean)
	{
		serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.CarBooking.toString());
		serviceRequestBean.setServiceName(ProductEnum.CarBooking.bookingAmend.toString());
		//serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		serviceRequestBean.setServiceConfigModel(CarBookingUtils.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		CBAmendResponseBean responseBean = (CBAmendResponseBean) serviceResponseBean.getResponseBean();
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
