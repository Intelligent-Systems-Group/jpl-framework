package de.upb.cs.is.jpl.cli.command.setlearningproblem;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.cli.command.ACommand;
import de.upb.cs.is.jpl.cli.command.CommandResult;
import de.upb.cs.is.jpl.cli.command.ECommand;
import de.upb.cs.is.jpl.cli.core.systemconfiguration.SystemConfiguration;
import de.upb.cs.is.jpl.cli.exception.command.setlearningproblem.UnknownLearningProblemException;


/**
 * This command is responsible for setting the learning problem defined in the given json file into
 * the {@link SystemConfiguration}.
 * 
 * @author Tanja Tornede
 *
 */
public class SetLearningProblemCommand extends ACommand {

   private static final Logger logger = LoggerFactory.getLogger(SetLearningProblemCommand.class);

   private static final String ERROR_UNKNOWN_IDENTIFIER = "The given identifier for the learning problem \"%s\" is unknown!";

   /**
    * The identifier of the learning problem, as defined in {@link ELearningProblem}.
    */
   private String learningProblemIdentifier;

   private boolean canBeExecuted = true;
   private String failureReason;


   /**
    * Constructor for a {@link SetLearningProblemCommand}.
    */
   private SetLearningProblemCommand() {
      super(ECommand.SET_LEARNING_PROBLEM.getCommandIdentifier());
      this.learningProblemIdentifier = StringUtils.EMPTY_STRING;
      this.failureReason = StringUtils.EMPTY_STRING;
   }


   /**
    * Constructor for a {@link SetLearningProblemCommand} with the given learningProblemIdentifier.
    * 
    * @param learningProblemIdentifier String which is the identifier of the learning problem
    */
   private SetLearningProblemCommand(String learningProblemIdentifier) {
      this();
      this.learningProblemIdentifier = learningProblemIdentifier;
   }


   /**
    * Constructor for a {@link SetLearningProblemCommand} which cannot be executed.
    * 
    * @param exception {@link Exception} including the information why the command cannot be
    *           executed
    */
   private SetLearningProblemCommand(Exception exception) {
      this();
      this.canBeExecuted = false;
      this.failureReason = exception.getMessage();
   }


   /**
    * Creates a {@link SetLearningProblemCommand} with an identifier of the learning problem as
    * parameter.
    * 
    * @param learningProblemIdentifier string which is the identifier of the learning problem
    * @return {@code SetLearningProblemCommand} with the given parameter
    */
   public static SetLearningProblemCommand createLearningProblemCommandByIdentifier(String learningProblemIdentifier) {
      return new SetLearningProblemCommand(learningProblemIdentifier);
   }


   /**
    * Creates a {@link SetLearningProblemCommand} which cannot be executed as the amount of
    * parameters is wrong.
    * 
    * @param exception including information why the command cannot be executed
    * @return {@code SetLearningProblemCommand} which cannot be executed
    */
   public static SetLearningProblemCommand createLearningProblemCommandWithWrongAmountOfParameters(Exception exception) {
      return new SetLearningProblemCommand(exception);
   }


   @Override
   public boolean canBeExecuted() {
      return canBeExecuted;
   }


   @Override
   public CommandResult executeCommand() {
      ELearningProblem learningProblem = ELearningProblem.getELearningProblemByIdentifier(learningProblemIdentifier);
      if (learningProblem == null) {
         String errorMessage = String.format(ERROR_UNKNOWN_IDENTIFIER, learningProblemIdentifier);
         logger.error(errorMessage);
         return CommandResult.createFailureCommandResult(new UnknownLearningProblemException(errorMessage));
      }
      SystemConfiguration.getSystemConfiguration().setLearningProblem(learningProblem);
      return CommandResult.createSuccessCommandResult(learningProblem);
   }


   @Override
   public String getFailureReason() {
      return failureReason;
   }


   @Override
   public void undo() {
      // nothing to do here
   }

}
