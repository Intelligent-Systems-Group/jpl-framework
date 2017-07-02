/**
 * This package contains the implementation of the matrix factorization algorithm for collaborative
 * filtering.
 * 
 * The used algorithm is based on
 * <a href="https://en.wikipedia.org/wiki/Singular_value_decomposition"> singular value
 * decomposition </a> and converts the existing rating information into two smaller matrices which
 * contain hidden features and allow the algorithm to make predictions based on the column and row
 * vectors of these hidden features matrices.
 * 
 * @author Sebastian Osterbrink
 *
 */
package de.upb.cs.is.jpl.api.algorithm.learningalgorithm.collaborativefiltering.matrixfactorization;