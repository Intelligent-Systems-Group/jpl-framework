package de.upb.cs.is.jpl.api.exception.math;


import de.upb.cs.is.jpl.api.exception.JplRuntimeException;
import de.upb.cs.is.jpl.api.math.distribution.IDistribution;


/**
 * This exception indicates that a statistic requested is not defined. Usually this exception will
 * be thrown by {@link IDistribution}s with an undefined expected value and variance.
 * 
 * @author Alexander Hetzer
 *
 */
public class UndefinedStatisticException extends JplRuntimeException {

   private static final long serialVersionUID = -1917701747138970000L;


   /**
    * Creates an {@link UndefinedStatisticException} with the given exception message.
    * 
    * @param message the message of the exception
    */
   public UndefinedStatisticException(String message) {
      super(message);
   }


   /**
    * Creates a {@link UndefinedStatisticException} with the given exception message, wrapping the
    * given throwable.
    * 
    * @param message the message of the exception
    * @param throwable the throwable to wrap
    */
   public UndefinedStatisticException(String message, Throwable throwable) {
      super(message, throwable);
   }

}
