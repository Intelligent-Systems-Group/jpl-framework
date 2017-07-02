package de.upb.cs.is.jpl.api.algorithm.baselearner;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.knearestneighbor.KNearestNeighborClassification;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear.minibatchpegasos.MiniBatchPegasosLearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear.perceptron.PerceptronLearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.linear.pocket.PocketLearningAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.logistic.LogisticClassification;
import de.upb.cs.is.jpl.api.algorithm.baselearner.classification.supportvectormachine.SupportVectorMachineClassification;
import de.upb.cs.is.jpl.api.algorithm.baselearner.regression.linear.LinearRegression;
import de.upb.cs.is.jpl.api.algorithm.baselearner.regression.logistic.LogisticRegression;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningAlgorithm;


/**
 * Enumeration of all supported base leaner in the tool. If a new algorithm is added it has to be
 * added to this enumeration if it should be available for the configuration of any current
 * {@link ILearningAlgorithm} which is using it. If the algorithm should only be used via the API,
 * there is no need to add it here.
 * 
 * @author Pritha Gupta
 *
 */
public enum EBaseLearner {
   /**
    * {@link Enum} associated with the {@link LogisticRegression} base leaner.
    */
   LOGISTIC_REGRESSION("logistic_regression", false, true, false, true) {
      @Override
      public IBaselearnerAlgorithm createBaseLearner() {
         return new LogisticRegression();
      }
   },
   /**
    * {@link Enum} associated with the {@link LinearRegression} base leaner.
    */
   LINEAR_REGRESSION("linear_regression", false, false, false, false) {
      @Override
      public IBaselearnerAlgorithm createBaseLearner() {
         return new LinearRegression();
      }

   },
   /**
    * {@link Enum} associated with the {@link PerceptronLearningAlgorithm} base leaner.
    */
   PERCEPTRON("perceptron", true, false, false, false) {
      @Override
      public IBaselearnerAlgorithm createBaseLearner() {
         return new PerceptronLearningAlgorithm();
      }
   },
   /**
    * {@link Enum} associated with the {@link PocketLearningAlgorithm} base leaner.
    */
   POCKET("pocket", true, false, false, false) {
      @Override
      public IBaselearnerAlgorithm createBaseLearner() {
         return new PocketLearningAlgorithm();
      }
   },
   /**
    * {@link Enum} associated with the {@link MiniBatchPegasosLearningAlgorithm} base leaner.
    */
   MINI_BATCH_PEGASOS("mini_batch_pegasos", true, false, false, false) {
      @Override
      public IBaselearnerAlgorithm createBaseLearner() {
         return new MiniBatchPegasosLearningAlgorithm();
      }
   },
   /**
    * {@link Enum} associated with the {@link LogisticClassification} base leaner.
    */
   LOGISTIC_CLASSIFICATION("logistic_classification", true, false, false, true) {
      @Override
      public IBaselearnerAlgorithm createBaseLearner() {
         return new LogisticClassification();
      }
   },
   /**
    * {@link Enum} associated with the {@link SupportVectorMachineClassification} base leaner.
    */
   SVM_CLASSIFICATION("svm_classification", true, false, false, false) {
      @Override
      public IBaselearnerAlgorithm createBaseLearner() {
         return new SupportVectorMachineClassification();
      }
   },
   /**
    * {@link Enum} associated with the {@link KNearestNeighborClassification} base leaner.
    */
   KNEAREST_NEIGHBOUR("k_nearest_neighbors", true, false, false, false) {
      @Override
      public IBaselearnerAlgorithm createBaseLearner() {
         return new KNearestNeighborClassification();
      }
   };

   private String baseLearnerIdentifier;

   private boolean isClassifier;
   private boolean isReturningProbability;
   private boolean isCapableOfHandlingMultipleClasses;
   private boolean isCapableOfHandlingInstanceWeights;


   /**
    * Creates a {@link EBaseLearner} with the given identifier. Also some flags are set so that it
    * can be checked later, if the given base learner is applicable for a specific learning
    * algorithm.
    * 
    * @param baseLearnerIdentifier the identifier of the base learner, has to be unique
    * @param isClassifier {@code true} if the base learner is a classifier
    * @param isReturningProbability {@code true} if the base learner is returning probabilities
    * @param isCapableOfHandlingMultipleClasses {@code true} if the base learner is able to handle
    *           multiple classes
    * @param isCapableOfHandlingInstanceWeights {@code true} if the base learner is able to handle
    *           instance weights
    */
   private EBaseLearner(String baseLearnerIdentifier, boolean isClassifier, boolean isReturningProbability,
         boolean isCapableOfHandlingMultipleClasses, boolean isCapableOfHandlingInstanceWeights) {
      this.baseLearnerIdentifier = baseLearnerIdentifier;
      this.isClassifier = isClassifier;
      this.isReturningProbability = isReturningProbability;
      this.isCapableOfHandlingMultipleClasses = isCapableOfHandlingMultipleClasses;
      this.isCapableOfHandlingInstanceWeights = isCapableOfHandlingInstanceWeights;
   }


