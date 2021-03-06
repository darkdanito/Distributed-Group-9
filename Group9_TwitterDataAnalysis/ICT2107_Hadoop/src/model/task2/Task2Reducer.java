package model.task2;

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
 * Developer: Jamie	  																			*
 * 																								*
 * Date: 03 April 2016  																		*
 * 																								*
 * Description: Reducer is input the grouped output of a Mapper. 								*
 * 				In the phase the framework, for each Reducer, fetches the relevant partition 	*
 * 				of the output of all the Mappers												*
 ************************************************************************************************/
public class Task2Reducer extends Reducer<Text, IntWritable, Text, IntWritable> {

	Text totalIW = new Text();
    private Map<String, Integer> sortMap = new HashMap<>();
 
	/************************************************************************************************
	 * Description: Loop through the IntWritable values that were passed from the Mapper class and 	*
	 * 				add them for each country name.													*
	 * 																								*
	 ************************************************************************************************/
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values,
            Reducer<Text,IntWritable,Text,IntWritable>.Context context)throws IOException, InterruptedException
    {
        int total = 0;
        

        for(IntWritable value:values){
        	
            total+= value.get();
        }   
        
        sortMap.put(key.toString(),total);
    }
     
	/************************************************************************************************
	 * Description: Using the default natural ordering of sorted map Integer key which implement 	*
	 * 				Comparable interface															*
	 * 																								*
	 ************************************************************************************************/
    @Override
    protected void cleanup(Reducer<Text,IntWritable,Text,IntWritable>.Context context)
    		throws IOException, InterruptedException
    {
        Map<String,Integer> sortedMap = sortValue(sortMap);
        int counter=0;
         
        for(String key: sortedMap.keySet()){
        	
            if(counter==1){
            	
                break;
            }
            
            context.write(new Text(key), new IntWritable(sortedMap.get(key)));
            counter++;
        }
    }
     
	/************************************************************************************************
	 * Description: The comparator accepts integers and compare the associated  					*
	 *   			integers of the given integer value in the map.									*
	 ************************************************************************************************/
    private static <K,V extends Comparable<?super V>> Map<K,V> sortValue(Map<K,V> map){
        List<Map.Entry<K,V>> queries = new LinkedList<Map.Entry<K,V>>(map.entrySet());
        Collections.sort(queries, new Comparator<Map.Entry<K,V>>(){
             
        	/************************************************************************************************
        	 * Description: return (v2.getValue()).compareTo(v1.getValue()); 								*
			 *				is setting the values in descending order.  									*
        	 *  																							*
        	 ************************************************************************************************/
            @Override
            public int compare(Map.Entry<K, V> v1 ,Map.Entry<K,V> v2){
            	
                return (v2.getValue()).compareTo(v1.getValue());
            }
        });
        
        Map<K, V> sortedMap = (Map<K, V>) new LinkedHashMap<K,V>();
        
    	/************************************************************************************************
    	 * Description: This is to put the sorted value into a Hash table and also the country name. 	*
    	 * 																								*
    	 ************************************************************************************************/
        for(Map.Entry<K, V> query : queries){
        	
            sortedMap.put(query.getKey(),query.getValue());
        }
        
        return sortedMap;
    }
}
