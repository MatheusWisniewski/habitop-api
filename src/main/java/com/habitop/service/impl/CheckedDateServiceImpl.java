package com.habitop.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
		return checkedDateRepo.findAllInDateRange(appUser.getId(), DateUtils.addDays(date, -7),
				DateUtils.addDays(date, 1));
	}

	public List<CheckedDate> getCheckedDatesWithDateRange(AppUser appUser, Date startDate, Date endDate) {
		return checkedDateRepo.findAllInDateRange(appUser.getId(), DateUtils.addDays(startDate, -7),
				DateUtils.addDays(endDate, 1));
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
	public CheckedDate getCheckedDateByDate(Date date, Long habitId) {
		return checkedDateRepo.findCheckedDateByDate(date, habitId);
	}

	@Override
	public CheckedDate getNextCheckedDate(CheckedDate c) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(c.getDate());
		Integer weekday = calendar.get(Calendar.DAY_OF_WEEK) - 1;

		Habit h = c.getHabit();
		Collections.sort(h.getWeekdays());

		Integer weekdayIndex = h.getWeekdays().indexOf(weekday);

		Integer nextIndex;

		if (weekdayIndex == h.getWeekdays().size() - 1) {
			nextIndex = 0;
		} else {
			nextIndex = weekdayIndex + 1;
		}

		Integer nextWeekday = h.getWeekdays().get(nextIndex);

		Date traverser = DateUtils.addDays(c.getDate(), 1);
		calendar.setTime(traverser);
		Integer traverserWeekday = calendar.get(Calendar.DAY_OF_WEEK) - 1;

		while (traverserWeekday != nextWeekday) {
			traverser = DateUtils.addDays(traverser, 1);
			calendar.setTime(traverser);
			traverserWeekday = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		}

		return checkedDateRepo.findCheckedDateByDate(traverser, h.getId());
	}

	@Override
	public List<CheckedDate> updateSubsequentCheckedDates(CheckedDate checkedDate) {
		List<CheckedDate> updatedCheckedDates = new ArrayList<CheckedDate>();

		Boolean isAdding = checkedDate.getIsChecked();
		Integer streak;

		if (isAdding) {
			streak = checkedDate.getStreak() + 1;
		} else {
			streak = 1;
		}

		CheckedDate next = getNextCheckedDate(checkedDate);

		while (next != null && next.getIsChecked()) {
			next.setStreak(streak);
			checkedDate = updateCheckedDate(next);
			updatedCheckedDates.add(checkedDate);
			streak++;
			next = getNextCheckedDate(next);
		}

		if (next != null && !next.getIsChecked()) {
			next.setStreak(streak - 1);
			checkedDate = updateCheckedDate(next);
			updatedCheckedDates.add(checkedDate);
		}

		return updatedCheckedDates;

	}
}
