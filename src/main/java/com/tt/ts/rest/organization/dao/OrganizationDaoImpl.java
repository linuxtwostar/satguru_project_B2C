package com.tt.ts.rest.organization.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.tt.nc.common.util.QueryConstant;
import com.tt.nc.common.util.TTLog;
import com.tt.nc.core.dao.GenericDAOImpl;
import com.tt.ts.airline.model.AirlineModel;
import com.tt.ts.airport.model.AirportModal;
import com.tt.ts.blacklist.model.BlackListFlightCountryPosModel;
import com.tt.ts.blacklist.model.BlackOutFlightModel;
import com.tt.ts.bsp.model.BspCommissionModel;
import com.tt.ts.common.enums.model.EnumModel;
import com.tt.ts.currency.model.CurrencyBean;
import com.tt.ts.flighttag.model.FlightTagModel;
import com.tt.ts.flighttag.model.TagFlightCountryModel;
import com.tt.ts.gdsdealcode.model.GDSDealCodeModel;
import com.tt.ts.geolocations.model.CountryBean;
import com.tt.ts.knowledgecenter.model.KnowledgeCenterAgencyModel;
import com.tt.ts.knowledgecenter.model.KnowledgeCenterBranchModel;
import com.tt.ts.knowledgecenter.model.KnowledgeCenterCountryModel;
import com.tt.ts.knowledgecenter.model.KnowledgeCenterDestinationCityModel;
import com.tt.ts.knowledgecenter.model.KnowledgeCenterModel;
import com.tt.ts.organization.model.OrganizationAirlineCodeModal;
import com.tt.ts.organization.model.OrganizationDocumentModel;
import com.tt.ts.organization.model.OrganizationFinancialModel;
import com.tt.ts.payment.model.PaymentMode;
import com.tt.ts.product.model.ProductModel;
import com.tt.ts.rest.common.util.CommonUtil;
import com.tt.ts.rest.common.util.QueryConstantRest;
import com.tt.ts.supplier.modal.SupplierModal;
import com.tt.ts.suppliercredential.modal.SupplierCredentialModal;

@Repository(value = "orgDaoRest")
public class OrganizationDaoImpl extends GenericDAOImpl<Object, Long> implements OrganizationDao
{
	@Override
	public List<ProductModel> getProductById(Integer groupId, String productName) throws Exception
	{
		String query = QueryConstantRest.FETCH_PRODUCT_BY_GROUPID;

		List<Object> paraList = new ArrayList<>();
		paraList.add(groupId);
		if (productName != null)
		{
			query += " AND pm.productName = ? ";
			paraList.add(productName);
		}
		return fetchWithHQL(query, paraList);
	}

	@Override
	public List<SupplierModal> getSupplierById(Integer groupId, Integer productId) throws Exception
	{
		String query = QueryConstantRest.FETCH_SUPPLIER_BY_GROUPID;
		List<Object> paraList = new ArrayList<>();
		paraList.add(groupId);
		paraList.add(productId);
		return fetchWithHQL(query, paraList);
	}

	@Override
	public List<Object> getCurrencyCode(String credentialId) throws Exception
	{
		String searchQuery;
		searchQuery = "SELECT TTCURR.CURRENCY_CODE FROM TT_TS_SUPPLIER_CREDENTIAL TSCR,TT_CURRENCY TTCURR WHERE TTCURR.CURRENCY_ID = TSCR.CURRENCY_ID AND TSCR.CREDENTIAL_ID='"+credentialId+"'";
		return fetchWithSQL(searchQuery,null);
	}
	
	@Override
	public List<Object> getProductSupplierCredentialId(String agencyCode,
			Integer productId, Integer supplierId, String orgType) throws Exception
	{
		String searchQuery;
		if(orgType.equals("Branch")){
		searchQuery = "SELECT CREDENTIAL_ID FROM TT_ORG_PRODUCTS_SUPPLIER_MAP WHERE ORGANIZATION_ID='"+agencyCode+"' AND PRODUCT_ID='"+productId+"' AND SUPPLIER_ID='"+supplierId+"' AND FACT_APPROVAL_STATUS=1 ";
		}
		else{
			searchQuery = "SELECT CREDENTIAL_ID FROM TT_ORG_PRODUCTS_SUPPLIER_MAP WHERE ORGANIZATION_ID='"+agencyCode+"' AND PRODUCT_ID='"+productId+"' AND SUPPLIER_ID='"+supplierId+"'";
		}
		return fetchWithSQL(searchQuery,null);
	}
	
	@Override
	public List<SupplierCredentialModal> getCredentialById(Integer groupId, Integer productId, Integer supplierId) throws Exception
	{
		String query = QueryConstantRest.FETCH_CREDENTIAL_BY_GROUPID;
		List<Object> paraList = new ArrayList<>();
		paraList.add(groupId);
		paraList.add(productId);
		paraList.add(supplierId);

		return fetchWithHQL(query, paraList);
	}

	@Override
	public List<SupplierModal> getSupplierCredentialById(Integer groupId) throws Exception
	{
		String query = QueryConstantRest.FETCH_SUPPLIER_PRODUCT_BY_GROUPID;
		List<Object> paraList = new ArrayList<>();
		paraList.add(groupId);
		return fetchWithHQL(query, paraList);
	}

