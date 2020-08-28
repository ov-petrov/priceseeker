import generator.FileSaver;
import price.FilesParser;
import price.Price;
import price.PriceMapper;
import price.ThreadSafeTreeSet;
import runnable.Processor;
import runnable.ResultPrinter;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PriceSeeker {

    private static final Integer DEFAULT_RESULT_SIZE = 1000;
    private static final Integer DEFAULT_MAX_SAME_ID = 20;
    private static final String DEFAULT_PATH = "E:/temp/td";

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        String path = DEFAULT_PATH;
        if (args.length == 4) {
            switch (args[0]) {
                case "generate":
                    int filesNumber = Integer.parseInt(args[1]);
                    int recordsNumber = Integer.parseInt(args[2]);
                    path = args[3];
                    new FileSaver().generateTestData(filesNumber, recordsNumber, path);
                    break;
                case "search":
                    int resultRecords = Integer.parseInt(args[1]);
                    int maxSameId = Integer.parseInt(args[2]);
                    path = args[3];
                    new PriceSeeker().processPriceSearching(resultRecords, maxSameId, path);
                default:
                    new PriceSeeker().processPriceSearching(DEFAULT_RESULT_SIZE, DEFAULT_MAX_SAME_ID, path);
            }
        } else {
            new PriceSeeker().processPriceSearching(DEFAULT_RESULT_SIZE, DEFAULT_MAX_SAME_ID, path);
        }

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.printf("Task finished. Elapsed time: %d min %d sec%n",
                TimeUnit.MILLISECONDS.toMinutes(elapsedTime),
                TimeUnit.MILLISECONDS.toSeconds(elapsedTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsedTime)));

    }

    public void processPriceSearching(int resultSize, int maxSameId, String path) {
        FilesParser filesParser = new FilesParser(path);
        List<File> filesToParse = filesParser.getFilesInDirectory();
        final int filesNumber = filesToParse.size();
        System.out.printf("Found %s .CSV files in directory %s%n", filesNumber, path);

        ThreadSafeTreeSet<Price> result = new ThreadSafeTreeSet<>(resultSize);
        ResultPrinter resultPrinter = new ResultPrinter(result, filesNumber);

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);
        executorService.execute(resultPrinter);
        for (int i = 0; i < filesNumber; i++) {
            File file = filesToParse.get(i);
            System.out.printf("Preparing to process file %s%n", file.getName());
            PriceMapper priceMapper = new PriceMapper();
            List<Price> pricesList = priceMapper.getPricesList(file, PriceMapper.PRICE_MAPPER);
            executorService.execute(new Processor("Worker " + i + ". Processing filename: " + file.getName(), result, pricesList, resultPrinter, maxSameId));
        }
        executorService.shutdown();
    }


}
