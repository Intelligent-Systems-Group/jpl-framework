package de.upb.cs.is.jpl.api.metric.labelbasedfmeasure;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.upb.cs.is.jpl.api.math.linearalgebra.DenseDoubleVector;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import de.upb.cs.is.jpl.api.metric.AMetric;
import de.upb.cs.is.jpl.api.metric.ANonDecomposableMetricTest;
import de.upb.cs.is.jpl.api.metric.IMetric;
import de.upb.cs.is.jpl.api.util.TestUtils;
import de.upb.cs.is.jpl.api.util.datastructure.Pair;


/**
 * THis class tests the {@link MacroLabelBasedFMeasure}.
 * 
 * @author Alexander Hetzer
 *
 */
public class MacroLabelBasedFMeasureTest extends ANonDecomposableMetricTest<IVector, Double> {

   private static final String RESOURCE_DIRECTORY_LEVEL = "macro_label_based_f_measure" + File.separator;

   private static final String REFLECTION_ERROR_CANNOT_COMPUTE_F_MEASURE = "ERROR_CANNOT_COMPUTE_F_MEASURE_FOR_THIS_DATASET";
   private static final String REFLECTION_ERROR_UNEQUAL_VECTOR_SIZES = "ERROR_UNEQUAL_VECTOR_SIZES";


   /**
    * Creates a new {@link MacroLabelBasedFMeasureTest}.
    */
   public MacroLabelBasedFMeasureTest() {
      super(RESOURCE_DIRECTORY_LEVEL);
   }


   @Override
   public IMetric<IVector, Double> getEvaluationMetric() {
      return new MacroLabelBasedFMeasure();
   }


   @Override
   public List<Pair<Pair<List<IVector>, List<IVector>>, Double>> getCorrectListofPairsListOfRatings() {
      List<IVector> firstRatings = new ArrayList<>();
      List<IVector> secondRatings = new ArrayList<>();

      double[] firstRating1 = { 1, 0, 1, 1, 0 };
      double[] secondRating1 = { 0, 1, 0, 0, 1 };
      addVectorsToLists(firstRatings, secondRatings, firstRating1, secondRating1);

      double[] firstRating2 = { 0, 1, 1, 1, 0 };
      double[] secondRating2 = { 0, 1, 1, 0, 1 };
      addVectorsToLists(firstRatings, secondRatings, firstRating2, secondRating2);

      double[] firstRating4 = { 1, 1, 1, 1, 1 };
      double[] secondRating4 = { 1, 1, 1, 1, 1 };
      addVectorsToLists(firstRatings, secondRatings, firstRating4, secondRating4);

      double[] firstRating5 = { 0, 1, 1, 1, 1 };
      double[] secondRating5 = { 1, 1, 1, 1, 1 };
      addVectorsToLists(firstRatings, secondRatings, firstRating5, secondRating5);

      List<Pair<Pair<List<IVector>, List<IVector>>, Double>> correctListOfPairsListOfRatings = new ArrayList<>();
      correctListOfPairsListOfRatings.add(Pair.of(Pair.of(firstRatings, secondRatings), (0.5 + 2 * (6 / 7.0) + 2 * (2 / 3.0)) / 5.0));
      return correctListOfPairsListOfRatings;
   }


   /**
    * Adds the given two arrays as vectors the list, such that the first one is added to the first
    * list and the second one to the second list.
    * 
    * @param firstRatings the first list
    * @param secondRatings the second list
    * @param firstRating the first array
    * @param secondRating the second array
    */
   private void addVectorsToLists(List<IVector> firstRatings, List<IVector> secondRatings, double[] firstRating, double[] secondRating) {
      firstRatings.add(new DenseDoubleVector(firstRating));
      secondRatings.add(new DenseDoubleVector(secondRating));
   }


   @Override
   public List<Pair<String, Pair<Pair<List<IVector>, List<IVector>>, Double>>> getWrongListOfPairsOfListOfRatingsWithExceptionMessage() {
      List<Pair<String, Pair<Pair<List<IVector>, List<IVector>>, Double>>> wrongListOfPairsOfListOfRatingsWithExceptionMessage = new ArrayList<>();

      List<IVector> firstRatings = new ArrayList<>();
      List<IVector> secondRatings = new ArrayList<>();
      double[] firstRating1 = { 1, 0, 1, 1, 0, 0 };
      double[] secondRating1 = { 0, 1, 0, 0, 1 };
      addVectorsToLists(firstRatings, secondRatings, firstRating1, secondRating1);

      wrongListOfPairsOfListOfRatingsWithExceptionMessage
            .add(Pair.of(TestUtils.getStringByReflectionSafely(AMetric.class, REFLECTION_ERROR_UNEQUAL_VECTOR_SIZES),
                  Pair.of(Pair.of(firstRatings, secondRatings), 148 / 231.0)));

      List<IVector> firstRatings2 = new ArrayList<>();
      List<IVector> secondRatings2 = new ArrayList<>();
      double[] firstRating2 = { 0, 0, 0, 0, 0 };
      double[] secondRating2 = { 0, 0, 0, 0, 0 };
      addVectorsToLists(firstRatings2, secondRatings2, firstRating2, secondRating2);

      wrongListOfPairsOfListOfRatingsWithExceptionMessage
            .add(Pair.of(TestUtils.getStringByReflectionSafely(MacroLabelBasedFMeasure.class, REFLECTION_ERROR_CANNOT_COMPUTE_F_MEASURE),
                  Pair.of(Pair.of(firstRatings2, secondRatings2), 148 / 231.0)));

      return wrongListOfPairsOfListOfRatingsWithExceptionMessage;
   }

}
