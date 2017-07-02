package de.upb.cs.is.jpl.api.evaluation;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;
import de.upb.cs.is.jpl.api.dataset.DatasetFile;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.math.linearalgebra.DenseDoubleVector;
import de.upb.cs.is.jpl.api.metric.IMetric;
import de.upb.cs.is.jpl.api.util.CollectionsUtils;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This output generator is responsible for generating nicely formatted output for the
 * {@link AEvaluation}, which is provided evaluation command result interpretation process. This
 * class also provide util methods for Evaluation.
 * 
 * @author Pritha Gupta
 *
 */
public class EvaluationsOutputGenerator {
   private static final String HEADING_FOR_MEAN_AND_STD = "Mean Values And Standard Deviations";
   private static final String MEAN_AND_STD_OUTPUT = "Mean: %f and Std: %f";
   private static final String NOT_EVALUATED = "Not Evaluated check error above";
   private static final String EVALUATION_OUTPUT = "Extra Information";
   private static final String DATASET = "Dataset";
   private static final String CONFIGURATION = "Configuration";
   private static final String LEARNING_ALGORITHMS = "LearningAlgorithms";
   private List<ILearningAlgorithm> learningAlgorithms;
   private List<IDataset<?, ?, ?>> datasets;
   private List<IMetric<?, ?>> metrics;
   private List<EvaluationResult> evaluationResults;
   private Map<ILearningAlgorithm, Map<IMetric<?, ?>, double[]>> meanAndStandardDeviation;
   private List<IMetric<?, ?>> doubleMetrics = new ArrayList<>();


   /**
    * Creates a new {@link EvaluationsOutputGenerator} with empty lists.
    */
   public EvaluationsOutputGenerator() {
      resetOutputGenerator();
   }


   /**
    * Creates a new {@link EvaluationsOutputGenerator}, with list of {@link ILearningAlgorithm},
    * {@link DatasetFile} and list of {@link IMetric}.
    * 
    * @param learningAlgorithms the list of algorithms
    * @param evaluationMetrics the list of evaluation metrics
    * 
    */
   public EvaluationsOutputGenerator(List<ILearningAlgorithm> learningAlgorithms, List<IMetric<?, ?>> evaluationMetrics) {
      resetOutputGenerator();
      this.learningAlgorithms = CollectionsUtils.getDeepCopyOf(learningAlgorithms);
      this.metrics = CollectionsUtils.getDeepCopyOf(evaluationMetrics);
   }


   /**
    * Resets the {@link EvaluationsOutputGenerator} object with empty lists.
    */
   private void resetOutputGenerator() {
      this.learningAlgorithms = new ArrayList<>();
      this.datasets = new ArrayList<>();
      this.metrics = new ArrayList<>();
      this.evaluationResults = new ArrayList<>();
      this.meanAndStandardDeviation = new HashMap<>();
      this.doubleMetrics = new ArrayList<>();
   }


   /**
    * Sets the list of {@link DatasetFile} on which evaluations is done.
    * 
    * @param datasets the list of datasets on which evaluation is done
    */
   public void setDatasets(List<IDataset<?, ?, ?>> datasets) {
      this.datasets = CollectionsUtils.getDeepCopyOf(datasets);
      Collections.sort(this.datasets, (o1, o2) -> o1.getUniqueStringIdentifyingDataset().compareTo(o2.getUniqueStringIdentifyingDataset()));
   }


   /**
    * The evaluation results for which you want the output.
    * 
    * @param evaluationResults the evaluation results to set
    */
   public void setEvaluationResults(List<EvaluationResult> evaluationResults) {
      this.evaluationResults = CollectionsUtils.getDeepCopyOf(evaluationResults);
   }


