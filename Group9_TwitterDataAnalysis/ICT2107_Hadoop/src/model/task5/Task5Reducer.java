package model.task5;

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
 * Description: Reducer class for Task 5.  														*
 ************************************************************************************************/
public class Task5Reducer extends Reducer<Text, Text, Text, Text> {
	
	/************************************************************************************************
	 * Description: Reducer method for Task 5  														*
	 * 																								*
	 ************************************************************************************************/
	@Override
	protected void reduce(Text key, Iterable<Text> values, 
			Reducer<Text, Text, Text, Text>.Context context) 
					throws IOException, InterruptedException 
	{
		double median = 0;
	
		ArrayList<Double> inputArray = new ArrayList<Double>();
		
		/************************************************************************************************
		 * Description: This for loop will loop though the values that was taken in for the input,		*
		 * 				to split the string to validate if the value is equal to "one". If it is equal	*
		 * 				to "one", the value is added to the ArrayList to be stored for sorting.			*
		 * 																								*
		 ************************************************************************************************/
		for(Text t: values)
		{
			String parts[] = t.toString().split("\t");
			
			if(parts[0].equals("one"))
			{
				double in = Double.parseDouble(parts[1]);
				inputArray.add(in);
			}
		}
		
		/************************************************************************************************
		 * Description: Sorting method for the ArrayList above. As in order to calculate median,		*
		 * 				the values in the ArrayList have to be sorted first. Once the ArrayList is		*
		 * 				sorted, it will be check to see if the the ArrayList size when modulus will		*
		 * 				return 0 or 1. If the return value is 1, it will need to take the "middle two"	*
		 * 				value and add them together and divide by 2 in order to get the median.			*
		 * 				Else, the median can be obtain by the value in the middle of ArrayList			*
		 * 																								*
		 ************************************************************************************************/
		if(inputArray.size() != 0)
		{
			Collections.sort(inputArray);
			
			if (inputArray.size() % 2  == 1)
			{
				median = inputArray.get(((inputArray.size() + 1 )/ 2 - 1));
			}
			else
			{
				double lower = inputArray.get(inputArray.size() / 2 - 1);
				double upper = inputArray.get(inputArray.size() / 2);
		
				median = (lower + upper) / 2;
			}
			
			String countStr = String.valueOf(median);
			
			context.write(key, new Text(countStr));
		}
	}
}