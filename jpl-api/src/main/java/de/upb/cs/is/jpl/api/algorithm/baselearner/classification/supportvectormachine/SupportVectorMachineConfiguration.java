package de.upb.cs.is.jpl.api.algorithm.baselearner.classification.supportvectormachine;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import de.upb.cs.is.jpl.api.algorithm.AAlgorithmConfiguration;
import de.upb.cs.is.jpl.api.configuration.json.IJsonConfiguration;
import de.upb.cs.is.jpl.api.exception.configuration.json.ParameterValidationFailedException;
import de.upb.cs.is.jpl.api.util.StringUtils;
import libsvm.svm_parameter;
import libsvm.svm_problem;


/**
 * This configuration stores all parameters used by {@link SupportVectorMachineClassification}. The
 * current implementation make use of the {@link libsvm.svm} parameters to set different parameters
 * for {@link svm_problem}.
 * 
 * @author Pritha Gupta
 *
 */
public class SupportVectorMachineConfiguration extends AAlgorithmConfiguration {


   private static final String DEFAULT_CONFIGURATION_FILE_NAME = "baselearner" + StringUtils.FORWARD_SLASH + "classification"
         + StringUtils.FORWARD_SLASH + "supportvectormachine" + StringUtils.FORWARD_SLASH + "support_vector_machine_classification";


   private static final String INVALID_KERNEL_TYPE = "The kernel type provided is invalid, it can be either 0:LINEAR, 1: POLY, 2: RBF and 3: SIGMOID.";
   private static final String INVALID_SVM_TYPE = "The svm type provided is invalid, it can be either 0:CSVC, 1: NU_SVR, 2: ONE_CLASS, 3: EPSILON_SVR and 4: NU_SVR.";

   private static final String INVALID_MARGIN_TYPE = "The margin type provided is invalid, it can not be negative.";
   private static final String INVALID_CACHE_SIZE = "The cache size provided is invalid,it cannot be negative.";
   private static final String INVALID_STOPPING_CRITERIA = "The stopping criteria provided is invalid, it cannot be negative.";
   private static final String INVALID_GAMMA = "The value of gamma provided is invalid for %s kernel.";
   private static final String INVALID_COEF = "The coefficient value provided is invalid for %s kernel.";
   private static final String INVALID_PROBABILITY = "The probability provided is invalid, it can be zero or one.";
   private static final String INVALID_SHRINKING_HEURISTICS = "The shrinking heuristics provided is invalid, it can be zero or one.";
   private static final String INVALID_DEGREE = "The value of degree provided is invalid, it has to be greater than zero.";
   private static final String INVALID_WEIGTHS = "The number of elements in the weight vector cannot be grater than 2 as we have this implementaiton only for classification.";
   private static final String INVALID_NU_FOR_SVMTYPE = "Invalid value of nu for svm type %s.";


   @SerializedName(SupportVectorMachineConfigurationData.SVM_TYPE)
   private int svmType = Integer.MAX_VALUE; // CSVC, NuSVC

   @SerializedName(SupportVectorMachineConfigurationData.KERNEL_TYPE)
   private int kernelType = Integer.MAX_VALUE; // Poly, rbf, sigmoid,linear

   @SerializedName(SupportVectorMachineConfigurationData.CACHE_SIZE)
   private double cacheSize = Double.MAX_VALUE; // in MB

   @SerializedName(SupportVectorMachineConfigurationData.STOPPING_CRITERIA)
   private double stoppingCriteria = Double.MAX_VALUE; // stopping criteria

   @SerializedName(SupportVectorMachineConfigurationData.C_MARGIN)
   private double cMargin = Double.MAX_VALUE; // for C_SVC

   @SerializedName(SupportVectorMachineConfigurationData.PROBABILITY)
   private int applyProbabilityEstimate = Integer.MAX_VALUE; // do probability estimates

   @SerializedName(SupportVectorMachineConfigurationData.SHRINKING)
   private int applyShrinkingHeuristics = Integer.MAX_VALUE; // use the shrinking heuristics

   @SerializedName(SupportVectorMachineConfigurationData.DEGREE)
   private int degreeForPolyKernel = Integer.MAX_VALUE; // for poly kernel

