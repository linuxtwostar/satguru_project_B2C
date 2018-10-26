package com.tt.ws.rest.login.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tt.nc.common.util.TTLog;
import com.tt.nc.user.manager.UserManager;
import com.tt.nc.user.model.User;
import com.tt.nc.user.util.UserUtil;
import com.tt.ts.common.modal.ResultBean;
import com.tt.ts.organization.model.OrganizationModel;
import com.tt.ts.rest.agent.manager.AgentManager;

@Service
public class LoginRestService {
    @Autowired
    private UserManager userManager;
    
    @Autowired
    private AgentManager agentManager;
    
    public static final String INVALIDPASS="Invalid credentials, Please try again.";
    public static final String INVALIDAUTH="Sorry, Your account is not authorized. Please contact to your administrator.";
    
    private static final  String algo="PBEWITHSHA256AND256BITAES-CBC-BC";

    public  ResultBean validateUser(String uName, String password, String agencyCode) {

        ResultBean response = new ResultBean();
        try{
            User user = new User();
            user.setUserAlias(uName);
            user.setPassword(password);
            List<User> userList;
            String password1 = user.getPassword();
            String hql = UserUtil.preparedQueryByUserName(user.getUserAlias());
            userList = userManager.getUsersByQuery(hql);// checks user exist
            if (userList != null && userList.size() == 1)
            { // user Exist
                User loggedUser = userList.get(0);
            
              //getUserMapping changed this method to getUserMappingAuthorized; swati(to check status, approval status and fact approval status)
                List<OrganizationModel> agentUserMapping= agentManager.getUserMappingAuthorized(loggedUser.getUserId(), agencyCode);
                if(agentUserMapping!=null && !agentUserMapping.isEmpty()){
                	OrganizationModel mappingModel = agentUserMapping.get(0);
                	if (password1.trim().equals(UserUtil.decodeString(loggedUser.getPassword()).trim())) {
                
                	
                	int factApprovalStatus = mappingModel.getFactApprovalStatus();
                	int status = mappingModel.getStatus();
                	int approvalStatus = mappingModel.getApprovalStatus();
                	if(mappingModel.getOrganizationId().equals(agencyCode)){
                		
                	
                	
                	if(factApprovalStatus == 1 && status==1 && approvalStatus == 1 ){
                		 boolean isAgencyCodeExist;
                         if(!agentUserMapping.isEmpty()){

                         	if(mappingModel.getOrganizationId().equals(agencyCode))
                                 isAgencyCodeExist=true;
                             else
                                 isAgencyCodeExist=false;

                         }else{
                         	response.setResultString(INVALIDPASS);// Invalid password !
                 			response.setResultObject(null);
                 			return response; 
                         }
                         if(!agentUserMapping.isEmpty() && mappingModel.getOrganizationId().equals(agencyCode))
                             isAgencyCodeExist=true;
                         else
                             isAgencyCodeExist=false;
                         
                         response = validateResponse(response,loggedUser,password1,isAgencyCodeExist);
                	}
                	else
                		
                	{
                		response.setResultString(INVALIDAUTH);// Invalid password !
             			response.setResultObject(null);
             			return response; 
                	}
                }
                	else{
                		response.setResultString(INVALIDPASS);// Invalid password !
             			response.setResultObject(null);
             			return response; 
                	}
                	}
                	else{
                		response.setResultString(INVALIDPASS);// Invalid password !
             			response.setResultObject(null);
             			return response; 
                	}
                }else{
                	
                	// WHEN NO DATA FOUND FOR USER ID
                	response.setResultString(INVALIDPASS);// Invalid password !
         			response.setResultObject(null);
         			return response; 
                }
            }
          
            else
            {
                response.setResultString(INVALIDPASS);//User does not exist
            }
        }catch (Exception e)
        {
            response.setIserror(true);
            TTLog.info(0, "[LoginRestController][loginCheck] Total User  :::::::::" + e);
        }
        return response;
    
    }
    
