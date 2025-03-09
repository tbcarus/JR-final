package ru.tbcarus.jrfinal;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class EnvLoader {
    public static Properties loadEnv(String envFileName) throws IOException {
        Properties properties = new Properties();
        File envFile = new File("src/test/resources/" + envFileName);
        if (envFile.exists()) {
            try (FileReader reader = new FileReader(envFile)) {
                properties.load(reader);
            }
        } else {
            throw new IOException("Файл не найден: " + envFileName);
        }
        return properties;
    }
}