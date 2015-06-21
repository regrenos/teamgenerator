package parsing;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import parsing.strategy.GroupStrategy;
import parsing.strategy.StudentStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * This parser uses the Apache POI library to parse XLS files.
 * <p>
 * Created by steve on 6/8/15.
 */
public class XLSParser extends Parser {

    public XLSParser(StudentStrategy studentStrategy, GroupStrategy groupStrategy) {
        super(studentStrategy, groupStrategy);
    }

    public XLSParser() {
        super();
    }

    @Override
    protected List<List<String>> getValidCells(InputStream file) throws IOException {
        return getValidCells(getSheetFromFile(file));
    }

    protected List<List<String>> getValidCells(Sheet sheet) {
        List<List<String>> validCells = new ArrayList<>();
        toFiniteStream(sheet.rowIterator())
                .map(Row::cellIterator)
                .map(this::toFiniteStream)
                .forEach(cellStream ->
                                validCells.add(cellStream.filter(this::hasValidContent)
                                                .map(Cell::getStringCellValue)
                                                .collect(Collectors.toList())
                                )
                );

        return validCells;
    }

    protected boolean hasValidContent(Cell cell) {
        try {
            return cell.getStringCellValue().length() > 0;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    protected <T> Stream<T> toFiniteStream(Iterator<T> iterator) {
        Iterable<T> iterable = () -> iterator;
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    protected Sheet getSheetFromFile(InputStream file) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook(file);
        return workbook.getSheetAt(0);
    }
}
