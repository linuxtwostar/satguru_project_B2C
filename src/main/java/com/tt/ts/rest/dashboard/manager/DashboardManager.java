package com.tt.ts.rest.dashboard.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tt.ts.rest.corporate.model.CorporateModel;
import com.tt.ts.rest.dashboard.dao.DashboardDao;
import com.tt.ts.rest.dashboard.model.AgentToDoListModel;
import com.tt.ts.rest.dashboard.model.DashboardReportBean;

@Component
public class DashboardManager
{
	@Autowired
	private DashboardDao dashboardDao;
	
	public List<DashboardReportBean> dashboardReportGetRest(DashboardReportBean dashreport) throws Exception {
		return dashboardDao.dashboardReportGetRest(dashreport);
	}
	public List<AgentToDoListModel> getToDoListRest(AgentToDoListModel agentToDoListModel) throws Exception {
		return dashboardDao.getToDoListRest(agentToDoListModel);
	}
	public AgentToDoListModel saveToDoList(AgentToDoListModel agentToDoListModel) throws Exception {
		return dashboardDao.saveToDoList(agentToDoListModel);
	}
	
	public List<DashboardReportBean> getLatestTransactionData(int userId, String userType, String agencyCode) throws Exception
	{
		return dashboardDao.getLatestTransactionData(userId,userType,agencyCode);
	
	}
	
	public List<Object> checkSpPnrDate(String spPNR) throws Exception
	{
		return dashboardDao.checkSpPnrDate(spPNR);
	}
}
