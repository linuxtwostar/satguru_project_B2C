package com.tt.ts.rest.dashboard.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.tt.nc.core.dao.GenericDAOImpl;
import com.tt.ts.rest.common.util.QueryConstantRest;
import com.tt.ts.rest.corporate.model.CorporateModel;
import com.tt.ts.rest.dashboard.model.AgentToDoListModel;
import com.tt.ts.rest.dashboard.model.DashboardReportBean;
import com.ws.services.util.CallLog;

@Repository
public class DashboardDaoImpl extends  GenericDAOImpl<Object, Long>   implements DashboardDao 
{

	@SuppressWarnings("unchecked")
	@Override
    public List<DashboardReportBean> dashboardReportGetRest(DashboardReportBean dashreport) throws Exception {
		List<DashboardReportBean> dashreportlList =null;
		List<Object> parameterList = new ArrayList<>();
		StringBuilder stringQuery =null;
		
		if(dashreport.getReportFrom().equals("0")){
			stringQuery = new StringBuilder(QueryConstantRest.FETCH_DASHBOARDREPORT_FLIGHT_BY_CLIENT_ID);
			if(dashreport.getSelRepFlightType().equals("1")) {
				stringQuery.append("-365");
			}else if(dashreport.getSelRepFlightType().equals("2")) {
				stringQuery.append("-30");
			}else if(dashreport.getSelRepFlightType().equals("3")) {
				stringQuery.append("-7");
			}else{
				stringQuery.append("-0");
			}
			stringQuery.append(" DAY)) AND DATE(NOW()) ");
			if(dashreport.getReportFrom() != null) {
			    stringQuery.append(" AND C.ORDER_TYPE =  "+dashreport.getReportFrom()+ " ");
			    //parameterList.add(dashreport.getReportFrom());
			}
			if(dashreport.getUserType()!=null && dashreport.getUserType().equals("agent") && dashreport.getAgentId() != null) {
			    stringQuery.append(" AND B.CREATED_BY = "+dashreport.getAgentId()+" ");
			    //parameterList.add(dashreport.getAgentId());
			    stringQuery.append(" AND C.AGENCY_ID = '"+dashreport.getAgencyCode()+"' ");
			}
			if(dashreport.getUserType()!=null && dashreport.getUserType().equals("manager") && dashreport.getAgencyCode() != null) {
			    stringQuery.append(" AND C.AGENCY_ID = '"+dashreport.getAgencyCode()+"' ");
			    //parameterList.add(dashreport.getAgencyCode());
			}
			stringQuery.append(" GROUP BY B.BOOKING_STATUS ");
	   }else if(dashreport.getReportFrom().equals("1")){
			stringQuery = new StringBuilder(QueryConstantRest.FETCH_DASHBOARDREPORT_HOTEL_BY_CLIENT_ID);
			if(dashreport.getSelRepHotelType().equals("1")) {
				stringQuery.append("-365");
			}else if(dashreport.getSelRepHotelType().equals("2")) {
				stringQuery.append("-30");
			}else if(dashreport.getSelRepHotelType().equals("3")) {
				stringQuery.append("-7");
			}else{
				stringQuery.append("-0");
			}
			stringQuery.append(" DAY)) AND DATE(NOW()) ");
			if(dashreport.getReportFrom() != null) {
			    stringQuery.append(" AND C.ORDER_TYPE = "+ " "+dashreport.getReportFrom());
			    //parameterList.add(dashreport.getReportFrom());
			}
			if(dashreport.getUserType()!=null && dashreport.getUserType().equals("agent") && dashreport.getAgentId() != null) {
			    stringQuery.append(" AND B.USER_ID = "+ " "+dashreport.getAgentId());
			    //parameterList.add(dashreport.getAgentId());
			    stringQuery.append(" AND C.AGENCY_ID = '"+dashreport.getAgencyCode()+"' ");
			}
			if(dashreport.getUserType()!=null && dashreport.getUserType().equals("manager") && dashreport.getAgencyCode() != null) {
			    stringQuery.append(" AND C.AGENCY_ID = '"+dashreport.getAgencyCode()+"' ");
			    //parameterList.add(dashreport.getAgencyCode());
			}
			stringQuery.append(" GROUP BY B.BOOKING_STATUS ");
	   }else if(dashreport.getReportFrom().equals("3")){
			stringQuery = new StringBuilder(QueryConstantRest.FETCH_DASHBOARDREPORT_INSURANCE_BY_CLIENT_ID);
			if(dashreport.getSelRepInsuranceType().equals("1")) {
				stringQuery.append("-365");
			}else if(dashreport.getSelRepInsuranceType().equals("2")) {
				stringQuery.append("-30");
			}else if(dashreport.getSelRepInsuranceType().equals("3")) {
				stringQuery.append("-7");
			}else{
				stringQuery.append("-0");
			}
			stringQuery.append(" DAY)) AND DATE(NOW()) ");
			if(dashreport.getReportFrom() != null) {
			    stringQuery.append(" AND C.ORDER_TYPE = "+ dashreport.getReportFrom()+ " ");
			    //parameterList.add(dashreport.getReportFrom());
			}
			if(dashreport.getUserType()!=null && dashreport.getUserType().equals("agent") && dashreport.getAgentId() != null) {
			    stringQuery.append(" AND B.CREATED_BY = "+dashreport.getAgentId()+" ");
			    //parameterList.add(dashreport.getAgentId());
			    stringQuery.append(" AND C.AGENCY_ID = '"+dashreport.getAgencyCode()+"' ");
			}
			if(dashreport.getUserType()!=null && dashreport.getUserType().equals("manager") && dashreport.getAgencyCode() != null) {
			    stringQuery.append(" AND C.AGENCY_ID = '"+dashreport.getAgencyCode()+"' ");
			    //parameterList.add(dashreport.getAgencyCode());
			}
			stringQuery.append(" GROUP BY B.BOOKING_STATUS ");
	   }
		
		if(stringQuery !=null){
			CallLog.info(26, "dashboardReportGetRest Query<> "+stringQuery.toString());
		}
		
		dashreportlList = fetchWithSQL(stringQuery.toString(), parameterList);
	  return dashreportlList;
    }
	
