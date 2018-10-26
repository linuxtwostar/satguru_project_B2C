package com.tt.ts.rest.corporate.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tt.ts.rest.corporate.dao.CorporateDao;
import com.tt.ts.rest.corporate.model.CorporateFinanceContact;
import com.tt.ts.rest.corporate.model.CorporateModel;
import com.tt.ts.rest.corporate.model.CorporateTravelCordinator;

@Component(value = "CorporateManager")
public class CorporateManager {

    @Autowired
    private CorporateDao corporateDao;

    public CorporateModel saveCorporate(CorporateModel corporateModel) throws Exception {
	return corporateDao.saveCorporate(corporateModel);
    }

    public CorporateModel updateCorporate(CorporateModel corporateModel) throws Exception {
	return corporateDao.updateCorporate(corporateModel);
    }

    public List<CorporateTravelCordinator> fetchTravelCrdntDetails(Integer corpId) throws Exception {
	return corporateDao.fetchTravelCrdntDetails(corpId);
    }

    public List<CorporateFinanceContact> fetchFinanceContactDetails(Integer corpId) throws Exception {
	return corporateDao.fetchFinanceContactDetails(corpId);
    }

    public List<CorporateModel> searchCorporateList(CorporateModel corporateModel) throws Exception {
	return corporateDao.searchCorporateList(corporateModel);
    }

    public List<CorporateModel> searchCorporateListByName(CorporateModel corporateModel) throws Exception {
	return corporateDao.searchCorporateListByName(corporateModel);
    }

    public List<CorporateModel> getCorporateListByAgencyId(Integer groupId) throws Exception {
	return corporateDao.getCorporateListByAgencyId(groupId);
    }

    public CorporateModel saveUpdateCorporate(CorporateModel corporateModal) throws Exception {
	return corporateDao.saveUpdateCorporate(corporateModal);
    }
    
    public List<Object> getCurrencyRate(String fromCurrency, String toCurrency) throws Exception
	{
		return corporateDao.getCurrencyRate(fromCurrency,toCurrency);
	}
    
    public List<Object> getCurrencyId(String currencyCode) throws Exception
	{
		return corporateDao.getCurrencyId(currencyCode);
	}
	//add by amit
	public List<Object> groupId(int id) throws Exception
	{
		return corporateDao.groupId(id);
	}
	public boolean checkDuplicate(String branchId, String firstName, String email) throws Exception
	{
		return corporateDao.checkDuplicate(branchId, firstName, email);
	}
	
	public List<CorporateModel> fetCompanyName(String corpId) throws Exception {
		return corporateDao.fetCompanyName(corpId);
	    }
	//Added By Pramod for SAT-14018 on 08-08-2018
	public List<CorporateModel> searchCorporateListByLoggedInUser(CorporateModel corporateModel) throws Exception {
		return corporateDao.searchCorporateListByLoggedInUser(corporateModel);
	}
}
