package de.upb.cs.is.jpl.api.algorithm.baselearner.classification.logistic;


import de.upb.cs.is.jpl.api.algorithm.baselearner.ABaseLearningModel;
import de.upb.cs.is.jpl.api.algorithm.baselearner.ABaselearnerAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.baselearner.EBaseLearner;
import de.upb.cs.is.jpl.api.algorithm.baselearner.IBaselearnerAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;


/**
 * This class is an implementation of Logistic Classification, using Logistic Regression to predict.
 * 
 * @author Tanja Tornede
 *
 */
public class LogisticClassification extends ABaselearnerAlgorithm<LogisticClassificationConfiguration> {


   /**
    * Creates a {@link LogisticClassification} with the default threshold value.
    */
   public LogisticClassification() {
      super(EBaseLearner.LOGISTIC_CLASSIFICATION.getBaseLearnerIdentifier());
   }


   /**
    * Creates a {@link LogisticClassification} with the given threshold value.
    * 
    * @param threshold the threshold to predict the positive case
    */
   public LogisticClassification(double threshold) {
      super(EBaseLearner.LOGISTIC_CLASSIFICATION.getBaseLearnerIdentifier());
      this.configuration.threshold = threshold;
   }


   @Override
   protected LogisticClassificationConfiguration createDefaultAlgorithmConfiguration() {
      LogisticClassificationConfiguration defaultConfiguration = new LogisticClassificationConfiguration();
      defaultConfiguration.initializeDefaultConfiguration();
      return defaultConfiguration;
   }


   @Override
   protected ILearningModel<Double> performTraining(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      BaselearnerDataset baselearnerDataset = (BaselearnerDataset) dataset;

      IBaselearnerAlgorithm baselearner = configuration.getBaseLearnerAlgorithm();
      @SuppressWarnings("unchecked")
      ABaseLearningModel<Double> baseLearningModel = (ABaseLearningModel<Double>) baselearner.train(baselearnerDataset);

      return new LogisticClassificationLearningModel(configuration.getThreshold(), baseLearningModel,
            baselearnerDataset.getNumberOfFeatures());
   }


   @Override
   public LogisticClassificationLearningModel train(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      return (LogisticClassificationLearningModel) super.train(dataset);
   }


   @Override
   public boolean equals(Object secondObject) {
      if (super.equals(secondObject) && secondObject instanceof LogisticClassification) {
         return true;
      }
      return false;
   }

}
