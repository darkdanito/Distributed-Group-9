package model.task8;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import model.ITask;

/************************************************************************************************
 * Developer: Yun Yong 																			*
 * 																								*
 * Date: 04 April 2016  																		*
 * 																								*
 * Description: XXXXX  																			*
 ************************************************************************************************/
public class Task8 implements ITask{
	
	private boolean isDone;
	private long start, end;
	
	public Task8()
	{
		isDone = false;
		start = end = 0;
	}
	
	@Override
	public void start(){ 
		
		start = System.currentTimeMillis();
		
		try{
		    Configuration conf = new Configuration();
		    
		    Job job = Job.getInstance(conf, "Task8");
		    
		    job.setJarByClass(Task8.class);
		    job.setMapperClass(Task8Mapper.class);
		    job.setCombinerClass(Task8Reducer.class);
		    job.setReducerClass(Task8Reducer.class);
		    
		    job.setOutputKeyClass(Text.class);
		    job.setOutputValueClass(IntWritable.class);
		    
		    Path inputPath = new Path("hdfs://localhost:9000/user/phamvanvung/group9_hadoop/input"); 
			Path outputPath = new Path("hdfs://localhost:9000/user/phamvanvung/group9_hadoop/output/task8"); 
			FileInputFormat.addInputPath(job, inputPath);
			FileOutputFormat.setOutputPath(job, outputPath);
		    outputPath.getFileSystem(conf).delete(outputPath, true);
			
		    isDone = job.waitForCompletion(true)? true : false;
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