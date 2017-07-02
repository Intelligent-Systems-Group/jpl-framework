package de.upb.cs.is.jpl.cli.core.systemconfiguration;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.dataset.DatasetFile;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;
import de.upb.cs.is.jpl.api.math.RandomGenerator;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * The SystemConfiguration class store important global state information about the system that is
 * crucial to be kept consistently and coherently. This class follows the singleton pattern and
 * consist of a reference to itself via private member variable {@code systemConfiguration}.
 * 
 * @author Pritha Gupta
 * @author Sebastian Osterbrink
 * @author Andreas Kornelsen
 * @author Sebastian Gottschalk
 */
public class SystemConfiguration {


   /** Singleton variable of the class itself. */
   private static SystemConfiguration systemConfiguration;

   /**
    * A list of DatasetFiles for the learning algorithms. Each DatasetFile could have multiple
    * IDataset depending on the chosen algorithms.
    */
   private List<DatasetFile> datasetFiles = new ArrayList<>();

   /**
    * A learning problem which should be solved by the applied procedure. Only one learning problem
    * for example 'rank_aggregation' {@link ELearningProblem#RANK_AGGREGATION} can be resolved with
    * a configuration.
    */
   private ELearningProblem learningProblem;

   /** The list of learning algorithms which should be trained and evaluated. */
   private List<ILearningAlgorithm> learningAlgorithms = new ArrayList<>();

   /** A list of algorithms with parameters as a json array. */
   private JsonArray learningAlgorithmConfiguration;

   /**
    * Defines the parameters of the evaluation technique to apply with list of evaluation metrics
    * and its parameters.
    */
   private JsonObject evaluation;

   /**
    * Provides the mapping between a learning model, a pair of learning algorithm and a dataset.
    */
   private Map<Pair<ILearningAlgorithm, IDataset<?, ?, ?>>, ILearningModel<?>> learningAlgorithmDatasetPairToLearningModelMap = new HashMap<>();

   /**
    * Path to the file in which the output of the applied algorithms and evaluations should be
    * stored.
    */
   private String outputFilePath;

   /**
    * The random seed for the {@link RandomGenerator}
    */
   private long randomSeed = RandomGenerator.DEFAULT_SEED;


   /**
    * Creates a new empty system configuration.
    */
   private SystemConfiguration() {
      init();
   }


   /**
    * Get the singleton instance of {@link SystemConfiguration}.
    *
    * @return systemConfiguration the singleton instance of {@link SystemConfiguration}
    */
   public static SystemConfiguration getSystemConfiguration() {
      if (systemConfiguration == null) {
         systemConfiguration = new SystemConfiguration();
      }
      return systemConfiguration;
   }


   /**
    * Resets the system configuration to its initial status.
    */
   public void resetSystemConfiguration() {
      getSystemConfiguration().init();
   }


   /**
    * Initializes the system configuration. Each member variable should be initialized in this
    * method.
    */
   private void init() {
      datasetFiles = new ArrayList<>();
      learningProblem = null;
      learningAlgorithms = new ArrayList<>();
      learningAlgorithmConfiguration = new JsonArray();
      evaluation = new JsonObject();
      learningAlgorithmDatasetPairToLearningModelMap = new HashMap<>();
      outputFilePath = StringUtils.EMPTY_STRING;
   }


   /**
    * Initializes the random generator with the given seed.
    * 
    * @param seed the random seed
    */
   public void setSeed(long seed) {
      randomSeed = seed;
      RandomGenerator.initializeRNG(seed);
   }


   /**
    * Returns the value of the random seed used in this system configuration.
    * 
    * @return the random seed for the random generator
    */
   public long getSeed() {
      return randomSeed;
   }


   /**
    * Add an instance of {@link ILearningAlgorithm} to system configuration.
    *
    * @param learningAlgorithm the learning algorithm to be added
    */
   public void addLearningAlgorithm(final ILearningAlgorithm learningAlgorithm) {
      learningAlgorithms.add(learningAlgorithm);
   }


   /**
    * Resets the list of learning algorithms to an empty state.
    */
   public void resetLearningAlgorithmsList() {
      learningAlgorithms.clear();
   }