   @SerializedName(SupportVectorMachineConfigurationData.COEFF)
   private double extraCoeffForPolyAndSigmoidKernel = Double.MAX_VALUE; // for poly/sigmoid

   @SerializedName(SupportVectorMachineConfigurationData.GAMMA)
   private double gammaForPolyRbfAndSigmoid = Double.MAX_VALUE; // for poly/rbf/sigmoid


   @SerializedName(SupportVectorMachineConfigurationData.M_NU) // for NU_SVC
   private double mNu = Double.MAX_VALUE;

   @SerializedName(SupportVectorMachineConfigurationData.WEIGHT)
   private double[] weightsForClasses = new double[] { 1.0, 1.0 };

   /**
    * The indexes of different {@link svm_parameter#kernel_type} to be used in
    * {@link SupportVectorMachineConfiguration}.
    */
   private static final List<Integer> kernelTypes = new ArrayList<>(
         Arrays.asList(SupportVectorMachineConfigurationData.LINEAR, SupportVectorMachineConfigurationData.POLY,
               SupportVectorMachineConfigurationData.RBF, SupportVectorMachineConfigurationData.SIGMOID));
   /**
    * The string values of different {@link svm_parameter#kernel_type} to be used in
    * {@link SupportVectorMachineConfiguration}.
    */
   private static final List<String> kernelTypesStrings = new ArrayList<>(Arrays.asList("LINEAR", "POLY", "RBF", "SIGMOID"));

   /**
    * The indexes of different {@link svm_parameter#svm_type} to be used in
    * {@link SupportVectorMachineConfiguration}.
    */
   private static final List<Integer> svmTypes = new ArrayList<>(
         Arrays.asList(SupportVectorMachineConfigurationData.C_SVC, SupportVectorMachineConfigurationData.NU_SVC));
   /**
    * The string values of different {@link svm_parameter#svm_type} to be used in
    * {@link SupportVectorMachineConfiguration}.
    */
   private static final List<String> svmTypesStrings = new ArrayList<>(Arrays.asList("C_SVC", "NU_SVC"));


   /**
    * Creates a default Support Vector Machine configuration.
    */
   public SupportVectorMachineConfiguration() {
      super(DEFAULT_CONFIGURATION_FILE_NAME);
   }


   @Override
   protected void validateParameters() throws ParameterValidationFailedException {
      if (!svmTypes.contains(svmType)) {
         throw new ParameterValidationFailedException(INVALID_SVM_TYPE);
      }
      if (!kernelTypes.contains(kernelType)) {
         throw new ParameterValidationFailedException(INVALID_KERNEL_TYPE);
      }
      if (Double.compare(cMargin, 0.0) < 0) {
         throw new ParameterValidationFailedException(INVALID_MARGIN_TYPE);
      }
      if (Double.compare(cacheSize, 0.0) < 0) {
         throw new ParameterValidationFailedException(INVALID_CACHE_SIZE);
      }
      if (Double.compare(stoppingCriteria, 0.0) < 0) {
         throw new ParameterValidationFailedException(INVALID_STOPPING_CRITERIA);
      }
      validateExtraParameters();
   }