	@Override
	public List<Object[]> getSupplierCredentialById(Integer groupId, Integer productId, Integer supplierId,String credentialId) throws Exception
	{
		String query = QueryConstantRest.FETCH_CREDENTIAL_SUPPLIER_BY_CREDID;
		List<Object> paraList = new ArrayList<>();
		paraList.add(groupId);
		paraList.add(productId);
		paraList.add(supplierId);
		paraList.add(credentialId);
		return fetchWithHQL(query, paraList);
	}
	
	@Override
	public List<Object[]> getProductSupplierCredentialById(Integer groupId, Integer productId, Integer supplierId) throws Exception
	{
		String query = QueryConstantRest.FETCH_CREDENTIAL_SUPPLIER_PRODUCT_GROUPID;
		List<Object> paraList = new ArrayList<>();
		paraList.add(groupId);
		paraList.add(productId);
		paraList.add(supplierId);
		return fetchWithHQL(query, paraList);
	}
	
	@SuppressWarnings("unchecked")
    @Override
    public List<CurrencyBean> fetchCurrencyById(Integer id) throws Exception {
	String query = QueryConstantRest.FETCH_CURRENCY_BY_ID;
	List<Object> paraList = new ArrayList<Object>();
	paraList.add(id);
	List<CurrencyBean> quotationModelList = fetchWithHQL(query, paraList);
	return quotationModelList;
    }

	@Override
	public List<Object> getCompanyDetailsById(Integer userId, Integer organizationType) throws Exception
	{
		StringBuilder query =new StringBuilder("");
		if(organizationType==3){
			query.append(QueryConstantRest.FETCH_GROUP_BY_USER_ID);
			query.append(" AND u.USER_ID= ? ");
			query.append(" AND om.ORGANIZATION_TYPE=? ");
		}else if(organizationType==2){
			query.append(QueryConstantRest.FETCH_BRN_GROUP_BY_GROUP_ID);
			query.append(" AND gm.GROUP_ID =? ");
			query.append(" AND om.ORGANIZATION_TYPE=? ");
		}
		List<Object> paraList = new ArrayList<>();
		paraList.add(userId);
		paraList.add(organizationType);
		return fetchWithSQL(query.toString(), paraList);
	}
	
	@Override
	public List<Object> getCompanyDetailsByAgentId(Integer userId, Integer organizationType) throws Exception
	{
		String query = QueryConstantRest.FETCH_GROUP_BY_AGENT_ID;
		List<Object> paraList = new ArrayList<>();
		paraList.add(userId);
		paraList.add(organizationType);
		return fetchWithSQL(query, paraList);
	}

	@Override
	public List<Object> fetchOrganizationDetails(Integer groupId,String agencyCode) throws Exception
	{
		String query=null;
		if(agencyCode.contains("AGN")){
			query = QueryConstantRest.FETCH_COUNTRY_DETAIL_BY_GROUPID;
		}else{
			query = QueryConstantRest.FETCH_COUNTRY_DETAIL_BY_GROUPID_BRN;
		}
		List<Object> paraList = new ArrayList<>();
		paraList.add(groupId);
		return fetchWithHQL(query, paraList);
	}

	@Override
	public List<OrganizationFinancialModel> fetchAgencyCreditLimit(Integer groupId) throws Exception
	{

		String query = QueryConstantRest.FETCH_AGENCY_CREDIT_LIMIT;
		List<Object> paraList = new ArrayList<>();
		paraList.add(groupId);
		return fetchWithHQL(query, paraList);

	}

	@Override
	public List<Object[]> searchAirportData(AirportModal airportModel) throws Exception
	{
		String searchQuery;
		searchQuery = "SELECT  TMP1.AIRPORT_ID, TMP1.AIRPORT_CODE, TMP1.AIRPORT_NAME, TMP1.COUNTRY_NAME,TMP1.CITY_NAME,TMP1.COUNTRY_ID,TMP1.CITY_ID,TMP1.COUNTRY_CODE, TMP1.STATION_TYPE,TMP1.STATUS,TMP1.APPROVAL_STATUS,TMP1.LAST_MOD_TIME "
		+"FROM(SELECT TT.AIRPORT_ID, TT.AIRPORT_CODE, TT.AIRPORT_NAME, TT_COUNTRY.COUNTRY_NAME, TT_CITY.CITY_NAME, TT_COUNTRY.COUNTRY_ID, TT_CITY.CITY_ID,TT_COUNTRY.COUNTRY_CODE, "
		+"TT.STATION_TYPE,TT.STATUS,TT.APPROVAL_STATUS,TT.LAST_MOD_TIME FROM TT_AIRPORT_DETAILS TT  LEFT JOIN TT_CITY TT_CITY ON TT.CITY_ID = TT_CITY.CITY_ID LEFT JOIN TT_COUNTRY TT_COUNTRY ON TT.COUNTRY_ID = TT_COUNTRY.COUNTRY_ID WHERE "
		+"LOWER(TT.AIRPORT_CODE) LIKE LOWER('% " + airportModel.getAirportCode() + "%') OR LOWER(TT.AIRPORT_CODE) LIKE LOWER('" + airportModel.getAirportCode() + "%') OR LOWER(TT.AIRPORT_NAME) LIKE LOWER('% " + airportModel.getAirportCode() + "%') OR LOWER(TT.AIRPORT_NAME) LIKE LOWER('" + airportModel.getAirportCode() + "%') OR LOWER(TT_COUNTRY.COUNTRY_NAME) LIKE LOWER('% " + airportModel.getAirportCode() + "%') OR LOWER(TT_COUNTRY.COUNTRY_NAME) LIKE LOWER('" + airportModel.getAirportCode() + "%') "
		+"OR LOWER(TT_CITY.CITY_NAME) LIKE LOWER('% " + airportModel.getAirportCode() + "%') OR LOWER(TT_CITY.CITY_NAME) LIKE LOWER('" + airportModel.getAirportCode() + "%') AND (TT.STATUS = " + airportModel.getStatus() + ")AND (TT.APPROVAL_STATUS = " + airportModel.getApprovalStatus() + ")) TMP1 WHERE TMP1.AIRPORT_ID NOT IN (SELECT AIRPORT_ID FROM TT_RESTRICTED_AIRP_COUNTRY WHERE  COUNTRY_ID = " + airportModel.getAirportCountryMappings().get(0).getCountryId() + ") AND TMP1.STATUS=1 AND TMP1.APPROVAL_STATUS=1 ORDER BY TMP1.LAST_MOD_TIME DESC";
		return fetchWithSQL(searchQuery);
	}

