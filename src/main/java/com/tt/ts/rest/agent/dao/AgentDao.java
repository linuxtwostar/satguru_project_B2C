package com.tt.ts.rest.agent.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.tt.nc.group.model.GroupModal;
import com.tt.ts.organization.model.OrganizationModel;
import com.tt.ts.rest.agent.model.AgencyMarkupModel;
import com.tt.ts.rest.agent.model.AgentCreditLimitModel;
import com.tt.ts.rest.agent.model.AgentModel;

@Repository
public interface AgentDao {

    public Serializable saveAgent(AgentModel staff) throws Exception;

    public void updateAgent(AgentModel staff) throws Exception;
    
    List<AgentModel> searchAgentList(AgentModel agentModel) throws Exception;
    
    public List<AgentCreditLimitModel> searchAgentCreditLimitList(AgentModel agentModel) throws Exception;
    
    public void updateSettingResult(GroupModal groupModel) throws Exception;

	public List<Object> fetchAgencyMarkup(AgencyMarkupModel agencyMarkup) throws Exception;
	
	public List<Object> getUserMapping(int userId, String agencyCode) throws Exception;
	
	public List<OrganizationModel> getUserMappingAuthorized(int userId, String agencyCode) throws Exception;
}
