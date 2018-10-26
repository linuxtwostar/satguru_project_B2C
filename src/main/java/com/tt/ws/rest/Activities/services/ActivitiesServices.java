package com.tt.ws.rest.Activities.services;

import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tt.nc.common.util.QueryConstant;
import com.tt.satguruportal.activities.model.ActivitiesWidget;
import com.tt.satguruportal.agent.model.AgencyMarkup;
import com.tt.satguruportal.hotel.model.HotelResultsRequestBean;
import com.tt.satguruportal.login.bean.UserSessionBean;
import com.tt.ts.common.modal.ResultBean;
import com.tt.ts.product.model.ProductModel;
import com.tt.ts.product.service.ProductService;
import com.tt.ts.rest.agent.model.AgencyMarkupModel;
import com.tt.ts.rest.agent.service.AgentService;
import com.tt.ts.rest.common.RedisService;
import com.tt.ts.rest.common.util.CommonUtil;
import com.tt.ts.rest.common.util.RedisCacheUtil;
import com.tt.ts.rest.ruleengine.service.KieSessionService;
import com.tt.ts.rest.ruleengine.service.RuleSimulationService;
import com.tt.ws.rest.Activities.model.ActivitiesResultRequestBean;
import com.tt.ws.rest.Activities.utils.ActivitiesUtil;
import com.ws.interaction.beans.ServiceRequestBean;
import com.ws.interaction.beans.ServiceResponseBean;
import com.ws.interaction.factory.ServiceResolverFactory;
import com.ws.services.activities.bean.ActivitiesSearchRequestBean;
import com.ws.services.activities.bean.ActivitiesSearchResponseBean;
import com.ws.services.util.CallLog;

@Service
public class ActivitiesServices 
{
	ServiceResolverFactory serviceResolverFactory = null;
	ServiceRequestBean serviceRequestBean = null;
	ServiceResponseBean serviceResponseBean = null;
	String logStr = "[ActivitiesServices]";

	@Autowired
	RedisService redisService;

	@Autowired
	ProductService productService;


	@SuppressWarnings("unchecked")
	public String applyRuleEngineOnResult(String activityResults, ActivitiesSearchRequestBean requestBean,UserSessionBean userSessionBean,RuleSimulationService ruleSimulationService) 
	{
		String jsonString;
		long startingTime = 0;
		
		serviceRequestBean = new ServiceRequestBean();

		String agencyId=userSessionBean.getAgencyId();
		String branchId=userSessionBean.getBranchId();
		ActivitiesSearchResponseBean activitiesSearchRespBean = null;
		CommonUtil<Object> commonUtil = new CommonUtil<>();

		/* Start Code to apply rule engine */
		boolean isRuleTextUpdated = false;
		ProductModel productModel = new ProductModel();
		productModel.setStatus(1);
		productModel.setProductCode(QueryConstant.RULE_GROUP_FIVE);
		ResultBean resultBeanProduct = productService.getProducts(productModel);
		if (resultBeanProduct != null && !resultBeanProduct.isError()) 
		{
			List<ProductModel> productList = (List<ProductModel>) resultBeanProduct.getResultList();
			if (productList != null && !productList.isEmpty()) 
			{
				ProductModel productModal = productList.get(0);
				if (productModal != null && productModal.getRuleText() != null && !productModal.getRuleText().isEmpty()) 
				{

					Date ruleLastUpdated = productModal.getRuleLastUpdated();
					Date kieSessionLastUpdated = KieSessionService.kieActivitySessionLastUpdated;
					if (ruleLastUpdated != null && kieSessionLastUpdated != null
							&& ruleLastUpdated.getTime() < kieSessionLastUpdated.getTime()) {
						CallLog.info(105,
								"Rules for Activity Not changed.\nRule Last Updated Time :: " + ruleLastUpdated
								+ "(Time : " + ruleLastUpdated.getTime()
								+ " ). KissSession Last Updated Time :: " + kieSessionLastUpdated
								+ "(Time : " + kieSessionLastUpdated.getTime() + ").");
						isRuleTextUpdated = true;
					}
					if (KieSessionService.kieSessActivity != null && isRuleTextUpdated) {
						KieSessionService.getKieSessionActivity();

					} else {
						startingTime = System.currentTimeMillis();
						boolean flagTextRule = KieSessionService.writeRuleTextActivity(productModal.getRuleText());
						if (flagTextRule) {
							KieSessionService.kieSessActivity = null;
							KieSessionService.getKieSessionActivity();
						}
						CallLog.info(105, "Rules for Activity Modified.\nRule Last Updated Time :: " + ruleLastUpdated
								+ "(Time : " + ruleLastUpdated.getTime() + " ). KissSession Last Updated Time :: "
								+ KieSessionService.kieActivitySessionLastUpdated + "(Time : "
								+ KieSessionService.kieActivitySessionLastUpdated.getTime() + ").");
					}
				}
				else {
					KieSessionService.kieSessActivity = null;
					CallLog.info(105, "No approved Activity rules found.");
				}
			}
		}


		activitiesSearchRespBean = (ActivitiesSearchResponseBean) commonUtil.convertJSONIntoObject(activityResults, ActivitiesSearchResponseBean.class);
		jsonString = ruleSimulationService.applyRuleOnActivitiesResult(activitiesSearchRespBean,KieSessionService.kieSessActivity,agencyId,branchId,requestBean);



		return jsonString;
	}

