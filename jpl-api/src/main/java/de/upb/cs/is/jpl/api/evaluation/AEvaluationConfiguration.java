package de.upb.cs.is.jpl.api.evaluation;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.configuration.json.AJsonConfiguration;
import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.JsonParsingFailedException;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;
import de.upb.cs.is.jpl.api.metric.EMetric;
import de.upb.cs.is.jpl.api.metric.IMetric;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This is an abstract class containing configuration for the evaluation that is running on current
 * command. It can be extended for different types of evaluations. You should always call up to your
 * superclass when implementing these methods:
 * <ul>
 * <li>validateParameters()</li>
 * <li>copyValues(IJsonConfiguration configuration)</li>
 * </ul>
 * 
 * @author Pritha Gupta
 *
 */
public abstract class AEvaluationConfiguration extends AJsonConfiguration {

   private static final String DEFAULT_OUTPUT_FILE = "jpl-output.txt";
   private static final String PATH_TO_DEFAULT_CONFIGURATION_LOCATION = "evaluation";

   private static final String WARNING_METRIC_IS_ALREADY_PART_OF_THIS_CONFIGURATION = "Metric '%s' is already part of this configuration.";
   private static final String EVALUATION_SETTING_NOT_SET_WARNING_MESSAGE = "Evaluation setting is not set not set number: %s";
   private static final String EVALUATION_METRIC_IDENTIFER_NULL = "The provided metric identifier is invalid or null";
   private static final String VALIDATION_EVALUATIONMETRICS_ERROR_MESSAGE = "A value for the \"%s\" metric identifier is invalid";
   private static final String EVALUATION_NOTEXIST_FOR_LEARNINGPROBLEM_ERROR_MESSAGE = "A value for the \"%s\" metric identifier is valid but it doesn't exist for learning problem: %s";
   private static final String EVALUATION_IDENTIFIER_NULL_ERROR_MESSAGE = "The metric identifier is not parsed from the evaluation_metric Json string.";

   private static final Logger logger = LoggerFactory.getLogger(AEvaluationConfiguration.class);
   private transient List<Pair<Integer, List<EvaluationSetting>>> setNumberWithEvaluationSettings;
   private transient List<String> metricIdentifiers;
   protected transient ELearningProblem eLearningProblem;
   protected transient String outputFilePath = DEFAULT_OUTPUT_FILE;
   protected List<IMetric<?, ?>> metrics;
   @SerializedName(EvaluationsKeyValuePairs.EVALUATION_METRIC_ARRAY_IDENTIFIER)
   protected List<MetricDefinition> metricsDefinitions;


   /**
    * Creates an abstract evaluations configuration and initialize it with default configuration
    * provided in the file.
    * 
    * @param defaultConfigurationFileName the default configuration file name
    */
   protected AEvaluationConfiguration(String defaultConfigurationFileName) {
      super(defaultConfigurationFileName, PATH_TO_DEFAULT_CONFIGURATION_LOCATION);
      setNumberWithEvaluationSettings = new ArrayList<>();
      metricIdentifiers = new ArrayList<>();
      metricsDefinitions = new ArrayList<>();
      outputFilePath = DEFAULT_OUTPUT_FILE;
      metrics = new ArrayList<>();
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      if (metricsDefinitions != null && !metricsDefinitions.isEmpty()) {
         getMetricIdentifiers().clear();
         for (int i = 0; i < metricsDefinitions.size(); i++) {
            String metricIdentifier = metricsDefinitions.get(i).getName();
            if (metricIdentifier != null) {
               checkEvaluationMetricIdentifierValidity(metricIdentifier);
               addEvaluationMetric(metricIdentifier);
            } else {
               logger.error(EVALUATION_IDENTIFIER_NULL_ERROR_MESSAGE);
               throw new ParameterValidationFailedException(EVALUATION_IDENTIFIER_NULL_ERROR_MESSAGE);
            }
         }
      }

   }


   /**
    * This method checks of the provided evaluation metric identifier is defined in the class
    * {@link EMetric}.
    * 
    * @param evaluationMetricIdentifier the evaluation metric identifier to be checked
    * @throws ParameterValidationFailedException if the evaluation metric identifier specified in
    *            the configuration is not present in {@link EMetric}
    */
   private void checkEvaluationMetricIdentifierValidity(String evaluationMetricIdentifier) throws ParameterValidationFailedException {
      EMetric evaluationMetric = EMetric.getEEvaluationMetricByProblemAndIdentifier(eLearningProblem, evaluationMetricIdentifier);
      if (evaluationMetric == null) {
         if (!EMetric.getMetricIdentifiers().contains(evaluationMetricIdentifier)) {
            String errorMessage = String.format(VALIDATION_EVALUATIONMETRICS_ERROR_MESSAGE, evaluationMetricIdentifier);
            logger.error(errorMessage);
            throw new ParameterValidationFailedException(errorMessage);
         }
         String errorMessage = String.format(EVALUATION_NOTEXIST_FOR_LEARNINGPROBLEM_ERROR_MESSAGE, evaluationMetricIdentifier,
               eLearningProblem.getLearningProblemIdentifier());
         logger.error(errorMessage);
         throw new ParameterValidationFailedException(errorMessage);
      }

   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      AEvaluationConfiguration castedConfiguration = (AEvaluationConfiguration) configuration;
      if (castedConfiguration.metricsDefinitions != null && !castedConfiguration.metricsDefinitions.isEmpty()) {
         this.metricsDefinitions = CollectionsUtils.getDeepCopyOf(castedConfiguration.metricsDefinitions);
      }
   }


   @Override
   protected JsonObject getDefaultConfigurationFileAsJsonObject() throws JsonParsingFailedException {
      return super.getDefaultConfigurationFileAsJsonObject().getAsJsonObject(DEFAULT_PARAMETER_VALUES);
   }


