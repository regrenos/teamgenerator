package parsing;

import parsing.strategy.GroupStrategy;
import parsing.strategy.StudentStrategy;
import parsing.strategy.defaults.DefaultGroupStrategy;
import parsing.strategy.defaults.DefaultStudentStrategy;
import representation.EmptyStudent;
import representation.Group;
import representation.SectionGrouping;
import representation.Student;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Parser class exposes methods for parsing input files into objects used by the group generation algorithm.
 *
 * Created by steve on 6/8/15.
 */
public abstract class Parser {

    private StudentStrategy studentStragegy;
    private GroupStrategy groupStrategy;

    public Parser(StudentStrategy studentStragegy, GroupStrategy groupStrategy){
        this.studentStragegy = studentStragegy;
        this.groupStrategy = groupStrategy;
    }

    public Parser() {
        this(new DefaultStudentStrategy(), new DefaultGroupStrategy());
    }

    /**
     * Parse a (spread)sheet as a list of {@link representation.Student}s.
     * @param file the input file
     * @return a list of valid {@link representation.Student}s parsed from the file
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
     * Parse a (spread)sheet as a list of {@link representation.Group}s.
     * @param file the input file
     * @return a list of valid {@link representation.Group}s parsed from the file
     * @throws IOException if there are errors reading the file
     */
    public List<Group> parseSheetOfGroups(InputStream file) throws IOException{
        List<List<String>> validCells = getValidCells(file);
        return validCells.stream()
                .map(groupStrategy::interpretRow)
                .collect(Collectors.toList());
    }

    /**
     * Parse a (spread)sheet as a {@link representation.SectionGrouping}.
     * @param file the input file
     * @return a valid {@link representation.SectionGrouping} parsed from the file
     * @throws IOException if there are errors reading the file
     */
    public SectionGrouping parseSheetAsSectionGrouping(InputStream file) throws IOException{
        return new SectionGrouping(this.parseSheetOfStudents(file));
    }

    protected abstract List<List<String>> getValidCells(InputStream file) throws IOException;
}
