package T5;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class TwitterReducer extends Reducer<Text, Text, Text, Text> {
	@Override
	protected void reduce(Text key, Iterable<Text> values, 
			Reducer<Text, Text, Text, Text>.Context context) 
					throws IOException, InterruptedException 
	{
		double median = 0;
	
		ArrayList<Double> inputArray = new ArrayList<Double>();
		
		for(Text t: values)
		{
			String parts[] = t.toString().split("\t");
			
			if(parts[0].equals("one"))
			{
				double in = Double.parseDouble(parts[1]);
				inputArray.add(in);
			}
		}
		
		if(inputArray.size() != 0)
		{
			Collections.sort(inputArray);
			
			if (inputArray.size() % 2  == 1)
			{
				median = inputArray.get(((inputArray.size() + 1 )/ 2 - 1));
			}
			else
			{
				double lower = inputArray.get(inputArray.size() / 2 - 1);
				double upper = inputArray.get(inputArray.size() / 2);
		
				median = (lower + upper) / 2;
			}
			
			String countStr = String.valueOf(median);
			
			context.write(key, new Text(countStr));
				
		    System.out.println("Reducer: " + key + " : " + countStr);
		}
		

	}
}