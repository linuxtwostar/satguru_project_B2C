package com.tt.ts.rest.corporate.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.tt.nc.core.dao.GenericDAOImpl;
import com.tt.ts.rest.common.util.QueryConstantRest;
import com.tt.ts.rest.corporate.model.CorporateFinanceContact;
import com.tt.ts.rest.corporate.model.CorporateModel;
import com.tt.ts.rest.corporate.model.CorporateTravelCordinator;
import com.tt.ts.rest.pax.model.PaxModel;

@Repository
public class CorporateDaoImpl extends GenericDAOImpl<Object, Long> implements CorporateDao {

    @Override
    public CorporateModel saveCorporate(CorporateModel corporateModel) throws Exception {

	Session session = getSessionFactory().openSession();
	Transaction tx = session.beginTransaction();
	try {
	    if (null != tx) {
		session.save(corporateModel);
		tx.commit();
	    }
	} catch (HibernateException e) {
	    if (null != tx)
		tx.rollback();
	    e.printStackTrace();
	    throw e;
	} finally {
	    session.flush();
	    session.close();
	}
	return corporateModel;
    }

    @Override
    public CorporateModel updateCorporate(CorporateModel corporateModal) throws Exception {
	Session session = getSessionFactory().openSession();
	Transaction tx = session.beginTransaction();
	try {
	    if (corporateModal != null && corporateModal.getCorpId() > 0) {
		List<String> queryList = new ArrayList<>();
		List<Object> paramList = new ArrayList<>();
		String corporateTrvlDeletion = QueryConstantRest.GET_DELETE_FOR_CORP_TRVL;
		if (corporateTrvlDeletion != null && !corporateTrvlDeletion.isEmpty()) {
		    queryList.add(corporateTrvlDeletion);
		}
		String corporateFinanceDeletion = QueryConstantRest.GET_DELETE_FOR_CORP_FINC;
		if (corporateFinanceDeletion != null && !corporateFinanceDeletion.isEmpty()) {
		    queryList.add(corporateFinanceDeletion);
		}
		paramList.add(corporateModal.getCorpId());
		deleteChildTableListItem(queryList, paramList, session, tx);
	    }
	    session.saveOrUpdate(corporateModal);
	    tx.commit();
	} catch (HibernateException e) {
	    if (null != tx) {
		tx.rollback();
	    }
	    throw e;
	} finally {
	    session.flush();
	    session.close();
	}
	return corporateModal;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<CorporateTravelCordinator> fetchTravelCrdntDetails(Integer corpId) throws Exception {
	List<CorporateTravelCordinator> list = new ArrayList<>();
	if (corpId > 0) {
	    String query = QueryConstantRest.FETCH_CORPORATE_TRAVEL_CORDNT_BY_CORPORATEID;
	    List<Object> paraList = new ArrayList<>();
	    paraList.add(corpId);
	    list = fetchWithHQL(query, paraList);
	}
	return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<CorporateFinanceContact> fetchFinanceContactDetails(Integer corpId) throws Exception {
	List<CorporateFinanceContact> list = new ArrayList<>();
	if (corpId > 0) {
	    String query = QueryConstantRest.FETCH_CORPORATE_FINANCE_CONTANT_BY_CORPORATEID;
	    List<Object> paraList = new ArrayList<>();
	    paraList.add(corpId);
	    list = fetchWithHQL(query, paraList);
	}
	return list;
    }

    @Override
    public List<CorporateModel> searchCorporateListByName(CorporateModel corporateModel) throws Exception {
	List<CorporateModel> corporateList;
	List<Object> parameterList = new ArrayList<>();
	StringBuilder stringQuery = new StringBuilder(QueryConstantRest.FETCH_CORPORATES);

	if (corporateModel.getCorporateName() != null && !corporateModel.getCorporateName().isEmpty()) {
	    stringQuery.append("AND cm.corporateName = ? ");
	    parameterList.add(corporateModel.getCorporateName());
	}
	
	if (corporateModel.getStatus() != null) {
	    stringQuery.append("AND cm.status = ? ");
	    parameterList.add(corporateModel.getStatus());
	}

	if(corporateModel.getCreatedByAgentId() != null) {
	    stringQuery.append("AND cm.createdByAgentId = ? ");
	    parameterList.add(corporateModel.getCreatedByAgentId());
	}
	if(corporateModel.getBranchId()!=null && !corporateModel.getBranchId().isEmpty())
	{
		stringQuery.append("AND cm.branchId = ? ");
		parameterList.add(corporateModel.getBranchId());
	}
	
	if(corporateModel.getAgencyId()!=null && !corporateModel.getAgencyId().isEmpty())
	{
		stringQuery.append("AND cm.agencyId = ? ");
		parameterList.add(corporateModel.getAgencyId());
	}
	corporateList = fetchWithHQL(stringQuery.toString(), parameterList);
	return corporateList;
    }

    @Override
    public List<CorporateModel> searchCorporateList(CorporateModel corporateModel) throws Exception {
	List<CorporateModel> corporateList;
	List<Object> parameterList = new ArrayList<>();
	StringBuilder stringQuery=null;
	 stringQuery = new StringBuilder(QueryConstantRest.FETCH_CORPORATES);
	if (corporateModel.getCorpId() != null && corporateModel.getCorpId() > 0) {
	    stringQuery.append("AND cm.corpId = ? ");
	    parameterList.add(corporateModel.getCorpId());
	}
	if(corporateModel.getBranchId()!=null && !corporateModel.getBranchId().isEmpty())
	{
		stringQuery.append("AND cm.branchId = ? ");
		parameterList.add(corporateModel.getBranchId());
	}
	
	if(corporateModel.getAgencyId()!=null && !corporateModel.getAgencyId().isEmpty())
	{
		stringQuery.append("AND cm.agencyId = ? ");
		parameterList.add(corporateModel.getAgencyId());
	}
	
	if (corporateModel.getCorporateName() != null && !corporateModel.getCorporateName().isEmpty()) {
	    stringQuery.append("AND lower(cm.corporateName) LIKE lower('" + corporateModel.getCorporateName() + "%') ");
	}
	if (corporateModel.getEmail() != null && !corporateModel.getEmail().isEmpty()) {
	    stringQuery.append("AND lower(cm.email) LIKE lower('" + corporateModel.getEmail() + "%') ");
	}
	if (corporateModel.getMobileCode() != null && !corporateModel.getMobileCode().isEmpty()) {
	    stringQuery.append("AND cm.mobileCode = ? ");
	    parameterList.add(corporateModel.getMobileCode());
	}
	if (corporateModel.getMobile() != null && !corporateModel.getMobile().isEmpty()) {
	    stringQuery.append("AND cm.mobile = ? ");
	    parameterList.add(corporateModel.getMobile());
	}
	if (corporateModel.getFax() != null && !corporateModel.getFax().isEmpty()) {
	    stringQuery.append("AND cm.fax = ? ");
	    parameterList.add(corporateModel.getFax());
	}
	//add condition by amit
	if ((corporateModel.getStatus() != null && corporateModel.getStatus() != -1) && (corporateModel.getBranchId()!=null && !corporateModel.getBranchId().isEmpty() ) ) {
	    stringQuery.append("AND cm.status = ? AND cm.branchId=?");
	    parameterList.add(corporateModel.getStatus());
	    parameterList.add(corporateModel.getBranchId());
	}
	//new add by amit
	if (corporateModel.getStatus() != null && corporateModel.getStatus() != -1  ) {
	    stringQuery.append("AND cm.status = ? ");
	    parameterList.add(corporateModel.getStatus());
	}
	//commented by amit
	if(corporateModel.getCreatedByAgentId() != null) {
	    stringQuery.append("AND cm.createdByAgentId = ? ");
	    
	    parameterList.add(corporateModel.getCreatedByAgentId());
	}
	/*if (corporateModel.getBranchId() != null && corporateModel.getBranchId().length() > 0) {
	    stringQuery.append(" AND cm.branchId = ? ");
	    parameterList.add(corporateModel.getBranchId());
	}*/
	corporateList = fetchWithHQL(stringQuery.toString(), parameterList);
	return corporateList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<CorporateModel> getCorporateListByAgencyId(Integer groupId) throws Exception {
	List<CorporateModel> list = new ArrayList<>();
	if (groupId > 0) {
	    String query = QueryConstantRest.FETCH_CORPORATE_LIST_BY_AGENCY_ID;
	    List<Object> paraList = new ArrayList<>();
	    paraList.add(groupId);
	    list = fetchWithHQL(query, paraList);
	}
	return list;
    }

    @Override
    public CorporateModel saveUpdateCorporate(CorporateModel corporateModal) throws Exception {
	if (corporateModal != null) {
	    saveorupdate(corporateModal);
	}
	return corporateModal;
    }
    
    @Override
	public List<Object> getCurrencyRate(String fromCurrency,String toCurrecny) throws Exception
	{
		String query = QueryConstantRest.GET_FACT_CURRENCY_RATE;
		if(null != fromCurrency && !"".equals(fromCurrency)){
			query += " and FCRD.FROM_CURR_ID ="+Integer.parseInt(fromCurrency);
		}
		if(null != toCurrecny && !"".equals(toCurrecny)){
			query += " and FCRD.TO_CURR_ID ="+Integer.parseInt(toCurrecny);
		}
		return fetchWithSQL(query,null);
	}
	
	@Override
	public List<Object> getCurrencyId(String currencyCode) throws Exception
	{
		String query = QueryConstantRest.GET_CURRENCY_ID;
		if(null != currencyCode && !"".equals(currencyCode)){
			query += " and c.CURRENCY_CODE = '"+currencyCode+"'";
		}
		return fetchWithSQL(query,null);
	}
	
	//add by amit
	
	@Override
	public List<Object> groupId(int id) throws Exception{
		StringBuilder stringQuery = new StringBuilder(QueryConstantRest.FETCH_GROUP_ID);
		stringQuery.append(" WHERE user_id = "+id+" ");
		List<Object> gList = fetchWithSQL(stringQuery.toString(), null);
		return gList;
		
	}
	
	@Override
	public boolean checkDuplicate(String branchId,String firstName, String email) throws Exception
	{
		boolean status=false;
		String query= QueryConstantRest.CHECK_CORPORATE_DUP;
		List<Object> parameterList = new ArrayList<>();
		parameterList.add(branchId);
		parameterList.add(firstName);
		parameterList.add(email);
		List<PaxModel> paxModel= fetchWithHQL(query, parameterList);
		if(paxModel!=null && !paxModel.isEmpty())
		{
			status =true;
		}
		
		return status;
	}
	public List<CorporateModel> fetCompanyName(String corpId) throws Exception {
		String query= QueryConstantRest.CHECK_CORPORATE_COMPANY_NAME;
		List<Object> parameterList = new ArrayList<>();
		parameterList.add(corpId);
		List<CorporateModel> corporateModel= fetchWithHQL(query, parameterList);
		return corporateModel;
	}
	   //Added By Pramod for SAT-14018 on 08-08-2018
	 @Override
	    public List<CorporateModel> searchCorporateListByLoggedInUser(CorporateModel corporateModel) throws Exception {
		List<CorporateModel> corporateList;
		List<Object> parameterList = new ArrayList<>();
		StringBuilder stringQuery=null;
		 stringQuery = new StringBuilder(QueryConstantRest.FETCH_CORPORATES_BY_USER_ID);
		 
		if(stringQuery!=null)
		parameterList.add(corporateModel.getLoggedInuserId());
		
		if (corporateModel.getCorpId() != null && corporateModel.getCorpId() > 0) {
		    stringQuery.append("AND cm.corpId = ? ");
		    parameterList.add(corporateModel.getCorpId());
		}
		if(corporateModel.getBranchId()!=null && !corporateModel.getBranchId().isEmpty()&& !"-1".equalsIgnoreCase(corporateModel.getBranchId()))
		{
			stringQuery.append("AND cm.branchId = ? ");
			parameterList.add(corporateModel.getBranchId());
		}
		
		if(corporateModel.getAgencyId()!=null && !corporateModel.getAgencyId().isEmpty())
		{
			stringQuery.append("AND cm.agencyId = ? ");
			parameterList.add(corporateModel.getAgencyId());
		}
		
		if (corporateModel.getCorporateName() != null && !corporateModel.getCorporateName().isEmpty()) {
		    stringQuery.append("AND lower(cm.corporateName) LIKE lower('" + corporateModel.getCorporateName() + "%') ");
		}
		if (corporateModel.getEmail() != null && !corporateModel.getEmail().isEmpty()) {
		    stringQuery.append("AND lower(cm.email) LIKE lower('" + corporateModel.getEmail() + "%') ");
		}
		if (corporateModel.getMobileCode() != null && !corporateModel.getMobileCode().isEmpty()) {
		    stringQuery.append("AND cm.mobileCode = ? ");
		    parameterList.add(corporateModel.getMobileCode());
		}
		if (corporateModel.getMobile() != null && !corporateModel.getMobile().isEmpty()) {
		    stringQuery.append("AND cm.mobile = ? ");
		    parameterList.add(corporateModel.getMobile());
		}
		if (corporateModel.getFax() != null && !corporateModel.getFax().isEmpty()) {
		    stringQuery.append("AND cm.fax = ? ");
		    parameterList.add(corporateModel.getFax());
		}
		//add condition by amit
		if ((corporateModel.getStatus() != null && corporateModel.getStatus() != -1) && (corporateModel.getBranchId()!=null && !corporateModel.getBranchId().isEmpty() ) ) {
		    stringQuery.append("AND cm.status = ? AND cm.branchId=? ");
		    parameterList.add(corporateModel.getStatus());
		    parameterList.add(corporateModel.getBranchId());
		}
		//new add by amit
		if (corporateModel.getStatus() != null && corporateModel.getStatus() != -1  ) {
		    stringQuery.append("AND cm.status = ? ");
		    parameterList.add(corporateModel.getStatus());
		}
		//commented by amit
		if(corporateModel.getCreatedByAgentId() != null) {
		    stringQuery.append("AND cm.createdByAgentId = ? ");
		    
		    parameterList.add(corporateModel.getCreatedByAgentId());
		}
		/*if (corporateModel.getBranchId() != null && corporateModel.getBranchId().length() > 0) {
		    stringQuery.append(" AND cm.branchId = ? ");
		    parameterList.add(corporateModel.getBranchId());
		}*/
		corporateList = fetchWithHQL(stringQuery.toString(), parameterList);
		return corporateList;
	    }
}
