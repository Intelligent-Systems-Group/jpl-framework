/**
 * This package contains all classes which are directly related to the Binary Relevance Learning
 * approach to solve the multilabel classification problem. The general idea of this approach is to
 * train a binary classifier for each label separately. At prediction time an instance is passed to
 * all binary learning models and their predictions are aggregated in order to obtain the overall
 * prediction.
 * 
 * @author Alexander Hetzer
 *
 */
package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.multilabelclassification.binaryrelevancelearning;