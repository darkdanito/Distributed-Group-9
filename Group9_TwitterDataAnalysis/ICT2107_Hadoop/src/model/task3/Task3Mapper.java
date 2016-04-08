package model.task3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/************************************************************************************************
 * Developer: Winnie	  																		*
 * 																								*
 * Date: 03 April 2016  																		*
 * 																								*
 * Description: This class contains the algorithm to filter out the data that is needed. The	*
 *				values are mapped with a key and is send over to the reducer class for 			*
 *				processing.																		*
 ************************************************************************************************/
public class Task3Mapper extends Mapper<LongWritable, Text,Text,IntWritable> {
	
	Hashtable<String, String> countryCodes = new Hashtable<>();
	
	/************************************************************************************************
	 * Description: For this class, a buffered reader is needed to extract out the country names 	*
	 * 				by the country code.																*
	 ************************************************************************************************/
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
		
		BufferedReader br = new BufferedReader(new FileReader("ISO-3166-alpha3.tsv"));

		String line = null;
		
		/************************************************************************************************
		 * Description: The loop below loops through the ISO files to read the country code and name,	*
		 * 				to parts[0] and parts[1] respectively.											*
		 ************************************************************************************************/
		while (true) {
			
			line = br.readLine();
			
			if (line != null) {
				
				String parts[] = line.split("\t");
				countryCodes.put(parts[0], parts[1]);
				
			} else {
				
				break; 
			}
		}
		
		br.close();
		
		String[] parts = value.toString().split(",");
		
		String countryCode = parts[10];
		String reasons = parts[15];
	
		/************************************************************************************************
		 * Description:  The if condition below checks if the country code is null or empty,checks if   *
		 * 				 the reason is equals to "badflight" or "CSProblem". If it fulfills the 		*
		 * 				 conditions, then it will be mapped and sent to the reducer class.				*
		 ************************************************************************************************/
		if(countryCode!= null && !countryCode.equals("") && (reasons.equals("badflight") || reasons.equals("CSProblem"))) {
			
			context.write(new Text(countryCodes.get(countryCode)+"\t"+reasons), new IntWritable(1));
		}
	}
}
