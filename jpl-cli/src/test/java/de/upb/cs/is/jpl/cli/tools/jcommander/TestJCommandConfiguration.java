package de.upb.cs.is.jpl.cli.tools.jcommander;


import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;


/**
 * 
 * Sample command configuration for JCommander, which stores variables based on the annotations. Is
 * used in combination with {@link JCommanderTest} in order to test the command functionality of
 * JCommander.
 * 
 * @author Alexander Hetzer
 *
 */

@Parameters(separators = "=", commandDescription = "sets all given algorithms")
public class TestJCommandConfiguration {

   @Parameter(description = "algorithms to set")
   private List<String> algorithms;

   @Parameter(names = { "--check", "-c" })
   private Boolean checkAlgorithmsOnSuitability = false;

   @Parameter(names = { "--author", "-a" })
   private String author;


   /**
    * Returns the list of algorithms.
    * 
    * @return the list of algorithms
    */
   public List<String> getAlgorithms() {
      return algorithms;
   }


   /**
    * Returns if the given algorithms should be checked on suitability.
    * 
    * @return {@code true}, if the algorithms should be checked, otherwise {@code false}
    */
   public Boolean getCheckAlgorithmsOnSuitability() {
      return checkAlgorithmsOnSuitability;
   }


   /**
    * Returns the author given by the user.
    * 
    * @return the author given by the user
    */
   public String getAuthor() {
      return author;
   }

}
