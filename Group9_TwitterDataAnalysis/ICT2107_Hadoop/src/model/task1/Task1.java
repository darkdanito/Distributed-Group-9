package model.task1;

import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.chain.ChainMapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import model.ITask;

public class Task1 implements ITask{
	private Boolean isDone;
	
	public Task1()
	{
		isDone = false;
	}
	
	public boolean isDone()
	{
		return this.isDone;
	}
	
	public void start()
	{
		try{
			Configuration conf = new Configuration();
			Job job = Job.getInstance(conf, "Task1");
			job.setJarByClass(Task1.class);
			Path inPath = new Path("hdfs://localhost:9000/user/phamvanvung/group9_hadoop/input");
			Path outPath = new Path("hdfs://localhost:9000/user/phamvanvung/group9_hadoop/output/task1");
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
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
