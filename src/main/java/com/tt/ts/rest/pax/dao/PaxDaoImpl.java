package com.tt.ts.rest.pax.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.tt.nc.common.util.TTLog;
import com.tt.nc.core.dao.GenericDAOImpl;
import com.tt.ts.rest.common.util.CommonUtil;
import com.tt.ts.rest.common.util.QueryConstantRest;
import com.tt.ts.rest.pax.model.PaxAirlinePrefernce;
import com.tt.ts.rest.pax.model.PaxDocumentModel;
import com.tt.ts.rest.pax.model.PaxFrequentFlyer;
import com.tt.ts.rest.pax.model.PaxModel;
import com.tt.ts.rest.pax.model.PaxRelationModel;

@Repository
public class PaxDaoImpl extends GenericDAOImpl<Object, Long> implements PaxDao {

    @Override
    public PaxModel saveUpdatePax(PaxModel paxModal) throws Exception {

	if (paxModal != null) {
	    saveorupdate(paxModal);
	}
	return paxModal;
    }

   @Override
    public List<PaxAirlinePrefernce> fetchAirlinePrefDetails(Integer paxId) throws Exception {
	List<PaxAirlinePrefernce> list = new ArrayList<>();
	if (paxId > 0) {
	    String query = QueryConstantRest.FETCH_AIRLINE_PREF_BY_PAX_ID;
	    List<Object> paraList = new ArrayList<>();
	    paraList.add(paxId);
	    list = fetchWithHQL(query, paraList);
	}
	return list;
    }

    @Override
    public List<PaxFrequentFlyer> fetchPaxFrequentFlyrDetails(Integer paxId) throws Exception {
	List<PaxFrequentFlyer> list = new ArrayList<>();
	if (paxId > 0) {
	    String query = QueryConstantRest.FETCH_PAX_FREQ_FLYR_BY_PAX_ID;
	    List<Object> paraList = new ArrayList<>();
	    paraList.add(paxId);
	    list = fetchWithHQL(query, paraList);
	}
	return list;
    }

   @Override
    public List<PaxDocumentModel> fetchPaxDocumentDetails(Integer paxId) throws Exception {
	List<PaxDocumentModel> list = new ArrayList<>();
	if (paxId > 0) {
	    String query = QueryConstantRest.FETCH_PAX_DOC_BY_PAX_ID;
	    List<Object> paraList = new ArrayList<>();
	    paraList.add(paxId);
	    list = fetchWithHQL(query, paraList);
	}
	return list;
    }
   
