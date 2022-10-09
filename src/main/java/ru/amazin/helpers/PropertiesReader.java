package ru.amazin.helpers;

import ru.amazin.exceptions.GetPropertyException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static ru.amazin.helpers.TestConstants.PROPERTIES_PATH;

public class PropertiesReader {
    private static final PropertiesReader reader = new PropertiesReader();
    private final Properties properties;

    public synchronized static PropertiesReader getInstance() {
        return reader;
    }

    private PropertiesReader() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(PROPERTIES_PATH));
            properties = new Properties();
            try {
                properties.load(reader);
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            GetPropertyException exception = new GetPropertyException("Test.properties not found at " + PROPERTIES_PATH);
            exception.initCause(e);
            throw exception;
        }
    }

    public String getProperty(String propertyName) {
        String property = properties.getProperty(propertyName);
        if (property != null) return property;
        else throw
                new GetPropertyException(
                        String.format("Property \"%s\" not specified in the Test.properties file", propertyName));
    }
}
