package de.upb.cs.is.jpl.api.dataset.defaultdataset.relative;


import java.util.List;

import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.DefaultDataset;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;


/**
 * The default dataset class for datasets with relative instance values.
 * 
 * @author Sebastian Osterbrink
 *
 */
public class DefaultRelativeDataset extends DefaultDataset<Ranking> {

   @Override
   public IDataset<double[], List<double[]>, Ranking> getPartOfDataset(int from, int to) {
      DefaultRelativeDataset result = new DefaultRelativeDataset();
      result.contextVectors = CollectionsUtils.getDeepCopyOf(contextVectors);
      result.contextFeatures = CollectionsUtils.getDeepCopyOf(contextFeatures);
      result.itemVectors = CollectionsUtils.getDeepCopyOf(itemVectors);
      result.itemFeatures = CollectionsUtils.getDeepCopyOf(itemFeatures);
      result.ratings = CollectionsUtils.getDeepCopyOf(ratings.subList(from, to));
      return result;
   }


}