	@SuppressWarnings("unchecked")
	public String getTenFilteredResult(com.tt.ws.rest.Activities.model.ActivitiesResultRequestBean activitiesResultRequestBean,UserSessionBean userSessionBean,RuleSimulationService ruleSimulationService,AgentService agentServices) 
	{
		String jsonString;
		long startingTime = 0;
		long endingTime = 0;
		long totalTime = 0;
		long totalTimeInMillis = 0;
		double grossPrice = 0;
		double totalAgencyMarkup = 0;
		double totalPrice = 0;
		double totalAmtToPay=0;
		double priceWithT3Markup=0;
		double priceWithoutServiceCharge=0;
		serviceRequestBean = new ServiceRequestBean();
		ActivitiesWidget activitiesWidget= activitiesResultRequestBean.getActivitiesWidget();
		ActivitiesSearchRequestBean activitiesReqBean=activitiesResultRequestBean.getSearchRequestBean();
		activitiesReqBean =ActivitiesUtil.keyForTenFilteredResults(activitiesReqBean,userSessionBean.getAgentId());

		String activitySearchKey = activitiesReqBean.getActivitiesSearchKey();

		CallLog.info(1, "activitiesSearchKey for getFilteredResult Call === " + activitySearchKey);
		Boolean isSearchKeyFound = false;
		String agencyId=userSessionBean.getAgencyId();
		String branchId=userSessionBean.getBranchId();
		ResultBean resultBean = RedisCacheUtil.isSearchKeyInCacheActivity(activitySearchKey, redisService);
		ActivitiesSearchResponseBean activitiesSearchRespBean = null;

		if (null != userSessionBean.getAgencyCode())
			activitiesResultRequestBean = fetchAgencyMarkup(activitiesResultRequestBean, agentServices,userSessionBean);


		if (resultBean != null && !resultBean.isError()) {
			isSearchKeyFound = resultBean.getResultBoolean();
		}

		if (isSearchKeyFound) {
			/* Start Code to apply rule engine */
			Long startTime = System.currentTimeMillis();
			boolean isRuleTextUpdated = false;
			ProductModel productModel = new ProductModel();
			productModel.setStatus(1);
			productModel.setProductCode(QueryConstant.RULE_GROUP_FIVE);
			ResultBean resultBeanProduct = productService.getProducts(productModel);
			if (resultBeanProduct != null && !resultBeanProduct.isError()) {
				List<ProductModel> productList = (List<ProductModel>) resultBeanProduct.getResultList();
				if (productList != null && !productList.isEmpty()) {
					ProductModel productModal = productList.get(0);
					if (productModal != null && productModal.getRuleText() != null
							&& !productModal.getRuleText().isEmpty()) {

						Date ruleLastUpdated = productModal.getRuleLastUpdated();
						Date kieSessionLastUpdated = KieSessionService.kieActivitySessionLastUpdated;
						if (ruleLastUpdated != null && kieSessionLastUpdated != null
								&& ruleLastUpdated.getTime() < kieSessionLastUpdated.getTime()) {
							CallLog.info(105,
									"Rules for Activity Not changed.\nRule Last Updated Time :: " + ruleLastUpdated
									+ "(Time : " + ruleLastUpdated.getTime()
									+ " ). KissSession Last Updated Time :: " + kieSessionLastUpdated
									+ "(Time : " + kieSessionLastUpdated.getTime() + ").");
							isRuleTextUpdated = true;
						}
						if (KieSessionService.kieSessActivity != null && isRuleTextUpdated) {
							KieSessionService.getKieSessionActivity();

						} else {
							startingTime = System.currentTimeMillis();
							boolean flagTextRule = KieSessionService.writeRuleTextActivity(productModal.getRuleText());
							if (flagTextRule) {
								KieSessionService.kieSessActivity = null;
								KieSessionService.getKieSessionActivity();
							}
							CallLog.info(105, "Rules for Activity Modified.\nRule Last Updated Time :: " + ruleLastUpdated
									+ "(Time : " + ruleLastUpdated.getTime() + " ). KissSession Last Updated Time :: "
									+ KieSessionService.kieActivitySessionLastUpdated + "(Time : "
									+ KieSessionService.kieActivitySessionLastUpdated.getTime() + ").");
						}
					}
					else {
						KieSessionService.kieSessActivity = null;
						CallLog.info(105, "No approved Activity rules found.");
					}
				}
			}
			startingTime = System.currentTimeMillis();
			ResultBean resultBeanCache = RedisCacheUtil.getResponseFromCacheActivity(activitySearchKey, redisService);
			endingTime = System.currentTimeMillis();
			totalTime = (endingTime - startTime) / 1000;
			totalTimeInMillis = endingTime - startingTime;
			CallLog.info(103, "TOTAL TIME TO FECTH FILTERED ACTIVITY RESPONSE FROM CACHE ::: " + totalTime
					+ " SECONDS || TIME(millisecs)::" + totalTimeInMillis + " milliseconds");
			if (resultBeanCache != null && !resultBeanCache.isError()) {
				jsonString = resultBeanCache.getResultString();
				CallLog.createXmlFiles(jsonString, "Activities", "Search", "FilteredResp.json");
				startingTime = System.currentTimeMillis();
				endingTime = System.currentTimeMillis();
				totalTime = (endingTime - startTime) / 1000;
				totalTimeInMillis = endingTime - startingTime;
				CallLog.info(103, "TOTAL TIME TO DESERIALIZE FILTERED ACTIVITY RESPONSE OF CACHE ::: " + totalTime
						+ " SECONDS || TIME(millisecs)::" + totalTimeInMillis + " milliseconds");

				if (!isRuleTextUpdated) 
				{
					CommonUtil<Object> commonUtil = new CommonUtil<>();
					startingTime = System.currentTimeMillis();
					CallLog.createXmlFiles(jsonString, "Activities", "Search", "FilteredResp.json");
					jsonString = ruleSimulationService.applyRuleOnActivitiesResult(activitiesSearchRespBean,KieSessionService.kieSessActivity,agencyId,branchId,activitiesReqBean);
					activitiesSearchRespBean = (ActivitiesSearchResponseBean) commonUtil.convertJSONIntoObject(jsonString, ActivitiesSearchResponseBean.class);
					//JSONObject filteredJsonArr = new JSONObject(jsonString);
					JSONArray activityJsonArr = new JSONArray(jsonString);
					for (int i = 0; i < activityJsonArr.length(); i++) 
					{
						JSONObject activitiesJsonObj = activityJsonArr.getJSONObject(i);

						double orgFare = activitiesJsonObj.optDouble("price");
						double discPrice = activitiesJsonObj.getDouble("discountPrice");
						double t3MarkUp=activitiesJsonObj.getDouble("t3Markup");
						double serviceChargePrice=activitiesJsonObj.getDouble("serviceChargePrice");
						totalAmtToPay = orgFare + t3MarkUp + serviceChargePrice - discPrice;

						totalPrice=orgFare+activitiesJsonObj.getDouble("serviceChargePrice")+activitiesJsonObj.getDouble("t3Markup");
						
						priceWithT3Markup=orgFare + t3MarkUp ;
						priceWithoutServiceCharge=orgFare + t3MarkUp-discPrice;
						if (activitiesResultRequestBean.getTotalAgencyMarkUp() > 0)
							totalAgencyMarkup = activitiesResultRequestBean.getTotalAgencyMarkUp();

						if (activitiesResultRequestBean.getMarkUpType() == 1)
						{
							totalAgencyMarkup = (totalPrice -discPrice) * (totalAgencyMarkup / 100);
							grossPrice = totalPrice + totalAgencyMarkup - discPrice;
						}
						else
							grossPrice = totalPrice + totalAgencyMarkup - discPrice;

						/*if(Double.valueOf(currConvertRate).equals(0.0))
							currConvertRate = 1;*/

						activitiesJsonObj.put("t3Markup",t3MarkUp);
						activitiesJsonObj.put("grossPrice",grossPrice);
						activitiesJsonObj.put("totalAgencyMarkup",totalAgencyMarkup);
						activitiesJsonObj.put("serviceChargePrice", serviceChargePrice);
						activitiesJsonObj.put("discountPrice", discPrice);
						activitiesJsonObj.put("priceWithoutServiceCharge", priceWithoutServiceCharge);
						activitiesJsonObj.put("priceWithT3Markup", priceWithT3Markup);
						activitiesJsonObj.put("totalAmtToPay", totalAmtToPay);
						activitiesJsonObj.put("supplierPrice", orgFare);


						if(discPrice>=grossPrice || discPrice>=priceWithoutServiceCharge)
							activityJsonArr.remove(i);
					}
					jsonString = activityJsonArr.toString();
					RedisCacheUtil.setResponseInCacheActivity(activitySearchKey, jsonString, redisService);
					endingTime = System.currentTimeMillis();
					totalTime = (endingTime - startTime) / 1000;
					totalTimeInMillis = endingTime - startingTime;
					CallLog.info(103, "TOTAL TIME TO APPLY RULE ON CACHE RESPONSE ::: " + totalTime
							+ " SECONDS || TIME(millisecs)::" + totalTimeInMillis + " milliseconds");
				} 
				else 
				{
					/*if (resultsReqBean.getTotalAgencyMarkUp() > 0)
					    totalAgencyMarkup = resultsReqBean.getTotalAgencyMarkUp();*/

					//JSONArray filteredJsonArr = new JSONArray(jsonString);
					JSONArray activityJsonArr = new JSONArray(jsonString);
					for(int i=0;i<activityJsonArr.length();i++)
					{
						JSONObject activitiesJsonObj = activityJsonArr.getJSONObject(i);

						double orgFare = activitiesJsonObj.optDouble("price");
						double discPrice = activitiesJsonObj.getDouble("discountPrice");
						double t3MarkUp=activitiesJsonObj.getDouble("t3Markup");
						double serviceChargePrice=activitiesJsonObj.getDouble("serviceChargePrice");
						totalAmtToPay = orgFare + t3MarkUp + serviceChargePrice - discPrice;

						totalPrice=orgFare+activitiesJsonObj.getDouble("serviceChargePrice")+activitiesJsonObj.getDouble("t3Markup");

						priceWithT3Markup=orgFare + t3MarkUp ;
						priceWithoutServiceCharge=orgFare + t3MarkUp-discPrice;
						if (activitiesResultRequestBean.getTotalAgencyMarkUp() > 0)
							totalAgencyMarkup = activitiesResultRequestBean.getTotalAgencyMarkUp();

						if (activitiesResultRequestBean.getMarkUpType() == 1)
						{
							totalAgencyMarkup = (totalPrice -discPrice) * (totalAgencyMarkup / 100);
							grossPrice = totalPrice + totalAgencyMarkup - discPrice;
						}
						else
							grossPrice = totalPrice + totalAgencyMarkup - discPrice;

						/*if(Double.valueOf(currConvertRate).equals(0.0))
							currConvertRate = 1;*/
						
						activitiesJsonObj.put("t3Markup",t3MarkUp);
						activitiesJsonObj.put("grossPrice",grossPrice);
						activitiesJsonObj.put("totalAgencyMarkup",totalAgencyMarkup);
						activitiesJsonObj.put("serviceChargePrice", serviceChargePrice);
						activitiesJsonObj.put("discountPrice", discPrice);
						activitiesJsonObj.put("priceWithoutServiceCharge", priceWithoutServiceCharge);
						activitiesJsonObj.put("priceWithT3Markup", priceWithT3Markup);
						activitiesJsonObj.put("totalAmtToPay", totalAmtToPay);
						activitiesJsonObj.put("supplierPrice", orgFare);
					}
					jsonString = activityJsonArr.toString();
				}
			} else
				jsonString = "No Results";
		} else
			jsonString = "No Results";

		return jsonString;
	}
	@SuppressWarnings("unchecked")
	public String getFilteredResult(com.tt.ws.rest.Activities.model.ActivitiesResultRequestBean activitiesResultRequestBean,UserSessionBean userSessionBean,RuleSimulationService ruleSimulationService,AgentService agentServices) 
	{
		String jsonString;
		long startingTime = 0;
		long endingTime = 0;
		long totalTime = 0;
		long totalTimeInMillis = 0;
		double grossPrice = 0;
		double totalAgencyMarkup = 0;
		double totalPrice = 0;
		double totalAmtToPay=0;
		double priceWithT3Markup=0;
		double priceWithoutServiceCharge=0;
		serviceRequestBean = new ServiceRequestBean();
		ActivitiesWidget activitiesWidget= activitiesResultRequestBean.getActivitiesWidget();
		ActivitiesSearchRequestBean activitiesReqBean=activitiesResultRequestBean.getSearchRequestBean();
		
		activitiesReqBean =ActivitiesUtil.keyForFilteredResults(activitiesReqBean,userSessionBean.getAgentId());
		String activitySearchKey = activitiesReqBean.getActivitiesSearchKey();

		CallLog.info(1, "activitiesSearchKey for getFilteredResult Call === " + activitySearchKey);
		Boolean isSearchKeyFound = false;
		String agencyId=userSessionBean.getAgencyId();
		String branchId=userSessionBean.getBranchId();
		ResultBean resultBean = RedisCacheUtil.isSearchKeyInCacheActivity(activitySearchKey, redisService);
		ActivitiesSearchResponseBean activitiesSearchRespBean = null;

		if (null != userSessionBean.getAgencyCode())
			activitiesResultRequestBean = fetchAgencyMarkup(activitiesResultRequestBean, agentServices,userSessionBean);

		if (resultBean != null && !resultBean.isError()) {
			isSearchKeyFound = resultBean.getResultBoolean();
		}

		if (isSearchKeyFound) {
			/* Start Code to apply rule engine */
			Long startTime = System.currentTimeMillis();
			boolean isRuleTextUpdated = false;
			ProductModel productModel = new ProductModel();
			productModel.setStatus(1);
			productModel.setProductCode(QueryConstant.RULE_GROUP_FIVE);
			ResultBean resultBeanProduct = productService.getProducts(productModel);
			if (resultBeanProduct != null && !resultBeanProduct.isError()) {
				List<ProductModel> productList = (List<ProductModel>) resultBeanProduct.getResultList();
				if (productList != null && !productList.isEmpty()) {
					ProductModel productModal = productList.get(0);
					if (productModal != null && productModal.getRuleText() != null
							&& !productModal.getRuleText().isEmpty()) {

						Date ruleLastUpdated = productModal.getRuleLastUpdated();
						Date kieSessionLastUpdated = KieSessionService.kieActivitySessionLastUpdated;
						if (ruleLastUpdated != null && kieSessionLastUpdated != null
								&& ruleLastUpdated.getTime() < kieSessionLastUpdated.getTime()) {
							CallLog.info(105,
									"Rules for Activity Not changed.\nRule Last Updated Time :: " + ruleLastUpdated
									+ "(Time : " + ruleLastUpdated.getTime()
									+ " ). KissSession Last Updated Time :: " + kieSessionLastUpdated
									+ "(Time : " + kieSessionLastUpdated.getTime() + ").");
							isRuleTextUpdated = true;
						}
						if (KieSessionService.kieSessActivity != null && isRuleTextUpdated) {
							KieSessionService.getKieSessionActivity();

						} else {
							startingTime = System.currentTimeMillis();
							boolean flagTextRule = KieSessionService.writeRuleTextActivity(productModal.getRuleText());
							if (flagTextRule) {
								KieSessionService.kieSessActivity = null;
								KieSessionService.getKieSessionActivity();
							}
							CallLog.info(105, "Rules for Activity Modified.\nRule Last Updated Time :: " + ruleLastUpdated
									+ "(Time : " + ruleLastUpdated.getTime() + " ). KissSession Last Updated Time :: "
									+ KieSessionService.kieActivitySessionLastUpdated + "(Time : "
									+ KieSessionService.kieActivitySessionLastUpdated.getTime() + ").");
						}
					}
					else {
						KieSessionService.kieSessActivity = null;
						CallLog.info(105, "No approved Activity rules found.");
					}
				}
			}
			startingTime = System.currentTimeMillis();
			ResultBean resultBeanCache = RedisCacheUtil.getResponseFromCacheActivity(activitySearchKey, redisService);
			endingTime = System.currentTimeMillis();
			totalTime = (endingTime - startTime) / 1000;
			totalTimeInMillis = endingTime - startingTime;
			CallLog.info(103, "TOTAL TIME TO FETCH FILTERED ACTIVITY RESPONSE FROM CACHE ::: " + totalTime
					+ " SECONDS || TIME(millisecs)::" + totalTimeInMillis + " milliseconds");
			if (resultBeanCache != null && !resultBeanCache.isError()) {
				jsonString = resultBeanCache.getResultString();
				startingTime = System.currentTimeMillis();
				endingTime = System.currentTimeMillis();
				totalTime = (endingTime - startTime) / 1000;
				totalTimeInMillis = endingTime - startingTime;
				CallLog.info(103, "TOTAL TIME TO DESERIALIZE FILTERED ACTIVITY RESPONSE OF CACHE ::: " + totalTime
						+ " SECONDS || TIME(millisecs)::" + totalTimeInMillis + " milliseconds");

				if (!isRuleTextUpdated) {
					CommonUtil<Object> commonUtil = new CommonUtil<>();
					startingTime = System.currentTimeMillis();
					CallLog.createXmlFiles(jsonString, "Activities", "Search", "FilteredResp.json");


					jsonString = ruleSimulationService.applyRuleOnActivitiesResult(activitiesSearchRespBean,KieSessionService.kieSessActivity,agencyId,branchId,activitiesReqBean);
					activitiesSearchRespBean = (ActivitiesSearchResponseBean) commonUtil.convertJSONIntoObject(jsonString, ActivitiesSearchResponseBean.class);

					JSONObject filteredJsonArr = new JSONObject(jsonString);
					JSONArray activityJsonArr = filteredJsonArr.getJSONArray("activitiesList");
					for (int i = 0; i < activityJsonArr.length(); i++) {
						JSONObject activitiesJsonObj = activityJsonArr.getJSONObject(i);
						
						//double currConvertRate = activitiesJsonObj.optDouble("currConvertRate");
						double orgFare = activitiesJsonObj.optDouble("price");
						double discPrice = activitiesJsonObj.getDouble("discountPrice");
						double t3MarkUp=activitiesJsonObj.getDouble("t3Markup");
						double serviceChargePrice=activitiesJsonObj.getDouble("serviceChargePrice");
						totalAmtToPay = orgFare + t3MarkUp + serviceChargePrice - discPrice;

						totalPrice=orgFare+activitiesJsonObj.getDouble("serviceChargePrice")+activitiesJsonObj.getDouble("t3Markup");
						
						priceWithT3Markup=orgFare + t3MarkUp ;
						priceWithoutServiceCharge=orgFare + t3MarkUp-discPrice;
						if (activitiesResultRequestBean.getTotalAgencyMarkUp() > 0)
							totalAgencyMarkup = activitiesResultRequestBean.getTotalAgencyMarkUp();

						if (activitiesResultRequestBean.getMarkUpType() == 1)
						{
							totalAgencyMarkup = (totalPrice -discPrice) * (totalAgencyMarkup / 100);
							grossPrice = totalPrice + totalAgencyMarkup - discPrice;
						}
						else
							grossPrice = totalPrice + totalAgencyMarkup - discPrice;

						/*if(Double.valueOf(currConvertRate).equals(0.0))
							currConvertRate = 1;*/

						activitiesJsonObj.put("t3Markup",t3MarkUp);
						activitiesJsonObj.put("grossPrice",grossPrice);
						activitiesJsonObj.put("totalAgencyMarkup",totalAgencyMarkup);
						activitiesJsonObj.put("serviceChargePrice", serviceChargePrice);
						activitiesJsonObj.put("discountPrice", discPrice);
						activitiesJsonObj.put("priceWithoutServiceCharge", priceWithoutServiceCharge);
						activitiesJsonObj.put("priceWithT3Markup", priceWithT3Markup);
						activitiesJsonObj.put("totalAmtToPay", totalAmtToPay);
						activitiesJsonObj.put("supplierPrice", orgFare);


						if(discPrice>=grossPrice || discPrice>=priceWithoutServiceCharge)
							activityJsonArr.remove(i);
					}
					jsonString = filteredJsonArr.toString();
					RedisCacheUtil.setResponseInCacheActivity(activitySearchKey, jsonString, redisService);
					endingTime = System.currentTimeMillis();
					totalTime = (endingTime - startTime) / 1000;
					totalTimeInMillis = endingTime - startingTime;
					CallLog.info(103, "TOTAL TIME TO APPLY RULE ON CACHE RESPONSE ::: " + totalTime
							+ " SECONDS || TIME(millisecs)::" + totalTimeInMillis + " milliseconds");
				} 
				else 
				{
					/*if (resultsReqBean.getTotalAgencyMarkUp() > 0)
					    totalAgencyMarkup = resultsReqBean.getTotalAgencyMarkUp();*/

					JSONObject activityJsonObj = new JSONObject(jsonString);
					JSONArray activityJsonArr =activityJsonObj.getJSONArray("activitiesList");
					for(int i=0;i<activityJsonArr.length();i++)
					{
						JSONObject activitiesJsonObj = activityJsonArr.getJSONObject(i);

						double orgFare = activitiesJsonObj.optDouble("price");
						double discPrice = activitiesJsonObj.getDouble("discountPrice");
						double t3MarkUp=activitiesJsonObj.getDouble("t3Markup");
						double serviceChargePrice=activitiesJsonObj.getDouble("serviceChargePrice");
						totalAmtToPay = orgFare + t3MarkUp + serviceChargePrice - discPrice;

						totalPrice=orgFare+activitiesJsonObj.getDouble("serviceChargePrice")+activitiesJsonObj.getDouble("t3Markup");

						priceWithT3Markup=orgFare + t3MarkUp ;
						priceWithoutServiceCharge=orgFare + t3MarkUp-discPrice;
						if (activitiesResultRequestBean.getTotalAgencyMarkUp() > 0)
							totalAgencyMarkup = activitiesResultRequestBean.getTotalAgencyMarkUp();

						if (activitiesResultRequestBean.getMarkUpType() == 1)
						{
							totalAgencyMarkup = (totalPrice -discPrice) * (totalAgencyMarkup / 100);
							grossPrice = totalPrice + totalAgencyMarkup - discPrice;
						}
						else
							grossPrice = totalPrice + totalAgencyMarkup - discPrice;

						/*if(Double.valueOf(currConvertRate).equals(0.0))
							currConvertRate = 1;*/
						
						activitiesJsonObj.put("t3Markup",t3MarkUp);
						activitiesJsonObj.put("grossPrice",grossPrice);
						activitiesJsonObj.put("totalAgencyMarkup",totalAgencyMarkup);
						activitiesJsonObj.put("serviceChargePrice", serviceChargePrice);
						activitiesJsonObj.put("discountPrice", discPrice);
						activitiesJsonObj.put("priceWithoutServiceCharge", priceWithoutServiceCharge);
						activitiesJsonObj.put("priceWithT3Markup", priceWithT3Markup);
						activitiesJsonObj.put("totalAmtToPay", totalAmtToPay);
						activitiesJsonObj.put("supplierPrice", orgFare);
						
					}
					jsonString = activityJsonObj.toString();
				}
			} else
				jsonString = "No Results";
		} else
			jsonString = "No Results";

		return jsonString;
	}

