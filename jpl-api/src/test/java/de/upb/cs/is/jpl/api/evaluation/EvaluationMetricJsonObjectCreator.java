package de.upb.cs.is.jpl.api.evaluation;


import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;


/**
 * Required to create the evaluation metric json object with the name and metric parameters for the
 * evaluation to be implemented.
 * 
 * @author Pritha Gupta
 *
 */
public class EvaluationMetricJsonObjectCreator {

   @Expose
   private String name;

   @Expose
   private JsonObject parameters;


   /**
    * Creates the EvaluationMetricElement with name and the parameter {@link JsonObject}.
    * 
    * @param name the identifier for the evaluation metric
    * @param parameters the parameter json object
    */
   public EvaluationMetricJsonObjectCreator(String name, JsonObject parameters) {
      this.name = name;
      this.parameters = parameters;
   }


   /**
    * The utility method to create json object for evaluation metric in the json configuration.
    * 
    * @param evaluationMetricElementArray the list of evaluation metric elements
    * @return the json object of the evaluation metric
    */
   public static JsonObject getEvalautionMetricJsonArray(List<EvaluationMetricJsonObjectCreator> evaluationMetricElementArray) {
      Gson gson = new GsonBuilder().create();
      JsonElement evaluationMetrics = gson.toJsonTree(evaluationMetricElementArray);
      JsonObject jsonObject = new JsonObject();
      jsonObject.add("evaluation_metrics", evaluationMetrics);
      return jsonObject;
   }


}