   /**
    * Generated the evaluation output for the provided evaluation.
    * 
    * @param valuesColumnWidth the column width
    * @param firstColumnWidth the first column width
    * @return the string output to be printed
    */
   public String generateEvaluationOutputForEvaluationResults(int valuesColumnWidth, int firstColumnWidth) {
      generateEmptyStandardDeviationAndMeanMap();
      int updatedValuesColumnWidth = updateValuesColumnWidth(valuesColumnWidth);
      StringBuilder generatedEvaluationOutput = new StringBuilder();
      int numberofColumns = learningAlgorithms.size();
      int totalWidth = firstColumnWidth + numberofColumns * updatedValuesColumnWidth;
      generatedEvaluationOutput.append(StringUtils.LINE_BREAK);
      generatedEvaluationOutput.append(StringUtils.repeat(StringUtils.EQUALS_SIGN, totalWidth));
      generatedEvaluationOutput.append(StringUtils.LINE_BREAK);
      generatedEvaluationOutput.append(getCenteredString(LEARNING_ALGORITHMS, firstColumnWidth));
      for (ILearningAlgorithm learningALgorithm : learningAlgorithms) {
         generatedEvaluationOutput.append(getCenteredString(learningALgorithm.toString(), updatedValuesColumnWidth));
      }
      generatedEvaluationOutput.append(StringUtils.LINE_BREAK);

      generatedEvaluationOutput.append(getCenteredString(CONFIGURATION, firstColumnWidth));

      List<String[]> learningAlgorithmsOutputLines = new ArrayList<>();
      int max = 0;
      for (ILearningAlgorithm learningALgorithm : learningAlgorithms) {
         String configurationString = StringUtils.ROUND_BRACKET_OPEN + learningALgorithm.getAlgorithmConfiguration().toString()
               + StringUtils.ROUND_BRACKET_CLOSE;
         String[] outputLines = splitConfigurationString(configurationString, updatedValuesColumnWidth);
         max = Math.max(max, outputLines.length);
         learningAlgorithmsOutputLines.add(outputLines);
      }
      for (int i = 0; i < max; i++) {
         for (String[] strings : learningAlgorithmsOutputLines) {
            if (strings.length > i) {
               generatedEvaluationOutput.append(getCenteredString(strings[i], updatedValuesColumnWidth));
            } else {
               generatedEvaluationOutput.append(getCenteredString(StringUtils.EMPTY_STRING, updatedValuesColumnWidth));
            }
         }
         generatedEvaluationOutput.append(StringUtils.LINE_BREAK);
         if (i != max - 1) {
            generatedEvaluationOutput.append(getCenteredString(StringUtils.EMPTY_STRING, firstColumnWidth));
         }
      }

      generatedEvaluationOutput.append(StringUtils.repeat(StringUtils.EQUALS_SIGN, totalWidth));
      for (int datasetFileIndex = 0; datasetFileIndex < datasets.size(); datasetFileIndex++) {
         generatedEvaluationOutput.append(getLearningAlgorithmsMetricResultsForOneDatasetFile(datasets.get(datasetFileIndex),
               datasetFileIndex, updatedValuesColumnWidth, firstColumnWidth, totalWidth));
      }

      generatedEvaluationOutput.append(StringUtils.LINE_BREAK);
      generatedEvaluationOutput.append(StringUtils.repeat(StringUtils.DASH, totalWidth));
      generatedEvaluationOutput.append(StringUtils.LINE_BREAK);

      generatedEvaluationOutput.append(getCenteredString(HEADING_FOR_MEAN_AND_STD, totalWidth));
      generatedEvaluationOutput.append(StringUtils.LINE_BREAK);
      generatedEvaluationOutput.append(StringUtils.repeat(StringUtils.DASH, totalWidth));
      generatedEvaluationOutput.append(StringUtils.LINE_BREAK);
      for (IMetric<?, ?> metric : doubleMetrics) {
         generatedEvaluationOutput.append(getCenteredString(metric.toString(), firstColumnWidth));
         for (ILearningAlgorithm learningAlgorithm : learningAlgorithms) {
            double[] lossesForMetricAndLAgorithm = meanAndStandardDeviation.get(learningAlgorithm).get(metric);
            DenseDoubleVector vector = new DenseDoubleVector(lossesForMetricAndLAgorithm);
            String meanAndStandardDeviationString = String.format(MEAN_AND_STD_OUTPUT, vector.mean(), vector.standardDeviation());
            generatedEvaluationOutput.append(getCenteredString(meanAndStandardDeviationString, updatedValuesColumnWidth));
         }
         generatedEvaluationOutput.append(StringUtils.LINE_BREAK);
      }
      generatedEvaluationOutput.append(getCenteredString(StringUtils.EMPTY_STRING, firstColumnWidth));
      for (int i = 0; i < learningAlgorithms.size(); i++) {
         generatedEvaluationOutput.append(getCenteredString(StringUtils.EMPTY_STRING, updatedValuesColumnWidth));
      }
      generatedEvaluationOutput.append(StringUtils.LINE_BREAK);
      generatedEvaluationOutput.append(StringUtils.repeat(StringUtils.DASH, totalWidth));
      return generatedEvaluationOutput.toString();

   }


