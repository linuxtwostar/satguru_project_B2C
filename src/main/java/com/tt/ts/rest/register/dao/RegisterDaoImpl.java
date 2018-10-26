package com.tt.ts.rest.register.dao;

import org.springframework.stereotype.Repository;

import com.tt.nc.core.dao.GenericDAOImpl;
import com.tt.ts.rest.register.model.RegisterModel;

@Repository
public class RegisterDaoImpl extends GenericDAOImpl<Object, Long> implements RegisterDao {

	@Override
	public void registerUser(RegisterModel registerModel) throws Exception {
		if(registerModel!=null)
		{
			makePersistent(registerModel);
		}
		
	}
}
