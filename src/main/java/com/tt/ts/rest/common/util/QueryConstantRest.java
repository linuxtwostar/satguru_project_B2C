package com.tt.ts.rest.common.util;



public class QueryConstantRest {
	
	public static final String FETCH_DASHBOARDREPORT_INSURANCE_BY_CLIENT_ID = "SELECT B.BOOKING_STATUS AS bookingStatus, count(B.ORDER_ID) AS reportAmount " +
			"FROM TT_TS_INSURANCE_INVOICE A, " +
			"TT_TS_INSURANCE_BOOKING B, TT_TS_AGENT_ORDER C " +
			"WHERE A.IN_BOOKING_REF_NO =B.IN_BOOKING_REF_NO AND B.ORDER_ID =C.ORDER_ID " +
			"AND DATE(B.CREATION_TIME) BETWEEN DATE(DATE_ADD(NOW(), INTERVAL ";
	
	public static final String FETCH_DASHBOARDREPORT_HOTEL_BY_CLIENT_ID = "SELECT B.BOOKING_STATUS AS bookingStatus, count(B.ORDER_ID) AS reportAmount " +
			"FROM TT_HOTELS_BOOK B, TT_TS_AGENT_ORDER C " +
			"WHERE B.ORDER_ID =C.ORDER_ID " +
			"AND DATE(B.CREATION_TIME) BETWEEN DATE(DATE_ADD(NOW(), INTERVAL ";
	
	public static final String FETCH_DASHBOARDREPORT_FLIGHT_BY_CLIENT_ID = "SELECT B.BOOKING_STATUS AS bookingStatus, count(B.ORDER_ID) AS reportAmount " +
			"FROM TT_TS_FB_INVOICE_DETAIL A, " +
			"TT_TS_FLIGHT_BOOK B, TT_TS_AGENT_ORDER C " +
			"WHERE A.FB_BOOKING_REF_NO =B.FB_BOOKING_REF_NO AND B.ORDER_ID =C.ORDER_ID " +
			"AND DATE(B.CREATION_TIME) BETWEEN DATE(DATE_ADD(NOW(), INTERVAL ";
	
	public static final String FETCH_TODOLIST_BY_CLIENT_ID = "FROM AgentToDoListModel tdom where 1=1 ";

    public static final String FORGOT_PASSWORD = "UPDATE User u SET u.password=? WHERE u.userAlias = ?";

    public static final String FETCH_PRODUCT_BY_GROUPID = "SELECT distinct pm FROM ProductModel pm,OrganizationProductModel op,OrganizationModel om,GroupModal gm WHERE pm.productId = op.productId AND op.organizationModel=om.organizationId AND om.groupId=gm.groupId AND gm.groupId = ? AND pm.status=1 AND pm.approvalStatus=1";

    public static final String FETCH_SUPPLIER_BY_GROUPID = "SELECT distinct sm FROM SupplierModal sm,SupplierProductModal sp, ProductModel pm,OrganizationProductModel op,OrganizationModel om,GroupModal gm,OrganizationProductSupplierModel ps WHERE sm.supplierId=sp.supplierModal AND pm.productId=sp.productId AND pm.productId = op.productId AND op.organizationModel=om.organizationId AND om.groupId=gm.groupId AND ps.productId =pm.productId AND ps.supplierId = sm.supplierId AND ps.organizationModel = om.organizationId  AND gm.groupId = ? AND sp.productId=? AND sm.status=1 AND sm.approvalStatus=1";

    public static final String FETCH_CREDENTIAL_BY_GROUPID = "SELECT distinct sc FROM SupplierCredentialModal sc,SupplierModal sm, ProductModel pm,OrganizationProductModel op,OrganizationModel om,GroupModal gm WHERE sc.productId=pm.productId AND sm.supplierId=sc.supplierId AND  pm.productId = op.productId AND op.organizationModel=om.organizationId AND om.groupId=gm.groupId AND gm.groupId = ? AND  pm.productId=? AND sm.supplierId= ? AND sc.status = 1";

