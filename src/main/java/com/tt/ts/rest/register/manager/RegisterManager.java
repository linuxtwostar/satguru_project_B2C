package com.tt.ts.rest.register.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tt.ts.rest.register.dao.RegisterDao;
import com.tt.ts.rest.register.model.RegisterModel;

@Component
public class RegisterManager {
	@Autowired
	private RegisterDao registerDao;
	
	public void registerUser(RegisterModel registerModal) throws Exception
	{
		registerDao.registerUser(registerModal);
	}
}
