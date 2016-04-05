package model.task9;

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

public class Task9 implements ITask{

	private boolean isDone;
	private long start;
	private long end;
	
	public Task9()
	{
		isDone = false;
		start = end = 0;
	}

	@Override
	public void start() {
		start = System.currentTimeMillis();
		try{
			// TODO Auto-generated method stub
			Configuration conf = new Configuration();
			Job job = Job.getInstance(conf, "Task9");
			job.setJarByClass(Task9.class);
			Path inPath = new Path("hdfs://localhost:9000/user/phamvanvung/group9_hadoop/input");
			Path outPath = new Path("hdfs://localhost:9000/user/phamvanvung/group9_hadoop/output/task9");
			outPath.getFileSystem(conf).delete(outPath, true);
	
			// Put this file to distributed cache so we can use it to join
			job.addCacheFile(new URI("hdfs://localhost:9000/user/phamvanvung/group9_hadoop/ISO-3166-alpha3.tsv"));
	
			Configuration validationConf = new Configuration(false);
			ChainMapper.addMapper(job, Task9ValidationMapper.class, LongWritable.class, Text.class, LongWritable.class,
					Text.class, validationConf);
	
			Configuration ansConf = new Configuration(false);
			ChainMapper.addMapper(job, Task9Mapper.class, LongWritable.class, Text.class, Text.class, IntWritable.class,
					ansConf);
	
			job.setMapperClass(ChainMapper.class);
			job.setCombinerClass(Task9Reducer.class);
			job.setReducerClass(Task9Reducer.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(IntWritable.class);
	
			FileInputFormat.addInputPath(job, inPath);
			FileOutputFormat.setOutputPath(job, outPath);
	
			isDone = job.waitForCompletion(true) ? true : false;
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
