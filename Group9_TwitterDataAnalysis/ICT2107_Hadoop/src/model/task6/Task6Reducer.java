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
 * Description: XXXXX  																			*
 ************************************************************************************************/
public class Task6Reducer extends Reducer<Text, Text, Text, Text> {

	int totalcount = 0;
	
	/************************************************************************************************
	 * Description: XXXXX  																			*
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
		 * Description: XXXXX  																			*
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