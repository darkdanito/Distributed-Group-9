package model.task3;
import java.net.URI;

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
 * 				class and the validatemapper class.                                              *
 ************************************************************************************************/
public class Task3 implements ITask{

	private boolean isDone;
	private long start, end;
	
	public Task3()
	{
		isDone = false;
		start = end = 0;
	}
	
	@Override
	public void start() {
		
		start = System.currentTimeMillis();
		
		try{
			
			Configuration conf = new Configuration();
			
			Job job = Job.getInstance(conf, "Task3");
			job.setJarByClass(Task3.class);
			
			Path inPath = new Path("hdfs://localhost:9000/" + Constants.hadoopPath + "/input");
			Path outPath = new Path("hdfs://localhost:9000/" + Constants.hadoopPath + "/output/task3");
			outPath.getFileSystem(conf).delete(outPath, true);
	
			//Put this file to distributed cache so we can use it to join and retrieve the country names 
			job.addCacheFile(new URI("hdfs://localhost:9000/" + Constants.hadoopPath + "/ISO-3166-alpha3.tsv"));
			
			Configuration validationConf = new Configuration(false);
			ChainMapper.addMapper(job, Task3ValidateMapper.class, LongWritable.class, Text.class, LongWritable.class, Text.class, validationConf);
			
			Configuration ansConf = new Configuration(false);
			ChainMapper.addMapper(job, Task3Mapper.class, LongWritable.class, Text.class, Text.class, IntWritable.class, ansConf);		
			
			job.setMapperClass(ChainMapper.class);
			
			job.setCombinerClass(Task3Reducer.class);
			job.setReducerClass(Task3Reducer.class);
			
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(IntWritable.class);
			
			FileInputFormat.addInputPath(job, inPath);
			FileOutputFormat.setOutputPath(job, outPath);
			
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
