package pkg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Group implements Iterable<Student> {

    private int numStudents;
    private List<Student> members;

    public Group(int size) {
        numStudents = size;
        members = new ArrayList<>();
    }

    public Group(List<Student> students) {
        this(students.size());
        students.stream().forEach(this::addStudentToGroup);
    }

    public boolean containsStudent(Student student) {
        return members.contains(student);
    }

    public void addStudentToGroup(Student newMember) {
        members.add(newMember);
        members.stream().forEach(s -> s.addCollaborator(newMember));
        newMember.addCollaborators(members);
    }

    public boolean isFull() {
        return numStudents == members.size();
    }

    public boolean isEmpty() {
        return members.size() == 0;
    }

    @Override
    public Iterator<Student> iterator() {
        return members.iterator();
    }

    public void removeStudent(Student student) {
        members.remove(student);
        student.removeCollaborators(members);
        members.stream().forEach(s -> s.removeCollaborator(student));
    }

    public boolean equals(Object o) {
        if (o instanceof Group) {
            Group g = (Group) o;
            boolean returnValue = true;
            for (Student s : g) {
                returnValue = returnValue && members.contains(s);
            }
            return returnValue && (members.size() == g.members.size());
        }
        return false;
    }

    public int hashCode() {
        return members.stream().mapToInt(Student::hashCode).sum();
    }

}
