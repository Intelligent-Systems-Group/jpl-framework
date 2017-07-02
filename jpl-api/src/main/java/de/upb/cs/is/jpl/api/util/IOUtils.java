package de.upb.cs.is.jpl.api.util;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import de.upb.cs.is.jpl.api.dataset.DatasetFile;
import de.upb.cs.is.jpl.api.exception.JplRuntimeException;


/**
 * This util class offers convenience methods for IOs.
 * 
 * @author Tanja Tornede
 *
 */
public class IOUtils {

   private static final String ERROR_GIVEN_PATH_TO_FOLDER_IS_NOT_FOLDER = "The given path to a folder is not a folder.";
   private static PrintStream correctOutputStream = System.out;


   /**
    * Hides the public constructor.
    */
   private IOUtils() {
      throw new IllegalAccessError(StringUtils.EXCEPTION_MESSAGE_ACCESS_ERROR);
   }


   /**
    * Returns the containing string of the given file.
    * 
    * @param filePath the path to file
    * @return the content of the given file
    * @throws IOException if an I/O error occurs while opening the file
    */
   public static String readStringFromFile(final String filePath) throws IOException {
      StringBuilder builder = new StringBuilder();
      try (Stream<String> stream = Files.lines(Paths.get(filePath), Charset.defaultCharset())) {
         for (Object line : stream.toArray()) {
            builder.append(line);
            builder.append(StringUtils.LINE_BREAK);
         }
      }
      return builder.toString();
   }


   /**
    * Redirects the output stream to a {@link ByteArrayOutputStream}.
    * 
    * @return the new source for output
    */
   public static ByteArrayOutputStream redirectOutputStream() {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      PrintStream ps = new PrintStream(byteArrayOutputStream);
      correctOutputStream = System.out;
      System.setOut(ps);
      return byteArrayOutputStream;
   }


   /**
    * Undoes the redirection of the output stream.
    */
   public static void undoOutputStreamRedirection() {
      System.out.flush();
      System.setOut(correctOutputStream);
   }


   /**
    * Returns a list of {@link DatasetFile}'s containing all files stored in the folder of the given
    * path.
    * 
    * @param pathToFolder the path to the folder containing the files to add to the returned list
    * @return a list containing all files stored in the given folder
    */
   public static List<DatasetFile> getListOfDatasetFilesInFolder(String pathToFolder) {
      List<DatasetFile> datasetFileList = new ArrayList<>();
      File folder = new File(pathToFolder);
      if (folder.isDirectory()) {
         for (File file : folder.listFiles()) {
            if (file.isFile()) {
               datasetFileList.add(new DatasetFile(file));
            }
         }
      } else {
         throw new JplRuntimeException(ERROR_GIVEN_PATH_TO_FOLDER_IS_NOT_FOLDER);
      }
      return datasetFileList;
   }
}
