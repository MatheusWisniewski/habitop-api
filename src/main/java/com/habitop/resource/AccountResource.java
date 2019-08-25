package com.habitop.resource;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.habitop.model.AppUser;
import com.habitop.model.CheckedDate;
import com.habitop.model.Habit;
import com.habitop.service.AccountService;
import com.habitop.service.CheckedDateService;
import com.habitop.service.HabitService;

@RestController
@RequestMapping("/users")
public class AccountResource {

//	@Autowired
//	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private HabitService habitService;
	
	@Autowired
	private CheckedDateService checkedDateService;
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getUserInfo(
		@PathVariable("id") Long id
	) {
		AppUser user = accountService.findById(id);
		
		if (user == null) {
			return new ResponseEntity<>("Nenhum usuário encontrado.", HttpStatus.OK);
		}
		
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> register(
		@RequestBody HashMap<String, String> request
	) {
		String email = request.get("email");
		
		if (accountService.findByEmail(email) != null) {
			return new ResponseEntity<>("Já existe um usuário com esse email.", HttpStatus.CONFLICT);
		}
		
		String password = request.get("password");
		
		try {
			AppUser user = accountService.saveUser(email, password);
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Ocorreu um erro", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(
		@PathVariable("id") Long id, 
		@RequestBody HashMap<String, String> request
	) {
		AppUser user = accountService.findById(id);
		
		if (user == null) {
			return new ResponseEntity<>("Nenhum usuário encontrado.", HttpStatus.NOT_FOUND);
		}

		String password = request.get("password");
		
		user.setPassword(password);
		
		try {
			accountService.updateUser(user);
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Ocorreu um erro.", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(
		@RequestBody HashMap<String, String> request
	) {
		String email = request.get("email");
		AppUser user = accountService.findByEmail(email);
		
		if (user == null) {
			return new ResponseEntity<>("Email não registrado.", HttpStatus.BAD_REQUEST);
		}
		
		String password = request.get("password");
		
		if (user.getPassword().equals(password)) {
			return new ResponseEntity<>(user, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Senha incorreta.", HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/{id}/habits")
	public ResponseEntity<?> getHabits(
		@PathVariable("id") Long id
	) {
		AppUser user = accountService.findById(id);
		
		if (user == null) {
			return new ResponseEntity<>("Usuário não encontrado.", HttpStatus.NOT_FOUND);
		}
		
		try {
			List<Habit> habits = habitService.findHabitsByUserId(id);
			return new ResponseEntity<>(habits, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Ocorreu um erro.", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/{id}/habits")
	public ResponseEntity<?> createHabit(
		@PathVariable("id") Long id,
		@RequestBody Habit habit
	) {
		AppUser user = accountService.findById(id);
		
		if (user == null) {
			return new ResponseEntity<>("Usuário não encontrado.", HttpStatus.NOT_FOUND);
		}
		
		try {
			Habit newHabit = habitService.saveHabit(user, habit);
			return new ResponseEntity<>(newHabit, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Ocorreu um erro.", HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/{id}/checked-dates")
	public ResponseEntity<?> createHabit(
		@PathVariable("id") Long id, 
		@RequestParam(value = "startDate", required = true) String startDate,
		@RequestParam(value = "endDate", required = false) String endDate
	) {
		AppUser user = accountService.findById(id);
		
		if (user == null) {
			return new ResponseEntity<>("Nenhum usuário encontrado com id = " + id, HttpStatus.NOT_FOUND);
		}
		
		if (startDate == null) {
			return new ResponseEntity<>("Providencie uma data de início", HttpStatus.BAD_REQUEST);
		}
		
		try {
			if (endDate == null) {
				List<CheckedDate> checkedDates = checkedDateService.getCheckedDatesWithDate(
						user, 
						new SimpleDateFormat("yyyy-MM-dd").parse(startDate)
				);
				return new ResponseEntity<>(checkedDates, HttpStatus.OK);
			} else {
				List<CheckedDate> checkedDates = checkedDateService.getCheckedDatesWithDateRange(
						user, 
						new SimpleDateFormat("yyyy-MM-dd").parse(startDate), 
						new SimpleDateFormat("yyyy-MM-dd").parse(endDate)
				);
				return new ResponseEntity<>(checkedDates, HttpStatus.OK);
			}
			
		} catch (Exception e) {
			return new ResponseEntity<>("Ocorreu um erro.", HttpStatus.BAD_REQUEST);
		}
	}
}