   /**
    * Validates other extra parameters for the {@link SupportVectorMachineClassification}. The extra
    * parameters are only to define the optimization. For example gamma value is only valid for
    * poly,rbf and sigmoid kernel. the shrinking and probability defines whether to use probability
    * estimated or shrinking heuristics for the Support Vector Machine training method. And the
    * degree is only valid for poly kernel and weights of each class.
    * 
    * @throws ParameterValidationFailedException if the parameter validation process fails
    */
   private void validateExtraParameters() throws ParameterValidationFailedException {

      if (kernelType != SupportVectorMachineConfigurationData.LINEAR && Double.compare(gammaForPolyRbfAndSigmoid, -1.0) < 0) {
         throw new ParameterValidationFailedException(String.format(INVALID_GAMMA, kernelTypesStrings.get(kernelType)));
      }
      if ((kernelType == SupportVectorMachineConfigurationData.POLY || kernelType == SupportVectorMachineConfigurationData.SIGMOID)
            && Double.compare(extraCoeffForPolyAndSigmoidKernel, -1.0) < 0) {
         throw new ParameterValidationFailedException(String.format(INVALID_COEF, kernelTypesStrings.get(kernelType)));
      }
      if (applyProbabilityEstimate < 0 || applyProbabilityEstimate > 2) {
         throw new ParameterValidationFailedException(INVALID_PROBABILITY);
      }
      if (applyShrinkingHeuristics < 0 || applyShrinkingHeuristics > 2) {
         throw new ParameterValidationFailedException(INVALID_SHRINKING_HEURISTICS);
      }
      if (kernelType == SupportVectorMachineConfigurationData.POLY && degreeForPolyKernel < 2) {
         throw new ParameterValidationFailedException(INVALID_DEGREE);
      }
      if ((svmType == SupportVectorMachineConfigurationData.NU_SVC) && mNu < 0.0) {
         throw new ParameterValidationFailedException(String.format(INVALID_NU_FOR_SVMTYPE, svmTypesStrings.get(svmType)));
      }

      if (weightsForClasses.length != 2 || (weightsForClasses[0] < 0.0) || (weightsForClasses[1] < 0.0)) {
         throw new ParameterValidationFailedException(INVALID_WEIGTHS);
      }

   }


   @Override
   protected void copyValues(IJsonConfiguration configuration) {
      SupportVectorMachineConfiguration castedConfiguration = (SupportVectorMachineConfiguration) configuration;
      if (castedConfiguration.svmType < Integer.MAX_VALUE) {
         this.svmType = castedConfiguration.svmType;
      }
      if (castedConfiguration.kernelType < Integer.MAX_VALUE) {
         this.kernelType = castedConfiguration.kernelType;
      }
      if (Double.compare(castedConfiguration.cMargin, Double.MAX_VALUE) != 0) {
         this.cMargin = castedConfiguration.cMargin;
      }
      if (Double.compare(castedConfiguration.cacheSize, Double.MAX_VALUE) != 0) {
         this.cacheSize = castedConfiguration.cacheSize;
      }
      if (Double.compare(castedConfiguration.stoppingCriteria, Double.MAX_VALUE) != 0) {
         this.stoppingCriteria = castedConfiguration.stoppingCriteria;
      }
      if (Double.compare(castedConfiguration.gammaForPolyRbfAndSigmoid, Double.MAX_VALUE) != 0) {
         this.gammaForPolyRbfAndSigmoid = castedConfiguration.gammaForPolyRbfAndSigmoid;
      }
      if (Double.compare(castedConfiguration.extraCoeffForPolyAndSigmoidKernel, Double.MAX_VALUE) != 0) {
         this.extraCoeffForPolyAndSigmoidKernel = castedConfiguration.extraCoeffForPolyAndSigmoidKernel;
      }
      if (castedConfiguration.applyProbabilityEstimate < Integer.MAX_VALUE) {
         this.applyProbabilityEstimate = castedConfiguration.applyProbabilityEstimate;
      }
      if (castedConfiguration.applyShrinkingHeuristics < Integer.MAX_VALUE) {
         this.applyShrinkingHeuristics = castedConfiguration.applyShrinkingHeuristics;
      }
      if (!Arrays.equals(castedConfiguration.weightsForClasses, weightsForClasses)) {
         this.weightsForClasses = Arrays.copyOf(castedConfiguration.weightsForClasses, this.weightsForClasses.length);
      }
      if (Double.compare(castedConfiguration.mNu, Double.MAX_VALUE) != 0) {
         this.mNu = castedConfiguration.mNu;
      }

   }


