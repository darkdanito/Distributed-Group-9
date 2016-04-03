package T4;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class AirlineMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
	
	IntWritable one = new IntWritable(1);
	
	@Override
	protected void map(LongWritable key, Text value, 
			Mapper<LongWritable, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException 
	{	
		if(isValid(value.toString()))
		{	
			String[] parts = value.toString().split(",");
	
			String airline = parts[16];
			String tweet = parts[14];
			
			if (airline != null && tweet != null) 
			{	
				if (tweet.equals("positive")){
					
					context.write(new Text(airline), one);
				//	System.out.println("Mapper: " + airline + " : 1");
				}
			}
		} 
	}

	private boolean isValid(String line)
	{
		String[] parts = line.split(",");
		
		if (parts.length==27)
		{
			return true;
		
		}else
		{
			return false;
			
		}
	}
}