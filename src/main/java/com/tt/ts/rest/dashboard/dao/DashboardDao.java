package com.tt.ts.rest.dashboard.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tt.ts.rest.corporate.model.CorporateModel;
import com.tt.ts.rest.dashboard.model.AgentToDoListModel;
import com.tt.ts.rest.dashboard.model.DashboardReportBean;

@Repository
public interface DashboardDao
{
	List<DashboardReportBean> dashboardReportGetRest(DashboardReportBean dashreport) throws Exception;
	
	List<AgentToDoListModel> getToDoListRest(AgentToDoListModel agentToDoListModel) throws Exception;
	
	AgentToDoListModel saveToDoList(AgentToDoListModel staff) throws Exception;
	
	List<DashboardReportBean> getLatestTransactionData(int userId, String userType, String agencyCode) throws Exception;
	
	List<Object> checkSpPnrDate(String spPNR) throws Exception;

}
