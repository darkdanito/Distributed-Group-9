package model.task1;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Task1Mapper extends Mapper<LongWritable, Text,Text,IntWritable> {
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
		//String record = value.toString();
		//String[] parts = record.split("\t"); //split by tab ?
		//context.write(new Text(parts[0]), new Text("accounts\t"+parts[1]));
		//context.write(new Text(parts[14]), new IntWritable(1));
		
		
		String[] parts = value.toString().split(",");
		String reasons = parts[15];
		String sentiment = parts[14];
		if (reasons != null && sentiment != null) {
			//String countryName = countryCodes.get(countryCode);
			if (sentiment.equals("negative") && reasons != null) {
				context.write(new Text(reasons), new IntWritable(1));
			}
		}
}
	
}
