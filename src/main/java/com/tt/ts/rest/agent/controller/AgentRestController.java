package com.tt.ts.rest.agent.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.test.model.ServiceResponse;
import com.tt.nc.group.model.GroupModal;
import com.tt.ts.common.modal.ResultBean;
import com.tt.ts.rest.agent.model.AgentModel;
import com.tt.ts.rest.agent.service.AgentService;
import com.tt.ts.rest.common.util.CommonUtil;

@RestController	
@RequestMapping("/dashboard")
public class AgentRestController
{

	private static final String FAILED = "Failed";
	private static final String FAILED_MSG = "Something went wrong.";

	@Autowired
	private AgentService agentServices;

	@RequestMapping(value = "/agentAction", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String agentCreateAction(@RequestBody AgentModel staff)
	{
		ServiceResponse response = new ServiceResponse();
		ResultBean resultBean = agentServices.createStaff(staff);
		if (resultBean.isError())
		{
			response.setResponseStatus(FAILED);
			response.setErrorMsg(FAILED_MSG);
			response.setErrorCode(resultBean.getErrorCode());
		}
		else
		{
			response.setResultInteger(resultBean.getResultInteger());
		}
		return CommonUtil.convertIntoJson(response);
	}

	@RequestMapping(value = "/updateAgentAction", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String updateAgentAction(@RequestBody AgentModel staff)
	{
		ServiceResponse response = new ServiceResponse();
		ResultBean resultBean = agentServices.updateStaff(staff);
		if (resultBean.isError())
		{
			response.setResponseStatus(FAILED);
			response.setErrorMsg(FAILED_MSG);
			response.setErrorCode(resultBean.getErrorCode());
		}
		
		return CommonUtil.convertIntoJson(response);
	}
	
	
	@RequestMapping(value = "/agentSearchResult", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String agentSearchResult(@RequestBody AgentModel agentModel)
	{
		ServiceResponse response = new ServiceResponse();
		String jsonString;

		ResultBean resultBean = agentServices.searchAgentList(agentModel);
		if (resultBean.isError())
		{
			response.setResponseStatus(FAILED);
			response.setErrorMsg(FAILED_MSG);
			response.setErrorCode(resultBean.getErrorCode());
		}
		else
		{
			response.setResultList(resultBean.getResultList());
		}
		jsonString = CommonUtil.convertIntoJson(response);

		return jsonString;
	}

	@RequestMapping(value = "/agentCreditLimitDetails", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public String agentUserDetails(@RequestBody AgentModel agentModel) {
	
		ServiceResponse response = new ServiceResponse();
		String jsonString;
		
		ResultBean resultBean = agentServices.searchAgentCreditLimitList(agentModel);
		if (resultBean.isError())
		{
			response.setResponseStatus(FAILED);
			response.setErrorMsg(FAILED_MSG);
			response.setErrorCode(resultBean.getErrorCode());
		}
		else
		{		
			response.setResultList(resultBean.getResultList());
		}
		jsonString = CommonUtil.convertIntoJson(response);

		return jsonString;
    }
	
	@RequestMapping(value = "/updateAgentSetting", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "siteId")
	public String updateAgentSetting(@RequestBody GroupModal groupModel)
	{
		ServiceResponse response = new ServiceResponse();
		String jsonString;

		ResultBean resultBean = agentServices.updateAgentSetting(groupModel);
		if (resultBean.isError())
		{
			response.setResponseStatus(FAILED);
			response.setErrorMsg(FAILED_MSG);
			response.setErrorCode(resultBean.getErrorCode());
		}
		else
		{
			response.setResultObject(resultBean.getResultObject());
		}
		jsonString = CommonUtil.convertIntoJson(response);

		return jsonString;
	}

}