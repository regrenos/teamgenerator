package pkg;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Student {

    private String firstName;
    private String lastName;
    private List<Student> priorCollaborators;

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
        List<String> name = new ArrayList<>();
        name.add(firstName);
        name.add(lastName);
        return name.stream().collect(Collectors.joining(" "));
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
