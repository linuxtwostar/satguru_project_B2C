package com.tt.ts.rest.forgotpass.service;

import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.tt.nc.common.util.TTLog;
import com.tt.nc.user.manager.UserManager;
import com.tt.nc.user.model.User;
import com.tt.nc.user.util.UserUtil;
import com.tt.ts.common.errorConstant.ErrorCodeContant;
import com.tt.ts.common.modal.ResultBean;
import com.tt.ts.rest.forgotpass.manager.ForgotPasswordManager;
import com.tt.ts.rest.forgotpass.modal.ForgotPasswordModal;
import com.tt.ts.rest.forgotpass.modal.UserForgotPasswordLink;

@Service
public class ForgotPasswordService
{

	
	@Autowired
	private UserManager userManager;
	
	@Autowired
	private ForgotPasswordManager forgotPasswordManager;
	
	public ResultBean forgotPassOrChange(ForgotPasswordModal forgotPasswordModal)
	{
		ResultBean resultBean =new ResultBean();
		try
		{
			if(forgotPasswordModal!=null )
			{
				String hql = UserUtil.preparedQueryByUserName(forgotPasswordModal.getUserName());
				List<User> userList =userManager.getUsersByQuery(hql); // check user exist
				if (userList!=null && userList.size() == 1)
				{
					// user Exist
					User loggedUser = userList.get(0);
					String userPassword = loggedUser.getPassword();
					int disableStatus = loggedUser.getDisableSignIn();
					int userStatus = loggedUser.getUserStatus();
					if (disableStatus == 0 || userStatus == 0)
					{
						resultBean.setErrorMessage("Not authorized");
					}
					else if (forgotPasswordModal.getOldPass().equals(UserUtil.decodeString(userPassword)))
					{
						forgotPasswordManager.updateChangePassword(forgotPasswordModal);
						resultBean.setResultString("Password reset successfully");
					}
					else
					{
						resultBean.setErrorMessage( "Invalid password !");
					}
			    }
			}
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			String errorCode = ErrorCodeContant.DATA_NOT_FOUND_KC;
			String errorMessage = ResourceBundle.getBundle("ApplicationResources", LocaleContextHolder.getLocale()).getString(errorCode);
			resultBean.setErrorCode(errorCode);
			resultBean.setErrorMessage(errorMessage);
			TTLog.printStackTrace(0, errorCode, e);
		}
		return resultBean;
	}
	
	 public List<UserForgotPasswordLink> getForgotPasswordLink(String agencyCode,String userId)
	 {  
		 ResultBean resultBean =new ResultBean();
		 List<UserForgotPasswordLink> forgotPasswordLinks=null;
		try {
			forgotPasswordLinks = forgotPasswordManager.getForgotPasswordLinkDetails(agencyCode, userId);
			resultBean.setIserror(false);
		} catch (Exception e) {
			resultBean.setIserror(true);
			TTLog.printStackTrace(0, e);
		}
		return forgotPasswordLinks;
	}
	    
	 public ResultBean ResetForgotPassword(String userId,String newPassword) throws Exception
	 {
		ResultBean resultBean = new ResultBean();
		try {
			int i = forgotPasswordManager.ResetForgotPassword(userId,
					newPassword);
			resultBean.setResultInteger(i);
			resultBean.setIserror(false);
		} catch (Exception e) {
			resultBean.setIserror(true);
			TTLog.printStackTrace(0, e);

		}
		return resultBean;

	}
	 
	 public boolean updateForgotPasswordLink(UserForgotPasswordLink updateForgotPasswordLink) throws Exception
	 {
		try {
			 forgotPasswordManager.updateForgotPasswordLink(updateForgotPasswordLink);
			 return true;
		} catch (Exception e) {
			TTLog.printStackTrace(0, e);
		}
		return false;

	}
	 
}