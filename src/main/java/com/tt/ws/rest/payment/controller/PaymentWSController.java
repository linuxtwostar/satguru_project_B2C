package com.tt.ws.rest.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tt.ts.common.modal.ResultBean;
import com.tt.ts.rest.common.util.CommonUtil;
import com.tt.ws.rest.payment.service.PaymentRestService;
import com.ws.payment.bean.PaymentRequest;

@RestController
@RequestMapping("/payment")
public class PaymentWSController
{
	@Autowired
	PaymentRestService paymentRestService;
	
	 @RequestMapping(value = "/doPGPayment", method = RequestMethod.POST)
	    public String doPGPaymentForFlight(@RequestBody PaymentRequest paymentRequestBean) {
	       
	       ResultBean resultBean= paymentRestService.processDoPayment(paymentRequestBean);
	        return CommonUtil.convertIntoJson(resultBean.getResultObject());
	    }
	    
}
