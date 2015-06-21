package representation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * A {@link Student} holds identifying information about the student it represents, as well as a list of other
 * {@link Student}s that this student has collaborated with.
 */
@XmlRootElement(name = "Student")
public class Student {

    @XmlAttribute(name = "firstName")
    private String firstName;
    @XmlAttribute(name = "lastName")
    private String lastName;
    private List<Student> priorCollaborators;

    /**
     * Creates a {@link Student} with no name, used for JAXB marshalling.
     */
    public Student(){
        this("","");
    }

    public Student(String first, String last) {
        firstName = first.trim();
        lastName = last.trim();
        priorCollaborators = new ArrayList<>();
    }

    public boolean hasCollaborated(Student other) {
        return priorCollaborators.contains(other);
    }

    public List<Student> getCollaborators() {
        return priorCollaborators;
    }

    public boolean equals(Object o) {
        // TODO: assign ID numbers?
        if (o instanceof Student) {
            Student s = (Student) o;
            return firstName.equalsIgnoreCase(s.firstName) && lastName.equalsIgnoreCase(s.lastName);
        }
        return false;
    }

    public int hashCode() {
        return firstName.hashCode() * 17 + lastName.hashCode() * 31;
    }

    public void addCollaborators(List<Student> students) {
        students.stream().forEach(this::addCollaborator);
    }

    public void removeCollaborators(List<Student> students) {
        priorCollaborators.removeAll(students);
    }

    public String getName() {
        return Arrays.asList(firstName, lastName).stream().collect(Collectors.joining(" "));
    }

    public String toString() {
        return getName();
    }

    public void addCollaborator(Student chosenStudent) {
        if (!chosenStudent.equals(this)) {
            priorCollaborators.add(chosenStudent);
        }
    }

    public void removeCollaborator(Student student) {
        priorCollaborators.remove(student);
    }
}
