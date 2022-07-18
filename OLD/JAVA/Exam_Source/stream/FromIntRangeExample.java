package stream;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FromIntRangeExample {
	public static int sum;

	public static void main(String[] args) {

		IntStream stream = IntStream.rangeClosed(1, 100);
		stream.forEach(a -> sum += a);
		System.out.println("รัวี : " + sum);
	}
}
