package model.task6;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import model.ITask;

public class Task6 implements ITask{

	private boolean isDone;
	private long start, end;
	
	public Task6()
	{
		isDone = false;
		start = end = 0;
	}
	
	@Override
	public void start() {
		start = System.currentTimeMillis();
		try
		{
			// TODO Auto-generated method stub
	
			Configuration conf = new Configuration();
			
			Job job = Job.getInstance(conf, "Task6");
			
			job.setJarByClass(Task6.class);
			
			job.setMapperClass(Task6Mapper.class);
			job.setReducerClass(Task6Reducer.class);
			
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(Text.class);
			
			Path twitterInput = new Path("hdfs://localhost:9000/user/phamvanvung/group9_hadoop/input");
			Path outputPath = new Path("hdfs://localhost:9000/user/phamvanvung/group9_hadoop/output/task6");
			
			outputPath.getFileSystem(conf).delete(outputPath, true);
			
			FileInputFormat.addInputPath(job, twitterInput);
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
		// TODO Auto-generated method stub
		return isDone;
	}
	
	@Override
	public long timeElapsed() {
		// TODO Auto-generated method stub
		return end-start;
	}
	
}
