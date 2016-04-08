package airlinenegativesentim;

import java.io.IOException;
import java.util.HashMap;
import java.util.*;
 
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
 
public class ANSReducer extends Reducer<Text,IntWritable,Text,IntWritable>{
    Text totalIW = new Text();
    private Map<String, Integer> sortMap = new HashMap<>();
 
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
     
    private static <K,V extends Comparable<?super V>> Map<K,V> sortValue(Map<K,V> map){
        List<Map.Entry<K,V>> queries = new LinkedList<Map.Entry<K,V>>(map.entrySet());
         
         
 
         
        Collections.sort(queries, new Comparator<Map.Entry<K,V>>(){
             
            @Override
            public int compare(Map.Entry<K, V> v1 ,Map.Entry<K,V> v2){
                return (v2.getValue()).compareTo(v1.getValue());
            }
        });
        
        Map<K, V> sortedMap = (Map<K, V>) new LinkedHashMap<K,V>();
        
        for(Map.Entry<K, V> query : queries){
            sortedMap.put(query.getKey(),query.getValue());
        }
        
        return sortedMap;
    }
     
     
}