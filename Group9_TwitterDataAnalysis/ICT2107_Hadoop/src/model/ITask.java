package model;

/************************************************************************************************
 * Developer: Anton 																			*
 * 																								*
 * Date: 03 April 2016  																		*
 * 																								*
 * Description: XXXXX  																			*
 ************************************************************************************************/
public interface ITask {
	
	void start();
	boolean isDone();
	long timeElapsed();
}
