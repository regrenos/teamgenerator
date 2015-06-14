package parsing.strategy.defaults;

import parsing.strategy.StudentStrategy;
import pkg.EmptyStudent;
import pkg.Student;

import java.util.List;

/**
 * This strategy is the default {@link StudentStrategy}, it assumes {@link pkg.Student} names are in the first cell of
 * each row, with the cell text being "lastName, firstName."
 * <p>
 * Created by steve on 6/8/15.
 */
public class DefaultStudentStrategy implements StudentStrategy {
    @Override
    public Student interpretRow(List<String> row) {
        if (row.get(0).length() == 0 || !row.get(0).contains(",")) {
            return new EmptyStudent();
        }
        String[] fullName = row.get(0).split(",");
        return new Student(fullName[1], fullName[0]);
    }
}
