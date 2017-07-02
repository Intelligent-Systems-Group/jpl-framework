package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.objectranking;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.algorithm.baselearner.ABaseLearningModel;
import de.upb.cs.is.jpl.api.algorithm.baselearner.ABaselearnerAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.baselearner.IBaselearnerAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.AAlgorithmConfigurationWithBaseLearner;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ALearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IDatasetParser;
import de.upb.cs.is.jpl.api.dataset.objectranking.ObjectRankingDataset;
import de.upb.cs.is.jpl.api.dataset.objectranking.ObjectRankingDatasetParser;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;


/**
 * Abstract class for Object Ranking which uses some {@link ABaselearnerAlgorithm} for training and
 * predicting the ranking.
 * 
 * @author Pritha Gupta
 * @param <CONFIG> the generic type extending AAlgorithmConfigurationWithBaseLearner, configuration
 *           associated with the AObjectRankingWithBaseLearnerLearningAlgorithm class.
 */
public abstract class AObjectRankingWithBaseLearner<CONFIG extends AAlgorithmConfigurationWithBaseLearner>
      extends ALearningAlgorithm<CONFIG> {
   protected static final Logger logger = LoggerFactory.getLogger(AObjectRankingWithBaseLearner.class);
   protected static final String BASE_LEARNER_TRIANING_FAILED = "The model was not trained on dataset %s due to error %s.";

   protected BaselearnerDataset baselearnerDataset;
   protected ABaseLearningModel<Double> baseLearningModel;


   /**
    * Creates a new object ranking with the object ranking algorithm identifier.
    *
    * @param identifier the object ranking identifier
    */
   public AObjectRankingWithBaseLearner(String identifier) {
      super(identifier);
   }


   @Override
   public void init() {
      baselearnerDataset = null;
      baseLearningModel = null;
   }


   @Override
   public IDatasetParser getDatasetParser() {
      return new ObjectRankingDatasetParser();
   }


   @Override
   protected ILearningModel<?> performTraining(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      ObjectRankingDataset objectRankingDataset = (ObjectRankingDataset) dataset;
      setBaselearnerDataset(objectRankingDataset);
      createAndSetBaseLearningModel();
      return createObjectRankingLearningModel(objectRankingDataset.getNumofItemFeatures(), objectRankingDataset.getNumofContextFeatures());
   }


   protected abstract void setBaselearnerDataset(ObjectRankingDataset objectRankingDataset);


   @SuppressWarnings("unchecked")
   protected void createAndSetBaseLearningModel() throws TrainModelsFailedException {
      IBaselearnerAlgorithm baseLearnerAlgorithm = configuration.getBaseLearnerAlgorithm();
      try {
         baseLearningModel = (ABaseLearningModel<Double>) baseLearnerAlgorithm.train(baselearnerDataset);
      } catch (Exception exception) {
         String errorMessage = String.format(BASE_LEARNER_TRIANING_FAILED, baselearnerDataset.toString(), exception.getMessage());
         logger.error(errorMessage);
         throw new TrainModelsFailedException(errorMessage, exception);
      }
   }


   protected abstract ILearningModel<?> createObjectRankingLearningModel(int numberOfItemFeatures, int numberOfContextFeatures);


   /**
    * Returns the {@link BaselearnerDataset} transformed from the {@link ObjectRankingDataset}
    * dataset.
    * 
    * @return the base learner transformed dataset
    */
   public BaselearnerDataset getBaselearnerDataset() {
      return baselearnerDataset;
   }


   /**
    * Sets the base learner transformed from the {@link ObjectRankingDataset} dataset.
    * 
    * @param baselearnerDataset the base learner transformed dataset
    * 
    */
   public void setBaselearnerDataset(BaselearnerDataset baselearnerDataset) {
      this.baselearnerDataset = baselearnerDataset;
   }


   /**
    * Returns the {@link ABaseLearningModel} model trained on the transformed base learner dataset.
    * 
    * @return the base learner models
    */
   public ABaseLearningModel<Double> getBaseLearningModel() {
      return baseLearningModel;
   }


   /**
    * Sets the {@link ABaseLearningModel} model trained on the transformed base learner dataset.
    * 
    * @param baseLearningModel he base learner model
    */
   public void setBaseLearningModel(ABaseLearningModel<Double> baseLearningModel) {
      this.baseLearningModel = baseLearningModel;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((baselearnerDataset == null) ? 0 : baselearnerDataset.hashCode());
      return result;
   }


   @Override
   public boolean equals(Object secondObject) {
      if (super.equals(secondObject) && secondObject instanceof AObjectRankingWithBaseLearner) {
         AObjectRankingWithBaseLearner<?> castedObject = (AObjectRankingWithBaseLearner<?>) secondObject;
         if (baseLearningModel.equals(castedObject.baseLearningModel) && baselearnerDataset.equals(castedObject.baselearnerDataset)
               && configuration.equals(castedObject.configuration))
            return true;
      }
      return false;
   }
}
