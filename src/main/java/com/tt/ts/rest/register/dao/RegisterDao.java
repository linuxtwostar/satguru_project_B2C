package com.tt.ts.rest.register.dao;

import org.springframework.stereotype.Repository;

import com.tt.ts.rest.register.model.RegisterModel;

@Repository
public interface RegisterDao {
	
	void registerUser(RegisterModel registerModel) throws Exception;
	
}
