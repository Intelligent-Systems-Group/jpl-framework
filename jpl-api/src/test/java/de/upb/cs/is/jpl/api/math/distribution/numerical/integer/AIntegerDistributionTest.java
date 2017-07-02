package de.upb.cs.is.jpl.api.math.distribution.numerical.integer;


import de.upb.cs.is.jpl.api.exception.configuration.json.JsonParsingFailedException;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.exception.math.DistributionException;
import de.upb.cs.is.jpl.api.math.distribution.numerical.ANumericalDistributionTest;
import de.upb.cs.is.jpl.api.math.distribution.numerical.INumericalDistribution;
import de.upb.cs.is.jpl.api.util.StringUtils;


/**
 * Abstract test class for {@link AIntegerDistribution} implementations.
 * 
 * @author Tanja Tornede
 *
 */
public abstract class AIntegerDistributionTest extends ANumericalDistributionTest<Integer> {

   private static final String RESOURCE_DIRECTORY_LEVEL = "integer" + StringUtils.FORWARD_SLASH;


   /**
    * Creates a new unit test for integer distributions with the additional path to the resources
    * given.
    * 
    * @param additionalResourcePath the additional path to the resources
    */
   public AIntegerDistributionTest(String additionalResourcePath) {
      super(RESOURCE_DIRECTORY_LEVEL + additionalResourcePath);
   }


   @Override
   protected abstract IIntegerDistribution getDistribution();


   @Override
   protected IIntegerDistribution getTestingDistribution() throws JsonParsingFailedException, ParameterValidationFailedException {
      IIntegerDistribution distribution = getDistribution();
      distribution.setParameters(getTestingParameters());
      return distribution;
   }


   @Override
   protected double getProbabilityFor(INumericalDistribution<Integer> distribution, Integer lowerBound, Integer upperBound)
         throws DistributionException {
      return ((IIntegerDistribution) distribution).getProbabilityFor(lowerBound.intValue(), upperBound.intValue());
   }


   @Override
   protected double getCumulativeDistributionFunctionValueFor(INumericalDistribution<Integer> distribution, Integer value) {
      return ((IIntegerDistribution) distribution).getCumulativeDistributionFunctionValueFor(value.intValue());
   }

}
