package com.tt.ws.rest.Activities.controller;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tt.ws.rest.Activities.utils.ActivitiesUtil;
import com.ws.interaction.beans.ServiceRequestBean;
import com.ws.interaction.beans.ServiceResponseBean;
import com.ws.interaction.factory.ServiceResolverFactory;
import com.ws.services.activities.bean.ActivitiesCountriesResponseBean;
import com.ws.services.activities.bean.ActivitiesCurrenciesRespBean;
import com.ws.services.activities.bean.ActivitiesDestinationRespBean;
import com.ws.services.activities.bean.ActivitiesHotelsResponseBean;
import com.ws.services.activities.bean.ActivitiesLanguagesRespBean;
import com.ws.services.activities.bean.ActivitiesRequestBean;
import com.ws.services.activities.bean.ActivitiesSearchResponseBean;
import com.ws.services.activities.bean.ActivitiesSegmentationRespBean;
import com.ws.services.enums.ProductServiceEnum;
import com.ws.services.util.CallLog;
import com.ws.services.util.ProductEnum;

@RestController
@RequestMapping("/HBDContent")
public class HBDActivitiesContentController {
	
	ServiceResolverFactory serviceResolverFactory = null;
	
	ServiceRequestBean serviceRequestBean = null;
	ServiceResponseBean serviceResponseBean=null;
	String logStr = "[HBDContentActivitiesController]";
	String jsonString = null;

	/*{
	              
	
    }*/
	
	@RequestMapping(value = "/getSegmentationList",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String getSegmentList(@RequestBody ActivitiesRequestBean requestBean)
	{
	    serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Activities.toString());
		serviceRequestBean.setServiceName(ProductEnum.Activities.segmentation.toString());
		//serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		serviceRequestBean.setServiceConfigModel(ActivitiesUtil.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		ActivitiesSegmentationRespBean responseBean = (ActivitiesSegmentationRespBean) serviceResponseBean.getResponseBean();
		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(responseBean);
			} catch (Exception e) {
				CallLog.printStackTrace(30,e);
			}
		}
		return jsonString;	
	}
	
	
	/*{
	
    }*/
	
	@RequestMapping(value = "/getLanguageList",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String searchLanguageList(@RequestBody ActivitiesRequestBean requestBean)
	{
	    serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Activities.toString());
		serviceRequestBean.setServiceName(ProductEnum.Activities.languages.toString());
		//serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		serviceRequestBean.setServiceConfigModel(ActivitiesUtil.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		ActivitiesLanguagesRespBean responseBean = (ActivitiesLanguagesRespBean) serviceResponseBean.getResponseBean();
		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(responseBean);
			} catch (Exception e) {
				CallLog.printStackTrace(30,e);
			}
		}
		return jsonString;	
	}
	
   /*{
	
    }*/
	
	@RequestMapping(value = "/getCurrencyList",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String searchCurrencyList(@RequestBody ActivitiesRequestBean requestBean)
	{
	    serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Activities.toString());
		serviceRequestBean.setServiceName(ProductEnum.Activities.currency.toString());
		//serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		serviceRequestBean.setServiceConfigModel(ActivitiesUtil.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		ActivitiesCurrenciesRespBean responseBean = (ActivitiesCurrenciesRespBean) serviceResponseBean.getResponseBean();
		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(responseBean);
			} catch (Exception e) {
				CallLog.printStackTrace(30,e);
			}
		}
		return jsonString;	
	}
	
	 /*{
		
		"countryCode":"ES"
	    }*/
		
		@RequestMapping(value = "/getDestinationList",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
		public String searchDestinationList(@RequestBody ActivitiesRequestBean requestBean)
		{
		    serviceRequestBean = new ServiceRequestBean();
			serviceRequestBean.setProductType(ProductServiceEnum.Activities.toString());
			serviceRequestBean.setServiceName(ProductEnum.Activities.destination.toString());
			//serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
			serviceRequestBean.setServiceConfigModel(ActivitiesUtil.setServiceConfigModel()); 
			serviceRequestBean.setRequestBean(requestBean);
			serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
			serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
			ActivitiesDestinationRespBean responseBean = (ActivitiesDestinationRespBean) serviceResponseBean.getResponseBean();
			if(null!=responseBean)
			{
				ObjectMapper mapper = new ObjectMapper();
				try {
					jsonString = mapper.writeValueAsString(responseBean);
				} catch (Exception e) {
					CallLog.printStackTrace(30,e);
				}
			}
			return jsonString;	
		}
		
		 /*{
		
	    }*/
		
		@RequestMapping(value = "/getActivityDetailSimple",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
		public String searchActivityDetailSimple(@RequestBody ActivitiesRequestBean requestBean)
		{
		    serviceRequestBean = new ServiceRequestBean();
			serviceRequestBean.setProductType(ProductServiceEnum.Activities.toString());
			serviceRequestBean.setServiceName(ProductEnum.Activities.search.toString());
			//serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
			serviceRequestBean.setServiceConfigModel(ActivitiesUtil.setServiceConfigModel()); 
			serviceRequestBean.setRequestBean(requestBean);
			serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
			serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
			ActivitiesSearchResponseBean responseBean = (ActivitiesSearchResponseBean) serviceResponseBean.getResponseBean();
			if(null!=responseBean)
			{
				ObjectMapper mapper = new ObjectMapper();
				try {
					jsonString = mapper.writeValueAsString(responseBean);
				} catch (Exception e) {
					CallLog.printStackTrace(30,e);
				}
			}
			return jsonString;	
		}
		
 /*{
		
	    }*/
		
		@RequestMapping(value = "/getActivityCountries",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
		public String searchActivityCountry(@RequestBody ActivitiesRequestBean requestBean)
		{
		    serviceRequestBean = new ServiceRequestBean();
			serviceRequestBean.setProductType(ProductServiceEnum.Activities.toString());
			serviceRequestBean.setServiceName(ProductEnum.Activities.countries.toString());
			//serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
			serviceRequestBean.setServiceConfigModel(ActivitiesUtil.setServiceConfigModel()); 
			serviceRequestBean.setRequestBean(requestBean);
			serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
			serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
			ActivitiesCountriesResponseBean responseBean = (ActivitiesCountriesResponseBean) serviceResponseBean.getResponseBean();
			if(null!=responseBean)
			{
				ObjectMapper mapper = new ObjectMapper();
				try {
					jsonString = mapper.writeValueAsString(responseBean);
				} catch (Exception e) {
					CallLog.printStackTrace(30,e);
				}
			}
			return jsonString;	
		}
		/*{
		
	    }*/
		
		@RequestMapping(value = "/getActivityHotels",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
		public String searchActivityHotels(@RequestBody ActivitiesRequestBean requestBean)
		{
		    serviceRequestBean = new ServiceRequestBean();
			serviceRequestBean.setProductType(ProductServiceEnum.Activities.toString());
			serviceRequestBean.setServiceName(ProductEnum.Activities.hotels.toString());
			//serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
			serviceRequestBean.setServiceConfigModel(ActivitiesUtil.setServiceConfigModel()); 
			serviceRequestBean.setRequestBean(requestBean);
			serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
			serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
			ActivitiesHotelsResponseBean responseBean = (ActivitiesHotelsResponseBean) serviceResponseBean.getResponseBean();
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
