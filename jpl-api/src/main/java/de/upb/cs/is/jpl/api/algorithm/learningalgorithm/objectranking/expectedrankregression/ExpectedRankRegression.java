package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.objectranking.expectedrankregression;


import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ELearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.objectranking.AObjectRankingWithBaseLearner;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.objectranking.ObjectRankingDataset;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;


/**
 * This class implements the basic Expected Rank Regression for Object Ranking learning problem. It
 * is linked to the {@link Enum} value, {@link ELearningAlgorithm#EXPECTED_RANK_REGRESSION}.
 * 
 * @author Pritha Gupta
 *
 */
public class ExpectedRankRegression extends AObjectRankingWithBaseLearner<ExpectedRankRegressionConfiguration> {


   /**
    * Creates a new expected rank regression learning algorithm with the enum identifier.
    */
   public ExpectedRankRegression() {
      super(ELearningAlgorithm.EXPECTED_RANK_REGRESSION.getIdentifier());
   }


   @Override
   protected AAlgorithmConfiguration createDefaultAlgorithmConfiguration() {
      return new ExpectedRankRegressionConfiguration();
   }


   @Override
   protected void setBaselearnerDataset(ObjectRankingDataset objectRankingDataset) {
      baselearnerDataset = RankingRegressionTransformerUtils.transformDatasetToBaseLearnerRegressionDataset(objectRankingDataset);

   }


   @Override
   protected ILearningModel<?> createObjectRankingLearningModel(int numberofItemFeatures, int numberOfContextFeatures) {
      return new ExpectedRankRegressionLearningModel(baseLearningModel, numberofItemFeatures, numberOfContextFeatures);
   }


   @Override
   public ExpectedRankRegressionLearningModel train(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      return (ExpectedRankRegressionLearningModel) super.train(dataset);
   }

}
