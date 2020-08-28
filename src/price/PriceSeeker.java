package price;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PriceSeeker {

    private static final Integer RESULT_SIZE = 10000;
    private static final Integer MAX_SAME_ID = 20;

    public static void main(String[] args) {
        FilesParser filesParser = new FilesParser(args.length == 0 ? "C:/fake-data" : args[0]);
        List<File> filesToParse = filesParser.getFilesInDirectory();
        final int filesNumber = filesToParse.size();

        ThreadSafeTreeSet<Price> result = new ThreadSafeTreeSet<>(RESULT_SIZE);
        ResultPrinter resultPrinter = new ResultPrinter(result, filesNumber);

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        executorService.execute(resultPrinter);
        for (int i = 0; i < filesNumber; i++) {
            File file = filesToParse.get(i);
            PriceMapper priceMapper = new PriceMapper();
            List<Price> pricesList = priceMapper.getPricesList(file, PriceMapper.PRICE_MAPPER);

            executorService.execute(new Processor("Worker " + i + "file name: " + file.getName(), result, pricesList, resultPrinter, MAX_SAME_ID));
        }

        executorService.shutdown();
    }


}