	@Override
	public List<AirlineModel> getAirlinesByAgencyId(Integer groupId, Integer domOrIntl) throws Exception
	{
		List<AirlineModel> list = new ArrayList<>();
		if (groupId > 0)
		{
			StringBuilder query = new StringBuilder(QueryConstantRest.FETCH_AIRLINE_LIST_BY_AGENCY_ID);
			query.append(" order by am.airlineName asc");
			List<Object> paraList = new ArrayList<>();
			paraList.add(domOrIntl);
			list = fetchWithHQL(query.toString(), paraList);
		}
		return list;
	}

	@Override
	public List<OrganizationDocumentModel> getProfileImage(Integer groupId) throws Exception
	{
		List<OrganizationDocumentModel> list = new ArrayList<>();
		if (groupId != null)
		{
			String query = QueryConstantRest.FETCH_PROFILE_IMAGE;
			List<Object> paraList = new ArrayList<>();
			paraList.add(groupId);
			paraList.add("LOGO");
			list = fetchWithHQL(query, paraList);
		}
		return list;
	}

	@Override
	public List<Object> getMarkupRecordByUser(Integer organizationId, int corpOrRetail, Integer corpId) throws Exception
	{
		List<Object> list = new ArrayList<>();
		if (organizationId != null)
		{
			String query = QueryConstantRest.FETCH_MARKUP_RECORD;
			List<Object> paraList = new ArrayList<>();
			paraList.add(organizationId);
			paraList.add(corpOrRetail);
			paraList.add(corpId);
			list = fetchWithHQL(query, paraList);
		}
		return list;
	}

	@Override
	public List<Object> getRestrictedAirlineCredential(Integer credentialId) throws Exception
	{
		String query = QueryConstantRest.FETCH_CREDENTIAL_REST_AIRLINE;
		List<Object> paraList = new ArrayList<>();
		paraList.add(credentialId);
		return fetchWithHQL(query, paraList);
	}

	@Override
	public List<Object> gdsSearchSupplierCredential(Integer groupId, String orgType) throws Exception
	{
		String query =null;
		if(orgType.equals("Branch")){
		 query = QueryConstantRest.FETCH_GDS_SEARCH_CREDENTIAL;
		}
		else{
		 query = QueryConstantRest.FETCH_GDS_SEARCH_CREDENTIAL_AGENCY;
		}
		List<Object> paraList = new ArrayList<>();
		paraList.add(groupId);
		return fetchWithHQL(query, paraList);
	}

	@Override
	public List<Object> gdsTicketingSupplierCredential(Integer groupId,String orgType) throws Exception
	{
		String query= null;
		if(orgType.equals("Branch")){
			query = QueryConstantRest.FETCH_GDS_TICKET_CREDENTIAL;
		}else{
			query = QueryConstantRest.FETCH_GDS_TICKET_CREDENTIAL_AGENCY;
		}
		 
		List<Object> paraList = new ArrayList<>();
		paraList.add(groupId);
		return fetchWithHQL(query, paraList);
	}

	@Override
	public List<Object> fetchCredential(Integer crendentialId) throws Exception
	{
		String query = QueryConstantRest.FETCH_CREDENTIAL_NAME_BY_CREDENTIAL_ID;
		List<Object> paraList = new ArrayList<>();
		paraList.add(crendentialId);
		return fetchWithHQL(query, paraList);
	}

	@Override
	public List<Object> fetchDealDetails(Integer groupId, Integer credentialId) throws Exception
	{
		String query = QueryConstantRest.FETCH_DEALCODE_BY_AGENCY;
		List<Object> paraList = new ArrayList<>();
		paraList.add(groupId);
		paraList.add(credentialId);
		return fetchWithHQL(query, paraList);
	}

