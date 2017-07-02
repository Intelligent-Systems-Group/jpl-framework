package de.upb.cs.is.jpl.api.dataset.ordinalclassification;


import java.util.Collections;

import de.upb.cs.is.jpl.api.dataset.ADatasetParser;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.exception.dataset.NotAllowedValueException;
import de.upb.cs.is.jpl.api.exception.dataset.ParsingFailedException;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;


/**
 * This parser is used to parse a dataset for ordinal classification.
 * 
 * @author Tanja Tornede
 *
 */
public class OrdinalClassificationDatasetParser extends ADatasetParser {

   private static final String ERROR_GIVEN_DATASET_NOT_ABSOLUTE = "The given dataset is not an absolute one.";

   private static final String ERROR_GIVEN_LINE_DOES_NOT_CONTAIN_VALID_ID = "The given line does not contain a valid id:\n%s";

   private static final String ERROR_GIVEN_LINE_CONTAINS_INVALID_CONTEXT_FEATURE_VALUE = "The given line contains an invalid context feature value:\n%s\n%s";
   private static final String ERROR_GIVEN_LINE_DOES_NOT_HAVE_CORRECT_AMOUNT_OF_CONTEXT_FEATURES = "The given line does not have the correct amount %s of context features:\n%s";
   private static final String ERROR_GIVEN_LINE_DOES_NOT_CONTAIN_VALID_CONTEXT_FEATURES = "The given line does not contain valid context features:\n%s";

   private static final String ERROR_GIVEN_LINE_DOES_NOT_CONTAIN_VALID_RATING = "The given line does not contain valid rating:\n%s";
   private static final String ERROR_GIVEN_LINE_DOES_EXACT_ONE_RATING = "The given line does not have exactly one rating:\n%s";

   private static final String ERROR_INVALID_VALUE_FOR_CONTEXT_FEATURE = "Invalid value <%s> for context feature.";
   private static final String ERROR_INVALID_VALUE_FOR_RATING = "Invalid value <%s> for rating.";

   private static final String ERROR_UNEQUAL_AMOUNT_CONTEXT_FEATURE_VECTORS_RATINGS = "The amount of context feature vectors and ratings does not coincide.";

   private OrdinalClassificationDataset dataset;


   @Override
   public IDataset<?, ?, ?> getDataset() {
      if (dataset == null) {
         return new OrdinalClassificationDataset(Collections.<Double> emptyList(), 0);
      }
      return dataset;
   }


   @Override
   protected void initParse() throws ParsingFailedException {
      if (isRelative) {
         throw new ParsingFailedException(ERROR_GIVEN_DATASET_NOT_ABSOLUTE);
      }
      if (allowedRatingValues.isEmpty()) {
         dataset = new OrdinalClassificationDataset(contextFeatures.size());
      } else {
         dataset = new OrdinalClassificationDataset(allowedRatingValues, contextFeatures.size());
      }
   }


   @Override
   protected void parseItemVectorLine(String itemInformationLine) throws ParsingFailedException {
      // not used
   }


   @Override
   protected void parseContextVectorLine(String contextInformationLine) throws ParsingFailedException {
      String[] splittedContextInformationLine = contextInformationLine.split(ID_DIVIDER);

      Integer id = getIdOf(splittedContextInformationLine[0]);
      double[] contextFeatureVector;
      try {
         contextFeatureVector = getContextFeatureVectorOf(splittedContextInformationLine[1]);
      } catch (NotAllowedValueException e) {
         throw new ParsingFailedException(
               String.format(ERROR_GIVEN_LINE_CONTAINS_INVALID_CONTEXT_FEATURE_VALUE, contextInformationLine, e));
      }
      dataset.setFeatureVectorForInstance(id.intValue(), contextFeatureVector);
   }


   /**
    * Returns the id of the given part of a context information line.
    * 
    * @param idPartOfContextInformationLine the part of a line to get the id from
    * 
    * @return the id of the given part of a line
    * 
    * @throws ParsingFailedException if parsing the id to an {@link Integer} is not possible
    */
   private Integer getIdOf(String idPartOfContextInformationLine) throws ParsingFailedException {
      int markerPosition = idPartOfContextInformationLine.indexOf(CONTEXT_MARKER);

      try {
         return Integer.parseInt(idPartOfContextInformationLine.substring(markerPosition + 1));
      } catch (NumberFormatException ex) {
         throw new ParsingFailedException(String.format(ERROR_GIVEN_LINE_DOES_NOT_CONTAIN_VALID_ID, idPartOfContextInformationLine));
      }
   }


