package de.upb.cs.is.jpl.api.algorithm.baselearner.classification.supportvectormachine;


import de.upb.cs.is.jpl.api.algorithm.baselearner.ABaseLearningModel;
import de.upb.cs.is.jpl.api.algorithm.baselearner.dataset.BaselearnerInstance;
import de.upb.cs.is.jpl.api.dataset.IInstance;
import de.upb.cs.is.jpl.api.exception.algorithm.PredictionFailedException;
import de.upb.cs.is.jpl.api.math.linearalgebra.DenseDoubleVector;
import de.upb.cs.is.jpl.api.math.linearalgebra.IVector;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;


/**
 * This class implements Support Vector Machine Learning model which contains the trained
 * {@link svm_model} and weight vectors of the trained model.
 * 
 * @author Pritha Gupta
 *
 */
public class SupportVectorMachineLearningModel extends ABaseLearningModel<Double> {

   private static final String MODEL_TRAINED_IS_OF_TYPE_REGRESSION = "The model trained is of type regression, which is not supported by this implementation.";
   private svm_model svmTrainedModel;
   private IVector weightVector;
   private double bias;


   /**
    * Creates a new {@link SupportVectorMachineLearningModel} with the with the empty parameters.
    * 
    * @param numberOfFeaturesTrainedOne the number of feature vectors the classifier is trained on
    */
   public SupportVectorMachineLearningModel(int numberOfFeaturesTrainedOne) {
      super(numberOfFeaturesTrainedOne);
      svmTrainedModel = new svm_model();
      weightVector = new DenseDoubleVector(new double[0]);
      bias = 0.0;
   }


   /**
    * Creates a new {@link SupportVectorMachineLearningModel} with the with the default parameter
    * values and {@link svm_model} and weight vector of the trained model.
    * 
    * @param svmTrainedModel the {@link svm_model} trained on some dataset
    * @param weights the double weight vector
    * @param bias the bias of the learner
    * @param numberOfFeaturesTrainedOne the number of feature vectors the classifier is trained on
    */
   public SupportVectorMachineLearningModel(svm_model svmTrainedModel, IVector weights, double bias, int numberOfFeaturesTrainedOne) {
      super(numberOfFeaturesTrainedOne);
      this.svmTrainedModel = svmTrainedModel;
      this.weightVector = weights;
      this.bias = bias;
   }


   @Override
   public Double predict(IInstance<?, ?, ?> instance) throws PredictionFailedException {
      BaselearnerInstance baseLearnerinstance = (BaselearnerInstance) instance;
      double[] instanceFeatures = baseLearnerinstance.getContextFeatureVector();
      svm_node[] nodes = new svm_node[instanceFeatures.length];
      for (int i = 0; i < instanceFeatures.length; i++) {
         svm_node node = new svm_node();
         node.index = i;
         node.value = instanceFeatures[i];
         nodes[i] = node;
      }
      int totalClasses = 2;
      int[] labels = new int[totalClasses];
      svm.svm_get_labels(svmTrainedModel, labels);
      double[] probabilityEstimates = new double[totalClasses];
      if (svmTrainedModel.param.svm_type == svm_parameter.ONE_CLASS || svmTrainedModel.param.svm_type == svm_parameter.EPSILON_SVR
            || svmTrainedModel.param.svm_type == svm_parameter.NU_SVR) {
         throw new PredictionFailedException(MODEL_TRAINED_IS_OF_TYPE_REGRESSION);
      } else {
         return svm.svm_predict_probability(svmTrainedModel, nodes, probabilityEstimates);
      }
   }


   /**
    * Sets the {@link svm_model} trained model for this base learner.
    * 
    * @param svmTrainedModel the trained SVM model
    */
   public void setSvmTrainedModel(svm_model svmTrainedModel) {
      this.svmTrainedModel = svmTrainedModel;
   }


   @Override
   public double getBias() {
      return bias;
   }


   @Override
   public IVector getWeightVector() {
      return weightVector;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      long temp;
      temp = Double.doubleToLongBits(bias);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      result = prime * result + ((svmTrainedModel == null) ? 0 : svmTrainedModel.hashCode());
      result = prime * result + ((weightVector == null) ? 0 : weightVector.hashCode());
      return result;
   }


   @Override
   public boolean equals(Object secondObject) {
      if (super.equals(secondObject) && secondObject instanceof SupportVectorMachineLearningModel) {
         SupportVectorMachineLearningModel castedObject = (SupportVectorMachineLearningModel) secondObject;
         if (svmTrainedModel.equals(castedObject.svmTrainedModel)
               && (Double.doubleToLongBits(bias) == Double.doubleToLongBits(castedObject.bias))
               && weightVector.equals(castedObject.weightVector))
            return true;
      }
      return false;
   }


}
