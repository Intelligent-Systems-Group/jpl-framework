package de.upb.cs.is.jpl.api.algorithm.baselearner.classification.logistic;


import de.upb.cs.is.jpl.api.algorithm.baselearner.ABaseLearningModel;
import de.upb.cs.is.jpl.api.algorithm.baselearner.regression.logistic.LogisticRegressionLearningModel;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.exception.UnsupportedOperationException;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;


/**
 * Learning model for the Logistic Classification implementation.
 * 
 * @author Tanja Tornede
 *
 */
public class LogisticClassificationLearningModel extends ABaseLearningModel<Double> {

   private ABaseLearningModel<Double> baselearningModel;
   private double threshold;


   /**
    * Creates a new {@link LogisticClassificationLearningModel} with the given threshold and the
    * given {@link LogisticRegressionLearningModel}.
    * 
    * @param threshold the threshold to predict the positive case
    * @param logisticRegressionLearningModel the logistic regression learning model to use during
    *           the prediction
    * @param numberOfFeaturesTrainedOn the number of trained features of this model
    */
   public LogisticClassificationLearningModel(double threshold, ABaseLearningModel<Double> logisticRegressionLearningModel,
         int numberOfFeaturesTrainedOn) {
      super(numberOfFeaturesTrainedOn);
      this.threshold = threshold;
      this.baselearningModel = logisticRegressionLearningModel;
   }


   @Override
   public Double predict(IInstance<?, ?, ?> instance) throws PredictionFailedException {
      checkInstanceForCompatibility(instance);
      Double prediction = baselearningModel.predict(instance);
      return greaterThreshold(prediction);
   }


   /**
    * Returns {@code 1} if the given value is greater than the threshold.
    * 
    * @param value the value to check
    * @return {@code 1} if the value is greater than or equal to the threshold and {@code -1} if the
    *         value is smaller
    */
   private double greaterThreshold(Double value) {
      return value.doubleValue() >= threshold ? 1 : -1;
   }


   @Override
   public double getBias() throws UnsupportedOperationException {
      return baselearningModel.getBias();
   }


   @Override
   public IVector getWeightVector() throws UnsupportedOperationException {
      return baselearningModel.getWeightVector();
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (getClass() != obj.getClass())
         return false;
      LogisticClassificationLearningModel other = (LogisticClassificationLearningModel) obj;
      if (baselearningModel == null) {
         if (other.baselearningModel != null)
            return false;
      } else if (!baselearningModel.equals(other.baselearningModel))
         return false;
      if (Double.doubleToLongBits(threshold) != Double.doubleToLongBits(other.threshold))
         return false;
      return true;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((baselearningModel == null) ? 0 : baselearningModel.hashCode());
      long temp;
      temp = Double.doubleToLongBits(threshold);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      return result;
   }

}
