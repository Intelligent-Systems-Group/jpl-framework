package de.upb.cs.is.jpl.api.exception.dataset;


import de.upb.cs.is.jpl.api.dataset.IDataset;


/**
 * Exception which can be thrown, if the {@link IDataset} is filled with invalid data.
 * 
 * @author Sebastian Gottschalk
 *
 */
public class WrongDatasetInputException extends RuntimeException {
   private static final long serialVersionUID = 7971739211293123459L;


   /**
    * Creates a new {@link InvalidInstanceException}.
    * 
    * @param message the message which is thrown
    */
   public WrongDatasetInputException(String message) {
      super(message);
   }
}
