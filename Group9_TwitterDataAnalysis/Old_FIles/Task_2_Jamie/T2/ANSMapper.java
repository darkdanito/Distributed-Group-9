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

		String[] parts = value.toString().split(",");

		String countryCode = parts[10];
		String sentiment = parts[14];
		String airline = parts[16];

		if (countryCode != null && sentiment != null) {

			String countryName = countryCodes.get(countryCode);

			String result = "";
			if (sentiment.equals("negative") && countryName != null) {

				for (int i = 0; i < airline.length(); i++) {
					if (!result.contains(String.valueOf(airline.charAt(i)))) {
						result += String.valueOf(airline.charAt(i));

					}

				}
				context.write(new Text(result + " " + countryName), new IntWritable(1));

			}

		}
	}
}
