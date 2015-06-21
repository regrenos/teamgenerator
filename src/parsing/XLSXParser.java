package parsing;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import parsing.strategy.GroupStrategy;
import parsing.strategy.StudentStrategy;

import java.io.IOException;
import java.io.InputStream;

/**
 * This parser uses the Apache POI Library to parse XLSX files.
 * <p>
 * Created by steve on 6/14/15.
 */
public class XLSXParser extends XLSParser {

    public XLSXParser() {
        super();
    }

    public XLSXParser(StudentStrategy studentStrategy, GroupStrategy groupStrategy) {
        super(studentStrategy, groupStrategy);
    }

    @Override
    protected Sheet getSheetFromFile(InputStream file) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        return workbook.getSheetAt(0);
    }
}