	public String setTenFilteredResult(com.tt.ws.rest.Activities.model.ActivitiesResultRequestBean activitiesResultRequestBean,String agencyId,String branchId,RuleSimulationService ruleSimulationService) 
	{
		String jsonString;
		long startingTime = 0;
		long endingTime = 0;
		long totalTime = 0;
		long totalTimeInMillis = 0;
		serviceRequestBean = new ServiceRequestBean();
		try 
		{
			ActivitiesWidget activitiesWidget= activitiesResultRequestBean.getActivitiesWidget();
			ActivitiesSearchRequestBean activitiesReqBean=activitiesResultRequestBean.getSearchRequestBean();
			activitiesReqBean = ActivitiesUtil.keyForTenFilteredResults(activitiesReqBean, agencyId);
			String activitiesSearchKey = activitiesReqBean.getActivitiesSearchKey();
			CallLog.info(1, "activitiesSearchKey for setFilteredResult Call === " + activitiesSearchKey);
			Long startTime = System.currentTimeMillis();
			RedisCacheUtil.setResponseInCacheActivity(activitiesSearchKey, activitiesResultRequestBean.getTenFilteredResult(), redisService);
			endingTime = System.currentTimeMillis();
			totalTime = (endingTime - startTime) / 1000;
			totalTimeInMillis = endingTime - startingTime;
			CallLog.info(103, "TOTAL TIME TO SET FILTERED ACTIVITIES RESULT RESPONSE IN CACHE ::: " + totalTime
					+ " SECONDS || TIME(millisecs)::" + totalTimeInMillis + " milliseconds");
			Long endTime = System.currentTimeMillis();
			Long timeTaken = endTime - startTime;
			CallLog.info(103,"TOTAL RESPONSE TIME(SECS)----->>>" + timeTaken / 1000 + " || TIME(MILLISECS)::" + timeTaken);
			jsonString = "success";
		} 
		catch (Exception e) {
			jsonString = "failure";
			CallLog.printStackTrace(1, e);
		}

		return jsonString;
	}

