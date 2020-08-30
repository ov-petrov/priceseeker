package runnable;

import generator.FileSaver;
import price.Price;
import price.ThreadSafeTreeSet;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ResultPrinter implements Runnable {
    private final ThreadSafeTreeSet<Price> result;
    private final int workersCount;
    private int workersFinished = 0;
    private final long startTime;

    public ResultPrinter(ThreadSafeTreeSet<Price> result, int workersCount) {
        startTime = System.currentTimeMillis();
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

            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            System.out.printf("The task is completed. Elapsed time: %d min %d sec%n",
                    TimeUnit.MILLISECONDS.toMinutes(elapsedTime),
                    TimeUnit.MILLISECONDS.toSeconds(elapsedTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsedTime)));
        }
    }

    @Override
    public void run() {
    }
}
