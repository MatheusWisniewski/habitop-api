package com.habitop.service;

import java.util.List;

import com.habitop.model.AppUser;
import com.habitop.model.Habit;

public interface HabitService {

	public Habit saveHabit(AppUser appUser, Habit habit);
	
	public Habit updateHabit(Habit newHabit);
	
	public Habit getHabitById(Long id);
	
	public List<Habit> findHabitsByUserId(Long id);
	
	public void deleteHabit(Habit habit);
}
