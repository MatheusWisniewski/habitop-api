package com.habitop.service;

import java.util.Date;
import java.util.List;

import com.habitop.model.AppUser;
import com.habitop.model.CheckedDate;
import com.habitop.model.Habit;

public interface CheckedDateService {
	
	public List<CheckedDate> getCheckedDatesWithDate(AppUser appUser, Date date);
	
	public List<CheckedDate> getCheckedDatesWithDateRange(AppUser appUser, Date startDate, Date endDate);

	public CheckedDate saveCheckedDate(Habit habit, CheckedDate checkedDate);
	
	public CheckedDate updateCheckedDate(CheckedDate checkedDate);
	
	public CheckedDate getCheckedDateById(Long id);
	
	public CheckedDate getCheckedDateByPreviousId(Long previousId);
	
	public CheckedDate getCheckedDateByDate(Date date, Long habitId);
}