    public static final String FETCH_SUPPLIER_PRODUCT_BY_GROUPID = "SELECT sm FROM SupplierModal sm,SupplierProductModal sp, ProductModel pm,OrganizationProductModel op,OrganizationModel om,GroupModal gm WHERE sm.supplierId=sp.supplierModal AND pm.productId = op.productId AND op.organizationModel=om.organizationId AND om.groupId=gm.groupId AND gm.groupId = ?";

    public static final String FETCH_CREDENTIAL_SUPPLIER_BY_CREDID = "SELECT distinct c.credentialFieldName, cv.value, scm.credentialId, scm.currencyId, scm.credentialType,scm from ProductModel p, SupplierModal s, SupplierProductModal sp,SupplierCredentialFieldModal c, SupplierCredentialValueModal cv, OrganizationProductModel op, OrganizationModel om, GroupModal gm,SupplierCredentialModal scm join scm.supplierCredentialCountryModals sccm where p.productId=sp.productId and s.supplierId=sp.supplierModal.supplierId and c.productId=p.productId and cv.credentialName=c.fieldId  AND s.supplierId=scm.supplierId AND scm.productId=p.productId AND scm.credentialId=cv.credentialId and p.status=1 AND op.organizationModel.organizationId=om.organizationId AND om.groupId=gm.groupId and gm.groupId=? AND  p.productId=? AND s.supplierId= ? AND scm.credentialId = ? AND scm.status = 1";
    
    public static final String FETCH_CREDENTIAL_SUPPLIER_PRODUCT_GROUPID = "SELECT distinct c.credentialFieldName, cv.value, scm.credentialId,scm.currencyId from ProductModel p, SupplierModal s, SupplierProductModal sp,SupplierCredentialFieldModal c, SupplierCredentialValueModal cv, OrganizationProductModel op, OrganizationModel om, GroupModal gm,SupplierCredentialModal scm where p.productId=sp.productId and s.supplierId=sp.supplierModal.supplierId and c.productId=p.productId and cv.credentialName=c.fieldId  AND s.supplierId=scm.supplierId AND scm.productId=p.productId AND scm.credentialId=cv.credentialId and p.status=1 AND op.organizationModel.organizationId=om.organizationId AND om.groupId=gm.groupId and gm.groupId=? AND  p.productId=? AND s.supplierId= ? AND scm.status = 1";

    public static final String FETCH_GROUP_BY_USER_ID = "SELECT gm.GROUP_ID as agentID,gm.GROUP_NAME as agentName,gm1.GROUP_ID as branchID,gm1.GROUP_NAME  as branchName,gm1.PARENT_GROUP  as companyID, om.WEB,u.USER_ALIAS, u.PASSWORD , u.PROFILE_IMAGE, om.ORGANIZATION_ID as agencyCode,  om1.ORGANIZATION_ID as branchCode,om1.BRAND,om.NOTIFICATION_TYPE, om.IMPORT_PNR,crr.CURRENCY_NAME, om2.COUNTRY_ID,tc.COUNTRY_CODE,orgC.PHONE,orgC.EMAIL,om.ADDRESS1,crr.CURRENCY_CODE,om.FACT_OBJECT_ID as agencyFact,om1.FACT_OBJECT_ID as branchFact,orgC.name as contactName FROM TT_USER_GROUP gm , TT_USER_GROUP gm1 , TT_ORGANIZATION om ,TT_GROUP_USER_MAPPING gu ,TT_USER u,  TT_ORGANIZATION om1,TT_USER_GROUP gm2 ,TT_ORGANIZATION om2,TT_CURRENCY crr,TT_COUNTRY tc,TT_ORG_CONTACTS orgC WHERE  om.GROUP_ID=gm.GROUP_ID  AND gu.GROUP_ID = gm.GROUP_ID AND u.USER_ID = gu.USER_ID AND gm.PARENT_GROUP=gm1.GROUP_ID AND gm1.GROUP_ID=om1.GROUP_ID  AND gm2.GROUP_ID=om2.GROUP_ID AND om2.GROUP_ID=gm1.PARENT_GROUP AND om2.CURRENCY_ID=crr.CURRENCY_ID  AND tc.COUNTRY_ID = om2.COUNTRY_ID AND orgC.ORGANIZATION_ID=om.ORGANIZATION_ID";
    
