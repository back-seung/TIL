package stream;import java.util.Arrays;import java.util.Comparator;import java.util.List;import java.util.stream.IntStream;public class SortingExample {    public static void main(String[] args) {        // 숫자 요소일 경우        IntStream intStream = Arrays.stream(new int[]{5, 2, 4, 3, 1});        intStream                .sorted()                .forEach(n -> System.out.print(n + ","));        System.out.println();        //객체 요소일 경우        List<Student> studentList = Arrays.asList(                new Student("홍길동", 30, Student.SEX.MALE),                new Student("백승한", 40, Student.SEX.MALE),                new Student("신용권", 20, Student.SEX.MALE)        );        studentList.stream()                .sorted()   // 오름차순 정렬                .forEach(s -> System.out.print(s.getScore() + ","));        System.out.println();        studentList.stream()                .sorted(Comparator.reverseOrder()) // 내림차순 정렬                .forEach(s -> System.out.print(s.getScore() + ","));    }}