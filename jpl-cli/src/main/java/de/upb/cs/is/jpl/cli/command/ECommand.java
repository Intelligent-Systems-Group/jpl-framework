package de.upb.cs.is.jpl.cli.command;


import java.util.Arrays;
import java.util.List;

import de.upb.cs.is.jpl.cli.command.adddataset.AddDatasetCommandHandler;
import de.upb.cs.is.jpl.cli.command.addlearningalgorithm.AddLearningAlgorithmCommandHandler;
import de.upb.cs.is.jpl.cli.command.determineapplicablealgorithms.DetermineApplicableAlgorithmsCommandHandler;
import de.upb.cs.is.jpl.cli.command.evaluatealgorithms.EvaluateAlgorithmsCommandHandler;
import de.upb.cs.is.jpl.cli.command.help.HelpCommandHandler;
import de.upb.cs.is.jpl.cli.command.loadlearningalgorithms.LoadLearningAlgorithmsCommandHandler;
import de.upb.cs.is.jpl.cli.command.readsystemconfiguration.ReadSystemConfigurationCommandHandler;
import de.upb.cs.is.jpl.cli.command.runcompletetoolchain.RunCompleteToolChainCommandHandler;
import de.upb.cs.is.jpl.cli.command.setlearningproblem.SetLearningProblemCommandHandler;
import de.upb.cs.is.jpl.cli.command.trainmodels.TrainModelsCommandHandler;


/**
 * Enumeration of all supported commands. If a new command is added it has to be added to this
 * enumeration as the registration of command handlers for commands is done implicitly via this
 * enumeration.
 *
 * @author Alexander Hetzer
 */
public enum ECommand {

   /**
    * Represents the help command.
    */
   HELP("help") {
      @Override
      public ICommandHandler createCommandHandler() {
         return new HelpCommandHandler();
      }
   },
   /**
    * Represents the determine applicable algorithms command.
    */
   DETERMINE_APPLICABLE_ALGORITHMS("determine_applicable_algorithms") {
      @Override
      public ICommandHandler createCommandHandler() {
         return new DetermineApplicableAlgorithmsCommandHandler();
      }
   },
   /**
    * Represents the evaluate algorithms command.
    */
   EVALUATE_ALGORITHMS("evaluate_algorithms") {
      @Override
      public ICommandHandler createCommandHandler() {
         return new EvaluateAlgorithmsCommandHandler();
      }
   },
   /**
    * Represents the read system configuration command.
    */
   READ_SYSTEM_CONFIGURATION("read_system_configuration") {
      @Override
      public ICommandHandler createCommandHandler() {
         return new ReadSystemConfigurationCommandHandler();
      }
   },
   /**
    * Represents the add dataset command.
    */
   ADD_DATASET("add_dataset") {
      @Override
      public ICommandHandler createCommandHandler() {
         return new AddDatasetCommandHandler();
      }
   },
   /**
    * Represents the set learning problem command.
    */
   SET_LEARNING_PROBLEM("set_learning_problem") {
      @Override
      public ICommandHandler createCommandHandler() {
         return new SetLearningProblemCommandHandler();
      }
   },
   /**
    * Represents the add learning algorithm command.
    */
   ADD_LEARNING_ALGORITHM("add_learning_algorithm") {
      @Override
      public ICommandHandler createCommandHandler() {
         return new AddLearningAlgorithmCommandHandler();
      }
   },
   /**
    * Represents the load learning algorithms command.
    */
   LOAD_LEARNING_ALGORITHMS("load_learning_algorithms") {
      @Override
      public ICommandHandler createCommandHandler() {
         return new LoadLearningAlgorithmsCommandHandler();
      }
   },
   /**
    * Represents the train models command.
    */
   TRAIN_MODELS("train_models") {
      @Override
      public ICommandHandler createCommandHandler() {
         return new TrainModelsCommandHandler();
      }
   },
   /**
    * Represents the run complete toolchain command.
    */
   RUN_COMPLETE_TOOLCHAIN("run_toolchain") {
      @Override
      public ICommandHandler createCommandHandler() {
         return new RunCompleteToolChainCommandHandler();
      }
   };
   /*
    * When adding a new command here, make sure that the createCommandHandler method DOES NOT RETURN
    * NULL!
    */

   private String commandIdentifier;


   /**
    * Creates the {@link ECommand} with the command identifier. Private constructor, as enumeration
    * instances should not be created from outside the enumeration.
    * 
    * @param commandIdentifier the identifier of the command
    */
   private ECommand(String commandIdentifier) {
      this.commandIdentifier = commandIdentifier;
   }


   /**
    * Creates the according {@link ICommandHandler} and returns it. It is extremely important that
    * every implementation of this method does not return {@code null}!
    *
    * @return the {@link ICommandHandler} responsible for the command represented by this enum, must
    *         not be {@code null}
    */
   public abstract ICommandHandler createCommandHandler();


   /**
    * Returns the according ECommand instance which has the identifier to search for.
    *
    * @param commandIdentifier the identifier of the command to search for
    * 
    * @return an {@link ECommand} with the correct identifier if found, {@code null} if none is
    *         found
    */
   public static ECommand getECommandByIdentifier(String commandIdentifier) {
      ECommand[] commands = ECommand.values();
      for (ECommand command : commands) {
         if (command.commandIdentifier.equals(commandIdentifier)) {
            return command;
         }
      }
      return null;
   }


   /**
    * Returns a list containing all {@link ECommand}s.
    *
    * @return the list of all {@link ECommand}s
    */
   public static List<ECommand> getECommands() {
      return Arrays.asList(ECommand.values());
   }


   /**
    * Returns the identifier of this command.
    *
    * @return the identifier of this command
    */
   public String getCommandIdentifier() {
      return commandIdentifier;
   }

}