   /**
    * Returns the baseLearner identifier of type {@link String} associated with the
    * {@code EBaseLearner}.
    * 
    * @return the baseLearner identifier associated with base learner
    */
   public String getBaseLearnerIdentifier() {
      return baseLearnerIdentifier;
   }


   /**
    * Returns whether the base learner is of type classifier or a regression.
    * 
    * @return {@code true} if the base leaner is a classifier, otherwise {@code false}
    */
   public boolean isClassifier() {
      return isClassifier;
   }


   /**
    * Returns whether the base learner is returning probabilities.
    * 
    * @return {@code true} if the base learner is returning probabilities, otherwise {@code false}
    */
   public boolean isReturningProbability() {
      return isReturningProbability;
   }


   /**
    * Returns whether the base learner is capable of handling multiple classes.
    * 
    * @return {@code true} if the base learner is able to handle multiple classes, otherwise
    *         {@code false}
    */
   public boolean isCapableOfHandlingMultipleClasses() {
      return isCapableOfHandlingMultipleClasses;
   }


   /**
    * Returns whether the base learner is capable of handling instance weights.
    * 
    * @return {@code true} if the base learner is able to handle instance weights, otherwise
    *         {@code false}
    */
   public boolean isCapableOfHandlingInstanceWeights() {
      return isCapableOfHandlingInstanceWeights;
   }


   /**
    * Returns the according {@code EBaseLearner} instance which has the identifier to search for.
    * 
    * @param baseLearnerIdentifier the identifier of the base learner as string to search for
    * @return the instance of {@link EBaseLearner} with the given {@code baseLearnerIdentifier} if
    *         found, otherwise {@code null}
    */
   public static EBaseLearner getEBaseLearnerByIdentifier(String baseLearnerIdentifier) {
      EBaseLearner[] baseLearners = EBaseLearner.values();
      for (EBaseLearner baseLearner : baseLearners) {
         if (baseLearner.baseLearnerIdentifier.equals(baseLearnerIdentifier)) {
            return baseLearner;
         }
      }
      return null;
   }


   /**
    * Returns a list containing all existing {@link EBaseLearner}.
    * 
    * @return a list of all {@link EBaseLearner}
    */
   public static List<EBaseLearner> getEBaseLearners() {
      return Arrays.asList(EBaseLearner.values());
   }


   /**
    * Returns a list containing all existing {@link EBaseLearner}, which are classifiers.
    * 
    * @return a list of all classifiers {@code EBaseLearner}
    */
   public static List<EBaseLearner> getClassifiersEBaseLearners() {
      List<EBaseLearner> classifiers = new ArrayList<>();
      for (EBaseLearner baseLearner : EBaseLearner.values()) {
         if (baseLearner.isClassifier) {
            classifiers.add(baseLearner);
         }
      }
      return classifiers;
   }


   /**
    * Returns a list containing all existing {@link EBaseLearner}, which are regression.
    * 
    * @return a list of all regression {@code EBaseLearner}
    */
   public static List<EBaseLearner> getRegressionEBaseLearners() {
      List<EBaseLearner> classifiers = new ArrayList<>();
      for (EBaseLearner baseLearner : EBaseLearner.values()) {
         if (!baseLearner.isClassifier) {
            classifiers.add(baseLearner);
         }
      }
      return classifiers;
   }


   /**
    * Returns a list containing all existing {@link EBaseLearner}, which are returning
    * probabilities.
    * 
    * @return a list of all probability returning {@link EBaseLearner}
    */
   public static List<EBaseLearner> getProbabilityReturningBaseLearners() {
      List<EBaseLearner> probabilityHandlers = new ArrayList<>();
      for (EBaseLearner baseLearner : EBaseLearner.values()) {
         if (baseLearner.isReturningProbability) {
            probabilityHandlers.add(baseLearner);
         }
      }
      return probabilityHandlers;
   }


   /**
    * Returns a list containing all existing {@link EBaseLearner}, which are able to handle multiple
    * classes.
    * 
    * @return a list of all multiple classes handling {@link EBaseLearner}
    */
   public static List<EBaseLearner> getMultipleClassesHandlingBaseLearners() {
      List<EBaseLearner> multipleClassHandlers = new ArrayList<>();
      for (EBaseLearner baseLearner : EBaseLearner.values()) {
         if (baseLearner.isCapableOfHandlingMultipleClasses) {
            multipleClassHandlers.add(baseLearner);
         }
      }
      return multipleClassHandlers;
   }


   /**
    * Returns a list containing all existing {@link EBaseLearner}, which are able to handle instance
    * weights.
    * 
    * @return a list of all instance weight handling {@link EBaseLearner}
    */
   public static List<EBaseLearner> getInstanceWeightHandlingBaseLearners() {
      List<EBaseLearner> instanceWeightHandlers = new ArrayList<>();
      for (EBaseLearner baseLearner : EBaseLearner.values()) {
         if (baseLearner.isCapableOfHandlingInstanceWeights) {
            instanceWeightHandlers.add(baseLearner);
         }
      }
      return instanceWeightHandlers;
   }


   /**
    * Create a new instance of the base learner implementation which is linked to this enumeration.
    * 
    * @return a new instance of the base learner which is linked to this enumeration
    */
   public abstract IBaselearnerAlgorithm createBaseLearner();


}
