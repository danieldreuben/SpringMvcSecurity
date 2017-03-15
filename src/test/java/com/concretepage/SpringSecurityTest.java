package com.concretepage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.concretepage.config.AppConfig;
import com.concretepage.service.IUserService;
import org.springframework.util.Assert;

import static org.springframework.security.access.AccessDecisionVoter.ACCESS_DENIED;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(classes = AppConfig.class)
@WebAppConfiguration
public class SpringSecurityTest {
	@Autowired
	public IUserService userService;
	@Autowired
	public UserDetailsService userDetailsService;
	
	@Test(expected = AuthenticationCredentialsNotFoundException.class)
	public void testOne() {
		userService.methodOne();
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void testTwo() {
		userService.methodTwo("This is Admin");
	}

	@Test
	@WithMockUser(username="ADMIN",roles={"USER","ADMIN","ROLE_TEST"})
	public void getMessageWithMockUserCustomUser() {
		//System.out.println("Principal: " +  SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		//System.out.println("Authorities.."+ SecurityContextHolder.getContext().getAuthentication().getAuthorities());
		//Authorities auths = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.filter(r -> "ROLE_TEST".equals(r))
				.findAny()
				.map(s -> ACCESS_DENIED);
	}

	@Test
	@WithMockUser(username = "ravan")
	public void testThree() {
		User user = new User();
		user.setUserName("ravan");
		userService.methodThree(user);
		System.out.println("test3!!");
	}

	@Test
	@WithUserDetails("ram") 
	public void testFour() {
		userService.methodFour();
	}
}
