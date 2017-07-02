package de.upb.cs.is.jpl.api.evaluation;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * This class contains JSON key values four ways to evaluate or test for each problem
 * use_training_dataset, supplied_testset, cross_validation and percentage_split.
 * 
 * @author Pritha Gupta
 *
 */
public class EvaluationsKeyValuePairs {

   /**
    * The key value to identify use_training_dataset identifier for Evaluation.
    */
   public static final String EVALUATION_USE_TRAINING_DATASET_IDENTIFIER = "use_training_dataset";

   /**
    * The key value to identify supplied_testset identifier for Evaluation.
    */
   public static final String EVALUATION_SUPPLIED_TEST_SET_IDENTIFIER = "supplied_testset";

   /**
    * The key value to identify cross_validation identifier for Evaluation.
    */
   public static final String EVALUATION_CROSS_VALIDATION_IDENTIFIER = "cross_validation";

   /**
    * The key value to identify percentage_split identifier for Evaluation.
    */
   public static final String EVALUATION_PERCENTAGE_SPLIT_IDENTIFIER = "percentage_split";

   /**
    * The key value to identify the parameters array for the evaluation metrics in {@code JSON}
    * files.
    */
   public static final String METRIC_BASELEARNER_PARAMETER = "parameters";

   /**
    * The key value to identify the name or identifier for the evaluation metrics in {@code JSON}
    * files.
    */
   public static final String METRIC_BASELEARNER_IDENTIFIER = "name";

   /**
    * The key value to identify the parameters array for the learning algorithm in {@code JSON}
    * files.
    */
   public static final String METRIC_ALGORITHM_PARAMETER = "parameters";

   /**
    * The key value to identify the name or identifier for the learning algorithm {@code JSON}
    * files.
    */
   public static final String METRIC_ALGORITHM_IDENTIFIER = "name";

   /**
    * The key value to identify the array of for the evaluation metrics in {@code JSON} files.
    */
   public static final String EVALUATION_METRIC_ARRAY_IDENTIFIER = "evaluation_metrics";

   /**
    * The key value to identify the evaluation identifier in {@code JSON} files.
    */
   public static final String EVALUATION_NAME = "evaluation_name";

   /**
    * The key value to identify the folds for cross validation in {@code JSON} files.
    */
   public static final String FOLDS_CROSS_VALIDATION = "folds";

   /**
    * The key value to identify the percentage for splits in {@code JSON} files.
    */
   public static final String PERCENTAGE_FOR_EVALUATION = "percentage";
   /**
    * The key value to identify number of pair of training and testing dataset to be generated with
    * provided percentage split in {@code JSON}.
    */
   public static final String NUMBER_OF_ITERATIONS_FOR_PERCENATGE_SPLIT = "num_of_iterations";
   /**
    * The key value to identify number of pair of training and testing dataset to be generated with
    * provided percentage split in {@code JSON}.
    */
   public static final String SUPPLIED_TEST_SET_FOR_DATASET = "supplied_test_set_with_dataset";


   /**
    * Hides the public constructor.
    */
   private EvaluationsKeyValuePairs() {
      throw new IllegalAccessError(StringUtils.EXCEPTION_MESSAGE_ACCESS_ERROR);
   }


   /**
    * There are four ways to evaluate any algorithms. This is the static list containing the
    * identifiers for the evaluations.
    * 
    * @return the list of evaluation identifiers
    */
   public static List<String> getEvaluationIdentifiers() {
      return new ArrayList<>(Arrays.asList(EVALUATION_USE_TRAINING_DATASET_IDENTIFIER, EVALUATION_SUPPLIED_TEST_SET_IDENTIFIER,
            EVALUATION_CROSS_VALIDATION_IDENTIFIER, EVALUATION_PERCENTAGE_SPLIT_IDENTIFIER));
   }


}
