package kuznikos.restapp.restapp.repos;

import kuznikos.restapp.restapp.model.Course;
import kuznikos.restapp.restapp.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findAllBySurname(String surname);
    List<Student> findStudentByCourses(Long id);
}