	///////////////////Set Filtered Result Ends//////////////////////////////////////////	

	public String setFilteredResult(ActivitiesResultRequestBean activitiesResultRequestBean,String agencyId,String branchId,RuleSimulationService ruleSimulationService) 
	{
		String jsonString;
		long startingTime = 0;
		long endingTime = 0;
		long totalTime = 0;
		long totalTimeInMillis = 0;	
		serviceRequestBean = new ServiceRequestBean();
		try 
		{
			ActivitiesWidget activitiesWidget= activitiesResultRequestBean.getActivitiesWidget();
			ActivitiesSearchRequestBean activitiesReqBean=activitiesResultRequestBean.getSearchRequestBean();
			activitiesReqBean = ActivitiesUtil.keyForFilteredResults(activitiesReqBean,agencyId);
			String activitySearchKey = activitiesReqBean.getActivitiesSearchKey();
			CallLog.info(1, "hotelSearchKey for setFilteredResult Call === " + activitySearchKey);
			Long startTime = System.currentTimeMillis();
			RedisCacheUtil.setResponseInCacheActivity(activitySearchKey, activitiesResultRequestBean.getAllFiltersResult(),redisService);
			endingTime = System.currentTimeMillis();
			totalTime = (endingTime-startTime)/1000;
			totalTimeInMillis = endingTime-startingTime;
			CallLog.info(103, "TOTAL TIME TO SET FILTERED ACTIVITY RESULT RESPONSE IN CACHE ::: "+totalTime +" SECONDS || TIME(millisecs)::" +totalTimeInMillis+" milliseconds");
			Long endTime = System.currentTimeMillis();
			Long timeTaken = endTime - startTime;
			CallLog.info(103, "TOTAL RESPONSE TIME(SECS)----->>>" + timeTaken/1000+" || TIME(MILLISECS)::"+timeTaken);
			jsonString = "success";
		} 
		catch (Exception e) {
			jsonString = "failure";
			CallLog.printStackTrace(1, e);
		}

		return jsonString;
	}
	
