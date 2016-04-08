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
 * Description: XXXXX  																			*
 ************************************************************************************************/
public class Task1Mapper extends Mapper<LongWritable, Text,Text,IntWritable> {
	
	/************************************************************************************************
	 * Description: XXXXX  																			*
	 * 																								*
	 ************************************************************************************************/
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
		
		String[] parts = value.toString().split(",");
		
		String reasons = parts[15];
		String sentiment = parts[14];
		
		/************************************************************************************************
		 * Description: XXXXX  																			*
		 * 																								*
		 ************************************************************************************************/
		if (reasons != null && sentiment != null) {

			if (sentiment.equals("negative") && reasons != null) {
				
				context.write(new Text(reasons), new IntWritable(1));
			}
		}
	}
}
