package model.task9;
import java.io.IOException;

import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

/************************************************************************************************
 * Developer: Khaleef 																			*
 * 																								*
 * Date: 05 April 2016  																		*
 * 																								*
 * Description: Validation Mapper class for Task 9.												*
 ************************************************************************************************/
public class Task9ValidationMapper extends Mapper<LongWritable, Text, LongWritable, Text> {
	
	/************************************************************************************************
	 * Description: Write to context if valid 														*
	 * 																								*
	 ************************************************************************************************/
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, LongWritable, Text>.Context context)
			throws IOException, InterruptedException {
		
		if (isValid(value.toString())) {
			
			context.write(key, value);
		}
	}

	/************************************************************************************************
	 * Description: Split the rows into different parts and check for validity						*
	 * 																								*
	 ************************************************************************************************/
	private boolean isValid(String line) {
		
		String[] parts = line.split(",");
		
		if (parts.length == 27) {
			
			return true;
			
		} else {
			
			return false;
		}
	}
}
