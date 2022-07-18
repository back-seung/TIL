package stream;

import java.util.Arrays;
import java.util.stream.Stream;

public class FromArrayExample {
	public static void main(String[] args) {
		String[] strArray = {"baek", "weon", "cho"};
		Stream<String> strStream = Arrays.stream(strArray);
		
		strStream.forEach(a -> System.out.print(a + ", "));
	}
}
