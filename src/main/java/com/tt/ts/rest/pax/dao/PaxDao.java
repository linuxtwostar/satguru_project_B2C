package com.tt.ts.rest.pax.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.tt.ts.rest.corporate.model.CorporateModel;
import com.tt.ts.rest.pax.model.PaxAirlinePrefernce;
import com.tt.ts.rest.pax.model.PaxDocumentModel;
import com.tt.ts.rest.pax.model.PaxFrequentFlyer;
import com.tt.ts.rest.pax.model.PaxModel;
import com.tt.ts.rest.pax.model.PaxRelationModel;

@Repository
public interface PaxDao {

    public PaxModel saveUpdatePax(PaxModel paxModal) throws Exception;
    public List<PaxAirlinePrefernce> fetchAirlinePrefDetails(Integer paxId) throws Exception;
    public List<PaxFrequentFlyer> fetchPaxFrequentFlyrDetails(Integer paxId) throws Exception;
    public List<PaxDocumentModel> fetchPaxDocumentDetails(Integer paxId) throws Exception;
    public List<PaxModel> searchPaxList(PaxModel paxModal) throws Exception;
    public List<Object> searchPaxListCount(PaxModel paxModal) throws Exception;
    public List<PaxModel> paxSearchResultByNameMob(PaxModel paxModal) throws Exception; 
    public List<PaxModel> searchColleagueList(PaxModel paxModal) throws Exception;
    public List<PaxRelationModel> searchPaxRelationList(Integer paxId) throws Exception;
    public int savePaxRelation(PaxRelationModel paxRelationModel) throws Exception;
    public void deleteChildTableListItem(List<String> hqlList, List<Object> parameterList) throws Exception;
    public void deleteChildTableListItem(List<String> hqlList, List<Object> paramList, Session session, Transaction tx) throws Exception;
    public PaxModel updatePaxDetail(PaxModel paxModal) throws Exception;
	public List<PaxModel> paxValidation(PaxModel paxModal) throws Exception;
	public List<PaxModel> hotelPaxValidation(PaxModel paxModal) throws Exception;
	void updatePaxDetails(PaxModel paxModal) throws Exception;
	//add by amit	
		 public List<Object> groupId(int id) throws Exception;
		 public boolean checkDuplicate(String branchId, String firstName, String email) throws Exception;
		 List<PaxModel> searchCorporateListByName(PaxModel paxModel) throws Exception;
		 void savePaxDocumentData(PaxDocumentModel paxDocument) throws Exception;
		 void deletePaxDocuments(Integer paxId) throws Exception;
}
