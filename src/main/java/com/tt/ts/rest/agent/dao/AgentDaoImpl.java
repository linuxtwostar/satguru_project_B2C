package com.tt.ts.rest.agent.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import com.tt.nc.common.util.TTLog;
import com.tt.nc.core.dao.GenericDAOImpl;
import com.tt.nc.group.model.GroupModal;
import com.tt.ts.organization.model.OrganizationModel;
import com.tt.ts.rest.agent.model.AgencyMarkupModel;
import com.tt.ts.rest.agent.model.AgentCreditLimitModel;
import com.tt.ts.rest.agent.model.AgentModel;
import com.tt.ts.rest.agent.model.UserModal;
import com.tt.ts.rest.common.util.QueryConstantRest;

@Component("agentDAO")
public class AgentDaoImpl extends GenericDAOImpl<Object, Long> implements AgentDao {

    @Override
    public Serializable saveAgent(AgentModel staff) throws Exception {
	Serializable id = 0;
	Session session = getSessionFactory().openSession();
	Transaction tx = session.beginTransaction();
	try {
	    if (null != tx) {
		UserModal user = staff.getUser();
		user.setAgentModel(staff);
		session.save(user);
		staff.setId(user.getUserId());
		id = session.save(staff);
		tx.commit();
	    }
	} catch (HibernateException e) {
	    if (tx!=null) {
		tx.rollback();
	    }
	    TTLog.printStackTrace(0, e);
	    throw e;
	} finally {
	    session.close();
	}
	return id;
    }
    
    @Override
    public void updateAgent(AgentModel staff) throws Exception {
    	Session session = getSessionFactory().openSession();
    	Transaction tx = session.beginTransaction();
    	List<String> hqlList = new ArrayList<String>();
		List<Object> paramList = new ArrayList<Object>();
    	try {
    	    if (null != tx) {
    	    	String creditLimit = QueryConstantRest.DELETE_AGENTS_CREDITLIMIT;
    	    	hqlList.add(creditLimit);
    	    	paramList.add(staff.getId());
    	    	deleteChildTableListItem(hqlList, paramList,session,tx);
    	    	
    	    	UserModal user = staff.getUser();
	    		user.setAgentModel(staff);
	    		session.update(user);
	    		staff.setId(user.getUserId());
	    		session.update(staff);
	    		tx.commit();
    	    }
    	} catch (HibernateException e) {
    	    if (tx!=null) {
    		tx.rollback();
    	    }
    	    TTLog.printStackTrace(0, e);
    	    throw e;
    	} finally {
    	    session.close();
    	}
    }
    
    @Override
    public List<AgentModel> searchAgentList(AgentModel agentModel) throws Exception {
	List<AgentModel> agentList;
	List<Object> parameterList = new ArrayList<>();
	StringBuilder stringQuery = new StringBuilder(QueryConstantRest.FETCH_AGENTS);
	if (agentModel.getId() > 0) {
	    stringQuery.append("AND am.id = ? ");
	    parameterList.add(agentModel.getId());
	}
	if (agentModel.getFirstName() != null && !agentModel.getFirstName().isEmpty()) {
	    stringQuery.append("AND upper(am.user.firstName) LIKE '%" + agentModel.getFirstName().toUpperCase() + "%'");
	}
	if (agentModel.getEmail() != null && !agentModel.getEmail().isEmpty()) {
	    stringQuery.append("AND am.email = ? ");
	    parameterList.add(agentModel.getEmail());
	}
	if (agentModel.getMobileNo() != null && !agentModel.getMobileNo().isEmpty()) {
	    stringQuery.append("AND am.mobileNo = ? ");
	    parameterList.add(agentModel.getMobileNo());
	}
	if(agentModel.getOrgId() != null && agentModel.getOrgId() > 0) {
	    stringQuery.append("AND am.orgId = ? ");
	    parameterList.add(agentModel.getOrgId());
	}
	agentList = fetchWithHQL(stringQuery.toString(), parameterList);
	return agentList;
    }

    @Override
    public List<AgentCreditLimitModel> searchAgentCreditLimitList(AgentModel agentModel) throws Exception {
    	List<AgentCreditLimitModel> creditLimitList;
    	List<Object> parameterList = new ArrayList<>();
    	StringBuilder stringQuery = new StringBuilder(QueryConstantRest.FETCH_AGENTS_CREDITLIMIT);
    	if(agentModel.getId() > 0) {
    		 stringQuery.append("AND acm.userModal = ? ");
    		    parameterList.add(agentModel.getId());
    	}
    	creditLimitList = fetchWithHQL(stringQuery.toString(), parameterList);
    	return creditLimitList;
    }
    
	@Override
	public void updateSettingResult(GroupModal groupModel)
			throws Exception {
		String query =QueryConstantRest.UPDATE_AGENT_SETTING;
		List<Object> paraList =new ArrayList<>();
		paraList.add(groupModel.getOrgModal().getCompanyURL());
		paraList.add(groupModel.getOrgModal().getNotificationType());
		paraList.add(groupModel.getGroupId());
		paraList.add(groupModel.getOrgModal().getOrganizationId());
		updateWithHQL(query, paraList);
	}

	
	@Override
	public List<Object> fetchAgencyMarkup(AgencyMarkupModel agencyMarkup) throws Exception
	{
		String query;
		if(agencyMarkup.getProductRefId()==1)
			query =QueryConstantRest.FETCH_AGENCY_MARKUP;
		else
			query =QueryConstantRest.FETCH_AGENCY_MARKUP_OTHERPRODUCT;	
		List<Object> paraList =new ArrayList<>();
		paraList.add(agencyMarkup.getProductRefId());
		paraList.add(agencyMarkup.getAgencyId());
		if(agencyMarkup.getProductRefId()!=1)// In flight status should not be considered while getting markup data.
			paraList.add(agencyMarkup.getStatus());
		paraList.add(agencyMarkup.getCorporateId());
		if(agencyMarkup.getProductRefId()==1)
			paraList.add(agencyMarkup.getDomOrInternational());
		return fetchWithHQL(query, paraList);
		
		
	}

	@Override
	public List<Object> getUserMapping(int userId, String agencyCode) throws Exception {
		StringBuilder stringQuery = new StringBuilder(QueryConstantRest.GET_USER_AGENCY_CODE);
		if (userId > -1) {
			stringQuery.append(" gu.userId="+userId+") ");
		}
		return fetchWithHQL(stringQuery.toString(), null);
	}
	
	
	@Override
	public List<OrganizationModel> getUserMappingAuthorized(int userId, String agencyCode) throws Exception {
		StringBuilder stringQuery = new StringBuilder(QueryConstantRest.GET_USER_AGENCY_CODE);
		if (userId > -1) {
			stringQuery.append(" gu.userId="+userId+") ");
			//stringQuery.append(" and om.factApprovalStatus=1 ");
			//stringQuery.append(" and om.status=1 ");
			//stringQuery.append(" and om.approvalStatus=1 ");
		}
		return fetchWithHQL(stringQuery.toString(), null);
	}
	
	
	
}
