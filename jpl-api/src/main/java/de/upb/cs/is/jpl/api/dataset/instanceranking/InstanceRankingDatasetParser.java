package de.upb.cs.is.jpl.api.dataset.instanceranking;


import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.dataset.ADatasetParser;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.exception.dataset.ParsingFailedException;
import de.upb.cs.is.jpl.api.util.datastructure.NullType;


/**
 * A dataset parser for instance ranking problems. The parser can only parse and handle absolute
 * features and ratings.
 * 
 * @author Sebastian Gottschalk
 */
public class InstanceRankingDatasetParser extends ADatasetParser {
   private static final String PROBLEM_BY_PARSING_LINE = "Problem by parsing line %s.";
   private static final String ERROR_IN_CONTEXT_LINE_DECOMPOSITION = PROBLEM_BY_PARSING_LINE
         + " The line could not be decomposed into id and features.";
   private static final String ERROR_IN_RATING_LINE_DECOMPOSITION = PROBLEM_BY_PARSING_LINE
         + " The line could not be decomposed into id and rating.";
   private static final String ERROR_IN_CONTEXT_LINE_FEATURE_NUMBERS = PROBLEM_BY_PARSING_LINE
         + " The line has not the equal number of features than the line before.";
   private static final String ERROR_IN_CONTEXT_LINE_PARSING_FEATURES = PROBLEM_BY_PARSING_LINE
         + " The features should be only double values.";
   private static final String ERROR_IN_RATING_LINE_PARSING_FEATURES = PROBLEM_BY_PARSING_LINE + " The rating should be a double value.";
   private static final String ERROR_FEATURE_AND_RATING_LIST_WITH_DIFFERENT_LENGTH = "The context feature list and the rating list should have the same length.";

   private List<double[]> contextFeatureList;
   private List<Integer> ratingList;
   private int numberOfFeatures = -1;


   @Override
   public void parseContextVectorLine(String contextInformationLine) throws ParsingFailedException {
      // Split line into [c*]:[*] and use second part
      String[] splittedLine = contextInformationLine.trim().split(ID_DIVIDER, 2);
      try {
         // Split line into single features
         String[] features = splittedLine[1].trim().split(VECTOR_DIVIDER);

         // Find different feature vector length
         if (numberOfFeatures == -1) {
            // Save features of first line
            numberOfFeatures = features.length;
         }

         if (numberOfFeatures != features.length) {
            throw new ParsingFailedException(String.format(ERROR_IN_CONTEXT_LINE_FEATURE_NUMBERS, this.contextsDeclared));
         }

         // Add features
         contextFeatureList.add(createInstanceFeatureArray(features));

      } catch (NumberFormatException e) {
         throw new ParsingFailedException(String.format(ERROR_IN_CONTEXT_LINE_PARSING_FEATURES, this.contextsDeclared), e);
      } catch (IndexOutOfBoundsException e) {
         throw new ParsingFailedException(String.format(ERROR_IN_CONTEXT_LINE_DECOMPOSITION, this.contextsDeclared), e);
      }
   }


   /**
    * Create a context feature array out of feature string array which separated features via comma.
    * This function will also remove unused features.
    * 
    * @param features string array with the context features
    * @return a double array with the context features
    */
   private double[] createInstanceFeatureArray(String[] features) {
      double[] instanceList = new double[numberOfFeatures];

      for (int i = 0; i < numberOfFeatures; i++) {
         // Check if only a few features should be selected
         if (!this.itemFeatures.isEmpty()) {
            if (this.itemFeatures.containsKey(i + 1)) {
               instanceList[i] = Double.parseDouble(features[i]);
            }
         } else {
            instanceList[i] = Double.parseDouble(features[i]);
         }
      }
      return instanceList;
   }


   @Override
   public void parseRatingInformationLine(String ratingInformationLine) throws ParsingFailedException {
      // Split line into [c*]:[*] and use second part
      String[] splittedLine = ratingInformationLine.split(ID_DIVIDER, 2);
      try {
         Integer rating = Integer.parseInt(splittedLine[1].trim());
         ratingList.add(rating);
      } catch (NumberFormatException e) {
         throw new ParsingFailedException(String.format(ERROR_IN_RATING_LINE_PARSING_FEATURES, this.contextsDeclared), e);
      } catch (IndexOutOfBoundsException e) {
         throw new ParsingFailedException(String.format(ERROR_IN_RATING_LINE_DECOMPOSITION, this.contextsDeclared), e);
      }
   }


   @Override
   protected void initParse() {
      contextFeatureList = new ArrayList<>();
      ratingList = new ArrayList<>();
      numberOfFeatures = -1;
   }


   @Override
   public IDataset<double[], NullType, Integer> getDataset() {
      InstanceRankingDataset dataset = new InstanceRankingDataset();
      if (!isDatasetParsed()) {
         return dataset;
      }

      dataset.fillDataset(contextFeatureList, ratingList);
      return dataset;
   }


   /**
    * Checks if the dataset is parsed and the rating list and context feature list is not empty.
    *
    * @return true, if the dataset is parsed and not empty
    */
   private boolean isDatasetParsed() {
      if (contextFeatureList == null || ratingList == null || contextFeatureList.isEmpty() || ratingList.isEmpty()) {
         return false;
      }

      if (contextFeatureList.size() != ratingList.size()) {
         return false;
      }
      return true;
   }


   @Override
   protected void finishParse() throws ParsingFailedException {
      if (this.contextFeatureList.size() != this.ratingList.size()) {
         throw new ParsingFailedException(ERROR_FEATURE_AND_RATING_LIST_WITH_DIFFERENT_LENGTH);
      }
   }


   @Override
   protected void parseItemVectorLine(String itemInformationLine) throws ParsingFailedException {
      // Function not needed
   }

}