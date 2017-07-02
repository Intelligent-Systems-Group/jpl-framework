package de.upb.cs.is.jpl.api.algorithm.learningalgorithm;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.collaborativefiltering.matrixfactorization.MatrixFactorizationLearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.collaborativefiltering.userbased.UserBasedFilteringLearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.instanceranking.combinedrankingandregression.CombinedRankingAndRegressionLearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.instanceranking.perceptronrank.PerceptronRankLearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.labelranking.instancebasedlabelranking.InstanceBasedLabelRankingLearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.labelranking.labelrankingbypairwisecomparison.LabelRankingByPairwiseComparisonLearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.multilabelclassification.binaryrelevancelearning.BinaryRelevanceLearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.multilabelclassification.classifierchains.ClassifierChainsLearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.objectranking.expectedrankregression.ExpectedRankRegression;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.objectranking.pairwiserankingalgorithm.PairwiseRankingLearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ordinalclassification.reductionframework.OrdinalClassificationReductionFramework;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ordinalclassification.simple.SimpleOrdinalClassification;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.rankaggregation.bordacount.BordaCountLearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.rankaggregation.kemenyyoung.KemenyYoungLearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.rankaggregation.plackettluce.PlackettLuceLearningAlgorithm;
import de.upb.cs.is.jpl.api.learningproblem.ELearningProblem;


/**
 * Enumeration of all supported learning algorithms. If a new algorithm is added it has to be added
 * to this enumeration if it should be available via any user interface. If the algorithm should
 * only be used via the API, there is no need to add it here. For each entry a distinct identifier
 * has to be specified. This identifier is used to determine which algorithm the user selects via
 * the user interface. Furthermore the problem which the algorithm solves has to be defined. If an
 * algorithm can solve multiple problems, please create one entry per problem and make sure to make
 * the identifier unambiguous (e.g. by including the problem name).
 * 
 * @author Pritha Gupta
 *
 */
public enum ELearningAlgorithm {
   /**
    * {@link Enum} associated with the {@link PairwiseRankingLearningAlgorithm} for the Object
    * Ranking problem.
    */
   PAIRWISE_RANKING("pairwise_ranking", ELearningProblem.OBJECT_RANKING) {
      @Override
      public ILearningAlgorithm createLearningAlgorithm() {
         return new PairwiseRankingLearningAlgorithm();
      }
   },
   /**
    * {@link Enum} associated with the {@link ExpectedRankRegression} for the Object Ranking
    * problem.
    */
   EXPECTED_RANK_REGRESSION("expected_rank_regression", ELearningProblem.OBJECT_RANKING) {
      @Override
      public ILearningAlgorithm createLearningAlgorithm() {
         return new ExpectedRankRegression();
      }
   },
   /**
    * {@link Enum} associated with the {@link PerceptronRankLearningAlgorithm} for the Instance
    * Ranking problem.
    */
   PERCEPTRON_RANK("perceptron_rank", ELearningProblem.INSTANCE_RANKING) {


      @Override
      public ILearningAlgorithm createLearningAlgorithm() {
         return new PerceptronRankLearningAlgorithm();
      }
   },
   /**
    * {@link Enum} associated with the {@link CombinedRankingAndRegressionLearningAlgorithm} for the
    * Instance Ranking problem.
    */
   COMBINED_RANKING_AND_REGRESSION("combined_ranking_and_regression", ELearningProblem.INSTANCE_RANKING) {


      @Override
      public ILearningAlgorithm createLearningAlgorithm() {
         return new CombinedRankingAndRegressionLearningAlgorithm();
      }
   },
   /**
    * {@link Enum} associated with the {@link BordaCountLearningAlgorithm} for the Rank Aggregation
    * problem.
    */
   BORDA_COUNT("borda_count", ELearningProblem.RANK_AGGREGATION) {
      @Override
      public ILearningAlgorithm createLearningAlgorithm() {
         return new BordaCountLearningAlgorithm();
      }
   },
   /**
    * {@link Enum} associated with the {@link KemenyYoungLearningAlgorithm} for the Rank Aggregation
    * problem.
    */
   KEMENEY_YOUNG("kemeny_young", ELearningProblem.RANK_AGGREGATION) {
      @Override
      public ILearningAlgorithm createLearningAlgorithm() {
         return new KemenyYoungLearningAlgorithm();
      }
   },
   /**
    * {@link Enum} associated with the {@link PlackettLuceLearningAlgorithm} for the Rank
    * Aggregation problem.
    */
   PLACKETT_LUCE("plackett_luce", ELearningProblem.RANK_AGGREGATION) {
      @Override
      public ILearningAlgorithm createLearningAlgorithm() {
         return new PlackettLuceLearningAlgorithm();
      }
   },
   /**
    * {@link Enum} associated with the {@link LabelRankingByPairwiseComparisonLearningAlgorithm} for
    * the label ranking problem.
    */
   RPC("label_ranking_pairwise_comparison", ELearningProblem.LABEL_RANKING) {
      @Override
      public ILearningAlgorithm createLearningAlgorithm() {
         return new LabelRankingByPairwiseComparisonLearningAlgorithm();
      }
   },
   /**
    * {@link Enum} associated with the {@link InstanceBasedLabelRankingLearningAlgorithm} for the
    * label ranking problem.
    */
   IB_LR("instance_based_label_ranking", ELearningProblem.LABEL_RANKING) {
      @Override
      public ILearningAlgorithm createLearningAlgorithm() {
         return new InstanceBasedLabelRankingLearningAlgorithm();
      }
   },
   /**
    * {@link Enum} associated with the naive {@link UserBasedFilteringLearningAlgorithm} algorithm
    * for the collaborative filtering problem.
    */
   USER_BASED_FILTERING("user_based_filtering", ELearningProblem.COLLABORATIVE_FILTERING) {
      @Override
      public ILearningAlgorithm createLearningAlgorithm() {
         return new UserBasedFilteringLearningAlgorithm();
      }
   },
   /**
    * {@link Enum} associated with the {@link MatrixFactorizationLearningAlgorithm} for the
    * collaborative filtering problem.
    */
   MATRIX_FACTORIZATION("matrix_factorization", ELearningProblem.COLLABORATIVE_FILTERING) {
      @Override
      public ILearningAlgorithm createLearningAlgorithm() {
         return new MatrixFactorizationLearningAlgorithm();
      }
   },
   /**
    * This entry is associated with the {@link BinaryRelevanceLearningAlgorithm} for the multilabel
    * classification learning problem.
    */
   BINARY_RELEVANCE_LEARNING("binary_relevance_learning", ELearningProblem.MULTILABEL_CLASSIFICATION) {
      @Override
      public ILearningAlgorithm createLearningAlgorithm() {
         return new BinaryRelevanceLearningAlgorithm();
      }
   },
   /**
    * This entry is associated with the {@link ClassifierChainsLearningAlgorithm} for the multilabel
    * classification learning problem.
    */
   CLASSIFIER_CHAINS("classifier_chains", ELearningProblem.MULTILABEL_CLASSIFICATION) {
      @Override
      public ILearningAlgorithm createLearningAlgorithm() {
         return new ClassifierChainsLearningAlgorithm();
      }
   },
   /**
    * This entry is associated with the {@link OrdinalClassificationReductionFramework} for the
    * ordinal classification learning problem.
    */
   ORDINAL_CLASSIFICATION_REDUCTION_FRAMEWORK("ordinal_classification_reduction_framework", ELearningProblem.ORDINAL_CLASSIFICATION) {
      @Override
      public ILearningAlgorithm createLearningAlgorithm() {
         return new OrdinalClassificationReductionFramework();
      }
   },
   /**
    * This entry is associated with the {@link SimpleOrdinalClassification} for the ordinal
    * classification learning problem.
    */
   SIMPLE_ORDINAL_CLASSIFICATION("simple_ordinal_classification", ELearningProblem.ORDINAL_CLASSIFICATION) {
      @Override
      public ILearningAlgorithm createLearningAlgorithm() {
         return new SimpleOrdinalClassification();
      }
   };