	@Override
	public List<SupplierModal> getGdsSupplierList(String userId) throws Exception
	{
		String query = QueryConstantRest.GET_GDS_SUPPLIER_LIST;
		List<Object> paraList = new ArrayList<>();
		paraList.add(userId);
		paraList.add(1); // Product Id
		paraList.add(1); // GDS Type
		paraList.add(1); // Status
		return fetchWithHQL(query, paraList);
	}

	@Override
	public List<OrganizationAirlineCodeModal> getRestrictedAirlineListByAgengy(String agencyId) throws Exception
	{
		String query = QueryConstantRest.GET_RESTRICTED_AIRLINE_LIST_BY_AGENCY;
		List<Object> paraList = new ArrayList<>();
		paraList.add(agencyId);
		return fetchWithHQL(query, paraList);
	}

	@Override
	public List<Object> getAgentType(Integer userId) throws Exception
	{
		String query = QueryConstantRest.GET_AGENT_TYPE;
		List<Object> paraList = new ArrayList<>();
		paraList.add(userId);
		return fetchWithHQL(query, paraList);
	}

	@Override
	public List<FlightTagModel> searchFlightTag(FlightTagModel flightTagModel) throws Exception
	{
		String searchQuery;
		if (flightTagModel != null && flightTagModel.getTagCountryPosList() != null && !flightTagModel.getTagCountryPosList().isEmpty())
		{
			searchQuery = QueryConstantRest.SEARCH_FLIGHT_TAG_COUNTRY;
		}
		else
		{
			searchQuery = QueryConstant.SEARCH_FLIGHT_TAG;
		}

		List<FlightTagModel> flightTagModels;
		List<Object> parameterList = new ArrayList<>();
		if (flightTagModel != null)
		{
			if (flightTagModel.getFlightId() != null && !flightTagModel.getFlightId().isEmpty())
			{
				searchQuery += " and  fld.flightId = '" + flightTagModel.getFlightId().trim() + "'";
			}
			if (flightTagModel.getAirLineId() != null && flightTagModel.getAirLineId() > 0)
			{
				searchQuery += " and fld.airLineId=?";
				parameterList.add(flightTagModel.getAirLineId());
			}
			if (flightTagModel.getStatus() > -1)
			{
				searchQuery += " and fld.status=?";
				parameterList.add(flightTagModel.getStatus());
			}
			if (flightTagModel.getApprovalStatus() != null)
			{
				searchQuery += " AND fld.approvalStatus=?";
				parameterList.add(flightTagModel.getApprovalStatus());
			}
			if (flightTagModel.getSearchFromDate() != null)
			{
				searchQuery += " AND '" + flightTagModel.getSearchFromDate() + "' BETWEEN fld.validFrom  and fld.validTo ";

			}
			if (flightTagModel.getSearchToDate() != null && !("").equalsIgnoreCase(flightTagModel.getSearchToDate()))
			{
				searchQuery += " AND  '" + flightTagModel.getSearchToDate() + "' BETWEEN fld.validFrom  and fld.validTo ";
			}

			if (flightTagModel.getFlightCountryList() != null && !flightTagModel.getFlightCountryList().isEmpty())
			{
				StringBuilder sb = new StringBuilder();
				int index = 0;
				for (TagFlightCountryModel companyId : flightTagModel.getFlightCountryList())
				{
					if (index == 0)
					{
						sb.append(companyId.getCountryId());
					}
					else
					{
						sb.append("," + companyId.getCountryId());
					}
					index++;
				}
				searchQuery += " AND cm.posId IN  (" + sb.toString() + ")";
			}
		}
		searchQuery += " ORDER BY fld.lastModTime DESC ";
		flightTagModels = fetchWithHQL(searchQuery, parameterList);
		return flightTagModels;
	}

	@Override
	public List<BlackOutFlightModel> searchBlackOutFlight(BlackOutFlightModel blackOutFlightModel) throws Exception
	{
		List<BlackOutFlightModel> blackOutList;
		String query;
		if (blackOutFlightModel != null && blackOutFlightModel.getBlackCountryPosList() != null && !blackOutFlightModel.getBlackCountryPosList().isEmpty())
		{
			query = QueryConstantRest.SEARCH_BLACKOUT_FLIGHT_COUNTRY;
		}
		else
		{
			query = QueryConstant.SEARCH_BLACKOUT_FLIGHT;
		}

		List<Object> parameterList = new ArrayList<>();
		if (blackOutFlightModel != null)
		{
			if (blackOutFlightModel.getAirlineId() != null && blackOutFlightModel.getAirlineId() > 0)
			{
				query += " and  bof.airlineId=?";
				parameterList.add(blackOutFlightModel.getAirlineId());
			}
			if (blackOutFlightModel.getFlightNo() != null && !blackOutFlightModel.getFlightNo().trim().isEmpty())
			{
				query += " and  bof.flightNo LIKE '%" + blackOutFlightModel.getFlightNo().trim() + "%'";
			}
			if (blackOutFlightModel.getSearchFromDate() != null)
			{
				query += " and  '" + blackOutFlightModel.getSearchFromDate() + "' BETWEEN  bof.validFrom  AND bof.validTo ";

			}
			if (blackOutFlightModel.getSearchToDate() != null && !blackOutFlightModel.getSearchToDate().equalsIgnoreCase("null"))
			{
				query += " and  '" + blackOutFlightModel.getSearchToDate() + "' BETWEEN  bof.validFrom  AND bof.validTo ";
			}

			if (blackOutFlightModel.getStatus() > -1)
			{
				query += " and bof.status=?";
				parameterList.add(blackOutFlightModel.getStatus());
			}
			if (blackOutFlightModel.getApprovalStatus() != null && !blackOutFlightModel.getApprovalStatus().equals(""))
			{
				query += " AND bof.approvalStatus=?";
				parameterList.add(blackOutFlightModel.getApprovalStatus());
			}
			if (blackOutFlightModel.getBlackCountryPosList() != null && !blackOutFlightModel.getBlackCountryPosList().isEmpty())
			{
				StringBuilder sb = new StringBuilder();
				int index = 0;
				for (BlackListFlightCountryPosModel countryId : blackOutFlightModel.getBlackCountryPosList())
				{
					if (index == 0)
					{
						sb.append(countryId.getPosId());
					}
					else
					{
						sb.append("," + countryId.getPosId());
					}
					index++;
				}
				query += " AND cbm.posId IN  (" + sb.toString() + ")";
			}
		}
		query += " ORDER BY  bof.lastModTime DESC";
		blackOutList = fetchWithHQL(query, parameterList);
		return blackOutList;

	}

