package price;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PriceMapper {
    public static final String DELIMITER = ",";
    public static final Function<String, Price> PRICE_MAPPER = v -> {
        String[] fields = v.split(DELIMITER);
        return new Price(Integer.parseInt(fields[0]), fields[1], fields[2], fields[3], Float.parseFloat(fields[4]));
    };

    public List<Price> getPricesList(File file, Function<String, Price> mapper) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            return reader
                    .lines()
                    .map(mapper)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }
}
