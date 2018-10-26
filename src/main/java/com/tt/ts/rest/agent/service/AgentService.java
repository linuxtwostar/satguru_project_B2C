package com.tt.ts.rest.agent.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.tt.nc.common.util.TTLog;
import com.tt.nc.group.model.GroupModal;
import com.tt.nc.user.util.UserUtil;
import com.tt.ts.common.modal.ResultBean;
import com.tt.ts.common.util.CommonUtil;
import com.tt.ts.rest.agent.manager.AgentManager;
import com.tt.ts.rest.agent.model.AgencyMarkupModel;
import com.tt.ts.rest.agent.model.AgentCreditLimitModel;
import com.tt.ts.rest.agent.model.AgentModel;
import com.tt.ts.rest.agent.model.UserModal;

@Service
@Component(value = "AgentService")
public class AgentService {
   
    @Autowired
    AgentManager agentManager;
    
    public ResultBean createStaff(AgentModel staff) {
        ResultBean resultBean = new ResultBean();
        int savedId = 0;
        try {
            UserModal user = new UserModal();
            if(staff!=null){
                user.setMobileNo(staff.getMobileNo());
                user.setMobileNo1(staff.getMobileNo1());
                user.setEmail(staff.getEmail());
                String encryptPassword = new String(UserUtil.encodeString(new String(CommonUtil.generateRandomPswd(6,10,1,2,1))));
                user.setPassword(encryptPassword);
                user.setFirstName(staff.getFirstName());
                user.setUserAlias(staff.getOrgCode()+":"+staff.getEmail());
                user.setCreationTime(new Date());
                user.setLastModifiedDate(new Date());
                           
                user.setUserType(3);
	            user.setUserStatus(1);
	            user.setDisableSignIn(1);
           
	            GroupModal groupModel = new GroupModal();
	            groupModel.setGroupId(staff.getOrgId());
	            
	            
	            Set<GroupModal> gmSet = new HashSet<>();
			    gmSet.add(groupModel);
			    user.setGroupBeanList(gmSet);
	            
	            
                staff.setStatus(1);
                staff.setApprovalStatus(0);
               
                List<AgentCreditLimitModel> agentCreditLmtModelNew = new ArrayList<>();
                for(int i=0; i<staff.getUser().getAgentCreditLimit().size() ; i++){
                    if(staff.getUser().getAgentCreditLimit().get(i).getAmount() != null){
                    	AgentCreditLimitModel model = new AgentCreditLimitModel();
                    	model.setAmount(staff.getUser().getAgentCreditLimit().get(i).getAmount());
                    	model.setMonth(staff.getUser().getAgentCreditLimit().get(i).getMonth());
                    	model.setSequence(i);
                    	model.setStatus(staff.getUser().getAgentCreditLimit().get(i).getStatus());
                    	model.setYear(staff.getUser().getAgentCreditLimit().get(i).getYear());
                    	model.setUserModal(user);
                    	agentCreditLmtModelNew.add(model);
                    }
                }
               
                user.setAgentCreditLimit(agentCreditLmtModelNew);   
                staff.setUser(user);
                savedId = agentManager.saveAgent(staff);
                
             
            }
            resultBean.setResultInteger(savedId);
            resultBean.setIserror(false);//after saving staff a email will shoot to the staff with user name and password details
        }
        catch(SQLException sqlE){
        	 resultBean.setIserror(true);
             TTLog.error(5,sqlE);
        }
        catch (Exception e)
        {
            resultBean.setIserror(true);
            TTLog.error(5,e);
        }
        return resultBean;
    }
    
