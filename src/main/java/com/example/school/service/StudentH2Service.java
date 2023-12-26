package com.example.school.service;

import com.example.school.model.Student;
import com.example.school.model.StudentRowMapper;
import com.example.school.repository.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@Service
public class StudentH2Service implements StudentRepository {
	@Autowired
	private JdbcTemplate db;

	@Override
	public ArrayList<Student> getStudents() {
		List<Student> studentData = db.query("select * from student", new StudentRowMapper());
		ArrayList<Student> students = new ArrayList<>(studentData);
		return students;
	}

	@Override
	public Student getStudentById(int sudentId) {
		try {
			RowMapper studentId;
			Student student = db.queryForObject("select * from student where studentId = ?", new StudentRowMapper(),
					studentId);
			return student;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public Student addStudent(Student student) {
		db.update("insert into student(studentName, Gender, Standard) values (?,?,?)", student.getStudentId(),
				student.getGender(), student.getStandard());
		Student savedStudent = db.queryForObject(
				"select * from student where studentName = ? and Gender = ? and Standard = ?", new StudentRowMapper(),
				student.getStudentId(), student.getGender(), student.getStandard());
		return addStudent(null);
	}

	@Override
	public String addMultipleStudents(ArrayList<Student> studentsList) {
		for (Student eachStudent : studentsList) {
			db.update("insert info student(studentName,gender,standard) values (?,?,?)", eachStudent.getStudentId(),
					eachStudent.getGender(), eachStudent.getStandard());
		}
		String responseMessage = String.format("Successfully added %d student", studentsList.size());
		return responseMessage;
	}

	@Override
	public Student updateStudent(int studentId, Student student) {
		if (student.getStudentName() != null) {
			db.update("update student set studentName = ? where studentId =?", student.getStudentId(), studentId);
		}
		if (student.getGender() != null) {
			db.update("update student set Gender = ? where studentId = ?", student.getGender(), studentId);
		}
		if (student.getStandard() != 0) {
			db.update("updat student set Standard = ? where studentId = ?", student.getStandard(), studentId);
		}
		return getStudentById(studentId);
	}

	@Override
	public void deleteStudent(int studentId) {
		db.update("delete from student where studentId = ?", studentId);

	}
}