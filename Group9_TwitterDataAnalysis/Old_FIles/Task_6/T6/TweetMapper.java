package T6;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class TweetMapper extends Mapper<LongWritable, Text, Text, Text> {
	
	@Override
	protected void map(LongWritable key, Text value, 
			Mapper<LongWritable, Text, Text, Text>.Context context)
			throws IOException, InterruptedException 
	{	
		if(isValid(value.toString()))
		{	
			String[] parts = value.toString().split(",");
	
			String airline = parts[16];
			String tweet = parts[21];
			
			if (airline != null && tweet != null) 
			{	
					context.write(new Text("Delayed Flights"), new Text("one\t" + tweet));
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