package com.habitop.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.habitop.model.Habit;

public interface HabitRepo extends JpaRepository<Habit, Long> {

	@Query("SELECT h FROM Habit h WHERE h.appUser.id=:app_user_id ORDER BY h.createdDate")
	public List<Habit> findByAppUserId(@Param("app_user_id") Long id);

	@Query("SELECT h FROM Habit h WHERE h.id=:x")
	public Habit findHabitById(@Param("x") Long id);

	@Modifying
	@Query("DELETE Habit WHERE id=:x")
	public void deleteHabitById(@Param("x") Long id);
}