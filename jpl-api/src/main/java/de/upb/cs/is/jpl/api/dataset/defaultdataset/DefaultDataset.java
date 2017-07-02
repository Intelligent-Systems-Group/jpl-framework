package de.upb.cs.is.jpl.api.dataset.defaultdataset;


import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.dataset.ADataset;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.exception.dataset.InvalidInstanceException;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * The Basic dataset class for handling datasets with generic ratings.
 * 
 * @author Sebastian Osterbrink
 * @param <T> the type of the ratings in this dataset. Expected values are for example
 *           {@link IVector} for absolute ratings and {@link Ranking} for relative ratings.
 */
public abstract class DefaultDataset<T> extends ADataset<double[], List<double[]>, T>


{

   private static final String THE_PROVIDED_ITEM_VECTORS_ARE_NOT_THE_SAME_AS_IN_THE_DATASET = "The provided item vectors are not the same as in the dataset.";

   private static final String ITEM = "item";

   private static final String CONTEXT = "context";

   private static final String INVALID_VECTOR_WRONG_LENGTH = "Invalid %s vector. The given %s vector has dimension %s but the dataset expects context vectors of size %s";


   protected static final String CONTEXT_ID_NOT_VALID = "The context id %s is not a valid Id.";
   protected static final String ITEM_ID_NOT_VALID = "The item id %s is not a valid Id.";

   protected List<String> contextFeatures;
   protected List<List<Double>> contextFeatureAllowedValues;
   protected List<double[]> contextVectors;

   protected List<String> itemFeatures;
   protected List<List<Double>> itemFeatureAllowedValues;
   protected List<double[]> itemVectors;

   // List of Rating Vectors
   protected List<DefaultInstance<T>> ratings;

   protected List<Double> ratingAllowedValues;


   /**
    * Returns the complete list of item vectors.
    * 
    * @return the {@link List} of item vectors
    */
   public List<double[]> getItemVectors() {
      return itemVectors;
   }


   /**
    * Returns the complete list of context vectors.
    * 
    * @return the {@link List} of context vectors
    */
   public List<double[]> getContexVectors() {
      return contextVectors;
   }


   @Override
   protected void init() {
      contextVectors = new ArrayList<>();
      contextFeatures = new ArrayList<>();
      contextFeatureAllowedValues = new ArrayList<>();
      itemVectors = new ArrayList<>();
      itemFeatures = new ArrayList<>();
      itemFeatureAllowedValues = new ArrayList<>();
      ratings = new ArrayList<>();
      ratingAllowedValues = new ArrayList<>();
   }


   @Override
   public int getNumberOfInstances() {
      return ratings.size();
   }


   @Override
   public DefaultInstance<T> getInstance(int position) {
      return ratings.get(position);
   }


   @Override
   public void addInstance(IInstance<double[], List<double[]>, T> instance) throws InvalidInstanceException {
      assertInstanceHasCorrectType(instance, DefaultInstance.class);
      ratings.add((DefaultInstance<T>) instance);
      if (instance.getContextId() == -1) {
         if (instance.getItemFeatureVectors().equals(itemVectors)) {
            throw new InvalidInstanceException(THE_PROVIDED_ITEM_VECTORS_ARE_NOT_THE_SAME_AS_IN_THE_DATASET);
         }
         contextVectors.add(instance.getContextFeatureVector());
         instance.setContextId(contextVectors.size());
      }
   }


   /**
    * Defines an entire row in the context / item matrix which contains the rating information
    * 
    * @param contextId the context Id which defines the row in the context / item matrix
    * @param rating the object with the rating information
    */
   public void addInstance(int contextId, T rating) {
      ratings.add(new DefaultInstance<T>(contextId, rating, this));
   }


   /**
    * Transforms a context ID into the corresponding properties vector.
    * 
    * @param id the id which identifies the context
    * @return an array which contains the values for the selected context
    */
   public double[] getContextVector(Integer id) {
      return contextVectors.get(id);
   }