	@Override
	public List<Object> getRBDListCountryWise(Integer countryId) throws Exception
	{
		String query = QueryConstantRest.GET_RBD_LIST_BY_COUNTRYID;
		List<Object> paraList = new ArrayList<>();
		paraList.add(countryId);
		return fetchWithHQL(query, paraList);
	}

	@Override
	public List<BspCommissionModel> getBspCommisisonList(BspCommissionModel bspCommissionModel) throws Exception
	{
		String searchProductQuery = QueryConstant.SEARCH_BSP_COMMISSION;
		List<Object> parameterList = new ArrayList<>();
		if (bspCommissionModel != null)
		{
			if (null != bspCommissionModel.getAirlineId() && bspCommissionModel.getAirlineId() > 0)
			{
				searchProductQuery = searchProductQuery + " and bcm.airlineId = ?";
				parameterList.add(bspCommissionModel.getAirlineId());
			}

			if (null != bspCommissionModel.getJourneyType() && bspCommissionModel.getJourneyType() != -1)
			{
				if(bspCommissionModel.getJourneyType()==1)
					searchProductQuery = searchProductQuery + " and (bcm.journeyType = ? OR bcm.journeyType = 4 OR bcm.journeyType = 5 OR bcm.journeyType = 7) ";
				else if(bspCommissionModel.getJourneyType()==2)
					searchProductQuery = searchProductQuery + " and (bcm.journeyType = ? OR bcm.journeyType = 4 OR bcm.journeyType = 6 OR bcm.journeyType = 7) ";
				else if(bspCommissionModel.getJourneyType()==3)
					searchProductQuery = searchProductQuery + " and (bcm.journeyType = ? OR bcm.journeyType = 5 OR bcm.journeyType = 6 OR bcm.journeyType = 7) ";
				parameterList.add(bspCommissionModel.getJourneyType());
			}
			if(bspCommissionModel.isHasAdult() && bspCommissionModel.isHasChild()){
				searchProductQuery = searchProductQuery + " and ( bcm.hasAdult =1 OR bcm.hasChild =1 ) ";
			}else if(bspCommissionModel.isHasAdult()){
				searchProductQuery = searchProductQuery + " and bcm.hasAdult =1 ";
			}else if(bspCommissionModel.isHasChild()){
				searchProductQuery = searchProductQuery + " and bcm.hasChild =1 ";
			}
			if (null != bspCommissionModel.getTicketPccId() && bspCommissionModel.getTicketPccId() > 0)
			{
				searchProductQuery = searchProductQuery + " and bcm.ticketPccId = ?";
				parameterList.add(bspCommissionModel.getTicketPccId());
			}
			if (null != bspCommissionModel.getTktFrmDate())
			{
				searchProductQuery = searchProductQuery + " and '" + CommonUtil.convertDateToString(bspCommissionModel.getTktFrmDate()) + "' BETWEEN bcm.tktFrmDate and bcm.tktToDate ";

			}
			if (null != bspCommissionModel.getTktToDate())
			{
				searchProductQuery = searchProductQuery + " and '" + CommonUtil.convertDateToString(bspCommissionModel.getTktToDate()) + "' BETWEEN bcm.tktFrmDate and bcm.tktToDate ";
			}
			if (null != bspCommissionModel.getOutBFrmDate())
			{
				searchProductQuery = searchProductQuery + " and '" + CommonUtil.convertDateToString(bspCommissionModel.getOutBFrmDate()) + "' BETWEEN bcm.outBFrmDate and bcm.outBToDate ";

			}
			if (null != bspCommissionModel.getOutBToDate())
			{
				searchProductQuery = searchProductQuery + " and '" + CommonUtil.convertDateToString(bspCommissionModel.getOutBToDate()) + "' BETWEEN bcm.outBFrmDate and bcm.outBToDate";
			}

			if (null != bspCommissionModel.getInBFrmDate())
			{
				searchProductQuery = searchProductQuery + " and '" + CommonUtil.convertDateToString(bspCommissionModel.getInBFrmDate()) + "' BETWEEN bcm.inBFrmDate and bcm.inBToDate ";

			}
			if (null != bspCommissionModel.getInBToDate())
			{
				searchProductQuery = searchProductQuery + " and '" + CommonUtil.convertDateToString(bspCommissionModel.getInBToDate()) + "' BETWEEN bcm.inBFrmDate and bcm.inBToDate ";
			}

			searchProductQuery += " AND bcm.status=?";
			parameterList.add(bspCommissionModel.getStatus());

			searchProductQuery += " AND bcm.approvalStatus=?";
			parameterList.add(bspCommissionModel.getApprovalStatus());

			searchProductQuery += " ORDER BY  bcm.lastModTime DESC";
		}

		return fetchWithHQL(searchProductQuery, parameterList);
	}

