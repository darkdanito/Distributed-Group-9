package model.task4;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import model.ITask;
 
public class Task4 implements ITask{
	
	private boolean isDone;
	
	public Task4()
	{
		isDone = false;
	}

	@Override
	public void start() {
		try{
			// TODO Auto-generated method stub
			Configuration conf = new Configuration();
	
	    	Job job = Job.getInstance(conf, "Task4");
	
	    	job.setJarByClass(Task4.class);
	
	    	job.setMapperClass(Task4Mapper.class);
	    	job.setReducerClass(Task4Reducer.class);
	
	
	    	job.setOutputKeyClass(Text.class);
	    	job.setOutputValueClass(IntWritable.class);
	
	    	Path accountInput = new Path("hdfs://localhost:9000/user/phamvanvung/group9_hadoop/input");
	
	    	Path outputPath = new Path("hdfs://localhost:9000/user/phamvanvung/group9_hadoop/output/task4");
	
	    	outputPath.getFileSystem(conf).delete(outputPath, true);
	    	FileInputFormat.addInputPath(job, accountInput);
	
	    	FileOutputFormat.setOutputPath(job, outputPath);
	    	
	    	isDone = job.waitForCompletion(true)?true:false;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return isDone;
	}
 
}