   private String learningAlgorithmIdentifier;
   private ELearningProblem learningProblem;


   /**
    * Private constructor for this enumeration.
    * 
    * @param learningAlgorithmIdentifier an unambiguous identifier of the algorithm, which is used
    *           to determine which algorithm should be used depending on the user input
    * @param learningProblem the learning problem which this algorithm solves
    */
   private ELearningAlgorithm(String learningAlgorithmIdentifier, ELearningProblem learningProblem) {
      this.learningAlgorithmIdentifier = learningAlgorithmIdentifier;
      this.learningProblem = learningProblem;
   }


   /**
    * Returns the identifier of the learning algorithm.
    * 
    * @return the identifier of the learning algorithm
    */
   public String getIdentifier() {
      return learningAlgorithmIdentifier;
   }


   /**
    * Returns the according ELearningAlgorithm instance which has the identifier to search for.
    * 
    * @param learningAlgorithmIdentifier the identifier of the learning algorithm to search for
    * @return the @link {@link ELearningAlgorithm} with the correct identifier if found,
    *         {@code null} if not found
    */
   public static ELearningAlgorithm getELearningAlgorithmByIdentifier(String learningAlgorithmIdentifier) {
      ELearningAlgorithm[] learningAlgorithms = ELearningAlgorithm.values();
      for (ELearningAlgorithm learningAlgorithm : learningAlgorithms) {
         if (learningAlgorithm.learningAlgorithmIdentifier.equals(learningAlgorithmIdentifier)) {
            return learningAlgorithm;
         }
      }
      return null;
   }


   /**
    * Returns the according ELearningAlgorithm instance which has the learningProblem to search for.
    * 
    * @param learningProblem the learning problem to search for
    * @return the {@link ELearningAlgorithm} with the correct learningProblem if found, {@code null}
    *         if not found
    */
   public static ELearningAlgorithm[] getELearningAlgorithmsByLearningProblem(ELearningProblem learningProblem) {
      ELearningAlgorithm[] learningAlgorithms = ELearningAlgorithm.values();
      List<ELearningAlgorithm> listOfLearningAlgorithms = new ArrayList<>();
      for (ELearningAlgorithm learningAlgorithm : learningAlgorithms) {
         if (learningAlgorithm.learningProblem == learningProblem) {
            listOfLearningAlgorithms.add(learningAlgorithm);
         }
      }
      return listOfLearningAlgorithms.toArray(new ELearningAlgorithm[listOfLearningAlgorithms.size()]);
   }


   /**
    * Returns a list containing all {@link ELearningAlgorithm}s.
    * 
    * @return a list containing all {@link ELearningAlgorithm}s
    */
   public static List<ELearningAlgorithm> getELearningAlgorithms() {
      return Arrays.asList(ELearningAlgorithm.values());
   }


   /**
    * Create a new instance of the learning algorithm implementation which is linked to this
    * enumeration.
    * 
    * @return a new instance of the learning algorithm which is linked to this enumeration
    */
   public abstract ILearningAlgorithm createLearningAlgorithm();

}