   /**
    * Returns the context feature vector of the given part of a context information line.
    * 
    * @param contextFeatureVectorPartOfContextInformationLine the part of a line to get the context
    *           feature vector from.
    * 
    * @return the context feature vector of the given part of a line
    * 
    * @throws ParsingFailedException if the size of the context feature vector of the given line is
    *            incorrect, or if the context feature values cannot be casted to double values
    * @throws NotAllowedValueException if the context feature vector contains a value that is not
    *            defined before
    */
   private double[] getContextFeatureVectorOf(String contextFeatureVectorPartOfContextInformationLine)
         throws ParsingFailedException,
            NotAllowedValueException {
      String[] contextFeatureVectorAsString = contextFeatureVectorPartOfContextInformationLine.trim().split(VECTOR_DIVIDER);
      if (contextFeatureVectorAsString.length != contextFeatures.size()) {
         throw new ParsingFailedException(String.format(ERROR_GIVEN_LINE_DOES_NOT_HAVE_CORRECT_AMOUNT_OF_CONTEXT_FEATURES,
               contextFeatures.size(), contextFeatureVectorPartOfContextInformationLine));
      }

      double[] contextFeatureVector;
      try {
         contextFeatureVector = CollectionsUtils.convertStringArrayToDoubleArray(contextFeatureVectorAsString);
      } catch (NumberFormatException ex) {
         throw new ParsingFailedException(
               String.format(ERROR_GIVEN_LINE_DOES_NOT_CONTAIN_VALID_CONTEXT_FEATURES, contextFeatureVectorPartOfContextInformationLine));
      }

      checkIfContextFeatureValuesAreAllowed(contextFeatureVector);
      return contextFeatureVector;
   }


   /**
    * Checks if the context feature vector contains only allowed values if some were defined.
    * 
    * @param contextFeatureVector the context vector to check
    * 
    * @throws NotAllowedValueException if a not allowed value was detached
    */
   private void checkIfContextFeatureValuesAreAllowed(double[] contextFeatureVector) throws NotAllowedValueException {
      for (int i = 0; i < contextFeatureVector.length; i++) {
         if (allowedContextFeatureValues.containsKey(i) && !allowedContextFeatureValues.get(i).contains(contextFeatureVector[i])) {
            throw new NotAllowedValueException(String.format(ERROR_INVALID_VALUE_FOR_CONTEXT_FEATURE, contextFeatureVector[i]));
         }
      }
   }


   @Override
   protected void parseRatingInformationLine(String ratingInformationLine) throws ParsingFailedException {
      String[] splittedRatingInformationLine = ratingInformationLine.split(ID_DIVIDER);

      Integer id = getIdOf(splittedRatingInformationLine[0]);
      double rating;
      try {
         rating = getRatingOf(splittedRatingInformationLine[1]);
      } catch (NotAllowedValueException e) {
         throw new ParsingFailedException(String.format(ERROR_GIVEN_LINE_CONTAINS_INVALID_CONTEXT_FEATURE_VALUE, ratingInformationLine, e));
      }
      dataset.setRatingForInstance(id.intValue(), rating);
   }


   /**
    * Returns the rating of the given part of a rating information line.
    * 
    * @param ratingPartOfRatingInformationLine the part of a line to get the rating from.
    * 
    * @return the rating of the given part of a line
    * 
    * @throws ParsingFailedException if there is not exactly one rating defined, or if the context
    *            feature values cannot be casted to double values
    * @throws NotAllowedValueException if the rating contains a value that is not defined before
    */
   private double getRatingOf(String ratingPartOfRatingInformationLine) throws ParsingFailedException, NotAllowedValueException {
      String[] ratingAsString = ratingPartOfRatingInformationLine.trim().split(VECTOR_DIVIDER);
      if (ratingAsString.length != 1) {
         throw new ParsingFailedException(String.format(ERROR_GIVEN_LINE_DOES_EXACT_ONE_RATING, ratingPartOfRatingInformationLine));
      }

      double rating;
      try {
         rating = Double.parseDouble(ratingAsString[0]);
      } catch (NumberFormatException ex) {
         throw new ParsingFailedException(String.format(ERROR_GIVEN_LINE_DOES_NOT_CONTAIN_VALID_RATING, ratingPartOfRatingInformationLine));
      }

      checkIfRatingIsAllowed(rating);
      return rating;
   }


   /**
    * Checks if the rating has an allowed value if some values were defined.
    * 
    * @param rating the rating to check
    * 
    * @throws NotAllowedValueException if a not allowed value was detached
    */
   private void checkIfRatingIsAllowed(double rating) throws NotAllowedValueException {
      if (!allowedRatingValues.isEmpty() && !allowedRatingValues.contains(rating)) {
         throw new NotAllowedValueException(String.format(ERROR_INVALID_VALUE_FOR_RATING, rating));
      }
   }


   @Override
   protected void finishParse() throws ParsingFailedException {
      if (dataset.getRatings().size() != dataset.getFeatureVectors().size()) {
         throw new ParsingFailedException(ERROR_UNEQUAL_AMOUNT_CONTEXT_FEATURE_VECTORS_RATINGS);
      }
   }

}
