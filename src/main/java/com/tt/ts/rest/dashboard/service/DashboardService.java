package com.tt.ts.rest.dashboard.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tt.nc.common.util.TTLog;
import com.tt.ts.common.enums.manager.EnumManager;
import com.tt.ts.common.enums.model.EnumModel;
import com.tt.ts.common.modal.ResultBean;
import com.tt.ts.common.util.CommonUtil;
import com.tt.ts.geolocations.model.CountryBean;
import com.tt.ts.rest.corporate.model.CorporateModel;
import com.tt.ts.rest.dashboard.manager.DashboardManager;
import com.tt.ts.rest.dashboard.model.AgentToDoListModel;
import com.tt.ts.rest.dashboard.model.DashboardReportBean;

@Service
public class DashboardService
{
	@Autowired
	private DashboardManager dashboardManager;
	
	@Autowired
	private EnumManager enumManager;
	
	public ResultBean getLatestTransactionData(DashboardReportBean dashreport) {
		ResultBean resultBean = new ResultBean();
		try {
		    List<DashboardReportBean> list = dashboardManager.getLatestTransactionData(dashreport.getAgentId(), dashreport.getUserType(),dashreport.getAgencyCode());
		    resultBean.setResultList(list);
		    resultBean.setIserror(false);
		} catch (Exception e) {
		    resultBean.setIserror(true);
		    TTLog.error(0, e);
		}
		return resultBean;
	    }
	
	public ResultBean dashboardReportGetRest(DashboardReportBean dashreport) {
		ResultBean resultBean = new ResultBean();
		try {
		    List<DashboardReportBean> list = dashboardManager.dashboardReportGetRest(dashreport);
		    resultBean.setResultList(list);
		    resultBean.setIserror(false);
		} catch (Exception e) {
		    resultBean.setIserror(true);
		    TTLog.error(0, e);
		}
		return resultBean;
	    }
	
	public ResultBean getToDoListRest(AgentToDoListModel agentToDoListModel) {
		ResultBean resultBean = new ResultBean();
		try {
		    List<AgentToDoListModel> list = dashboardManager.getToDoListRest(agentToDoListModel);
		    List<EnumModel> taskType = enumManager.getEnumList("AGENT_TODOLIST_TASK_DUE_TYPE");
		    if (list != null && !list.isEmpty() && taskType !=null) {
				for (AgentToDoListModel tdModel : list) {
				    if (tdModel.getTaskType() != null) {
				    	for (EnumModel enumModel : taskType)
						{
							if (tdModel.getTaskType().equals(enumModel.getIntCode()))
							{
								tdModel.setDueTypeName(enumModel.getValue());
								break;
							}
						}
				    }
				}
				//list.sort((p1, p2) -> p2.getTaskId()-(p1.getTaskId()));
				list.sort((p1, p2) -> p1.getDueDate().compareTo(p2.getDueDate()));
			}
		    
		    resultBean.setResultList(list);
		    resultBean.setIserror(false);
		} catch (Exception e) {
		    resultBean.setIserror(true);
		    TTLog.error(0, e);
		}
		return resultBean;
	    }

	public ResultBean saveToDoList(AgentToDoListModel agentToDoListModel) {
		ResultBean resultBean = new ResultBean();
		try {
			 
			agentToDoListModel.setStatus(1);
		
		    

			AgentToDoListModel todolistMdl = dashboardManager.saveToDoList(agentToDoListModel);

		    if (todolistMdl.getTaskId() != null && todolistMdl.getTaskId() > 0) {
			

		    }
		    resultBean.setIserror(false);
		    resultBean.setResultObject(todolistMdl);

		} catch (Exception e) {
		    resultBean.setIserror(true);
		    TTLog.error(0, e);
		}
		return resultBean;
	    }
	
	
	public ResultBean checkSpPnrDate(String spPNR)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			boolean existanceFlag= false;
			List<Object> listOfExistance = dashboardManager.checkSpPnrDate(spPNR);
			if(listOfExistance!=null && !listOfExistance.isEmpty())
				existanceFlag = true;
			
			resultBean.setResultBoolean(existanceFlag);
			resultBean.setIserror(false);
		}
		catch (Exception e)
		{
			TTLog.printStackTrace(0, e);
			resultBean.setIserror(true);
		}
		return resultBean;
	} 
}