    @Override
    public List<AgentToDoListModel> getToDoListRest(AgentToDoListModel agentToDoListModel) throws Exception {
	List<AgentToDoListModel> agentToDoListModelList;
	List<Object> parameterList = new ArrayList<>();
	StringBuilder stringQuery = new StringBuilder(QueryConstantRest.FETCH_TODOLIST_BY_CLIENT_ID);
	
	
	if(agentToDoListModel.getCreatedByAgentId() != null) {
	    stringQuery.append("AND tdom.createdByAgentId = ? ");
	    parameterList.add(agentToDoListModel.getCreatedByAgentId());
	}
	if (agentToDoListModel.getStatus() != null) {
	    stringQuery.append("AND tdom.status = ? ");
	    parameterList.add(agentToDoListModel.getStatus());
	}
	agentToDoListModelList = fetchWithHQL(stringQuery.toString(), parameterList);
	return agentToDoListModelList;
    }
	
	@Override
    public AgentToDoListModel saveToDoList(AgentToDoListModel agentToDoListModel) throws Exception {

	Session session = getSessionFactory().openSession();
	Transaction tx = session.beginTransaction();
	try {
	    if (null != tx) {
		session.saveOrUpdate(agentToDoListModel);
		tx.commit();
	    }
	} catch (HibernateException e) {
	    if (null != tx)
		tx.rollback();
	    e.printStackTrace();
	    throw e;
	} finally {
	    session.flush();
	    session.close();
	}
	return agentToDoListModel;
    }
	
