import java.io.*;
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

    private void generateGroupForGrouping(Queue<Group> groupsToGenerate, SectionGrouping grouping)
            throws Exception {
        Group group = groupsToGenerate.poll();
        while (!group.isFull()) {
            Set<Student> collaborators = new HashSet<>();
            for (Student s : group) {
                collaborators.addAll(s.getCollaborators());
            }
            List<Student> availableStudents =
                    grouping.getSubsetOfUnassignedStudents(s -> !collaborators.contains(s));
            Student chosenStudent;
            if (availableStudents.size() != 0) {
                chosenStudent = chooseAtRandom(availableStudents);
            } else if (numRepairings < NUM_ALLOWED_REPAIRINGS) {
                chosenStudent = chooseAtRandom(grouping.getSubsetOfUnassignedStudents(s -> true));
                numRepairings++;
            } else {
                if (numReassignments >= NUM_ALLOWED_REASSIGNMENTS) {
                    throw new Exception();
                }
                availableStudents =
                        students.stream().filter(s -> !collaborators.contains(s))
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
            group.addStudentToGroup(chosenStudent);
            grouping.removeStudentFromEligibility(chosenStudent);
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
        int index = 2;
        for (SectionGrouping grouping : groupings) {
            BufferedWriter writer = null;
            try {
                File outputFile = new File("output" + (index + 1) + ".tex");
                writer = new BufferedWriter(new FileWriter(outputFile));

                writer.write("\\documentclass{article}\n" + "\\usepackage{multirow}\n"
                        + "\\usepackage{vmargin}\n" + "\\usepackage{tikz}\n"
                        + "\\usepackage{caption}\n" + "\\usepackage{microtype}\n"
                        + "\\setpapersize{USletter}\n"
                        + "\\setmarginsrb{1in}{.5in}{1in}{0.25in}{12pt}{11mm}{0pt}{11mm}\n"
                        + "\\begin{document}\n" + "\\renewcommand{\\arraystretch}{1.2} \n");
                writer.write("\\begin{center}\n" + "{\\Large Design Challenge: " + (index + 1) +
                        "\\\\ \n" + "Group Assignments} \\\\ \n");
                writer.write("\\begin{tabular}{|c|} \\hline \n");
                int i = 0;
                for (Group g : grouping) {
                    if (i != 0) {
                        writer.write("\\hline \n");
                    }
                    for (Student s : g) {
                        writer.write(s.getName().replaceAll("\"", "") + "\\\\ \\hline \n");
                    }
                    writer.write("\\multicolumn{1}{c}{\\vspace{1mm}} \\\\ \n");
                    i++;
                }
                writer.write("\n");
                writer.write("\\end{tabular} \\\\ \n");

                writer.write("\\end{center}\n" + "\\end{document}");
            } catch (Exception e) {
                // cry
            } finally {
                try {
                    writer.close();
                } catch (IOException | NullPointerException e) {
                    // cry again
                }
            }
            index++;
        }
    }

    public void printCollaborators() {
        for (Student student : students) {
            List<String> collaborators =
                    student.getCollaborators().stream().map(Student::getName)
                            .collect(Collectors.toList());
            Collections.sort(collaborators);
            System.out.print(student + ": ");
            System.out.println(collaborators);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Map<String, Student> studentMapping = new HashMap<>();
        List<Student> students = new ArrayList<>();

        Scanner scanner = new Scanner(new File("ThursdayLateWithDC1.csv"));
        while (scanner.hasNextLine()) {
            String data = scanner.nextLine();
            String[] names = data.replaceAll("\"", "").split(",");
            String[] name = {names[0], names[1]};
            Student newStudent = new Student(name[1], name[0]);
            studentMapping.put(Arrays.asList(name).stream().collect(Collectors.joining()), newStudent);
            students.add(newStudent);
        }
        scanner.close();

        Scanner secondScanner = new Scanner(new File("ThursdayLateWithDC1.csv"));
        secondScanner.useDelimiter(",");
        while (secondScanner.hasNextLine()) {
            String data = secondScanner.nextLine();
            String[] names = data.replaceAll("\"", "").split(",");
            String[] name = {names[0], names[1]};
            Student student = studentMapping.get(Arrays.asList(name).stream().collect(Collectors.joining()));
            boolean contains = studentMapping.containsKey(Arrays.asList(name).stream().collect(Collectors.joining()));
            String[] collaborators = Arrays.copyOfRange(names, 2, names.length);
            for (int i = 0; i < collaborators.length; i += 2) {
                String[] collaboratorName = {collaborators[i], collaborators[i + 1]};
                boolean sa = studentMapping.containsKey(Arrays.asList(collaboratorName).stream().collect(Collectors.joining()));
                student.addCollaborator(studentMapping.get(Arrays.asList(collaboratorName).stream().collect(Collectors.joining())));
            }
        }
        secondScanner.close();

        GroupGenerator generator = new GroupGenerator(NUM_GROUPINGS, students);
        for (int i = 0; i < 1000; i++) {
            try {
                generator.generateGroups();
            } catch (Exception e) {
                // i)gnore, try again
                generator = new GroupGenerator(NUM_GROUPINGS, students);
                System.out.println("numIterations: " + i);
            }
        }
        generator.generateOutput();
        generator.printCollaborators();
    }

}