    public static final String FETCH_GROUP_BY_AGENT_ID ="SELECT gm.GROUP_ID as agentID,gm.GROUP_NAME as agentName,gm1.GROUP_ID as branchID,gm1.GROUP_NAME  as branchName,gm1.PARENT_GROUP  as companyID, om.WEB,u.USER_ALIAS, u.PASSWORD , u.PROFILE_IMAGE, om.ORGANIZATION_ID as agencyCode,  om1.ORGANIZATION_ID as branchCode,om1.BRAND,om.NOTIFICATION_TYPE, om.IMPORT_PNR,crr.CURRENCY_NAME, om2.COUNTRY_ID,tc.COUNTRY_CODE,orgC.PHONE,orgC.EMAIL,om.ADDRESS1,crr.CURRENCY_CODE FROM TT_USER_GROUP gm , TT_USER_GROUP gm1 , TT_ORGANIZATION om ,TT_GROUP_USER_MAPPING gu ,TT_USER u,  TT_ORGANIZATION om1,TT_USER_GROUP gm2 ,TT_ORGANIZATION om2,TT_CURRENCY crr,TT_COUNTRY tc,TT_ORG_CONTACTS orgC WHERE  om.GROUP_ID=gm.GROUP_ID  AND gu.GROUP_ID = gm.GROUP_ID AND u.USER_ID = gu.USER_ID AND gm.PARENT_GROUP=gm1.GROUP_ID AND gm1.GROUP_ID=om1.GROUP_ID  AND gm2.GROUP_ID=om2.GROUP_ID AND om2.GROUP_ID=gm1.PARENT_GROUP AND om2.CURRENCY_ID=crr.CURRENCY_ID  AND tc.COUNTRY_ID = om2.COUNTRY_ID AND orgC.ORGANIZATION_ID=om.ORGANIZATION_ID AND gm.GROUP_ID= ? AND om.ORGANIZATION_TYPE=?";

    public static final String FETCH_BRN_GROUP_BY_GROUP_ID = "SELECT  distinct '' as agentID,'' as agentName,gm.GROUP_ID as branchId,gm.GROUP_NAME as branchName,gm1.GROUP_ID as compId, om.WEB,u.USER_ALIAS, u.PASSWORD , u.PROFILE_IMAGE, '' as agencyCode,om.ORGANIZATION_ID as branchCode,om1.BRAND,om.NOTIFICATION_TYPE, om.IMPORT_PNR,crr.CURRENCY_NAME, om1.COUNTRY_ID,tc.COUNTRY_CODE,orgC.PHONE,orgC.EMAIL,om.ADDRESS1,crr.CURRENCY_CODE,om.FACT_OBJECT_ID as branchFact,om1.FACT_OBJECT_ID as compFact,orgC.name as contactName FROM TT_USER_GROUP gm , TT_USER_GROUP gm1 , TT_ORGANIZATION om ,TT_GROUP_USER_MAPPING gu ,TT_USER u,TT_ORGANIZATION om1,TT_CURRENCY crr,TT_COUNTRY tc,TT_ORG_CONTACTS orgC WHERE om.GROUP_ID=gm.GROUP_ID  AND gu.GROUP_ID = gm.GROUP_ID AND u.USER_ID = gu.USER_ID AND gm.PARENT_GROUP=gm1.GROUP_ID AND gm1.GROUP_ID=om1.GROUP_ID AND om1.CURRENCY_ID=crr.CURRENCY_ID AND tc.COUNTRY_ID = om1.COUNTRY_ID AND orgC.ORGANIZATION_ID=om.ORGANIZATION_ID";
    
    public static final String FETCH_COUNTRY_DETAIL_BY_GROUPID = "SELECT gm.groupId as branchId,gm.groupName as branchName,c.countryId,c.countryName,s.stateId,s.stateName,ci.cityId,ci.cityName,gm1.groupId as OC,gm1.groupName as OC,cur.currencyId,cur.currencyName,cur.currencyCode,om.organizationId as branchCode FROM GroupModal gm, OrganizationModel om,CountryBean c,StateBean s,CityBean ci,GroupModal gm1,OrganizationModel org1,CurrencyBean cur WHERE gm.groupId=om.groupId AND om.country=c.countryId AND om.state=s.stateId AND ci.cityId=om.city AND gm1.groupId = gm.parentGroupId AND org1.groupId = gm1.groupId AND org1.currencyId=cur.currencyId  AND gm.groupId =  (select parentGroupId from GroupModal WHERE groupId=?)";

