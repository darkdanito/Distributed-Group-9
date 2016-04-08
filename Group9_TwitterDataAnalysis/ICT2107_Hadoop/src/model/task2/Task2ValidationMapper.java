package model.task2;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

/************************************************************************************************
 * Developer: Jamie	  																			*
 * 																								*
 * Date: 03 April 2016  																		*
 * 																								*
 * Description: The main function for Task2ValidationMapper is to check if the 
 * Airline-Full-Non-Ag-DFE-Sentiment.csv has 27 rows, if it is true, then
 * passed the values to context.write(key, value), if it is false then dont pass the values to
 * context.write*
 ************************************************************************************************/
public class Task2ValidationMapper extends Mapper<LongWritable, Text, LongWritable, Text> {
	
	/************************************************************************************************
	 * Description: XXXXX  																			*
	 * Check for validity of the string value.																								*
	 ************************************************************************************************/
	@Override
    protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, LongWritable, Text>.Context context)
            throws IOException, InterruptedException {
     
        if(isValid(value.toString())){
        	
            context.write(key, value);
        }
    }
     
	/************************************************************************************************
	 * Description: XXXXX  																			*
	 * Split the rows into different parts.																								*
	 ************************************************************************************************/
    private boolean isValid(String line){
     
        String[] parts = line.split(",");
        
    	/************************************************************************************************
    	 * Description: XXXXX  																			*
    	 * Check for the length of the rows.																								*
    	 ************************************************************************************************/
        if (parts.length==27){
         
            return true;
         
        }else{
             
            return false;
         
        }
    }
}
