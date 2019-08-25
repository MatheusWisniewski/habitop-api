package com.habitop.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class AppUser implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String password;

	@Column(unique = true)
	private String email;
	private Date createdDate;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "app_user_id")
	private List<Habit> habits;

	public AppUser() {
	}

	public AppUser(Long id, String password, String email, Date createdDate, List<Habit> habits) {
		super();
		this.id = id;
		this.password = password;
		this.email = email;
		this.createdDate = createdDate;
		this.habits = habits;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public List<Habit> getHabits() {
		return habits;
	}

	public void setHabit(Habit habit) {
		this.habits.add(habit);
	}

	public void removeHabit(Habit habit) {
		this.habits.remove(habit);
	}
}
