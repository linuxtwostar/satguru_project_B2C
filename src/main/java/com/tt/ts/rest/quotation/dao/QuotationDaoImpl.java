package com.tt.ts.rest.quotation.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tt.nc.core.dao.GenericDAOImpl;
import com.tt.ts.rest.common.util.QueryConstantRest;
import com.tt.ts.rest.quotation.model.QuotationModel;

public class QuotationDaoImpl extends GenericDAOImpl<Object, Long> implements QuotationDao {

    @Override
    public QuotationModel saveUpdateQuotation(QuotationModel model) throws Exception {
	return saveorupdate(model);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<QuotationModel> fetchQuotationByQuoteNo(Integer quotationId) throws Exception {
	String query = QueryConstantRest.FETCH_QUOTATION_BY_ID;
	List<Object> paraList = new ArrayList<Object>();
	paraList.add(quotationId);
	List<QuotationModel> quotationModelList = fetchWithHQL(query, paraList);
	return quotationModelList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<QuotationModel> fetchQuoteByUserId(Integer userId) throws Exception {
	String query = QueryConstantRest.FETCH_QUOTATION_BY_CLIENT_ID;
	List<Object> paraList = new ArrayList<Object>();
	paraList.add(userId);
	List<QuotationModel> quotationModelList = fetchWithHQL(query, paraList);
	return quotationModelList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<QuotationModel> fetchQuoteByUserIdAndDateRange(Integer userId, Date startDate, Date endDate) throws Exception {
	String query = QueryConstantRest.FETCH_QUOTATION_BY_CLIENTID_AND_DATE_RANGE;
	List<Object> paraList = new ArrayList<Object>();
	paraList.add(userId);
	paraList.add(startDate);
	paraList.add(endDate);
	List<QuotationModel> quotationModelList = fetchWithHQL(query, paraList);
	return quotationModelList;
    }

}
