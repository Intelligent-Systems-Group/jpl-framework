package de.upb.cs.is.jpl.api.dataset.ordinalclassification;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.dataset.ADatasetTest;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.util.datastructure.NullType;


/**
 * Tests for the {@link OrdinalClassificationDataset}.
 * 
 * @author Tanja Tornede
 *
 */
public class OrdinalClassificationDatasetTest extends ADatasetTest<double[], NullType, Double> {

   private static final String RESOURCE_DIRECTORY_LEVEL = "ordinalclassification" + File.separator;

   private List<Double> predictionClasses;
   private int numberOfContextFeatures;


   /**
    * Creates a test for the {@link OrdinalClassificationDataset}.
    */
   public OrdinalClassificationDatasetTest() {
      super(RESOURCE_DIRECTORY_LEVEL);

      double[] predictionClassArray = { 1, 2, 3, 4, 5 };
      predictionClasses = new ArrayList<>();
      for (int i = 0; i < predictionClassArray.length; i++) {
         predictionClasses.add(predictionClassArray[i]);
      }

      numberOfContextFeatures = 3;
   }


   @Override
   protected IDataset<double[], NullType, Double> getDataset() {
      return new OrdinalClassificationDataset(predictionClasses, numberOfContextFeatures);
   }


   @Override
   protected List<IInstance<double[], NullType, Double>> getValidInstances() {
      List<IInstance<double[], NullType, Double>> instanceList = new ArrayList<>();

      double[][] featureVectors = { { 5.3, 2.1, 2.5 }, { 3.1, 3.5, 1.7 }, { 2.5, 4.5, 2.6 } };
      double[] ratings = { 2, 4, 5 };
      for (int i = 0; i < ratings.length; i++) {
         instanceList.add(new OrdinalClassificationInstance(featureVectors[i], ratings[i]));
      }

      return instanceList;
   }

}