	public ResultBean validateForgotCredentials(String uName, String agencyCode) {

		ResultBean response = new ResultBean();
		try {
			
			List<User> userList;
			String hql = UserUtil.preparedQueryByUserName(uName.trim());
			userList = userManager.getUsersByQuery(hql);// checks user exist
			if (userList != null && userList.size() == 1) {   
				User loggedUser = userList.get(0);
				List<OrganizationModel> agentUserMapping = agentManager.getUserMappingAuthorized(loggedUser.getUserId(),agencyCode);
				if (agentUserMapping != null && !agentUserMapping.isEmpty()) {
					OrganizationModel mappingModel = agentUserMapping.get(0);
					int factApprovalStatus = mappingModel.getFactApprovalStatus();
					int status = mappingModel.getStatus();
					int approvalStatus = mappingModel.getApprovalStatus();

					if (mappingModel.getOrganizationId().equals(agencyCode)) {

						if (factApprovalStatus == 1 && status == 1 && approvalStatus == 1) {
							boolean isAgencyCodeExist;
							if (!agentUserMapping.isEmpty()) {

								if (mappingModel.getOrganizationId().equals(agencyCode))
									isAgencyCodeExist = true;
								else
									isAgencyCodeExist = false;

							} else {
								response.setResultString(INVALIDPASS);// Invalid// password// !
								response.setResultObject(null);
								return response;
							}
							if (!agentUserMapping.isEmpty() && mappingModel.getOrganizationId().equals(agencyCode))
								isAgencyCodeExist = true;
							else
								isAgencyCodeExist = false;

							response = validateForgotResponse(response,loggedUser, isAgencyCodeExist);
						} else {
							response.setResultString(INVALIDAUTH);// Invalid// password// !
							response.setResultObject(null);
							return response;
						}
					} else {
						response.setResultString(INVALIDPASS);// Invalid// password !
						response.setResultObject(null);
						return response;
					}
				} else {
					// WHEN NO DATA FOUND FOR USER ID
					response.setResultString(INVALIDPASS);// Invalid password !
					response.setResultObject(null);
					return response;
				}
			} else {
				response.setResultString(INVALIDPASS);// User does not exist
			}
		} catch (Exception e) {
			response.setIserror(true);
			TTLog.info(0,"[LoginRestController][validateForgotCredentials] Total User  :::::::::"+ e);
		}
		return response;

	}
    
    
    private ResultBean validateResponse( ResultBean response, User loggedUser, String password1, boolean isAgencyCodeExist) throws UnsupportedEncodingException {
		boolean flag = true;
    	if(!isAgencyCodeExist){
    		response.setResultString(INVALIDPASS);// Invalid password !
			response.setResultObject(null);
			flag = false;
			return response; 
    	}
		if (password1.trim().equals(
				UserUtil.decodeString(loggedUser.getPassword()).trim())) {
			response.setResultString("Login Successful");
			response.setResultObject(loggedUser);
			
		} else {
			response.setResultString(INVALIDPASS);// Invalid password !
			response.setResultObject(null);
			flag = false;
		}
			
		if (flag && ((loggedUser.getDisableSignIn() == 0 || loggedUser.getUserStatus() == 0) || loggedUser.getUserType() != 3 || !isAgencyCodeExist)) {
			response.setResultString(INVALIDAUTH);// Not authorized
			response.setResultObject(null);
		}
		
        return response;
    }
    
    
    private ResultBean validateForgotResponse( ResultBean response, User loggedUser, boolean isAgencyCodeExist) throws UnsupportedEncodingException {
		boolean flag = true;
    	if(!isAgencyCodeExist){
    		response.setResultString(INVALIDPASS);// Invalid password !
			response.setResultObject(null);
			flag = false;
			return response; 
    	}
		if (flag && ((loggedUser.getDisableSignIn() == 0 || loggedUser.getUserStatus() == 0) || loggedUser.getUserType() != 3 || !isAgencyCodeExist)) {
			response.setResultString(INVALIDAUTH);// Not authorized
			response.setResultObject(null);
		}
		return response;
        }
    
    
    public static String encrypt(String password)
    {
    	StringBuilder hexString = new StringBuilder();
        try
        {
            KeySpec ks=new PBEKeySpec(password.toCharArray());
            SecretKeyFactory skf=SecretKeyFactory.getInstance(algo);
            SecretKey key=skf.generateSecret(ks);
            MessageDigest md=MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] digest=md.digest();
            byte[] salt=Arrays.copyOf(digest, 16);
            AlgorithmParameterSpec aps=new PBEParameterSpec(salt, 20);
            Cipher cipher=Cipher.getInstance(algo);
            cipher.init(Cipher.ENCRYPT_MODE, key, aps);

            for (int i = 0; i < digest.length; i++) {
                String hex = Integer.toHexString(0xff & digest[i]);
                if(hex.length() == 1) 
                	hexString.append('0');
                hexString.append(hex);
            }
        }
        catch(Exception e)
        {
        	  TTLog.printStackTrace(0, e);
        }
        return hexString.toString();
    }
     
    public static String decrypt( String password)
    {
    	StringBuilder hexString = new StringBuilder();
        try
        {
            KeySpec ks=new PBEKeySpec(password.toCharArray());
            SecretKeyFactory skf=SecretKeyFactory.getInstance(algo);
            SecretKey key=skf.generateSecret(ks);
            MessageDigest md=MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] digest=md.digest();
            byte[] salt=Arrays.copyOf(digest, 16);
            AlgorithmParameterSpec aps=new PBEParameterSpec(salt, 20);
            Cipher cipher=Cipher.getInstance(algo);
            cipher.init(Cipher.DECRYPT_MODE, key, aps);
            for (int i = 0; i < digest.length; i++) {
                String hex = Integer.toHexString(0xff & digest[i]);
                if(hex.length() == 1) 
                	hexString.append('0');
                hexString.append(hex);
            }
        }
        catch(Exception e)
        {
            TTLog.printStackTrace(0, e);
        }
        return hexString.toString();
    }
    public static String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
           throw new RuntimeException(ex);
        }
    }
    
    public ResultBean validateForgotCredentialsAdmin(String userName) {

		ResultBean response = new ResultBean();
		try {
			User user = new User();
			user.setUserAlias(userName);
			String hql = UserUtil.preparedQueryByUserNameType(user.getUserAlias());			
			List<User> userList = userManager.getUsersByQuery(hql);// checks user exist
			if (userList!= null && !userList.isEmpty() && userList.size()>0 ) {
				int status=userList.get(0).getUserStatus();
				if(status==1){
					User loggedUser = userList.get(0);
					if(loggedUser.getUserType()!=1)
					{
						List<OrganizationModel> agentUserMapping = agentManager.getUserMappingAuthorized(loggedUser.getUserId(),"");
						if (agentUserMapping != null && !agentUserMapping.isEmpty()) {
							OrganizationModel mappingModel = agentUserMapping.get(0);
							response.setPosObjectId(mappingModel.getOrganizationId());
						}
						response.setResultString("");// Invalid password !
						response.setResultObject(loggedUser);
						return response;
					} 
					else
					{
						response.setPosObjectId("admin");
						response.setResultObject(loggedUser);
						return response;
					}
				}else{
					response.setResultString(INVALIDAUTH);// Invalid password !
					response.setResultObject(null);
					return response;
					
				}
				
			}
			else {
				// WHEN NO DATA FOUND FOR USER ID
				response.setResultString(INVALIDPASS);// Invalid password !
				response.setResultObject(null);
				return response;
		}
		} catch (Exception e) {
			response.setIserror(true);
			TTLog.info(0,"[LoginRestController][validateForgotCredentials] Total User  :::::::::"+ e);
		}
		return response;

	}
    
}