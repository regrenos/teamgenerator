package parsing.strategy;

import pkg.Student;

import java.util.List;

/**
 * This interface defines the API for interpreting a spreadsheet row as a {@link pkg.Student}.
 * <p>
 * Created by steve on 6/8/15.
 */
public interface StudentStrategy {
    /**
     * Interpret a list of strings representing a row as a {@link pkg.Student}.
     *
     * @param row list of strings read from input
     * @return a {@link pkg.Student}.
     */
    Student interpretRow(List<String> row);
}
