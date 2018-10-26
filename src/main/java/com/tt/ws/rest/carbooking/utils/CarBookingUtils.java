package com.tt.ws.rest.carbooking.utils;

import java.util.ArrayList;
import java.util.List;

import com.ws.services.flight.config.CredentialConfigModel;
import com.ws.services.flight.config.ServiceConfigModel;
import com.ws.services.flight.config.SupplierConfigModal;

public class CarBookingUtils {

	public static ServiceConfigModel setServiceConfigModel()
	{
		ServiceConfigModel serviceConfigModel=new ServiceConfigModel();
        List <CredentialConfigModel> credentialActivities=new ArrayList<CredentialConfigModel>();
        List <SupplierConfigModal> supplierConfigModelL=new ArrayList<SupplierConfigModal>();
        CredentialConfigModel credentialsC=new CredentialConfigModel();
        SupplierConfigModal supplierConfigModel = new SupplierConfigModal();
		credentialsC.setCredentialName("CarBookingRequestUrl");
		credentialsC.setCredentialValue("https://secure.rentalcars.com/service/ServiceRequest.do");


		//credentialsC.setCredentialValue("https://api.test.hotelbeds.com/activity-content-api/3.0/");
	


		credentialActivities.add(credentialsC);
		
		//credentialsC.setCredentialName("ActivitiesBookingtUrl");
		//credentialsC.setCredentialValue("https://api.test.hotelbeds.com/activity-api/3.0");

		credentialActivities.add(credentialsC);
		
		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("username");
		credentialsC.setCredentialValue("satgurutravel");
		credentialActivities.add(credentialsC);
		
		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("password");  //sha256Hex : x-signature 
		credentialsC.setCredentialValue("satgurutravel2710"); //FfHJgtBbkQ
		credentialActivities.add(credentialsC);
		
		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("remoteip");  
		credentialsC.setCredentialValue("91.151.7.6");
		credentialActivities.add(credentialsC);
		
		supplierConfigModel.setSupplierName("RentalCars");
		supplierConfigModelL.add(supplierConfigModel);

        serviceConfigModel.setProductName("CarBooking");
		supplierConfigModel.setCredential(credentialActivities);
		serviceConfigModel.setSupplier(supplierConfigModelL);
		return serviceConfigModel;
		
	}
}
