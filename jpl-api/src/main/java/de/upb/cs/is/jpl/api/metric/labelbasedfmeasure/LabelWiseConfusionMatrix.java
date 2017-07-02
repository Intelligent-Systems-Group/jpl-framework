package de.upb.cs.is.jpl.api.metric.labelbasedfmeasure;


import java.util.List;

import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;


/**
 * This class is a labelwise confusion matrix for multilabel classification. It can store the number
 * of false positives, true positives, false negatives and true negatives predictions for each label
 * separately. Additionally it can compute these values, given a list of expected and predicted
 * label vectors.
 * 
 * @author Alexander Hetzer
 *
 */
public class LabelWiseConfusionMatrix {

   protected static final String ERROR_UNEQUAL_VECTOR_SIZES = "The expected rating and the predicted rating should have the same length.";

   private int[] falsePositives;
   private int[] truePositives;
   private int[] falseNegatives;
   private int[] trueNegatives;


   /**
    * Creates a new {@link LabelWiseConfusionMatrix} for the given number of labels.
    * 
    * @param numberOfLabels the number of labels to use for creating the confusion matrix
    */
   public LabelWiseConfusionMatrix(int numberOfLabels) {
      falsePositives = new int[numberOfLabels];
      truePositives = new int[numberOfLabels];
      falseNegatives = new int[numberOfLabels];
      trueNegatives = new int[numberOfLabels];
   }


   /**
    * Fills the confusion matrix with the correct confusion values, which are computed based on the
    * given list of expected and predicted label vectors.
    * 
    * @param expectedLabelVectors the list of expected label vectors
    * @param predictedLabelVectors the list of predicted label vectors
    * 
    * @throws LossException if a pair of vectors of unequal sizes is found
    */
   public void fill(List<IVector> expectedLabelVectors, List<IVector> predictedLabelVectors) throws LossException {
      for (int i = 0; i < expectedLabelVectors.size(); i++) {
         IVector expectedRating = expectedLabelVectors.get(i);
         IVector predictedRating = predictedLabelVectors.get(i);
         assertEqualVectorLength(expectedRating, predictedRating);
         updateConfusionValuesForVectorPair(expectedRating, predictedRating);
      }
   }


   /**
    * Updates the locally stored confusion values based on the given expected label vector and the
    * predicted one.
    * 
    * @param expectedLabelVector the expected label vector
    * @param predictedLabelVector the predicted label vector
    */
   private void updateConfusionValuesForVectorPair(IVector expectedLabelVector, IVector predictedLabelVector) {
      for (int j = 0; j < expectedLabelVector.length(); j++) {
         if (Double.compare(expectedLabelVector.getValue(j), predictedLabelVector.getValue(j)) != 0) {
            if (Double.compare(expectedLabelVector.getValue(j), 1.0) == 0) {
               falseNegatives[j]++;
            } else {
               falsePositives[j]++;
            }
         } else {
            if (Double.compare(expectedLabelVector.getValue(j), 1.0) == 0) {
               truePositives[j]++;
            } else {
               trueNegatives[j]++;
            }
         }
      }
   }


   /**
    * Checks if the given two vectors have the same length. If this is not the case a
    * {@link LossException} is thrown.
    * 
    * @param firstVector the first vector to check
    * @param secondVector the second vector to check
    * 
    * @throws LossException if the given vectors have an unequal length
    */
   protected void assertEqualVectorLength(IVector firstVector, IVector secondVector) throws LossException {
      if (firstVector.length() != secondVector.length()) {
         throw new LossException(ERROR_UNEQUAL_VECTOR_SIZES);
      }
   }


   /**
    * Returns the number of false positives of the label with the given id.
    * 
    * @param labelId the id of the label interested in
    * 
    * @return the number of false positives of the label with the given id
    */
   public int getFalsePositives(int labelId) {
      return falsePositives[labelId];
   }


   /**
    * Returns the number of true positives of the label with the given id.
    * 
    * @param labelId the id of the label interested in
    * 
    * @return the number of true positives of the label with the given id
    */
   public int getTruePositives(int labelId) {
      return truePositives[labelId];
   }


   /**
    * Returns the number of false negatives of the label with the given id.
    * 
    * @param labelId the id of the label interested in
    * 
    * @return the number of false negatives of the label with the given id
    */
   public int getFalseNegatives(int labelId) {
      return falseNegatives[labelId];
   }


   /**
    * Returns the number of true negatives of the label with the given id.
    * 
    * @param labelId the id of the label interested in
    * 
    * @return the number of true negatives of the label with the given id
    */
   public int getTrueNegatives(int labelId) {
      return trueNegatives[labelId];
   }


}