   @Override
   public List<Object> searchPaxListCount(PaxModel paxModel) throws Exception {
	List<Object> paxList;
	List<Object> parameterList = new ArrayList<>();
	StringBuilder stringQuery = new StringBuilder(QueryConstantRest.FETCH_PAXES_COUNT);
	if (paxModel.getId() != null && paxModel.getId() > 0) {
	    stringQuery.append(" AND pm.id = ? ");
	    parameterList.add(paxModel.getId());
	}
	if (paxModel.getFirstName() != null && !paxModel.getFirstName().isEmpty()) {
	    stringQuery.append(" AND lower(pm.firstName) LIKE lower('" + paxModel.getFirstName() + "%') ");
	}
	if (paxModel.getMiddleName() != null && !paxModel.getMiddleName().isEmpty()) {
	    stringQuery.append(" AND lower(pm.middleName) LIKE lower('" + paxModel.getMiddleName() + "%') ");
	}
	if (paxModel.getLastName() != null && !paxModel.getLastName().isEmpty()) {
	    stringQuery.append(" AND lower(pm.lastName) LIKE lower('" + paxModel.getLastName() + "%') ");
	}
	if (paxModel.getFromSearch()) {
	    if (paxModel.getCompanyId() != null && paxModel.getCompanyId() > 0) {
		stringQuery.append(" AND pm.companyId = ? ");
		parameterList.add(paxModel.getCompanyId());
	    } else {
		stringQuery.append(" AND pm.companyId IS NULL ");
	    }
	}

	if (paxModel.getEmail() != null && !paxModel.getEmail().isEmpty()) {
	    stringQuery.append(" AND lower(pm.email) = ? ");
	    parameterList.add(paxModel.getEmail().toLowerCase());
	}
	if (paxModel.getPhone() != null && !paxModel.getPhone().isEmpty()) {
		 stringQuery.append(" AND pm.phone LIKE '"+paxModel.getPhone()+"%' ");
	}
	if (paxModel.getNationality() != null && !paxModel.getNationality().isEmpty()) {
	    stringQuery.append(" AND pm.nationality = ? ");
	    parameterList.add(paxModel.getNationality());
	}
	if (paxModel.getStatus() != null && paxModel.getStatus()!= -1) {
	    stringQuery.append(" AND pm.status = ? ");
	    parameterList.add(paxModel.getStatus());
	}
	
	// Added By Shubham on August 8 ,2018
	if(paxModel.getBranchIds() != null && !paxModel.getBranchIds().isEmpty()){
		stringQuery.append(" AND pm.branchId IN ( " + paxModel.getBranchIds()  + " ) ");
	}
	
	//commented by amit
	/*if (paxModel.getCreatedBy() != null && paxModel.getCreatedBy() > 0) {
	    stringQuery.append(" AND pm.createdBy = ? ");
	    parameterList.add(paxModel.getCreatedBy());
	}*/
	if (paxModel.getAgencyId() != null && paxModel.getAgencyId().length() > 0) {
	    stringQuery.append(" AND pm.agencyId = ? ");
	    parameterList.add(paxModel.getAgencyId() );
	}
	else if(paxModel.getId() ==null || paxModel.getId()==0){
		stringQuery.append(" AND pm.agencyId IS NULL ");
	}
	//add by amit
	if (paxModel.getBranchId() != null && paxModel.getBranchId().length() > 0) {
	    stringQuery.append(" AND pm.branchId = ? ");
	    parameterList.add(paxModel.getBranchId());
	}
	paxList = fetchWithHQL(stringQuery.toString(), parameterList);
	/*if(paxModel!=null && paxModel.getPageNumber()>0 && paxModel.getMaxRecordPerPage()>0){
		paxList = getPaginatedDataWithHQL(paxModel.getPageNumber(),paxModel.getMaxRecordPerPage(),stringQuery.toString(), parameterList);
	}
	else
	{
		paxList = fetchWithHQL(stringQuery.toString(), parameterList);
	} */
	return paxList;
   }

