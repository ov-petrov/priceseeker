package generator;

import price.Price;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Generator {
    private Random random = new Random();

    public List<Price> generate(Integer count) {
        return IntStream.rangeClosed(0, count).boxed()
                .map(v -> new Price(v * 10, generateString(10), generateString(8), generateString(6), generateFloat()))
                .collect(Collectors.toList());
    }

    private Float generateFloat() {
        return random.nextFloat() * random.nextInt(1000);
    }

    private String generateString(int length) {
        final int leftLimit = 97; // letter 'a'
        final int rightlimit = 122; // letter 'z'

        return random.ints(leftLimit, rightlimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
