package test.parsing.strategy.defaults;

import org.junit.Test;
import parsing.strategy.defaults.DefaultStudentStrategy;
import representation.EmptyStudent;
import representation.Student;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * DefaultStudentStrategy Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Jun 10, 2015</pre>
 */
public class DefaultStudentStrategyTest {

    /**
     * Tests that a well-formatted row results in a correct Student and that a
     * poorly-formatted returns an EmptyStudent.
     * Method: interpretRow(List<String> row)
     */
    @Test
    public void testInterpretRow() throws Exception {
        List<String> wellFormattedRow = Arrays.asList("Smith, John", "", "", "");
        Student wellFormattedStudent = new Student("John", "Smith");

        List<String> poorlyFormattedRow = Arrays.asList("Smith John", "", "", "");

        DefaultStudentStrategy studentStrategy = new DefaultStudentStrategy();
        Method method = DefaultStudentStrategy.class.getMethod("interpretRow", List.class);
        method.setAccessible(true);

        assertEquals(method.invoke(studentStrategy, wellFormattedRow), wellFormattedStudent);
        assertEquals(method.invoke(studentStrategy, poorlyFormattedRow), new EmptyStudent());
    }
} 
