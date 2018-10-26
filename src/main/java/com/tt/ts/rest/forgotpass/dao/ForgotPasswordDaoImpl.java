package com.tt.ts.rest.forgotpass.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.tt.nc.core.dao.GenericDAOImpl;
import com.tt.nc.user.util.UserUtil;
import com.tt.ts.rest.common.util.QueryConstantRest;
import com.tt.ts.rest.forgotpass.modal.ForgotPasswordModal;
import com.tt.ts.rest.forgotpass.modal.UserForgotPasswordLink;

@Repository
public class ForgotPasswordDaoImpl extends GenericDAOImpl<Object, Long> implements ForgotPasswordDao
{

	@Override
	public void updateChangePassword(ForgotPasswordModal forgotPasswordModal) throws Exception
	{
		
		if(forgotPasswordModal!=null)
		{
			String hqlQuery = QueryConstantRest.FORGOT_PASSWORD ;
			List<Object> parameterList =new ArrayList<>();
			parameterList.add(new String(UserUtil.encodeString(forgotPasswordModal.getNewpass())));
			parameterList.add(forgotPasswordModal.getUserName());
			updateWithHQL(hqlQuery, parameterList);
		}
		
	}
	
	public List<UserForgotPasswordLink> getForgotPasswordLinkDetails(String agencyCode, String userId) throws Exception
	{
		String hqlQuery = QueryConstantRest.FORGOT_PASSWORD_LINK_DETAILS ;	
		List<Object> paraList = new ArrayList<>();
		paraList.add(agencyCode);
		paraList.add(userId);
		
		return fetchWithHQL(hqlQuery,paraList);
	}
	
	public List<UserForgotPasswordLink> getForgotPasswordLinkDetails(String userId) throws Exception
	{
		String hqlQuery = QueryConstantRest.FORGOT_PASSWORD_LINK_DETAILS_ADMIN ;	
		List<Object> paraList = new ArrayList<>();
		paraList.add(userId);
		
		return fetchWithHQL(hqlQuery,paraList);
	}
	
	public int  ResetForgotPassword(String userId,String newPassword) throws Exception
	{
	String hqlQuery = QueryConstantRest.RESET_FORGOT_PASSWORD ;	
	List<Object> paraList = new ArrayList<>();
	paraList.add(new String(UserUtil.encodeString(newPassword))); 
	paraList.add(new Date());  
	paraList.add(userId);
	return updateReturnWithHQL(hqlQuery,paraList);
	}
	
	public void updateForgotPasswordLink(UserForgotPasswordLink forgotPasswordLink) throws Exception
	{
		saveorupdate(forgotPasswordLink);
	}

}
