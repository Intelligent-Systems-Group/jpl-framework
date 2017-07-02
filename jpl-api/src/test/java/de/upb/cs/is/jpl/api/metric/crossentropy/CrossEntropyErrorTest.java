package de.upb.cs.is.jpl.api.metric.crossentropy;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.metric.AAverageDecomposableDoubleDoubleMetricTest;
import de.upb.cs.is.jpl.api.metric.IMetric;
import de.upb.cs.is.jpl.api.util.StringUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * This class tests the {@link CrossEntropyError}.
 * 
 * @author Alexander Hetzer
 *
 */
public class CrossEntropyErrorTest extends AAverageDecomposableDoubleDoubleMetricTest {

   private static final String RESOURCE_DIRECTORY_LEVEL = "cross_entropy" + File.separator;


   /**
    * Creates a new {@link CrossEntropyErrorTest}.
    */
   public CrossEntropyErrorTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   public List<Pair<Pair<Double, Double>, Double>> getCorrectListofPairsOfRatings() {
      List<Pair<Pair<Double, Double>, Double>> correctListOfPairsOfRatings = new ArrayList<>();
      correctListOfPairsOfRatings.add(Pair.of(Pair.of(-1.0, 1.0), Math.log(1 + Math.E)));
      correctListOfPairsOfRatings.add(Pair.of(Pair.of(1.0, 1.0), Math.log(1 + 1 / Math.E)));
      correctListOfPairsOfRatings.add(Pair.of(Pair.of(-1.0, -1.0), Math.log(1 + 1 / Math.E)));
      // test case for lower bound of exponential argument
      correctListOfPairsOfRatings.add(Pair.of(Pair.of(-500.0, -500.0), 0.0));
      // test case for upper bound of exponential argument
      correctListOfPairsOfRatings.add(Pair.of(Pair.of(-500.0, 5.0), 2500.0));
      return correctListOfPairsOfRatings;
   }


   @Override
   public List<Pair<String, Pair<Pair<Double, Double>, Double>>> getWrongListOfPairsOfRatingsWithExceptionMessage() {
      List<Pair<String, Pair<Pair<Double, Double>, Double>>> wrongListOfPairsOfRatingsWithExceptionMessage = new ArrayList<>();
      for (Pair<Pair<Double, Double>, Double> correctTestCase : this.getCorrectListofPairsOfRatings()) {
         correctTestCase.setSecond(correctTestCase.getSecond() * 500 + 100);
         wrongListOfPairsOfRatingsWithExceptionMessage.add(Pair.of(StringUtils.EMPTY_STRING, correctTestCase));
      }
      return wrongListOfPairsOfRatingsWithExceptionMessage;
   }


   @Override
   public IMetric<Double, Double> getEvaluationMetric() {
      return new CrossEntropyError();
   }

}
