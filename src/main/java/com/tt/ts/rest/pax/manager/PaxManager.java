package com.tt.ts.rest.pax.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tt.ts.rest.corporate.model.CorporateModel;
import com.tt.ts.rest.pax.dao.PaxDao;
import com.tt.ts.rest.pax.model.PaxAirlinePrefernce;
import com.tt.ts.rest.pax.model.PaxDocumentModel;
import com.tt.ts.rest.pax.model.PaxFrequentFlyer;
import com.tt.ts.rest.pax.model.PaxModel;
import com.tt.ts.rest.pax.model.PaxRelationModel;

@Component(value = "paxManager")
public class PaxManager {

    @Autowired
    private PaxDao paxDao;

    public PaxModel saveUpdatePax(PaxModel paxModal) throws Exception {
	return paxDao.saveUpdatePax(paxModal);
    }

    public List<PaxAirlinePrefernce> fetchAirlinePrefDetails(Integer paxId) throws Exception {
	return paxDao.fetchAirlinePrefDetails(paxId);
    }

    public List<PaxFrequentFlyer> fetchPaxFrequentFlyrDetails(Integer paxId) throws Exception {
	return paxDao.fetchPaxFrequentFlyrDetails(paxId);
    }

    public List<PaxDocumentModel> fetchPaxDocumentDetails(Integer paxId) throws Exception {
	return paxDao.fetchPaxDocumentDetails(paxId);
    }
    
    public List<Object> searchPaxListCount(PaxModel paxModal) throws Exception {
    	return paxDao.searchPaxListCount(paxModal);
     }

    public List<PaxModel> searchPaxList(PaxModel paxModal) throws Exception {
	return paxDao.searchPaxList(paxModal);
    }

    public List<PaxModel> paxSearchResultByNameMob(PaxModel paxModal) throws Exception {
	return paxDao.paxSearchResultByNameMob(paxModal);
    }

    public List<PaxModel> searchColleagueList(PaxModel paxModal) throws Exception {
	return paxDao.searchColleagueList(paxModal);
    }

    public List<PaxRelationModel> searchPaxRelationList(Integer paxId) throws Exception {
	return paxDao.searchPaxRelationList(paxId);
    }

    public int savePaxRelation(PaxRelationModel paxRelationModel) throws Exception {
	return paxDao.savePaxRelation(paxRelationModel);
    }

    public PaxModel updatePaxDetail(PaxModel paxModal) throws Exception {
	return paxDao.updatePaxDetail(paxModal);
    }

    public void deleteChildTableListItem(List<String> hqlList, List<Object> parameterList) throws Exception {
	paxDao.deleteChildTableListItem(hqlList, parameterList);
    }

	public List<PaxModel> paxValidation(PaxModel paxModal) throws Exception
	{
		return paxDao.paxValidation(paxModal);
	}
	public void updatePaxDetails(PaxModel paxModal) throws Exception
	{
		paxDao.updatePaxDetails(paxModal);
	}
	//add by amit
		public List<Object> groupId(int id) throws Exception
		{
			return paxDao.groupId(id);
		}
		public boolean checkDuplicate(String branchId, String firstName, String email) throws Exception
		{
			return paxDao.checkDuplicate(branchId, firstName, email);
		}
		 public List<PaxModel> searchCorporateListByName(PaxModel paxModel) throws Exception {
				return paxDao.searchCorporateListByName(paxModel);
			    }

	public List<PaxModel> hotelPaxValidation(PaxModel paxModal) throws Exception
	{
			return paxDao.hotelPaxValidation(paxModal);
	}
	//
	public void savePaxDocumentData(PaxDocumentModel paxDocument) throws Exception {
		paxDao.savePaxDocumentData(paxDocument);
	}
	public void deletePaxDocuments(Integer userId) throws Exception {
		paxDao.deletePaxDocuments(userId);
	}
}
