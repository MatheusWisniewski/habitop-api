package com.habitop.resource;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.habitop.model.CheckedDate;
import com.habitop.model.Habit;
import com.habitop.service.CheckedDateService;
import com.habitop.service.HabitService;

@RestController
@RequestMapping("/habits")
public class HabitResource {

	@Autowired
	private HabitService habitService;
	
	@Autowired
	private CheckedDateService checkedDateService;
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateHabit(
		@PathVariable("id") Long id, 
		@RequestBody Habit habit
	) {
		Habit dbHabit = habitService.getHabitById(id);
		
		if (dbHabit == null) {
			return new ResponseEntity<>("Nenhum hábito encontrado com id = " + id, HttpStatus.NOT_FOUND);
		}
		
		try {
			habit.setAppUser(dbHabit.getAppUser());
			habit.setCheckedDates(dbHabit.getCheckedDates());
			Habit updatedHabit = habitService.updateHabit(habit);
			return new ResponseEntity<>(updatedHabit, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Ocorreu um erro.", HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteHabit(
		@PathVariable("id") Long id
	) {
		Habit dbHabit = habitService.getHabitById(id);
		
		if (dbHabit == null) {
			return new ResponseEntity<>("Nenhum hábito encontrado com id = " + id, HttpStatus.NOT_FOUND);
		}
		
		try {
			habitService.deleteHabit(dbHabit);
			return new ResponseEntity<>(null, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Ocorreu um erro.", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/{id}/checked-dates")
	public ResponseEntity<?> createCheckedDate(
		@PathVariable("id") Long id, 
		@RequestBody CheckedDate checkedDate
	) {
		Habit habit = habitService.getHabitById(id);
		
		if (habit == null) {
			return new ResponseEntity<>("Nenhum hábito encontrado com id = " + id, HttpStatus.NOT_FOUND);
		}
		
		try {
			List<CheckedDate> response = new ArrayList<CheckedDate>();
			
			CheckedDate newCheckedDate = checkedDateService.saveCheckedDate(habit, checkedDate);
			response.add(newCheckedDate);
			
			Long previousId = newCheckedDate.getId();
			Integer streak = newCheckedDate.getStreak() + 1;
			
			CheckedDate next = getNextCheckedDate(newCheckedDate);
			
			while (next != null && next.getIsChecked()) {
				next.setPreviousId(previousId);
				next.setStreak(streak);
				newCheckedDate = checkedDateService.updateCheckedDate(next);
				response.add(newCheckedDate);
				previousId = next.getId();
				streak++;
				next = getNextCheckedDate(next);
			}
			
			if (next != null && !next.getIsChecked()) {
				next.setPreviousId(null);
				next.setStreak(streak - 1);
				newCheckedDate = checkedDateService.updateCheckedDate(next);
				response.add(newCheckedDate);
			}
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Ocorreu um erro.", HttpStatus.BAD_REQUEST);
		}
	}
	
	private CheckedDate getNextCheckedDate(CheckedDate c) {
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
		
		return checkedDateService.getCheckedDateByDate(traverser, h.getId());
	}
}
