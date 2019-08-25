package com.habitop.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.habitop.model.CheckedDate;

public interface CheckedDateRepo extends JpaRepository<CheckedDate, Long> {
	
	@Query("SELECT c \n" + 
			"		FROM CheckedDate c \n" + 
			"		INNER JOIN Habit h\n" + 
			"			ON c.habit.id = h.id\n" + 
			"	    INNER JOIN AppUser a\n" + 
			"			ON h.appUser.id = a.id\n" + 
			"		WHERE a.id = :app_user_id \n" +
			"			AND date_format(date(c.date), '%Y-%m-%d') \n" + 
			"				BETWEEN date_format(:start_date, '%Y-%m-%d') AND date_format(:end_date, '%Y-%m-%d')")
	public List<CheckedDate> findAllInDateRange(@Param("app_user_id") Long id, @Param("start_date") Date startDate, @Param("end_date") Date endDate);
	
	@Query("SELECT c FROM CheckedDate c WHERE c.id=:x")
	public CheckedDate findCheckedDateById(@Param("x") Long id);
	
	@Query("SELECT c FROM CheckedDate c WHERE c.previousId=:x")
	public CheckedDate findCheckedDateByPreviousId(@Param("x") Long previousId);
	
	@Query("SELECT c FROM CheckedDate c "
			+ "WHERE date_format(date(c.date), '%Y-%m-%d') = date_format(date(:date), '%Y-%m-%d')"
			+ "		AND c.habit.id = :habit_id")
	public CheckedDate findCheckedDateByDate(@Param("date") Date date, @Param("habit_id") Long habitId);
	
	@Modifying
	@Query("DELETE CheckedDate WHERE id=:x")
	public void deleteCheckedDateById(@Param("x") Long id);
}
