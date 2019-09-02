package com.habitop.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.habitop.model.AppUser;

public interface AppUserRepo extends JpaRepository<AppUser, Long> {

	@Query("SELECT a FROM AppUser a WHERE a.email=:x")
	public AppUser findByEmail(@Param("x") String email);

	@Query("SELECT a FROM AppUser a WHERE a.id=:x")
	public AppUser findUserById(@Param("x") Long id);

}
