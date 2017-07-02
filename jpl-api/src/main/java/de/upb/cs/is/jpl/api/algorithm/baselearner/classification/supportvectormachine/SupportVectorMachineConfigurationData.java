package de.upb.cs.is.jpl.api.algorithm.baselearner.classification.supportvectormachine;


import de.upb.cs.is.jpl.api.util.StringUtils;
import libsvm.svm_parameter;


/**
 * This class contains the json key values and utility strings required in
 * {@link SupportVectorMachineConfiguration}, which is used to set the parameters for
 * {@link svm_parameter}.
 * 
 * @author Pritha Gupta
 *
 */
public class SupportVectorMachineConfigurationData {

   /**
    * LINEAR Kernel defined be equation = u'*v
    */
   public static final int LINEAR = 0;
   /**
    * POLY kernel defined as POLY=(gamma*u'*v + coef0)^degree
    */
   public static final int POLY = 1;
   /**
    * RBF kernel defined as RBF= exp(-gamma*|u-v|^2)
    */
   public static final int RBF = 2;
   /**
    * SIGMOID kernel is defined as, SIGMOID=tanh(gamma*u'*v + coef0)
    */
   public static final int SIGMOID = 3;

   /**
    * C-Support Vector Classification (value 0).
    */
   public static final int C_SVC = 0;
   /**
    * Nu-Support Vector Classification. Similar to SVC but uses a parameter to control the number of
    * support vectors (value 1).
    */
   public static final int NU_SVC = 1;

   /**
    * Json key value specifying the svm type which will be set for value
    * {@link svm_parameter#svm_type}.
    */
   public static final String SVM_TYPE = "svm_type";
   /**
    * Json key value specifying the kernel type which will be set for value
    * {@link svm_parameter#kernel_type}.
    */
   public static final String KERNEL_TYPE = "kernel_type";
   /**
    * Json key value specifying the size of the kernel cache (in MB) to set for value
    * {@link svm_parameter#cache_size}.
    */
   public static final String CACHE_SIZE = "cache_size";
   /**
    * Json key value specifying Tolerance for stopping criterion. to set for value
    * {@link svm_parameter#eps}.
    */
   public static final String STOPPING_CRITERIA = "stopping_criteria";
   /**
    * Json key value specifying Penalty parameter C of the error term. to set for value
    * {@link svm_parameter#C}.
    */
   public static final String C_MARGIN = "c_margin";
   /**
    * Json key value weather to do probability estimates or not, to set for value
    * {@link svm_parameter#probability} (0 for no and 1 for yes).
    */
   public static final String PROBABILITY = "probability";
   /**
    * Json key value weather to use shrinking heuristics or not, to set for value
    * {@link svm_parameter#shrinking} (0 for no and 1 for yes).
    */
   public static final String SHRINKING = "shrinking";
   /**
    * Json key value degree of the polynomial kernel function (‘poly’) (Ignored by all other
    * kernels) to set for value {@link svm_parameter#degree}.
    */
   public static final String DEGREE = "degree";
   /**
    * Json key value for specifying independent term in kernel function. It is only significant in
    * ‘poly’ and ‘sigmoid’, to set for for value {@link svm_parameter#coef0}.
    */
   public static final String COEFF = "coeff";
   /**
    * Json key value for specifying weights defining the the weights for (1,-1) label. Example is
    * (15,1), set the parameter C of class i to weight*C in C-SVC (C*15) for label 1 while its C*1
    * for label -1.
    */
   public static final String WEIGHT = "weight";
   /**
    * Json key value for specifying Kernel coefficient for ‘rbf’, ‘poly’ and ‘sigmoid’. If gamma is
    * ‘auto’ then 1/n_features will be used instead, to set for for value
    * {@link svm_parameter#gamma}.
    */
   public static final String GAMMA = "gamma";

   /**
    * Json key value for specifying parameter nu of nu-SVC {@link svm_parameter#nu} (default 0.5).
    */
   public static final String M_NU = "m_nu";


   /**
    * Hides the public constructor.
    */
   private SupportVectorMachineConfigurationData() {
      throw new IllegalAccessError(StringUtils.EXCEPTION_MESSAGE_ACCESS_ERROR);

   }
}