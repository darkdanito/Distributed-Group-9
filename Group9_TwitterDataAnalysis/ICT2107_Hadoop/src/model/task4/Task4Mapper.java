package model.task4;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/************************************************************************************************
 * Developer: Anton	  																			*
 * 																								*
 * Date: 03 April 2016  																		*
 * 																								*
 * Description: Mappper class for Task 4.   													*
 ************************************************************************************************/
public class Task4Mapper extends Mapper<LongWritable, Text, Text, IntWritable> {
	
	IntWritable one = new IntWritable(1);
	
	/************************************************************************************************
	 * Description: Read the data taken from the Twitter dataset.  									*
	 * 																								*
	 ************************************************************************************************/
	@Override
	protected void map(LongWritable key, Text value, 
		Mapper<LongWritable, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException 
	{	
		/************************************************************************************************
		 * Description: Ensure that the airline data and airline_sentiment data are valid. By ensuring 	*
		 * 				that they are not null. If it is valid, it will check if the sentiment equals	*
		 * 				to "positive", if it is equal it will be written to the context					*
		 * 																								*
		 ************************************************************************************************/
		if(isValid(value.toString()))
		{	
			String[] parts = value.toString().split(",");
	
			String airline = parts[16];
			String airlineSentiment = parts[14];
			
			if (airline != null && airlineSentiment != null) 
			{	
				if (airlineSentiment.equals("positive")){
					
					context.write(new Text(airline), one);
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