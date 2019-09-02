package com.habitop.service.impl;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.habitop.model.AppUser;
import com.habitop.repo.AppUserRepo;
import com.habitop.service.AccountService;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AppUserRepo appUserRepo;

	@Override
	public AppUser saveUser(String email, String password) {
		AppUser appUser = new AppUser();
		appUser.setEmail(email);
		appUser.setPassword(password);
		appUser.setCreatedDate(new Date());
		appUserRepo.save(appUser);
		return appUser;
	}

	@Override
	public AppUser findByEmail(String email) {
		return appUserRepo.findByEmail(email);
	}

	@Override
	public void updateUser(AppUser appUser) {
		appUserRepo.save(appUser);
	}

	@Override
	public AppUser findById(Long id) {
		return appUserRepo.findUserById(id);
	}

	@Override
	public void deleteUser(AppUser appUser) {
		appUserRepo.delete(appUser);
	}
}
