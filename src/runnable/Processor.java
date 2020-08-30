package runnable;

import price.Price;
import price.ThreadSafeTreeSet;

import java.util.List;
import java.util.TreeSet;
import java.util.function.BiFunction;

public class Processor implements Runnable {
    private final String name;
    private final ThreadSafeTreeSet<Price> resultSet;
    private final List<Price> processingList;
    private final ResultPrinter resultPrinter;
    private final Integer maxSameId;

    public Processor(String name, ThreadSafeTreeSet<Price> resultSet, List<Price> processingList, ResultPrinter resultPrinter, Integer maxSameId) {
        this.name = name;
        this.resultSet = resultSet;
        this.processingList = processingList;
        this.resultPrinter = resultPrinter;
        this.maxSameId = maxSameId;
    }

    @Override
    public void run() {
        Price biggest = resultSet.getBiggest();
        if (biggest == null) {
            biggest = new Price();
            biggest.setPrice(100_000_000.00F); // Put very big price
        }
        // It's not a problem if a 'biggest' value become a bit obsolete in the process of searching
        for (Price item : processingList) {
            if (item.getPrice() < biggest.getPrice() && checkProductId(resultSet, item)) {
                System.out.printf("Founded small price: %s. The biggest price at the result collection: %s%n", item.getPrice(), biggest.getPrice());
                resultSet.add(item);
                biggest = resultSet.getBiggest();
            }
        }
        resultPrinter.workerFinished(this);
    }

    private boolean checkProductId(ThreadSafeTreeSet<Price> resultSet, Price item) {
        BiFunction<TreeSet<Price>, Price, Integer> biFunction = (set, pr) -> (int) set.stream()
                .filter(v -> v.getId().equals(pr.getId()))
                .count();
        boolean result = resultSet.checkProperty(biFunction, item) < maxSameId;
        if (!result)
            System.out.printf("Price with id %s can't added because already added %s prices with same id", item.getId(), maxSameId);

        return result;
    }

    public String getName() {
        return name;
    }
}
