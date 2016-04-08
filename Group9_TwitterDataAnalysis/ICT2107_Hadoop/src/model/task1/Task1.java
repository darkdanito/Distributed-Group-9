package model.task1;

//import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.chain.ChainMapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import model.ITask;
import util.Constants;

/************************************************************************************************
 * Developer: Winnie	  																		*
 * 																								*
 * Date: 03 April 2016  																		*
 * 																								*
 * Description: This is the file to set the path for input and output destination for the       *
 *	            reading of the data set. The chainmapper is also declared here for the mapper   *
 * 				class and the validatemapper class                                              *
 ************************************************************************************************/
public class Task1 implements ITask{
	
	private Boolean isDone;
	private long start;
	private long end;
	
	public Task1()
	{
		isDone = false;
		start = 0;
		end = 0;
	}
	
	public boolean isDone()
	{
		return this.isDone;
	}
	
	public void start()
	{
		try{
			start = System.currentTimeMillis();
			
			Configuration conf = new Configuration();
			
			Job job = Job.getInstance(conf, "Task1");
			job.setJarByClass(Task1.class);
			
			
			
			Path inPath = new Path("hdfs://localhost:9000/" + Constants.hadoopPath + "/input");
			Path outPath = new Path("hdfs://localhost:9000/" + Constants.hadoopPath + "/output/task1");
			outPath.getFileSystem(conf).delete(outPath, true);
	
			Configuration validationConf = new Configuration(false);
			ChainMapper.addMapper(job, Task1ValidateMapper.class, LongWritable.class, Text.class, LongWritable.class, Text.class, validationConf);
			
			Configuration ansConf = new Configuration(false);
			ChainMapper.addMapper(job, Task1Mapper.class, LongWritable.class, Text.class, Text.class, IntWritable.class, ansConf);		
			
			job.setMapperClass(ChainMapper.class);
			
			job.setCombinerClass(Task1Reducer.class);
			job.setReducerClass(Task1Reducer.class);
			
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(IntWritable.class);
			
			FileInputFormat.addInputPath(job, inPath);
			FileOutputFormat.setOutputPath(job, outPath);
			
			isDone = job.waitForCompletion(true)?true:false;
			end = System.currentTimeMillis();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	@Override
	public long timeElapsed() {
		
		return end-start;
	}
}
