package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ordinalclassification;


import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerInstance;
import de.upb.cs.is.jpl.api.dataset.ordinalclassification.OrdinalClassificationDataset;
import de.upb.cs.is.jpl.api.dataset.ordinalclassification.OrdinalClassificationInstance;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This utils class contains methods of ordinal classification.
 * 
 * @author Tanja Tornede
 *
 */
public class OrdinalClassificationUtils {

   /**
    * Hides the public constructor.
    */
   private OrdinalClassificationUtils() {
      throw new IllegalAccessError(StringUtils.EXCEPTION_MESSAGE_ACCESS_ERROR);
   }


   /**
    * Transforms the given ordinal instance to a list of {@link BaselearnerInstance}s.
    * 
    * @param ordinalInstance the ordinal instance to transform
    * @param predictionClasses the classes used for prediction
    * 
    * @return a list of transformed baselearner instances of the given instance, one for each
    *         prediction class
    */
   public static List<BaselearnerInstance> transformToAllBaselearnerInstancesWithCorrectResult(
         OrdinalClassificationInstance ordinalInstance, List<Double> predictionClasses) {
      List<BaselearnerInstance> transformedInstances = new ArrayList<>(predictionClasses.size());
      for (int i = 0; i < predictionClasses.size(); i++) {
         BaselearnerInstance instance = transformToBaselearnerInstanceWithCorrectResult(ordinalInstance, predictionClasses.get(i));
         transformedInstances.add(instance);
      }
      return transformedInstances;
   }


   /**
    * Transforms the given ordinal instance to a single {@link BaselearnerInstance}.
    * 
    * @param ordinalInstance the ordinal instance to transform
    * 
    * @return the transformed baselearner instance
    */
   public static BaselearnerInstance transformToBaselearnerInstance(OrdinalClassificationInstance ordinalInstance) {
      double[] contextFeatureVector = ordinalInstance.getContextFeatureVector();
      return new BaselearnerInstance(contextFeatureVector);
   }


   /**
    * Transforms the given ordinal instance to a single {@link BaselearnerInstance} having a rating.
    * 
    * @param ordinalInstance the ordinal instance to transform
    * @param predictionClass the class used for prediction
    * 
    * @return the transformed baselearner instance of the given ordinal instance
    */
   public static BaselearnerInstance transformToBaselearnerInstanceWithCorrectResult(OrdinalClassificationInstance ordinalInstance,
         double predictionClass) {
      double[] contextFeatureVector = ordinalInstance.getContextFeatureVector();
      double rating = ordinalInstance.getRating() <= predictionClass ? -1 : 1;
      return new BaselearnerInstance(contextFeatureVector, rating);
   }


   /**
    * Transforms the given ordinal dataset to a list of {@link BaselearnerDataset}s, each for the
    * given prediction classes.
    * 
    * @param ordinalDataset the ordinal dataset to transform
    * @param predictionClasses the classes used for prediction
    * 
    * @return a list of transformed baselearner datasets of the given dataset, one for each
    *         prediction class
    */
   public static List<BaselearnerDataset> transformToAllBaselearnerDatasets(OrdinalClassificationDataset ordinalDataset,
         List<Double> predictionClasses) {
      List<BaselearnerDataset> transformedDatasets = new ArrayList<>();
      for (int i = 0; i < predictionClasses.size(); i++) {
         BaselearnerDataset dataset = transformToBaselearnerDataset(ordinalDataset);
         transformedDatasets.add(dataset);
      }
      return transformedDatasets;
   }


   /**
    * Transforms the given ordinal dataset to a single {@link BaselearnerDataset}.
    * 
    * @param ordinalDataset the ordinal dataset to transform
    * 
    * @return the transformed baselearner dataset of the given ordinal instance
    */
   public static BaselearnerDataset transformToBaselearnerDataset(OrdinalClassificationDataset ordinalDataset) {
      BaselearnerDataset baselearnerDataset = new BaselearnerDataset(ordinalDataset.getNumberOfInstances(),
            ordinalDataset.getNumberOfFeatures());

      List<double[]> featureVectors = ordinalDataset.getFeatureVectors();
      for (int i = 0; i < ordinalDataset.getNumberOfInstances(); i++) {
         baselearnerDataset.addFeatureVectorWithoutResult(featureVectors.get(i));
      }
      return baselearnerDataset;
   }
}
