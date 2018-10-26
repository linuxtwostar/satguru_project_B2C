package com.tt.ws.rest.insurance.util;

import org.codehaus.jackson.map.ObjectMapper;

import com.ws.interaction.beans.ServiceRequestBean;
import com.ws.interaction.beans.ServiceResponseBean;
import com.ws.interaction.factory.ServiceResolverFactory;
import com.ws.services.insurance.bean.product.ProductFamilyPriceResponseBean;
import com.ws.services.insurance.bean.product.ProductPriceRequestBean;
import com.ws.services.util.CallLog;
import com.ws.services.util.ProductEnum;

public class InsuranceHelperUtilRest {
	 public static String processToInsuranceSearchResponse(ServiceRequestBean serviceRequestBean, ProductPriceRequestBean requestBean) {
		 
		serviceRequestBean.setServiceName(ProductEnum.Insurance.price.toString());
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
	 
	 public static ProductFamilyPriceResponseBean processToInsuranceSearchResponseObject(ServiceRequestBean serviceRequestBean, ProductPriceRequestBean requestBean) {
		 
			serviceRequestBean.setServiceName(ProductEnum.Insurance.price.toString());
			serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
			ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
			
			ServiceResponseBean serviceResponseBean = serviceResolverFactory.getServiceResponse();
			ProductFamilyPriceResponseBean responseBean  = (ProductFamilyPriceResponseBean) serviceResponseBean.getResponseBean();
			
			return responseBean;
		 }
	 
	 public static ProductFamilyPriceResponseBean processToInsuranceSearchResponseObjectCrossSell(ServiceRequestBean serviceRequestBean, ProductPriceRequestBean requestBean) {
		 
			serviceRequestBean.setServiceName(ProductEnum.Insurance.pricecrosssell.toString());
			serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
			ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
			
			ServiceResponseBean serviceResponseBean = serviceResolverFactory.getServiceResponse();
			ProductFamilyPriceResponseBean responseBean  = (ProductFamilyPriceResponseBean) serviceResponseBean.getResponseBean();
			
			return responseBean;
		 }
}
