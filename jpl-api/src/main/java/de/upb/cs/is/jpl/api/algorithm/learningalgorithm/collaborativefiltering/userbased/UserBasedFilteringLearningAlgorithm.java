package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.collaborativefiltering.userbased;


import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ALearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ELearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IDatasetParser;
import de.upb.cs.is.jpl.api.dataset.collaborativefiltering.CollaborativeFilteringDataset;
import de.upb.cs.is.jpl.api.dataset.collaborativefiltering.CollaborativeFilteringParser;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.metric.IMetric;
import de.upb.cs.is.jpl.api.metric.correlation.CosineSimilariy;
import de.upb.cs.is.jpl.api.metric.correlation.PearsonCorrelation;


/**
 * This algorithm is a very naive approach to collaborative filtering an is based on simple nearest
 * neighbor calculations. The algorithm will find the k nearest neighbors based either on cosine
 * similarity or pearson correlation. For these neighbors it will calculate the average rating.
 * 
 * @author Sebastian Osterbrink
 *
 */
public class UserBasedFilteringLearningAlgorithm extends ALearningAlgorithm<UserBasedFilteringConfiguration> {


   /**
    * Creates a new user based filtering algorithm with the enum identifier.
    */
   public UserBasedFilteringLearningAlgorithm() {
      super(ELearningAlgorithm.USER_BASED_FILTERING.getIdentifier());
   }


   @Override
   public boolean equals(Object object) {
      if (this == object)
         return true;
      if (!super.equals(object))
         return false;
      if (object instanceof UserBasedFilteringLearningAlgorithm) {
         UserBasedFilteringLearningAlgorithm comparedObject = (UserBasedFilteringLearningAlgorithm) object;
         return this.configuration.equals(comparedObject.getAlgorithmConfiguration());
      }
      return false;
   }


   @Override
   public int hashCode() {
      return super.hashCode() + 31 * configuration.hashCode();
   }


   @Override
   public IDatasetParser getDatasetParser() {
      return new CollaborativeFilteringParser();
   }


   @Override
   protected ILearningModel<?> performTraining(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      IMetric<IVector, Double> similarity;
      assertDatasetHasCorrectType(dataset, CollaborativeFilteringDataset.class);
      if (UserBasedFilteringConfiguration.COSINE.equals(configuration.getCorrelationType())) {
         similarity = new CosineSimilariy();
      } else {
         similarity = new PearsonCorrelation();
      }

      return new UserBasedFilteringLearningModel((CollaborativeFilteringDataset) dataset, configuration.getNumberOfNeighbors(),
            configuration.getMinDouble(), similarity);
   }


   @Override
   protected AAlgorithmConfiguration createDefaultAlgorithmConfiguration() {
      AAlgorithmConfiguration result = new UserBasedFilteringConfiguration();
      result.initializeDefaultConfiguration();
      return result;
   }


   @Override
   public UserBasedFilteringLearningModel train(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      return (UserBasedFilteringLearningModel) super.train(dataset);
   }


   @Override
   public void init() {
      // nothing to do
   }


}