   /**
    * Adds an context vector to the set of existing context vectors.
    * 
    * @param id the id under which this vector is saved
    * @param contextVector the vector of context properties
    * @throws InvalidInstanceException if the dimension of the context vector is not valid
    */
   public void setContextVector(Integer id, double[] contextVector) throws InvalidInstanceException {
      if (contextVector.length != contextFeatures.size()) {
         throw new InvalidInstanceException(
               String.format(INVALID_VECTOR_WRONG_LENGTH, CONTEXT, CONTEXT, contextVector.length, contextFeatures.size()));
      }
      if (id < contextVectors.size()) {
         contextVectors.set(id, contextVector);
      } else {
         double[] blankArray = new double[contextVector.length];
         while (contextVectors.size() < id) {
            contextVectors.add(blankArray);
         }
         contextVectors.add(contextVector);
      }
   }


   /**
    * Transforms an item ID into the corresponding properties vector.
    * 
    * @param id the id which identifies the item
    * @return an array which contains the values for the selected item
    */
   public double[] getItemVector(Integer id) {
      return itemVectors.get(id);
   }


   /**
    * Adds an item vector to the set of existing context vectors.
    * 
    * @param id the id under which this vector is saved
    * @param itemVector the vector of context properties
    * @throws InvalidInstanceException if the dimension of the context vector is not valid
    */
   public void setItemVector(Integer id, double[] itemVector) throws InvalidInstanceException {
      if (itemVector.length != itemFeatures.size())
         throw new InvalidInstanceException(String.format(INVALID_VECTOR_WRONG_LENGTH, ITEM, ITEM, itemVector.length, itemFeatures.size()));
      if (id < itemVectors.size()) {
         itemVectors.set(id, itemVector);
      } else {
         double[] blankArray = new double[itemVector.length];
         while (itemVectors.size() < id) {
            itemVectors.add(blankArray);
         }
         itemVectors.add(itemVector);
      }
   }


   /**
    * Getter method for the number of existing item vectors.
    * 
    * @return the number of existing items
    */
   public int getNumberOfItems() {
      return itemVectors.size();
   }


   /**
    * Getter method for the number of existing context vectors.
    * 
    * @return the number of existing contexts
    */
   public int getNumberOfContexts() {
      return contextVectors.size();
   }


   /**
    * Get the context feature at given id.
    * 
    * @param id the number of this context feature
    * @return returns the contextFeatures.
    */
   public String getContextFeature(Integer id) {
      return contextFeatures.get(id);
   }


   /**
    * Sets the context feature at the given given index provided as id.
    * 
    * @param id the id number of the context feature
    * @param contextFeature the description text of the contextFeature
    */
   public void setContextFeature(Integer id, String contextFeature) {
      if (contextFeatures.size() > id) {
         contextFeatures.set(id, contextFeature);
      } else {
         while (contextFeatures.size() < id) {
            contextFeatures.add(StringUtils.EMPTY_STRING);
         }
         contextFeatures.add(contextFeature);
      }
   }


   /**
    * Get the item feature at given index provided as id.
    * 
    * @param id the id of the item feature
    * @return the itemFeatures
    */
   public String getItemFeature(Integer id) {
      return itemFeatures.get(id);
   }


   /**
    * Sets the item feature at the given given index provided as id.
    * 
    * @param id the id number of the context feature
    * @param itemFeature the description text of the itemFeature
    */
   public void setItemFeature(Integer id, String itemFeature) {
      if (itemFeatures.size() > id) {
         itemFeatures.set(id, itemFeature);
      } else {
         while (itemFeatures.size() < id) {
            itemFeatures.add(StringUtils.EMPTY_STRING);
         }
         itemFeatures.add(itemFeature);
      }
   }


   /**
    * Returns the list of allowed values for the given context feature
    * 
    * @param contextFeature the number of the selected context feature
    * @return the list of allowed values
    */
   public List<Double> getContextFeatureAllowedValues(int contextFeature) {
      return contextFeatureAllowedValues.get(contextFeature);
   }


