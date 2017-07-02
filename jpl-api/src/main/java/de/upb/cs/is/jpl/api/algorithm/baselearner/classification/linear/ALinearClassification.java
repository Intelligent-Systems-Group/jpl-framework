package de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear;


import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.algorithm.baselearner.ABaselearnerAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerInstance;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;
import de.upb.cs.is.jpl.api.math.RandomGenerator;


/**
 * Abstract class for Linear Classification algorithms.
 * 
 * @author Tanja Tornede
 * 
 * @param <CONFIG> the configuration to use for the implementation of this class
 */
public abstract class ALinearClassification<CONFIG extends AAlgorithmConfiguration> extends ABaselearnerAlgorithm<CONFIG> {

   private static final Logger logger = LoggerFactory.getLogger(ALinearClassification.class);

   private static final String SOMETHING_UNFORESEEABLE_HAPPENED = "Something unforeseeable happened during prediction, this message should not occure due to construction!";


   /**
    * Creates a linear classification algorithm with the default configuration and the given
    * algorithm identifier.
    *
    * @param algorithmIdentifier the identifier of the implementation of this class
    */
   public ALinearClassification(String algorithmIdentifier) {
      super(algorithmIdentifier);
   }


   @Override
   public LinearClassificationLearningModel train(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      return (LinearClassificationLearningModel) super.train(dataset);
   }


   /**
    * Returns one instance of the given data set which is misclassified by the given learning model.
    * 
    * @param learningModel the learning model which will classify the instance
    * @param baselearnerDataset the dataset of which an instance will be picked
    * 
    * @return a misclassified instance of the given dataset, {@code null} if there was none found
    */
   protected BaselearnerInstance pickMisclassifiedInstance(LinearClassificationLearningModel learningModel,
         final BaselearnerDataset baselearnerDataset) {
      Random randomGenerator = RandomGenerator.getRNG();
      Set<Integer> seenInstancesAsIndex = new HashSet<>();

      while (seenInstancesAsIndex.size() < baselearnerDataset.getNumberOfInstances()) {
         int randomInstanceIndex = randomGenerator.nextInt(baselearnerDataset.getNumberOfInstances());
         BaselearnerInstance baselearnerInstance = baselearnerDataset.getInstance(randomInstanceIndex);
         seenInstancesAsIndex.add(randomInstanceIndex);

         try {
            Double prediction = learningModel.predict(baselearnerInstance);
            if (Double.compare(prediction.doubleValue(), baselearnerInstance.getRating()) != 0) {
               return baselearnerInstance;
            }
         } catch (PredictionFailedException e) {
            // not possible as of construction
            logger.error(SOMETHING_UNFORESEEABLE_HAPPENED, e);
         }
      }
      return null;
   }


   /**
    * Returns a subset of instances of the given size {@code sizeOfSubset} of the given
    * {@code baselearnerDataset}.
    * 
    * @param baselearnerDataset the data set to choose from
    * @param sizeOfSubset the size of the returning subset of the given data set
    * 
    * @return a subset of instances of the given data set
    */
   protected Set<BaselearnerInstance> chooseSubsetUniformlyAtRandom(final BaselearnerDataset baselearnerDataset, final int sizeOfSubset) {
      Random randomGenerator = RandomGenerator.getRNG();
      Set<Integer> seenInstancesAsIndex = new HashSet<>();
      Set<BaselearnerInstance> subset = new HashSet<>();

      while (seenInstancesAsIndex.size() < sizeOfSubset) {
         int randomInstanceIndex = randomGenerator.nextInt(baselearnerDataset.getNumberOfInstances());
         if (!seenInstancesAsIndex.contains(randomInstanceIndex)) {
            subset.add(baselearnerDataset.getInstance(randomInstanceIndex));
            seenInstancesAsIndex.add(randomInstanceIndex);
         }
      }
      return subset;
   }


   @Override
   public boolean equals(Object secondObject) {
      if (super.equals(secondObject) && secondObject instanceof ALinearClassification) {
         return true;
      }
      return false;
   }

}
