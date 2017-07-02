package de.upb.cs.is.jpl.api.algorithm;


import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;


/**
 * The class provided utility functions that can be used for providing the evaluation and evaluation
 * metric test.
 * 
 * @author Pritha Gupta
 *
 */
public class BaseLearnerJsonObjectCreator {

   @Expose
   private String name;

   @Expose
   private JsonObject parameters;

   private static final String PARAMETER_BASELEARNER = "base_learner";


   /**
    * Creates the EvaluationMetricElement with name and the parameter {@link JsonObject}.
    * 
    * @param name the identifier for the evaluation metric
    * @param parameters the parameter json object
    */
   public BaseLearnerJsonObjectCreator(String name, JsonObject parameters) {
      this.name = name;
      this.parameters = parameters;
   }


   /**
    * The utility method to create json object for evaluation metric in the json configuration.
    * 
    * @param baseLearnerObject the list of evaluation metric elements
    * @return the json object of the evaluation metric
    */
   public static JsonObject getBaseLearnerJsonObject(BaseLearnerJsonObjectCreator baseLearnerObject) {
      Gson gson = new GsonBuilder().create();
      JsonElement baseLearners = gson.toJsonTree(baseLearnerObject);
      JsonObject jsonObject = new JsonObject();
      jsonObject.add(PARAMETER_BASELEARNER, baseLearners);
      return jsonObject;
   }


   /**
    * The utility method to create json object for evaluation metric in the json configuration.
    * 
    * @param baseLearnerObjectArray the list of evaluation metric elements
    * @return the json object of the evaluation metric
    */
   public static JsonObject getBaseLearnerJsonArray(List<BaseLearnerJsonObjectCreator> baseLearnerObjectArray) {
      Gson gson = new GsonBuilder().create();
      JsonElement baseLearners = gson.toJsonTree(baseLearnerObjectArray);
      JsonObject jsonObject = new JsonObject();
      jsonObject.add(PARAMETER_BASELEARNER, baseLearners);
      return jsonObject;
   }

}
