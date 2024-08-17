package com.client.utilities;

import java.io.*;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;

public class JsonUtil {

    private static ObjectMapper jsonMapper = new ObjectMapper();
    private static ObjectWriter jsonWriter = null;
    private static ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
    private static ObjectWriter yamlWriter = null;

    static {
        // Grab values from fields only instead of methods
        Lists.newArrayList(jsonMapper, yamlMapper).stream().forEach(it -> {
            it.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
            it.setVisibility(it.getSerializationConfig().
                    getDefaultVisibilityChecker()
                    .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                    .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                    .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                    .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
        });

        jsonWriter = jsonMapper.writerWithDefaultPrettyPrinter();
        yamlWriter = yamlMapper.writerWithDefaultPrettyPrinter();
    }

    @Deprecated
    public static <T> void toJson(T t, String filePath) {
        Gson prettyGson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        String prettyJson = prettyGson.toJson(t);
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(new File(filePath)));
            bw.write(prettyJson);
            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public static <T> T fromJson(String filePath) throws IOException {
        return fromJson(filePath, new TypeToken<T>(){});
    }

    @Deprecated
    public static <T> T fromJson(String data, TypeToken<T> typeToken) throws IOException {
        return new Gson().fromJson(data, typeToken.getType());
    }

    public static <T> T fromJson(InputStream data, TypeToken<T> typeToken) throws IOException {
        return new Gson().fromJson(inputStreamToString(data), typeToken.getType());
    }


    @Deprecated
    public static <T> T fromJsonOrDefault(InputStream inputStream, TypeToken<T> typeToken, T defaultObject) throws IOException {
        return fromJson(inputStreamToString(inputStream), typeToken);
    }

    public static <T> T fromJsonString(String jsonString, TypeToken<T> typeToken) throws IOException {
        return new Gson().fromJson(jsonString, typeToken.getType());
    }

    public static <T> String toJsonString(T object) throws IOException {
        return new Gson().toJson(object);
    }

    /**
     * Serialize an object to json with Jackson.
     * Note: it's supposed to only use fields but some methods are still
     * used. For instance if you have no flag field
     * and there's a method called getFlag() it will call it during serialization.
     * So be sure to check and mark the methods with {@link com.fasterxml.jackson.annotation.JsonIgnore}
     * if it needs to be ignored.
     */
    public static <T> void toJacksonJson(T t, String filePath) throws IOException {
        jsonWriter.writeValue(new File(filePath), t);
    }

    public static <T> T fromJacksonJson(String filePath, TypeReference clazz) throws IOException {
        return (T) jsonMapper.readValue(new File(filePath), clazz);
    }

    /**
     * Serialize an object to yaml with Jackson.
     * Note: it's supposed to only use fields but some methods are still
     * used. For instance if you have no flag field
     * and there's a method called getFlag() it will call it during serialization.
     * So be sure to check and mark the methods with {@link com.fasterxml.jackson.annotation.JsonIgnore}
     * if it needs to be ignored.
     */
    public static <T> void toYaml(T t, String filePath) throws IOException {
        yamlWriter.writeValue(new File(filePath), t);
    }

    public static <T> T fromYaml(InputStream filePath, TypeReference clazz) throws IOException {
        return (T) yamlMapper.readValue(inputStreamToString(filePath), clazz);
    }
    public static String inputStreamToString(InputStream inputStream) throws IOException {
        // Read all bytes from the input stream
        byte[] bytes = inputStream.readAllBytes();

        // Convert the byte array to a String using the UTF-8 encoding
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
