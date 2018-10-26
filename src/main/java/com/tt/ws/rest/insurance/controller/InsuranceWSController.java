package com.tt.ws.rest.insurance.controller;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tt.ts.common.modal.ResultBean;
import com.tt.ts.rest.common.RedisService;
import com.tt.ts.rest.common.util.RedisCacheUtil;
import com.tt.ws.rest.insurance.util.InsuranceHelperUtilRest;
import com.ws.interaction.beans.ServiceRequestBean;
import com.ws.interaction.beans.ServiceResponseBean;
import com.ws.interaction.factory.ServiceResolverFactory;
import com.ws.services.insurance.bean.policy.PolicyAmendRequestBean;
import com.ws.services.insurance.bean.policy.PolicyAmendResponseBean;
import com.ws.services.insurance.bean.policy.PolicyCancelRequestBean;
import com.ws.services.insurance.bean.policy.PolicyCancelResponseBean;
import com.ws.services.insurance.bean.policy.PolicyIssueRequestBean;
import com.ws.services.insurance.bean.policy.PolicyIssueResponseBean;
import com.ws.services.insurance.bean.policy.PolicyResendRequestBean;
import com.ws.services.insurance.bean.policy.PolicyResendResponseBean;
import com.ws.services.insurance.bean.product.ProductFamilyPriceResponseBean;
//import com.ws.services.insurance.bean.product.Personalisation;
import com.ws.services.insurance.bean.product.ProductPriceRequestBean;
import com.ws.services.util.CallLog;
import com.ws.services.util.ProductEnum;
import com.ws.services.util.ProductServiceEnum;

@RestController
@RequestMapping("/insurance")
public class InsuranceWSController {
	
    private static final String REST_CACHE_PORT = "tt.rest.memcache.port";

	@Autowired
	MessageSource messageSource;

	@Autowired
	RedisService redisService;
	
	@RequestMapping(value = "/searchProduct", method = RequestMethod.POST)
	public String searchAirFlights(@RequestBody ProductPriceRequestBean requestBean) {
		ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
		
		serviceRequestBean.setRequestBean(requestBean);
		serviceRequestBean.setProductType(ProductServiceEnum.Insurance.toString());
		
	//	String host = messageSource.getMessage(REST_IP, null, null, null);
		String portCache = messageSource.getMessage(REST_CACHE_PORT, null, null, null);
		String jsonString;
	//	Integer port = Integer.parseInt(portCache);
		String searchInsuranceKey = requestBean.getInsuranceSearchKey();
		Boolean isSearchKeyFound = false;
		//ResultBean resultBean = MemCacheUtil.isSearchKeyInCache(searchFlightKey, host, port);
		ResultBean resultBean = RedisCacheUtil.isSearchKeyInCacheOthers(searchInsuranceKey,redisService);
		if(resultBean != null && !resultBean.isError()) {
		    isSearchKeyFound = resultBean.getResultBoolean();
		}
		
		Long startTime = System.currentTimeMillis();
		if (isSearchKeyFound) {
			//ResultBean resultBeanCache = MemCacheUtil.getResponseFromCache(searchFlightKey, host, port);
		    ResultBean resultBeanCache = RedisCacheUtil.getResponseFromCacheOther(searchInsuranceKey,redisService);
			if(resultBeanCache != null && !resultBeanCache.isError()) {
			    jsonString = resultBeanCache.getResultString();
			}else {
			    jsonString = InsuranceHelperUtilRest.processToInsuranceSearchResponse(serviceRequestBean, requestBean);
			    //MemCacheUtil.setResponseInCache(searchFlightKey, jsonString, host, port);
			   RedisCacheUtil.setResponseInCacheOther(searchInsuranceKey, jsonString,redisService);
			}
		} else {
			jsonString = InsuranceHelperUtilRest.processToInsuranceSearchResponse(serviceRequestBean, requestBean);
			if(!resultBean.isError()) {
				//MemCacheUtil.setResponseInCache(searchFlightKey, jsonString, host, port);
				RedisCacheUtil.setResponseInCacheOther(searchInsuranceKey, jsonString,redisService);
			}			
		}
		Long endTime = System.currentTimeMillis();
		Long timeTaken = endTime - startTime;
		CallLog.info(100, "TOTAL RESPONSE time----->>>"+timeTaken);	
		return jsonString;
	}
	
