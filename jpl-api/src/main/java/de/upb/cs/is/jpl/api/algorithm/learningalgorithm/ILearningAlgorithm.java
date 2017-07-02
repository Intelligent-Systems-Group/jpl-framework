package de.upb.cs.is.jpl.api.algorithm.learningalgorithm;


import de.upb.cs.is.jpl.api.algorithm.ITrainableAlgorithm;
import de.upb.cs.is.jpl.api.dataset.IDatasetParser;


/**
 * Interface for a learning algorithm specifying the required behavior. A learning algorithm should
 * be able to return its associated dataset and configuration file parser and it should be able to
 * be trained on a given dataset.
 * 
 * @author Sebastian Gottschalk
 *
 */
public interface ILearningAlgorithm extends ITrainableAlgorithm {

   /**
    * Returns the associated dataset parser. Only this parser should be used to create a dataset
    * instance from a file specifically tailored for this learning algorithm.
    * 
    * @return the dataset parser tailored for this learning algorithm. Must not be {@code null}.
    */
   public IDatasetParser getDatasetParser();

}
