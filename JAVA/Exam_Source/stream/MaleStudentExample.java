package stream;import java.util.Arrays;import java.util.List;public class MaleStudentExample {    public static void main(String[] args) {        List<Student> totalList = Arrays.asList(                new Student("홍길동", 25, Student.SEX.MALE),                new Student("백승한", 6, Student.SEX.MALE),                new Student("김수애", 10, Student.SEX.FEMALE),                new Student("박수미", 6, Student.SEX.FEMALE)        );        MaleStudent maleStudent = totalList.stream()                .filter(s -> s.getSEX() == Student.SEX.MALE)                .collect(MaleStudent::new, MaleStudent::accumulate, MaleStudent::combine);        maleStudent.getList().stream()                .forEach(s -> System.out.println(s.getName()));    }}