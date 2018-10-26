package com.tt.ts.rest.quotation.dao;

import java.util.Date;
import java.util.List;

import com.tt.ts.rest.quotation.model.QuotationModel;

public interface QuotationDao {
	QuotationModel saveUpdateQuotation(QuotationModel model) throws Exception;
	List<QuotationModel> fetchQuotationByQuoteNo(Integer quotationId) throws Exception;
	List<QuotationModel> fetchQuoteByUserId(Integer userId) throws Exception;
	List<QuotationModel> fetchQuoteByUserIdAndDateRange(Integer userId,Date startDate,Date endDate) throws Exception;
}