    @SuppressWarnings("unchecked")
	public ResultBean updateStaff(AgentModel staff) {
        ResultBean resultBean = new ResultBean();
        try {
        	UserModal user = new UserModal();
        	AgentModel agentModal = new AgentModel();
        	agentModal.setId(staff.getId());
        	List<AgentModel> listAgent = (List<AgentModel>)searchAgentList(agentModal).getResultList();
            if(listAgent != null && !listAgent.isEmpty()) {
            	user = listAgent.get(0).getUser();
            }
        	
            if(staff!=null && user!=null){
                user.setMobileNo(staff.getMobileNo());
                user.setMobileNo1(staff.getMobileNo1());
                user.setEmail(staff.getEmail());
                user.setFirstName(staff.getFirstName());
                user.setLastModifiedDate(new Date());
                            
                user.setUserType(3);
	            user.setUserStatus(staff.getStatus());
	            user.setDisableSignIn(1);
           
	            GroupModal groupModel = new GroupModal();
	            groupModel.setGroupId(staff.getOrgId());
	            	            
	            Set<GroupModal> gmSet = new HashSet<>();
			    gmSet.add(groupModel);
			    user.setGroupBeanList(gmSet);
	            	            
                staff.setApprovalStatus(0);
               
                List<AgentCreditLimitModel> agentCreditLmtModelNew = new ArrayList<>();
                if(staff.getUser()!=null && staff.getUser().getAgentCreditLimit()!=null) {
                	for(int i=0; i<staff.getUser().getAgentCreditLimit().size() ; i++){
                        if(staff.getUser().getAgentCreditLimit().get(i).getAmount() != null){
                        	AgentCreditLimitModel model = new AgentCreditLimitModel();
                        	model.setAmount(staff.getUser().getAgentCreditLimit().get(i).getAmount());
                        	model.setMonth(staff.getUser().getAgentCreditLimit().get(i).getMonth());
                        	model.setSequence(i);
                        	model.setStatus(staff.getUser().getAgentCreditLimit().get(i).getStatus());
                        	model.setYear(staff.getUser().getAgentCreditLimit().get(i).getYear());
                        	model.setUserModal(user);
                        	agentCreditLmtModelNew.add(model);
                        }
                    }
                } else {
                	ResultBean resultBeanAgentCredit = searchAgentCreditLimitList(staff);
                	if(resultBeanAgentCredit!=null && !resultBeanAgentCredit.isError()) {
                		agentCreditLmtModelNew = (List<AgentCreditLimitModel>)resultBeanAgentCredit.getResultList();
                	}
                }
                user.setAgentCreditLimit(agentCreditLmtModelNew); 
                staff.setUser(user);
                agentManager.updateAgent(staff);             
            }
            
            resultBean.setIserror(false);//after saving staff a email will shoot to the staff with user name and password details
        }
        catch(SQLException sqlE){
        	 resultBean.setIserror(true);
             TTLog.error(5,sqlE);
        }
        catch (Exception e)
        {
            resultBean.setIserror(true);
            TTLog.error(5,e);
        }
        return resultBean;
    }
    
	public ResultBean searchAgentList(AgentModel agentModel) {
		ResultBean resultBean = new ResultBean();
		try {
		    List<AgentModel> list = agentManager.searchAgentList(agentModel);
		    if(list != null && !list.isEmpty()) { 
			for(AgentModel am : list) {
			    am.setFirstName(am.getUser()!= null ? am.getUser().getFirstName() : "");
			}
		    }
		    resultBean.setResultList(list);
		    resultBean.setIserror(false);
		} catch (Exception e) {
		    resultBean.setIserror(true);
		    TTLog.error(0, e);
		}
		return resultBean;
    }
    
	public ResultBean searchAgentCreditLimitList(AgentModel agentModel) {
		ResultBean resultBean = new ResultBean();
		try {
			List<AgentCreditLimitModel> list = agentManager
					.searchAgentCreditLimitList(agentModel);
			if(list != null && !list.isEmpty()) {
				for(AgentCreditLimitModel agentCreditLimit : list) {
					String monthStr = com.tt.ts.rest.common.util.CommonUtil.fetchMonthString(Integer.parseInt(agentCreditLimit.getMonth()==null ? "0" : agentCreditLimit.getMonth()));
					agentCreditLimit.setMonthString(monthStr);
				}
			}
			resultBean.setResultList(list);
			resultBean.setIserror(false);
		} catch (Exception e) {
			resultBean.setIserror(true);
			TTLog.error(0, e);
		}
		return resultBean;
	}
	
