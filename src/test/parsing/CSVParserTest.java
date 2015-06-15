package test.parsing;

import org.junit.Test;
import parsing.CSVParser;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * CSVParser Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Jun 14, 2015</pre>
 */
public class CSVParserTest {


    /**
     * Method: getValidCells(InputStream file)
     */
    @Test
    public void testGetValidCells() throws Exception {
        List<String> firstRow = Arrays.asList("Lannister, Tyrion", "Lannister, Cersei", "Lannister, Jaime");
        List<String> secondRow = Arrays.asList("Targaryen, Denerys", "Snow, Jon", "Mormont, Jorah");
        List<String> thirdRow = Arrays.asList("Stark, Sansa", "Stark Arya", "Stark, Bran");
        List<String> fourthRow = Arrays.asList("Tarly, Samwell", "Geryjoy, Theon");
        List<String> fifthRow = Collections.singletonList("Baelish, Petyr");
        List<List<String>> desiredRows = Arrays.asList(firstRow, secondRow, thirdRow, fourthRow, fifthRow);

        InputStream inputStream = getClass().getResourceAsStream("test_groups_default.csv");
        CSVParser parser = new CSVParser();
        Method method = CSVParser.class.getDeclaredMethod("getValidCells", InputStream.class);
        method.setAccessible(true);

        assertEquals(method.invoke(parser, inputStream), desiredRows);
    }


    /**
     * Method: hasValidContent(String cell)
     */
    @Test
    public void testHasValidContent() throws Exception {
        CSVParser parser = new CSVParser();
        Method method = CSVParser.class.getDeclaredMethod("hasValidContent", String.class);
        method.setAccessible(true);

        assertTrue((boolean) method.invoke(parser, "Smith, John"));
        assertTrue((boolean) method.invoke(parser, "Smith, John Jr"));
        assertTrue((boolean) method.invoke(parser, "Smith, John Abraham"));
        assertTrue((boolean) method.invoke(parser, "Smith, John Abraham Jr"));
        assertTrue((boolean) method.invoke(parser, "Smith John"));
    }
} 
