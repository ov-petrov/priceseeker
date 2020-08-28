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

    public static void main(String[] args) {
        FileSaver fileSaver = new FileSaver();
        Generator generator = new Generator();
        fileSaver.save(generator.generate(20), "testData/test02.csv", ",");

    }

}
