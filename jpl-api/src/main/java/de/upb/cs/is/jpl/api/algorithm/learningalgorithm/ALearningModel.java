package de.upb.cs.is.jpl.api.algorithm.learningalgorithm;


import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.algorithm.UnsupportedDatasetTypeException;
import de.upb.cs.is.jpl.api.exception.algorithm.UnsupportedInstanceTypeException;
import de.upb.cs.is.jpl.api.util.TypeCheckUtils;


/**
 * Abstract superclass of all learning models. Any specific learning model should be a subclass of
 * this one.
 * 
 * @author Sebastian Gottschalk
 *
 * @param <RATING> the return type of the prediction(s)
 */
public abstract class ALearningModel<RATING> implements ILearningModel<RATING> {


   private static final String ERROR_INCOMPATIBLE_DATASET = "Dataset %s is incompatible to this learning model.";


   @Override
   public List<RATING> predict(final IDataset<?, ?, ?> dataset) throws PredictionFailedException {
      if (!isDatasetCompatible(dataset)) {
         throw new PredictionFailedException(String.format(ERROR_INCOMPATIBLE_DATASET, dataset.toString()));
      }
      List<RATING> result = new ArrayList<>(dataset.getNumberOfInstances());
      for (int i = 0; i < dataset.getNumberOfInstances(); i++) {
         result.add(predict(dataset.getInstance(i)));
      }
      return result;
   }


   /**
    * Asserts that the given dataset is of the given type. If this is not the case a
    * {@link PredictionFailedException} is thrown.
    * 
    * @param dataset the dataset to check the type of
    * @param expectedType the expected type of the dataset
    * 
    * @throws PredictionFailedException if the dataset is not compatible
    */
   protected void assertDatasetHasCorrectType(IDataset<?, ?, ?> dataset, Class<?> expectedType) throws PredictionFailedException {
      try {
         TypeCheckUtils.assertDatasetHasCorrectType(dataset, expectedType);
      } catch (UnsupportedDatasetTypeException unsupportedDatatsetTypeException) {
         throw new PredictionFailedException(unsupportedDatatsetTypeException);
      }
   }


   /**
    * Asserts that the given instance is of the given type. If this is not the case a
    * {@link PredictionFailedException} is thrown.
    * 
    * @param instance the instance to check the type of
    * @param expectedType the expected type of the instance
    * 
    * @throws PredictionFailedException if the instance is not compatible
    */
   protected void assertInstanceHasCorrectType(IInstance<?, ?, ?> instance, Class<?> expectedType) throws PredictionFailedException {
      try {
         TypeCheckUtils.assertInstanceHasCorrectType(instance, expectedType);
      } catch (UnsupportedInstanceTypeException unsupportedInstanceTypeException) {
         throw new PredictionFailedException(unsupportedInstanceTypeException);
      }
   }


   /**
    * Converts the given prediction (0 or 1) to a -1/1 prediction. If the given prediction is 1, it
    * will stay 1. If it is 0 it will become -1.
    * 
    * @param value the prediction to convert (0 or 1)
    * @return -1, if the given prediction is 0, 1, otherwise
    */
   protected double convertPositiveBinaryPredictionToNegativePrediction(double value) {
      if (Double.compare(value, 0.0) == 0) {
         return -1;
      }
      return 1.0;
   }


   @Override
   public boolean equals(Object secondObject) {
      if (secondObject instanceof ALearningModel) {
         return true;
      }
      return false;
   }


   @Override
   public String toString() {
      return this.getClass().getSimpleName();
   }

}
