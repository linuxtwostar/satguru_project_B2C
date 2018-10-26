package com.tt.ts.rest.error.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tt.ts.rest.error.dao.ErrorDao;
import com.tt.ts.rest.error.modal.ErrorCodeModal;

@Component
public class ErrorManager
{
	@Autowired
	private ErrorDao errorDao;
	
	public List<ErrorCodeModal> checkExistanceAndFetch(ErrorCodeModal errorCodeModal) throws Exception
	{
		return errorDao.checkExistanceAndFetch(errorCodeModal);
	}
	public int saveErrorCode(ErrorCodeModal errorCodeContant)  throws Exception
	{
		return errorDao.saveErrorCode(errorCodeContant);
	}

}
