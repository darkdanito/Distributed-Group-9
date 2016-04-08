package model.task5;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/************************************************************************************************
 * Developer: Yun Yong 																			*
 * 																								*
 * Date: 01 April 2016  																		*
 * 																								*
 * Description: Mappper class for Task 5.  														*					*
 ************************************************************************************************/
public class Task5Mapper extends Mapper<LongWritable, Text, Text, Text> {
	
	/************************************************************************************************
	 * Description: Read the data taken from the Twitter dataset.									*
	 * 																								*
	 ************************************************************************************************/
	@Override
	protected void map(LongWritable key, Text value, 
			Mapper<LongWritable, Text, Text, Text>.Context context)
			throws IOException, InterruptedException 
	{	
		/************************************************************************************************
		 * Description: Check if the row is valid. If the Row is valid, extract column 16 of parts		*
		 * 				that contains the data for the Airline and extract column 8 which contains		*
		 * 				the value for the trust.														*
		 * 																								*
		 ************************************************************************************************/
		if(isValid(value.toString()))
		{	
			String[] parts = value.toString().split(",");
	
			String airline = parts[16];
			String trust = parts[8];
			
			/************************************************************************************************
			 * Description: Ensure that the airline data and trust data are valid. By ensuring that they 	*
			 * 				are not null and trust data contain "." to check that it is a decimal or		*
			 * 				the value is == 1. If it is valid, it will be written to the context.			*
			 * 																								*
			 ************************************************************************************************/
			if (airline != null && trust != null) 
			{	
				if (trust.contains(".") || trust.equals("1")){
					
					context.write(new Text(airline), new Text("one\t" + trust));
				}
			}
		} 
	}

	/************************************************************************************************
	 * Description: Check if the row taken from the Twitter dataset is correct or not.				*
	 * 				Checking of the parts is of length 27 is due to the total column of				*
	 * 				the Twitter dataset is 27 columns. If the row is length is 27 then we use it	*
	 * 				by returning true, else return false.											*
	 * 																								*
	 ************************************************************************************************/
	private boolean isValid(String line)
	{
		String[] parts = line.split(",");
		
		if (parts.length==27)
		{
			return true;
		
		}else
		{
			return false;
			
		}
	}
}