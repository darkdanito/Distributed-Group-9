package controller;

import model.ITask;
import model.task1.Task1;
import model.task2.Task2;
import model.task3.Task3;
import model.task4.Task4;
import model.task5.Task5;
import model.task6.Task6;
import model.task7.Task7;
import model.task8.Task8;
import model.task9.Task9;

public class TaskFactory {
	public static ITask getTask(int taskId)
	{
		switch(taskId)
		{
		case 1:
			return new Task1();
		case 2:
			return new Task2();
		case 3:
			return new Task3();
		case 4:
			return new Task4();
		case 5:
			return new Task5();
		case 6:
			return new Task6();
		case 7:
			return new Task7();
		case 8:
			return new Task8();
		case 9:
			return new Task9();
		default:
			return null;
		}
	}
}
