import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Sorter {

    private static final Integer RESULT_SIZE = 10;

    public static void main(String[] args) {
        Random random = new Random();

        List<Integer> list01 = IntStream.range(0, 2001)
                .map(v -> random.nextInt(10000))
                .boxed()
                .collect(Collectors.toList());
        List<Integer> list02 = IntStream.range(0, 2001)
                .map(v -> random.nextInt(20000))
                .boxed()
                .collect(Collectors.toList());
        List<Integer> list03 = IntStream.range(0, 2001)
                .map(v -> random.nextInt(30000))
                .boxed()
                .collect(Collectors.toList());
        List<Integer> list04 = IntStream.range(0, 2001)
                .map(v -> random.nextInt(40000))
                .boxed()
                .collect(Collectors.toList());
        List<Integer> list05 = IntStream.range(0, 4001)
                .map(v -> random.nextInt(70000))
                .boxed()
                .collect(Collectors.toList());

        ThreadSafeTreeSet<Integer> result = new ThreadSafeTreeSet<>(RESULT_SIZE);
        ResultPrinter resultPrinter = new ResultPrinter(result, 5);

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        executorService.execute(resultPrinter);
        executorService.execute(new Processor("worker 1", result, list01, resultPrinter));
        executorService.execute(new Processor("worker 2", result, list02, resultPrinter));
        executorService.execute(new Processor("worker 3", result, list03, resultPrinter));
        executorService.execute(new Processor("worker 4", result, list04, resultPrinter));
        executorService.execute(new Processor("worker 5", result, list05, resultPrinter));

        executorService.shutdown();
    }

    private static class Processor implements Runnable {
        private final String name;
        private final ThreadSafeTreeSet<Integer> resultSet;
        private final List<Integer> processingList;
        private final ResultPrinter resultPrinter;

        public Processor(String name, ThreadSafeTreeSet<Integer> resultSet, List<Integer> processingList, ResultPrinter resultPrinter) {
            this.name = name;
            this.resultSet = resultSet;
            this.processingList = processingList;
            this.resultPrinter = resultPrinter;
        }

        @Override
        public void run() {
            Integer smallest = resultSet.getSmallest();
            if (smallest == null)
                smallest = 0;
            for (Integer item : processingList) {
                if (item > smallest) {
                    resultSet.add(item);
                    smallest = resultSet.getSmallest();
                }
            }
            resultPrinter.workerFinished(this);
        }

        public String getName() {
            return name;
        }
    }

    private static class ResultPrinter implements Runnable {
        private final ThreadSafeTreeSet<Integer> result;
        private final int workersCount;
        private int workersFinished = 0;

        public ResultPrinter(ThreadSafeTreeSet<Integer> result, int workersCount) {
            this.result = result;
            this.workersCount = workersCount;
        }

        public synchronized void workerFinished(Processor processor) {
            System.out.printf("Worker %s is finished %n", processor.getName());
            workersFinished++;
            if (workersFinished == workersCount) {
                System.out.println("Result:");
                System.out.println(result.print());
            }
        }

        @Override
        public void run() {
        }
    }

}
