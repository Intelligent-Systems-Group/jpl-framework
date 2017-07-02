package de.upb.cs.is.jpl.api.algorithm.baselearner.classification.supportvectormachine;


import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.algorithm.baselearner.ABaselearnerAlgorithm;
import de.upb.cs.is.jpl.api.algorithm.baselearner.EBaseLearner;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerDataset;
import de.upb.cs.is.jpl.api.algorithm.learningalgorithm.ILearningModel;
import de.upb.cs.is.jpl.api.dataset.IDataset;
import de.upb.cs.is.jpl.api.exception.algorithm.TrainModelsFailedException;
import de.upb.cs.is.jpl.api.math.linearalgebra.DenseDoubleVector;
import de.upb.cs.is.jpl.api.util.IOUtils;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;


/**
 * This class implements SVM base learner which uses Support Vector Machine implementation done in
 * {@link libsvm.svm}.
 * 
 * @author Pritha Gupta
 * @see <a href="http://www.csie.ntu.edu.tw/~r94100/libsvm-2.8/README">LIBSVM</a>
 *
 */
public class SupportVectorMachineClassification extends ABaselearnerAlgorithm<SupportVectorMachineConfiguration> {
   private static final String DATASET_NOT_OF_TYPE_CLASSIFICATION_ERROR_MESSAGE = "The dataset provided is not of type classification";
   private static final String SVM_DOESNOT_SUPPORT_INSTACE_WEIGHTS_ERROR_MESSAGE = "The support vector machine classification cannot support weighted instances";

   private static final Logger logger = LoggerFactory.getLogger(SupportVectorMachineClassification.class);


   /**
    * Creates a new Support Vector Machine classification base learner with the enum identifier.
    */
   public SupportVectorMachineClassification() {
      super(EBaseLearner.SVM_CLASSIFICATION.getBaseLearnerIdentifier());
   }


   @Override
   protected AAlgorithmConfiguration createDefaultAlgorithmConfiguration() {
      return new SupportVectorMachineConfiguration();
   }


   @Override
   protected ILearningModel<?> performTraining(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      BaselearnerDataset baseLearnerdataset = (BaselearnerDataset) dataset;
      assertCompatibilityOfBaseLearnerDatasetWithSVM(baseLearnerdataset);
      double[][] features = baseLearnerdataset.getFeatureVectors();
      double[] classes = baseLearnerdataset.getCorrectResults();
      svm_problem svmProblem = new svm_problem();
      int recordCount = baseLearnerdataset.getNumberOfInstances();
      int featureCount = baseLearnerdataset.getNumberOfFeatures();
      svm.rand.setSeed(1234);
      // Setting up the SVM Problem with features and classes
      svmProblem.y = new double[recordCount];
      svmProblem.l = recordCount;
      svmProblem.x = new svm_node[recordCount][featureCount];
      for (int i = 0; i < recordCount; i++) {
         double[] featuresForOneInstance = features[i];
         svmProblem.x[i] = new svm_node[featuresForOneInstance.length];
         for (int j = 0; j < featuresForOneInstance.length; j++) {
            svm_node node = new svm_node();
            node.index = j;
            node.value = featuresForOneInstance[j];
            svmProblem.x[i][j] = node;
         }
         svmProblem.y[i] = classes[i];
      }

      svm_parameter svmParameters = configuration.createSVMParametersFromConfiguration();

      ByteArrayOutputStream outputStream = IOUtils.redirectOutputStream();
      svm_model svmModel = svm.svm_train(svmProblem, svmParameters);
      IOUtils.undoOutputStreamRedirection();
      logger.debug(outputStream.toString());
      double[] weights = new double[featureCount + 1];
      if (svmParameters.svm_type == svm_parameter.C_SVC) {
         weights = createWeightVectorFromSVMModel(svmModel, featureCount);
      }
      DenseDoubleVector weightVector = new DenseDoubleVector(Arrays.copyOfRange(weights, 0, weights.length - 1));
      return new SupportVectorMachineLearningModel(svmModel, weightVector, weights[weights.length - 1], featureCount);
   }


   @Override
   public SupportVectorMachineLearningModel train(IDataset<?, ?, ?> dataset) throws TrainModelsFailedException {
      return (SupportVectorMachineLearningModel) super.train(dataset);
   }


   /**
    * Checks the compatibility of the {@link BaselearnerDataset} with the current implementation of
    * {@link SupportVectorMachineClassification}.
    * 
    * @param baseLearnerdataset the base learner dataset
    * @throws TrainModelsFailedException if the dataset is not of type classification or it has
    *            weights
    */
   private void assertCompatibilityOfBaseLearnerDatasetWithSVM(BaselearnerDataset baseLearnerdataset) throws TrainModelsFailedException {
      for (double classValue : baseLearnerdataset.getCorrectResults()) {
         if (!(Double.compare(classValue, 1.0) == 0 || Double.compare(classValue, -1.0) == 0)) {
            throw new TrainModelsFailedException(DATASET_NOT_OF_TYPE_CLASSIFICATION_ERROR_MESSAGE);
         }
      }
      for (double weightValue : baseLearnerdataset.getInstanceWeights()) {
         if (Double.compare(weightValue, 1.0) != 0) {
            throw new TrainModelsFailedException(SVM_DOESNOT_SUPPORT_INSTACE_WEIGHTS_ERROR_MESSAGE);
         }
      }
   }


   /**
    * This method creates a weight vector in the form of double array.
    * 
    * @param model {@link svm_model}
    * @param featureCount the number of features in the dataset
    * @return the weight vector in a double array
    */
   private double[] createWeightVectorFromSVMModel(svm_model model, int featureCount) {
      double[] weights;
      double[][][] weightList = createWeightListForSVMModel(model, featureCount);

      weights = new double[featureCount + 1];
      for (int i = 0; i < model.nr_class - 1; ++i) {
         for (int j = i + 1, k = i; j < model.nr_class; ++j, ++k) {
            for (int m = 0; m < featureCount; ++m) {
               weights[m] = weightList[i][k][m] + weightList[j][i][m];
            }
         }
      }
      if (model.rho.length != 0) {
         weights[featureCount] = -model.rho[0];
      }
      return weights;

   }


   /**
    * This method provides the weight list 3d array used to build the weight vector for the trained
    * {@link svm_model}.
    * 
    * @param model {@link svm_model}
    * @param featureCount the number of features in the dataset
    * @return the weight 3d array
    */
   public double[][][] createWeightListForSVMModel(svm_model model, int featureCount) {
      double[][] coef = model.sv_coef;
      double[][] probabilities = new double[model.SV.length][featureCount];
      double[][][] weightList = new double[model.nr_class][model.nr_class - 1][featureCount];
      for (int i = 0; i < model.SV.length; i++) {
         for (int j = 0; j < model.SV[i].length; j++) {
            probabilities[i][model.SV[i][j].index] = model.SV[i][j].value;
         }
      }
      /* Weights are only available for linear SVMs */
      for (int i = 0; i < featureCount; ++i) {
         for (int j = 0; j < model.nr_class - 1; ++j) {
            int index = 0;
            int end;
            double acc;
            for (int k = 0; k < model.nr_class; ++k) {
               acc = 0.0;
               index += (k == 0) ? 0 : model.nSV[k - 1];
               end = index + model.nSV[k];
               for (int m = index; m < end; ++m) {
                  acc += coef[j][m] * probabilities[m][i];
               }
               weightList[k][j][i] = acc;
            }
         }
      }
      return weightList;
   }


}
