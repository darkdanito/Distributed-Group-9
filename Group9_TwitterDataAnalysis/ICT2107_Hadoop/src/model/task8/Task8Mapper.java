package model.task8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;

/************************************************************************************************
 * Developer: Yun Yong 																			*
 * 																								*
 * Date: 04 April 2016  																		*
 * 																								*
 * Description: XXXXX  																			*
 ************************************************************************************************/
public class Task8Mapper extends Mapper<Object, Text, Text, IntWritable> {

	private final static IntWritable one = new IntWritable(1);
	private Text word = new Text();
	private List<String> sentiWordList;
	private SentiWordNetAnalysis sentiWordNetAnalyser;
	
	String tweet;

	/************************************************************************************************
	 * Description: XXXXX  																			*
	 * 																								*
	 ************************************************************************************************/
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
	
		String record = value.toString();
		
		if (isValid(record)) {
		
			String[] parts = record.split(",");
			tweet = parts[21];
		}
		
		if (this.sentiWordList == null) {
		
			getSentiFile(context);
		}

		if (tweet != null) {
			
			String senti = sentiWordNetAnalyser.analyze(tweet).toString();
			word.set(senti);
			context.write(word, one);
		}

	}

	/************************************************************************************************
	 * Description: XXXXX  																			*
	 * 																								*
	 ************************************************************************************************/
	private boolean isValid(String line) {
		
		String[] parts = line.split(",");
		
		if (parts.length == 27) {
			
			return true;
		
		} else {
		
			return false;
		}
	}

	/************************************************************************************************
	 * Description: XXXXX  																			*
	 * 																								*
	 ************************************************************************************************/
	private void getSentiFile(Context context) throws IOException {
		
		Configuration conf = context.getConfiguration();
		conf.set("sentiwordnetfile", "/user/phamvanvung/group9_hadoop/SentiWordNet.txt");
		
		String swnPath = conf.get("sentiwordnetfile");
		
		this.sentiWordList = new ArrayList<String>();
		
		try {
		
			FileSystem fs = FileSystem.get(new URI("hdfs://localhost:9000"), conf);
			BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(new Path(swnPath))));
			
			String line;
			
			line = br.readLine();
			
			while (line != null) {
			
				sentiWordList.add(line);
				line = br.readLine();
			}
		} catch (Exception e) {
			
			System.out.println("Bufffered Reader Error: " + e.getMessage());
			
			throw new IOException(e);
		}
		
		sentiWordNetAnalyser = new SentiWordNetAnalysis(sentiWordList);
	}
}