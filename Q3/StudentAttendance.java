
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

class Student {

    private final String regNo;
    private final String name;

    Student(String regNo, String name) {
        this.regNo = regNo;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Student)) {
            return false;
        }

        Student s = (Student) o;

        return regNo.equals(s.regNo);
    }

    // TODO 1: Complete hashCode()

    /*
    @Override
    public int hashCode() {
        return Objects.hash(regNo);
    }
    */
}

public class StudentAttendance {

    public static void main(String[] args) {

        Student s1 = new Student("192511336", "Lakshitha");
        Student s2 = new Student("192511336", "Lakshitha");
        Student s3 = new Student("192511336", "Lakshitha");
        Student s4 = new Student("192511337", "Krisha");
        Student s5 = new Student("192511336", "Lakshitha Sree");

        // Test 1: Same register number and same name
        Set<Student> test1 = new HashSet<>();
        test1.add(s1);
        test1.add(s2);

        System.out.println("Test 1 - Same student: "
                + test1.size());

        // Test 2: Same register number, different name
        Set<Student> test2 = new HashSet<>();
        test2.add(s1);
        test2.add(s5);

        System.out.println("Test 2 - Same register number: "
                + test2.size());

        // Test 3: Different register numbers
        Set<Student> test3 = new HashSet<>();
        test3.add(s1);
        test3.add(s4);

        System.out.println("Test 3 - Different students: "
                + test3.size());

        // Overall attendance
        Set<Student> attendance = new HashSet<>();

        attendance.add(s1);
        attendance.add(s2);
        attendance.add(s3);
        attendance.add(s4);
        attendance.add(s5);

        System.out.println("Total scans: 5");
        System.out.println("Unique students: "
                + attendance.size());
    }
}

