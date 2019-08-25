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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.habitop.model.CheckedDate;
import com.habitop.model.Habit;
import com.habitop.service.CheckedDateService;

@RestController
@RequestMapping("/checked-dates")
public class CheckedDateResource {

	
	@Autowired
	private CheckedDateService checkedDateService;
	

	@PutMapping("/{id}")
	public ResponseEntity<?> updateCheckedDate(
			@PathVariable("id") Long id, 
			@RequestBody CheckedDate checkedDate
	) {
		
		CheckedDate dbCheckedDate = checkedDateService.getCheckedDateById(id);
		
		if (dbCheckedDate == null) {
			return new ResponseEntity<>("Nenhum check encontrado com id = " + id, HttpStatus.NOT_FOUND);
		}
		
		try {
			List<CheckedDate> response = new ArrayList<CheckedDate>();
			checkedDate.setHabit(dbCheckedDate.getHabit());
			if (!checkedDate.getIsChecked()) {
				checkedDate.setPreviousId(null);
			} 
			CheckedDate updatedCheckedDate = checkedDateService.updateCheckedDate(checkedDate);
			response.add(updatedCheckedDate);
			
			// run loop logic to change streaks and previous
			Boolean isAdding = checkedDate.getIsChecked();
			
			if (isAdding) {
				Long previousId = checkedDate.getId();
				Integer streak = checkedDate.getStreak() + 1;
				
				CheckedDate next = getNextCheckedDate(checkedDate);
				
				while (next != null && next.getIsChecked()) {
					next.setPreviousId(previousId);
					next.setStreak(streak);
					updatedCheckedDate = checkedDateService.updateCheckedDate(next);
					response.add(updatedCheckedDate);
					previousId = next.getId();
					streak++;
					next = getNextCheckedDate(next);
				}
				
				if (next != null && !next.getIsChecked()) {
					next.setPreviousId(null);
					next.setStreak(streak - 1);
					updatedCheckedDate = checkedDateService.updateCheckedDate(next);
					response.add(updatedCheckedDate);
				}
			} else { 
				Long previousId = checkedDate.getId();
				CheckedDate next = checkedDateService.getCheckedDateByPreviousId(previousId);
				previousId = null;
				Integer streak = 1;
				
				while (next != null) {
					next.setPreviousId(previousId);
					if (next.getIsChecked()) {
						next.setStreak(streak);
					} else {
						next.setStreak(0);
					}
					updatedCheckedDate = checkedDateService.updateCheckedDate(next);
					response.add(updatedCheckedDate);
					previousId = next.getId();
					streak++;
					next = checkedDateService.getCheckedDateByPreviousId(previousId);
				}
				
				next = getNextCheckedDate(checkedDate);
				
				if (next != null && !next.getIsChecked()) {
					next.setPreviousId(null);
					next.setStreak(0);
					updatedCheckedDate = checkedDateService.updateCheckedDate(next);
					response.add(updatedCheckedDate);
				}
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
