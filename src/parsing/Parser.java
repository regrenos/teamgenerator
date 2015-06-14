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
 * The Parser interface exposes method for parsing input files into objects used by the group generation algorithm.
 *
 * Created by steve on 6/8/15.
 */
public interface Parser {
    /**
     * Parse a (spread)sheet as a list of {@link pkg.Student}s.
     * @param file the input file
     * @return a list of valid {@link pkg.Student}s parsed from the file
     * @throws IOException if there are errors reading the file
     */
    List<Student> parseSheetOfStudents(InputStream file) throws IOException;

    /**
     * Parse a (spread)sheet as a list of {@link pkg.Group}s.
     * @param file the input file
     * @return a list of valid {@link pkg.Group}s parsed from the file
     * @throws IOException if there are errors reading the file
     */
    List<Group> parseSheetOfGroups(InputStream file) throws IOException;

    /**
     * Parse a (spread)sheet as a {@link pkg.SectionGrouping}.
     * @param file the input file
     * @return a valid {@link pkg.SectionGrouping} parsed from the file
     * @throws IOException if there are errors reading the file
     */
    SectionGrouping parseSheetAsSectionGrouping(InputStream file) throws IOException;
}
