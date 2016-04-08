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
 * Description: XXXXX  																			*
 ************************************************************************************************/
public class Task3Mapper extends Mapper<LongWritable, Text,Text,IntWritable> {
	
	Hashtable<String, String> countryCodes = new Hashtable<>();
	
	/************************************************************************************************
	 * Description: XXXXX  																			*
	 * 																								*
	 ************************************************************************************************/
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
		
		BufferedReader br = new BufferedReader(new FileReader("ISO-3166-alpha3.tsv"));

		String line = null;
		
		/************************************************************************************************
		 * Description: XXXXX  																			*
		 * 																								*
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
		 * Description: XXXXX  																			*
		 * 																								*
		 ************************************************************************************************/
		if(countryCode!= null && !countryCode.equals("") && (reasons.equals("badflight") || reasons.equals("CSProblem"))) {
			
			context.write(new Text(countryCodes.get(countryCode)+"\t"+reasons), new IntWritable(1));
		}
	}
}
