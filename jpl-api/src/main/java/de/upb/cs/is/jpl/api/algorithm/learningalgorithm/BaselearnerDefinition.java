package de.upb.cs.is.jpl.api.algorithm.learningalgorithm;


import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.algorithm.baselearner.IBaselearnerAlgorithm;
import de.upb.cs.is.jpl.api.evaluation.EvaluationsKeyValuePairs;


/**
 * This class is required to parse the {@link IBaselearnerAlgorithm} with configuration to be used
 * for different {@link ILearningAlgorithm}.
 * 
 * @author Pritha Gupta
 *
 */
public class BaselearnerDefinition {

   @SerializedName(EvaluationsKeyValuePairs.METRIC_BASELEARNER_IDENTIFIER)
   private String name;

   @SerializedName(EvaluationsKeyValuePairs.METRIC_BASELEARNER_PARAMETER)
   private JsonObject parameters;


   /**
    * The base learner identifier parsed from the system configuration.
    * 
    * @return the identifier of the evaluation metric
    */
   public String getName() {
      return name;
   }


   /**
    * The parameters for the {@link JsonObject} for the {@link IBaselearnerAlgorithm} parsed from
    * the system configuration.
    * 
    * @return the JSON object having parameters
    */
   public JsonObject getParameters() {
      return parameters;
   }


   @Override
   public String toString() {
      return "[baseLearner=" + name + "]";
   }


}