    @Override
    public List<PaxModel> searchPaxList(PaxModel paxModel) throws Exception {
	List<PaxModel> paxList;
	List<Object> parameterList = new ArrayList<>();
	StringBuilder stringQuery = new StringBuilder(QueryConstantRest.FETCH_PAXES);
	if (paxModel.getId() != null && paxModel.getId() > 0) {
	    stringQuery.append(" AND pm.id = ? ");
	    parameterList.add(paxModel.getId());
	}
	if (paxModel.getFirstName() != null && !paxModel.getFirstName().isEmpty()) {
	    stringQuery.append(" AND lower(pm.firstName) LIKE lower('" + paxModel.getFirstName() + "%') ");
	}
	if (paxModel.getMiddleName() != null && !paxModel.getMiddleName().isEmpty()) {
	    stringQuery.append(" AND lower(pm.middleName) LIKE lower('" + paxModel.getMiddleName() + "%') ");
	}
	if (paxModel.getLastName() != null && !paxModel.getLastName().isEmpty()) {
	    stringQuery.append(" AND lower(pm.lastName) LIKE lower('" + paxModel.getLastName() + "%') ");
	}
	if (paxModel.getFromSearch()) {
	    if (paxModel.getCompanyId() != null && paxModel.getCompanyId() > 0) {
		stringQuery.append(" AND pm.companyId = ? ");
		parameterList.add(paxModel.getCompanyId());
	    } else {
		stringQuery.append(" AND pm.companyId IS NULL ");
	    }
	}

	if (paxModel.getEmail() != null && !paxModel.getEmail().isEmpty()) {
	    stringQuery.append(" AND lower(pm.email) = ? ");
	    parameterList.add(paxModel.getEmail().toLowerCase());
	}
	if (paxModel.getPhone() != null && !paxModel.getPhone().isEmpty()) {
		 stringQuery.append(" AND pm.phone LIKE '"+paxModel.getPhone()+"%'");
	}
	if (paxModel.getNationality() != null && !paxModel.getNationality().isEmpty()) {
	    stringQuery.append(" AND pm.nationality = ? ");
	    parameterList.add(paxModel.getNationality());
	}
	if (paxModel.getStatus() != null) {
	    stringQuery.append(" AND pm.status = ? ");
	    parameterList.add(paxModel.getStatus());
	}
	/*if (paxModel.getCreatedBy() != null && paxModel.getCreatedBy() > 0) {
	    stringQuery.append(" AND pm.createdBy = ? ");
	    parameterList.add(paxModel.getCreatedBy());
	}*/
	if (paxModel.getAgencyId() != null && paxModel.getAgencyId().length() > 0) {
	    stringQuery.append(" AND pm.agencyId = ? ");
	    parameterList.add(paxModel.getAgencyId() );
	}
	else if(paxModel.getId() ==null || paxModel.getId()==0){
		stringQuery.append(" AND pm.agencyId IS NULL ");
	}
	
	// Added By Shubham on August 8 ,2018
		if(paxModel.getBranchIds() != null && !paxModel.getBranchIds().isEmpty()){
			stringQuery.append(" AND pm.branchId IN ( " + paxModel.getBranchIds()  + " ) ");
		}
	
	//add by amit
		if (paxModel.getBranchId() != null && paxModel.getBranchId().length() > 0) {
		    stringQuery.append(" AND pm.branchId = ? ");
		    parameterList.add(paxModel.getBranchId());
		}
	//paxList = fetchWithHQL(stringQuery.toString(), parameterList);
	if(paxModel!=null && paxModel.getPageNumber()>0 && paxModel.getMaxRecordPerPage()>0){
		paxList = getPaginatedDataWithHQL(paxModel.getPageNumber(),paxModel.getMaxRecordPerPage(),stringQuery.toString(), parameterList);
	}
	else
	{
		paxList = fetchWithHQL(stringQuery.toString(), parameterList);
	}
	return paxList;
    }

    @Override
    public List<PaxModel> paxSearchResultByNameMob(PaxModel paxModel) throws Exception {
	List<PaxModel> paxList;
	List<Object> parameterList = new ArrayList<>();
	StringBuilder stringQuery = new StringBuilder(QueryConstantRest.FETCH_PAXES);

	if (paxModel.getFirstName() != null && !paxModel.getFirstName().isEmpty()) {
	    stringQuery.append("AND(( lower(pm.firstName || ' ' || pm.lastName) LIKE lower('" + paxModel.getFirstName() + "%'))  OR (" + "(pm.phoneCode || pm.phone) LIKE '" + paxModel.getFirstName() + "%') OR (" + "pm.phone LIKE '" + paxModel.getFirstName() + "%')) AND pm.companyId  is null ");
	}

	if (paxModel.getStatus() != null) {
	    stringQuery.append("AND pm.status = ? ");
	    parameterList.add(paxModel.getStatus());
	}
	/*if (paxModel.getCreatedBy() != null && paxModel.getCreatedBy() > 0) {
	    stringQuery.append("AND pm.createdBy = ? ");
	    parameterList.add(paxModel.getCreatedBy());
	}*/
	if (paxModel.getAgencyId() != null && paxModel.getAgencyId().length() > 0) {
	    stringQuery.append(" AND pm.agencyId = ? ");
	    parameterList.add(paxModel.getAgencyId() );
	}
	else{
		stringQuery.append(" AND pm.agencyId IS NULL ");
	}
	//add by amit
		if (paxModel.getBranchId() != null && paxModel.getBranchId().length() > 0) {
		    stringQuery.append(" AND pm.branchId = ? ");
		    parameterList.add(paxModel.getBranchId());
		}
	if (paxModel.getCompanyId() != null && paxModel.getCompanyId() > 0) {
	    stringQuery.append("AND pm.companyId = ? ");
	    parameterList.add(paxModel.getCompanyId());
	}
	paxList = fetchWithHQL(stringQuery.toString(), parameterList);
	return paxList;
    }

