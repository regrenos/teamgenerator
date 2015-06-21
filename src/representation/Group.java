package representation;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The {@link Group} has a certain size and contains {@link Student}s. The {@link Group} exposes an iterator for its'
 * {@link Student}s.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Group")
public class Group implements Iterable<Student> {

    private int numStudents;
    @XmlElement(name = "member")
    private List<Student> members;

    // Used for output group numbering
    @XmlAttribute(name = "groupNumber")
    private int groupNumber;

    /**
     * Create a {@link Group} with a size of 0, used for JAXB marshalling.
     */
    public Group() {
        this(0);
    }

    /**
     * Set the group number for output document labelling.
     * @param number number to assign to group
     */
    public void setGroupNumber(int number){
        this.groupNumber = number;
    }

    /**
     * Create a {@link Group} with a given size without any initial members.
     *
     * @param size the size of the {@link Group} to create
     */
    public Group(int size) {
        numStudents = size;
        members = new ArrayList<>();
    }

    /**
     * Create a {@link Group} from a list of member {@link Student}s, pre-populating the {@link Group} and setting its'
     * size to the size of the pre-populated list.
     *
     * @param students the students to use when populating the group
     */
    public Group(List<Student> students) {
        this(students.size());
        students.stream().forEach(this::addStudent);
    }

    /**
     * Create a {@link Group} from a list of member {@link Student}s, pre-populating the {@link Group} but setting its'
     * size to the given size.
     *
     * @param students the students to use when populating the group
     * @param size     the final desired size of the group
     */
    public Group(List<Student> students, int size) {
        this(size);
        students.stream().forEach(this::addStudent);
    }

    public boolean containsStudent(Student student) {
        return members.contains(student);
    }

    /**
     * Add a {@link Student} to this {@link Group}. Will add current {@link Group} members as collaborators to the given
     * {@link Student} as well as adding the given {@link Student} as a collaborator for current members.
     *
     * @param newMember {@link Student} to add to the {@link Group}
     */
    public void addStudent(Student newMember) {
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

    /**
     * Remove a {@link Student} from this {@link Group}. Will remove current {@link Group} members as collaborators from
     * the given {@link Student}, as well as removing the given {@link Student} as a collaborator from the current
     * members of the {@link Group}.
     *
     * @param student {@link Student} to remove from the {@link Group}
     */
    public void removeStudent(Student student) {
        members.remove(student);
        student.removeCollaborators(members);
        members.stream().forEach(s -> s.removeCollaborator(student));
    }

    /**
     * Determine if another {@link Object} equals this {@link Group}. {@link Group} equality necessiates that the
     * {@link Group}s contain the same students, and that neither is a superset of the other.
     *
     * @param o {@link Object} to compare this {@link Group} to
     * @return whether the {@link Object} equals this {@link Group}
     */
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