   /**
    * It checks all the algorithms configuration strings and split them for default width and then
    * get the maximum length of the split strings.
    * 
    * @param valuesColumnWidth the default values column width
    * @return the updated value of the width of values column
    */
   private int updateValuesColumnWidth(int valuesColumnWidth) {
      int maxWidth = valuesColumnWidth;
      for (ILearningAlgorithm learningALgorithm : learningAlgorithms) {
         String configurationString = StringUtils.ROUND_BRACKET_OPEN + learningALgorithm.getAlgorithmConfiguration().toString()
               + StringUtils.ROUND_BRACKET_CLOSE;
         String[] outputLines = splitConfigurationString(configurationString, valuesColumnWidth);
         for (String output : outputLines) {
            if (output.length() >= maxWidth) {
               maxWidth = output.length();
            }
         }
      }
      maxWidth = maxWidth + 10;
      return maxWidth;

   }


   /**
    * Returns one row of losses for {@link IMetric}s for all the {@link ILearningAlgorithm}s
    * evaluated for one {@link DatasetFile} to be printed for in the console in from of a string.
    * 
    * @param file the {@link DatasetFile}
    * @param datasetFileIndex the index of the dataset file in the list of {@link DatasetFile}s.
    * @param valuesColumnWidth the column width
    * @param firstColumnWidth the first column width
    * @param totalWidth the total width of the table
    * @return the string output to be printed for all the algorithms
    */
   private String getLearningAlgorithmsMetricResultsForOneDatasetFile(IDataset<?, ?, ?> dataset, int datasetFileIndex,
         int valuesColumnWidth, int firstColumnWidth, int totalWidth) {
      StringBuilder generatedEvaluationOutput = new StringBuilder();
      generatedEvaluationOutput.append(StringUtils.LINE_BREAK);
      generatedEvaluationOutput.append(StringUtils.repeat(StringUtils.DASH, totalWidth));
      generatedEvaluationOutput.append(StringUtils.LINE_BREAK);

      generatedEvaluationOutput.append(getCenteredString(DATASET, firstColumnWidth));
      generatedEvaluationOutput.append(getCenteredString(dataset.getDatasetFile().getFile().getName(), totalWidth - firstColumnWidth));
      generatedEvaluationOutput.append(StringUtils.LINE_BREAK);
      for (IMetric<?, ?> metric : metrics) {
         generatedEvaluationOutput.append(getCenteredString(metric.toString(), firstColumnWidth));
         for (ILearningAlgorithm learningAlgorithm : learningAlgorithms) {
            EvaluationResult result = getEvaluationResultForSet(learningAlgorithm, dataset);
            Map<IMetric<?, ?>, double[]> metricsWithLossArray = meanAndStandardDeviation.get(learningAlgorithm);
            if (result != null) {
               double[] losses = metricsWithLossArray.get(metric);
               losses[datasetFileIndex] = (double) result.getLossForMetric(metric);
               metricsWithLossArray.replace(metric, losses);
               generatedEvaluationOutput.append(getCenteredString(String.valueOf(result.getLossForMetric(metric)), valuesColumnWidth));
            } else {
               generatedEvaluationOutput.append(getCenteredString(NOT_EVALUATED, valuesColumnWidth));
            }
            meanAndStandardDeviation.replace(learningAlgorithm, metricsWithLossArray);
         }
         generatedEvaluationOutput.append(StringUtils.LINE_BREAK);
      }
      generatedEvaluationOutput.append(getEvaluationStringResult(dataset, valuesColumnWidth, firstColumnWidth));
      generatedEvaluationOutput.append(getCenteredString(StringUtils.EMPTY_STRING, firstColumnWidth));
      for (int i = 0; i < learningAlgorithms.size(); i++) {
         generatedEvaluationOutput.append(getCenteredString(StringUtils.EMPTY_STRING, valuesColumnWidth));
      }
      return generatedEvaluationOutput.toString();
   }


   /**
    * Returns one row of extra evaluation string output for all the {@link ILearningAlgorithm}s
    * evaluated for one {@link DatasetFile} to be printed for in the console in from of a string.
    * 
    * @param dataset the {@link IDataset}
    * @param valuesColumnWidth the column width
    * @param firstColumnWidth the first column width
    * @return the extra evaluation string output to be printed for all the algorithms
    */
   private String getEvaluationStringResult(IDataset<?, ?, ?> dataset, int valuesColumnWidth, int firstColumnWidth) {
      StringBuilder generatedEvaluationOutput = new StringBuilder();
      boolean printed = false;
      for (ILearningAlgorithm learningAlgorithm : learningAlgorithms) {
         EvaluationResult result = getEvaluationResultForSet(learningAlgorithm, dataset);
         if (result != null && !result.getExtraEvaluationInformation().isEmpty()) {
            if (!printed) {
               generatedEvaluationOutput.append(getCenteredString(EVALUATION_OUTPUT, firstColumnWidth));
               printed = true;
            }
            generatedEvaluationOutput.append(getCenteredString(String.valueOf(result.getExtraEvaluationInformation()), valuesColumnWidth));
         }
      }
      if (printed) {
         generatedEvaluationOutput.append(StringUtils.LINE_BREAK);
      }
      return generatedEvaluationOutput.toString();

   }


