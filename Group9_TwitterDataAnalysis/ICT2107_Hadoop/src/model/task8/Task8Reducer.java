package model.task8;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.io.Text;

/************************************************************************************************
 * Developer: Yun Yong 																			*
 * 																								*
 * Date: 04 April 2016  																		*
 * 																								*
 * Description: XXXXX  																			*
 ************************************************************************************************/
public class Task8Reducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    
	private IntWritable result = new IntWritable();

	/************************************************************************************************
	 * Description: XXXXX  																			*
	 * 																								*
	 ************************************************************************************************/
    public void reduce(Text key, Iterable<IntWritable> values, Context context
                       ) throws IOException, InterruptedException {
    
      int sum = 0;
      
      for (IntWritable val : values) {
      
    	  sum += val.get();
      
      }
      
      result.set(sum);
      context.write(key, result);
    }
  }