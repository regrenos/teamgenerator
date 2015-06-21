package parsing.strategy;

import representation.Group;

import java.util.List;

/**
 * This interface defines the API for interpreting a spreadsheet row as a {@link representation.Group}.
 * <p>
 * Created by steve on 6/8/15.
 */
public interface GroupStrategy {
    /**
     * Interpret a list of strings representing a row as a {@link representation.Group}.
     *
     * @param row list of strings read from input
     * @return a {@link representation.Group} of {@link representation.Student}s
     */
    Group interpretRow(List<String> row);
}
