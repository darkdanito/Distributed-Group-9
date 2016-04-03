package airline;
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



public class MainSentiments {
	public static void main(String[] args) throws Exception {
//		
		
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "MainSentiments");
		job.setJarByClass(MainSentiments.class);
		Path inPath = new Path("hdfs://localhost:9000/user/phamvanvung/airline/Airline-Full-Non-Ag-DFE-Sentiment.csv");
		Path outPath = new Path("hdfs://localhost:9000/user/phamvanvung/twitter/Output");
		outPath.getFileSystem(conf).delete(outPath, true);

		//Put this file to distributed cache so we can use it to join and retrieve the country names 
		job.addCacheFile(new URI("hdfs://localhost:9000/user/phamvanvung/airline/ISO-3166-alpha3.tsv"));
		
		Configuration validationConf = new Configuration(false);
		ChainMapper.addMapper(job, ValidateMapper.class, LongWritable.class, Text.class, LongWritable.class, Text.class, validationConf);
		
		Configuration ansConf = new Configuration(false);
		ChainMapper.addMapper(job, Task03Mapper.class, LongWritable.class, Text.class, Text.class, IntWritable.class, ansConf);		
		
		job.setMapperClass(ChainMapper.class);
		
		job.setCombinerClass(Task03Reducer.class);
		job.setReducerClass(Task03Reducer.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		FileInputFormat.addInputPath(job, inPath);
		FileOutputFormat.setOutputPath(job, outPath);
		
		System.exit(job.waitForCompletion(true)?0:1);
	}

}
