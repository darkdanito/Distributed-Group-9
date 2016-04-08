package model.task6;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/************************************************************************************************
 * Developer: Yun Yong 																			*
 * 																								*
 * Date: 01 April 2016  																		*
 * 																								*
 * Description: Reducer class for Task 6.  														*
 ************************************************************************************************/
public class Task6Reducer extends Reducer<Text, Text, Text, Text> {

	int totalcount = 0;
	
	/************************************************************************************************
	 * Description: Reducer method for Task 6  														*
	 * 																								*
	 ************************************************************************************************/
	@Override
	protected void reduce(Text key, Iterable<Text> values, 
			Reducer<Text, Text, Text, Text>.Context context) 
					throws IOException, InterruptedException 
	{
		int count = 0;
		
		String stringCount;
		
		/************************************************************************************************
		 * Description: This for loop will loop though the values that was taken in for the input,		*
		 * 				to split the string to validate if the value is equal to "one". If it is equal	*
		 * 				to "one", the value of part[1] is check to see if it contains any word of		*
		 * 				delayed or #SFO. if it contains such words it means that the tweet is about 	*
		 * 				delayed and thus a counter is added.											*
		 * 																								*
		 ************************************************************************************************/
		for(Text t: values)
		{
			String parts[] = t.toString().split("\t");
			
			if(parts[0].equals("one"))
			{
				if (parts[1].contains("delayed") || parts[1].contains("#SFO"))
				{
					count += 1;
				}
			}
		}
		
		stringCount = String.valueOf(count);
		
		context.write(key, new Text(stringCount));
	}
}