    @Override
    public List<PaxModel> searchColleagueList(PaxModel paxModel) throws Exception {
	List<PaxModel> paxList;
	List<Object> parameterList = new ArrayList<>();
	StringBuilder stringQuery = new StringBuilder(QueryConstantRest.FETCH_PAXES);
	if (paxModel.getFirstName() != null && !paxModel.getFirstName().isEmpty()) {
	    stringQuery.append("AND lower(pm.firstName) LIKE lower('" + paxModel.getFirstName() + "%')");
	}
	if (paxModel.getFromSearch()) {
	    if (paxModel.getCompanyId() != null && paxModel.getCompanyId() > 0) {
		stringQuery.append("AND pm.companyId = ? ");
		parameterList.add(paxModel.getCompanyId());
	    } else {
		stringQuery.append("AND pm.companyId IS NULL ");
	    }
	}
	if (paxModel.getStatus() != null) {
	    stringQuery.append("AND pm.status = ? ");
	    parameterList.add(paxModel.getStatus());
	}
	if (paxModel.getPaxIds() != null && !paxModel.getPaxIds().isEmpty()) {
	    stringQuery.append("AND pm.id NOT IN(" + paxModel.getPaxIds() + ") ");
	}

	paxList = fetchWithHQL(stringQuery.toString(), parameterList);
	return paxList;
    }

    @Override
    public List<PaxRelationModel> searchPaxRelationList(Integer paxId) throws Exception {
	List<PaxRelationModel> paxList;
	List<Object> parameterList = new ArrayList<>();
	StringBuilder stringQuery = new StringBuilder(QueryConstantRest.FETCH_PAXE_RELATIONS);
	if (paxId != null && paxId > 0) {
	    stringQuery.append("AND prm.paxId = ? ");
	    parameterList.add(paxId);
	}
	paxList = fetchWithHQL(stringQuery.toString(), parameterList);
	return paxList;
    }

    @Override
    public int savePaxRelation(PaxRelationModel paxRelationModel) throws Exception {
	int id = 0;
	if (paxRelationModel != null) {
	    id = (int) makePersistent(paxRelationModel);
	}
	return id;
    }

    private void setQueryparameter(Query query, List<Object> parameterList) {
	if (null != parameterList && !parameterList.isEmpty()) {
	    for (int i = 0; i < parameterList.size(); i++) {

		if (parameterList.get(i) instanceof Integer) {
		    query.setInteger(i, (Integer) parameterList.get(i));
		} else if (parameterList.get(i) instanceof Date) {
		    query.setTimestamp(i, (Date) parameterList.get(i));
		} else if (parameterList.get(i) instanceof Float) {
		    query.setFloat(i, (Float) parameterList.get(i));
		} else if (parameterList.get(i) instanceof Double) {
		    query.setDouble(i, (Double) parameterList.get(i));
		} else if (parameterList.get(i) instanceof Long) {
		    query.setLong(i, (Long) parameterList.get(i));
		} else if (parameterList.get(i) instanceof String) {
		    query.setString(i, (String) parameterList.get(i));
		} else {
		    query.setParameter(i, parameterList.get(i));
		}
	    }
	}
    }

