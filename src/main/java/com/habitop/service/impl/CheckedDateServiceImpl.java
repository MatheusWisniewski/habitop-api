package com.habitop.service.impl;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.habitop.model.AppUser;
import com.habitop.model.CheckedDate;
import com.habitop.model.Habit;
import com.habitop.repo.CheckedDateRepo;
import com.habitop.service.CheckedDateService;

@Service
@Transactional
public class CheckedDateServiceImpl implements CheckedDateService {

	@Autowired
	private CheckedDateRepo checkedDateRepo;
	
	public List<CheckedDate> getCheckedDatesWithDate(AppUser appUser, Date date) {
		return checkedDateRepo.findAllInDateRange(appUser.getId(), DateUtils.addDays(date, -7), DateUtils.addDays(date, 1));
	}
	
	public List<CheckedDate> getCheckedDatesWithDateRange(AppUser appUser, Date startDate, Date endDate) {
		return checkedDateRepo.findAllInDateRange(appUser.getId(), DateUtils.addDays(startDate, -7), DateUtils.addDays(endDate, 1));
	}
	
	@Override
	public CheckedDate saveCheckedDate(Habit habit, CheckedDate checkedDate) {
		checkedDate.setHabit(habit);
		habit.setCheckedDate(checkedDate);
		checkedDateRepo.save(checkedDate);
		return checkedDate;
	}
	
	@Override
	public CheckedDate updateCheckedDate(CheckedDate checkedDate) {
		checkedDateRepo.save(checkedDate);
		return checkedDate;
	}
	
	@Override
	public CheckedDate getCheckedDateById(Long id) {
		return checkedDateRepo.findCheckedDateById(id);
	}
	
	@Override
	public CheckedDate getCheckedDateByPreviousId(Long previousId) {
		return checkedDateRepo.findCheckedDateByPreviousId(previousId);
	}

	@Override
	public CheckedDate getCheckedDateByDate(Date date, Long habitId) {
		return checkedDateRepo.findCheckedDateByDate(date, habitId);
	}
}
