package parsing;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import parsing.strategy.GroupStrategy;
import parsing.strategy.StudentStrategy;
import pkg.EmptyStudent;
import pkg.Group;
import pkg.SectionGrouping;
import pkg.Student;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * The Parser class exposes methods for parsing input files into objects used by the group generation algorithm.
 *
 * Created by steve on 6/8/15.
 */
public abstract class Parser {

    private StudentStrategy studentStragegy;
    private GroupStrategy groupStrategy;

    /**
     * Parse a (spread)sheet as a list of {@link pkg.Student}s.
     * @param file the input file
     * @return a list of valid {@link pkg.Student}s parsed from the file
     * @throws IOException if there are errors reading the file
     */
    public List<Student> parseSheetOfStudents(InputStream file) throws IOException{
        List<List<String>> validCells = getValidCells(file);
        return validCells.stream()
                .map(studentStragegy::interpretRow)
                .filter(student -> !student.getClass().equals(EmptyStudent.class))
                .collect(Collectors.toList());
    }

    /**
     * Parse a (spread)sheet as a list of {@link pkg.Group}s.
     * @param file the input file
     * @return a list of valid {@link pkg.Group}s parsed from the file
     * @throws IOException if there are errors reading the file
     */
    public List<Group> parseSheetOfGroups(InputStream file) throws IOException{
        List<List<String>> validCells = getValidCells(file);
        return validCells.stream()
                .map(groupStrategy::interpretRow)
                .collect(Collectors.toList());
    }

    /**
     * Parse a (spread)sheet as a {@link pkg.SectionGrouping}.
     * @param file the input file
     * @return a valid {@link pkg.SectionGrouping} parsed from the file
     * @throws IOException if there are errors reading the file
     */
    public SectionGrouping parseSheetAsSectionGrouping(InputStream file) throws IOException{
        return new SectionGrouping(this.parseSheetOfStudents(file));
    }

    protected abstract List<List<String>> getValidCells(InputStream file) throws IOException;
}
