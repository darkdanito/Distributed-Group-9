package model.task4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;

/************************************************************************************************
 * Developer: Anton	  																			*
 * 																								*
 * Date: 03 April 2016  																		*
 * 																								*
 * Description: Reducer class for Task 4.  						   								*
 ************************************************************************************************/
public class Task4Reducer extends Reducer<Text, IntWritable, Text, IntWritable> {
	
	Map<String, Integer> unsortMap = new HashMap<String, Integer> ();
	
	/************************************************************************************************
	 * Description: Reducer method for Task 5. It will count the number of positive sentiments,		*
	 * 				and put into a Map for sorting												  	*
	 * 																								*
	 ************************************************************************************************/
	@Override
	protected void reduce(Text key, Iterable<IntWritable> values, 
			Reducer<Text, IntWritable, Text, IntWritable>.Context context) 
					throws IOException, InterruptedException 
	{
		int total = 0;
		
		for(IntWritable t: values)
		{
			total += t.get();
		}
		
		unsortMap.put(key.toString(), total);
	}
	
	/************************************************************************************************
	 * Description: Cleanup method to sort the unsorted map into a sorted map that only take in the	*
	 * 				top 3. 																			*
	 * 																								*
	 ************************************************************************************************/
	@Override
	protected void cleanup(Context context)
			throws	IOException, InterruptedException{
		
		// Keep track the top 3		
		Map<String, Integer> sortedMapDesc = sortByComparator(unsortMap, false); // false = DESC
		
		for(int i = 0; i < 3; i++){
		
			String key = (String) sortedMapDesc.keySet().toArray()[i];
			context.write(new Text(key), new IntWritable(sortedMapDesc.get(key)));
		}		
	}
	
	/************************************************************************************************
	 * Description: Map sorting for List															*
	 * 																								*
	 ************************************************************************************************/
	private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap, final boolean order)
    {

        List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, Integer>>()
        {
            public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2)
            {
                if (order)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        
        for (Entry<String, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
}