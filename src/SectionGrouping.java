import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class SectionGrouping implements Iterable<Group> {

    public static final int PREFERRED_GROUP_SIZE = 3;
    private List<Group> groups;
    private List<Student> unassignedStudents;

    public SectionGrouping (List<Student> studentList) {
        unassignedStudents = new ArrayList<>();
        unassignedStudents.addAll(studentList);

        groups = new ArrayList<>();

        int numStudents = studentList.size();
        int numGroups = numStudents / PREFERRED_GROUP_SIZE;
        int numLargerGroups = numStudents % PREFERRED_GROUP_SIZE;

        for (int i = 0; i < numGroups; i++) {
            groups.add(new Group(PREFERRED_GROUP_SIZE + (numLargerGroups > 0 ? 1 : 0)));
            numLargerGroups--;
        }
    }

    public boolean areStudentsGrouped (Set<Student> students) {
        boolean returnValue = true;
        for (Student s : students) {
            boolean studentValue = false;
            for (Group g : groups) {
                studentValue = g.containsStudent(s);
            }
            returnValue = returnValue && studentValue;
        }
        return returnValue;
    }

    public void addGroup (Group group) {
        groups.add(group);
    }

    public List<Group> getGroups () {
        return groups;
    }

    public List<Student> getSubsetOfUnassignedStudents (Predicate<Student> filter) {
        return unassignedStudents.stream().filter(filter).collect(Collectors.toList());
    }

    public void removeStudentFromEligibility (Student student) {
        unassignedStudents.remove(student);
    }

    public void addEligibleStudent (Student student) {
        unassignedStudents.add(student);
    }

    @Override
    public Iterator<Group> iterator () {
        return groups.iterator();
    }
}
