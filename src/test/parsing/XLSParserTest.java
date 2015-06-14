package test.parsing;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import parsing.XLSParser;
import pkg.Group;
import pkg.Student;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * XLSParser Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Jun 13, 2015</pre>
 */
public class XLSParserTest {
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

        InputStream file = getClass().getResourceAsStream("test_students_default.xls");

        XLSParser parser = new XLSParser();
        Method method = XLSParser.class.getMethod("parseSheetOfStudents", InputStream.class);
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

        InputStream file = getClass().getResourceAsStream("test_groups_default.xls");

        XLSParser parser = new XLSParser();
        Method method = XLSParser.class.getMethod("parseSheetOfGroups", InputStream.class);
        method.setAccessible(true);

        assertEquals(method.invoke(parser, file), desiredGroups);
    }

    /**
     * Method: parseSheetAsSectionGrouping(File file)
     */
    @Test
    public void testParseSheetAsSectionGrouping() throws Exception {
// TODO: make this test meaningful
//        InputStream file = getClass().getResourceAsStream("test_students_default.xls");
//
//        XLSParser parser = new XLSParser();
//        Method method = XLSParser.class.getMethod("parseSheetAsSectionGrouping", File.class);
//        method.setAccessible(true);

    }

    /**
     * Method: getValidCells(HSSFSheet sheet)
     */
    @Test
    public void testGetValidCellsSheet() throws Exception {
        List<String> firstRow = Arrays.asList("Lannister, Tyrion", "Lannister, Cersei", "Lannister, Jaime");
        List<String> secondRow = Arrays.asList("Targaryen, Denerys", "Snow, Jon", "Mormont, Jorah");
        List<String> thirdRow = Arrays.asList("Stark, Sansa", "Stark Arya", "Stark, Bran");
        List<String> fourthRow = Arrays.asList("Tarly, Samwell", "Geryjoy, Theon");
        List<String> fifthRow = Collections.singletonList("Baelish, Petyr");
        List<List<String>> desiredRows = Arrays.asList(firstRow, secondRow, thirdRow, fourthRow, fifthRow);

        InputStream inputStream = getClass().getResourceAsStream("test_groups_default.xls");
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        XLSParser parser = new XLSParser();
        Method method = XLSParser.class.getDeclaredMethod("getValidCells", Sheet.class);
        method.setAccessible(true);

        assertEquals(method.invoke(parser, sheet), desiredRows);
    }

    /**
     * Method: hasValidContent(Cell cell)
     */
    @Test
    public void testHasValidContent() throws Exception {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        Row row = sheet.createRow(0);
        Cell validCell = row.createCell(0);
        validCell.setCellValue("Smith, Steve");
        Cell invalidCell = row.createCell(1);
        invalidCell.setCellValue(6);

        XLSParser parser = new XLSParser();
        Method method = XLSParser.class.getDeclaredMethod("hasValidContent", Cell.class);
        method.setAccessible(true);

        assertTrue((boolean) method.invoke(parser, validCell));
        assertFalse((boolean) method.invoke(parser, invalidCell));
    }
} 
