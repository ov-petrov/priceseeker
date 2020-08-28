package price;

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
            biggest.setPrice(100_000_000.00F);
        }
        for (Price item : processingList) {
            if (item.price < biggest.price && checkProductId(resultSet, item)) {
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
        return resultSet.checkProperty(biFunction, item) < maxSameId;
    }

    public String getName() {
        return name;
    }
}
