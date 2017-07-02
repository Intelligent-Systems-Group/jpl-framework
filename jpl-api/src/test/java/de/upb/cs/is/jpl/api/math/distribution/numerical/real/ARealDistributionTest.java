package de.upb.cs.is.jpl.api.math.distribution.numerical.real;


import de.upb.cs.is.jpl.api.exception.configuration.json.JsonParsingFailedException;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.exception.math.DistributionException;
import de.upb.cs.is.jpl.api.math.distribution.numerical.ANumericalDistributionTest;
import de.upb.cs.is.jpl.api.math.distribution.numerical.INumericalDistribution;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.api.util.TestUtils;


/**
 * Abstract test class for {@link ARealDistribution} implementations.
 * 
 * @author Tanja Tornede
 *
 */
public abstract class ARealDistributionTest extends ANumericalDistributionTest<Double> {

   private static final String RESOURCE_DIRECTORY_LEVEL = "real" + StringUtils.FORWARD_SLASH;

   protected static final String REFLECTION_ERROR_NON_POSITIVE_SCALING_PARAMETER = "ERROR_NON_POSITIVE_SCALING_PARAMETER";


   /**
    * Creates a new unit test for integer distributions with the additional path to the resources
    * given.
    * 
    * @param additionalResourcePath the additional path to the resources
    */
   public ARealDistributionTest(String additionalResourcePath) {
      super(RESOURCE_DIRECTORY_LEVEL + additionalResourcePath);
   }


   /**
    * Returns the expected exception message for the case of a negative scaling parameter.
    * 
    * @return the expected exception message for the case of a negative scaling parameter
    */
   protected String getExpectedErrorForNonNegativeScalingParameter() {
      return TestUtils.getStringByReflectionSafely(ARealDistributionConfiguration.class, REFLECTION_ERROR_NON_POSITIVE_SCALING_PARAMETER);
   }


   @Override
   protected abstract IRealDistribution getDistribution();


   @Override
   protected IRealDistribution getTestingDistribution() throws JsonParsingFailedException, ParameterValidationFailedException {
      IRealDistribution distribution = getDistribution();
      distribution.setParameters(getTestingParameters());
      return distribution;
   }


   @Override
   protected double getProbabilityFor(INumericalDistribution<Double> distribution, Double lowerBound, Double upperBound)
         throws DistributionException {
      return ((IRealDistribution) distribution).getProbabilityFor(lowerBound.doubleValue(), upperBound.doubleValue());
   }


   @Override
   protected double getCumulativeDistributionFunctionValueFor(INumericalDistribution<Double> distribution, Double value) {
      return ((IRealDistribution) distribution).getCumulativeDistributionFunctionValueFor(value.doubleValue());
   }
}
