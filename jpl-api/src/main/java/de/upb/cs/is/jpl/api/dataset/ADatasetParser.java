package de.upb.cs.is.jpl.api.dataset;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.exception.dataset.ParsingFailedException;
import de.upb.cs.is.jpl.api.math.linearalgebra.DenseDoubleVector;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.math.linearalgebra.SparseDoubleVector;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This abstract class for a dataset parser handles everything from reading the files to extracting
 * the basic informations about the dataset.
 * 
 * Every parser for GPRF datasets can extend this class by implementing the three abstract methods.
 * These abstract methods define how the created dataset parser handles and stores the values from
 * the context, item and rating files in the dataset.
 * 
 * @author Sebastian Osterbrink
 *
 */
public abstract class ADatasetParser implements IDatasetParser {


   private static final String FEATURE = "Feature ";

   protected static final Logger logger = LoggerFactory.getLogger(ADatasetParser.class);

   protected static final String ERRORS_WHILE_PARSING = "Found Errors while parsing the file:";
   protected static final String NOT_A_PAIR = "The given string \" %s \" is not a pair of feature identifier and value";
   protected static final String INDEX_DOES_NOT_EXIST = "Tried to access an index which does not exist. "
         + "Expected a vector of dimension %s. Setting a value for feature %s is not possible.";
   protected static final String COULD_NOT_OPEN_FILE = "Could not open the given file. "
         + "Please ensure that the file exists, is valid ZIP file and that the encoding of the *.data files is UTF-8.\n";
   protected static final String NO_FORMAT_DECLARATION_FOUND = "No format declaration found. The first line in the rating.data must "
         + "contain a declaration whether the dataset contains absolute or relative data.";
   protected static final String FEATURE_AFTER_VALUE = "Feature declaration is no longer allowed, after values were defined";
   protected static final String NOT_A_NUMBER = "The value \"%s\" is not a valid double number. "
         + "Please check your dataset and ensure that only valid numbers of type double are included. ";
   protected static final String FOUND_NUMBERS = "Found numbers without item identifiers. "
         + "You can only compare items which are identified by an item identifier.";
   protected static final String INVALID_SYNTAX = "Invalid Syntax: \"%s\"";


   // The standard file names
   protected static final String CONTEXT_FILE = "context.data";
   protected static final String ITEM_FILE = "item.data";
   protected static final String RATINGS_FILE = "rating.data";

   // markers
   protected static final String CONTEXT_MARKER = "c";
   /** The Constant ITEM_MARKER. */
   public static final String ITEM_MARKER = "i";
   protected static final String FEATURE_MARKER = "f";
   protected static final String COMMENT_MARKER = "#";

   // These dividers are regular expressions
   protected static final String VECTOR_DIVIDER = "( |\t)+";
   protected static final String SPARSE_DIVIDER = ",";
   protected static final String ID_DIVIDER = ":";

   // These are normal strings
   protected static final String FEATURE_DECLARATION = "@feature";
   protected static final String CLOSE_ORDINGAL_RANGE = "]";
   protected static final String OPEN_ORDINAL_RANGE = "[";
   protected static final String FORMAT_DECLARATION = "@format";
   protected static final String FORMAT_DECLARATION_RELATIVE = "rel";


   protected static final int MAXIMUM_NUMBER_OF_EXCEPTIONS = 255;

   protected boolean isRelative = true;
   protected int itemsDeclared = 0;
   protected int contextsDeclared = 0;
   protected int ratingDeclared = 0;

   protected Map<Integer, String> contextFeatures = new HashMap<>();
   protected Map<Integer, String> itemFeatures = new HashMap<>();

   protected Map<Integer, List<Double>> allowedItemFeatureValues;
   protected Map<Integer, List<Double>> allowedContextFeatureValues;

   protected List<Double> allowedRatingValues = new ArrayList<>();


   /**
    * Prepare the dataset and feature maps.
    * 
    * @throws ParsingFailedException if the initialization fails
    */
   protected abstract void initParse() throws ParsingFailedException;


   /**
    * Pass final information to the dataset.
    * 
    * @throws ParsingFailedException if the finishing fails
    */
   protected abstract void finishParse() throws ParsingFailedException;


