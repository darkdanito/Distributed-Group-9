package model.task2;

import java.util.Hashtable;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

/************************************************************************************************
 * Developer: Jamie	  																			*
 * 																								*
 * Date: 03 April 2016  																		*
 * 																								*
 * Description: 
 * Mapper<KEYIN,VALUEIN,KEYOUT,VALUEOUT>
 * Maps input key/value pairs to a set of intermediate key/value pairs.
 * Maps are the individual tasks which transform input records into a intermediate records. 
 * The transformed intermediate records need not be of the same type as the input records. 
 * A given input pair may map to zero or many output pairs.*
 ************************************************************************************************/
public class Task2Mapper extends Mapper<LongWritable, Text, Text, IntWritable> {
	Hashtable<String, String> countryCodes = new Hashtable<>();

	/************************************************************************************************
	 * Description: This part of setup is suppose to get information from the tsv file from the HDFS location. 
																					*
	 ************************************************************************************************/
	@Override
    protected void setup(Mapper<LongWritable, Text, Text, IntWritable>.Context context)
            throws IOException, InterruptedException {
         
        // We will put the ISO-3166-alpha3.tsv to Distributed Cache in the driver class
        // so we can access to it here locally by its name
        BufferedReader br = new BufferedReader(new FileReader("ISO-3166-alpha3.tsv"));
         
        String line = null;
         
    	/************************************************************************************************
    	 * Description: Loop infinitely to read each line of the file. If file has something inside,
    	 * check for any spaces in between, and put them into a Hash table. parts[0] and parts[1] indicate
    	 * the different columns of where to get the information from the tsv file.*
    	 * 																								*
    	 ************************************************************************************************/
        while (true) {
             
            line = br.readLine();
             
            if (line != null) {
            	
                String parts[] = line.split("\t");
                countryCodes.put(parts[0], parts[1]);
                
            } else {
            	
            	// Finished reading
                break;
            }    
        }
         
        br.close();      
    }
 
	/************************************************************************************************
	 * Description: Split the information from Airline-Full-Non-Ag-DFE-Sentiment.csv into different 
	 * column parts.																						*
	 ************************************************************************************************/
    @Override
    protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context)
            throws IOException, InterruptedException {
         
        String[] parts = value.toString().split(",");
         
        String countryCode = parts[10];
        String sentiment = parts[14];
         
    	/************************************************************************************************
    	 * Description: The map function is to filter out information from Airline-Full-Non-Ag-DFE-Sentiment.csv.
	 * Check for any negative sentiments and if the country name is not null.*
	 * set the context.write as countryName and new IntWritable(1) as the total count.																							*
    	 ************************************************************************************************/
        if (countryCode != null && sentiment != null) {
             
            String countryName = countryCodes.get(countryCode);
             
            if (sentiment.equals("negative") && countryName != null) {
                 
                context.write(new Text(countryName), new IntWritable(1));
            }
        }
    }
}
