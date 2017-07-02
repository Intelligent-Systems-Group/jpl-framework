package de.upb.cs.is.jpl.cli.command.evaluatealgorithms;


import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;


/**
 * Required to create the evaluation metric json object with the name and metric parameters for the
 * evaluation to be implemented.
 * 
 * @author Pritha Gupta
 *
 */
public class EvaluationMetricJsonElement {

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
   public EvaluationMetricJsonElement(String name, JsonObject parameters) {
      this.name = name;
      this.parameters = parameters;
   }


}
