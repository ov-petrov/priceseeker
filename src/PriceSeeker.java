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

public class PriceSeeker {

    private static final Integer RESULT_SIZE = 100;
    private static final Integer MAX_SAME_ID = 20;

    public static void main(String[] args) {
        String directory = args.length == 0 ? "/testData" : args[0];
        FilesParser filesParser = new FilesParser(directory);
        List<File> filesToParse = filesParser.getFilesInDirectory();
        final int filesNumber = filesToParse.size();
        System.out.printf("Found %s .CSV files in directory %s%n", filesNumber, directory);

        ThreadSafeTreeSet<Price> result = new ThreadSafeTreeSet<>(RESULT_SIZE);
        ResultPrinter resultPrinter = new ResultPrinter(result, filesNumber);

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        executorService.execute(resultPrinter);
        for (int i = 0; i < filesNumber; i++) {
            File file = filesToParse.get(i);
            System.out.printf("Preparing to process file %s%n", file.getName());
            PriceMapper priceMapper = new PriceMapper();
            List<Price> pricesList = priceMapper.getPricesList(file, PriceMapper.PRICE_MAPPER);

            executorService.execute(new Processor("Worker " + i + ". Processing filename: " + file.getName(), result, pricesList, resultPrinter, MAX_SAME_ID));
        }

        executorService.shutdown();
    }


}