   /**
    * Adds an instance of {@link ILearningModel} obtained by training the given learning algorithm
    * on the given dataset to the system configuration.
    * 
    * @param setNumber the set number
    * @param evaluationSetting the evaluationSetting to be added
    * @throws NullPointerException if the dataset and learningAlgorithm both are {@code null}
    */
   public void addEvaluationSettingsWithSetNumber(final int setNumber, List<EvaluationSetting> evaluationSetting) {
      if (evaluationSetting != null) {
         setNumberWithEvaluationSettings.add(Pair.of(setNumber, evaluationSetting));
      } else {
         throw new NullPointerException(String.format(EVALUATION_SETTING_NOT_SET_WARNING_MESSAGE, setNumber));
      }

   }


   /**
    * Get the output file path in form of a string, where full evaluation output needs to be stored.
    * 
    * @return the file path evaluation output needs to be stored
    */
   public String getOutputFilePath() {
      return outputFilePath;
   }


   /**
    * Sets the output file path in form of a string, where full evaluation output needs to be
    * stored.
    * 
    * @param outputFilePath the file path evaluation output needs to be stored
    */
   public void setOutputFilePath(String outputFilePath) {
      this.outputFilePath = outputFilePath;
   }


   /**
    * Returns the {@link Map} linking a learning model to a learning algorithm and dataset
    * combination.
    *
    * @return the map of pair of learning algorithm, dataset and learning model
    */
   public List<Pair<Integer, List<EvaluationSetting>>> getListOfEvaluationSettingsWithSetNumber() {
      return setNumberWithEvaluationSettings;
   }


   /**
    * 
    * Returns the {@link List} of evaluation metric identifiers of type {@link String}.
    * 
    * @return the list of the evaluation metric identifiers
    */
   public List<String> getMetricIdentifiers() {
      return metricIdentifiers;
   }


   /**
    * Sets the {@link List} of evaluation metric identifiers of type {@link String}.
    * 
    * @param metricIdentifiers the list of the evaluation metric identifiers to set
    */
   public void setEvaluationMetricIdentifiers(List<String> metricIdentifiers) {
      this.metricIdentifiers = CollectionsUtils.getDeepCopyOf(metricIdentifiers);
   }


   /**
    * This function is used to add the provided evaluation metric identifier to the current
    * {@link List} of evaluation metric identifiers. A warning is logged if the metric identifier is
    * {@code null} or empty.
    * 
    * @param metricIdentifer the identifier {@link String} to be added in the {@code List} of
    *           identifiers
    * @throws NullPointerException if the evaluationMetricIdentifer is {@code null}
    * 
    */
   public void addEvaluationMetric(String metricIdentifer) {
      if (metricIdentifer != null && !metricIdentifer.isEmpty()) {
         metricIdentifiers.add(metricIdentifer);
      } else {
         logger.warn(EVALUATION_METRIC_IDENTIFER_NULL);
         throw new NullPointerException(EVALUATION_METRIC_IDENTIFER_NULL);
      }
   }


   /**
    * Returns the {@link Enum} learning problem associated with current evaluation.
    * 
    * @return the enum for learning problem associated with the configuration
    */
   public ELearningProblem getELearningProblem() {
      return eLearningProblem;
   }


   /**
    * Sets the {@link Enum} learning problem associated with current evaluation's configuration.
    * 
    * @param eLearningProblem the enum for learning problem
    */
   public void setELearningProblem(ELearningProblem eLearningProblem) {
      this.eLearningProblem = eLearningProblem;
   }


   /**
    * Returns the list of evaluation identifiers with parameters.
    * 
    * @return the list of evaluation identifiers with parameters
    */
   public List<MetricDefinition> getMetricDefinitions() {
      return metricsDefinitions;
   }


   /**
    * Returns the list of the evaluation metrics.
    * 
    * @return the list of metrics set in the configuration
    */
   public List<IMetric<?, ?>> getMetrics() {
      return metrics;
   }


   /**
    * Sets the list of the {@link IMetric}.
    * 
    * @param metrics the list of metrics set in the configuration
    */
   public void setMetrics(List<IMetric<?, ?>> metrics) {
      this.metrics = metrics;
   }


   /**
    * Add the provided metric to the list of the {@link IMetric}.
    * 
    * @param metric the metrics to be added in the list in the configuration
    */
   public void addMetric(IMetric<?, ?> metric) {
      if (metric != null) {
         if (!metrics.contains(metric)) {
            metrics.add(metric);
         } else {
            logger.warn(String.format(WARNING_METRIC_IS_ALREADY_PART_OF_THIS_CONFIGURATION, metric.toString()));
         }
      } else {
         logger.warn(EVALUATION_METRIC_IDENTIFER_NULL);
         throw new NullPointerException(EVALUATION_METRIC_IDENTIFER_NULL);
      }
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((metricsDefinitions == null) ? 0 : metricsDefinitions.hashCode());
      result = prime * result + ((eLearningProblem == null) ? 0 : eLearningProblem.hashCode());
      result = prime * result + ((metricIdentifiers == null) ? 0 : metricIdentifiers.hashCode());

      return result;
   }


   @Override
   public boolean equals(Object secondObject) {
      if (super.equals(secondObject) && secondObject instanceof AEvaluationConfiguration) {
         AEvaluationConfiguration castedObject = AEvaluationConfiguration.class.cast(secondObject);
         if (metricsDefinitions.equals(castedObject.metricsDefinitions) && eLearningProblem == castedObject.eLearningProblem
               && metricIdentifiers.equals(castedObject.metricIdentifiers)) {
            return true;
         }
      }
      return false;
   }

}
