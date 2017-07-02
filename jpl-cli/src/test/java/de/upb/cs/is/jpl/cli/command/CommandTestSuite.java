package de.upb.cs.is.jpl.cli.command;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.upb.cs.is.jpl.cli.command.adddatasets.AddDatasetCommandTestSuite;
import de.upb.cs.is.jpl.cli.command.addlearningalgorithm.AddLearningAlgorithmCommandTest;
import de.upb.cs.is.jpl.cli.command.determineapplicablealgorithms.DetermineApplicableAlgorithmsCommandTest;
import de.upb.cs.is.jpl.cli.command.evaluatealgorithms.EvaluateAlgorithmsCommandTestSuite;
import de.upb.cs.is.jpl.cli.command.loadlearningalgorithms.LoadLearningAlgorithmsCommandTest;
import de.upb.cs.is.jpl.cli.command.readsystemconfiguration.ReadSystemConfigurationCommandHandlerTest;
import de.upb.cs.is.jpl.cli.command.runcompletetoolchain.RunCompleteToolChainCommandTest;
import de.upb.cs.is.jpl.cli.command.setlearningproblem.SetLearningProblemCommandTest;
import de.upb.cs.is.jpl.cli.command.trainmodels.TrainModelsCommandTest;


/**
 * Test suite for all command tests.
 * 
 * @author Tanja Tornede
 *
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ AddDatasetCommandTestSuite.class, AddLearningAlgorithmCommandTest.class,
      DetermineApplicableAlgorithmsCommandTest.class, LoadLearningAlgorithmsCommandTest.class,
      ReadSystemConfigurationCommandHandlerTest.class, RunCompleteToolChainCommandTest.class, SetLearningProblemCommandTest.class,
      TrainModelsCommandTest.class,EvaluateAlgorithmsCommandTestSuite.class })
public class CommandTestSuite {

}