   /**
    * Add an instance of {@link ILearningModel} obtained by training the given learning algorithm on
    * the given dataset to the system configuration.
    *
    * @param learningModel the learning model obtained by the training process
    * @param learningAlgorithm the learning algorithm producing the model during training on the
    *           given dataset
    * @param dataset the dataset the learning algorithm was trained on
    */
   public void addModelForAlgorithmDatasetPair(final ILearningModel<?> learningModel, final ILearningAlgorithm learningAlgorithm,
         final IDataset<?, ?, ?> dataset) {
      Pair<ILearningAlgorithm, IDataset<?, ?, ?>> learningAlgorithmDatasetPair = Pair.of(learningAlgorithm, dataset);
      learningAlgorithmDatasetPairToLearningModelMap.put(learningAlgorithmDatasetPair, learningModel);
   }


   /**
    * Clears the mapping linking a learning model to a learning algorithm and dataset combination.
    */
   public void clearLearningAlgorithmDatasetPairToLearningModelMap() {
      learningAlgorithmDatasetPairToLearningModelMap.clear();
   }


   /**
    * Returns the list of learning algorithms.
    *
    * @return the list of learning algorithms
    */
   public List<ILearningAlgorithm> getLearningAlgorithms() {
      return learningAlgorithms;
   }


   /**
    * Adds the given dataset file to the system configuration.
    *
    * @param datasetFile the dataset file to be added
    * @return {@code true} if the dataset file was added, {@code false} if it already exists
    */
   public boolean addDatasetFile(DatasetFile datasetFile) {
      if (datasetFiles.contains(datasetFile)) {
         return false;
      }
      return datasetFiles.add(datasetFile);
   }


   /**
    * Removes the given dataset file from the system configuration.
    *
    * @param datasetFile the dataset file to be removed
    * @return {@code true} if the dataset file was removed, {@code false} if it was not in the list
    */
   public boolean removeDatasetFile(DatasetFile datasetFile) {
      return datasetFiles.remove(datasetFile);
   }


   /**
    * Returns the learning problem specified in the system configuration.
    *
    * @return the {@link ELearningProblem} Enum specified in the system configuration
    */
   public ELearningProblem getLearningProblem() {
      return learningProblem;
   }


   /**
    * Replaces the current learning problem with the given one.
    *
    * @param learningProblem the learning problem used as a replacement
    */
   public void setLearningProblem(ELearningProblem learningProblem) {
      this.learningProblem = learningProblem;
   }


   /**
    * Sets the json algorithm configuration json array.
    *
    * @param learningAlgorithmConfiguration the new json algorithm configuration array
    * 
    */
   public void setJsonAlgorithmConfiguration(JsonArray learningAlgorithmConfiguration) {
      this.learningAlgorithmConfiguration = learningAlgorithmConfiguration;
   }


   /**
    * Returns the learning algorithms configuration json array.
    *
    * @return the learning algorithms configuration json array
    */
   public JsonArray getLearningAlgorithmsConfigurationJsonArray() {
      return learningAlgorithmConfiguration;
   }


   /**
    * Set the value of json evaluation configuration.
    * 
    * @param evaluation the new json evaluation configuration
    */
   public void setJsonEvaluationConfiguration(JsonObject evaluation) {
      this.evaluation = evaluation;
   }


   /**
    * Returns the evaluation configuration json object.
    * 
    * @return the evaluation configuration json object
    */
   public JsonObject getJsonEvaluationConfiguration() {
      return evaluation;
   }


   /**
    * Returns the path to the output file.
    *
    * @return the path to the output file
    */
   public String getOutputFilePath() {
      return outputFilePath;
   }


   /**
    * Replaces the current path to the output file with the given one.
    *
    * @param outputFilePath the path to the new output file
    */
   public void setOutputFilePath(String outputFilePath) {
      this.outputFilePath = outputFilePath;
   }


   /**
    * Returns the list of dataset files stored in the system configuration.
    * 
    * @return the list of dataset files stored in the system configuration
    */
   public List<DatasetFile> getDatasetFiles() {
      return datasetFiles;
   }

}
