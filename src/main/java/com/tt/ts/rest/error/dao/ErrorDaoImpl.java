package com.tt.ts.rest.error.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.tt.nc.core.dao.GenericDAOImpl;
import com.tt.ts.rest.common.util.QueryConstantRest;
import com.tt.ts.rest.error.modal.ErrorCodeModal;

@Repository
public class ErrorDaoImpl extends GenericDAOImpl<Object, Long> implements ErrorDao 
{

	@Override
	public List<ErrorCodeModal> checkExistanceAndFetch(ErrorCodeModal errorCodeModal) throws Exception
	{
		List<Object> paramList =  new ArrayList<>();
		paramList.add(errorCodeModal.getErrorCode());
		paramList.add(errorCodeModal.getErrorDesc());
		paramList.add(errorCodeModal.getVendorName());
		String fetchError= QueryConstantRest.FETCH_ERROR_CODE;
		return fetchWithHQL(fetchError, paramList);
	}

	@Override
	public int saveErrorCode(ErrorCodeModal errorCodeContant) throws Exception
	{
		return (int) makePersistent(errorCodeContant);
	}

}
