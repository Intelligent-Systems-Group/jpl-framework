package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.objectranking.expectedrankregression;


import de.upb.cs.is.jpl.api.algorithm.baselearner.ABaseLearningModel;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.objectranking.AObjectRankingWithBaseLearnerLearningModel;
import de.upb.cs.is.jpl.api.dataset.objectranking.ObjectRankingDataset;


/**
 * The model class is created by {@link ExpectedRankRegression} after the training on the
 * {@link ObjectRankingDataset}.
 * 
 * @author Pritha Gupta
 *
 */
public class ExpectedRankRegressionLearningModel extends AObjectRankingWithBaseLearnerLearningModel {


   /**
    * Creates a new {@link ExpectedRankRegressionLearningModel} with the {@link ABaseLearningModel}
    * which is trained on the transformed dataset.
    * 
    * @param baseLearningModel the base learner model to set
    * @param numberOfItemFeatures the number of item features for the object on which the model was
    *           trained
    * @param numberOfContextFeatures the number of context features for the object on which the
    *           model was trained
    */
   public ExpectedRankRegressionLearningModel(ABaseLearningModel<Double> baseLearningModel, int numberOfItemFeatures,
         int numberOfContextFeatures) {
      super(baseLearningModel, numberOfItemFeatures, numberOfContextFeatures, true);
   }


}
