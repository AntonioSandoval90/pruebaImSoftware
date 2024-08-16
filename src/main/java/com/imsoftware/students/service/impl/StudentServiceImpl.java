package com.imsoftware.students.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.imsoftware.students.entity.Subject;
import com.imsoftware.students.repository.StudentRepository;
import org.springframework.stereotype.Service;

import com.imsoftware.students.domain.StudentDTO;
import com.imsoftware.students.entity.Student;
import com.imsoftware.students.service.IStudentService;

@Service
public class StudentServiceImpl implements IStudentService {

	private final StudentRepository studentRepository;

	public StudentServiceImpl(StudentRepository studentRepository) {
		super();
		this.studentRepository = studentRepository;
	}

	@Override
	public Collection<StudentDTO> findAll() {
		return studentRepository.findAll().stream().map(new Function<Student, StudentDTO>() {
			@Override
			public StudentDTO apply(Student student) {
				List<String> programmingLanguagesKnowAbout = student.getSubjects().stream()
						.map(pl -> new String(pl.getName())).collect(Collectors.toList());
				return new StudentDTO(student.getName(), programmingLanguagesKnowAbout);
			}

		}).collect(Collectors.toList());
		
	}

	@Override
	public Collection<StudentDTO> findAllAndShowIfHaveAPopularSubject() {
		// TODO Obtener la lista de todos los estudiantes e indicar la materia más concurrida existentes en la BD e
		// indicar si el estudiante cursa o no la materia más concurrida registrado en la BD.

		Collection<StudentDTO> studentsMatConcurrida = new ArrayList<>();

		//Obtener todos los estudiantes
		Collection<StudentDTO> students = findAll();

		//Obtener materia mas concurrida
		String subjectConcurrida = subjectConcurrida(students);

		//Agregar si el estudiante esta o no en la materia mas concurrida
		students.forEach(student ->{
			studentsMatConcurrida.add(new StudentDTO(student.getStudentName(),student.getCurrentSubject().contains(subjectConcurrida)));
		});

		return studentsMatConcurrida;
	}

	public String subjectConcurrida(Collection<StudentDTO> students){

		//Contar materias por alumnos
		Map<String, Long> subjectCount = students.stream()
				.flatMap(student -> student.getCurrentSubject().stream())
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

		//Materia mas concurrida
		return subjectCount.entrySet().stream()
				.max(Map.Entry.comparingByValue())
				.map(Map.Entry::getKey)
				.orElse(null);

	}

}
