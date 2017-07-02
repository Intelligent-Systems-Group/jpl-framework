package de.upb.cs.is.jpl.cli.command.setlearningproblem;


import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.cli.command.ACommandConfiguration;
import de.upb.cs.is.jpl.cli.command.ICommandConfiguration;


/**
 * This configuration encapsulates all necessary and/or possible combination of parameters for the
 * {@link SetLearningProblemCommand}. The Parameters are defined for easy handling by JCommander.
 * 
 * @author Tanja Tornede
 *
 */
@Parameters(separators = "=", commandDescription = "Initializes the system to work with the given learning problem.")
public class SetLearningProblemCommandConfiguration extends ACommandConfiguration {

   @Parameter(names = { "--learning_problem", "-lp" }, description = "Specifies the learning problem to work with.", required = true)
   private String learningProblem;


   @Override
   public void resetFields() {
      learningProblem = StringUtils.EMPTY_STRING;
   }


   @Override
   public ICommandConfiguration getCopy() {
      SetLearningProblemCommandConfiguration copy = new SetLearningProblemCommandConfiguration();
      copy.setLearningProblem(this.learningProblem);
      return copy;
   }


   /**
    * Sets the identifier of the learning problem to set.
    * 
    * @param learningProblem the dentifier of the learning problem to set
    */
   public void setLearningProblem(String learningProblem) {
      this.learningProblem = learningProblem;
   }


   /**
    * Returns the identifier for the learning problem.
    * 
    * @return the dentifier of the learning problem
    */
   public String getLearningProblem() {
      return learningProblem;
   }

}