	@Override
	public List<KnowledgeCenterModel> getKnowledgeCenters(KnowledgeCenterModel kcModel) throws Exception
	{
		String searchQuery;
		if (kcModel != null && ((kcModel.getKcCountryList() != null && !kcModel.getKcCountryList().isEmpty()) || (kcModel.getKcBranchList() != null && !kcModel.getKcBranchList().isEmpty()) || (kcModel.getKcAgencyList() != null && !kcModel.getKcAgencyList().isEmpty()) || (kcModel.getDestinationCity() != null && !kcModel.getDestinationCity().isEmpty())))
		{
			searchQuery = QueryConstant.SEARCH_KNOWLEDGECENTERS_WITH_LOCATION;
		}
		else
		{
			searchQuery = QueryConstant.SEARCH_KNOWLEDGECENTERS;
		}

		List<Object> parameterList = new ArrayList<>();
		if (kcModel != null)
		{
			
			if(kcModel.getDestinationCity() != null && !kcModel.getDestinationCity().isEmpty()){
				StringBuilder sb = new StringBuilder();
				int index = 0;
				for (KnowledgeCenterDestinationCityModel destinationCity : kcModel.getDestinationCity())
				{
					if (index == 0)
					{
						sb.append(destinationCity.getDestinatonCityId());
					}
					else
					{
						sb.append("," + destinationCity.getDestinatonCityId());
					}
					index++;
				}
				searchQuery += " AND d.destinatonCityId IN  (" + sb.toString() + ")";
			}
			
			if (kcModel.getKcCountryList() != null && !kcModel.getKcCountryList().isEmpty())
			{
				StringBuilder sb = new StringBuilder();
				int index = 0;
				for (KnowledgeCenterCountryModel companyId : kcModel.getKcCountryList())
				{
					if (index == 0)
					{
						sb.append(companyId.getCountryId());
					}
					else
					{
						sb.append("," + companyId.getCountryId());
					}
					index++;
				}
				searchQuery += " AND kcc.countryId IN  (" + sb.toString() + ")";

			}
			if (kcModel.getKcBranchList() != null && !kcModel.getKcBranchList().isEmpty())
			{
				StringBuilder sb = new StringBuilder();
				int index = 0;
				for (KnowledgeCenterBranchModel branch : kcModel.getKcBranchList())
				{
					if (index == 0)
					{
						sb.append(branch.getBranchId());
					}
					else
					{
						sb.append("," + branch.getBranchId());
					}
					index++;
				}
				searchQuery += " AND kbm.branchId IN  (" + sb.toString() + ")";

			}
			if (kcModel.getKcAgencyList() != null && !kcModel.getKcAgencyList().isEmpty())
			{
				StringBuilder sb = new StringBuilder();
				int index = 0;
				for (KnowledgeCenterAgencyModel agency : kcModel.getKcAgencyList())
				{
					if (index == 0)
					{
						sb.append(agency.getAgencyId());
					}
					else
					{
						sb.append("," + agency.getAgencyId());
					}
					index++;
				}
				searchQuery += " AND kam.agencyId IN  (" + sb.toString() + ")";

			}

			if (kcModel.getSearchFrmDate() != null)
			{
				searchQuery += " and '" + kcModel.getSearchFrmDate() + "' BETWEEN kc.validFromDate  and kc.validToDate ";

			}
			if (kcModel.getSearchToDate() != null && !kcModel.getSearchToDate().equalsIgnoreCase("null"))
			{
				searchQuery += " and '" + kcModel.getSearchToDate() + "' BETWEEN kc.validFromDate  and kc.validToDate ";
			}
			if (kcModel.getStatus() != null && kcModel.getStatus() > -1)
			{
				searchQuery += " and kc.status = ?";
				parameterList.add(kcModel.getStatus());
			}

			if (kcModel.getApprovalStatus() != null && !kcModel.getApprovalStatus().equals(""))
			{
				searchQuery += " AND kc.approvalStatus=?";
				parameterList.add(kcModel.getApprovalStatus());
			}
		}
		searchQuery += " order by kc.lastModTime desc";
		return fetchWithHQL(searchQuery, parameterList);
	}

	@Override
	public List<CountryBean> fetchCountryList(CountryBean countryBean) throws Exception
	{
		String searchProductQuery = QueryConstant.SEARCH_COUNTRY;
		List<Object> parameterList = new ArrayList<>();
		if (countryBean != null)
		{
			searchProductQuery += " AND cb.status=?";
			parameterList.add(countryBean.getStatus());

			searchProductQuery += " AND cb.approvalStatus=?";
			parameterList.add(countryBean.getApprovalStatus());

			searchProductQuery += " ORDER BY cb.countryName"; // cb.lastModTime DESC,
		}

		return fetchWithHQL(searchProductQuery, parameterList);
	}

