package com.tt.ws.rest.insurance.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tt.nc.common.util.QueryConstant;
import com.tt.ts.common.modal.ResultBean;
import com.tt.ts.geolocations.service.GeoLocationService;
import com.tt.ts.product.model.ProductModel;
import com.tt.ts.product.service.ProductService;
import com.tt.ts.rest.common.RedisService;
import com.tt.ts.rest.common.util.CommonUtil;
import com.tt.ts.rest.common.util.RedisCacheUtil;
import com.tt.ts.rest.corporate.service.CorporateService;
import com.tt.ts.rest.ruleengine.service.KieSessionService;
import com.tt.ts.rest.ruleengine.service.RuleSimulationService;
import com.tt.ws.rest.insurance.modal.InsuranceValidation;
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
import com.ws.services.insurance.bean.product.FProduct;
import com.ws.services.insurance.bean.product.PriceDetail;
import com.ws.services.insurance.bean.product.ProductFamilyPriceResponseBean;
import com.ws.services.insurance.bean.product.ProductPriceRequestBean;
import com.ws.services.util.CallLog;
import com.ws.services.util.ProductEnum;
import com.ws.services.util.ProductServiceEnum;


@Service
public class InsuranceWSService
{
    private static final String REST_CACHE_PORT = "tt.rest.memcache.port";

	@Autowired
	MessageSource messageSource;

	@Autowired
	RedisService redisService;
	
    @Autowired
	ProductService productService;
    
    @Autowired
    CorporateService corporateService;
    
    @Autowired
    GeoLocationService geoLocationService;
	