   @Override
   public IDataset<?, ?, ?> parse(DatasetFile file) throws ParsingFailedException {
      return parsePartialOf(file, Integer.MAX_VALUE);
   }


   @Override
   public IDataset<?, ?, ?> parsePartialOf(DatasetFile file, int amountOfInstances) throws ParsingFailedException {
      try {
         ZipFile zipFile = new ZipFile(file.getFile());

         checkIfDatasetIsRelative(zipFile);

         Pair<Map<Integer, String>, Map<Integer, List<Double>>> featuresAndAllowedValues = findFeatureDeclarationsAndAllowedValues(zipFile,
               CONTEXT_FILE);
         contextFeatures = featuresAndAllowedValues.getFirst();
         allowedContextFeatureValues = featuresAndAllowedValues.getSecond();

         featuresAndAllowedValues = findFeatureDeclarationsAndAllowedValues(zipFile, ITEM_FILE);
         itemFeatures = featuresAndAllowedValues.getFirst();
         allowedItemFeatureValues = featuresAndAllowedValues.getSecond();

         initParse();

         parseContextInformation(zipFile, amountOfInstances);
         parseItemInformation(zipFile);

         parsePartialOfRatingInformation(zipFile, amountOfInstances);

         finishParse();
         zipFile.close();

      } catch (IOException e) {
         throw new ParsingFailedException(COULD_NOT_OPEN_FILE + e.getMessage(), e);
      }

      IDataset<?, ?, ?> parsedDataset = getDataset();
      parsedDataset.setDatasetFile(file);
      return parsedDataset;
   }


   /**
    * Find all the feature declaration in the item and context files. This allows it to know the
    * 
    * @param zipFile the GPRF file containing the data
    * @throws IOException if opening the file failed
    * @throws ParsingFailedException if the parsing failed
    */
   private Pair<Map<Integer, String>, Map<Integer, List<Double>>> findFeatureDeclarationsAndAllowedValues(ZipFile zipFile, String entryName)
         throws IOException,
            ParsingFailedException {
      InputStream stream = zipFile.getInputStream(zipFile.getEntry(entryName));
      BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
      String line;

      Map<Integer, String> featureNames = new HashMap<>();
      Map<Integer, List<Double>> allowedFeatureValues = new HashMap<>();

      while ((line = readNonCommentLine(br)) != null && line.startsWith(FEATURE_DECLARATION)) {
         String[] splittedLine = line.split(ID_DIVIDER);

         int featureNumber = Integer.parseInt(splittedLine[0].split(VECTOR_DIVIDER)[1]);
         String featureName = splittedLine[1];

         if (line.contains(OPEN_ORDINAL_RANGE) && line.contains(CLOSE_ORDINGAL_RANGE)) {
            allowedFeatureValues.put(featureNumber, getRangeOfValues(line));
            featureName = splittedLine[1].substring(0, splittedLine[1].indexOf(OPEN_ORDINAL_RANGE));
         }
         featureNames.put(featureNumber, featureName.trim());
      }
      if (line != null && featureNames.isEmpty()) {
         String[] featureLine = line.split(ID_DIVIDER);
         if (featureLine.length > 1) {
            IVector firstLineVector = parseDenseVector(featureLine[1].trim().split(VECTOR_DIVIDER));
            for (int i = 0; i < firstLineVector.length(); i++) {
               featureNames.put(i, FEATURE + i);
            }
         }
      }
      br.close();
      stream.close();
      return Pair.of(featureNames, allowedFeatureValues);
   }


