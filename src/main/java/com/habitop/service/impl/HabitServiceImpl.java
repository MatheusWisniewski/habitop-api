package com.habitop.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.habitop.model.AppUser;
import com.habitop.model.CheckedDate;
import com.habitop.model.Habit;
//import com.habitop.model.Weekday;
import com.habitop.repo.HabitRepo;
import com.habitop.service.HabitService;

@Service
@Transactional
public class HabitServiceImpl implements HabitService {

	@Autowired
	private HabitRepo habitRepo;
	
	@Override
	public Habit saveHabit(AppUser appUser, Habit habit) {
		habit.setCreatedDate(new Date());
		habit.setAppUser(appUser);
		appUser.setHabit(habit);
		habitRepo.save(habit);
		return habit;
	}
	
	@Override
	public Habit updateHabit(Habit habit) {
		habitRepo.save(habit);
		return habit;
	}

	@Override
	public Habit getHabitById(Long id) {
		return habitRepo.findHabitById(id);
	}

	@Override
	public List<Habit> findHabitsByUserId(Long id) {
		return habitRepo.findByAppUserId(id);
	}

	@Override
	public void deleteHabit(Habit habit) {
		habit.getAppUser().removeHabit(habit);
	}

}
