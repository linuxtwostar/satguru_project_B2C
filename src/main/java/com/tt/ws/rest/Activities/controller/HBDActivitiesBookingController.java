package com.tt.ws.rest.Activities.controller;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tt.ts.common.modal.ResultBean;
import com.tt.ts.rest.common.RedisService;
import com.tt.ts.rest.common.util.RedisCacheUtil;
import com.tt.ws.rest.Activities.services.HBDActivitiesService;
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

@RestController
@RequestMapping("/HBDBooking")
public class HBDActivitiesBookingController {
	
	@Autowired
    RedisService redisService;
	
	ServiceResolverFactory serviceResolverFactory = null;
	
	ServiceRequestBean serviceRequestBean = null;
	ServiceResponseBean serviceResponseBean=null;
	String logStr = "[HBDContentActivitiesController]";
	String jsonString = null;
	
	@Autowired
	HBDActivitiesService activitiesService;

	/*{ "filter":[{
		   "filterType":"service",
		   "filterValue":"E-E10-DINNERCRUI"
		}],
	      "fromDate":"28/06/2017",
	      "toDate":"30/06/217"        
	
    }*/
	
	/*@RequestMapping(value = "/searchActivities",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String searchActivities(@RequestBody ActivitiesSearchRequestBean requestBean)
	{
		ActivitiesSearchResponseBean responseBean =null;
	    serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Activities.toString());
		serviceRequestBean.setServiceName(ProductEnum.Activities.searchActivities.toString());
		//serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		serviceRequestBean.setServiceConfigModel(ActivitiesUtil.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		//////////////
		String activitiesSearchKey=null;	
		try
		{
			// cache implementation
			activitiesSearchKey = ActivitiesUtil.prepareKeyForActivitiesSearch(requestBean);			
			CallLog.info(11,logStr+ "searchActivities for activitiesSearchKey=== " + activitiesSearchKey);
			Boolean isSearchKeyFound = false;
			ResultBean resultBean = RedisCacheUtil.isSearchKeyInCacheOthers(activitiesSearchKey,redisService);
			if (resultBean != null && !resultBean.isError())
			{
			    isSearchKeyFound = resultBean.getResultBoolean();
			}
			if (isSearchKeyFound)
	        {
				ResultBean resultBeanCache = RedisCacheUtil.getResponseFromCacheOthers(activitiesSearchKey,redisService);
			      if (resultBeanCache != null && !resultBeanCache.isError()) 
			        {
			    	  jsonString = resultBeanCache.getResultString();
			    	  CallLog.info(1,logStr+  "Resp JSON from RedisCachePortalUtil for getAutoSuggest::::" + jsonString.length());
				    }
			    else 
				    {
			    	serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
					serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
					 responseBean = (ActivitiesSearchResponseBean) serviceResponseBean.getResponseBean();
					if(null!=responseBean)
					{
						ObjectMapper mapper = new ObjectMapper();
						jsonString = mapper.writeValueAsString(responseBean);
						
					}
					if (null != jsonString && !jsonString.isEmpty() && !resultBean.isError())
					{
						RedisCacheUtil.setResponseInCacheOthers(activitiesSearchKey, jsonString,redisService);
				    }
			    	}
				  
				
	        }
			else{
				serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
				serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
				 responseBean = (ActivitiesSearchResponseBean) serviceResponseBean.getResponseBean();
				 CallLog.info(11,logStr+" Response bean values ::: "+responseBean);
				if(null!=responseBean)
				{
					JSONObject reqJson = new JSONObject(responseBean);
					ObjectMapper mapper = new ObjectMapper();
					jsonString = mapper.writeValueAsString(responseBean);
					if (null != jsonString && !jsonString.isEmpty() && reqJson.isNull("errorMsg") && !resultBean.isError())
					{
						RedisCacheUtil.setResponseInCacheOthers(activitiesSearchKey, jsonString,redisService);
				    }
					
				}
				
			}
	// cache implementation
		//////////////
				
		}
		catch (Exception e)
		{
			CallLog.printStackTrace(1, e);
		}
		return jsonString;	
	}*/
	
	
	@RequestMapping(value = "/searchActivities",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String searchActivities(@RequestBody ActivitiesSearchRequestBean requestBean)
	{
		try {
			jsonString = activitiesService.searchActivities(requestBean);
			} catch (Exception e) {
			CallLog.printStackTrace(30,e);
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
		try {
			jsonString = activitiesService.getActivityDetail(requestBean);
			} catch (Exception e) {
			CallLog.printStackTrace(30,e);
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
		try {
			jsonString = activitiesService.getActivityPickups(requestBean);
			} catch (Exception e) {
			CallLog.printStackTrace(30,e);
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
		try {
			jsonString = activitiesService.getActivityBookingPreConfirm(requestBean);
			} catch (Exception e) {
			CallLog.printStackTrace(30,e);
		}

		return jsonString;	
	}
	
	/*{

	"bookingReference":"235-3900230"

     }*/
	@RequestMapping(value = "/getActivityBookingReConfirm",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String getActivityBookingReConfirm(@RequestBody ActivitiesBookingRequestBean requestBean)
	{
		try {
			jsonString = activitiesService.getActivityBookingReConfirm(requestBean);
			} catch (Exception e) {
			CallLog.printStackTrace(30,e);
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
		try {
			jsonString = activitiesService.getActivityBookingConfirm(requestBean);
			} catch (Exception e) {
			CallLog.printStackTrace(30,e);
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
		try {
			jsonString = activitiesService.getActivityBookingDetails(requestBean);
			} catch (Exception e) {
			CallLog.printStackTrace(1,e);
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
		try {
			jsonString = activitiesService.getActivityBookingCancellation(requestBean);
			} catch (Exception e) {
			CallLog.printStackTrace(30,e);
		}

		return jsonString;	
	}
	
}

