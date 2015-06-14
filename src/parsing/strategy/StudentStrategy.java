package parsing.strategy;

import pkg.Student;

import java.util.List;

/**
 * This interface defines the API for interpreting a spreadsheet row as a Student.
 *
 * Created by steve on 6/8/15.
 */
public interface StudentStrategy {

    Student interpretRow(List<String> row);
}
