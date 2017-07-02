package de.upb.cs.is.jpl.api.util;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import de.upb.cs.is.jpl.api.exception.configuration.json.JsonParsingFailedException;


/**
 * This util class offers convenience methods for {@code JSON}.
 * 
 * @author Tanja Tornede
 *
 */
public class JsonUtils {

   /**
    * The file extension of a json file.
    */
   public static final String JSON_FILE_NAME_EXTENSION = ".json";


   private JsonUtils() {
   }


   /**
    * Creates a {@link JsonArray} from the given json string.
    * 
    * @param jsonString the json string to parse
    * @return the {@link JsonArray} received while parsing the given json string
    * @throws JsonParsingFailedException if any exception occurred at runtime
    */
   public static JsonArray createJsonArrayFromString(final String jsonString) throws JsonParsingFailedException {
      InputStream stream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
      Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);

      JsonArray readJsonArray = null;
      try {
         readJsonArray = new Gson().fromJson(reader, JsonArray.class);
      } catch (JsonSyntaxException jsonSyntaxException) {
         throw new JsonParsingFailedException(jsonSyntaxException);
      } catch (JsonIOException jsonIoException) {
         throw new JsonParsingFailedException(jsonIoException);
      }

      return readJsonArray;
   }


   /**
    * Creates a {@link JsonObject} from the given json string.
    * 
    * @param jsonString the json string to parse
    * @return the {@link JsonObject} received while parsing the given json string
    * @throws JsonParsingFailedException if any exception occurred at runtime
    */
   public static JsonObject createJsonObjectFromString(final String jsonString) throws JsonParsingFailedException {
      InputStream stream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
      Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);

      JsonObject readJsonObject = null;
      try {
         readJsonObject = new Gson().fromJson(reader, JsonObject.class);
      } catch (JsonSyntaxException jsonSyntaxException) {
         throw new JsonParsingFailedException(jsonSyntaxException);
      } catch (JsonIOException jsonIoException) {
         throw new JsonParsingFailedException(jsonIoException);
      }

      return readJsonObject;
   }


   /**
    * Creates a {@link JsonObject} from the json file which can be found at the given path.
    * 
    * Note that this method should only be used with a jsonFilePath starting at folders inside the
    * resource directory, e.g. algorithm/baselearner/... . Otherwise it will result in a
    * {@link NullPointerException}. For accessing {@link JsonObject}s with a more detailed path, use
    * {@link #createJsonObjectFromFile(File)}.
    * 
    * @param jsonFilePath the path of the json file
    * @return the {@link JsonObject} received while parsing the json file
    * @throws JsonParsingFailedException if any exception occurred at runtime
    * @throws NullPointerException if the given file path does not start with a folder or file
    *            inside the resource directory
    */
   public static JsonObject createJsonObjectFromFilePath(final String jsonFilePath) throws JsonParsingFailedException {
      JsonObject readJsonObject = null;
      InputStream inputStream = JsonUtils.class.getResourceAsStream(jsonFilePath);
      InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
      try (Reader reader = new BufferedReader(inputStreamReader)) {
         readJsonObject = new Gson().fromJson(reader, JsonObject.class);
      } catch (JsonSyntaxException jsonSyntaxException) {
         throw new JsonParsingFailedException(jsonSyntaxException);
      } catch (JsonIOException jsonIoException) {
         throw new JsonParsingFailedException(jsonIoException);
      } catch (FileNotFoundException fileNotFoundException) {
         throw new JsonParsingFailedException(fileNotFoundException);
      } catch (IOException ioException) {
         throw new JsonParsingFailedException(ioException);
      }
      return readJsonObject;
   }


   /**
    * Creates a {@link JsonObject} from the given json file.
    * 
    * @param jsonFile the json filet to read
    * @return the {@link JsonObject} received while parsing the json file
    * @throws JsonParsingFailedException if any exception occurred at runtime
    */
   public static JsonObject createJsonObjectFromFile(final File jsonFile) throws JsonParsingFailedException {
      JsonObject readJsonObject = null;
      try (Reader reader = new FileReader(jsonFile)) {
         readJsonObject = new Gson().fromJson(reader, JsonObject.class);
      } catch (JsonSyntaxException jsonSyntaxException) {
         throw new JsonParsingFailedException(jsonSyntaxException);
      } catch (JsonIOException jsonIoException) {
         throw new JsonParsingFailedException(jsonIoException);
      } catch (FileNotFoundException fileNotFoundException) {
         throw new JsonParsingFailedException(fileNotFoundException);
      } catch (IOException ioException) {
         throw new JsonParsingFailedException(ioException);
      }
      return readJsonObject;
   }


   /**
    * Creates a {@link JsonObject} with the given key and value as a property.
    * 
    * @param key the key of the property of the JSON object
    * @param value the value of the property of the JSON object
    * @return the created JSON object
    */
   public static JsonObject createJsonObjectFromKeyAndValue(String key, String value) {
      JsonObject jsonParameterObject = new JsonObject();
      jsonParameterObject.addProperty(key, String.valueOf(value));

      return jsonParameterObject;
   }


   /**
    * Creates a {@link JsonObject} with the given keys and values as a property.
    * 
    * @param keys the keys as array of the property of the JSON object
    * @param values the value as array of the property of the JSON object
    * @return a {@link JsonObject} with the given properties
    */
   public static JsonObject createJsonObjectFromKeyAndValueArrays(String[] keys, String[] values) {
      JsonObject jsonParameterObject = new JsonObject();
      for (int i = 0; i < keys.length; i++) {
         jsonParameterObject.addProperty(keys[i], String.valueOf(values[i]));
      }
      return jsonParameterObject;
   }

}
