package de.upb.cs.is.jpl.api.dataset;


import de.upb.cs.is.jpl.api.dataset.defaultdataset.relative.Ranking;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.util.ObjectUtils;


/**
 * An abstract class for an instance which is intended to be used in combination with the
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
public abstract class AInstance<CONTEXT, ITEM, RATING> implements IInstance<CONTEXT, ITEM, RATING> {

   protected RATING rating;

   protected Integer contextId = null;


   @Override
   public RATING getRating() {
      return rating;
   }


   @Override
   public void setRating(RATING rating) {
      this.rating = rating;

   }


   @Override
   public Integer getContextId() {
      return contextId;
   }


   @Override
   public void setContextId(Integer contextId) {
      this.contextId = contextId;
   }


   @Override
   public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      if (contextId != null) {
         stringBuilder.append(contextId);
         stringBuilder.append(":");
      }
      stringBuilder.append(rating.toString());
      return stringBuilder.toString();
   }


   @Override
   public boolean equals(Object secondInstance) {
      if (secondInstance instanceof IInstance<?, ?, ?>) {
         @SuppressWarnings("unchecked")
         IInstance<CONTEXT, ITEM, RATING> castedSecondInstance = IInstance.class.cast(secondInstance);
         if (ObjectUtils.areBothObjectsNull(contextId, castedSecondInstance.getContextId())
               || (ObjectUtils.areBothObjectsNotNull(contextId, castedSecondInstance.getContextId())
                     && contextId.equals(castedSecondInstance.getContextId()))) {
            return rating.equals(castedSecondInstance.getRating());
         }
      }
      return false;
   }


   @Override
   public int hashCode() {
      int hashCode = 1;
      hashCode = 31 * hashCode + (contextId == null ? 0 : contextId.hashCode());
      hashCode = 31 * hashCode + (rating == null ? 0 : rating.hashCode());
      return hashCode;
   }

}