    public static final String FETCH_COUNTRY_DETAIL_BY_GROUPID_BRN = "SELECT gm.groupId as branchId,gm.groupName as branchName,c.countryId,c.countryName,s.stateId,s.stateName,ci.cityId,ci.cityName,gm1.groupId as OC,gm1.groupName as OC,cur.currencyId,cur.currencyName,cur.currencyCode,om.organizationId as branchCode FROM GroupModal gm, OrganizationModel om,CountryBean c,StateBean s,CityBean ci,GroupModal gm1,OrganizationModel org1,CurrencyBean cur WHERE gm.groupId=om.groupId AND om.country=c.countryId AND om.state=s.stateId AND ci.cityId=om.city AND gm1.groupId = gm.parentGroupId AND org1.groupId = gm1.groupId AND org1.currencyId=cur.currencyId  AND gm.groupId = ?";
    public static final String FETCH_AGENCY_CREDIT_LIMIT = "SELECT op FROM OrganizationFinancialModel op,OrganizationModel om,GroupModal gm WHERE op.organizationModel = om.organizationId  AND om.groupId=gm.groupId AND gm.groupId = ?";

    public static final String FETCH_QUOTATION_BY_ID = "FROM QuotationModel qm where qm.quotationId = ?";
    public static final String FETCH_QUOTATION_BY_CLIENT_ID = "FROM QuotationModel qm where qm.clientId = ?";
    public static final String FETCH_QUOTATION_BY_CLIENTID_AND_DATE_RANGE = "FROM QuotationModel qm where qm.clientId = ? AND qm.validUpto BETWEEN ? AND ?";
    public static final String FETCH_CURRENCY_BY_ID = "FROM CurrencyBean cb where cb.currencyId = ?";
    
    public static final int HOTEL_PAGE_SIZE = 25;

    public static final String FETCH_HOTEL_CITIES = "FROM HotelCityDetailsModel";

    public static final String FETCH_HOTEL_COUNTRIES = "FROM HotelCountryDetailsModel";

    public static final String FETCH_HOTEL_INFO = "FROM HotelSatguruModel ht where ht.hotelId = ?";

    public static final String SEARCH_AIRPORT = "SELECT TA.AIRPORT_ID AS airportId,TA.AIRPORT_CODE as airportCode,TA.AIRPORT_NAME as airportName,TA.CITY_ID as cityId,TA.COUNTRY_ID as countryID FROM TT_AIRPORT_DETAILS TA WHERE 1=1";

    public static final String SEARCH_AIRPORT_COUNTRY = "SELECT TA.AIRPORT_ID AS airportId,TA.AIRPORT_CODE as airportCode,TA.AIRPORT_NAME as airportName,TA.CITY_ID as cityId,TA.COUNTRY_ID as countryID FROM TT_AIRPORT_DETAILS TA LEFT JOIN("
    	+ "SELECT AIRPORT_ID,COUNTRY_ID FROM TT_RESTRICTED_AIRP_COUNTRY WHERE COUNTRY_ID=? )TEMP ON TA.AIRPORT_ID=TEMP.AIRPORT_ID WHERE TA.APPROVAL_STATUS=1 AND TEMP.COUNTRY_ID IS NULL";
    
    public static final String FETCH_CORPORATE_TRAVEL_CORDNT_BY_CORPORATEID = "FROM CorporateTravelCordinator ctc where ctc.corporateModel.corpId = ? ";

    public static final String FETCH_CORPORATE_FINANCE_CONTANT_BY_CORPORATEID = "FROM CorporateFinanceContact cfc where cfc.corporateModel.corpId = ? ";

    public static final String FETCH_CORPORATES = "FROM CorporateModel cm where 1=1 ";

    public static final String FETCH_AGENTS = "FROM AgentModel am where 1=1 ";
    
