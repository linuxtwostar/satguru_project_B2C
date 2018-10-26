package com.tt.ts.rest.error.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.test.model.ServiceResponse;
import com.tt.ts.common.modal.ResultBean;
import com.tt.ts.rest.common.util.CommonUtil;
import com.tt.ts.rest.error.service.ErrorService;

@RestController
@RequestMapping("/error")
public class ErrorController
{
	@Autowired
	private ErrorService errorServcie;
	
	@RequestMapping(value = "/createUpdateError", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String createUpdateError(@RequestBody String errString)
	{
		ServiceResponse response = new ServiceResponse();
		if (errString!= null)
		{
			ResultBean resultBean = errorServcie.saveUpdateErroCode(errString);
			response.setResultList(resultBean.getResultList());
			if (resultBean.isError())
			{
				response.setResponseStatus("Failed");
				response.setErrorMsg(resultBean.getResultString());
			}
		}
		return CommonUtil.convertIntoJson(response);
	}
}