    @Override
    public void deleteChildTableListItem(List<String> hqlList, List<Object> parameterList) throws Exception {
	Session session = getSessionFactory().openSession();
	Transaction tx = null;
	try {
	    tx = session.beginTransaction();
	    if (hqlList != null && !hqlList.isEmpty()) {
		for (String deleteQuery : hqlList) {
		    Query query = session.createQuery(deleteQuery);
		    setQueryparameter(query, parameterList);
		    query.executeUpdate();
		}
	    }
	    tx.commit();
	} catch (HibernateException e) {
	    if (tx != null)
		tx.rollback();
	    TTLog.info(13, "GenericDAOImpl : deleteChildTableListItem HibernateException = " + e);
	    throw e;
	} catch (Exception e) {
	    if (tx != null)
		tx.rollback();
	    TTLog.info(13, "GenericDAOImpl : deleteChildTableListItem Exception = " + e);
	    throw e;
	} finally {
	    session.flush();
	    session.clear();
	    session.close();
	}
    }

    @Override
    public void deleteChildTableListItem(List<String> hqlList, List<Object> paramList, Session session, Transaction tx) throws Exception {
	if (session != null && tx != null) {
	    if (hqlList != null && hqlList.size() > 0) {
		for (String deleteQuery : hqlList) {
		    Query query = session.createQuery(deleteQuery);
		    setQueryparameter(query, paramList);
		    query.executeUpdate();
		}
	    }
	}

    }

