import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Group implements Iterable<Student> {

    private int numStudents;
    private List<Student> members;

    public Group (int size) {
        numStudents = size;
        members = new ArrayList<>();
    }

    public boolean containsStudent (Student student) {
        return members.contains(student);
    }

    public void addStudentToGroup (Student newMember) {
        members.add(newMember);
        getStudents().stream().forEach(s -> s.addCollaborator(newMember));
        newMember.addCollaborators(getStudents());
    }

    public boolean isFull () {
        return numStudents == members.size();
    }

    @Override
    public Iterator<Student> iterator () {
        return members.iterator();
    }

    public List<Student> getStudents () {
        return members;
    }

    public void removeStudent (Student student) {
        members.remove(student);
        student.removeCollaborators(getStudents());
        getStudents().stream().forEach(s -> s.removeCollaborator(student));
    }
    
    public boolean equals (Object o) {
        if (o instanceof Group) {
            Group g = (Group) o;
            boolean returnValue = true;
            for(Student s: g){
                returnValue = returnValue && members.contains(s);
            }
            return returnValue && (members.size() == g.members.size());
        }
        return false;
    }

    public int hashCode () {
        return members.stream().mapToInt(s -> s.hashCode()).sum();
    }

}