    public static final String FETCH_AGENTS_CREDITLIMIT = "FROM AgentCreditLimitModel acm where 1=1 ";

    public static final String DELETE_AGENTS_CREDITLIMIT = "DELETE FROM AgentCreditLimitModel acm where acm.userModal = ? ";
    
    public static final String FETCH_CORPORATE_LIST_BY_AGENCY_ID = "FROM CorporateModel cfc where cfc.createdByAgentId = ? ";

    public static final String FETCH_AIRLINE_LIST_BY_AGENCY_ID = "SELECT distinct am FROM AirlineModel am where am.networkType=?";

    public static final String FETCH_PAXES_COUNT = "SELECT COUNT(distinct pm.id) FROM PaxModel pm where 1=1 ";
    public static final String FETCH_PAXES = "FROM PaxModel pm LEFT JOIN FETCH pm.paxRelationList where 1=1 ";

    public static final String FETCH_PAXE_RELATIONS = "FROM PaxRelationModel prm where 1=1 ";

    public static final String FETCH_AIRLINE_PREF_BY_PAX_ID = "FROM PaxAirlinePrefernce pa where pa.paxModel.id = ? ";

    public static final String FETCH_PAX_FREQ_FLYR_BY_PAX_ID = "FROM PaxFrequentFlyer pf where pf.paxModel.id = ? ";

    public static final String UPDATE_AGENT_SETTING = "UPDATE OrganizationModel om SET om.companyURL=? , om.notificationType=? where om.groupId = ? AND om.organizationId = ?";

    public static final String FETCH_HOTEL_MAP_DETAILS = "FROM HotelSatMappingModel hsm WHERE hsm.suppHotelCode = ?";

    public static final String FETCH_HOTEL_ALL_DETAILS = "FROM HotelSatMappingModel hsm WHERE hsm.clarifiId = ?";

    public static final String GET_DELETE_FOR_PAX_RELATION = "delete from PaxRelationModel prm where prm.paxModel.id = ? AND prm.paxId = ?";

    public static final String FETCH_PAX_DOC_BY_PAX_ID = "FROM PaxDocumentModel pdm where pdm.paxModel.id = ? ";

    public static final String GET_DELETE_FOR_PAX_AIRPREF = "DELETE FROM PaxAirlinePrefernce pa where pa.paxModel.id = ? ";

    public static final String GET_DELETE_FOR_PAX_FREQFLYR_REF = "DELETE FROM PaxFrequentFlyer pf where pf.paxModel.id = ? ";

    public static final String FETCH_PROFILE_IMAGE = "select doc FROM OrganizationModel org,OrganizationDocumentModel doc WHERE org.organizationId = doc.organizationModel AND org.groupId = ? and doc.description = ? order by doc.Sequence ";

    public static final String FETCH_MARKUP_RECORD = "FROM AgencyMarkupModel m WHERE m.agencyId = ? and m.markingFor = ? and m.corporateId = ?";

    public static final String DELETE_MARKUP_RECORD = "DELETE FROM AgencyMarkupModel m WHERE  m.corporateId = ? and m.agencyId = ? AND m.productRefId = ? AND domOrInternational = ?";
    
    public static final String DELETE_MARKUP_RECORD_TO_DELETE = "FROM AgencyMarkupModel m WHERE m.corporateId = ? and m.agencyId = ? AND m.productRefId = ? AND domOrInternational = ?";//m.userId = ? and

    public static final String UPDATE_MARKUP_STATUS = "UPDATE FROM AgencyMarkupModel m set m.status=? WHERE m.userId = ? and m.corporateId = ? and m.agencyId = ? AND m.productRefId = ? AND domOrInternational = ?";

    public static final String GET_DELETE_FOR_CORP_TRVL = "DELETE FROM CorporateTravelCordinator ctc where ctc.corporateModel.corpId = ? ";

    public static final String GET_DELETE_FOR_CORP_FINC = "DELETE FROM CorporateFinanceContact cfc where cfc.corporateModel.corpId = ? ";

	public static final  String FETCH_CREDENTIAL_REST_AIRLINE = "SELECT am.airlineId FROM SupplierCredentialAirlineModal am WHERE am.credentialId=?";
	
