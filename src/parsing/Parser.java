package parsing;

import pkg.Group;
import pkg.SectionGrouping;
import pkg.Student;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by steve on 6/8/15.
 */
public interface Parser {

    List<Student> parseSheetOfStudents(InputStream file) throws IOException;

    List<Group> parseSheetOfGroups(InputStream file) throws IOException;

    SectionGrouping parseSheetAsSectionGrouping(InputStream file) throws IOException;
}
