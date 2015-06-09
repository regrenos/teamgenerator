package parsing.strategy;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import pkg.Group;

import java.util.List;

/**
 * This interface defines the API for iterpreting a spreadsheet row as a Group.
 *
 * Created by steve on 6/8/15.
 */
public interface GroupStrategy {

    Group interpretRow(List<String> row);
}