	public static final  String FETCH_DEALCODE_BY_AGENCY = "Select a.dealId, a.supplierId,a.dealCode, DATE_FORMAT(a.validTo,'%d/%m/%Y'),  DATE_FORMAT(a.validFrom,'%d/%m/%Y'), s.supplierName, air.airlineCode as airlineCode, a.secAirportFrm as airportCodeFrom, a.secAirportTo as airportCodeTo,a.orgCountry,a.destCountry,a.isCountrySelected,a.credentialId,a.lastModTime, a.importPNR FROM GDSDealCodeModel a, GDSDealCodeAgencyModel b,SupplierModal s, AirlineModel air WHERE a.dealId = b.dealCodeModel and a.supplierId = s.supplierId and air.contentId=a.airlineId   and b.agencyId=? and a.credentialId=? and a.status=1 AND a.approvalStatus=1";
	
	public static final String FETCH_GDS_SEARCH_CREDENTIAL = "SELECT opm.pccCodeType,opm.credentialId,scm.credentialName,opm.managerName,opm.description,scm.currencyId  FROM OrganizationPccModel opm,OrganizationModel om,SupplierCredentialModal scm  WHERE om.organizationId = opm.organizationModel AND opm.credentialId=scm.credentialId AND opm.factApprovalStatus = 1 AND om.groupId=? AND scm.status = 1";
	
	public static final String FETCH_GDS_SEARCH_CREDENTIAL_AGENCY = "SELECT opm.pccCodeType,opm.credentialId,scm.credentialName,opm.managerName,opm.description,scm.currencyId  FROM OrganizationPccModel opm,OrganizationModel om,SupplierCredentialModal scm  WHERE om.organizationId = opm.organizationModel AND opm.credentialId=scm.credentialId AND om.groupId=? AND scm.status = 1";
	
	public static final String FETCH_GDS_TICKET_CREDENTIAL = "SELECT otpm.credentialId,scm.credentialName,am.airlineCode,ap.airportCode,ap1.airportCode,scm.currencyId  FROM OrganizationTicketPccModal otpm,OrganizationModel om,SupplierCredentialModal scm,AirlineModel am ,AirportModal ap,AirportModal ap1 WHERE om.organizationId = otpm.organizationModel AND otpm.credentialId=scm.credentialId AND am.contentId= otpm.airlineCode AND ap.airportId=otpm.origin AND ap1.airportId=otpm.destination AND otpm.factApprovalStatus = 1 AND om.groupId=? AND scm.status = 1";
	
	public static final String FETCH_GDS_TICKET_CREDENTIAL_AGENCY = "SELECT otpm.credentialId,scm.credentialName,am.airlineCode,ap.airportCode,ap1.airportCode,scm.currencyId  FROM OrganizationTicketPccModal otpm,OrganizationModel om,SupplierCredentialModal scm,AirlineModel am ,AirportModal ap,AirportModal ap1 WHERE om.organizationId = otpm.organizationModel AND otpm.credentialId=scm.credentialId AND am.contentId= otpm.airlineCode AND ap.airportId=otpm.origin AND ap1.airportId=otpm.destination AND om.groupId=? AND scm.status = 1";
	
	public static final String FETCH_CREDENTIAL_NAME_BY_CREDENTIAL_ID ="SELECT distinct c.credentialFieldName, cv.value, scm.credentialId,scm.currencyId  from SupplierCredentialFieldModal c, SupplierCredentialValueModal cv, SupplierCredentialModal scm where  cv.credentialName=c.fieldId   AND scm.credentialId=cv.credentialId  AND scm.credentialId=? AND scm.status=1";

	public static final String FETCH_ERROR_CODE="FROM ErrorCodeModal WHERE errorCode= ? AND errorDesc = ? AND vendorName =?";

	public static final String CHECKSP_PNR = "SELECT CREATION_TIME FROM TT_TS_FB_FLIGHT_DETAILS WHERE SP_PNR_NO = ? AND CREATION_TIME = ? ";	
	
	public static final String GET_GDS_SUPPLIER_LIST = "Select sm.supplierId , sm.supplierName from SupplierProductModal spm, SupplierModal sm, OrganizationProductModel opm where sm.supplierId = spm.supplierModal and opm.organizationModel = ? and opm.productId=? and spm.productType = ? and sm.status = ?";
	