	@Override
	public List<Object> getAllAirportsCountryCity(AirportModal airportModel) throws Exception {
		String query  =  QueryConstantRest.GET_COUNRTY_AIRPORT_CITY;
		if(airportModel.getAirportName()!=null && !airportModel.getAirportName().isEmpty()){
			if(airportModel.getAirportName().length() == 3){// Search by Code
				
				query+=" ON c.country_id=cc.country_id WHERE LOWER(CITY_CODE) LIKE LOWER('%" + airportModel.getAirportName().trim() + "%') "
					+ " and  cc.status=1 and cc.approval_status=1 and c.status=1 and c.approval_status=1 union ALL SELECT CODE3, COUNTRY_NAME "
					+ ", CODE3, '', currency_Code, country_code, COUNTRY_ID FROM TT_COUNTRY  WHERE LOWER(CODE3) LIKE LOWER('%" + airportModel.getAirportName().trim() + "%')"
					+ " and status=1 and approval_status=1 limit 500";
				
			}else if(airportModel.getAirportName().length() > 3)// Search by name
				
				query+=" ON c.country_id=cc.country_id WHERE LOWER(CITY_NAME) LIKE LOWER('" + airportModel.getAirportName().trim() + "%') OR LOWER(COUNTRY_NAME) LIKE LOWER('" + airportModel.getAirportName().trim() + "%')"
					+ " OR LOWER(CITY_NAME) LIKE LOWER('% " + airportModel.getAirportName().trim() + "%') " + " and  cc.status=1 and cc.approval_status=1 and c.status=1 and c.approval_status=1 union ALL SELECT CODE3, COUNTRY_NAME "
					+ ", CODE3, '', currency_Code, country_code, COUNTRY_ID FROM TT_COUNTRY  WHERE LOWER(COUNTRY_NAME) LIKE LOWER('" + airportModel.getAirportName().trim() + "%') OR LOWER(COUNTRY_NAME) LIKE LOWER('% " + airportModel.getAirportName().trim() + "%')"
					+ " and status=1 and approval_status=1 limit 500";
		
		}
		return fetchWithSQL(query, null);

	}

	@Override
	public List<EnumModel> getEnumListByValue(String columnName) throws Exception
	{

		List<String> parameterList = new ArrayList<>();
		String enumQuery = QueryConstantRest.GET_ENUM_BY_VALUE;
		parameterList.add(columnName);
		return fetchWithHQL(enumQuery, parameterList);
	}

