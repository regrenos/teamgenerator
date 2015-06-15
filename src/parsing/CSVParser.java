package parsing;

import parsing.strategy.GroupStrategy;
import parsing.strategy.StudentStrategy;
import parsing.strategy.defaults.DefaultGroupStrategy;
import parsing.strategy.defaults.DefaultStudentStrategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The CSVParser parses csv files using built in Java libraries.
 * <p>
 * Created by steve on 6/14/15.
 */
public class CSVParser extends Parser {

    public CSVParser() {
        super();
    }

    public CSVParser(StudentStrategy studentStrategy, GroupStrategy groupStrategy) {
        super(studentStrategy, groupStrategy);
    }

    @Override
    protected List<List<String>> getValidCells(InputStream file) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(file));
        String line;
        List<List<String>> validCells = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            String[] cells = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            List<String> validRowCells = Arrays.asList(cells).stream()
                    .map(s -> s.replaceAll("[^a-zA-Z\\s,]", ""))
                    .filter(this::hasValidContent)
                    .collect(Collectors.toList());
            validCells.add(validRowCells);
        }
        return validCells;
    }

    private boolean hasValidContent(String cell) {
        return cell.matches("[\\w]+(,|)(\\s[\\w]+)+");
    }
}