	public static final String GET_RESTRICTED_AIRLINE_LIST_BY_AGENCY = "Select sm.airlineCode from OrganizationAirlineCodeModal sm WHERE sm.organizationModel = ? ";
	
	public static final String GET_PARENT_GROUP_ID="SELECT organizationId FROM OrganizationModel WHERE groupId= ?";
	
	public static final String GET_AGENT_TYPE="SELECT position FROM AgentModel WHERE id= ?";
	
	public static final String GET_RBD_LIST_BY_COUNTRYID="select rm.cabinType, rm.rbdType, rm.airlineId,am.airlineCode from RbdModel rm, RBDCountryPosMapping m,AirlineModel am WHERE m.rbdModal = rm.contentId and am.contentId=rm.airlineId and rm.status=1 and rm.approvalStatus=1 and m.countryId= ?";
	
	public static final String FETCH_AGENCY_MARKUP = "SELECT amm.markingFor,amm.markupType,amm.markupValue,am.airlineCode,amm.domOrInternational FROM AgencyMarkupModel amm,AirlineModel am WHERE am.contentId=amm.prodId AND amm.productRefId=? AND amm.agencyId = ? AND amm.corporateId=? AND amm.domOrInternational=?";//AND amm.status= ?  

	public static final String FETCH_AGENCY_MARKUP_OTHERPRODUCT = "SELECT amm.markingFor,amm.markupType,amm.markupValue FROM AgencyMarkupModel amm WHERE  amm.productRefId=? AND amm.agencyId = ? AND amm.status= ? AND amm.corporateId=?  ";
	
	public static final String GET_USER_AGENCY_CODE = "SELECT om FROM OrganizationModel om where om.groupId IN (Select gu.groupId from UserGroupMappingModel gu where ";

	public static final String GET_COUNRTY_AIRPORT_CITY = "SELECT CITY_CODE, cc.COUNTRY_NAME, cc.CODE3, city_name, '' as curr, cc.country_code, cc.COUNTRY_ID FROM TT_CITY c left outer join TT_COUNTRY cc ";
		//"//"SELECT a.airportCode, c.cityCode, c.cityName, cc.countryCode, cc.countryName FROM AirportModal a, CityBean c, CountryBean cc WHERE c.countryId= cc.countryId AND cc.approvalStatus=? and cc.status = ?";
	
	public static final String GET_ENUM_BY_VALUE = "from EnumModel em where em.value=?";
	
	public static final String FETCH_AIRPORT_BY_AIRPORTCODE = "FROM AirportModal a where a.airportCode = ?";

	public static final String SEARCH_BLACKOUT_FLIGHT_COUNTRY =" select distinct bof from BlackOutFlightModel bof,BlackListFlightCountryPosModel cbm where bof.blackOutFlightId=cbm.blackOutFlight  ";

	public static final String SEARCH_FLIGHT_TAG_COUNTRY = "SELECT distinct fld FROM FlightTagModel fld,TagFlightCountryPosModel cm   WHERE fld.contentId = cm.flightTagModel AND 1=1";
	
	public static final String FACT_ID_OF_ORGANIZAITON = "SELECT o.factObjectId FROM OrganizationModel o where o.groupId=? ";

	public static final String VALIDATE_PAX_MODAL = "FROM PaxModel WHERE lower(email) =? AND lower(firstName) =?  ";

	public static final String GET_PAYMENT_OPTION = "SELECT pm FROM OrganizationPaymentModel pmode,PaymentMode pm WHERE pmode.organizationModel =? AND  pm.paymentModeId=pmode.paymentoption AND pm.status=1 order by pm.paymentModeId";

