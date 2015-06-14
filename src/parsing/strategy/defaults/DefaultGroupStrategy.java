package parsing.strategy.defaults;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import parsing.strategy.GroupStrategy;
import pkg.EmptyStudent;
import pkg.Group;
import pkg.Student;

import java.util.List;
import java.util.stream.Collectors;

/**
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
