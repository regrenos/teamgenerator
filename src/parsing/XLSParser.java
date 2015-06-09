package parsing;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import parsing.strategy.GroupStrategy;
import parsing.strategy.StudentStrategy;
import parsing.strategy.defaults.DefaultGroupStrategy;
import parsing.strategy.defaults.DefaultStudentStrategy;
import pkg.EmptyStudent;
import pkg.Group;
import pkg.SectionGrouping;
import pkg.Student;

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
public class XLSParser implements Parser {

    private StudentStrategy studentStragegy;
    private GroupStrategy groupStrategy;

    public XLSParser() {
        studentStragegy = new DefaultStudentStrategy();
        groupStrategy = new DefaultGroupStrategy();
    }

    @Override
    public List<Student> parseSheetOfStudents(InputStream file) throws IOException {
        List<List<String>> validCells = getValidCells(file);
        return validCells.stream()
                .map(studentStragegy::interpretRow)
                .filter(student -> !student.getClass().equals(EmptyStudent.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Group> parseSheetOfGroups(InputStream file) throws IOException {
        List<List<String>> validCells = getValidCells(file);
        return validCells.stream()
                .map(groupStrategy::interpretRow)
                .collect(Collectors.toList());
    }

    @Override
    public SectionGrouping parseSheetAsSectionGrouping(InputStream file) throws IOException {
        return new SectionGrouping(this.parseSheetOfStudents(file));
    }

    private List<List<String>> getValidCells(InputStream file) throws IOException {
        return getValidCells(getSheetFromFile(file));
    }

    private List<List<String>> getValidCells(HSSFSheet sheet) {
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

    private boolean hasValidContent(Cell cell) {
        try {
            return cell.getStringCellValue().length() > 0;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    private <T> Stream<T> toFiniteStream(Iterator<T> iterator) {
        Iterable<T> iterable = () -> iterator;
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    private HSSFSheet getSheetFromFile(InputStream file) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook(file);
        return workbook.getSheetAt(0);
    }
}