   /**
    * This method creates the parameters for which the {@link svm_problem} needs to be trained after
    * reading it from the {@link SupportVectorMachineConfiguration}.
    * 
    * @return the {@link svm_parameter} for training
    */
   public svm_parameter createSVMParametersFromConfiguration() {
      svm_parameter svmParameters = new svm_parameter();
      svmParameters.probability = this.applyProbabilityEstimate;
      svmParameters.gamma = this.gammaForPolyRbfAndSigmoid;
      svmParameters.C = this.cMargin;
      svmParameters.svm_type = this.svmType;
      svmParameters.kernel_type = this.kernelType;
      svmParameters.cache_size = this.cacheSize;
      svmParameters.eps = this.stoppingCriteria;
      svmParameters.shrinking = this.applyShrinkingHeuristics;
      svmParameters.nr_weight = 2;
      svmParameters.weight_label = new int[] { 1, -1 };
      if (this.kernelType == SupportVectorMachineConfigurationData.POLY || this.kernelType == SupportVectorMachineConfigurationData.SIGMOID)
         svmParameters.coef0 = this.extraCoeffForPolyAndSigmoidKernel;
      svmParameters.weight = Arrays.copyOf(weightsForClasses, this.weightsForClasses.length);
      if (this.kernelType == SupportVectorMachineConfigurationData.POLY)
         svmParameters.degree = this.degreeForPolyKernel;
      if (svmType == SupportVectorMachineConfigurationData.NU_SVC)
         svmParameters.nu = mNu;
      return svmParameters;
   }


   /**
    * Returns the type of Support Vector Machine used.
    * 
    * @return the svmType
    */
   public int getSvmType() {
      return svmType;
   }


   /**
    * Sets the type of Support Vector Machine used.
    * 
    * @param svmType the svmType to set
    */
   public void setSvmType(int svmType) {
      this.svmType = svmType;
   }


   /**
    * Returns the Kernel Type of the {@link SupportVectorMachineClassification}.
    * 
    * @return the kernel type used by the Support Vector Machine
    */
   public int getKernelType() {
      return kernelType;
   }


   /**
    * Sets the Kernel Type of the {@link SupportVectorMachineClassification}.
    * 
    * @param kernelType the kernel type of the Support Vector Machine
    */
   public void setKernelType(int kernelType) {
      this.kernelType = kernelType;
   }


   /**
    * Returns the kernel cache size.
    * 
    * @return the kernel cache size
    */
   public double getCacheSize() {
      return cacheSize;
   }


   /**
    * Sets the kernel cache size.
    * 
    * @param cacheSize the cache size
    */
   public void setCacheSize(double cacheSize) {
      this.cacheSize = cacheSize;
   }


   /**
    * Returns the tolerance for the stopping criteria.
    * 
    * @return the value of tolerance for the stopping criteria
    */
   public double getStoppingCriteria() {
      return stoppingCriteria;
   }


   /**
    * Sets the tolerance for the stopping criteria.
    * 
    * @param stoppingCriteria the value of the stopping criteria
    */
   public void setStoppingCriteria(double stoppingCriteria) {
      this.stoppingCriteria = stoppingCriteria;
   }


   /**
    * Returns the C parameter tells the Support Vector Machine optimization how much you want to
    * avoid mis-classifying each training example.
    * 
    * @return the value of C parameter
    */
   public double getcMargin() {
      return cMargin;
   }


   /**
    * Set the C parameter value that tells the Support Vector Machine optimization how much you want
    * to avoid mis-classifying each training example.
    * 
    * @param cMargin the value of C parameter
    */
   public void setcMargin(double cMargin) {
      this.cMargin = cMargin;
   }


   /**
    * Returns {@code 1} if probability estimates are applied else {@code 0} for SVC or SVR model.
    * 
    * @return {@code 1} if probability estimates are applied else {@code 0}
    */
   public int getApplyProbabilityEstimate() {
      return applyProbabilityEstimate;
   }


   /**
    * Sets the value of if the probability estimates are applied for SVC or SVR model, {@code 1} if
    * probability estimates are applied else {@code 0}.
    * 
    * @param applyProbabilityEstimate the probability estimate
    */
   public void setApplyProbabilityEstimate(int applyProbabilityEstimate) {
      this.applyProbabilityEstimate = applyProbabilityEstimate;
   }


   /**
    * Returns {@code 1} if shrinking heuristics are applied else {@code 0}.
    * 
    * @return {@code 1} if shrinking heuristics are applied else {@code 0}
    */
   public int getApplyShrinkingHeuristics() {
      return applyShrinkingHeuristics;
   }


