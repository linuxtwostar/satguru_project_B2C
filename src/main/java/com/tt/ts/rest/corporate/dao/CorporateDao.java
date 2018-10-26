package com.tt.ts.rest.corporate.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tt.ts.rest.corporate.model.CorporateFinanceContact;
import com.tt.ts.rest.corporate.model.CorporateModel;
import com.tt.ts.rest.corporate.model.CorporateTravelCordinator;

@Repository
public interface CorporateDao {

    CorporateModel saveCorporate(CorporateModel staff) throws Exception;

    CorporateModel updateCorporate(CorporateModel corporateModel) throws Exception;
    
    List<CorporateTravelCordinator> fetchTravelCrdntDetails(Integer corpId) throws Exception;

    List<CorporateFinanceContact> fetchFinanceContactDetails(Integer corpId) throws Exception;

    List<CorporateModel> searchCorporateList(CorporateModel corporateModel) throws Exception;
    
    List<CorporateModel> searchCorporateListByName(CorporateModel corporateModel) throws Exception;
    
    List<CorporateModel> getCorporateListByAgencyId(Integer groupId) throws Exception;
    
    CorporateModel saveUpdateCorporate(CorporateModel corporateModal) throws Exception;
    
    List<Object> getCurrencyRate(String fromCurrency,String toCurrecny) throws Exception;
    
    List<Object> getCurrencyId(String currencyCode) throws Exception;
    
  //add by amit	
	 public List<Object> groupId(int id) throws Exception;
	 
	 public boolean checkDuplicate(String branchId, String firstName, String email) throws Exception;
	 public List<CorporateModel> fetCompanyName(String corpId) throws Exception;
	 //Added By Pramod for SAT-14018 on 08-08-2018
	 List<CorporateModel> searchCorporateListByLoggedInUser(CorporateModel corporateModel) throws Exception;
}