   /**
    * Sets the allowed values of the context feature at the given given index provided as id.
    * 
    * @param id the id number of the context feature
    * @param allowedValues the list of allowed values
    */
   public void setContextFeatureAllowedValues(Integer id, List<Double> allowedValues) {
      if (contextFeatureAllowedValues.size() > id) {
         contextFeatureAllowedValues.set(id, allowedValues);
      } else {
         while (contextFeatureAllowedValues.size() < id) {
            contextFeatureAllowedValues.add(new ArrayList<>());
         }
         contextFeatureAllowedValues.add(allowedValues);
      }
   }


   /**
    * Returns the list of allowed values for the given feature
    * 
    * @param itemFeature the number of the selected item feature
    * @return the list of allowed values
    */
   public List<Double> getItemFeatureAllowedValues(int itemFeature) {
      return itemFeatureAllowedValues.get(itemFeature);
   }


   /**
    * Sets the allowed values of the item feature at the given given index provided as id.
    * 
    * @param id the id number of the item feature
    * @param allowedValues the list of allowed values
    */
   public void setItemFeatureAllowedValues(Integer id, List<Double> allowedValues) {
      if (itemFeatureAllowedValues.size() > id) {
         itemFeatureAllowedValues.set(id, allowedValues);
      } else {
         while (itemFeatureAllowedValues.size() < id) {
            itemFeatureAllowedValues.add(new ArrayList<>());
         }
         itemFeatureAllowedValues.add(allowedValues);
      }
   }


   /**
    * Sets the list of allowed ordinal values for ratings.
    * 
    * @param allowedOrdinalValues the list of allowed values
    */
   public void setRatingAllowedValues(List<Double> allowedOrdinalValues) {
      ratingAllowedValues = allowedOrdinalValues;
   }


   /**
    * @return the allowedOrdinalValuesForRating
    */
   public List<Double> getRatingAllowedValues() {
      return ratingAllowedValues;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((contextFeatureAllowedValues == null) ? 0 : contextFeatureAllowedValues.hashCode());
      result = prime * result + ((contextFeatures == null) ? 0 : contextFeatures.hashCode());
      result = prime * result + ((contextVectors == null) ? 0 : contextVectors.hashCode());
      result = prime * result + ((itemFeatureAllowedValues == null) ? 0 : itemFeatureAllowedValues.hashCode());
      result = prime * result + ((itemFeatures == null) ? 0 : itemFeatures.hashCode());
      result = prime * result + ((itemVectors == null) ? 0 : itemVectors.hashCode());
      result = prime * result + ((ratingAllowedValues == null) ? 0 : ratingAllowedValues.hashCode());
      result = prime * result + ((ratings == null) ? 0 : ratings.hashCode());
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (!(obj instanceof DefaultDataset))
         return false;
      DefaultDataset<?> other = (DefaultDataset<?>) obj;
      if (contextFeatureAllowedValues == null) {
         if (other.contextFeatureAllowedValues != null)
            return false;
      } else if (!contextFeatureAllowedValues.equals(other.contextFeatureAllowedValues))
         return false;
      if (contextFeatures == null) {
         if (other.contextFeatures != null)
            return false;
      } else if (!contextFeatures.equals(other.contextFeatures))
         return false;
      if (contextVectors == null) {
         if (other.contextVectors != null)
            return false;
      } else if (!contextVectors.equals(other.contextVectors))
         return false;
      if (itemFeatureAllowedValues == null) {
         if (other.itemFeatureAllowedValues != null)
            return false;
      } else if (!itemFeatureAllowedValues.equals(other.itemFeatureAllowedValues))
         return false;
      if (itemFeatures == null) {
         if (other.itemFeatures != null)
            return false;
      } else if (!itemFeatures.equals(other.itemFeatures))
         return false;
      if (itemVectors == null) {
         if (other.itemVectors != null)
            return false;
      } else if (!itemVectors.equals(other.itemVectors))
         return false;
      if (ratingAllowedValues == null) {
         if (other.ratingAllowedValues != null)
            return false;
      } else if (!ratingAllowedValues.equals(other.ratingAllowedValues))
         return false;
      if (ratings == null) {
         if (other.ratings != null)
            return false;
      } else if (!ratings.equals(other.ratings))
         return false;
      return true;
   }


}
