package price;

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
            System.out.println("Result:");
            System.out.println(result.print());
        }
    }

    @Override
    public void run() {
    }
}
