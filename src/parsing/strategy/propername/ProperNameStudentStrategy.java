package parsing.strategy.propername;

import parsing.strategy.StudentStrategy;
import representation.EmptyStudent;
import representation.Student;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Steve on 9/14/2015.
 */
public class ProperNameStudentStrategy implements StudentStrategy {
    @Override
    public Student interpretRow(List<String> row) {
        if (row.get(0).length() == 0 || !row.get(0).contains(" ")) {
            return new EmptyStudent();
        }
        String[] fullName = row.get(0).split(" ");
        String lastName = Arrays.asList(Arrays.copyOfRange(fullName, 1, fullName.length)).stream()
                .collect(Collectors.joining(" "));
        return new Student(fullName[0], lastName);
    }
}
