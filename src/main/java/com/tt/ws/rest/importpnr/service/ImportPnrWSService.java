package com.tt.ws.rest.importpnr.service;

import org.springframework.stereotype.Service;

import com.tt.ts.rest.common.util.CommonUtil;
import com.ws.interaction.beans.ServiceRequestBean;
import com.ws.interaction.beans.ServiceResponseBean;
import com.ws.interaction.factory.ServiceResolverFactory;
import com.ws.services.flight.bean.booking.FlightBookingResponseBean;
import com.ws.services.flight.bean.importpnr.ImportPNRIssueTicketRequestBean;
import com.ws.services.flight.bean.importpnr.ImportPNRPricingRequestBean;
import com.ws.services.flight.bean.importpnr.ImportPNRPricingResponseBean;
import com.ws.services.flight.bean.importpnr.ImportPNRResponseBean;
import com.ws.services.flight.bean.importpnr.ImportPNRServiceRequestBean;
import com.ws.services.util.CallLog;
import com.ws.services.util.ProductEnum;
import com.ws.services.util.ProductServiceEnum;

@Service
public class ImportPnrWSService
{
	public ImportPNRResponseBean importPnrSearch(ImportPNRServiceRequestBean importPNRServiceRequestBean) {
		ServiceResponseBean serviceResponseBean;
		ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Flight.toString());
		serviceRequestBean.setServiceName(ProductEnum.Flight.importPNR.toString());

		serviceRequestBean.setRequestBean(importPNRServiceRequestBean);
		serviceRequestBean.setServiceConfigModel(importPNRServiceRequestBean.getServiceConfigModel());
		ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		/*
		 * //** Will return the bean containing corresponding service response
		 * bean, response status and error message
		 */		
		ImportPNRResponseBean responseBean = null;
		serviceResponseBean = serviceResolverFactory.getServiceResponse();
		if (serviceResponseBean.getResponseBean() != null) {
			responseBean = (ImportPNRResponseBean) serviceResponseBean.getResponseBean();
		}
		return responseBean;
	}
	
	public ImportPNRPricingResponseBean importPnrPrice(ImportPNRPricingRequestBean importPNRPricingRequestBean) {
		//String jsonString = null;
		ImportPNRPricingResponseBean responseBean = null;
		ServiceResponseBean serviceResponseBean;
		ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Flight.toString());
		serviceRequestBean.setServiceName(ProductEnum.Flight.importPNRPricing.toString());

		serviceRequestBean.setRequestBean(importPNRPricingRequestBean);
		serviceRequestBean.setServiceConfigModel(importPNRPricingRequestBean.getServiceConfigModel());
		ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();
		if (serviceResponseBean.getResponseBean() != null) {
			responseBean = (ImportPNRPricingResponseBean) serviceResponseBean.getResponseBean();
			/*if (responseBean != null) {
				jsonString = CommonUtil.convertIntoJson(responseBean);
			}*/
		}
		return responseBean;
	}
	
	
	public ImportPNRPricingResponseBean importPnrRecheckPrice(ImportPNRPricingRequestBean importPNRPricingRequestBean) {
		ImportPNRPricingResponseBean importPNRPricingResponseBean = null;
		ServiceResponseBean serviceResponseBean;
		ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setServiceName(ProductEnum.Flight.importPNRPricingAfterPayClick.toString());
		serviceRequestBean.setProductType(ProductServiceEnum.Flight.toString());
		serviceRequestBean.setRequestBean(importPNRPricingRequestBean);
		serviceRequestBean.setServiceConfigModel(importPNRPricingRequestBean.getServiceConfigModel());
		
		ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();
		
		importPNRPricingResponseBean = (ImportPNRPricingResponseBean)serviceResponseBean.getResponseBean();
		if (serviceResponseBean.getResponseBean() != null) {
			importPNRPricingResponseBean = (ImportPNRPricingResponseBean) serviceResponseBean.getResponseBean();
		}
		return importPNRPricingResponseBean;
	}
	
	
	public ImportPNRResponseBean syncPnr(ImportPNRServiceRequestBean importPNRServiceRequestBean) {
		ImportPNRResponseBean importPNRResponseBean = null;
		ServiceResponseBean serviceResponseBean;
		ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Flight.toString());
		serviceRequestBean.setServiceName(ProductEnum.Flight.syncPNR.toString());
		String jsonString = null;

		serviceRequestBean.setRequestBean(importPNRServiceRequestBean);
		serviceRequestBean.setServiceConfigModel(importPNRServiceRequestBean.getServiceConfigModel());
		ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();
		if (serviceResponseBean.getResponseBean() != null) {
			importPNRResponseBean = (ImportPNRResponseBean) serviceResponseBean.getResponseBean();
			if (importPNRResponseBean != null) {
				jsonString = CommonUtil.convertIntoJson(importPNRResponseBean);
			}
			
			importPNRResponseBean = (ImportPNRResponseBean)serviceResponseBean.getResponseBean();
		}
		return importPNRResponseBean;
	}
	
	//NIU
	public String bookingImportPnr(ImportPNRIssueTicketRequestBean importPNRIssueTicketRequestBean) {
		String jsonString = null;
		ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
		CallLog.info(0, ProductEnum.Flight.issueTicketImportPNR);
		serviceRequestBean.setProductType(ProductServiceEnum.Flight.toString());
		serviceRequestBean.setServiceName(ProductEnum.Flight.issueTicketImportPNR.toString());
		serviceRequestBean.setServiceConfigModel(importPNRIssueTicketRequestBean.getServiceConfigModel());
		serviceRequestBean.setRequestBean(importPNRIssueTicketRequestBean);
		ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResolverFactory.setServiceRequestBean(serviceRequestBean);
		ServiceResponseBean flightBookResponseBean = serviceResolverFactory.getServiceResponse();
		if (flightBookResponseBean != null) {
			FlightBookingResponseBean bookingResponseBean = (FlightBookingResponseBean) flightBookResponseBean.getResponseBean();
			jsonString = CommonUtil.convertIntoJson(bookingResponseBean);
		}
		return jsonString;
	}
}
