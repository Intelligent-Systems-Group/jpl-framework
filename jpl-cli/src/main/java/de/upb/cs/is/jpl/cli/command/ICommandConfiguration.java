package de.upb.cs.is.jpl.cli.command;


/**
 * This interface defines the common functionality for all command configurations, which are
 * compatible to the core system. This functionality is required to incorporate the use of
 * JCommander into the system in a correct and secure way.
 * 
 * @author Alexander Hetzer
 *
 */
public interface ICommandConfiguration {

   /**
    * Resets all fields of this class to their default values.
    * 
    * Note: It is extremely important that this method is overwritten correctly by any
    * implementation.
    */
   public void resetFields();


   /**
    * Returns an exact deep copy of this object.
    * 
    * @return an exact deep copy of this object
    */
   public ICommandConfiguration getCopy();
}
