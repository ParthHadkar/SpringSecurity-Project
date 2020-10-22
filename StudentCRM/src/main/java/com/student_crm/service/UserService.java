package com.student_crm.service;


import org.springframework.security.core.userdetails.UserDetailsService;

import com.student_crm.entity.User;
import com.student_crm.model.CrmUser;


public interface UserService  extends UserDetailsService{

	public User findUserByUserName(String name);
	public void save(CrmUser crmUser);

}
