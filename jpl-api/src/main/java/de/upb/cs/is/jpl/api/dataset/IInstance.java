package de.upb.cs.is.jpl.api.dataset;


import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;


/**
 * An interface for an instance which is intended to be used in combination with the
 * {@link IDataset}.
 * 
 * @author Sebastian Osterbrink
 * @author Tanja Tornede
 * 
 * @param <CONTEXT> the type of the context feature vector used in the implementation of this class
 *           to store the context feature vector
 * @param <ITEM> the type of the item feature vector used in the implementation of this class to
 *           store the item feature vector
 * @param <RATING> the type of the rating used in the implementation of this class to store the
 *           rating. Suggested values are {@link IVector} for absolute ratings and {@link Ranking}
 *           for relative ratings.
 * 
 */
public interface IInstance<CONTEXT, ITEM, RATING> {

   /**
    * Returns the context feature vector of this instance.
    * 
    * @return the context feature vector
    * @throws UnsupportedOperationException if this method is unsupported by the special
    *            implementation
    */
   public CONTEXT getContextFeatureVector();


   /**
    * Sets the context feature vector of this instance.
    * 
    * @param contextFeatureVector the context feature vector to set
    * @throws UnsupportedOperationException if this method is unsupported by the special
    *            implementation
    */
   public void setContextFeatureVector(CONTEXT contextFeatureVector);


   /**
    * Returns the item feature vectors of this instance.
    * 
    * @return the item feature vectors
    * @throws UnsupportedOperationException if this method is unsupported by the special
    *            implementation
    */
   public ITEM getItemFeatureVectors();


   /**
    * Sets the item feature vectors of this instance.
    * 
    * @param itemFeatureVectors the item feature vectors to set
    * @throws UnsupportedOperationException if this method is unsupported by the special
    *            implementation
    */
   public void setItemFeatureVector(ITEM itemFeatureVectors);


   /**
    * Returns the rating of this instance.
    * 
    * @return the rating
    */
   public RATING getRating();


   /**
    * Sets the rating of this instance.
    * 
    * @param rating the rating to set
    */
   public void setRating(RATING rating);


   /**
    * Returns the context id in the dataset of this instance.
    * 
    * @return the context id of this instance
    */
   public Integer getContextId();


   /**
    * Sets the context id of this instance.
    * 
    * @param contextId the context id to set
    */
   public void setContextId(Integer contextId);


}
