package controller;

import model.ITask;
import model.task1.Task1;

public class TaskFactory {
	public static ITask getTask(int taskId)
	{
		switch(taskId)
		{
		case 1:
			return new Task1();
		default:
			return null;
		}
	}
}
