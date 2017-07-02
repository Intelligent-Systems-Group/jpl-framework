package de.upb.cs.is.jpl.cli.command.addlearningalgorithm;


import de.upb.cs.is.jpl.cli.exception.command.addlearningalgorithm.ParameterParsingFailedException;


/**
 * This class encapsulates a parameter definition given by the user in the form of a string. A
 * parameter definition consists of a parameter name, a value and a separator, which is used to
 * separate the two parts in the string. Note that a parameter object should not be created with a
 * constructor, but only via the {@link #createParameterFromParameterDefinitionString(String)}
 * method.
 * 
 * @author Alexander Hetzer
 *
 */
public class Parameter {
   private static final String ERROR_MISSING_PARAMETER_NAME = "Missing parameter name in parameter definition %s.";
   private static final String ERROR_MISSING_PARAMETER_VALUE = "Missing parameter value in parameter definition %s.";

   private static final String ERROR_MISSING_NAME_VALUE_SEPARATOR = "Parameter definition %s does not contain the name-value separator: %s.";

   private static final String NAME_VALUE_SEPARATOR = ":";

   private String parameterName;
   private String parameterValue;


   /**
    * Private constructor, as a parameter object should only be created using the
    * {@link #createParameterFromParameterDefinitionString(String)} method.
    * 
    * @param parameterName the name of the parameter
    * @param parameterValue the value of the parameter
    */
   private Parameter(String parameterName, String parameterValue) {
      this.parameterName = parameterName.trim();
      this.parameterValue = parameterValue.trim();
   }


   /**
    * Creates a parameter instance based on the given parameter definition string.
    * 
    * @param parameterDefinition the parameter definition as a string
    * 
    * @return the parameter instance based on the given parameter definition string
    * 
    * @throws ParameterParsingFailedException if the given string does not hold to the conventions
    *            of a parameter definition resulting in a parsing problem
    */
   public static Parameter createParameterFromParameterDefinitionString(String parameterDefinition) throws ParameterParsingFailedException {
      String trimmedParamterDefinition = parameterDefinition.trim();
      assertDefinitionContainsSeparator(trimmedParamterDefinition);
      assertSeparatorNotAtFirstPosition(trimmedParamterDefinition);
      assertSeparatorNotAtLastPosition(trimmedParamterDefinition);

      return parseParameterDefinition(trimmedParamterDefinition);
   }


   /**
    * Parses the given parameter definition into a parameter instance and returns it. This method
    * assumes that the given parameter definition string is syntactically correct.
    * 
    * @param parameterDefinition the parameter definition string to be parsed
    * 
    * @return a parameter instance with the content defined in the parameter definition string
    */
   private static Parameter parseParameterDefinition(String parameterDefinition) {
      String[] parameterDefinitionNameValueSplit = parameterDefinition.split(NAME_VALUE_SEPARATOR);
      return new Parameter(parameterDefinitionNameValueSplit[0], parameterDefinitionNameValueSplit[1]);
   }


   /**
    * Asserts that the parameter name and value separator is not at the first character of the given
    * parameterDefinition and throws an exception if the assertion fails.
    * 
    * @param parameterDefinition the parameter definition string to be checked
    * 
    * @throws ParameterParsingFailedException if the parameter name and value separator is at the
    *            first character of the given parameter definition string
    */
   private static void assertSeparatorNotAtFirstPosition(String parameterDefinition) throws ParameterParsingFailedException {
      if (parameterDefinition.indexOf(NAME_VALUE_SEPARATOR) == 0) {
         throw new ParameterParsingFailedException(String.format(ERROR_MISSING_PARAMETER_NAME, parameterDefinition));
      }
   }


   /**
    * Asserts that the parameter name and value separator is not at the last character of the given
    * parameterDefinition and throws an exception if the assertion fails.
    * 
    * @param parameterDefinition the parameter definition string to be checked
    * 
    * @throws ParameterParsingFailedException if the parameter name and value separator is at the
    *            last character of the given parameter definition string
    */
   private static void assertSeparatorNotAtLastPosition(String parameterDefinition) throws ParameterParsingFailedException {
      if (parameterDefinition.lastIndexOf(NAME_VALUE_SEPARATOR) == parameterDefinition.length() - 1) {
         throw new ParameterParsingFailedException(String.format(ERROR_MISSING_PARAMETER_VALUE, parameterDefinition));
      }
   }


   /**
    * Asserts that the given parameterDefinition contains the parameter name and value separator and
    * throws an exception if the assertion fails.
    * 
    * @param parameterDefinition the parameter definition string to be checked
    * 
    * @throws ParameterParsingFailedException if the given parameterDefinition does not contain the
    *            parameter name and value separator
    */
   private static void assertDefinitionContainsSeparator(String parameterDefinition) throws ParameterParsingFailedException {
      if (!parameterDefinition.contains(NAME_VALUE_SEPARATOR)) {
         throw new ParameterParsingFailedException(
               String.format(ERROR_MISSING_NAME_VALUE_SEPARATOR, parameterDefinition, NAME_VALUE_SEPARATOR));
      }
   }


   /**
    * Returns the parameter name.
    * 
    * @return the name of this parameter
    */
   public String getParameterName() {
      return parameterName;
   }


   /**
    * Returns the parameter value.
    * 
    * @return the value of this parameter
    */
   public String getParameterValue() {
      return parameterValue;
   }

}
