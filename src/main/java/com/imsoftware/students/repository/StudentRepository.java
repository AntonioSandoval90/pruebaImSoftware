package com.imsoftware.students.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.imsoftware.students.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {


}
