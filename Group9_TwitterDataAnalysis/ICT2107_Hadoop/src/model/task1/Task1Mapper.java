package model.task1;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/************************************************************************************************
 * Developer: Winnie	  																		*
 * 																								*
 * Date: 03 April 2016  																		*
 * 																								*
 * Description: This class contains the algorithm to filter out the data that is needed. The	*
 *				values are mapped with a key and is send over to the reducer class for 			*
 *				processing.																		*
 ************************************************************************************************/
public class Task1Mapper extends Mapper<LongWritable, Text,Text,IntWritable> {
	
	/************************************************************************************************
	 * Description: Parts[14] refers to the airline_sentiment columns and Parts[15] refers to the   *
	 * 			    negative reason.																*
	 ************************************************************************************************/
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
		
		String[] parts = value.toString().split(",");
		
		String reasons = parts[15];
		String sentiment = parts[14];
		
		/************************************************************************************************
		 * Description: The algorithm below extracts out the sentiments that are 'negative' in	the		*
		 * 				data set. It also ensures that reasons are not null.							*
		 ************************************************************************************************/
		if (reasons != null && sentiment != null) {

			if (sentiment.equals("negative") && reasons != null) {
				
				context.write(new Text(reasons), new IntWritable(1));
			}
		}
	}
}
