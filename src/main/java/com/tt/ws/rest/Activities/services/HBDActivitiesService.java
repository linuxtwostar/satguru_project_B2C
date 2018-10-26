package com.tt.ws.rest.Activities.services;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tt.ws.rest.Activities.utils.ActivitiesUtil;
import com.ws.interaction.beans.ServiceRequestBean;
import com.ws.interaction.beans.ServiceResponseBean;
import com.ws.interaction.factory.ServiceResolverFactory;
import com.ws.services.activities.bean.ActivitiesBookingConfirmResponseBean;
import com.ws.services.activities.bean.ActivitiesBookingRequestBean;
import com.ws.services.activities.bean.ActivitiesDetailResponseBean;
import com.ws.services.activities.bean.ActivitiesSearchRequestBean;
import com.ws.services.activities.bean.ActivitiesSearchResponseBean;
import com.ws.services.activities.bean.ActivityBookingDetailReqBean;
import com.ws.services.activities.bean.ActivityBookingDetailRespBean;
import com.ws.services.activities.bean.ActivityCancellationRespBean;
import com.ws.services.activities.bean.ActivityPickupReqBean;
import com.ws.services.activities.bean.ActivityPickupRespBean;
import com.ws.services.enums.ProductServiceEnum;
import com.ws.services.util.CallLog;
import com.ws.services.util.ProductEnum;

@Service
public class HBDActivitiesService {
	
	@Autowired
	ActivitiesServices activitiesServices;
	

	ServiceResolverFactory serviceResolverFactory = null;
	
	ServiceRequestBean serviceRequestBean = null;
	ServiceResponseBean serviceResponseBean=null;
	String logStr = "[HBDContentActivitiesController]";
	String jsonString = null;
	
	@RequestMapping(value = "/searchActivities",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String searchActivities(@RequestBody ActivitiesSearchRequestBean requestBean)
	{
	    serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Activities.toString());
		serviceRequestBean.setServiceName(ProductEnum.Activities.searchActivities.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		//serviceRequestBean.setServiceConfigModel(ActivitiesUtil.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		ActivitiesSearchResponseBean responseBean = (ActivitiesSearchResponseBean) serviceResponseBean.getResponseBean();
		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(responseBean);
				CallLog.createXmlFiles(jsonString, "Activities", "Search", "SearchResp.json");
			} catch (Exception e) {
				CallLog.printStackTrace(30,e);
			}
		}
		return jsonString;	
	}
	
