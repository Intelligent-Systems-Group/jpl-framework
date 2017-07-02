package de.upb.cs.is.jpl.api.evaluation;


import java.util.List;

import com.google.gson.JsonObject;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.dataset.ADatasetParser;
import de.upb.cs.is.jpl.api.dataset.DatasetFile;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.exception.evaluation.EvaluationNotCarriedOutSuccesfully;
import de.upb.cs.is.jpl.api.exception.evaluation.EvaluationResultsNotCreatedForUniquePairOfDatasetAndAlgorithm;
import de.upb.cs.is.jpl.api.exception.evaluation.TrainTestDatasetPairsNotCreated;
import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;
import de.upb.cs.is.jpl.api.metric.IMetric;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * Any evaluation corresponding to a {@link ELearningProblem} needs to implement this interface in
 * order to be able to carry out a detailed evaluation. This can be in the form of a comparison of
 * multiple algorithms or the simple evaluation of a single algorithm.
 * 
 * @author Pritha Gupta
 *
 */
public interface IEvaluation {


   /**
    * This method creates the metric list on which the evaluation has to be done and then calls the
    * method {@link IEvaluation#setupEvaluation(List, List, List)}} to setup the evaluation.
    * 
    * @param datasetPaths the list of dataset files
    * @param learningAlgorithms the list of learning algorithms
    * @throws ParameterValidationFailedException if the parameters for metric are not set correctly
    */
   public void setupEvaluation(final List<DatasetFile> datasetPaths, final List<ILearningAlgorithm> learningAlgorithms)
         throws ParameterValidationFailedException;


   /**
    * This method setups all the required variables in the {@link AEvaluationConfiguration} for the
    * current evaluation, by iterating over each pair of {@link DatasetFile} and
    * {@link ILearningAlgorithm} using the method
    * {@link IEvaluation#setupSingleEvaluationDatasetAndAlgorithm(int, DatasetFile, ILearningAlgorithm, List)}
    * .
    * 
    * @param datasetFiles the list of dataset files
    * @param learningAlgorithms the list of learning algorithms
    * @param metrics the list of metrics for which the setup has to be done
    * 
    */
   public void setupEvaluation(List<DatasetFile> datasetFiles, List<ILearningAlgorithm> learningAlgorithms, List<IMetric<?, ?>> metrics);


   /**
    * This method will create proper pair of {@link IDataset} and {@link ILearningAlgorithm} and add
    * them to the learning algorithm and dataset pair with learning model map in
    * {@link AEvaluationConfiguration} containing the {@link Pair} of the algorithm and dataset,
    * with the {@link ILearningModel} which is obtained by training the algorithm on the dataset or
    * the part of it. The object {@link IDataset} is obtained by parsing the {@link DatasetFile}
    * using the {@link ADatasetParser} corresponding to the algorithm.
    * 
    * @param setNumber the set number
    * @param datasetFile the dataset file
    * @param learningAlgorithm the algorithm which needs to be trained
    * @param evaluationMetric the list of metrics
    * @return the set number for which list of evaluations settings were created
    * 
    */
   public int setupSingleEvaluationDatasetAndAlgorithm(int setNumber, DatasetFile datasetFile, ILearningAlgorithm learningAlgorithm,
         List<IMetric<?, ?>> evaluationMetric);


   /**
    * This method will run the evaluation on each pair of {@link IDataset} and
    * {@link ILearningAlgorithm} and add them to the {@code HashMap, algorithmToDatasetMap} stored
    * in the {@link AEvaluationConfiguration}.
    * 
    * @throws EvaluationNotCarriedOutSuccesfully if evaluation is not able to get any evaluation
    *            result after running the evaluation
    */
   public void evaluate() throws EvaluationNotCarriedOutSuccesfully;


   /**
    * Runs the evaluation for one set of {@link EvaluationSetting} for which you want a combined
    * evaluation result.
    * 
    * @param evaluationSettingsForOneSet the pair of set number and list of evaluation settings to
    *           be evaluated
    * @return the combined evaluation result for running evaluations on all the settings
    * @throws EvaluationResultsNotCreatedForUniquePairOfDatasetAndAlgorithm if the set of evaluation
    *            result obtained were not evaluated for unique pair of algorithm and dataset
    * 
    */
   public EvaluationResult runEvaluationForOneSetOfEvaluationSettings(Pair<Integer, List<EvaluationSetting>> evaluationSettingsForOneSet)
         throws EvaluationResultsNotCreatedForUniquePairOfDatasetAndAlgorithm;


   /**
    * Returns the default configuration of this evaluation, initialized with values from the
    * according default configuration file.
    * 
    * @return the default configuration of this evaluation
    */
   public AEvaluationConfiguration getDefaultEvalutionConfiguration();


   /**
    * Returns the current {@link AEvaluationConfiguration} of this evaluation.
    * 
    * @return the current configuration of this evaluation
    */
   public AEvaluationConfiguration getEvaluationConfiguration();


   /**
    * This function will take the list of all {@link EvaluationResult}s and form the final
    * evaluation result to be printed as the console output.
    * 
    * @return the final result for evaluation to print
    * 
    */
   public String interpretEvaluationResult();


   /**
    * Sets the parameters of this evaluation based on the given JSON object and performs a
    * validation of the values. This method is added for super tests for evaluations.
    * 
    * @param jsonObject the JSON object containing the algorithm parameters
    * @throws ParameterValidationFailedException if the parameter validation failed
    */
   public void setParameters(JsonObject jsonObject) throws ParameterValidationFailedException;


   /**
    * Sets the evaluation configuration of this evaluation to the given configuration.
    * 
    * @param evaluationConfiguration the configuration to set
    */
   public void setEvaluationConfiguration(AEvaluationConfiguration evaluationConfiguration);


   /**
    * This method is used to get the user provided parameters for the metrics.
    * 
    * @param metricIdentifier the metric identifier
    * @return the JSON object for the parameters provided by he user for the metric identifier
    * 
    */
   public JsonObject getMetricParameterJsonObject(String metricIdentifier);


   /**
    * Splits the dataset into a list of testing and training dataset pairs by folding it into equal
    * chunks and selecting one of these as testing data and the rest as training data.
    * 
    * @param dataset the dataset on which these operations are performed
    * @return a list of test- and training-dataset pairs
    * @throws TrainTestDatasetPairsNotCreated if the dataset could not be folded
    */
   public List<Pair<IDataset<?, ?, ?>, IDataset<?, ?, ?>>> getTestTrainPairs(IDataset<?, ?, ?> dataset)
         throws TrainTestDatasetPairsNotCreated;


}
