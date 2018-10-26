package com.tt.ws.rest.payment.service;

import org.springframework.stereotype.Service;

import com.tt.satguruportal.util.common.TTPortalLog;
import com.tt.ts.common.modal.ResultBean;
import com.ws.interaction.beans.ServiceRequestBean;
import com.ws.interaction.beans.ServiceResponseBean;
import com.ws.interaction.factory.ServiceResolverFactory;
import com.ws.payment.bean.PaymentRequest;
import com.ws.payment.bean.PaymentResponse;
import com.ws.services.flight.config.ServiceConfigModel;
import com.ws.services.util.CallLog;
import com.ws.services.util.PaymentUtil;
import com.ws.services.util.ProductEnum;
import com.ws.services.util.ProductServiceEnum;

@Service
public class PaymentRestService
{

	@SuppressWarnings("static-access")
	public ResultBean processDoPayment(PaymentRequest paymentRequestBean)
	{
		ResultBean resultBean =new ResultBean();
		try
		{
			    CallLog.info(15, "value of paymentGatewayId = "+paymentRequestBean.getPaymentGatewayId());
		        CallLog.info(15, "value of Amount To Pay = "+paymentRequestBean.getPgPaymentAmount());
		        CallLog.info(15, "Booking Reference = "+paymentRequestBean.getBookingReference());
		        paymentRequestBean.setCurrency(paymentRequestBean.getCurrency());
		        paymentRequestBean.setEnvironment(System.getProperty("spring.profiles.active"));
		        //paymentRequestBean.setBookingReference(paymentRequestBean.getBookingReference());
		        paymentRequestBean.setOrderId(paymentRequestBean.getOrderId());
		        ServiceRequestBean paymentServiceRequestBean = new ServiceRequestBean();
		        paymentServiceRequestBean.setProductType(ProductServiceEnum.Payment.toString());
		        paymentServiceRequestBean.setServiceName(ProductEnum.Payment.doCardPayment.toString());
		        ServiceConfigModel servicePaymentConfigModel = new PaymentUtil(paymentRequestBean.getEnvironment()).setPaymentServiceConfigModel(paymentRequestBean);
		        paymentServiceRequestBean.setServiceConfigModel(servicePaymentConfigModel);
		        paymentServiceRequestBean.setRequestBean(paymentRequestBean);    
		        
		        ServiceResolverFactory serviceResolveFactory = new ServiceResolverFactory(paymentServiceRequestBean);
		        ServiceResponseBean servicePaymentResponseBean = serviceResolveFactory.getServiceResponse();
		        
		        if (servicePaymentResponseBean != null) {
		            PaymentResponse paymentResponseBean = (PaymentResponse) servicePaymentResponseBean.getResponseBean();
		            CallLog.info(15, "paymentResponseBean = "+paymentResponseBean);
		            paymentResponseBean.setPaymentRequest(paymentRequestBean);
		            resultBean.setResultObject(paymentResponseBean);
		            resultBean.setIserror(false);
		        }
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			TTPortalLog.printStackTrace(113, e);
		}
		return resultBean;
	}
	
	@SuppressWarnings("static-access")
	public ResultBean processDoRefund(PaymentRequest paymentRequestBean)
	{
		ResultBean resultBean =new ResultBean();
		try
		{
			    CallLog.info(15, "value of paymentGatewayId = "+paymentRequestBean.getPaymentGatewayId());
		        CallLog.info(15, "value of Amount To Pay = "+paymentRequestBean.getPgPaymentAmount());
		        CallLog.info(15, "Booking Reference = "+paymentRequestBean.getBookingReference());
		        paymentRequestBean.setCurrency(paymentRequestBean.getCurrency());
		        paymentRequestBean.setEnvironment(System.getProperty("spring.profiles.active"));
		        //paymentRequestBean.setBookingReference(paymentRequestBean.getBookingReference());
		        paymentRequestBean.setOrderId(paymentRequestBean.getOrderId());
		        ServiceRequestBean paymentServiceRequestBean = new ServiceRequestBean();
		        paymentServiceRequestBean.setProductType(ProductServiceEnum.Refund.toString());
		        paymentServiceRequestBean.setServiceName(ProductEnum.Payment.doRefund.toString());
		        ServiceConfigModel servicePaymentConfigModel = new PaymentUtil(paymentRequestBean.getEnvironment()).setPaymentServiceConfigModel(paymentRequestBean);
		        paymentServiceRequestBean.setServiceConfigModel(servicePaymentConfigModel);
		        paymentServiceRequestBean.setRequestBean(paymentRequestBean);    
		        
		        ServiceResolverFactory serviceResolveFactory = new ServiceResolverFactory(paymentServiceRequestBean);
		        ServiceResponseBean servicePaymentResponseBean = serviceResolveFactory.getRefundServiceResponse();
		        
		        if (servicePaymentResponseBean != null) {
		            PaymentResponse paymentResponseBean = (PaymentResponse) servicePaymentResponseBean.getResponseBean();
		            CallLog.info(15, "paymentResponseBean = "+paymentResponseBean);
		            paymentResponseBean.setPaymentRequest(paymentRequestBean);
		            resultBean.setResultObject(paymentResponseBean);
		            resultBean.setIserror(false);
		        }
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			TTPortalLog.printStackTrace(113, e);
		}
		return resultBean;
	}

	@SuppressWarnings("static-access")
	public ResultBean processDoFraudCheck(PaymentRequest paymentRequestBean)
	{
		ResultBean resultBean =new ResultBean();
		String log="[processDoFraudCheck] ";
		try
		{
			    CallLog.info(15,log+ "value of paymentGatewayId = "+paymentRequestBean.getPaymentGatewayId());
		        CallLog.info(15,log+ "value of Amount To verify for fraud check = "+paymentRequestBean.getFraudCheckAmount());
		        CallLog.info(15,log+ "Booking Reference = "+paymentRequestBean.getBookingReference());
		        paymentRequestBean.setCurrency(paymentRequestBean.getCurrency());
		        paymentRequestBean.setEnvironment(System.getProperty("spring.profiles.active"));
		        //paymentRequestBean.setBookingReference(paymentRequestBean.getBookingReference());
		        paymentRequestBean.setOrderId(paymentRequestBean.getOrderId());
		        ServiceRequestBean paymentServiceRequestBean = new ServiceRequestBean();
		        paymentServiceRequestBean.setProductType(ProductServiceEnum.FraudCheck.toString());
		        paymentServiceRequestBean.setServiceName(ProductEnum.Payment.authenticateCard.toString());
		        ServiceConfigModel servicePaymentConfigModel = new PaymentUtil(paymentRequestBean.getEnvironment()).setPaymentServiceConfigModel(paymentRequestBean);
		        paymentServiceRequestBean.setServiceConfigModel(servicePaymentConfigModel);
		        paymentServiceRequestBean.setRequestBean(paymentRequestBean);    
		        
		        ServiceResolverFactory serviceResolveFactory = new ServiceResolverFactory(paymentServiceRequestBean);
		        ServiceResponseBean servicePaymentResponseBean = serviceResolveFactory.getRefundServiceResponse();
		        
		        if (servicePaymentResponseBean != null) {
		            PaymentResponse paymentResponseBean = (PaymentResponse) servicePaymentResponseBean.getResponseBean();
		            CallLog.info(15,log+ "paymentResponseBean = "+paymentResponseBean);
		            paymentResponseBean.setPaymentRequest(paymentRequestBean);
		            resultBean.setResultObject(paymentResponseBean);
		            resultBean.setIserror(false);
		        }
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			TTPortalLog.printStackTrace(113, e);
		}
		return resultBean;
	}
}
