package de.upb.cs.is.jpl.api.algorithm.learningalgorithm;


import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.evaluation.EvaluationsKeyValuePairs;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * Required to parse the {@link ILearningAlgorithm} with configuration to be used for different
 * {@link ILearningAlgorithm}s
 * 
 * @author Andreas Kornelsen
 *
 */
public class AlgorithmDefinition {

   @SerializedName(EvaluationsKeyValuePairs.METRIC_ALGORITHM_IDENTIFIER)
   private String name;

   @SerializedName(EvaluationsKeyValuePairs.METRIC_ALGORITHM_PARAMETER)
   private JsonObject parameters;


   /**
    * The learning algorithm identifier parsed from the system configuration.
    * 
    * @return the identifier of the learning algorithm
    */
   public String getName() {
      return name;
   }


   /**
    * The parameters for the {@link JsonObject} for the {@link ILearningAlgorithm} parsed from the
    * system configuration.
    * 
    * @return the json object having parameters
    */
   public JsonObject getParameters() {
      return parameters;
   }


   @Override
   public String toString() {
      return StringUtils.ROUND_BRACKET_OPEN + "LearningAlgorithm=" + name + StringUtils.ROUND_BRACKET_CLOSE;
   }


}
