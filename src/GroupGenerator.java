import generation.PDFGenerator;
import parsing.CSVParser;
import parsing.Parser;
import parsing.XLSXParser;
import parsing.strategy.propername.ProperNameGroupStrategy;
import parsing.strategy.propername.ProperNameStudentStrategy;
import representation.Group;
import representation.SectionGrouping;
import representation.Student;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class GroupGenerator {

    public static final int NUM_GROUPINGS = 3;
    public static final int NUM_ALLOWED_REPAIRINGS = 3;
    public static final int NUM_ALLOWED_ATTEMPTS = 1000000;
    public static final int NUM_ALLOWED_REASSIGNMENTS = 1000000;
    private List<SectionGrouping> groupings;
    private List<Student> students;
    private Random random;
    private int numRepairings;
    private int numAttempts;
    private int numReassignments;

    public GroupGenerator(int numGroupings, List<Student> studentList) {
        random = new Random();
        groupings = new ArrayList<>();
        for (int i = 0; i < numGroupings; i++) {
            groupings.add(new SectionGrouping(studentList));
        }

        students = studentList;
        numRepairings = 0;
        numAttempts = 0;
        numReassignments = 0;
    }

    public static void main(String[] args) throws IOException {
        File input = new File("mappingfile.csv");
        FileInputStream inputStream = new FileInputStream(input);
        Parser parser = new CSVParser(new ProperNameStudentStrategy(), new ProperNameGroupStrategy());
        List<Student> students = parser.parseSheetOfStudents(inputStream);
        List<Group> previousGroups =  parser.parseSheetOfGroups(inputStream);
        for (Group group : previousGroups){
            for (Student student : group) {
                for (Student member : group) {
                    if (!student.equals(member)) {
                        student.addCollaborator(member);
                    }
                }
            }
        }

        // TODO: only retry until success?
        GroupGenerator generator = new GroupGenerator(NUM_GROUPINGS, students);
        for (int i = 0; i < 1000; i++) {
            try {
                generator.generateGroups();
            } catch (Exception e) {
                // ignore, try again
                generator = new GroupGenerator(NUM_GROUPINGS, students);
                System.out.println("numIterations: " + i);
            }
        }
        generator.generateOutput();
    }

    private void generateGroups() throws Exception {
        for (SectionGrouping grouping : groupings) {
            generateGrouping(grouping);
        }
    }

    private void generateGrouping(SectionGrouping grouping) throws Exception {
        Queue<Group> groupsToGenerate = new LinkedList<>();
        for (Group group : grouping) {
            groupsToGenerate.add(group);
        }
        while (groupsToGenerate.size() != 0) {
            generateGroupForGrouping(groupsToGenerate, grouping);
        }
    }

    private void generateGroupForGrouping(Queue<Group> groupsToGenerate, SectionGrouping grouping) throws Exception {
        Group group = groupsToGenerate.poll();
        while (!group.isFull()) {
            Set<Student> collaborators = new HashSet<>();
            for (Student s : group) {
                collaborators.addAll(s.getCollaborators());
            }
            List<Student> availableStudents = grouping.getSubsetOfUnassignedStudents(s -> !collaborators.contains(s));
            Student chosenStudent;
            if (availableStudents.size() != 0) {
                chosenStudent = chooseAtRandom(availableStudents);
            } else if (numRepairings < NUM_ALLOWED_REPAIRINGS) {
                chosenStudent = chooseAtRandom(grouping.getSubsetOfUnassignedStudents(s -> true));
                numRepairings++;
            } else {
                if (numReassignments >= NUM_ALLOWED_REASSIGNMENTS) {
                    // TODO: make custom exception here
                    throw new Exception();
                }
                availableStudents = students.stream()
                        .filter(s -> !collaborators.contains(s))
                        .collect(Collectors.toList());
                if (availableStudents.size() == 0) {
                    groupsToGenerate.add(group);
                    Group unluckyGroup = chooseAtRandom(grouping.iterator());
                    while (unluckyGroup.isEmpty()) {
                        unluckyGroup = chooseAtRandom(grouping.iterator());
                    }
                    Student unluckyStudent = chooseAtRandom(unluckyGroup.iterator());
                    unluckyGroup.removeStudent(unluckyStudent);
                    groupsToGenerate.add(unluckyGroup);
                    numAttempts++;
                    if (numAttempts >= NUM_ALLOWED_ATTEMPTS) {
                        throw new Exception();
                    }
                    return;
                }
                chosenStudent = chooseAtRandom(availableStudents);
                for (Group g : grouping) {
                    if (g.containsStudent(chosenStudent)) {
                        g.removeStudent(chosenStudent);
                        groupsToGenerate.add(g);
                    }
                }
                numReassignments++;
            }
            group.addStudent(chosenStudent);
            grouping.markStudentIneligible(chosenStudent);
        }
    }

    private <T> T chooseAtRandom(List<T> toChooseFrom) {
        if (toChooseFrom.size() == 1) {
            return toChooseFrom.get(0);
        }
        int randomIndex = random.nextInt(toChooseFrom.size());
        return toChooseFrom.get(randomIndex);
    }

    private <T> T chooseAtRandom(Iterator<T> toChooseFrom) {
        Iterable<T> iterable = () -> toChooseFrom;
        return StreamSupport.stream(iterable.spliterator(), false).findAny().get();
        // TODO: consider doing reservoir sampling to make use of the random object
    }

    private void generateOutput() {
        for (SectionGrouping grouping : groupings) {
            grouping.enumerateGroups();
            PDFGenerator.generateSectionGroupingPDF(grouping);
        }
    }
}
