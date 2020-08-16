package kuznikos.restapp.restapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of="id")
@NoArgsConstructor
@AllArgsConstructor
@Table(name="course")
public class Course {

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
    private String courseAbbr;

    @ManyToOne
    @JoinColumn(name="faculty_id")
    private Faculty courseFaculty;

    @JsonIgnore
    @ManyToMany(mappedBy = "courses")
    Set<Student> students= new HashSet<>();

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudent( Student student) {
        students.add(student);
    }
}
