package model;

/************************************************************************************************
 * Developer: Anton 																			*
 * 																								*
 * Date: 03 April 2016  																		*
 * 																								*
 * Description: This class provides an interface for the other component or main program to		*
 * 				call method in each Taskx class													*
 ************************************************************************************************/
public interface ITask {
	
	void start();
	boolean isDone();
	long timeElapsed();
}
