package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.collaborativefiltering.matrixfactorization;


import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ALearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.dataset.collaborativefiltering.CollaborativeFilteringDataset;
import de.upb.cs.is.jpl.api.dataset.collaborativefiltering.CollaborativeFilteringInstance;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.math.linearalgebra.IMatrix;


/**
 * The model created by the {@link MatrixFactorizationLearningAlgorithm} algorithm.
 * 
 * @author Sebastian Osterbrink
 *
 */
public class MatrixFactorizationLearningModel extends ALearningModel<Double> {

   private static final String PREDICTION_FAILED = "Prediction failed. Output is NaN.";
   private static final String THE_COMBINATION_OF_CONTEXT_ID_I_AND_ITEM_ID_I_IS_NOT_VALID = "The combination of Context ID %i and Item ID %i is not valid. "
         + "You can only predict for context / item combinations which are in the training dataset.";

   private IMatrix userHiddenFeatures;
   private IMatrix itemHiddenFeatures;


   /**
    * Creates the model based on the calculated hidden features of the dataset.
    * 
    * @param hiddenItemFeatures the hidden features found for the items
    * @param hiddenContextFeatures the hidden features found for the contexts
    */
   public MatrixFactorizationLearningModel(IMatrix hiddenItemFeatures, IMatrix hiddenContextFeatures) {
      this.userHiddenFeatures = hiddenContextFeatures;
      this.itemHiddenFeatures = hiddenItemFeatures;
   }


   @Override
   public Double predict(IInstance<?, ?, ?> instance) throws PredictionFailedException {
      CollaborativeFilteringInstance cfInstance = (CollaborativeFilteringInstance) instance;
      if (!isInstanceCompatible(cfInstance)) {
         throw new PredictionFailedException(String.format(THE_COMBINATION_OF_CONTEXT_ID_I_AND_ITEM_ID_I_IS_NOT_VALID,
               cfInstance.getContextId(), cfInstance.getItemId()));
      }

      double prediction = userHiddenFeatures.getRowVector(instance.getContextId())
            .dotProduct(itemHiddenFeatures.getColumnVector(cfInstance.getItemId()));

      if (Double.isNaN(prediction)) {
         throw new PredictionFailedException(PREDICTION_FAILED);
      }
      return prediction;
   }


   @Override
   public boolean isDatasetCompatible(IDataset<?, ?, ?> dataset) {
      if (!(dataset instanceof CollaborativeFilteringDataset)) {
         return false;
      }
      boolean compatible = true;
      for (int i = 0; i < dataset.getNumberOfInstances() && compatible; i++) {
         compatible = isInstanceCompatible(dataset.getInstance(i));
      }
      return compatible;

   }


   @Override
   public boolean isInstanceCompatible(IInstance<?, ?, ?> instance) {
      if (!(instance instanceof CollaborativeFilteringInstance)) {
         return false;
      }
      CollaborativeFilteringInstance castedInstance = (CollaborativeFilteringInstance) instance;
      return instance.getContextId() < userHiddenFeatures.getNumberOfRows()
            && castedInstance.getItemId() < itemHiddenFeatures.getNumberOfColumns();

   }


   @Override
   public boolean equals(Object object) {
      if (this == object)
         return true;
      if (!super.equals(object))
         return false;
      if (object instanceof MatrixFactorizationLearningModel) {
         MatrixFactorizationLearningModel comparedObject = (MatrixFactorizationLearningModel) object;
         return userHiddenFeatures.equals(comparedObject.userHiddenFeatures) && itemHiddenFeatures.equals(itemHiddenFeatures);
      }
      return false;
   }


   @Override
   public int hashCode() {
      int hashCode = super.hashCode();
      hashCode += 31 * userHiddenFeatures.hashCode();
      hashCode += 31 * itemHiddenFeatures.hashCode();
      return hashCode;
   }
}