   /**
    * Sets the value of if the shrinking heuristics are applied for SVC , {@code 1} if probability
    * estimates are applied else {@code 0}.
    * 
    * @param applyShrinkingHeuristics the value of shrinking heuristics
    */
   public void setApplyShrinkingHeuristics(int applyShrinkingHeuristics) {
      this.applyShrinkingHeuristics = applyShrinkingHeuristics;
   }


   /**
    * Gets the degree of the polynomial kernel function (‘poly’). Ignored by all other kernels.
    * 
    * @return the degree for the poly kernel
    */
   public int getDegreeForPolyKernel() {
      return degreeForPolyKernel;
   }


   /**
    * Gets the degree of the polynomial kernel function (‘poly’). Ignored by all other kernels.
    * 
    * @param degreeForPolyKernel the degree for the poly kernel
    */
   public void setDegreeForPolyKernel(int degreeForPolyKernel) {
      this.degreeForPolyKernel = degreeForPolyKernel;
   }


   /**
    * Returns the Kernel coefficient for poly’ and ‘sigmoid’ kernels. Default value is {@code 0}.
    * 
    * @return the value of extra coefficient for poly’ and ‘sigmoid’ kernel.
    */
   public double getExtraCoeffForPolyAndSigmoidKernel() {
      return extraCoeffForPolyAndSigmoidKernel;
   }


   /**
    * Sets the Kernel coefficient for poly’ and ‘sigmoid’ kernels, if not set default is {@code 0}.
    * 
    * @param extraCoeffForPolyAndSigmoidKernel the value of extra coefficient for poly’ and
    *           ‘sigmoid’ kernel.
    */
   public void setExtraCoeffForPolyAndSigmoidKernel(double extraCoeffForPolyAndSigmoidKernel) {
      this.extraCoeffForPolyAndSigmoidKernel = extraCoeffForPolyAndSigmoidKernel;
   }


   /**
    * Returns the gamma value for rbf ,poly’ and ‘sigmoid’ kernels. If gamma is ‘-1’ then
    * 1/n_features will be used instead.
    * 
    * @return the gamma value
    */
   public double getGammaForPolyRbfAndSigmoid() {
      return gammaForPolyRbfAndSigmoid;
   }


   /**
    * Sets the gamma value for rbf ,poly’ and ‘sigmoid’ kernels. If gamma is ‘-1’ then 1/n_features
    * will be used instead.
    * 
    * @param gammaForPolyRbfAndSigmoid the gamma value
    */
   public void setGammaForPolyRbfAndSigmoid(double gammaForPolyRbfAndSigmoid) {
      this.gammaForPolyRbfAndSigmoid = gammaForPolyRbfAndSigmoid;
   }


   /**
    * Returns the weights defining the the weights for (1,-1) label. Example is (15,1), set the
    * parameter C of class i to weight*C in C-SVC (C*15) for label 1 while its C*1 for label -1.
    * 
    * @return the weights defining the weights for (1,-1)
    */
   public double[] getWeightsForClasses() {
      return weightsForClasses;
   }


   /**
    * Sets the weights defining the the weights for (1,-1) label. Example is (15,1), set the
    * parameter C of class i to weight*C in C-SVC (C*15) for label 1 while its C*1 for label -1.
    * 
    * @param weightsForClasses the weights defining the weights for (1,-1)
    */
   public void setWeightsForClasses(double[] weightsForClasses) {
      this.weightsForClasses = weightsForClasses;
   }


   /**
    * Sets nu of nu-SVC (default 0.5).
    * 
    * @param mNu the new nu value
    */
   public void setNu(double mNu) {
      this.mNu = mNu;
   }


