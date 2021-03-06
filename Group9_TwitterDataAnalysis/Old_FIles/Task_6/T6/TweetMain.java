package T6;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class TweetMain {
	
	public static void main(String [] args) throws Exception{
		
		Configuration conf = new Configuration();
		
		Job job = Job.getInstance(conf, "TwitterMain");
		
		job.setJarByClass(TweetMain.class);
		
		job.setMapperClass(TweetMapper.class);
		job.setReducerClass(TweetReducer.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		Path twitterInput = new Path("hdfs://localhost:9000/user/phamvanvung/airline/input/Airline-Full-Non-Ag-DFE-Sentiment.csv");
		Path outputPath = new Path("hdfs://localhost:9000/user/phamvanvung/airline/output");
		
		outputPath.getFileSystem(conf).delete(outputPath, true);
		
		FileInputFormat.addInputPath(job, twitterInput);
		FileOutputFormat.setOutputPath(job, outputPath);
		
		System.exit(job.waitForCompletion(true)?0:1);
	}

}