   /**
    * Checks if the given dataset is relative or not.
    * 
    * @param zipFile the zip file containing the context, item and rating information of the dataset
    * @throws IOException if the zip file does not contain the file for the rating information
    * @throws ParsingFailedException if no format declaration was found
    */
   private void checkIfDatasetIsRelative(ZipFile zipFile) throws IOException, ParsingFailedException {

      InputStream stream = zipFile.getInputStream(zipFile.getEntry(RATINGS_FILE));
      BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));

      String line = readNonCommentLine(reader);
      if (line != null && line.startsWith(FORMAT_DECLARATION)) {
         isRelative = line.contains(FORMAT_DECLARATION_RELATIVE);
      } else {
         reader.close();
         throw new ParsingFailedException(NO_FORMAT_DECLARATION_FOUND);
      }

      if (!isRelative && line.contains(OPEN_ORDINAL_RANGE) && line.contains(CLOSE_ORDINGAL_RANGE)) {
         allowedRatingValues = getRangeOfValues(line);
      }
      reader.close();
   }


   /**
    * Extracts the range of allowed values from the line.
    * 
    * @param line the input line
    * @return the found list of allowed values
    * @throws ParsingFailedException if the syntax of the range declaration is wrong
    */
   private List<Double> getRangeOfValues(String line) throws ParsingFailedException {
      List<Double> allowedValues = new ArrayList<>();
      if (line.indexOf(OPEN_ORDINAL_RANGE) > line.lastIndexOf(CLOSE_ORDINGAL_RANGE)) {
         throw new ParsingFailedException(String.format(INVALID_SYNTAX, line));
      }
      String[] ordinalList = line.substring(line.indexOf(OPEN_ORDINAL_RANGE) + 1, line.lastIndexOf(CLOSE_ORDINGAL_RANGE))
            .split(VECTOR_DIVIDER);
      for (int i = 0; i < ordinalList.length; i++) {
         try {
            allowedValues.add(Double.parseDouble(ordinalList[i]));
         } catch (NumberFormatException e) {
            throw new ParsingFailedException(String.format(NOT_A_NUMBER, ordinalList[i]), e);
         }

      }
      return allowedValues;
   }


   /**
    * Reads from a {@link BufferedReader} until it finds a line which contains something which is
    * not a comment.
    * 
    * @param reader the {@link BufferedReader} which reads the lines
    * @return the content of the first line which contains something besides comments and
    *         {@code null} if there is no such line
    * @throws IOException is thrown if the reader encounters an error
    */
   private String readNonCommentLine(BufferedReader reader) throws IOException {
      String line = StringUtils.EMPTY_STRING;
      while (line.isEmpty()) {
         line = reader.readLine();
         if (line == null)
            return null;
         else if (line.contains(COMMENT_MARKER)) {
            line = line.substring(0, line.indexOf(COMMENT_MARKER));
         }
         line = line.trim();
      }
      return line;
   }


   /**
    * Parses the context information of the dataset into valid vectors and puts it into the dataset.
    * 
    * @param zipFile the zip file containing the context information of the dataset
    * @param amountOfLines the number of contexts read
    * @throws IOException if the zip file does not contain the file for the context information
    * @throws ParsingFailedException if there is a syntax error in the file
    */
   private void parseContextInformation(ZipFile zipFile, int amountOfLines) throws IOException, ParsingFailedException {
      InputStream stream = zipFile.getInputStream(zipFile.getEntry(CONTEXT_FILE));
      BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
      contextsDeclared = 0;
      String line;
      while ((line = readNonCommentLine(br)) != null && contextsDeclared < amountOfLines) {
         if (line.startsWith(FEATURE_DECLARATION) && contextsDeclared != 0) {
            br.close();
            throw new ParsingFailedException(FEATURE_AFTER_VALUE);
         } else if (line.startsWith(FEATURE_DECLARATION)) {
            // Already parsed
         } else {
            contextsDeclared++;
            parseContextVectorLine(line);
         }

      }
      br.close();
   }


   /**
    * Parses the item information of the dataset into valid vectors and puts it into the dataset.
    * 
    * @param zipFile the zip file containing the item information of the dataset
    * @throws IOException if the zip file does not contain the file for the item information
    * @throws ParsingFailedException if there is a syntax error in the file
    */
   private void parseItemInformation(ZipFile zipFile) throws IOException, ParsingFailedException {

      InputStream stream = zipFile.getInputStream(zipFile.getEntry(ITEM_FILE));
      BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));

      itemsDeclared = 0;
      String line;
      while ((line = readNonCommentLine(br)) != null) {
         // read the item feature data
         if (line.startsWith(FEATURE_DECLARATION) && itemsDeclared != 0) {
            br.close();
            throw new ParsingFailedException(FEATURE_AFTER_VALUE);
         } else if (line.startsWith(FEATURE_DECLARATION)) {
            // Already parsed
         } else {

            itemsDeclared++;
            parseItemVectorLine(line);
         }

      }
      br.close();


   }


   /**
    * Parses the first <i>n</i> rating information lines of the dataset into valid values and puts
    * it into the dataset.
    * 
    * @param zipFile the zip file containing the rating information of the dataset
    * @param amountOfInstances the amount of instances to parse from the rating information
    * @throws IOException if the zip file does not contain the file for the rating information
    * @throws ParsingFailedException if there is a syntax error in the file
    */
   private void parsePartialOfRatingInformation(ZipFile zipFile, int amountOfInstances) throws IOException, ParsingFailedException {
      InputStream stream = zipFile.getInputStream(zipFile.getEntry(RATINGS_FILE));
      BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));

      int lineNumber = 0;
      String line;
      while (((line = readNonCommentLine(br)) != null) && (lineNumber < amountOfInstances)) {
         if (line.startsWith(FORMAT_DECLARATION)) {
            // Ignore empty lines and format declaration lines
         } else {
            lineNumber++;
            // Then read the item features itself
            parseRatingInformationLine(line);
         }
      }
      br.close();
   }


   /**
    * Transforms a line of feature information into an id and a vector.
    * 
    * @param line the {@link String}[] read from the file
    * @param dimension the dimension of the vector inside the {@link String}
    * @return the finished, parsed {@link IVector}
    * @throws ParsingFailedException if the parsing operation encounters invalid input
    */
   protected IVector parseInformationVector(String[] line, int dimension) throws ParsingFailedException {
      IVector featureVector;
      if (line[0].contains(FEATURE_MARKER)) {
         featureVector = parseSparseVector(line, dimension);
      } else {
         featureVector = parseDenseVector(line, dimension);
      }
      return featureVector;
   }


   /**
    * Parses a string with a dense feature vector.
    * 
    * @param splittedLine the {@link String}[] read from the file
    * @param dimension the dimension of the vector inside the {@link String}
    * @return the finished, parsed {@link IVector}
    * @throws ParsingFailedException if the parsing operation encounters invalid input
    */
   protected IVector parseDenseVector(String[] splittedLine, int dimension) throws ParsingFailedException {
      IVector result = new DenseDoubleVector(dimension);
      for (int featureNumber = 0; featureNumber < splittedLine.length; featureNumber++) {
         if (!splittedLine[featureNumber].isEmpty()) {
            try {
               double value = Double.parseDouble(splittedLine[featureNumber].trim());
               result.setValue(featureNumber, value);
            } catch (NumberFormatException nfe) {
               throw new ParsingFailedException(String.format(NOT_A_NUMBER, splittedLine[featureNumber]), nfe);
            } catch (ArrayIndexOutOfBoundsException e) {
               throw new ParsingFailedException(String.format(INDEX_DOES_NOT_EXIST, result.length(), featureNumber), e);
            }

         }
      }
      return result;
   }


   /**
    * Parses a split string containing a dense vector into an {@link IVector}.
    * 
    * @param splittedLine the line which contains the values
    * @return the parsed vector
    * @throws ParsingFailedException if the parsing encountered unecpexted syntax
    */
   protected IVector parseDenseVector(String[] splittedLine) throws ParsingFailedException {
      return parseDenseVector(splittedLine, splittedLine.length);
   }


   /**
    * Parses a string with a sparse feature vector.
    * 
    * @param splittedLine the {@link String}[] read from the file
    * @param dimension the dimension of the vector inside the {@link String}
    * @return the finished, parsed {@link IVector}
    * @throws ParsingFailedException if the parsing operation encounters invalid input
    */
   protected IVector parseSparseVector(String[] splittedLine, int dimension) throws ParsingFailedException {
      IVector result = new SparseDoubleVector(dimension);
      for (int pairNumber = 0; pairNumber < splittedLine.length; pairNumber++) {
         String[] pair = splittedLine[pairNumber].split(SPARSE_DIVIDER);
         int itemId = Integer.parseInt(pair[0].trim().substring(1));
         try {
            double value = Double.parseDouble(pair[1]);
            result.setValue(itemId, value);
         } catch (NumberFormatException nfe) {
            throw new ParsingFailedException(String.format(NOT_A_NUMBER, pair[1]), nfe);
         } catch (ArrayIndexOutOfBoundsException e) {
            throw new ParsingFailedException(String.format(NOT_A_PAIR, splittedLine[pairNumber]), e);
         }
      }
      return result;
   }


   /**
    * Parses a line with relative rating information into a valid {@link Ranking}s.
    * 
    * @param ranking the string containing the ranking
    * @return the parsed {@link Ranking}
    * @throws ParsingFailedException if the parsing operation failed on encountering invalid data
    */
   public static Ranking parseRelativeRanking(String ranking) throws ParsingFailedException {
      List<Integer> itemIds = new ArrayList<>();
      List<Integer> operators = new ArrayList<>();

      boolean isItem = true;
      String startedItem = StringUtils.EMPTY_STRING;
      for (int i = 0; i < ranking.length(); i++) {
         char currentChar = ranking.charAt(i);
         if (ITEM_MARKER.equals(currentChar + StringUtils.EMPTY_STRING)) {
            // New item found
            if (!startedItem.isEmpty()) {
               operators.add(Integer.parseInt(startedItem));
               startedItem = StringUtils.EMPTY_STRING;
            }
            isItem = true;

         } else if (Character.isDigit(currentChar)) {
            // Item ID found
            if (!isItem) {
               throw new ParsingFailedException(FOUND_NUMBERS);
            }
            startedItem += Character.toString(currentChar);
         } else {
            // Operator found
            if (isItem && !startedItem.isEmpty()) {
               itemIds.add(Integer.parseInt(startedItem));
               startedItem = StringUtils.EMPTY_STRING;
            }
            isItem = false;
            startedItem += Ranking.encodeOperator(currentChar);
         }
      }
      // Save final id or operator
      if (!startedItem.isEmpty()) {
         if (isItem) {
            itemIds.add(Integer.parseInt(startedItem));
         } else {
            operators.add(Integer.parseInt(startedItem));
         }
      }
      return new Ranking(CollectionsUtils.convertIntegerListToArray(itemIds), CollectionsUtils.convertIntegerListToArray(operators));
   }


   /**
    * Parses a line with absolute rating information into a valid {@link IVector}.
    * 
    * @param ratingVector the parsed rating vector
    * @return the parsed {@link IVector}
    * @throws ParsingFailedException if the parsing failed because of invalid data or invalid syntax
    */
   protected IVector parseAbsoluteRatingVector(String ratingVector) throws ParsingFailedException {
      IVector ratings;
      if (ratingVector.contains(SPARSE_DIVIDER)) {
         // Sparse Case
         ratings = parseSparseVector(ratingVector.trim().split(VECTOR_DIVIDER), itemsDeclared);
      } else {
         // Dense Case
         ratings = parseDenseVector(ratingVector.trim().split(VECTOR_DIVIDER), itemsDeclared);
      }
      return ratings;
   }


   /**
    * Parses one line of the information in the item information file and enters it into the
    * dataset.
    * 
    * @param itemInformationLine A single line from the {@code item.data} file
    * @throws ParsingFailedException if the parser encountered a problem while parsing the file
    */
   protected abstract void parseItemVectorLine(String itemInformationLine) throws ParsingFailedException;


   /**
    * Parses one line of the information in the context information file and enters it into the
    * dataset.
    *
    * @param contextInformationLine a single line from the {@code context.data} file
    * @throws ParsingFailedException if the parser encountered a problem while parsing the file
    */
   protected abstract void parseContextVectorLine(String contextInformationLine) throws ParsingFailedException;


   /**
    * Parses one line of the information in the rating information file and enters it into the
    * dataset.
    *
    * @param ratingInformationLine a single line from the {@code rating.data} file
    * @throws ParsingFailedException if the parser encountered a problem while parsing the file
    */
   protected abstract void parseRatingInformationLine(String ratingInformationLine) throws ParsingFailedException;


}
