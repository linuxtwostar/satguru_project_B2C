package com.tt.ts.rest.dashboard;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.test.model.ServiceResponse;
import com.tt.nc.common.util.TTLog;
import com.tt.ts.common.modal.ResultBean;
import com.tt.ts.rest.common.util.CommonUtil;
import com.tt.ts.rest.dashboard.model.AgentToDoListModel;
import com.tt.ts.rest.dashboard.model.DashboardReportBean;
import com.tt.ts.rest.dashboard.service.DashboardService;

@RestController
@RequestMapping("/dashboard")
public class DashboardController	
{
	private static final String FAILED = "Failed";
	@Autowired
	private DashboardService dashboardService;
	
	@RequestMapping(value = "/dashboardLatestTxns", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public String dashboardLatestTxns(@RequestBody DashboardReportBean dashreport) {
	ServiceResponse response = new ServiceResponse();
	String jsonString = "";
	try {
	    ResultBean resultBean = dashboardService.getLatestTransactionData(dashreport);
	    if (resultBean.isError()) {
		response.setResponseStatus(FAILED);
		response.setErrorMsg("Something went wrong.");
		response.setErrorCode(resultBean.getErrorCode());
	    } else {
		response.setResultList(resultBean.getResultList());
	    }
	    ObjectMapper objMapper = new ObjectMapper();
	    jsonString = objMapper.writeValueAsString(response);
	} catch (Exception ex) {
	    TTLog.error(0, ex);
	}
	return jsonString;
    }
	@RequestMapping(value = "/dashboardreportgetrest", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public String dashboardReportGetRest(@RequestBody DashboardReportBean dashreport) {
	ServiceResponse response = new ServiceResponse();
	String jsonString = "";
	try {
	    ResultBean resultBean = dashboardService.dashboardReportGetRest(dashreport);
	    if (resultBean.isError()) {
		response.setResponseStatus(FAILED);
		response.setErrorMsg("Something went wrong.");
		response.setErrorCode(resultBean.getErrorCode());
	    } else {
		response.setResultList(resultBean.getResultList());
	    }
	    ObjectMapper objMapper = new ObjectMapper();
	    jsonString = objMapper.writeValueAsString(response);
	} catch (Exception ex) {
	    TTLog.error(0, ex);
	}
	return jsonString;
    }
	
	@RequestMapping(value = "/getToDoListRest", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public String getToDoListRest(@RequestBody AgentToDoListModel agentToDoListModel) {
	ServiceResponse response = new ServiceResponse();
	String jsonString = "";
	try {
	    ResultBean resultBean = dashboardService.getToDoListRest(agentToDoListModel);
	    if (resultBean.isError()) {
		response.setResponseStatus(FAILED);
		response.setErrorMsg("Something went wrong.");
		response.setErrorCode(resultBean.getErrorCode());
	    } else {
		response.setResultList(resultBean.getResultList());
	    }
	    ObjectMapper objMapper = new ObjectMapper();
	    jsonString = objMapper.writeValueAsString(response);
	} catch (Exception ex) {
	    TTLog.error(0, ex);
	}
	return jsonString;
    }
	
	
	@RequestMapping(value = "/saveToDoListRest", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public String saveToDoList(@RequestBody AgentToDoListModel agentToDoListModel) {
	ServiceResponse response = new ServiceResponse();
	String jsonString = "";
	try {
		
	    ResultBean resultBean = dashboardService.saveToDoList(agentToDoListModel);
	    if (resultBean.isError()) {
		response.setResponseStatus(FAILED);
		response.setErrorMsg("Error to save agent to do list");
		response.setErrorCode(resultBean.getErrorCode());
	    } else {
		response.setResultObject(resultBean.getResultObject());
	    }
	    ObjectMapper objMapper = new ObjectMapper();
	    jsonString = objMapper.writeValueAsString(response);
	} catch (Exception ex) {
	    TTLog.error(0, ex);
	}
	return jsonString;
    }
	
	@RequestMapping(value = "/checkPNRExistance", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String checkPNRExistance(@RequestBody String spPNR, HttpServletRequest request)
	{
		String jsonString;
		ResultBean resultBean = dashboardService.checkSpPnrDate(spPNR);
		jsonString= CommonUtil.convertIntoJson(resultBean.getResultBoolean());
		return jsonString;
	}
}
