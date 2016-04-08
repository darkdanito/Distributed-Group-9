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
 * Description: XXXXX  																			*
 ************************************************************************************************/
public class Task2Reducer extends Reducer<Text, IntWritable, Text, IntWritable> {

//	private Map<Text, IntWritable> map = new HashMap<>();

	Text totalIW = new Text();
    private Map<String, Integer> sortMap = new HashMap<>();
 
	/************************************************************************************************
	 * Description: XXXXX  																			*
	 * 																								*
	 ************************************************************************************************/
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values,
            Reducer<Text,IntWritable,Text,IntWritable>.Context context)throws IOException, InterruptedException
    {
        int total = 0;
        
    	/************************************************************************************************
    	 * Description: XXXXX  																			*
    	 * 																								*
    	 ************************************************************************************************/
        for(IntWritable value:values){
        	
            total+= value.get();
        }   
        
        sortMap.put(key.toString(),total);
    }
     
	/************************************************************************************************
	 * Description: XXXXX  																			*
	 * 																								*
	 ************************************************************************************************/
    @Override
    protected void cleanup(Reducer<Text,IntWritable,Text,IntWritable>.Context context)throws IOException, InterruptedException
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
	 * Description: XXXXX  																			*
	 * 																								*
	 ************************************************************************************************/
    private static <K,V extends Comparable<?super V>> Map<K,V> sortValue(Map<K,V> map){
        List<Map.Entry<K,V>> queries = new LinkedList<Map.Entry<K,V>>(map.entrySet());
        Collections.sort(queries, new Comparator<Map.Entry<K,V>>(){
             
        	/************************************************************************************************
        	 * Description: XXXXX  																			*
        	 * 																								*
        	 ************************************************************************************************/
            @Override
            public int compare(Map.Entry<K, V> v1 ,Map.Entry<K,V> v2){
            	
                return (v2.getValue()).compareTo(v1.getValue());
            }
        });
        
        Map<K, V> sortedMap = (Map<K, V>) new LinkedHashMap<K,V>();
        
    	/************************************************************************************************
    	 * Description: XXXXX  																			*
    	 * 																								*
    	 ************************************************************************************************/
        for(Map.Entry<K, V> query : queries){
        	
            sortedMap.put(query.getKey(),query.getValue());
        }
        
        return sortedMap;
    }
}
