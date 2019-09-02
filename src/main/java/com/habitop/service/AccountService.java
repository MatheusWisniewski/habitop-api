package com.habitop.service;

import com.habitop.model.AppUser;

public interface AccountService {

	public AppUser saveUser(String email, String password);

	public AppUser findByEmail(String email);

	public void updateUser(AppUser appUser);

	public AppUser findById(Long id);

	public void deleteUser(AppUser appUser);

}
