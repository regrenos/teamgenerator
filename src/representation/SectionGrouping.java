package representation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "SectionGrouping")
public class SectionGrouping implements Iterable<Group> {

    public static final int DEFAULT_PREFERRED_GROUP_SIZE = 3;
    @XmlElement(name = "memberGroup")
    private List<Group> groups;
    private List<Student> unassignedStudents;

    /**
     * Create a {@link SectionGrouping} with no students, used for JAXB marshalling.
     */
    public SectionGrouping() {
        this(new ArrayList<>());
    }

    public SectionGrouping(List<Student> studentList) {
        unassignedStudents = new ArrayList<>();
        unassignedStudents.addAll(studentList);

        groups = new ArrayList<>();

        int numStudents = studentList.size();
        int numGroups = numStudents / DEFAULT_PREFERRED_GROUP_SIZE;
        int numLargerGroups = numStudents % DEFAULT_PREFERRED_GROUP_SIZE;

        for (int i = 0; i < numGroups; i++) {
            groups.add(new Group(DEFAULT_PREFERRED_GROUP_SIZE + (numLargerGroups > 0 ? 1 : 0)));
            numLargerGroups--;
        }
    }

    /**
     * Returns true if all of the students are already assigned to a group in this section grouping.
     *
     * @param students students to check for
     * @return true if all students are assigned to a group
     */
    public boolean areStudentsGrouped(Set<Student> students) {
        boolean returnValue = true;
        for (Student s : students) {
            boolean studentValue = false;
            for (Group g : groups) {
                studentValue = studentValue | g.containsStudent(s);
            }
            returnValue = returnValue && studentValue;
        }
        return returnValue;
    }

    public void addGroup(Group group) {
        groups.add(group);
    }

    public List<Student> getSubsetOfUnassignedStudents(Predicate<Student> filter) {
        return unassignedStudents.stream().filter(filter).collect(Collectors.toList());
    }

    public void markStudentIneligible(Student student) {
        unassignedStudents.remove(student);
    }

    public void addEligibleStudent(Student student) {
        unassignedStudents.add(student);
    }

    @Override
    public Iterator<Group> iterator() {
        return groups.iterator();
    }

    public void enumerateGroups() {
        // TODO: add support for different enumeration schemes
        int i = 0;
        for (Group group : groups) {
            group.setGroupNumber(i);
            i++;
        }
    }
}
