package model.task3;

import java.io.IOException;

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
public class Task3ValidateMapper extends Mapper<LongWritable, Text, LongWritable, Text> {
	
	/************************************************************************************************
	 * Description: XXXXX  																			*
	 * 																								*
	 ************************************************************************************************/
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, LongWritable, Text>.Context context)
			throws IOException, InterruptedException {
		
		/************************************************************************************************
		 * Description: XXXXX  																			*
		 * 																								*
		 ************************************************************************************************/
		if(isValid(value.toString())){
			
			context.write(key, value);
		}
	}
	
	/************************************************************************************************
	 * Description: XXXXX  																			*
	 * 																								*
	 ************************************************************************************************/
	private boolean isValid(String line){
		
		String[] parts = line.split(",");
		
		if (parts.length==27){
			
			return true;
			
		}else{
			
			return false;
		}
	}
}
