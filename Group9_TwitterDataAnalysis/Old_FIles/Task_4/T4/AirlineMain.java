package T4;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
 
public class AirlineMain {
 
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException{
         
    	Configuration conf = new Configuration();

    	Job job = Job.getInstance(conf, "TwitterMain");

    	job.setJarByClass(AirlineMain.class);

    	job.setMapperClass(AirlineMapper.class);
   // 	job.setCombinerClass(AirlineReducer.class); 
    	job.setReducerClass(AirlineReducer.class);


    	job.setOutputKeyClass(Text.class);
    	job.setOutputValueClass(IntWritable.class);

    	Path accountInput = new Path("hdfs://localhost:9000/user/phamvanvung/airline/input/Airline-Full-Non-Ag-DFE-Sentiment.csv");

    	Path outputPath = new Path("hdfs://localhost:9000/user/phamvanvung/airline/output");

    	outputPath.getFileSystem(conf).delete(outputPath, true);
    	FileInputFormat.addInputPath(job, accountInput);

    	FileOutputFormat.setOutputPath(job, outputPath);
    	System.exit(job.waitForCompletion(true)?0:1);;
      
    }
 
}