   /**
    * Creates the map which stores all loss array for all metrics returning double loss for each
    * algorithm. Each loss entry in the double array is the loss evaluated on each dataset.
    */
   private void generateEmptyStandardDeviationAndMeanMap() {
      EvaluationResult evaluationResult = evaluationResults.get(0);
      for (IMetric<?, ?> metric : metrics) {
         Object loss = evaluationResult.getLossForMetric(metric);
         if (loss.getClass() == Double.class) {
            doubleMetrics.add(metric);
         }
      }
      for (ILearningAlgorithm algortihm : learningAlgorithms) {
         Map<IMetric<?, ?>, double[]> metricsWithLossArray = new HashMap<>();
         for (IMetric<?, ?> metric : doubleMetrics) {
            double[] losses = new double[datasets.size()];
            metricsWithLossArray.put(metric, losses);
         }
         meanAndStandardDeviation.put(algortihm, metricsWithLossArray);
      }


   }


   /**
    * Utility method to display a string in the center of the columns in the output.
    * 
    * @param centerString the string to be centered
    * @param width width of the column
    * @return the centered string
    */
   public static String getCenteredString(String centerString, int width) {
      StringBuilder stringBuilder = new StringBuilder();

      int padding = width - (centerString.length() + 2);
      stringBuilder.append(StringUtils.BITWISEOR);
      if (padding > 0) {
         stringBuilder.append(StringUtils.repeat(StringUtils.SINGLE_WHITESPACE, padding / 2));
         stringBuilder.append(centerString);
         stringBuilder.append(StringUtils.repeat(StringUtils.SINGLE_WHITESPACE, padding / 2));
         if (padding > 0 && padding % 2 != 0)
            stringBuilder.append(StringUtils.SINGLE_WHITESPACE);
         stringBuilder.append(StringUtils.BITWISEOR);
      } else {
         stringBuilder.append(centerString);
         stringBuilder.append(StringUtils.BITWISEOR);
      }

      return stringBuilder.toString();
   }


   /**
    * This method splits the provided string into array of string as per the width of the values
    * column and last white space in the string.
    * 
    * @param configurationString the string to be split according to the width of the values column
    * @param valuesColumnWidth the width of values column
    * @return the string array with all the strings in the array
    */
   public String[] splitConfigurationString(String configurationString, int valuesColumnWidth) {
      String[] outputLines = configurationString.split(StringUtils.SINGLE_WHITESPACE);
      int[] indexes = new int[outputLines.length];
      List<String> outputs = new ArrayList<>();
      indexes[0] = configurationString.indexOf(StringUtils.SINGLE_WHITESPACE);
      for (int i = 1; i < outputLines.length - 1; i++) {
         indexes[i] = configurationString.indexOf(StringUtils.SINGLE_WHITESPACE, indexes[i - 1] + 1);
      }
      indexes[indexes.length - 1] = configurationString.length();
      int firstIndex = 0;
      for (int i = 0; i < indexes.length; i++) {
         String substring = configurationString.substring(firstIndex, indexes[i]);
         if (substring.length() > (valuesColumnWidth - 2)) {
            if (i != 0) {
               outputs.add(configurationString.substring(firstIndex, indexes[i - 1]));
               firstIndex = indexes[i - 1] + 1;
            } else {
               outputs.add(configurationString.substring(firstIndex, indexes[0]));
               firstIndex = indexes[0] + 1;
            }
         }
         if (i == indexes.length - 1) {
            substring = configurationString.substring(firstIndex, indexes[i]);
            outputs.add(substring);
         }
      }
      return outputs.toArray(new String[outputs.size()]);

   }


   /**
    * Returns the {@link EvaluationResult} for {@link ILearningAlgorithm}, evaluated on dataset
    * generated from {@link DatasetFile} and for {@link IMetric}.
    * 
    * @param learningAlgorithm the learning algorithm
    * @param file the dataset file
    * @return the evaluation result for algorithm on dataset for evaluation metric
    */
   private EvaluationResult getEvaluationResultForSet(ILearningAlgorithm learningAlgorithm, IDataset<?, ?, ?> dataset) {
      for (EvaluationResult evaluationResult : evaluationResults) {
         if (evaluationResult.getDataset().equals(dataset) && evaluationResult.getLearningAlgorithm().equals(learningAlgorithm)) {
            return evaluationResult;
         }
      }
      return null;
   }

}
