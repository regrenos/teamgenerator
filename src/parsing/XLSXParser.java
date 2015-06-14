package parsing;

import org.apache.poi.ss.usermodel.Sheet;
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
 * <p>
 * Created by steve on 6/14/15.
 */
public class XLSXParser extends XLSParser {

    private StudentStrategy studentStrategy;
    private GroupStrategy groupStrategy;

    public XLSXParser() {
        this(new DefaultStudentStrategy(), new DefaultGroupStrategy());
    }

    public XLSXParser(StudentStrategy studentStrategy, GroupStrategy groupStrategy){
        this.studentStrategy = studentStrategy;
        this.groupStrategy = groupStrategy;
    }

    @Override
    protected Sheet getSheetFromFile(InputStream file) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        return workbook.getSheetAt(0);
    }
}
