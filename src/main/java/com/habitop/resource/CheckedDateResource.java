package com.habitop.resource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.habitop.model.CheckedDate;
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
			CheckedDate updatedCheckedDate = checkedDateService.updateCheckedDate(checkedDate);
			response.add(updatedCheckedDate);
			
			response.addAll(checkedDateService.updateSubsequentCheckedDates(updatedCheckedDate));
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Ocorreu um erro.", HttpStatus.BAD_REQUEST);
		}
	}
}