	@Override
	public List<AirportModal> getAirportDetailsByCode(String columnName) throws Exception
	{

		List<String> parameterList = new ArrayList<>();
		String enumQuery = QueryConstantRest.FETCH_AIRPORT_BY_AIRPORTCODE;
		parameterList.add(columnName);
		return fetchWithHQL(enumQuery, parameterList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AirportModal> searchAirport(AirportModal airportModel) throws Exception
	{
		String searchQuery;
		List<Object> parameterList = new ArrayList<>();
		if (airportModel != null && (airportModel.getAirportCountryMappings() != null && !airportModel.getAirportCountryMappings().isEmpty()))
		{
			searchQuery = QueryConstantRest.SEARCH_AIRPORT_COUNTRY;
			parameterList.add(airportModel.getAirportCountryMappings().get(0).getCountryId());
		}
		else
		{
			searchQuery = QueryConstantRest.SEARCH_AIRPORT;
		}

		if (airportModel != null)
		{
			if (airportModel.getStatus() > -1)
			{
				searchQuery += " and TA.STATUS = ?";
				parameterList.add(airportModel.getStatus());
			}

			if (airportModel.getApprovalStatus() != null && !airportModel.getApprovalStatus().equals(""))
			{
				searchQuery += " AND TA.APPROVAL_STATUS=?";
				parameterList.add(airportModel.getApprovalStatus());
			}

		}

		searchQuery += " order by TA.LAST_MOD_TIME desc";

		Session session = getSessionFactory().openSession();
		Transaction tx = null;
		List<AirportModal> result = null;
		try
		{
			tx = session.beginTransaction();
			SQLQuery sqlQuery = session.createSQLQuery(searchQuery);
			setQueryparameter(sqlQuery, parameterList);
			result = sqlQuery.addScalar("airportId").addScalar("airportCode").addScalar("airportName").addScalar("cityId").addScalar("countryID").setResultTransformer(Transformers.aliasToBean(AirportModal.class)).list();
		}
		catch (HibernateException e)
		{
			if (tx != null)
				tx.rollback();
			TTLog.info(13, "GenericDAOImpl : fetchWithSQL HibernateException = " + e);
			throw e;
		}
		catch (Exception e)
		{
			if (tx != null)
				tx.rollback();
			TTLog.info(13, "GenericDAOImpl : fetchWithSQL Exception = " + e);
			throw e;
		}
		finally
		{
			session.close();
		}
		return result;
	}

	private void setQueryparameter(Query query, List<Object> parameterList)
	{
		if (null != parameterList && !parameterList.isEmpty())
		{
			for (int i = 0; i < parameterList.size(); i++)
			{

				if (parameterList.get(i) instanceof Integer)
				{
					query.setInteger(i, (Integer) parameterList.get(i));
				}
				else if (parameterList.get(i) instanceof Date)
				{
					query.setTimestamp(i, (Date) parameterList.get(i));
				}
				else if (parameterList.get(i) instanceof Float)
				{
					query.setFloat(i, (Float) parameterList.get(i));
				}
				else if (parameterList.get(i) instanceof Double)
				{
					query.setDouble(i, (Double) parameterList.get(i));
				}
				else if (parameterList.get(i) instanceof Long)
				{
					query.setLong(i, (Long) parameterList.get(i));
				}
				else if (parameterList.get(i) instanceof String)
				{
					query.setString(i, (String) parameterList.get(i));
				}
				else
				{
					query.setParameter(i, parameterList.get(i));
				}
			}
		}
	}

	@Override
	public List<Object> getFactObjectIdAgencyBranch(List<Integer> ids) throws Exception {
		List<Object> o = new ArrayList<>();
		List<String> parameterList;
		String enumQuery = QueryConstantRest.FACT_ID_OF_ORGANIZAITON;
		
		for(int i=0; i<ids.size();i++){
			parameterList = new ArrayList<>();
			parameterList.add(ids.get(0).toString());
			o.add(fetchWithHQL(enumQuery, parameterList));
		}
		
		return o;
	}

	@Override
	public List<PaymentMode> fetchPaymentOption(String agencyCode) throws Exception
	{
		List<String> parameterList = new ArrayList<>();
		parameterList.add(agencyCode);
		String paymentQuery = QueryConstantRest.GET_PAYMENT_OPTION;
		return fetchWithHQL(paymentQuery, parameterList);
	}

	@Override
	public List<Object> fetchProductSupplierMappingWithAgency(Integer groupId) throws Exception
	{
		List<Integer> parameterList = new ArrayList<>();
		parameterList.add(groupId);
		String paymentQuery = QueryConstantRest.GET_PRODUCT_SUPPLIER_AGENCY;
		return fetchWithSQL(paymentQuery, parameterList);
	}

	@Override
	public List<AirlineModel> prefferdAirline(AirlineModel airlineModel)
			throws Exception {
		List<Integer> parameterList = new ArrayList<>();
		parameterList.add(airlineModel.getStatus());
		parameterList.add(airlineModel.getApprovalStatus());
		String query= QueryConstantRest.FETCH_PREFFERED;
		if(airlineModel.getAirlineType()==0){
			query +=" AND am.airlineType !=0";
		}
		if(airlineModel.getAirlineName()!=null){
			query +=" AND am.airlineName LIKE '%"+airlineModel.getAirlineName()+"%'";
		}
		return fetchWithHQL(query, parameterList);
	}

	@Override
	public List<CountryBean> fetchCountryById(String id) throws Exception {
		String query = QueryConstantRest.SEARCH_COUNTRY_BY_ID;
		query = query+"("+id+")";
		return fetchWithHQL(query, null);
	}
	
	@Override
	public List<GDSDealCodeModel> getDealCodeModelList(GDSDealCodeModel gdsDealCodeModel) throws Exception {
		String searchProductQuery = QueryConstant.SEARCH_DEAL_CODE;
		List<Object> parameterList = new ArrayList<>();
		if (gdsDealCodeModel != null)
		{
			if (null != gdsDealCodeModel.getAirlineId() && gdsDealCodeModel.getAirlineId() > 0)
			{
				searchProductQuery = searchProductQuery + " and dc.airlineId = ?";
				parameterList.add(gdsDealCodeModel.getAirlineId());
			}

			if (null != gdsDealCodeModel.getCredentialId() && gdsDealCodeModel.getCredentialId() > 0)
			{
				searchProductQuery = searchProductQuery + " and dc.credentialId = ?";
				parameterList.add(gdsDealCodeModel.getCredentialId());
			}
			if (null != gdsDealCodeModel.getValidFrom())
			{
				searchProductQuery = searchProductQuery + " and '" + CommonUtil.convertDateToString(gdsDealCodeModel.getValidFrom()) + "' BETWEEN dc.validFrom and dc.validTo ";

			}
			if (null != gdsDealCodeModel.getValidTo())
			{
				searchProductQuery = searchProductQuery + " and '" + CommonUtil.convertDateToString(gdsDealCodeModel.getValidTo()) + "' BETWEEN dc.validFrom and dc.validTo ";
			}
			
			searchProductQuery += " AND dc.status=?";
			parameterList.add(gdsDealCodeModel.getStatus());

			searchProductQuery += " AND dc.approvalStatus=?";
			parameterList.add(gdsDealCodeModel.getApprovalStatus());

			searchProductQuery += " ORDER BY  dc.lastModTime DESC";
		}

		return fetchWithHQL(searchProductQuery, parameterList);
	}

	@Override
	public List<Object> fetchNationalityName(String nationalityCode)
			throws Exception {
		String queryRest =QueryConstantRest.NATIONALITY_NAME_QUERY;
		List<String> parameterList = new ArrayList<>();
		parameterList.add(nationalityCode);
		return fetchWithSQL(queryRest, parameterList);
	}
}