	public static ActivitiesResultRequestBean fetchAgencyMarkup(ActivitiesResultRequestBean activitiesResultBean, AgentService agentServices,UserSessionBean userSessionBean)
	{
		
		AgencyMarkup agencyMarkup = activitiesResultBean.getAgencyMarkup();
		AgencyMarkupModel agencyMarkupModel = new AgencyMarkupModel();
		agencyMarkupModel.setStatus(1);
		agencyMarkupModel.setProductRefId(1);
		int corporateId = agencyMarkup.getCorporateId();
		String agencyType = agencyMarkup.getPassengerType();
		
		if (null != agencyType && !agencyType.isEmpty() && ("c-passenger").equalsIgnoreCase(agencyType))
		{
			List<String> passengerIdList = agencyMarkup.getPassengerIdList();
			if (passengerIdList != null && !passengerIdList.isEmpty())
			{
				boolean condition = passengerIdList.get(0).split("-")[1] != null && !("").equalsIgnoreCase(passengerIdList.get(0).split("-")[1]);
				corporateId = condition ? Integer.parseInt(passengerIdList.get(0).split("-")[1]) : 0;
			}
		}
		else if (agencyType!=null && ("corporate").equalsIgnoreCase(agencyType))
		{
			List<String> corporateList = agencyMarkup.getCorporateIdList();
			corporateId = corporateList != null && corporateList.get(0) != null ? Integer.parseInt(corporateList.get(0)) : 0;
		}
		
		agencyMarkup.setAgencyId(Integer.parseInt(userSessionBean.getAgentId()));
		agencyMarkupModel.setCorporateId(corporateId);
		agencyMarkupModel.setDomOrInternational(Integer.valueOf(agencyMarkup.getDomInt()));
		agencyMarkupModel.setStatus(agencyMarkup.getStatus());
		
		agencyMarkupModel.setProductRefId(agencyMarkup.getProductRefId());
		agencyMarkupModel.setAgencyId(Integer.parseInt(userSessionBean.getAgentId()));
		com.tt.ts.common.modal.ResultBean resultBean = agentServices.fetchAgencyMarkup(agencyMarkupModel);
		if(null!=resultBean.getResultList() && !resultBean.getResultList().isEmpty())
		{
		  Object[] markUpArr =  (Object [])resultBean.getResultList().get(0);
		  activitiesResultBean.setMarkUpType(Integer.parseInt(String.valueOf(markUpArr[1])));
		  activitiesResultBean.setTotalAgencyMarkUp(Integer.parseInt(String.valueOf(markUpArr[2])));
		}
		return activitiesResultBean;
	}

}