/*{

	"fromDate": "28/06/2017",
	"toDate": "30/06/2017",
	"activityCode": "E-U10-DSCVCOVE",
	"paxAges": [20]


}*/
	@RequestMapping(value = "/getActivityDetail",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String getActivityDetail(@RequestBody ActivitiesSearchRequestBean requestBean)
	{
	    serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Activities.toString());
		serviceRequestBean.setServiceName(ProductEnum.Activities.activityDetail.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		//serviceRequestBean.setServiceConfigModel(ActivitiesUtil.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		ActivitiesDetailResponseBean responseBean = (ActivitiesDetailResponseBean) serviceResponseBean.getResponseBean();
		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(responseBean);
				CallLog.createXmlFiles(jsonString, "Activities", "Details", "detailsResp.json");
			} catch (Exception e) {
				CallLog.printStackTrace(30,e);
			}
		}
		return jsonString;	
	}
	
	/*{

	"fromDate": "28/06/2017",
	"toDate": "30/06/2017",
	"pickupRetrievalKey": "falv0m4n2td6s48ahecgm407f6",
	"hotelCode": "183273"


     }*/
	@RequestMapping(value = "/getActivityPickups",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String getActivityPickups(@RequestBody ActivityPickupReqBean requestBean)
	{
	    serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Activities.toString());
		serviceRequestBean.setServiceName(ProductEnum.Activities.activityPickups.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		//serviceRequestBean.setServiceConfigModel(ActivitiesUtil.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		ActivityPickupRespBean responseBean = (ActivityPickupRespBean) serviceResponseBean.getResponseBean();
		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(responseBean);
				CallLog.info(30,logStr+" Response bean values::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(30,e);
			}
		}
		return jsonString;	
	}
	
	/*{

		"email": "test@gmail.com",
		"holdername": "Test",
		"holdersurname": "act",
		"telephone": "1234567890",
		"clientReference": "Agency test",
		"activities": [{
			"rateKey": "s2rjdlmcn4obnn9ibumnn9haiu",
			"toDate": "28/06/2017",
			"fromDate": "28/06/2017"
		}]


	}*/
	@RequestMapping(value = "/getActivityBookingPreConfirm",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String getActivityBookingPreConfirm(@RequestBody ActivitiesBookingRequestBean requestBean)
	{
	    serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Activities.toString());
		serviceRequestBean.setServiceName(ProductEnum.Activities.bookingPreConfirm.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		//serviceRequestBean.setServiceConfigModel(ActivitiesUtil.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		ActivitiesBookingConfirmResponseBean responseBean = (ActivitiesBookingConfirmResponseBean) serviceResponseBean.getResponseBean();
		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(responseBean);
				CallLog.createXmlFiles(jsonString, "Activities", "Booking", "PreconfrimBooking.json");
			} catch (Exception e) {
				CallLog.printStackTrace(30,e);
			}
		}
		return jsonString;	
	}
	
	/*{

	"bookingReference":"235-3900230"

     }*/
	@RequestMapping(value = "/getActivityBookingReConfirm",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String getActivityBookingReConfirm(@RequestBody ActivitiesBookingRequestBean requestBean)
	{
	    serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Activities.toString());
		serviceRequestBean.setServiceName(ProductEnum.Activities.bookingReConfirm.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		//serviceRequestBean.setServiceConfigModel(ActivitiesUtil.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		ActivitiesBookingConfirmResponseBean responseBean = (ActivitiesBookingConfirmResponseBean) serviceResponseBean.getResponseBean();
		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(responseBean);
				CallLog.createXmlFiles(jsonString, "Activities", "Booking", "ReconfrimBooking.json");
			} catch (Exception e) {
				CallLog.printStackTrace(30,e);
			}
		}
		return jsonString;	
	}

	/*{

	"email": "test@gmail.com",
	"holdername": "Test",
	"holdersurname": "act",
	"telephone": "1234567890",
	"clientReference": "Agency test",
	"activities": [{
		"rateKey": "1huocdhpa754fv57kd4n89ukaa",
		"toDate": "28/06/2017",
		"fromDate": "28/06/2017",
		"preferedLanguage": "en",
		"serviceLanguage": "en",
		"paxDetails": [{
			"paxAge": "20",
			"paxName": "testabc",
			"paxSurname": "abcc",
			"paxType": "ADULT"

		}]
	}]


}*/
	@RequestMapping(value = "/getActivityBookingConfirmation",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String getActivityBookingConfirm(@RequestBody ActivitiesBookingRequestBean requestBean)
	{
	    serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Activities.toString());
		serviceRequestBean.setServiceName(ProductEnum.Activities.bookingConfirm.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		//serviceRequestBean.setServiceConfigModel(ActivitiesUtil.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		ActivitiesBookingConfirmResponseBean responseBean = (ActivitiesBookingConfirmResponseBean) serviceResponseBean.getResponseBean();
		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(responseBean);
				CallLog.createXmlFiles(jsonString, "Activities", "Booking", "ConfrimBooking.json");
			} catch (Exception e) {
				CallLog.printStackTrace(30,e);
			}
		}
		return jsonString;	
	}
	/*  {
	 		"bookingReference":"235-3900213"
	 	}
	  */
	@RequestMapping(value = "/getActivityBookingDetails",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String getActivityBookingDetails(@RequestBody ActivityBookingDetailReqBean requestBean)
	{
	    serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Activities.toString());
		serviceRequestBean.setServiceName(ProductEnum.Activities.bookingDetail.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		//serviceRequestBean.setServiceConfigModel(ActivitiesUtil.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		ActivityBookingDetailRespBean responseBean = (ActivityBookingDetailRespBean) serviceResponseBean.getResponseBean();
		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(responseBean);
				CallLog.info(30,logStr+" Response bean values::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(30,e);
			}
		}
		return jsonString;	
	}
	/*  {
			"bookingReference":"58-1534406"
		}
	 */
	@RequestMapping(value = "/getActivityBookingCancellation",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String getActivityBookingCancellation(@RequestBody ActivityBookingDetailReqBean requestBean)
	{
	    serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Activities.toString());
		serviceRequestBean.setServiceName(ProductEnum.Activities.cancellation.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		//serviceRequestBean.setServiceConfigModel(ActivitiesUtil.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		ActivityCancellationRespBean responseBean = (ActivityCancellationRespBean) serviceResponseBean.getResponseBean();
		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(responseBean);
				CallLog.info(30,logStr+" Response bean values::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(30,e);
			}
		}
		return jsonString;	
	}

}
