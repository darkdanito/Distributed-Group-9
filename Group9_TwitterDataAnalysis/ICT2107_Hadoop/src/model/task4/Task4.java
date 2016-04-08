package model.task4;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import model.ITask;
import util.Constants;
 
/************************************************************************************************
 * Developer: Anton 																			*
 * 																								*
 * Date: 03 April 2016  																		*
 * 																								*
 * Description: Main Class for the Task 4. Handles the settings related to the Hadoop  			*
 * 				application for Task 4.															*
 ************************************************************************************************/
public class Task4 implements ITask{
	
	private boolean isDone;
	private long start, end;
	
	public Task4()
	{
		isDone = false;
		start = end = 0;
	}

	@Override
	public void start() {
		start = System.currentTimeMillis();
		try{

			Configuration conf = new Configuration();
	
	    	Job job = Job.getInstance(conf, "Task4");
	
	    	job.setJarByClass(Task4.class);
	
	    	job.setMapperClass(Task4Mapper.class);
	    	job.setReducerClass(Task4Reducer.class);
	
	
	    	job.setOutputKeyClass(Text.class);
	    	job.setOutputValueClass(IntWritable.class);
	
	    	Path accountInput = new Path("hdfs://localhost:9000/" + Constants.hadoopPath + "/input");
	
	    	Path outputPath = new Path("hdfs://localhost:9000/" + Constants.hadoopPath + "/output/task4");
	
	    	outputPath.getFileSystem(conf).delete(outputPath, true);
	    	FileInputFormat.addInputPath(job, accountInput);
	
	    	FileOutputFormat.setOutputPath(job, outputPath);
	    	
	    	isDone = job.waitForCompletion(true)?true:false;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		end = System.currentTimeMillis();
	}

	@Override
	public boolean isDone() {

		return isDone;
	}
 
	@Override
	public long timeElapsed() {

		return end-start;
	}
}