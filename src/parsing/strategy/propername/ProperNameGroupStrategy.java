package parsing.strategy.propername;

import parsing.strategy.GroupStrategy;
import representation.EmptyStudent;
import representation.Group;
import representation.Student;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Steve on 9/14/2015.
 */
public class ProperNameGroupStrategy implements GroupStrategy {
    @Override
    public Group interpretRow(List<String> row) {
        List<Student> students = row.stream()
                .map(this::interpretStudent)
                .filter(student -> !student.getClass().equals(EmptyStudent.class))
                .collect(Collectors.toList());
        return new Group(students);
    }

    private Student interpretStudent(String cell) {
        if (cell.length() == 0 || !cell.contains(" ")) {
            return new EmptyStudent();
        }
        String[] fullName = cell.split(" ");
        String lastName = Arrays.asList(Arrays.copyOfRange(fullName, 1, fullName.length)).stream()
                .collect(Collectors.joining(" "));
        return new Student(fullName[0], lastName);
    }
}

