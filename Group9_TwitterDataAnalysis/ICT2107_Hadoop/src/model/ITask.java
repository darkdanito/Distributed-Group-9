package model;

public interface ITask {
	void start();
	boolean isDone();
	long timeElapsed();
}
