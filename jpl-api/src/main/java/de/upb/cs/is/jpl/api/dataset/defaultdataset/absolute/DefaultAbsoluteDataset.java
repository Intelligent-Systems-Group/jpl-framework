package de.upb.cs.is.jpl.api.dataset.defaultdataset.absolute;


import java.util.List;

import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.DefaultDataset;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.DefaultInstance;
import de.upb.cs.is.jpl.api.exception.dataset.InvalidInstanceException;
import de.upb.cs.is.jpl.api.math.linearalgebra.DenseDoubleVector;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;


/**
 * The default dataset class for datasets with absolute instance values.
 * 
 * @author Sebastian Osterbrink
 *
 */
public class DefaultAbsoluteDataset extends DefaultDataset<IVector> {

   /**
    * Creates a new dataset from the subset of selected contexts and items
    * 
    * @param contexts the {@link List} of selected contexts in the order they should be in the new
    *           dataset
    * @param items the {@link List} of selected items in the order they should be in the new dataset
    * @param from the index of the first rating
    * @param to the index of the first rating not included
    * @return the reduced dataset
    * @throws InvalidInstanceException if creating the smaller dataset failed due to inconsistent
    *            instances
    */
   public DefaultAbsoluteDataset getPartOfDataset(List<Integer> contexts, List<Integer> items, int from, int to)
         throws InvalidInstanceException {
      DefaultAbsoluteDataset result = new DefaultAbsoluteDataset();
      result.datasetFile = this.datasetFile;

      for (int featureNumber = 0; featureNumber < itemFeatures.size(); featureNumber++) {
         result.setItemFeature(featureNumber, itemFeatures.get(featureNumber));
         result.setItemFeatureAllowedValues(featureNumber, itemFeatureAllowedValues.get(featureNumber));
      }
      for (int featureNumber = 0; featureNumber < contextFeatures.size(); featureNumber++) {
         result.setContextFeature(featureNumber, contextFeatures.get(featureNumber));
         result.setContextFeatureAllowedValues(featureNumber, contextFeatureAllowedValues.get(featureNumber));
      }

      for (int itemPos = 0; itemPos < items.size(); itemPos++) {
         result.setItemVector(itemPos, selectSubsetOfArray(datasetFile.getItemFeatures(), this.getItemVector(contexts.get(itemPos))));
      }
      for (int contextPos = 0; contextPos < contexts.size(); contextPos++) {
         result.setContextVector(contextPos,
               selectSubsetOfArray(datasetFile.getContextFeatures(), this.getContextVector(contexts.get(contextPos))));
      }

      List<DefaultInstance<IVector>> ratingsVectors = CollectionsUtils.getDeepCopyOf(ratings.subList(from, to));
      for (DefaultInstance<IVector> instance : ratingsVectors) {
         if (contexts.contains(instance.getContextId())) {
            double[] vector = selectSubsetOfArray(items, instance.getRating().asArray());
            DefaultInstance<IVector> newVector = new DefaultInstance<>(instance.getContextId(), new DenseDoubleVector(vector), result);
            result.addInstance(newVector);
         }

      }
      return result;
   }


   /**
    * Extracts a new array from the source array by adding the selected positions of the source
    * array to the new array. This is done in the order provided by the selectedPositions list.
    * 
    * @param selectedPositions the list of selected positions
    * @param source the source array from which the data is extracted
    * @return the selected subset
    */
   private double[] selectSubsetOfArray(List<Integer> selectedPositions, double[] source) {
      if (selectedPositions == null || selectedPositions.isEmpty())
         return source;
      double[] resultArray = new double[selectedPositions.size()];
      int i = 0;
      for (Integer index : selectedPositions) {
         resultArray[i] = source[index];
         i++;
      }
      return resultArray;
   }


   @Override
   public IDataset<double[], List<double[]>, IVector> getPartOfDataset(int from, int to) {

      DefaultAbsoluteDataset result = new DefaultAbsoluteDataset();
      result.datasetFile = this.datasetFile;
      result.contextVectors = CollectionsUtils.getDeepCopyOf(contextVectors);
      result.contextFeatures = CollectionsUtils.getDeepCopyOf(contextFeatures);
      result.itemVectors = CollectionsUtils.getDeepCopyOf(itemVectors);
      result.itemFeatures = CollectionsUtils.getDeepCopyOf(itemFeatures);
      result.ratings = CollectionsUtils.getDeepCopyOf(ratings.subList(from, to));
      return result;
   }


}
