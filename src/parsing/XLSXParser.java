package parsing;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import parsing.strategy.GroupStrategy;
import parsing.strategy.StudentStrategy;
import parsing.strategy.defaults.DefaultGroupStrategy;
import parsing.strategy.defaults.DefaultStudentStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * This parser uses the Apache POI Library to parse XLSX files.
 *
 * Created by steve on 6/14/15.
 */
public class XLSXParser extends XLSParser implements Parser {

    private StudentStrategy studentStrategy;
    private GroupStrategy groupStrategy;

    public XLSXParser() {
        studentStrategy = new DefaultStudentStrategy();
        groupStrategy = new DefaultGroupStrategy();
    }

    private List<List<String>> getValidCells(InputStream file) throws IOException {
        return getValidCells(getSheetFromFile(file));
    }

    @Override
    protected Sheet getSheetFromFile(InputStream file) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        return workbook.getSheetAt(0);
    }
}
