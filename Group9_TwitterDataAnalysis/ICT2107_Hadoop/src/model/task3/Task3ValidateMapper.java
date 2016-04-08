package model.task3;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/************************************************************************************************
 * Developer: Winnie	  																		*
 * 																								*
 * Date: 03 April 2016  																		*
 * 																								*
 * Description: This class validates the mapper class, making sure that it is valid.  			*
 ************************************************************************************************/
public class Task3ValidateMapper extends Mapper<LongWritable, Text, LongWritable, Text> {

	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, LongWritable, Text>.Context context)
			throws IOException, InterruptedException {
		
	/************************************************************************************************
	 * Description: This is the function of isValid, and the columns in the data set is split into  *
	 * 				distinct parts.																	*
	 ************************************************************************************************/
		if(isValid(value.toString())){
			
			context.write(key, value);
		}
	}
	
		/************************************************************************************************
		 * Description: If the columns of the data set is equals to 27, this function will return a     *
		 * 				value of true. If it is not, then it will return false.							*
		 ************************************************************************************************/
	private boolean isValid(String line){
		
		String[] parts = line.split(",");
		
		if (parts.length==27){
			
			return true;
			
		}else{
			
			return false;
		}
	}
}