	@RequestMapping(value = "/searchInsuranceCrossSell", method = RequestMethod.POST)
	public String searchInsuranceCrossSell(ProductPriceRequestBean requestBean) {
		
		ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setRequestBean(requestBean);
		serviceRequestBean.setProductType(ProductServiceEnum.Insurance.toString());
		serviceRequestBean.setServiceName(ProductEnum.Insurance.pricecrosssell.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		
		ProductFamilyPriceResponseBean finalSearchResponseBean = null;
		ResultBean resultBeanRule = null;
		CommonUtil<Object> commonUtil = new CommonUtil<>();
		String jsonString = "";
		Long startTime = System.currentTimeMillis();
		boolean isRuleTextUpdated = false;
		ProductModel productModel = new ProductModel();
		productModel.setStatus(1);
		productModel.setProductCode(QueryConstant.RULE_GROUP_THREE);
		
		ResultBean resultBeanProduct = productService.getProducts(productModel);
		if (resultBeanProduct != null && !resultBeanProduct.isError()) {
			List<ProductModel> productList = (List<ProductModel>) resultBeanProduct.getResultList();
			if (productList != null && !productList.isEmpty()) {
				ProductModel productModal = productList.get(0);
				if (productModal != null && productModal.getRuleText() != null && !productModal.getRuleText().isEmpty()) {		
					boolean flagTextRule = KieSessionService.writeRuleTextInsurance(productModal.getRuleText());
					if (flagTextRule) {
						KieSessionService.getKieSessionInsurance();
					}
				}else{
					KieSessionService.kieSessInsurance = null;
				}

			}
		}
		
		finalSearchResponseBean = InsuranceHelperUtilRest.processToInsuranceSearchResponseObject(serviceRequestBean, requestBean);
		
		ResultBean resultBeanAgencymarkup = settingAgencyMarkup(requestBean,finalSearchResponseBean);
		if(resultBeanAgencymarkup!=null && !resultBeanAgencymarkup.isError()) {
			finalSearchResponseBean = (ProductFamilyPriceResponseBean)resultBeanAgencymarkup.getResultObject();
		}
		   resultBeanRule = applyRuleOnInsuranceSearchResponse(requestBean, finalSearchResponseBean);
		   if (resultBeanRule != null && !resultBeanRule.isError() && resultBeanRule.getResultList()!=null &&  !resultBeanRule.getResultList().isEmpty()) {
				List<FProduct> updatedOptions = (List<FProduct>) resultBeanRule.getResultList();
				finalSearchResponseBean.getFfackage().setfProduct(updatedOptions);
			}	
		  
		   jsonString = CommonUtil.convertIntoJson(finalSearchResponseBean);
		   return jsonString;
	}
    
    
    private ResultBean applyRuleOnInsuranceSearchResponse(ProductPriceRequestBean requestBean,ProductFamilyPriceResponseBean responseBean) {
    	ResultBean resultBean = null;
    	if(responseBean != null && responseBean.getFfackage() != null){
	    	List<FProduct> fProduct = responseBean.getFfackage().getfProduct();
			resultBean = RuleSimulationService.applyRuleOnInsurance(fProduct, requestBean, geoLocationService, corporateService,productService);
    	}
    	return resultBean;
    }
    
	public String getInsurancePackages(ProductPriceRequestBean requestBean, HttpServletRequest request) {
		ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
		
		serviceRequestBean.setRequestBean(requestBean);
		serviceRequestBean.setProductType(ProductServiceEnum.Insurance.toString());
		String portCache = messageSource.getMessage(REST_CACHE_PORT, null, null, null);
		String jsonString = null;
		String searchInsuranceKey = requestBean.getInsuranceSearchKey();
		Boolean isSearchKeyFound = false;
		ResultBean resultBean = RedisCacheUtil.isSearchKeyInCacheOthers(searchInsuranceKey,redisService);
		if(resultBean != null && !resultBean.isError()) {
		    isSearchKeyFound = resultBean.getResultBoolean();
		}
		
		ProductFamilyPriceResponseBean finalSearchResponseBean = null;
		ResultBean resultBeanRule = null;
		CommonUtil<Object> commonUtil = new CommonUtil<>();
		
		
		Long startTime = System.currentTimeMillis();
		boolean isRuleTextUpdated = false;
		ProductModel productModel = new ProductModel();
		productModel.setStatus(1);
		productModel.setProductCode(QueryConstant.RULE_GROUP_THREE);

		ResultBean resultBeanProduct = productService.getProducts(productModel);
		if (resultBeanProduct != null && !resultBeanProduct.isError()) {
			List<ProductModel> productList = (List<ProductModel>) resultBeanProduct.getResultList();
			if (productList != null && !productList.isEmpty()) {
				ProductModel productModal = productList.get(0);
				if (productModal != null && productModal.getRuleText() != null && !productModal.getRuleText().isEmpty()) {

					Date ruleLastUpdated = productModal.getRuleLastUpdated();
					Date kieSessionLastUpdated = KieSessionService.kieSessionInsuranceLastUpdated;

					if (ruleLastUpdated != null && kieSessionLastUpdated != null && ruleLastUpdated.getTime() < kieSessionLastUpdated.getTime()) {
						isRuleTextUpdated = true;
					}
					if (KieSessionService.kieSessInsurance != null && isRuleTextUpdated) {
						KieSessionService.getKieSessionInsurance();						
					} else {
						boolean flagTextRule = KieSessionService.writeRuleTextInsurance(productModal.getRuleText());
						if (flagTextRule) {
							KieSessionService.kieSessInsurance = null;
							KieSessionService.getKieSessionInsurance();
							KieSessionService.kieSessionInsuranceLastUpdated = new Date();
						}
					}

				}else{
					KieSessionService.kieSessInsurance = null;
				}

			}
		}
		if (isSearchKeyFound) {
		    ResultBean resultBeanCache = RedisCacheUtil.getResponseFromCacheOther(searchInsuranceKey,redisService);
			if(resultBeanCache != null && !resultBeanCache.isError()) {
			    jsonString = resultBeanCache.getResultString();
			    finalSearchResponseBean = (ProductFamilyPriceResponseBean) commonUtil.convertJSONIntoObject(jsonString, ProductFamilyPriceResponseBean.class);
			    if (!isRuleTextUpdated) {
			    	resultBeanRule = applyRuleOnInsuranceSearchResponse(requestBean, finalSearchResponseBean);

					if (resultBeanRule != null && !resultBeanRule.isError() && resultBeanRule.getResultList()!= null && !resultBeanRule.getResultList().isEmpty()) {
						List<FProduct> updatedInsuranceOptions = (List<FProduct>) resultBeanRule.getResultList();
						finalSearchResponseBean.getFfackage().setfProduct(updatedInsuranceOptions);
					}
					
					if (resultBean != null && !resultBean.isError()) {
						jsonString = CommonUtil.convertIntoJson(finalSearchResponseBean);
						if(finalSearchResponseBean!= null && finalSearchResponseBean.getFfackage() != null && finalSearchResponseBean.getFfackage().getfProduct() != null && !finalSearchResponseBean.getFfackage().getfProduct().isEmpty())
							RedisCacheUtil.setResponseInCacheOther(searchInsuranceKey, jsonString, redisService);
					}
			    }
			}else {
				finalSearchResponseBean = InsuranceHelperUtilRest.processToInsuranceSearchResponseObject(serviceRequestBean, requestBean);
				   resultBeanRule = applyRuleOnInsuranceSearchResponse(requestBean, finalSearchResponseBean);
				   if (resultBeanRule != null && !resultBeanRule.isError() && resultBeanRule.getResultList() != null && !resultBeanRule.getResultList().isEmpty()) {
						List<FProduct> updatedFlightOptions = (List<FProduct>) resultBeanRule.getResultList();
						finalSearchResponseBean.getFfackage().setfProduct(updatedFlightOptions);
					}
				   if (resultBean != null && !resultBean.isError()) {
						jsonString = CommonUtil.convertIntoJson(finalSearchResponseBean);
						if(finalSearchResponseBean!= null && finalSearchResponseBean.getFfackage() != null && finalSearchResponseBean.getFfackage().getfProduct() != null && !finalSearchResponseBean.getFfackage().getfProduct().isEmpty())
							RedisCacheUtil.setResponseInCacheOther(searchInsuranceKey, jsonString, redisService);
					}
				}
		} else {
			finalSearchResponseBean = InsuranceHelperUtilRest.processToInsuranceSearchResponseObject(serviceRequestBean, requestBean);
		   resultBeanRule = applyRuleOnInsuranceSearchResponse(requestBean, finalSearchResponseBean);
		   if (resultBeanRule != null && !resultBeanRule.isError() && resultBeanRule.getResultList() != null && !resultBeanRule.getResultList().isEmpty()) {
				List<FProduct> updatedFlightOptions = (List<FProduct>) resultBeanRule.getResultList();
				finalSearchResponseBean.getFfackage().setfProduct(updatedFlightOptions);
			}
		   if (resultBean != null && !resultBean.isError()) {
				jsonString = CommonUtil.convertIntoJson(finalSearchResponseBean);
				if(finalSearchResponseBean!= null && finalSearchResponseBean.getFfackage() != null && finalSearchResponseBean.getFfackage().getfProduct() != null && !finalSearchResponseBean.getFfackage().getfProduct().isEmpty())
					RedisCacheUtil.setResponseInCacheOther(searchInsuranceKey, jsonString, redisService);
			}		
		}
		
//		finalSearchResponseBean = InsuranceHelperUtilRest.processToInsuranceSearchResponseObject(serviceRequestBean, requestBean);
		
		ResultBean resultBeanAgencymarkup = settingAgencyMarkup(requestBean,finalSearchResponseBean);
		if(resultBeanAgencymarkup!=null && !resultBeanAgencymarkup.isError()) {
			finalSearchResponseBean = (ProductFamilyPriceResponseBean)resultBeanAgencymarkup.getResultObject();
		}
		
		ResultBean resultBeanValidationmarkup = insuranceValidation(finalSearchResponseBean, request);
		
		jsonString = CommonUtil.convertIntoJson(finalSearchResponseBean);
		Long endTime = System.currentTimeMillis();
		Long timeTaken = endTime - startTime;
		CallLog.info(100, "TOTAL RESPONSE time----->>>"+timeTaken);	
		return jsonString;
	}
	
	public ResultBean insuranceValidation(ProductFamilyPriceResponseBean searchResponseBean, HttpServletRequest request){
		ResultBean resultBean = new ResultBean();
		InsuranceValidation i = null;
		try {
			if(searchResponseBean != null && searchResponseBean.getFfackage() != null && searchResponseBean.getFfackage().getfProduct() != null && !searchResponseBean.getFfackage().getfProduct().isEmpty()){
				for(FProduct f : searchResponseBean.getFfackage().getfProduct()){
					PriceDetail price = f.getConsolidatedPriceDetail().get(0);
					i = new InsuranceValidation();
					i.setCurrency(price.getCurrencyType());
					i.setAgencyMarkup(price.getAgencyMarkup());
					i.setDiscountPrice(price.getDiscountPrice());
					i.setMarkupPrice(price.getMarkupPrice());
					i.setServiceChargePrice(price.getServiceChargePrice());
					i.setSupplierCurrency("USD");
					i.setT3Price(price.getT3Price());
					i.setTotalBaseFare(price.getBaseAmount());
					i.setBaseAmountBySupplier(price.getBaseAmountBySupplier());
					i.setInsuranceOptionKey(f.getId());
					request.getSession().setAttribute(f.getId(), i);
					CallLog.info(100, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..->>>");	
					CallLog.info(100, ">>>>>>>>>>>>>>>>>>>>>>>>>>>..->>>"+ i.toString());	
					CallLog.info(100, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..->>>");	
					
				}
			}
		} catch (Exception e) {
			resultBean.setIserror(true);
		}
		return resultBean;
	}
	
	public String issueProduct(PolicyIssueRequestBean requestBean) {
		
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
	
	
	public void downloadSchedule(PolicyResendRequestBean requestBean) {
		ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setRequestBean(requestBean);
		serviceRequestBean.setProductType(ProductServiceEnum.Insurance.toString());
		serviceRequestBean.setServiceName(ProductEnum.Insurance.schedule.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		ServiceResponseBean serviceResponseBean = serviceResolverFactory.getServiceResponse();
	}
	
	public String amendProduct(PolicyAmendRequestBean requestBean) {
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
	
	public String cancelProduct(PolicyCancelRequestBean requestBean) {
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
	public String resendPolicy(PolicyResendRequestBean requestBean) {
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
	
	
	public ResultBean settingAgencyMarkup(ProductPriceRequestBean requestBean, ProductFamilyPriceResponseBean finalSearchResponseBean) {

		ResultBean resultBean = new ResultBean();
		try {
			
			if ( requestBean.getListAgencyMarkup() != null && ! requestBean.getListAgencyMarkup().isEmpty()) {
				boolean flag = true;
				for (int i = 0; i <  requestBean.getListAgencyMarkup().size(); i++) {
					Double t3PriceOnly = 0.0;
					Object[] agencyList = (Object[])  requestBean.getListAgencyMarkup().get(i);
					if(agencyList != null) {
						boolean condition = agencyList[2].toString() != null && !"null".equalsIgnoreCase(agencyList[2].toString()) && !"".equalsIgnoreCase(agencyList[2].toString());
						for(FProduct f :finalSearchResponseBean.getFfackage().getfProduct()){
							t3PriceOnly = f.getConsolidatedPriceDetail().get(0).getT3Price() - f.getConsolidatedPriceDetail().get(0).getServiceChargePrice();
							if(agencyList[1].toString().equals("1")){  	// %
								double agentMarkup = t3PriceOnly * (Double.parseDouble(agencyList[2].toString())/100);
								f.getConsolidatedPriceDetail().get(0).setAgencyMarkup(condition?agentMarkup:0);
							} else{
								f.getConsolidatedPriceDetail().get(0).setAgencyMarkup(condition?Double.parseDouble(agencyList[2].toString()):0);
							}
							flag = false;
							if (flag)
								f.getConsolidatedPriceDetail().get(0).setAgencyMarkup(0);
						}
					}
				}
				
			}
			resultBean.setResultObject(finalSearchResponseBean);
		} catch (Exception e) {
			resultBean.setIserror(true);
		}

		return resultBean;
	}
	
}
