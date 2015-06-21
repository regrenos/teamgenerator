package test.parsing;

import org.junit.Test;
import parsing.XLSXParser;
import representation.Group;
import representation.Student;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * XLSXParser Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Jun 14, 2015</pre>
 */
public class XLSXParserTest {
    /**
     * Method: parseSheetOfStudents(File file)
     */
    @Test
    public void testParseSheetOfStudents() throws Exception {
        List<Student> desiredStudents = Arrays.asList(
                new Student("Tyrion", "Lannister"),
                new Student("Cersei", "Lannister"),
                new Student("Denerys", "Targaryen"),
                new Student("Sansa", "Stark"),
                new Student("Jorah", "Mormont"),
                new Student("Arya", "Stark"),
                new Student("Jaime", "Lannister"),
                new Student("Samwell", "Tarly"),
                new Student("Theon", "Geryjoy"),
                new Student("Petyr", "Baelish"),
                new Student("Bran", "Stark"));

        InputStream file = getClass().getResourceAsStream("test_students_default.xlsx");

        XLSXParser parser = new XLSXParser();
        Method method = XLSXParser.class.getMethod("parseSheetOfStudents", InputStream.class);
        method.setAccessible(true);

        assertEquals(method.invoke(parser, file), desiredStudents);
    }

    /**
     * Method: parseSheetOfGroups(File file)
     */
    @Test
    public void testParseSheetOfGroups() throws Exception {
        Group firstGroup = new Group(Arrays.asList(
                new Student("Tyrion", "Lannister"),
                new Student("Cersei", "Lannister"),
                new Student("Jaime", "Lannister")));
        Group secondGroup = new Group(Arrays.asList(
                new Student("Denerys", "Targaryen"),
                new Student("Jon", "Snow"),
                new Student("Jorah", "Mormont")));
        Group thirdGroup = new Group(Arrays.asList(
                new Student("Sansa", "Stark"),
                new Student("Bran", "Stark")));
        Group fourthGroup = new Group(Arrays.asList(
                new Student("Samwell", "Tarly"),
                new Student("Theon", "Geryjoy")));
        Group fifthGroup = new Group(Collections.singletonList(
                new Student("Petyr", "Baelish")));
        List<Group> desiredGroups = Arrays.asList(firstGroup, secondGroup, thirdGroup, fourthGroup, fifthGroup);

        InputStream file = getClass().getResourceAsStream("test_groups_default.xlsx");

        XLSXParser parser = new XLSXParser();
        Method method = XLSXParser.class.getMethod("parseSheetOfGroups", InputStream.class);
        method.setAccessible(true);

        assertEquals(method.invoke(parser, file), desiredGroups);
    }

    /**
     * Method: parseSheetAsSectionGrouping(File file)
     */
    @Test
    public void testParseSheetAsSectionGrouping() throws Exception {
// TODO: make this test meaningful
//        InputStream file = getClass().getResourceAsStream("test_students_default.xlsx");
//
//        XLSXParser parser = new XLSXParser();
//        Method method = XLSXParser.class.getMethod("parseSheetAsSectionGrouping", File.class);
//        method.setAccessible(true);
    }
}
