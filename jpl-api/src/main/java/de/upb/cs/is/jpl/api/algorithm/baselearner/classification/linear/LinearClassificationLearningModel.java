package de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.algorithm.baselearner.ABaseLearningModel;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerInstance;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.math.linearalgebra.DenseDoubleVector;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;


/**
 * This learning model is used for the {@link ALinearClassification} implementations.
 * 
 * @author Tanja Tornede
 *
 */
public class LinearClassificationLearningModel extends ABaseLearningModel<Double> {

   private static final Logger logger = LoggerFactory.getLogger(ALinearClassification.class);

   private static final String ERROR_SOMETHING_UNFORESEEABLE_HAPPENED = "Something unforeseeable happened during prediction, this message should not occure due to construction!";

   private double bias;
   private IVector weightVector;
   private double learningRate;


   /**
    * Creates an empty learning model for Linear Classification, with an zero weight vector, a bias
    * of zero and the given learning rate.
    * 
    * @param numberOfFeatures the number of features of each instance used in {@code train()}
    * @param learningRate the learning rate of this learning model
    */
   public LinearClassificationLearningModel(final int numberOfFeatures, final double learningRate) {
      super(numberOfFeatures);
      this.weightVector = new DenseDoubleVector(numberOfFeatures);
      this.bias = 0;
      this.learningRate = learningRate;
   }


   /**
    * Creates a learning model for Linear Classification, with the given weight vector, the given
    * bias and the given learning rate.
    * 
    * @param weightVector the weight vector of this learning model
    * @param bias the bias of this learning model
    * @param learningRate the learning rate of this learning model
    */
   public LinearClassificationLearningModel(final IVector weightVector, final double bias, final double learningRate) {
      super(weightVector.length());
      this.weightVector = weightVector.duplicate();
      this.bias = bias;
      this.learningRate = learningRate;
   }


   @Override
   public IVector getWeightVector() {
      return this.weightVector;
   }


   /**
    * Sets the weight vector of this learning model.
    * 
    * @param weightVector the weight vector to set
    */
   public void setWeightVector(final IVector weightVector) {
      this.weightVector = weightVector.duplicate();
   }


   /**
    * Evaluates the in-sample error for the given dataset, using the 0/1-loss.
    * 
    * @param baselearnerDataset the dataset on which the in-sample error will be evaluated
    * 
    * @return the in-sample error for the given data set
    */
   public double evaluateInSampleError(final BaselearnerDataset baselearnerDataset) {
      double result = 0;
      for (int i = 0; i < baselearnerDataset.getNumberOfInstances(); i++) {
         BaselearnerInstance baselearnerInstance = baselearnerDataset.getInstance(i);
         double prediction = -1;
         try {
            prediction = predict(baselearnerInstance);
            if (Double.compare(prediction, baselearnerInstance.getRating()) != 0) {
               result++;
            }
         } catch (PredictionFailedException e) {
            // not possible as of construction
            logger.error(ERROR_SOMETHING_UNFORESEEABLE_HAPPENED, e);
         }
      }
      result /= baselearnerDataset.getNumberOfInstances();
      return result;
   }


   @Override
   public List<Double> predict(final IDataset<?, ?, ?> dataset) throws PredictionFailedException {
      checkDatasetForCompatibility(dataset);
      List<Double> predictionResults = new ArrayList<>();
      for (int i = 0; i < dataset.getNumberOfInstances(); i++) {
         predictionResults.add((double) predict(dataset.getInstance(i)));
      }
      return predictionResults;
   }


   @Override
   public Double predict(final IInstance<?, ?, ?> instance) throws PredictionFailedException {
      checkInstanceForCompatibility(instance);
      double dotProduct = weightVector.dotProduct(((BaselearnerInstance) instance).getContextFeatureVector());
      dotProduct += bias;
      return sign(dotProduct);
   }


   /**
    * Returns the sign of the given value.
    * 
    * @param value the value for which the sign will be returned
    * 
    * @return the sign of the given value, which is {@code 1} if the value is positive and
    *         {@code -1} if the value is negative
    */
   private double sign(double value) {
      return value >= 0 ? 1 : -1;
   }


   /**
    * Updates the weight vector for the given instance.
    * 
    * @param baselearnerInstance the instance for which the prediction was incorrect
    */
   public void updateWeightVector(final BaselearnerInstance baselearnerInstance) {
      bias += learningRate * baselearnerInstance.getRating();
      IVector scaledFeatureVector = new DenseDoubleVector(baselearnerInstance.getContextFeatureVector());
      scaledFeatureVector.multiplyByConstant(learningRate * baselearnerInstance.getRating());
      weightVector.addVector(scaledFeatureVector);
   }


   @Override
   public double getBias() {
      return bias;
   }


   /**
    * Sets the bias of the learning model.
    * 
    * @param bias the bias to set
    */
   public void setBias(double bias) {
      this.bias = bias;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (getClass() != obj.getClass())
         return false;
      LinearClassificationLearningModel other = (LinearClassificationLearningModel) obj;
      if (Double.doubleToLongBits(bias) != Double.doubleToLongBits(other.bias))
         return false;
      if (Double.doubleToLongBits(learningRate) != Double.doubleToLongBits(other.learningRate))
         return false;
      if (weightVector == null) {
         if (other.weightVector != null)
            return false;
      } else if (!weightVector.equals(other.weightVector))
         return false;
      return true;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      long temp;
      temp = Double.doubleToLongBits(bias);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      temp = Double.doubleToLongBits(learningRate);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      result = prime * result + ((weightVector == null) ? 0 : weightVector.hashCode());
      return result;
   }

}
