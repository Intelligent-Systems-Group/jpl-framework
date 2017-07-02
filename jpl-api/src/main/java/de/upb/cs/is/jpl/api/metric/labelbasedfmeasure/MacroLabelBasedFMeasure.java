package de.upb.cs.is.jpl.api.metric.labelbasedfmeasure;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.exception.evaluation.LossException;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.metric.ANonDecomposableIVectorDoubleMetric;
import de.upb.cs.is.jpl.api.metric.EMetric;
import de.upb.cs.is.jpl.api.metric.examplebasedfmeasure.FMeasureConfiguration;


/**
 * This class is the implementation of the macro label based f-measure (as presented in the paper "A
 * Review on Multi-Label Learning Algorithms" by Min-Ling Zhang and Zhi-Hua Zhou), which is computed
 * based one the values of the {@link LabelWiseConfusionMatrix}. It is parameterized with a beta
 * parameter.
 * 
 * @author Alexander Hetzer
 *
 */
public class MacroLabelBasedFMeasure extends ANonDecomposableIVectorDoubleMetric<FMeasureConfiguration> {

   private static final Logger logger = LoggerFactory.getLogger(MacroLabelBasedFMeasure.class);

   private static final String ERROR_CANNOT_COMPUTE_FMEASURE_FOR_LABEL = "Cannot compute f-measure for label: %d.";
   private static final String ERROR_CANNOT_COMPUTE_F_MEASURE_FOR_THIS_DATASET = "Cannot compute f-measure for this dataset as of 100% true negatives.";
   private static final String ERROR_CANNOT_COMPUTE_F_MEASURE_FOR_LABEL = "Cannot compute f-measure for label %d.";

   private LabelWiseConfusionMatrix confusionMatrix;

   private double beta = 1.0;


   /**
    * Creates a new {@link MacroLabelBasedFMeasure}.
    */
   public MacroLabelBasedFMeasure() {
      super(EMetric.MACRO_LABEL_BASED_F_MEASURE.getMetricIdentifier());
      beta = getMetricConfiguration().getBeta();

   }


   @Override
   public Double getAggregatedLossForRatings(List<IVector> expectedRatings, List<IVector> predictedRatings) throws LossException {
      assertRatingListsHaveSameSize(expectedRatings, predictedRatings);
      confusionMatrix = new LabelWiseConfusionMatrix(expectedRatings.get(0).length());
      confusionMatrix.fill(expectedRatings, predictedRatings);

      double macroFMeasure = 0;
      int numberOfComputableFMeasureLabels = 0;
      for (int i = 0; i < expectedRatings.get(0).length(); i++) {
         try {
            macroFMeasure += computeLabelBasedFMeasureForLabel(i);
            numberOfComputableFMeasureLabels++;
         } catch (LossException exception) {
            logger.error(String.format(ERROR_CANNOT_COMPUTE_FMEASURE_FOR_LABEL, i), exception);
         }
      }
      if (numberOfComputableFMeasureLabels == 0) {
         throw new LossException(ERROR_CANNOT_COMPUTE_F_MEASURE_FOR_THIS_DATASET);
      }
      return macroFMeasure / numberOfComputableFMeasureLabels;

   }


   /**
    * Computes the label based f-measure for the label with the given id.
    * 
    * @param labelId the id of the label to compute the measure for
    * 
    * @return the label based f-measure for the label with the given id
    * 
    * @throws LossException if the f-measure cannot be computed, as all predictions are true
    *            negatives
    */
   private double computeLabelBasedFMeasureForLabel(int labelId) throws LossException {
      double numerator = (1 + Math.pow(beta, 2)) * confusionMatrix.getTruePositives(labelId);
      double denominator = (1 + Math.pow(beta, 2)) * confusionMatrix.getTruePositives(labelId)
            + Math.pow(beta, 2) * confusionMatrix.getFalseNegatives(labelId) + confusionMatrix.getFalsePositives(labelId);
      if (denominator <= 0) {
         throw new LossException(String.format(ERROR_CANNOT_COMPUTE_F_MEASURE_FOR_LABEL, labelId));
      }
      return numerator / denominator;
   }


   @Override
   protected FMeasureConfiguration createDefaultMetricConfiguration() {
      return new FMeasureConfiguration();
   }
}
