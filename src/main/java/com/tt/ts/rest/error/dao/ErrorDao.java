package com.tt.ts.rest.error.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tt.ts.rest.error.modal.ErrorCodeModal;

@Repository
public interface ErrorDao
{

	List<ErrorCodeModal> checkExistanceAndFetch(ErrorCodeModal errorCodeModal)  throws Exception;

	int saveErrorCode(ErrorCodeModal errorCodeContant) throws Exception;

}
