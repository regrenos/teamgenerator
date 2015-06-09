package test.parsing.strategy.defaults;

import org.junit.Test;
import parsing.strategy.defaults.DefaultGroupStrategy;
import pkg.EmptyStudent;
import pkg.Group;
import pkg.Student;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * DefaultGroupStrategy Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Jun 10, 2015</pre>
 */
public class DefaultGroupStrategyTest {

    /**
     * Tests interpretRow to see if it parses a row into a Group correctly, ignoring malformed names.
     * Method: interpretRow(List<String> row)
     */
    @Test
    public void testInterpretRow() throws Exception {
        List<String> wellFormattedRow = Arrays.asList("Smith, John", "Becky, Bob", "Johann, Jim");
        List<Student> wellFormattedStudents = new ArrayList<>();
        wellFormattedStudents.add(new Student("John", "Smith"));
        wellFormattedStudents.add(new Student("Bob", "Becky"));
        wellFormattedStudents.add(new Student("Jim", "Johann"));
        Group wellFormattedGroup = new Group(wellFormattedStudents);

        List<String> poorlyFormattedRow = Arrays.asList("Smith, John", "Becky Bob", "Johann, Jim");
        List<Student> poorlyFormattedStudents = new ArrayList<>();
        poorlyFormattedStudents.add(new Student("John", "Smith"));
        poorlyFormattedStudents.add(new Student("Jim", "Johann"));
        Group poorlyFormattedGroup = new Group(poorlyFormattedStudents);

        DefaultGroupStrategy groupStrategy = new DefaultGroupStrategy();
        Method method = DefaultGroupStrategy.class.getMethod("interpretRow", List.class);
        method.setAccessible(true);

        assertEquals(method.invoke(groupStrategy, wellFormattedRow), wellFormattedGroup);
        assertEquals(method.invoke(groupStrategy, poorlyFormattedRow), poorlyFormattedGroup);
    }


    /**
     * Tests interpretStudent to see if it parses a cell into a Student correctly.
     * Method: interpretStudent(String cell)
     */
    @Test
    public void testInterpretStudent() throws Exception {
        String wellFormattedRow = "Smith, John";
        Student wellFormattedStudent = new Student("John", "Smith");

        String poorlyFormattedRow = "Smith John";

        DefaultGroupStrategy groupStrategy = new DefaultGroupStrategy();
        Method method = DefaultGroupStrategy.class.getDeclaredMethod("interpretStudent", String.class);
        method.setAccessible(true);

        assertEquals(method.invoke(groupStrategy, wellFormattedRow), wellFormattedStudent);
        assertEquals(method.invoke(groupStrategy, poorlyFormattedRow), new EmptyStudent());
    }

} 