	@RequestMapping(value = "/issueProduct", method = RequestMethod.POST)
	public String issueProduct(@RequestBody PolicyIssueRequestBean requestBean) {
		
		ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setRequestBean(requestBean);
		serviceRequestBean.setProductType(ProductServiceEnum.Insurance.toString());
		serviceRequestBean.setServiceName(ProductEnum.Insurance.policyissue.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		
		ServiceResponseBean serviceResponseBean = serviceResolverFactory.getServiceResponse();
		PolicyIssueResponseBean responseBean  = (PolicyIssueResponseBean) serviceResponseBean.getResponseBean();
		
		ObjectMapper mapper = null;
		String jsonString = null;
		try
		{
			mapper = new ObjectMapper();
			jsonString = mapper.writeValueAsString(responseBean);
		}
		catch (Exception e) {
			CallLog.info(10, e);
		}
		return jsonString;
	}
	
	@RequestMapping(value = "/amendProduct", method = RequestMethod.POST)
	public String amendProduct(@RequestBody PolicyAmendRequestBean requestBean) {
		ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setRequestBean(requestBean);
		serviceRequestBean.setProductType(ProductServiceEnum.Insurance.toString());
		serviceRequestBean.setServiceName(ProductEnum.Insurance.policyamend.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		
		ServiceResponseBean serviceResponseBean = serviceResolverFactory.getServiceResponse();
		PolicyAmendResponseBean responseBean  = (PolicyAmendResponseBean) serviceResponseBean.getResponseBean();
		
		ObjectMapper mapper = null;
		String jsonString = null;
		try
		{
			mapper = new ObjectMapper();
			jsonString = mapper.writeValueAsString(responseBean);
		}
		catch (Exception e) {
			CallLog.info(10, e);
		}
		return jsonString;
	}
	
	@RequestMapping(value = "/cancelProduct", method = RequestMethod.POST)
	public String cancelProduct(@RequestBody PolicyCancelRequestBean requestBean) {
		ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setRequestBean(requestBean);
		serviceRequestBean.setProductType(ProductServiceEnum.Insurance.toString());
		serviceRequestBean.setServiceName(ProductEnum.Insurance.policycancel.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		
		ServiceResponseBean serviceResponseBean = serviceResolverFactory.getServiceResponse();
		PolicyCancelResponseBean responseBean  = (PolicyCancelResponseBean) serviceResponseBean.getResponseBean();
		
		ObjectMapper mapper = null;
		String jsonString = null;
		try
		{
			mapper = new ObjectMapper();
			jsonString = mapper.writeValueAsString(responseBean);
		}
		catch (Exception e) {
			CallLog.info(10, e);
		}
		return jsonString;
	}
	
	@RequestMapping(value = "/resendPolicy", method = RequestMethod.POST)
	public String resendPolicy(@RequestBody PolicyResendRequestBean requestBean) {
		ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setRequestBean(requestBean);
		serviceRequestBean.setProductType(ProductServiceEnum.Insurance.toString());
		serviceRequestBean.setServiceName(ProductEnum.Insurance.policyresend.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		
		ServiceResponseBean serviceResponseBean = serviceResolverFactory.getServiceResponse();
		PolicyResendResponseBean responseBean  = (PolicyResendResponseBean) serviceResponseBean.getResponseBean();
		
		ObjectMapper mapper = null;
		String jsonString = null;
		try
		{
			mapper = new ObjectMapper();
			jsonString = mapper.writeValueAsString(responseBean);
		}
		catch (Exception e) {
			CallLog.info(10, e);
		}
		return jsonString;
	}
	
	
	@RequestMapping(value = "/searchInsuranceCrossSell", method = RequestMethod.POST)
	public String searchInsuranceCrossSell(@RequestBody ProductPriceRequestBean requestBean) {
		
		ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setRequestBean(requestBean);
		serviceRequestBean.setProductType(ProductServiceEnum.Insurance.toString());
		serviceRequestBean.setServiceName(ProductEnum.Insurance.pricecrosssell.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		
		ServiceResponseBean serviceResponseBean = serviceResolverFactory.getServiceResponse();
		ProductFamilyPriceResponseBean responseBean  = (ProductFamilyPriceResponseBean) serviceResponseBean.getResponseBean();
		
		
		ObjectMapper mapper = null;
		String jsonString = null;
		try
		{
			mapper = new ObjectMapper();
			jsonString = mapper.writeValueAsString(responseBean);
		}
		catch (Exception e) {
			CallLog.info(10, e);
		}
		return jsonString;
	}
	
	@RequestMapping(value = "/downloadSchedule", method = RequestMethod.POST)
	public void downloadSchedule(@RequestBody PolicyResendRequestBean requestBean) {
		
		ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setRequestBean(requestBean);
		serviceRequestBean.setProductType(ProductServiceEnum.Insurance.toString());
		serviceRequestBean.setServiceName(ProductEnum.Insurance.schedule.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		ServiceResponseBean serviceResponseBean = serviceResolverFactory.getServiceResponse();
	}
}
