package com.tt.ws.rest.Activities.utils;

import java.util.ArrayList;
import java.util.List;

import com.tt.satguruportal.activities.model.ActivitiesWidget;
import com.ws.interaction.beans.ServiceRequestBean;
import com.ws.services.activities.bean.ActivitiesSearchRequestBean;
import com.ws.services.flight.config.CredentialConfigModel;
import com.ws.services.flight.config.ServiceConfigModel;
import com.ws.services.flight.config.SupplierConfigModal;
import com.ws.services.hotel.bean.RoomRequestBean;
import com.ws.services.util.CallLog;

public class ActivitiesUtil {
	

	static final String HOTEL_CODE = "hotelCode";
	static final String PRICE = "price";
	static final String CITY_NAME = "cityName";
	static final String CITY_CODE = "cityCode";
	static final String COUNTRY_NAME = "countryName";
	RoomRequestBean roomRequestBean = null;
	ServiceRequestBean serviceRequestBean = null;
	private static String logStr = "[ActivitiesUtil]";
	
	
	public static ServiceConfigModel setServiceConfigModel()
	{
		ServiceConfigModel serviceConfigModel=new ServiceConfigModel();
        List <CredentialConfigModel> credentialActivities=new ArrayList<CredentialConfigModel>();
        List <SupplierConfigModal> supplierConfigModelL=new ArrayList<SupplierConfigModal>();
        CredentialConfigModel credentialsC=new CredentialConfigModel();
        SupplierConfigModal supplierConfigHotelBedsModel = new SupplierConfigModal();
		credentialsC.setCredentialName("ActivitiesRequestUrl");
		credentialsC.setCredentialValue("https://api.test.hotelbeds.com/activity-content-api/3.0/");
	
		credentialActivities.add(credentialsC);
		
		credentialsC.setCredentialName("ActivitiesBookingtUrl");
		credentialsC.setCredentialValue("https://api.test.hotelbeds.com/activity-api/3.0");

		credentialActivities.add(credentialsC);
		
		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("ActivitiesAPIKey");
		credentialsC.setCredentialValue("p5x2gffuw75sy2vk9d3b3fvp");
		credentialActivities.add(credentialsC);
		
		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("ActivitiesSecretKey");  //sha256Hex : x-signature 
		credentialsC.setCredentialValue("FfHJgtBbkQ"); //FfHJgtBbkQ
		credentialActivities.add(credentialsC);
		
		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("LanguageCode");  
		credentialsC.setCredentialValue("en");
		credentialActivities.add(credentialsC);
		
		supplierConfigHotelBedsModel.setSupplierName("HBDActivities");
		supplierConfigModelL.add(supplierConfigHotelBedsModel);

        serviceConfigModel.setProductName("Activities");
		supplierConfigHotelBedsModel.setCredential(credentialActivities);
		serviceConfigModel.setSupplier(supplierConfigModelL);
		return serviceConfigModel;
		
	}
	
	public static ActivitiesSearchRequestBean keyForTenFilteredResults(ActivitiesSearchRequestBean activitiesReqBean,String agencyid) {

		try 
		{
			StringBuilder searchKeyBuilder = new StringBuilder("");
			searchKeyBuilder.append("ACT_").append(activitiesReqBean.getFilter().get(0).getFilterValue().replaceAll("\\s", "") + "_" +   activitiesReqBean.getFromDate().replaceAll("\\s", "") + "_" +activitiesReqBean.getToDate() + "_" + agencyid + "_Filtered_10");
			String cacheKey = searchKeyBuilder.toString();
			if(null != cacheKey && !cacheKey.isEmpty())
				cacheKey = cacheKey.toUpperCase();
			activitiesReqBean.setActivitiesSearchKey(cacheKey);
			
		} catch (Exception e) {
			CallLog.printStackTrace(1, e);
		}
		return activitiesReqBean;
	}
	public static ActivitiesSearchRequestBean keyForFilteredResults(ActivitiesSearchRequestBean activitiesReqBean,String agencyid) {
		

		try 
		{
			StringBuilder searchKeyBuilder = new StringBuilder("");
			searchKeyBuilder.append("ACT_").append(activitiesReqBean.getFilter().get(0).getFilterValue().replaceAll("\\s", "") + "_" +  activitiesReqBean.getFromDate().replaceAll("\\s", "") + "_" + activitiesReqBean.getToDate() + "_" + agencyid + "_Filtered");
			
			String cacheKey = searchKeyBuilder.toString();
			if(null != cacheKey && !cacheKey.isEmpty())
				cacheKey = cacheKey.toUpperCase();
			activitiesReqBean.setActivitiesSearchKey(cacheKey);
			
		} catch (Exception e) {
			CallLog.printStackTrace(1, e);
		}
		return activitiesReqBean;
	}

	

}
