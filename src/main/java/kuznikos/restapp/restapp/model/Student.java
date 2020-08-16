package kuznikos.restapp.restapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="student")
@Getter
@Setter
@EqualsAndHashCode(of="id")
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name="name")
    @Size(min=3, max=50)
    private String name;

    @NotNull
    @Column(name="surname")
    @Size(min=3, max=50)
    private String surname;

    @NotNull
    @Column(name="age")
    @Digits(integer = 2, fraction = 0)
    private int age;

    @NotNull
    @Column(name="personal_number", unique = true)
    private String personalNumber;

    @NotNull
    @Column(name="email", unique = true)
    private String email;

    @ManyToMany
    @JoinTable(name = "student_course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    Set<Course> courses = new HashSet<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="faculty_id")
    private Faculty studentFaculty;

    public void setCourse(Course course){
        courses.add(course);
    }

    public void deleteCourse(Course course){courses.remove(course);}
}