	public static final String GET_PRODUCT_SUPPLIER_AGENCY = "SELECT pm.CONTENT_ID,pm.PRODUCT_NAME,ss.SUPPLIER_ID,ss.SUPPLIER_NAME FROM TT_TS_SUPPLIER_PRODUCTS as sp LEFT JOIN TT_TS_PRODUCTS as pm on pm.CONTENT_ID=sp.PRODUCT_ID LEFT JOIN TT_TS_SUPPLIER  as ss  on  sp.SUPPLIER_ID=ss.SUPPLIER_ID LEFT  JOIN TT_ORG_PRODUCTS_SUPPLIER_MAP as psm on psm.PRODUCT_ID=pm.CONTENT_ID AND psm.SUPPLIER_ID=ss.SUPPLIER_ID LEFT JOIN  TT_ORGANIZATION as org on org.ORGANIZATION_ID=psm.ORGANIZATION_ID WHERE org.GROUP_ID=? AND pm.STATUS=1 AND pm.APPROVAL_STATUS AND ss.STATUS=1 AND ss.APPROVAL_STATUS=1";

	public static final String FETCH_PREFFERED = "SELECT new AirlineModel(contentId,airlineCode,airlineName,status,networkType,airlineType,approvalStatus) FROM AirlineModel am WHERE status=? AND approvalStatus=? ";
	
	public static final String UPDATE_PAX_DETAILS = "UPDATE TT_TS_PAX PM SET ";
	
    public static final String GET_FACT_CURRENCY_RATE = "SELECT ROE FROM TT_FACTS_CURR_RATE_DUMP FCRD WHERE FCRD.FLAG_BUYER_SELLER='1'";
	
	public static final String GET_CURRENCY_ID = "SELECT FACTS_OBJECT_ID FROM TT_CURRENCY c WHERE 1=1";
	//add by amit
	
	public static final String FETCH_GROUP_ID = "SELECT ORGANIZATION_ID, ORGANIZATION_TYPE FROM TT_ORGANIZATION AS A JOIN TT_GROUP_USER_MAPPING AS B ON A.GROUP_ID = B.GROUP_ID ";
	
	public static final String CHECK_TRAVELER_DUP = "FROM PaxModel pm WHERE pm.branchId = ? and pm.firstName = ? and pm.email =? ";
	
	public static final String CHECK_CORPORATE_DUP = "FROM CorporateModel cm WHERE cm.branchId = ? and cm.corporateName = ? and cm.email =? ";
	
	public static final String FETCH_TRAVELLER = "FROM PaxModel pm where 1=1 ";
	
	public static final String SEARCH_COUNTRY_BY_ID = " FROM CountryBean cb WHERE cb.countryId IN ";
	
	public static final String GET_DIRECTORIES = "SELECT * FROM TT_TS_KEY_VALUE WHERE KEY_STRING IN ('hotel.priceResult.redisCache.timeout','hotel.dbResult.redisCache.timeout')";

	public static final String VALIDATE_HOTEL_CHILD_MODAL = "FROM PaxModel WHERE 1=1";

	public static final String INSERT_KEY_VALUE_OF_CACHE = "INSERT INTO TT_TS_KEY_VALUE VALUES ";	

	public static final String CHECK_CORPORATE_COMPANY_NAME = "FROM CorporateModel cm WHERE cm.corpId = ?";
	
	public static final String FORGOT_PASSWORD_LINK_DETAILS = "FROM UserForgotPasswordLink where agencyId= ? and agentId=? and  linkStatus=0  and linkExpDate>=now() order by creationTime desc";
	
	public static final String FORGOT_PASSWORD_LINK_DETAILS_ADMIN = "FROM UserForgotPasswordLink where agentId=? and  linkStatus=0  and linkExpDate>=now() order by creationTime desc";
	
	public static final String RESET_FORGOT_PASSWORD = "UPDATE User u SET u.password= ? , lastModifiedDate=? WHERE u.userId = ?";
	//
	public static final String GET_DELETE_FOR_PAX_DOCUMENT = "delete from PaxDocumentModel pd where pd.paxModel = ? ";
	//Added By Pramod for SAT-14018 on 08-08-2018
	public static final String FETCH_CORPORATES_BY_USER_ID = "SELECT  cm FROM CorporateModel cm,OrganizationModel om,UserGroupMappingModel op where cm.branchId=om.organizationId and om.groupId= op.groupId and op.userId =? ";

	public static final String NATIONALITY_NAME_QUERY = "SELECT COUNTRY_NAME FROM TT_COUNTRY WHERE COUNTRY_CODE=?";


}
