package com.tt.ts.rest.forgotpass.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tt.ts.rest.forgotpass.modal.ForgotPasswordModal;
import com.tt.ts.rest.forgotpass.modal.UserForgotPasswordLink;

@Repository
public interface ForgotPasswordDao
{

	void updateChangePassword(ForgotPasswordModal forgotPasswordModal) throws Exception;
	List<UserForgotPasswordLink> getForgotPasswordLinkDetails(String agencyCode,String userId) throws Exception;
	List<UserForgotPasswordLink> getForgotPasswordLinkDetails(String userId) throws Exception;
	public int ResetForgotPassword(String userId,String newPassword) throws Exception;
	public void updateForgotPasswordLink(UserForgotPasswordLink forgotPasswordLink) throws Exception;
	
}
