package de.upb.cs.is.jpl.api.learningproblem;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.upb.cs.is.jpl.api.evaluation.EEvaluation;


/**
 * Enumeration of all supported learning problems. If a new learning problem is added it has to be
 * added to this enumeration as the registration of evaluation metrics for learning problems is done
 * implicitly via this enumeration.
 * 
 * @author Tanja Tornede
 */
public enum ELearningProblem {

   /**
    * Represents the ordinal classification problem.
    */
   ORDINAL_CLASSIFICATION("ordinal_classification"),

   /**
    * Represents the multilabel classification problem.
    */
   MULTILABEL_CLASSIFICATION("multilabel_classification"),

   /**
    * Represents the rank aggregation problem.
    */
   RANK_AGGREGATION("rank_aggregation"),

   /**
    * Represents the object ranking problem.
    */
   OBJECT_RANKING("object_ranking"),

   /**
    * Represents the instance ranking problem.
    */
   INSTANCE_RANKING("instance_ranking"),

   /**
    * Represents the label ranking problem.
    */
   LABEL_RANKING("label_ranking"),

   /**
    * Represents the collaborative filtering problem.
    */
   COLLABORATIVE_FILTERING("collaborative_filtering");

   private String learningProblemIdentifier;


   private ELearningProblem(String learningProblemIdentifier) {
      this.learningProblemIdentifier = learningProblemIdentifier;
   }


   /**
    * Returns the {@code learningProblemIdentifier} of the instance of {@code ELearningProblem}.
    * 
    * @return the {@code learningProblemIdentifier} of the {@link ELearningProblem}
    */
   public String getLearningProblemIdentifier() {
      return this.learningProblemIdentifier;
   }


   /**
    * Returns a list of all evaluations which are supported by this learning problem.
    * 
    * @return a list of all evaluations supported by this learning problem
    */
   public List<EEvaluation> getSupportedEvaluations() {
      List<EEvaluation> supportedEvaluations = new ArrayList<>();
      for (EEvaluation evaluation : EEvaluation.getEEvaluations()) {
         if (evaluation.getLearningProblem() == this) {
            supportedEvaluations.add(evaluation);
         }
      }
      return supportedEvaluations;
   }


   /**
    * Returns the according {@code ELearningProblem} instance which has the identifier to search
    * for.
    * 
    * @param learningProblemIdentifier the identifier of the learning problem as String to search
    *           for
    * @return the instance of {@link ELearningProblem} with the given
    *         {@code learningProblemIdentifier} if found, otherwise null
    */
   public static ELearningProblem getELearningProblemByIdentifier(String learningProblemIdentifier) {
      ELearningProblem[] learningProblems = ELearningProblem.values();
      for (ELearningProblem learningProblem : learningProblems) {
         if (learningProblem.learningProblemIdentifier.equals(learningProblemIdentifier)) {
            return learningProblem;
         }
      }
      return null;
   }


   /**
    * Returns a list containing all existing {@code ELearningProblem}.
    * 
    * @return a list of all {@code ELearningProblem}
    */
   public static List<ELearningProblem> getELearningProblems() {
      return Arrays.asList(ELearningProblem.values());
   }
}
