package kuznikos.restapp.restapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(of="id")
@NoArgsConstructor
@AllArgsConstructor
@Table(name="faculty")
public class Faculty {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name="name")
    @Size(min=3, max=50)
    private String name;

    @NotNull
    @Column(name="abbreviation")
    @Size(min=3, max=50)
    private String facultyAbbr;

    @OneToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Course> courses = new HashSet<>();

    @OneToMany
    @JsonIgnore
    private Set<Student> students = new HashSet<>();

    public void addStudent( Student student) {
        students.add(student);
    }
    public void addCourse( Course course) {
        courses.add(course);
    }
}
