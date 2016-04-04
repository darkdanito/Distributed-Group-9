package model.task5;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import model.ITask;

public class Task5 implements ITask{

	private boolean isDone;
	
	public Task5()
	{
		isDone = false;
	}
	
	@Override
	public void start() {
		// TODO Auto-generated method stub
		Configuration conf = new Configuration();
		
		Job job = Job.getInstance(conf, "TwitterMain");
		
		job.setJarByClass(Task5.class);
		
		job.setMapperClass(Task5Mapper.class);
		job.setReducerClass(Task5Reducer.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		Path twitterInput = new Path("hdfs://localhost:9000/user/phamvanvung/group9_hadoop/input");
		Path outputPath = new Path("hdfs://localhost:9000/user/phamvanvung/group9_hadoop/output/task5");
		
		outputPath.getFileSystem(conf).delete(outputPath, true);
		
		FileInputFormat.addInputPath(job, twitterInput);
		FileOutputFormat.setOutputPath(job, outputPath);
		
		isDone = job.waitForCompletion(true)?true:false;
	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return isDone;
	}
	
}
