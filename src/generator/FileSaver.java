package generator;

import price.Price;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FileSaver {
    public void save(List<Price> pricesList, String fileName, String delimiter) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8));
            pricesList.forEach(v -> writeLine(bw, v, delimiter));
            bw.flush();
            bw.close();
            System.out.printf("File %s saved%n", fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeLine(BufferedWriter bw, Price v, String delimiter) {
        StringBuilder line = new StringBuilder();
        line.append(v.getId());
        line.append(delimiter);
        line.append(v.getName());
        line.append(delimiter);
        line.append(v.getCondition());
        line.append(delimiter);
        line.append(v.getState());
        line.append(delimiter);
        line.append(v.getPrice());
        try {
            bw.write(line.toString());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateTestData(int filesNumber, int recordsNumber, String path) {
        FileSaver fileSaver = new FileSaver();
        Generator generator = new Generator();
        for (int i = 1; i <= filesNumber; i++) {
            fileSaver.save(generator.generate(recordsNumber), path + "/test" + i + ".csv", ",");
        }
    }


}
