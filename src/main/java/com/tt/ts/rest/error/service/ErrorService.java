package com.tt.ts.rest.error.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.tt.nc.common.util.TTLog;
import com.tt.ts.common.modal.ResultBean;
import com.tt.ts.rest.common.util.CommonUtil;
import com.tt.ts.rest.error.manager.ErrorManager;
import com.tt.ts.rest.error.modal.ErrorCodeModal;
import com.tt.ts.rest.error.util.ErrorCodeContant;

@Service
public class ErrorService
{
	private static final String APPLICATIONRESOURCE = "ApplicationResources";
	
	
	@Autowired
	private ErrorManager errorManager;
	
	private static final  String  PRODUCT_NAME ="Hotel"; 
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ResultBean saveUpdateErroCode(String errString)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			CommonUtil errCommonUtil =new CommonUtil<>();
			JSONArray jsonArray = new JSONArray(errString);
			List<ErrorCodeModal> errorModal = new CopyOnWriteArrayList<>();
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject obj = jsonArray.getJSONObject(i);
				String product = obj.getString("product");
				if(product!=null && !product.equalsIgnoreCase(PRODUCT_NAME))
				{
					obj.remove("action");
					obj.remove("serviceName");
				}
				ErrorCodeModal errorCodeModal = (ErrorCodeModal) errCommonUtil.convertJSONIntoObject(obj.toString(), ErrorCodeModal.class);
				errorModal.add(errorCodeModal);
			}
			if(!errorModal.isEmpty())
			{
				List<ErrorCodeModal> errorListFinal =new ArrayList<>();
				for (ErrorCodeModal errorCodeModal : errorModal)
				{
					List<ErrorCodeModal> errorCodeList = errorManager.checkExistanceAndFetch(errorCodeModal);
					if(errorCodeList!=null && !errorCodeList.isEmpty())
					{
						errorListFinal.add(errorCodeList.get(0));
					}
					else
					{
						errorCodeModal.setStatus(0);
						errorCodeModal.setCreatedBy(1);
						errorCodeModal.setCreatedDateTime(new Date());
						errorCodeModal.setUpdatedDateTime(new Date());
						errorCodeModal.setUpdatedBy(0);
						errorCodeModal.setDisplayMessage(errorCodeModal.getErrorDesc());
						errorManager.saveErrorCode(errorCodeModal);
						errorListFinal.add(errorCodeModal);
					}
				}
				resultBean.setResultList(errorListFinal);
			}
			
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			String errorCode = ErrorCodeContant.DATA_NOT_SAVE_ERROR_CODE;
			String errorMessage = ResourceBundle.getBundle(APPLICATIONRESOURCE, LocaleContextHolder.getLocale()).getString(errorCode);
			resultBean.setErrorCode(errorCode);
			resultBean.setErrorMessage(errorMessage);
			TTLog.printStackTrace(15, errorCode, e);
		}
		return resultBean;
	
	}

}
