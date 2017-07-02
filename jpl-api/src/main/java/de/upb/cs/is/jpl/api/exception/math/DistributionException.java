package de.upb.cs.is.jpl.api.exception.math;


import de.upb.cs.is.jpl.api.exception.JplException;
import de.upb.cs.is.jpl.api.math.distribution.IDistribution;


/**
 * This exception indicates that a problem occurred while using an {@link IDistribution}. The
 * details should be provided in the exception message.
 * 
 * @author Alexander Hetzer
 *
 */
public class DistributionException extends JplException {

   private static final long serialVersionUID = -5266720129789725679L;


   /**
    * Creates an {@link DistributionException} with the given exception message.
    * 
    * @param message the message of the exception
    */
   public DistributionException(String message) {
      super(message);
   }


   /**
    * Creates a {@link DistributionException} with the given exception message, wrapping the given
    * throwable.
    * 
    * @param message the message of the exception
    * @param throwable the throwable to wrap
    */
   public DistributionException(String message, Throwable throwable) {
      super(message, throwable);
   }

}