    public ResultBean updateAgentSetting(GroupModal groupModel) {
    	ResultBean resultBean = new ResultBean();
    	groupModel.setCreatedBy(groupModel.getOrgModal().getCreatedBy());
    	int updatedId = 0;
    	try {
    		groupModel.setOrgModal(groupModel.getOrgModal());
    	    agentManager.updateSettingResult(groupModel);
    	    resultBean.setIserror(false);
    	    resultBean.setResultObject(updatedId);
    	} catch (Exception e) {
    	    resultBean.setIserror(true);
    	    TTLog.error(0, e);
    	}
    	return resultBean;
        }
    /*
    public ResultBean saveMarkup(AgencyMarkupModel agencyMarkup) {
        ResultBean resultBean = new ResultBean();
        int savedId = 0;
        try {
        	agencyMarkup.setCreatedDate(new Date());
        	agencyMarkup.setUpdatedDate(new Date());
        	
        	savedId = agentManager.saveMarkup(agencyMarkup);
        	resultBean.setResultInteger(savedId);
            resultBean.setIserror(false);
        }
        catch (Exception e) {
            resultBean.setIserror(true);
            TTLog.error(5,e);
        }
        return resultBean;
    }
    
   public ResultBean saveAllAirlieMarkup(AgencyMarkupModel agencyMarkup) {
        ResultBean resultBean = new ResultBean();
        int savedId = 0;
        try {
        	agencyMarkup.setCreatedDate(new Date());
        	agencyMarkup.setUpdatedDate(new Date());

        	savedId = agentManager.saveAllAirlieMarkup(agencyMarkup);
        	resultBean.setResultInteger(savedId);
            resultBean.setIserror(false);
        }
        catch (Exception e) {
            resultBean.setIserror(true);
            TTLog.error(5,e);
        }
        return resultBean;
    }
    
    
    public ResultBean updateAirlieMarkup(AgencyMarkupModel agencyMarkup) {
        ResultBean resultBean = new ResultBean();
        try {
        	agencyMarkup.setUpdatedDate(new Date());
     //   	agencyMarkup.setCreatedDate(agencyMarkup.getCreatedDate());
        	agencyMarkup.setStatus(1);
        	agentManager.updateAirlieMarkup(agencyMarkup);
            resultBean.setIserror(false);
        }
        catch (Exception e) {
            resultBean.setIserror(true);
            TTLog.error(5,e);
        }
        return resultBean;
    }
    
    public ResultBean agentMarkupDeleteAction(AgencyMarkupModel agencyMarkup) {
        ResultBean resultBean = new ResultBean();
        try {
      
        	agentManager.agentMarkupDeleteAction(agencyMarkup);
            resultBean.setIserror(false);
        }
        catch (Exception e) {
            resultBean.setIserror(true);
            TTLog.error(5,e);
        }
        return resultBean;
    }
    */
    

	public ResultBean fetchAgencyMarkup(AgencyMarkupModel agencyMarkup)
	{
        ResultBean resultBean = new ResultBean();
        try {
      
        	List<Object> agencyMarkupModels = agentManager.fetchAgencyMarkup(agencyMarkup);
        	resultBean.setResultList(agencyMarkupModels);
            resultBean.setIserror(false);
        }
        catch (Exception e) {
            resultBean.setIserror(true);
            TTLog.error(5,e);
        }
        return resultBean;
    }
	
	public ResultBean getUserMapping(int userId, String agencyCode){
		ResultBean resultBean =new ResultBean();
		try {
			List<Object> o = agentManager.getUserMapping(userId,agencyCode);
			if(!o.isEmpty() && o.get(0).equals(agencyCode))
				resultBean.setResultBoolean(true);
			else
				resultBean.setResultBoolean(false);
		}catch (Exception e) {
			resultBean.setIserror(true);
			TTLog.printStackTrace(0, e);
		}
		return resultBean;
	}
}