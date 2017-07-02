package de.upb.cs.is.jpl.api.evaluation;


import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;


/**
 * Required to parse the evaluation metric name and its parameters for the evaluation to be
 * implemented.
 * 
 * @author Pritha Gupta
 *
 */
public class MetricDefinition {

   @SerializedName(EvaluationsKeyValuePairs.METRIC_BASELEARNER_IDENTIFIER)
   private String name;

   @SerializedName(EvaluationsKeyValuePairs.METRIC_BASELEARNER_PARAMETER)
   private JsonObject parameters;


   /**
    * The evaluation metric identifier parsed from the system configuration.
    * 
    * @return the identifier of the evaluation metric
    */
   public String getName() {
      return name;
   }


   /**
    * The parameters for the {@link JsonObject} for the evaluation metric parsed from the system
    * configuration.
    * 
    * @return the json object having parameters
    */
   public JsonObject getParameters() {
      return parameters;
   }


}
