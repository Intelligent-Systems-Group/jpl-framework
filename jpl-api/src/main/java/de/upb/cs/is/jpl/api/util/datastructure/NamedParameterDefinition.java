package de.upb.cs.is.jpl.api.util.datastructure;


import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;


/**
 * This class represents a named parameter definition in the form of a name for the definition and a
 * {@link JsonObject} holding the according parameters for the definition.
 * 
 * @author Alexander Hetzer
 *
 */
public class NamedParameterDefinition {

   @SerializedName("identifier")
   private String name;

   @SerializedName("parameters")
   private JsonObject parameters;


   /**
    * Returns the identifier of this definition.
    * 
    * @return the identifier of this definition
    */
   public String getIdentifier() {
      return name;
   }


   /**
    * Returns the parameters of this definition as a {@link JsonObject}.
    * 
    * @return the parameters of this definition
    */
   public JsonObject getParameters() {
      return parameters;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((name == null) ? 0 : name.hashCode());
      result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      NamedParameterDefinition other = (NamedParameterDefinition) obj;
      if (name == null) {
         if (other.name != null)
            return false;
      } else if (!name.equals(other.name))
         return false;
      if (parameters == null) {
         if (other.parameters != null)
            return false;
      } else if (!parameters.equals(other.parameters))
         return false;
      return true;
   }


}
