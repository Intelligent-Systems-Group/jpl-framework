package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ordinalclassification;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ALearningAlgorithmTest;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ordinalclassification.reductionframework.OrdinalClassificationReductionFramework;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ordinalclassification.simple.SimpleOrdinalClassification;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.absolute.DefaultAbsoluteDatasetParser;
import de.upb.cs.is.jpl.api.dataset.ordinalclassification.OrdinalClassificationDataset;
import de.upb.cs.is.jpl.api.dataset.ordinalclassification.OrdinalClassificationDatasetParser;
import de.upb.cs.is.jpl.api.dataset.ordinalclassification.OrdinalClassificationInstance;
import de.upb.cs.is.jpl.api.util.datastructure.NullType;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This abstract class is used for all tests of ordinal classification.
 * 
 * @author Tanja Tornede
 */
public abstract class AOrdinalClassificationTest extends ALearningAlgorithmTest<double[], NullType, Double> {

   protected static final String ERROR_REFLECTION_FAILED = "The reflection failed as of some reson.";

   private static final String RESOURCE_DIRECTORY_LEVEL = "ordinalclassification" + File.separator;
   protected static final String WRONG_DATASET_NAME = "wrongdataset.gprf";
   private static final String DATASET_FOLDER_AUTOMOBILE = "automobile";
   protected static final String DATASET_FOLDER_AUTOMOBILE_TRAIN = "train";
   protected static final String DATASET_FOLDER_AUTOMOBILE_TEST = "test";


   /**
    * Creates a test for ordinal classification algorithms.
    */
   public AOrdinalClassificationTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   /**
    * Creates a test for ordinal classification algorithms with the additional path to the resources
    * given.
    * 
    * @param additionalResourcePath the additional path to the resources
    */
   public AOrdinalClassificationTest(String additionalResourcePath) {
      super(RESOURCE_DIRECTORY_LEVEL + additionalResourcePath);
   }


   @Override
   public List<IDataset<?, ?, ?>> getWrongDatasetList() {
      List<IDataset<?, ?, ?>> datsetList = new ArrayList<>();
      datsetList.add(this.createDatasetOutOfFile(new DefaultAbsoluteDatasetParser(), getTestRessourcePathFor(WRONG_DATASET_NAME)));
      return datsetList;
   }


   @Override
   public List<IDataset<double[], NullType, Double>> getCorrectDatasetList() {
      List<IDataset<double[], NullType, Double>> datasetList = new ArrayList<>();
      datasetList.addAll(getAutomobileDatasets(DATASET_FOLDER_AUTOMOBILE_TRAIN));
      return datasetList;
   }


   @Override
   public List<Pair<IDataset<double[], NullType, Double>, List<Double>>> getPredictionsForDatasetList() {
      List<Pair<IDataset<double[], NullType, Double>, List<Double>>> datasetList = new ArrayList<>();
      datasetList.addAll(createDatasetPredictionPairs(getAutomobileDatasets(DATASET_FOLDER_AUTOMOBILE_TEST)));
      return datasetList;
   }


   /**
    * Returns a list of {@link OrdinalClassificationDataset}s of the automobile test or train
    * dataset, according to the given folder name.
    * 
    * <p>
    * If the given folder name relates to the test folder, only a few instances of the according
    * test datasets will be considered. Therefore the array {@code testDatasetParts} contains the
    * ranges of the instances of the returned datasets: the first dimension relates to the different
    * algorithms, {@link SimpleOrdinalClassification} and
    * {@link OrdinalClassificationReductionFramework}, the second and third dimensions give the
    * ranges of the subset that is created.
    * 
    * @param folderName the name of the folder of the automobile dataset, either test or train
    * 
    * @return a list of {@link OrdinalClassificationDataset}s of the automobile dataset
    */
   protected List<OrdinalClassificationDataset> getAutomobileDatasets(String folderName) {
      int[][][] testDatasetParts = {
            { { 30, 33 }, { 30, 33 }, { 25, 30 }, { 34, 37 }, { 0, 5 }, { 42, 43 }, { 8, 10 }, { 28, 31 }, { 28, 31 }, { 21, 25 } },
            { { 0, 3 }, { 2, 3 }, { 24, 25 }, { 16, 17 }, { 1, 2 }, { 42, 43 }, { 14, 16 }, { 28, 29 }, { 30, 31 }, { 30, 31 } } };

      List<OrdinalClassificationDataset> datasetList = new ArrayList<>();
      File folder = new File(getTestRessourcePathFor(DATASET_FOLDER_AUTOMOBILE + File.separator + folderName));
      File[] listedFiles = folder.listFiles();
      Arrays.sort(listedFiles, new FileByNameComparator());

      for (int i = 0; i < listedFiles.length; i++) {
         File file = listedFiles[i];
         OrdinalClassificationDataset dataset = (OrdinalClassificationDataset) createDatasetOutOfFile(
               new OrdinalClassificationDatasetParser(), file.getAbsolutePath());
         if (folderName.equals(DATASET_FOLDER_AUTOMOBILE_TEST)) {
            if (getTrainableAlgorithm() instanceof SimpleOrdinalClassification) {
               dataset = dataset.getPartOfDataset(testDatasetParts[0][i][0], testDatasetParts[0][i][1]);
            } else if (getTrainableAlgorithm() instanceof OrdinalClassificationReductionFramework) {
               dataset = dataset.getPartOfDataset(testDatasetParts[1][i][0], testDatasetParts[1][i][1]);
            }
         }
         datasetList.add(dataset);
      }
      return datasetList;
   }


   /**
    * Returns a pair of a dataset and a list of its ratings.
    * 
    * @param dataset the dataset to put into the pair
    * 
    * @return a pair of the given dataset and its ratings
    */
   protected Pair<IDataset<double[], NullType, Double>, List<Double>> createDatasetPredictionPair(OrdinalClassificationDataset dataset) {
      return Pair.of(dataset, getRatingsForDataset(dataset));
   }


   /**
    * Returns a list of pairs of a dataset and their ratings.
    * 
    * @param datasetList the list of datasets pair with their ratings
    * 
    * @return a list of pairs of the given datasets and their ratings
    */
   protected List<Pair<IDataset<double[], NullType, Double>, List<Double>>> createDatasetPredictionPairs(
         List<OrdinalClassificationDataset> datasetList) {
      List<Pair<IDataset<double[], NullType, Double>, List<Double>>> datasetRatingPairList = new ArrayList<>();
      for (int i = 0; i < datasetList.size(); i++) {
         datasetRatingPairList.add(createDatasetPredictionPair(datasetList.get(i)));
      }
      return datasetRatingPairList;
   }


   /**
    * Returns a sorted list of all ratings, in the same order as the instances are in the dataset.
    * 
    * @param dataset the dataset to get the ratings from
    * 
    * @return an sorted list of all ratings of the instances in the given dataset
    */
   private List<Double> getRatingsForDataset(OrdinalClassificationDataset dataset) {
      List<Double> ratings = new ArrayList<>();
      for (int i = 0; i < dataset.getNumberOfInstances(); i++) {
         OrdinalClassificationInstance instance = dataset.getInstance(i);
         ratings.add(instance.getRating());
      }
      return ratings;
   }

}


class FileByNameComparator implements Comparator<File> {
   @Override
   public int compare(File o1, File o2) {
      return o1.getName().compareTo(o2.getName());
   }
}
