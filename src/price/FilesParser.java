package price;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FilesParser {
    private final String directory;

    public FilesParser(String directory) {
        this.directory = directory;
    }

    public List<File> getFilesInDirectory() {
        if (!checkIsDirectory())
            return Collections.emptyList();

        return Arrays.stream(Objects.requireNonNull(new File(this.directory).listFiles()))
                .filter(v -> v.isFile() && v.getName().endsWith(".csv"))
                .collect(Collectors.toList());
    }

    private boolean checkIsDirectory() {
        File directory = new File(this.directory);
        return directory.isDirectory();
    }

    public String getDirectory() {
        return directory;
    }
}
