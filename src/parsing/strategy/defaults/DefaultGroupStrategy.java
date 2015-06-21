package parsing.strategy.defaults;

import parsing.strategy.GroupStrategy;
import representation.EmptyStudent;
import representation.Group;
import representation.Student;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This strategy is the default {@link GroupStrategy}, it assumes {@link representation.Student} names are listed in the cells of
 * each row, with the cell text being "lastName, firstName." Each row defines a group.
 * <p>
 * Created by steve on 6/8/15.
 */
public class DefaultGroupStrategy implements GroupStrategy {
    @Override
    public Group interpretRow(List<String> row) {
        List<Student> students = row.stream()
                .map(this::interpretStudent)
                .filter(student -> !student.getClass().equals(EmptyStudent.class))
                .collect(Collectors.toList());
        return new Group(students);
    }

    private Student interpretStudent(String cell) {
        if (cell.length() == 0 || !cell.contains(",")) {
            return new EmptyStudent();
        }
        String[] fullName = cell.split(",");
        return new Student(fullName[1], fullName[0]);
    }
}
