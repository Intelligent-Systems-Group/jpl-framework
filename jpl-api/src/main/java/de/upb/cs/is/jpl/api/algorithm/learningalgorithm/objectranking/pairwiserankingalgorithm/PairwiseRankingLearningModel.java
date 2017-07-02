package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.objectranking.pairwiserankingalgorithm;


import java.util.Arrays;

import de.upb.cs.is.jpl.api.algorithm.baselearner.ABaseLearningModel;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.objectranking.AObjectRankingWithBaseLearnerLearningModel;
import de.upb.cs.is.jpl.api.dataset.objectranking.ObjectRankingDataset;
import de.upb.cs.is.jpl.api.exception.UnsupportedOperationException;
import de.upb.cs.is.jpl.api.math.linearalgebra.DenseDoubleVector;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * The model class is created by {@link PairwiseRankingLearningAlgorithm} after the training on the
 * {@link ObjectRankingDataset}.
 * 
 * @author Pritha Gupta
 *
 */
public class PairwiseRankingLearningModel extends AObjectRankingWithBaseLearnerLearningModel {

   private String methodType = StringUtils.EMPTY_STRING;


   /**
    * Creates a new {@link PairwiseRankingLearningModel} with the {@link ABaseLearningModel} which
    * is trained on the transformed dataset.
    * 
    * @param baseLearnerModel the base learner model to set
    * @param numberOfItemFeatures the number of item features for the object on which the model was
    *           trained
    * @param numberOfContextFeatures the number of context features for the object on which the
    *           model was trained
    * @param methodType the type of method used to generate classification pairs
    */
   public PairwiseRankingLearningModel(ABaseLearningModel<Double> baseLearnerModel, int numberOfItemFeatures, int numberOfContextFeatures,
         String methodType) {
      super(baseLearnerModel, numberOfItemFeatures, numberOfContextFeatures, false);
      this.methodType = methodType;
   }


   @Override
   protected IVector createCorrectWeightVectorFromBaseLearner() throws UnsupportedOperationException {
      IVector weights = baseLearnerModel.getWeightVector();
      double[] newWeights;
      if (methodType.equals(PairwiseRankingConfiguration.ORDER_SVM)) {
         newWeights = Arrays.copyOfRange(weights.asArray(), 0, numberOfItemFeatures + numberOfContextFeatures);
         return new DenseDoubleVector(newWeights);
      } else {
         return baseLearnerModel.getWeightVector();
      }
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((methodType == null) ? 0 : methodType.hashCode());
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (getClass() != obj.getClass())
         return false;
      PairwiseRankingLearningModel other = (PairwiseRankingLearningModel) obj;
      if (methodType == null) {
         if (other.methodType != null)
            return false;
      } else if (!methodType.equals(other.methodType))
         return false;
      return true;
   }


}
