package runnable;

import generator.FileSaver;
import price.Price;
import price.ThreadSafeTreeSet;

import java.util.List;
import java.util.stream.Collectors;

public class ResultPrinter implements Runnable {
    private final ThreadSafeTreeSet<Price> result;
    private final int workersCount;
    private int workersFinished = 0;

    public ResultPrinter(ThreadSafeTreeSet<Price> result, int workersCount) {
        this.result = result;
        this.workersCount = workersCount;
    }

    public synchronized void workerFinished(Processor processor) {
        System.out.printf("%s is finished%n", processor.getName());
        workersFinished++;
        if (workersFinished == workersCount) {
            FileSaver fileSaver = new FileSaver();
            List<Price> finalPrices = this.result.getResult();
            fileSaver.save(finalPrices, "result.csv", ",");
            System.out.println("Result:");
            System.out.println(finalPrices.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(",\n")));
        }
    }

    @Override
    public void run() {
    }
}
