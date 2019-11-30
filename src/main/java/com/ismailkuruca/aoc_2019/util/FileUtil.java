package com.ismailkuruca.aoc_2019.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtil {
    public static List<String> readFile(String file) {
        Path path = null;
        try {
            path = Paths.get(FileUtil.class.getClassLoader()
                    .getResource(file).toURI());
            return Files.lines(path).collect(Collectors.toList());
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }
}
