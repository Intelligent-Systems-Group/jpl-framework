package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ordinalclassification;


import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ALearningAlgorithm;
import de.upb.cs.is.jpl.api.dataset.IDatasetParser;
import de.upb.cs.is.jpl.api.dataset.ordinalclassification.OrdinalClassificationDataset;
import de.upb.cs.is.jpl.api.dataset.ordinalclassification.OrdinalClassificationDatasetParser;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;


/**
 * Abstract class for Ordinal Classification algorithms.
 * 
 * @author Tanja Tornede
 * 
 * @param <CONFIG> the type of the algorithm configuration used by the implementation of this
 *           abstract class
 */
public abstract class AOrdinalClassification<CONFIG extends AAlgorithmConfiguration> extends ALearningAlgorithm<CONFIG> {


   /**
    * Creates a new ordinal classification learning algorithm with the given algorithm identifier.
    *
    * @param algorithmIdentifier the identifier of the implementation of this class
    */
   public AOrdinalClassification(String algorithmIdentifier) {
      super(algorithmIdentifier);
   }


   @Override
   public IDatasetParser getDatasetParser() {
      return new OrdinalClassificationDatasetParser();
   }


   /**
    * Returns the prediction classes of the given dataset.
    * 
    * @param dataset the dataset containing the prediction classes
    * 
    * @return the prediction classes of the dataset
    */
   protected List<Double> getPredictionClasses(OrdinalClassificationDataset dataset) {
      return CollectionsUtils.getDeepCopyOf(dataset.getValidRatings());
   }


   @Override
   public boolean equals(Object secondObject) {
      if (super.equals(secondObject) && secondObject instanceof AOrdinalClassification) {
         return true;
      }
      return false;
   }

}
