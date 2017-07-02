package de.upb.cs.is.jpl.api.dataset.labelranking;


import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.dataset.ADatasetParser;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.exception.dataset.ParsingFailedException;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;


/**
 * This class parses an GPRF dataset of the correct form for the label ranking problem and
 * transforms it into the {@link LabelRankingDataset}.
 * 
 * @author Andreas Kornelsen
 *
 */
public class LabelRankingDatasetParser extends ADatasetParser {

   private static final String ERROR_LINE_CONTAINS_NO_CONTENT = "Line contains no content, line: %s.";
   private static final String ERROR_PARSE_FEATURE_LINE = "Error by parsing double values of feature linem line %s.";
   private static final String ERROR_FEATURE_LINE_SPLIT = "The provided feature line couldn't be split, featureLine: %s.";
   private static final String ERROR_FEATURE_VALUE_COULDNT_BE_PARSED = "A feature value couldn't be parsed, featureValue: %s.";
   private static final String ERROR_UNEQUAL_SIZE_OF_RANKINGS_AND_FEATURES = "The size of the rankings and the features is not the same, rankingsSize: %d, featuresSize: %d.";
   private static final String ERROR_NO_ID_DIVIDER_IN_STRING = "No id divider found in the line, line: %s.";
   private static final String ERROR_INTEGER_PARSE = "String couldn't be parsed to Integer, integerString: %s.";

   private List<Integer> labels;
   private List<double[]> features;
   private List<Ranking> rankings;
   private LabelRankingDataset labelRankingDataset;


   @Override
   public IDataset<?, ?, ?> getDataset() {
      if (labelRankingDataset == null) {
         return new LabelRankingDataset();
      }
      return labelRankingDataset;
   }


   @Override
   protected void initParse() throws ParsingFailedException {
      this.labels = new ArrayList<>();
      this.features = new ArrayList<>();
      this.rankings = new ArrayList<>();
   }


   @Override
   protected void finishParse() throws ParsingFailedException {
      int featuresSize = features.size();
      int rankingsSize = rankings.size();

      if (featuresSize != rankingsSize) {
         throw new ParsingFailedException(String.format(ERROR_UNEQUAL_SIZE_OF_RANKINGS_AND_FEATURES, rankingsSize, featuresSize));
      }

      labelRankingDataset = new LabelRankingDataset(labels, features, rankings);
   }


   @Override
   protected void parseItemVectorLine(String itemInformationLine) throws ParsingFailedException {
      String itemLineContent = getLineContent(itemInformationLine);
      int label = getIntegerValue(itemLineContent);
      labels.add(label);
   }


   @Override
   protected void parseContextVectorLine(String contextInformationLine) throws ParsingFailedException {
      String contextInformationLineContent = getLineContent(contextInformationLine);
      double[] featureValues = getFeatureValuesOfLine(contextInformationLineContent);
      features.add(featureValues);
   }


   @Override
   protected void parseRatingInformationLine(String ratingInformationLine) throws ParsingFailedException {
      String lineContent = getLineContent(ratingInformationLine);
      Ranking ranking = parseRelativeRanking(lineContent);
      rankings.add(ranking);
   }


   /**
    * Returns the parsed integer value for a given string.
    *
    * @param integerString a string which contains an integer value
    * @return the parsed integer value of the given string
    * @throws ParsingFailedException if an error occurs during the parse operation
    */
   private int getIntegerValue(String integerString) throws ParsingFailedException {
      int firstFeatureValueOfLine = -1;
      try {
         firstFeatureValueOfLine = Integer.parseInt(integerString);
      } catch (NumberFormatException e) {
         throw new ParsingFailedException(String.format(ERROR_INTEGER_PARSE, integerString), e);
      }

      if (firstFeatureValueOfLine == -1) {
         throw new ParsingFailedException(String.format(ERROR_INTEGER_PARSE, integerString));
      }

      return firstFeatureValueOfLine;
   }


   /**
    * Returns the line content without the line index and the id divider.
    *
    * @param indexedLine a line in the dataset with an index in the beginning and an id divider
    * @return the line without the index and the id divider in the beginning
    * @throws ParsingFailedException if the line doesn't contain an id divider
    */
   private String getLineContent(String indexedLine) throws ParsingFailedException {
      int indexOfIdDivider = indexedLine.indexOf(ID_DIVIDER);
      if (indexOfIdDivider == -1) {
         throw new ParsingFailedException(String.format(ERROR_NO_ID_DIVIDER_IN_STRING, indexedLine));
      }

      return indexedLine.substring(indexOfIdDivider + 1).trim();
   }


   /**
    * Returns the first feature value of the line without the index.
    *
    * @param featureLine a line with feature values
    * @return the feature values of the line
    * @throws ParsingFailedException if the line is empty or an error occurs during the parsing of
    *            doubles
    */
   private double[] getFeatureValuesOfLine(String featureLine) throws ParsingFailedException {
      if (featureLine.isEmpty()) {
         throw new ParsingFailedException(String.format(ERROR_LINE_CONTAINS_NO_CONTENT, featureLine));
      }
      double[] firstFeatureValueOfLine = null;
      try {
         firstFeatureValueOfLine = parseFeatureValues(featureLine);
      } catch (NumberFormatException e) {
         throw new ParsingFailedException(String.format(ERROR_PARSE_FEATURE_LINE, featureLine));
      }

      return firstFeatureValueOfLine;
   }


   /**
    * Parses the provided features values out of the string and returns the feature values in an
    * array.
    * 
    * @param featureLineString the string which contains the double feature values
    * @return the parsed feature values
    * @throws ParsingFailedException if the parsing of the double values fails or the string is
    *            empty
    */
   private double[] parseFeatureValues(String featureLineString) throws ParsingFailedException {
      String[] split = featureLineString.split(VECTOR_DIVIDER);

      if (split == null || split.length == 0) {
         throw new ParsingFailedException(String.format(ERROR_FEATURE_LINE_SPLIT, featureLineString));
      }

      List<Double> featureValuesList = new ArrayList<>();

      for (String featureValueString : split) {
         try {
            double featureValue = Double.parseDouble(featureValueString);
            featureValuesList.add(featureValue);
         } catch (NumberFormatException | NullPointerException e) {
            throw new ParsingFailedException(String.format(ERROR_FEATURE_VALUE_COULDNT_BE_PARSED, featureValueString), e);
         }
      }

      return CollectionsUtils.convertDoubleListToArray(featureValuesList);
   }
}
