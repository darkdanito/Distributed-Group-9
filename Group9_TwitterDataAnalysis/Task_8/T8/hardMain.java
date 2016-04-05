package T8;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

  
public class hardMain {
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException{ 
    Configuration conf = new Configuration();
    
    Job job = Job.getInstance(conf, "Task8");
    
    job.setJarByClass(hardMain.class);
    job.setMapperClass(hardMapper.class);
    job.setCombinerClass(hardReducer.class);
    job.setReducerClass(hardReducer.class);
    
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    
    Path inputPath = new Path("hdfs://localhost:9000/user/phamvanvung/airline/input"); 
	Path outputPath = new Path("hdfs://localhost:9000/user/phamvanvung/airline/output"); 
	FileInputFormat.addInputPath(job, inputPath);
	FileOutputFormat.setOutputPath(job, outputPath);
    outputPath.getFileSystem(conf).delete(outputPath, true);
	
    job.waitForCompletion(true);
  }
}