	public List<DashboardReportBean> getLatestTransactionData(int userId,String userType,String agencyCode) throws Exception
	{
		//String query = "select a.order_id, a.order_type, a.order_total from  TT_TS_AGENT_ORDER a  order by a.creation_time desc limit 5";
		String query ="";
		List<Object> parameterList = new ArrayList<>();
		if(userType.equals("agent"))
		 //query ="select * from (select a.order_id, a.order_type, a.order_total, b.FB_BOOKING_REF_NO, a.creation_time from  TT_TS_AGENT_ORDER a INNER JOIN TT_TS_FLIGHT_BOOK b on a.order_id=b.order_id where a.ORDER_TYPE = 0 and a.created_by="+userId+" union select a.order_id, a.order_type, a.order_total, c.BOOKING_REF_NO, a.creation_time from  TT_TS_AGENT_ORDER a INNER JOIN TT_HOTELS_BOOK c  on a.order_id=c.order_id where ORDER_TYPE = 1 and a.created_by="+userId+" union select a.order_id, a.order_type, a.order_total, b.IN_BOOKING_REF_NO, a.creation_time from  TT_TS_AGENT_ORDER a INNER JOIN TT_TS_INSURANCE_BOOKING b on a.order_id=b.order_id where ORDER_TYPE = 3 and a.created_by="+userId+") d order by creation_time desc limit 5";
			query ="select * from (select a.order_id, a.order_type, a.order_total, b.FB_BOOKING_REF_NO,a.agency_id,a.branch_id, a.creation_time, b.currency, a.FACTS_ID from  TT_TS_AGENT_ORDER a INNER JOIN TT_TS_FLIGHT_BOOK b on a.order_id=b.order_id where a.ORDER_TYPE = 0 and a.created_by="+userId+" union select a.order_id, a.order_type, a.order_total, c.BOOKING_REF_NO,a.agency_id,a.branch_id, a.creation_time, c.currency_code,  a.FACTS_ID from  TT_TS_AGENT_ORDER a INNER JOIN TT_HOTELS_BOOK c  on a.order_id=c.order_id where ORDER_TYPE = 1 and a.created_by="+userId+" union select a.order_id, a.order_type, a.order_total, b.IN_BOOKING_REF_NO,a.agency_id,a.branch_id, a.creation_time, b.currency_id,  a.FACTS_ID from  TT_TS_AGENT_ORDER a INNER JOIN TT_TS_INSURANCE_BOOKING b on a.order_id=b.order_id where ORDER_TYPE = 3 and a.created_by="+userId+" union SELECT TT_TS_AGENT_ORDER.ORDER_ID,TT_TS_AGENT_ORDER.ORDER_TYPE, TT_TS_INSURANCE_BOOKING.TOTAL_AMOUNT , TT_TS_FLIGHT_BOOK.FB_BOOKING_REF_NO, TT_TS_INSURANCE_BOOKING.IN_BOOKING_REF_NO, TT_TS_FLIGHT_BOOK.TOTAL_AGENT_AMT_TO_RECEIVE, TT_TS_AGENT_ORDER.CREATION_TIME, TT_TS_FLIGHT_BOOK.CURRENCY, TT_TS_INSURANCE_BOOKING.CURRENCY_ID  FROM    (   TT_TS_AGENT_ORDER TT_TS_AGENT_ORDER INNER JOIN TT_TS_INSURANCE_BOOKING TT_TS_INSURANCE_BOOKING ON (TT_TS_AGENT_ORDER.ORDER_ID = TT_TS_INSURANCE_BOOKING.ORDER_ID)) INNER JOIN TT_TS_FLIGHT_BOOK TT_TS_FLIGHT_BOOK ON (TT_TS_AGENT_ORDER.ORDER_ID = TT_TS_FLIGHT_BOOK.ORDER_ID) WHERE (TT_TS_AGENT_ORDER.ORDER_TYPE = 11 and TT_TS_AGENT_ORDER.CREATED_BY="+userId+")) d order by creation_time desc limit 5";
		else if(userType.equals("manager"))
		 //query ="select * from (select a.order_id, a.order_type, a.order_total, b.FB_BOOKING_REF_NO, a.creation_time from  TT_TS_AGENT_ORDER a INNER JOIN TT_TS_FLIGHT_BOOK b on a.order_id=b.order_id where a.ORDER_TYPE = 0 and a.agency_id='"+agencyCode+"' union select a.order_id, a.order_type, a.order_total, c.BOOKING_REF_NO, a.creation_time from  TT_TS_AGENT_ORDER a INNER JOIN TT_HOTELS_BOOK c  on a.order_id=c.order_id where ORDER_TYPE = 1 and a.agency_id='"+agencyCode+"' union select a.order_id, a.order_type, a.order_total, b.IN_BOOKING_REF_NO, a.creation_time from  TT_TS_AGENT_ORDER a INNER JOIN TT_TS_INSURANCE_BOOKING b on a.order_id=b.order_id where ORDER_TYPE = 3 and a.agency_id='"+agencyCode+"') d order by creation_time desc limit 5";
			query ="select * from (select a.order_id, a.order_type, a.order_total, b.FB_BOOKING_REF_NO,a.agency_id,a.branch_id, a.creation_time, b.currency, a.FACTS_ID from  TT_TS_AGENT_ORDER a INNER JOIN TT_TS_FLIGHT_BOOK b on a.order_id=b.order_id where a.ORDER_TYPE = 0 and a.agency_id='"+agencyCode+"' union select a.order_id, a.order_type, a.order_total, c.BOOKING_REF_NO,a.agency_id,a.branch_id, a.creation_time, c.currency_code,  a.FACTS_ID from  TT_TS_AGENT_ORDER a INNER JOIN TT_HOTELS_BOOK c  on a.order_id=c.order_id where ORDER_TYPE = 1 and a.agency_id='"+agencyCode+"' union select a.order_id, a.order_type, a.order_total, b.IN_BOOKING_REF_NO,a.agency_id,a.branch_id, a.creation_time, b.currency_id,  a.FACTS_ID from  TT_TS_AGENT_ORDER a INNER JOIN TT_TS_INSURANCE_BOOKING b on a.order_id=b.order_id where ORDER_TYPE = 3 and a.agency_id='"+agencyCode+"' union SELECT TT_TS_AGENT_ORDER.ORDER_ID,TT_TS_AGENT_ORDER.ORDER_TYPE, TT_TS_INSURANCE_BOOKING.TOTAL_AMOUNT , TT_TS_FLIGHT_BOOK.FB_BOOKING_REF_NO, TT_TS_INSURANCE_BOOKING.IN_BOOKING_REF_NO, TT_TS_FLIGHT_BOOK.TOTAL_AGENT_AMT_TO_RECEIVE, TT_TS_AGENT_ORDER.CREATION_TIME, TT_TS_FLIGHT_BOOK.CURRENCY, TT_TS_INSURANCE_BOOKING.CURRENCY_ID  FROM    (   TT_TS_AGENT_ORDER TT_TS_AGENT_ORDER INNER JOIN TT_TS_INSURANCE_BOOKING TT_TS_INSURANCE_BOOKING ON (TT_TS_AGENT_ORDER.ORDER_ID = TT_TS_INSURANCE_BOOKING.ORDER_ID)) INNER JOIN TT_TS_FLIGHT_BOOK TT_TS_FLIGHT_BOOK ON (TT_TS_AGENT_ORDER.ORDER_ID = TT_TS_FLIGHT_BOOK.ORDER_ID) WHERE (TT_TS_AGENT_ORDER.ORDER_TYPE = 11 and TT_TS_AGENT_ORDER.agency_id='"+agencyCode+"')) d order by creation_time desc limit 5";
		List<DashboardReportBean> respList = fetchWithSQL(query,parameterList);
		if(null!=respList)
			CallLog.info(26, "getLatestTransactionData response list from database:: "+respList.size());
		else
			CallLog.info(26, "getLatestTransactionData response list from database::null");
		return respList;
	}
	
	@Override
	public List<Object> checkSpPnrDate(String spPNR) throws Exception
	{
		String query= QueryConstantRest.CHECKSP_PNR;
		List<Object> parList = new ArrayList<>();
		parList.add(spPNR);
		parList.add(new Date());
		return  fetchWithSQL(query, parList);
	}

}
