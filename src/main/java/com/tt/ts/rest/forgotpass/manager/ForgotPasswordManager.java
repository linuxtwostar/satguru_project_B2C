package com.tt.ts.rest.forgotpass.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tt.ts.rest.forgotpass.dao.ForgotPasswordDao;
import com.tt.ts.rest.forgotpass.modal.ForgotPasswordModal;
import com.tt.ts.rest.forgotpass.modal.UserForgotPasswordLink;

@Component
public class ForgotPasswordManager
{
	@Autowired
	private ForgotPasswordDao forgotPasswordDao;
	
	public void updateChangePassword(ForgotPasswordModal forgotPasswordModal) throws Exception
	{
		forgotPasswordDao.updateChangePassword(forgotPasswordModal);
	}
	
	public List<UserForgotPasswordLink> getForgotPasswordLinkDetails(String agencyCode,String userId) throws Exception
	{     
		if(agencyCode!=null && !agencyCode.isEmpty())
			return forgotPasswordDao.getForgotPasswordLinkDetails(agencyCode,userId);
		else
			return forgotPasswordDao.getForgotPasswordLinkDetails(userId);
	}
	
	public int ResetForgotPassword(String userId,String newPassword) throws Exception
	{
		return forgotPasswordDao.ResetForgotPassword(userId,newPassword);
	}
	
	public void updateForgotPasswordLink(UserForgotPasswordLink forgotPasswordLink) throws Exception
	{
		forgotPasswordDao.updateForgotPasswordLink(forgotPasswordLink);
	}
	
	
	

}
