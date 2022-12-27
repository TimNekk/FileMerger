package timnekk;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileWorker {
    private final File root;

    public FileWorker(File root) {
        this.root = root;
    }

    public Set<File> getDependencies(File file) {
        Set<File> dependencies = new HashSet<>();

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                Pattern pattern = Pattern.compile("require ‘(.+)’");
                Matcher matcher = pattern.matcher(line);

                if (matcher.find()) {
                    String dependencyPath = matcher.group(1);
                    File dependency = new File(root, dependencyPath);
                    dependencies.add(dependency);
                }
            }

            return dependencies;
        } catch (FileNotFoundException e) {
            return dependencies;
        }
    }

    public static Set<File> getFiles(File rootFile) {
        Set<File> files = new HashSet<>();

        for (File file : rootFile.listFiles()) {
            if (file.isFile()) {
                files.add(file);
            } else if (file.isDirectory()) {
                files.addAll(getFiles(file));
            }
        }

        return files;
    }
}
