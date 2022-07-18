package stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class FromCollectionExample {
	public static void main(String[] args) {
		List<Student> studentList = Arrays.asList(new Student("baek", 25, Student.SEX.MALE), new Student("weon", 25, Student.SEX.MALE),
				new Student("cho", 25, Student.SEX.MALE));
		
		Stream<Student> stream = studentList.stream();
		
		stream.forEach(s -> {
			System.out.println(s.getName());
		});
	}
}
