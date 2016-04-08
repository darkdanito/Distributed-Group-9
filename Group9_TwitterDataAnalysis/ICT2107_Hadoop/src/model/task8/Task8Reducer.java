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
 * Description: Reducer class for Task 8.														*
 ************************************************************************************************/
public class Task8Reducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    
	private IntWritable result = new IntWritable();

	/************************************************************************************************
	 * Description: Reducer method for Task 8. It will do a count of the different type of emotion	*
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