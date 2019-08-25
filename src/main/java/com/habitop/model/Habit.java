package com.habitop.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Habit implements Serializable {

	private static final long serialVersionUID = 3L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(updatable = true, nullable = false)
	private Long id;
	private String name;
	private String icon;
	private String color;
	private Date createdDate;

	@ElementCollection
	private List<Integer> weekdays = new ArrayList<Integer>();

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "habit_id")
	private List<CheckedDate> checkedDates = new ArrayList<CheckedDate>();

	@JsonIgnore
	@ManyToOne
	private AppUser appUser;

	public Habit() {
	}

	public Habit(Long id, String name, String icon, String color, Date createdDate, List<Integer> weekdays,
			List<CheckedDate> checkedDates, AppUser appUser) {
		super();
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.color = color;
		this.createdDate = createdDate;
		this.weekdays = weekdays;
		this.checkedDates = checkedDates;
		this.appUser = appUser;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public List<Integer> getWeekdays() {
		return weekdays;
	}

	public void setWeekdays(List<Integer> weekdays) {
		this.weekdays = weekdays;
	}

	public List<CheckedDate> getCheckedDates() {
		return checkedDates;
	}

	public void setCheckedDates(List<CheckedDate> checkedDates) {
		this.checkedDates = checkedDates;
	}

	public AppUser getAppUser() {
		return appUser;
	}

	public void setAppUser(AppUser appUser) {
		this.appUser = appUser;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setCheckedDate(CheckedDate checkedDate) {
		this.checkedDates.add(checkedDate);
	}

	public void removeCheckedDate(CheckedDate checkedDate) {
		this.checkedDates.remove(checkedDate);
	}
	
	public void removeAllCheckedDates() {
		this.checkedDates.clear();
	}
}
