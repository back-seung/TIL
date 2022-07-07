package lambda;

import java.util.function.Function;

import static java.lang.Integer.toBinaryString;

public class HTest {
    public static void main(String[] args) {
        Function<String, Integer> f = s -> Integer.parseInt(s, 16);
        Function<Integer, String> g = i -> toBinaryString(i);
        Function<String, String> h = f.andThen(g);
        System.out.println(f.apply("FF"));
        
        System.out.println(h.apply("FF"));
    }
}
