package com.tt.ts.rest.register.service;

import java.util.Date;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.tt.nc.common.util.TTLog;
import com.tt.ts.rest.common.util.ErrorCodeConstant;
import com.tt.ts.common.modal.ResultBean;
import com.tt.ts.rest.register.manager.RegisterManager;
import com.tt.ts.rest.register.model.RegisterModel;

@Service
public class RegisterService {
	
	@Autowired
	private RegisterManager registerManager;
	
	public ResultBean registerUser(RegisterModel registerModal) {
		ResultBean resultBean =new ResultBean();
		try {
			if(registerModal!=null ) 
				registerModal.setCreationTime(new Date());
				registerModal.setUpdatedTime(new Date());
				registerManager.registerUser(registerModal);
			
		} catch (Exception e)
		{
			resultBean.setIserror(true);
			String errorCode = ErrorCodeConstant.DATA_NOT_FOUND_KC;
			String errorMessage = ResourceBundle.getBundle("ApplicationResources", LocaleContextHolder.getLocale()).getString(errorCode);
			resultBean.setErrorCode(errorCode);
			resultBean.setErrorMessage(errorMessage);
			TTLog.printStackTrace(0, errorCode, e);
		}
		return resultBean;
	}
}
