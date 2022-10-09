package ru.amazin.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Log4j2
public class Mapper {

    private final static ObjectMapper mapper = new ObjectMapper();
    private final static String path = PropertiesReader.getInstance().getProperty("json_path");
    private final static String FILE_EXTENSION = ".json";

    public static <T> T getPojoFromJson(Class<T> clazz, String fileName) {
        T pojo = null;
        try {
            pojo = mapper.readValue(new File(path + fileName + FILE_EXTENSION), clazz);
        } catch (IOException e) {
            log.error("Receiving json file error " + Arrays.toString(e.getStackTrace()));
        }
        return pojo;
    }
}