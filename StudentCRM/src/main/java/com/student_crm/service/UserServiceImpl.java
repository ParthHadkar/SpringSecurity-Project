package com.student_crm.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.student_crm.dao.RoleDao;
import com.student_crm.dao.UserDao;
import com.student_crm.entity.Role;
import com.student_crm.entity.User;
import com.student_crm.model.CrmUser;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	
	public UserServiceImpl() {}

	@Override
	@Transactional("securityTransactionManager")
	public User findUserByUserName(String name) {
		User user = userDao.findUserByUserName(name);
		return user;
	}

	@Override
	@Transactional("securityTransactionManager")
	public void save(CrmUser crmUser) {
		User user = new User();
		user.setUsername(crmUser.getUsername());
		user.setPassword(passwordEncoder.encode(crmUser.getPassword()));
		user.setFirstname(crmUser.getFirstname());
		user.setLastName(crmUser.getLastName());
		user.setEmailId(crmUser.getEmailId());
		user.setRoles(Arrays.asList(roleDao.findRoleByName(crmUser.getRoles())));
		userDao.save(user);
	}
	
	@Override
	@Transactional("securityTransactionManager")
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("username "+username);
		User user = userDao.findUserByUserName(username);
		System.out.println("user "+user);
		if(user == null) {
			System.out.println("user "+user);
			throw new UsernameNotFoundException("Invalidss usernames or passwords");
			//throw new BadCredentialsException("Username not found.");
		}
       /* List<GrantedAuthority > authorities = AuthorityUtils.createAuthorityList();
        
        // if the user selected role other than employee, 
        // then add that one too (multiple roles)
        //Collection<Role> formRole = user.getRoles();
        List<Role> formRole = user.getRoles();
        System.out.println("formRole "+formRole);
        for ( Role formRoles:formRole) {
        		authorities.add(new SimpleGrantedAuthority(formRoles.getName()));
        }*/
        /*return new org.springframework.security.core.userdetails.User(user.getUsername(),
				user.getPassword(), authorities);*/
        /*return new org.springframework.security.core.userdetails.User(user.getUsername(),
		user.getPassword(), buildUserAuthority(user.getRoles()));*/
		
		return new org.springframework.security.core.userdetails.User(user.getUsername(),
				user.getPassword(), mapRolesToAuthorities(user.getRoles()));
		
		
	}
	
	private List<GrantedAuthority> buildUserAuthority(List<Role> userRoles) {
	    Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();

	    for (Role userRole : userRoles) {
	        setAuths.add(new SimpleGrantedAuthority(userRole.getName()));
	    }

	    List<GrantedAuthority> results = new ArrayList<GrantedAuthority>(setAuths);
	    return results;
	}
	
	private Collection<? extends GrantedAuthority>mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new
		SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}
	
}
