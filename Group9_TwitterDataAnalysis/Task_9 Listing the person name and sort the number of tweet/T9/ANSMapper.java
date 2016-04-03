import java.util.Hashtable;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

public class ANSMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
	Hashtable<String, String> countryCodes = new Hashtable<>();

	@Override
	protected void setup(Mapper<LongWritable, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
		// We will put the ISO-3166-alpha3.tsv to Distributed Cache in the
		// driver class
		// so we can access to it here locally by its name
		BufferedReader br = new BufferedReader(new FileReader("ISO-3166-alpha3.tsv"));
		String line = null;
		while (true) {
			line = br.readLine();
			if (line != null) {
				String parts[] = line.split("\t");
				countryCodes.put(parts[0], parts[1]);
			} else {
				break;// finished reading
			}
		}
		br.close();
	}

	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
		// splitting all column
		String[] parts = value.toString().split(",");

		// taking the name column
		String name = parts[18];

		if (name != null) {
			context.write(new Text(name), new IntWritable(1));
		}
	}
}
