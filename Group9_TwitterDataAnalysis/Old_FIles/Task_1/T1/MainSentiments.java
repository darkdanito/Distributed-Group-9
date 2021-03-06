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

//import airlinenegativesentim.ANSMapper;
//import airlinenegativesentim.ANSReducer;
//import airlinenegativesentim.ANSValidationMapper;
//import airlinenegativesentim.AirlineNegativeSentiments;



public class MainSentiments {
	public static void main(String[] args) throws Exception {
//		Configuration conf = new Configuration();
//		Job job = Job.getInstance(conf, "MainSentiments");
//		job.setJarByClass(MainSentiments.class);
//		job.setReducerClass(Task01Reducer.class);
//		job.setOutputKeyClass(Text.class);
//		job.setOutputValueClass(Text.class);
//		//Path  = new Path("hdfs://localhost:9000/user/phamvanvung/airline/Airline-Full-Non-Ag-DFE-Sentiment.csv");
//		Path sentiments= new Path("hdfs://localhost:9000/user/phamvanvung/airline/Airline-Full-Non-Ag-DFE-Sentiment.csv");
//		//outPath.getFileSystem(conf).delete(outPath, true);
//		
//		MultipleInputs.addInputPath(job, sentiments, TextInputFormat.class, Task01Mapper.class);
//		Path outputPath = new Path("hdfs://localhost:9000/user/phamvanvung/twitter/Output");
//		outputPath.getFileSystem(conf).delete(outputPath, true);
//		FileOutputFormat.setOutputPath(job, outputPath);
//		System.exit(job.waitForCompletion(true)?0:1);
		
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "MainSentiments");
		job.setJarByClass(MainSentiments.class);
		Path inPath = new Path("hdfs://localhost:9000/user/phamvanvung/airline/Airline-Full-Non-Ag-DFE-Sentiment.csv");
		Path outPath = new Path("hdfs://localhost:9000/user/phamvanvung/twitter/Output");
		outPath.getFileSystem(conf).delete(outPath, true);

		//Put this file to distributed cache so we can use it to join
		//job.addCacheFile(new URI("hdfs://localhost:9000/user/phamvanvung/airline/ISO-3166-alpha3.tsv"));
		
		Configuration validationConf = new Configuration(false);
		ChainMapper.addMapper(job, ValidateMapper.class, LongWritable.class, Text.class, LongWritable.class, Text.class, validationConf);
		
		Configuration ansConf = new Configuration(false);
		ChainMapper.addMapper(job, Task01Mapper.class, LongWritable.class, Text.class, Text.class, IntWritable.class, ansConf);		
		
		job.setMapperClass(ChainMapper.class);
		
		job.setCombinerClass(Task01Reducer.class);
		job.setReducerClass(Task01Reducer.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		FileInputFormat.addInputPath(job, inPath);
		FileOutputFormat.setOutputPath(job, outPath);
		
		System.exit(job.waitForCompletion(true)?0:1);
	}

}
