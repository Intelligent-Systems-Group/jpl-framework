package de.upb.cs.is.jpl.cli.command;


import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.cli.exception.command.NullException;


/**
 * 
 * The {@link CommandResult} is used to store information from the execution of a {@link ICommand}.
 * It is supposed to store a result object fitting the according {@link ICommand} or an exception in
 * case the execution was not successful. Furthermore additional information can be stored.
 * 
 * @author Alexander Hetzer
 *
 */
public class CommandResult {

   private static final String ERROR_EXCEPTION_SET_NULL = "Set exception of command result to null.";
   private static final String ERROR_ADDITIONAL_INFORMATION_SET_NULL = "Set additional information of command result to null.";
   private static final String ERROR_COMMAND_RESULT_SET_NULL = "Set result of command result to null.";


   private Object result;
   private String additionalInformation;
   private Exception exception;


   /**
    * Creates an empty command result. Note that this constructor should not be used. To create an
    * instance, use the static factory methods provided by this class.
    */
   private CommandResult() {
      result = null;
      additionalInformation = StringUtils.EMPTY_STRING;
      exception = new NullException(StringUtils.EMPTY_STRING);
   }


   /**
    * Returns the result object associated with this command result.
    * 
    * @return the result object associated with this command result
    */
   public Object getResult() {
      return result;
   }


   /**
    * Returns the additional information associated with this command result.
    * 
    * @return the additional information associated with this command result
    */
   public String getAdditionalInformation() {
      return additionalInformation;
   }


   /**
    * Checks if the command producing this result was executed successfully.
    * 
    * @return {@code true} if executed successfully otherwise {@code false}
    */
   public boolean isExecutedSuccessfully() {
      return !hasExceptionOccurred() && !isResultObjectNull();
   }


   /**
    * Checks if the result object is {@code null} and returns {@code true} if this is the case.
    * 
    * @return {@code true}, if the result object is {@code null} otherwise {@code false}
    */
   private boolean isResultObjectNull() {
      return result == null;
   }


   /**
    * Checks whether an exception has occurred and returns {@code true} if this is the case.
    * 
    * @return {@code true} if an exception has occurred otherwise {@code false}
    */
   private boolean hasExceptionOccurred() {
      return !(exception instanceof NullException);
   }


   /**
    * If this command result is a failure command result, this method checks whether the failure
    * reason is known. This method should only be called if {@link #isExecutedSuccessfully()}
    * returns {@code false}.
    * 
    * @return {@code true} if the failure reason is known otherwise {@code false}
    */
   public boolean isFailureReasonKnown() {
      return hasExceptionOccurred();
   }


   /**
    * Returns the stored exception.
    * 
    * @return the stored exception
    */
   public Exception getException() {
      return exception;
   }


   /**
    * Replaces the current result object with the given one.
    * 
    * @param result the result to set; must not be {@code null}
    * 
    * @throws NullPointerException if the parameter is {@code null}
    */
   public void setResult(Object result) {
      if (result != null) {
         this.result = result;
      } else {
         throw new NullPointerException(ERROR_COMMAND_RESULT_SET_NULL);
      }
   }


   /**
    * Replaces the current additional information with the given one.
    * 
    * @param additionalInformation the additional information to set; must not be {@code null}
    * 
    * @throws NullPointerException if the parameter is {@code null}
    */
   public void setAdditionalInformation(String additionalInformation) {
      if (additionalInformation != null) {
         this.additionalInformation = additionalInformation;
      } else {
         throw new NullPointerException(ERROR_ADDITIONAL_INFORMATION_SET_NULL);
      }
   }


   /**
    * Replaces the current exception with the given one.
    * 
    * @param exception the exception to set; must not be {@code null}
    * 
    * @throws NullPointerException if the parameter is {@code null}
    */
   public void setException(Exception exception) {
      if (exception != null) {
         this.exception = exception;
      } else {
         throw new NullPointerException(ERROR_EXCEPTION_SET_NULL);
      }
   }


   /**
    * Creates a success command result with only the result set.
    * 
    * @param result the result object to set; must not be {@code null}
    * 
    * @return the according success command result
    * 
    * @throws NullPointerException if the parameter is {@code null}
    */
   public static CommandResult createSuccessCommandResult(Object result) {
      CommandResult successCommandResult = new CommandResult();
      successCommandResult.setResult(result);
      return successCommandResult;
   }


   /**
    * Creates a success command result with additional information where both the result and
    * additional information are set.
    * 
    * @param result the result object to set; must not be {@code null}
    * @param additionalInformation the additional information to set; must not be {@code null}
    * 
    * @return the according success command result
    * 
    * @throws NullPointerException if one of the parameters is {@code null}
    */
   public static CommandResult createSuccessCommandResultWithAdditionalInformation(Object result, String additionalInformation) {
      CommandResult successCommandResult = createSuccessCommandResult(result);
      successCommandResult.setAdditionalInformation(additionalInformation);
      return successCommandResult;
   }


   /**
    * Creates a success command result with exception where both a result and an exception is set.
    * 
    * @param result the result object to set; must not be {@code null}
    * @param exception the exception important for the returning {@code CommandResult}; must not be
    *           {@code null}
    * 
    * @return the according success {@code CommandResult}
    * 
    * @throws NullPointerException if one of the parameters is {@code null}
    */
   public static CommandResult createSuccessCommandResultWithException(Object result, Exception exception) {
      CommandResult successCommandResult = createSuccessCommandResult(result);
      successCommandResult.setException(exception);
      return successCommandResult;
   }


   /**
    * Creates a failure command result where only the exception variable is set.
    * 
    * @param exception the exception to set; must not be {@code null}
    * 
    * @return the according failure command result
    * 
    * @throws NullPointerException if the parameter is {@code null}
    */
   public static CommandResult createFailureCommandResult(Exception exception) {
      CommandResult failureCommandResult = new CommandResult();
      failureCommandResult.setException(exception);
      return failureCommandResult;
   }

}
