package com.tt.ts.rest.quotation.manager;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.tt.ts.rest.quotation.dao.QuotationDao;
import com.tt.ts.rest.quotation.dao.QuotationDaoImpl;
import com.tt.ts.rest.quotation.model.QuotationModel;

@Repository
@Component(value = "QuotationManager")
public class QuotationManager {

	private QuotationDao quotationDao;

	public QuotationManager() {
		quotationDao = (QuotationDao) new QuotationDaoImpl();
	}
	
	public QuotationModel saveUpdateQuotation(QuotationModel model) throws Exception{
		return quotationDao.saveUpdateQuotation(model);
	}
	
	public List<QuotationModel> fetchQuotationByQuoteNo(Integer quotationId) throws Exception {
		return quotationDao.fetchQuotationByQuoteNo(quotationId);
	}
	
	public List<QuotationModel> fetchQuoteByUserId(Integer userId) throws Exception {
		return quotationDao.fetchQuoteByUserId(userId);
	}
	
	public List<QuotationModel> fetchQuoteByUserIdAndDateRange(Integer userId,Date startDate,Date endDate) throws Exception {
		return quotationDao.fetchQuoteByUserIdAndDateRange(userId,startDate,endDate);
	}
}
