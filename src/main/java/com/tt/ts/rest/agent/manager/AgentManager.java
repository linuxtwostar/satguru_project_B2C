package com.tt.ts.rest.agent.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tt.nc.group.model.GroupModal;
import com.tt.ts.organization.model.OrganizationModel;
import com.tt.ts.rest.agent.dao.AgentDao;
import com.tt.ts.rest.agent.model.AgencyMarkupModel;
import com.tt.ts.rest.agent.model.AgentCreditLimitModel;
import com.tt.ts.rest.agent.model.AgentModel;

@Component
public class AgentManager {
	
    @Autowired
    AgentDao agentDao;

    public int saveAgent(AgentModel staff) throws Exception {
    	return (int) agentDao.saveAgent(staff);
    }

    public void updateAgent(AgentModel staff) throws Exception {
    	agentDao.updateAgent(staff);
    }
    
    public List<AgentModel> searchAgentList(AgentModel agentModel) throws Exception {
	return agentDao.searchAgentList(agentModel);
    }
    
    public List<AgentCreditLimitModel> searchAgentCreditLimitList(AgentModel agentModel) throws Exception {
    	return agentDao.searchAgentCreditLimitList(agentModel);
    }
    
    public void updateSettingResult(GroupModal groupModel) throws Exception {
    	agentDao.updateSettingResult(groupModel);
    }

	public List<Object> fetchAgencyMarkup(AgencyMarkupModel agencyMarkup) throws Exception
	{
		return agentDao.fetchAgencyMarkup(agencyMarkup);
	}
	
	public List<Object> getUserMapping(int userId, String agencyCode) throws Exception{
		return agentDao.getUserMapping(userId, agencyCode);
	}
	
	public List<OrganizationModel> getUserMappingAuthorized(int userId, String agencyCode) throws Exception{
		return agentDao.getUserMappingAuthorized(userId, agencyCode);
	}
    
}