   /**
    * Returns nu of nu-SVC, one-class SVM, and nu-SVR (default 0.5)
    * 
    * @return the current nu value
    */
   public double getNu() {
      return mNu;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + applyProbabilityEstimate;
      result = prime * result + applyShrinkingHeuristics;
      long temp;
      temp = Double.doubleToLongBits(cMargin);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      temp = Double.doubleToLongBits(cacheSize);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      result = prime * result + degreeForPolyKernel;
      temp = Double.doubleToLongBits(extraCoeffForPolyAndSigmoidKernel);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      temp = Double.doubleToLongBits(gammaForPolyRbfAndSigmoid);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      result = prime * result + kernelType;

      result = prime * result + (int) (temp ^ (temp >>> 32));
      temp = Double.doubleToLongBits(mNu);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      temp = Double.doubleToLongBits(stoppingCriteria);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      result = prime * result + svmType;
      result = prime * result + Arrays.hashCode(weightsForClasses);
      return result;
   }


   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (getClass() != obj.getClass())
         return false;
      SupportVectorMachineConfiguration other = (SupportVectorMachineConfiguration) obj;
      if (applyProbabilityEstimate != other.applyProbabilityEstimate)
         return false;
      if (applyShrinkingHeuristics != other.applyShrinkingHeuristics)
         return false;
      if (Double.doubleToLongBits(cMargin) != Double.doubleToLongBits(other.cMargin))
         return false;
      if (Double.doubleToLongBits(cacheSize) != Double.doubleToLongBits(other.cacheSize))
         return false;
      if (degreeForPolyKernel != other.degreeForPolyKernel)
         return false;
      if (Double.doubleToLongBits(extraCoeffForPolyAndSigmoidKernel) != Double.doubleToLongBits(other.extraCoeffForPolyAndSigmoidKernel))
         return false;
      if (Double.doubleToLongBits(gammaForPolyRbfAndSigmoid) != Double.doubleToLongBits(other.gammaForPolyRbfAndSigmoid))
         return false;
      if (kernelType != other.kernelType)
         return false;

      if (Double.doubleToLongBits(mNu) != Double.doubleToLongBits(other.mNu))
         return false;
      if (Double.doubleToLongBits(stoppingCriteria) != Double.doubleToLongBits(other.stoppingCriteria))
         return false;
      if (svmType != other.svmType)
         return false;
      if (!Arrays.equals(weightsForClasses, other.weightsForClasses))
         return false;
      return true;
   }


   @Override
   public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(SupportVectorMachineConfigurationData.SVM_TYPE + StringUtils.COLON);
      stringBuilder.append(svmTypesStrings.get(svmType));
      stringBuilder.append(
            StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND + SupportVectorMachineConfigurationData.KERNEL_TYPE + StringUtils.COLON);
      stringBuilder.append(kernelTypesStrings.get(kernelType));
      stringBuilder
            .append(StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND + SupportVectorMachineConfigurationData.CACHE_SIZE + StringUtils.COLON);
      stringBuilder.append(cacheSize);
      stringBuilder.append(
            StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND + SupportVectorMachineConfigurationData.STOPPING_CRITERIA + StringUtils.COLON);
      stringBuilder.append(stoppingCriteria);
      stringBuilder
            .append(StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND + SupportVectorMachineConfigurationData.C_MARGIN + StringUtils.COLON);
      stringBuilder.append(cMargin);
      stringBuilder.append(
            StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND + SupportVectorMachineConfigurationData.PROBABILITY + StringUtils.COLON);
      stringBuilder.append(applyProbabilityEstimate);
      stringBuilder
            .append(StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND + SupportVectorMachineConfigurationData.SHRINKING + StringUtils.COLON);
      stringBuilder.append(applyShrinkingHeuristics);
      stringBuilder
            .append(StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND + SupportVectorMachineConfigurationData.DEGREE + StringUtils.COLON);
      stringBuilder.append(degreeForPolyKernel);
      stringBuilder
            .append(StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND + SupportVectorMachineConfigurationData.COEFF + StringUtils.COLON);
      stringBuilder.append(extraCoeffForPolyAndSigmoidKernel);
      stringBuilder
            .append(StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND + SupportVectorMachineConfigurationData.GAMMA + StringUtils.COLON);
      stringBuilder.append(gammaForPolyRbfAndSigmoid);
      stringBuilder
            .append(StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND + SupportVectorMachineConfigurationData.M_NU + StringUtils.COLON);
      stringBuilder.append(mNu);
      stringBuilder
            .append(StringUtils.COMMA_WITH_SINGLE_WHITESPACE_BEHIND + SupportVectorMachineConfigurationData.WEIGHT + StringUtils.COLON);
      stringBuilder.append(Arrays.toString(weightsForClasses));
      return stringBuilder.toString();
   }

}