    public PaxModel updatePaxDetail(PaxModel paxModal) throws Exception {
	Session session = getSessionFactory().openSession();
	Transaction tx = session.beginTransaction();
	try {
	    if (paxModal != null && paxModal.getId() > 0) {
		List<String> queryList = new ArrayList<>();
		List<Object> paramList = new ArrayList<>();
		String paxAirPrefDeletion = QueryConstantRest.GET_DELETE_FOR_PAX_AIRPREF;
		if (paxAirPrefDeletion != null && !paxAirPrefDeletion.isEmpty()) {
		    queryList.add(paxAirPrefDeletion);
		}
		String paxFreqFlyrPrefDeletion = QueryConstantRest.GET_DELETE_FOR_PAX_FREQFLYR_REF;
		if (paxFreqFlyrPrefDeletion != null && !paxFreqFlyrPrefDeletion.isEmpty()) {
		    queryList.add(paxFreqFlyrPrefDeletion);
		}
		paramList.add(paxModal.getId());
		deleteChildTableListItem(queryList, paramList, session, tx);
	    }
	    session.saveOrUpdate(paxModal);
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
	return paxModal;
    }

	@Override
	public List<PaxModel> paxValidation(PaxModel paxModal) throws Exception
	{
		List<String> paramList=new ArrayList<>();
		paramList.add(paxModal.getEmail()!=null?paxModal.getEmail().trim().toLowerCase():null);
		paramList.add(paxModal.getFirstName()!=null?paxModal.getFirstName().trim().toLowerCase():null);
		if(paxModal.getAgencyId()!=null){
			paramList.add(paxModal.getAgencyId());
		}
		if(paxModal.getBranchId()!=null){
			paramList.add(paxModal.getBranchId());
		}
		StringBuilder stringQuery = new StringBuilder(QueryConstantRest.VALIDATE_PAX_MODAL);
		if(paxModal.getAgencyId()!=null && !("").equalsIgnoreCase(paxModal.getAgencyId())){
			stringQuery.append(" AND  agencyId = ?  ");
		}
		else{
			stringQuery.append(" AND agencyId IS NULL ");
		}
		if(paxModal.getBranchId()!=null && !("").equalsIgnoreCase(paxModal.getBranchId())){
			stringQuery.append(" AND  branchId = ?  ");
		}
		return fetchWithHQL(stringQuery.toString(), paramList);
	}

	@Override
	public void updatePaxDetails(PaxModel paxModal) throws Exception {
		List<Object> paramList = new ArrayList<>();
		StringBuilder stringQuery = new StringBuilder(QueryConstantRest.UPDATE_PAX_DETAILS);
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		boolean isParamAdded = false;
		if(null != paxModal.getTitle())
		{
			if(isParamAdded)
				stringQuery.append(",PM.TITLE = ?");
			else 
				stringQuery.append("PM.TITLE = ?");
			
			paramList.add(paxModal.getTitle());
			isParamAdded = true;
		}
		if(null != paxModal.getLastName() && !paxModal.getLastName().isEmpty())
		{
			if(isParamAdded)
				stringQuery.append(" ,PM.LAST_NAME = ?");
			else
				stringQuery.append("PM.LAST_NAME = ?");
			
			paramList.add(paxModal.getLastName());
			isParamAdded = true;
		}
		if(null != paxModal.getPhone() && !paxModal.getPhone().isEmpty())
		{
			if(isParamAdded)
				stringQuery.append(" ,PM.PHONE = ?");
			else
				stringQuery.append("PM.PHONE = ?");
			paramList.add(paxModal.getPhone());
			isParamAdded = true;
		}
		if(null != paxModal.getDob())
		{
	        String sqlDate = inputFormat.format(paxModal.getDob());
	        if(isParamAdded)
				stringQuery.append(" ,PM.DOB = DATE('"+sqlDate+"')");
			else
				stringQuery.append("PM.DOB = DATE('"+sqlDate+"')");
			isParamAdded = true;
		}
		if(null != paxModal.getNationality() && !paxModal.getNationality().isEmpty())
		{
			if(isParamAdded)
				stringQuery.append(" ,PM.NATIONALITY = ?");
			else
				stringQuery.append("PM.NATIONALITY = ?");
			paramList.add(paxModal.getNationality());
			isParamAdded = true;
		}	
		if(null != paxModal.getAddress() && !paxModal.getAddress().isEmpty())
		{
			if(isParamAdded)
				stringQuery.append(" ,PM.ADDRESS = ?");
			else
				stringQuery.append("PM.ADDRESS = ?");
			paramList.add(paxModal.getAddress());
			isParamAdded = true;
		}
		if(isParamAdded)
			stringQuery.append(" ,PM.LAST_UPDATION_TIME = ?");
		else
			stringQuery.append("PM.LAST_UPDATION_TIME = ?");
		paramList.add(new Date());
		
		stringQuery.append(" WHERE PM.ID = ?");
		paramList.add(paxModal.getId());
		
		executeSQLQuery(stringQuery.toString(), paramList);
		
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
		String query= QueryConstantRest.CHECK_TRAVELER_DUP;
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
	
    @SuppressWarnings("unchecked")
    @Override
	   public List<PaxModel> searchCorporateListByName(PaxModel paxModel) throws Exception {
			List<PaxModel> travellerList;
			List<Object> parameterList = new ArrayList<>();
			StringBuilder stringQuery = new StringBuilder(QueryConstantRest.FETCH_TRAVELLER);

			if (paxModel.getFirstName() != null && !paxModel.getFirstName().isEmpty()) {
			    stringQuery.append("AND pm.firstName = ? ");
			    parameterList.add(paxModel.getFirstName());
			}
			if (paxModel.getStatus() != null) {
			    stringQuery.append("AND cm.status = ? ");
			    parameterList.add(paxModel.getStatus());
			}
			/*if(paxModel.getCreatedByAgentId() != null) {
			    stringQuery.append("AND cm.createdByAgentId = ? ");
			    parameterList.add(paxModel.getCreatedByAgentId());
			}*/
			travellerList = fetchWithHQL(stringQuery.toString(), parameterList);
			return travellerList;
		    }

    @Override
	public List<PaxModel> hotelPaxValidation(PaxModel paxModal) throws Exception
	{
		List<String> paramList=new ArrayList<>();
		StringBuilder stringQuery = new StringBuilder();
		if(!"1".equals(paxModal.getPaxType())){ // Adult case
			paramList.add(paxModal.getEmail()!=null?paxModal.getEmail().trim().toLowerCase():null);
			paramList.add(paxModal.getFirstName()!=null?paxModal.getFirstName().trim().toLowerCase():null);
			if(paxModal.getAgencyId()!=null){
				paramList.add(paxModal.getAgencyId());
			}
			if(paxModal.getBranchId()!=null){
				paramList.add(paxModal.getBranchId());
			}
			//stringQuery = new StringBuilder(QueryConstantRest.VALIDATE_PAX_MODAL);
			stringQuery.append(QueryConstantRest.VALIDATE_PAX_MODAL);
			if(paxModal.getAgencyId()!=null && !("").equalsIgnoreCase(paxModal.getAgencyId())){
				stringQuery.append(" AND  agencyId = ?  ");
			}
			else{
				stringQuery.append(" AND agencyId IS NULL ");
			}
			if(paxModal.getBranchId()!=null && !("").equalsIgnoreCase(paxModal.getBranchId())){
				stringQuery.append(" AND  branchId = ?  ");
			}
		}else if("1".equals(paxModal.getPaxType())){ // child case
			String convertedDate = null;
			String firstName = paxModal.getFirstName()!=null?paxModal.getFirstName().trim().toLowerCase():null;
			String lastName = paxModal.getLastName()!=null?paxModal.getLastName().trim().toLowerCase():null;
			String email = paxModal.getEmail()!=null?paxModal.getEmail().trim().toLowerCase():null;
			Date dob = paxModal.getDob()!=null?paxModal.getDob():null;
			if(null != dob){
				convertedDate = CommonUtil.convertDatetoString(dob, "yyyy-MM-dd");
			}
			stringQuery.append(QueryConstantRest.VALIDATE_HOTEL_CHILD_MODAL);
			if(null != firstName && !"".equals(firstName)){
				stringQuery.append(" AND lower(firstName) = ").append("'"+firstName+"'");
			}
			if(null != lastName && !"".equals(lastName)){
				stringQuery.append(" AND lower(lastName) = ").append("'"+lastName+"'");
			}
			if(null != email && !"".equals(email)){
				stringQuery.append(" AND lower(email) = ").append("'"+email+"'");
			}
			if(null != convertedDate){
				stringQuery.append(" AND DATE(dob) = DATE('").append(convertedDate+"')");
			}
			if(paxModal.getAgencyId()!=null && !("").equalsIgnoreCase(paxModal.getAgencyId())){
				stringQuery.append(" AND  agencyId = ").append("'"+paxModal.getAgencyId()+"'");
			}
			else
			{
				stringQuery.append(" AND agencyId IS NULL ");	
			}
			if(paxModal.getBranchId()!=null && !("").equalsIgnoreCase(paxModal.getBranchId())){
				stringQuery.append(" AND  branchId = ").append("'"+paxModal.getBranchId()+"'");
			}
		}
		
		return fetchWithHQL(stringQuery.toString(), paramList);
	}
    @Override
    public void savePaxDocumentData(PaxDocumentModel paxDocument) throws Exception {
	saveorupdate(paxDocument);
    }
    @Override
    public void deletePaxDocuments(Integer paxId) throws Exception {
	List<String> queryList = new ArrayList<>();
	List<Object> paramList = new ArrayList<>();
	String paDocDeletion = QueryConstantRest.GET_DELETE_FOR_PAX_DOCUMENT;
	if (paDocDeletion != null && !paDocDeletion.isEmpty()) {
	    queryList.add(paDocDeletion);
	}
	paramList.add(paxId);
	deleteChildTableListItem(queryList, paramList);
    }
}
