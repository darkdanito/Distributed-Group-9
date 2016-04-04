package model.task5;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Task5Mapper extends Mapper<LongWritable, Text, Text, Text> {
	
	@Override
	protected void map(LongWritable key, Text value, 
			Mapper<LongWritable, Text, Text, Text>.Context context)
			throws IOException, InterruptedException 
	{	
		if(isValid(value.toString()))
		{	
			String[] parts = value.toString().split(",");
	
			String airline = parts[16];
			String trust = parts[8];
			
			if (airline != null && trust != null) 
			{	
				if (trust.contains(".") || trust.equals("1")){
					context.write(new Text(airline), new Text("one\t" + trust));
			//		System.out.println("Mapper: " + airline + " : " + trust);
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