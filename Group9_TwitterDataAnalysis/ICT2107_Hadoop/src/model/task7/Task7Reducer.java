package model.task7;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;

import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

/************************************************************************************************
 * Developer: Khaleef 																			*
 * 																								*
 * Date: 03 April 2016  																		*
 * 																								*
 * Description: XXXXX  																			*
 ************************************************************************************************/
public class Task7Reducer extends Reducer<Text, IntWritable, Text, IntWritable> {
	private Map<Text, IntWritable> map = new HashMap<>();

	/************************************************************************************************
	 * Description: XXXXX  																			*
	 * 																								*
	 ************************************************************************************************/
	@Override
	protected void reduce(Text key, Iterable<IntWritable> values,
			Reducer<Text, IntWritable, Text, IntWritable>.Context context) throws IOException, InterruptedException {
		
		// Get the count
		int count = 0;
		
		for (IntWritable value : values) {
			
			count += value.get();
		}

		map.put(new Text(key), new IntWritable(count));
	}

	/************************************************************************************************
	 * Description: XXXXX  																			*
	 * 																								*
	 ************************************************************************************************/
	@Override
	protected void cleanup(Context context) throws IOException, InterruptedException {
		
		Map<Text, IntWritable> sortedMap = sortValues(map);
		
		for (Text key : sortedMap.keySet()) {
			
			context.write(key, sortedMap.get(key));
		}
	}

	// Sort based on desc order
	private static <K extends Comparable, V extends Comparable> Map<K, V> sortValues(Map<K, V> map) {
		
		List<Map.Entry<K, V>> iterator = new LinkedList<Map.Entry<K, V>>(map.entrySet());
		
		Collections.sort(iterator, new Comparator<Map.Entry<K, V>>() {
			
			@Override
			public int compare(Map.Entry<K, V> m1, Map.Entry<K, V> m2) {
				
				return m2.getValue().compareTo(m1.getValue());
			}
		});
		
		Map<K, V> sortedMap = new LinkedHashMap<K, V>();
		
		for (Map.Entry<K, V> item : iterator) {
			
			sortedMap.put(item.getKey(), item.getValue());
		}
		
		return sortedMap;
	}
}
