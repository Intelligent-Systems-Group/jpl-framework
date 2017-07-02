package de.upb.cs.is.jpl.api.algorithm.baselearner.regression.linear;


import java.util.LinkedList;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.baselearner.ABaseLearningModel;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerInstance;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.math.linearalgebra.DenseDoubleVector;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;


/**
 * An implementation of the learning model of the ordinal least squares as base learner for
 * regression. At the initialization phase the need the {@code solution} of the QR-Decomposition in
 * the training step as an {@link IVector}.
 *
 * @author Sebastian Gottschalk
 */
public class LinearRegressionLearningModel extends ABaseLearningModel<Double> {
   private double bias;
   private IVector weightVector;


   /**
    * Creates a new linear regression model from a {@link IVector}.
    *
    * @param linearRegressionResultVector the vector which should be used for linear regression
    *           model
    */
   public LinearRegressionLearningModel(IVector linearRegressionResultVector) {
      super(linearRegressionResultVector.length() - 1);
      bias = linearRegressionResultVector.getValue(0);
      double[] weightingsWithoutBias = new double[linearRegressionResultVector.length() - 1];
      for (int i = 0; i < linearRegressionResultVector.length() - 1; i++) {
         weightingsWithoutBias[i] = linearRegressionResultVector.getValue(i + 1);
      }
      weightVector = new DenseDoubleVector(weightingsWithoutBias);

   }


   @Override
   public List<Double> predict(IDataset<?, ?, ?> dataset) throws PredictionFailedException {
      checkDatasetForCompatibility(dataset);

      BaselearnerDataset simpleDataset = (BaselearnerDataset) dataset;
      List<Double> predictionResultList = new LinkedList<>();
      for (int i = 0; i < simpleDataset.getNumberOfInstances(); i++) {
         predictionResultList.add(predict(simpleDataset.getInstance(i)));
      }

      return predictionResultList;
   }


   @Override
   public Double predict(IInstance<?, ?, ?> instance) throws PredictionFailedException {
      checkInstanceForCompatibility(instance);
      BaselearnerInstance simpleInstance = (BaselearnerInstance) instance;
      return getWeightVector().dotProduct(simpleInstance.getContextFeatureVector()) + getBias();
   }


   @Override
   public double getBias() {
      return bias;
   }


   @Override
   public IVector getWeightVector() {
      return weightVector;
   }


   @Override
   public boolean equals(Object secondObject) {
      if (super.equals(secondObject) && secondObject instanceof LinearRegressionLearningModel) {
         LinearRegressionLearningModel linearRegressionModel = (LinearRegressionLearningModel) secondObject;
         if (Double.compare(bias, linearRegressionModel.bias) == 0 && weightVector.equals(linearRegressionModel.weightVector)) {
            return true;
         }
      }
      return false;
   }


   @Override
   public int hashCode() {
      int hashCode = super.hashCode();
      hashCode += 31 * hashCode + bias;
      hashCode += 31 * hashCode + weightVector.hashCode();
      return hashCode;
   }


   @Override
   public boolean isDatasetCompatible(IDataset<?, ?, ?> dataset) {
      BaselearnerDataset castedDataset = (BaselearnerDataset) dataset;

      return castedDataset.getNumberOfFeatures() == weightVector.length();
   }


   @Override
   public boolean isInstanceCompatible(IInstance<?, ?, ?> instance) {
      BaselearnerInstance castedInstance = (BaselearnerInstance) instance;

      return castedInstance.getFeatureVector().length == weightVector.length();
   }


}
