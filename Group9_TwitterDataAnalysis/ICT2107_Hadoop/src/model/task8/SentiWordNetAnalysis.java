package model.task8;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SentiWordNetAnalysis {

	public enum SENTIMENT {HAPPY, NEUTRAL, SAD, ANGRY};
	private Map<String, Double> dictionary;

	public SentiWordNetAnalysis(List<String> wordList) throws IOException {
		
		// This is our main dictionary representation
		dictionary = new HashMap<String, Double>();

		// From String to list of doubles.
		HashMap<String, HashMap<Integer, Double>> tempDictionary = new HashMap<String, HashMap<Integer, Double>>();

		try {
			int lineNumber = 0;

			for (String line : wordList) {
				lineNumber++;

				// If it's a comment, skip this line.
				if (!line.trim().startsWith("#")) {
					// We use tab separation
					String[] data = line.split("\t");
					String wordTypeMarker = data[0];

					// Is it a valid line? Otherwise, through exception.
					if (data.length != 6) {
						throw new IllegalArgumentException(
								"Incorrect tabulation format in file, line: " + lineNumber);
					}

					// Calculate synset score as score = PosS - NegS
					// A synset refers to one line
					Double synsetScore = Double.parseDouble(data[2])
							- Double.parseDouble(data[3]);

					// Get all Synset terms (words)
					String[] synTermsSplit = data[4].split(" ");

					// Go through all terms of current synset.
					for (String synTermSplit : synTermsSplit) {
						
						// Get synterm and synterm rank
						String[] synTermAndRank = synTermSplit.split("#");
						
						// Return the sysTerm concatenated with the word type
						String synTerm = synTermAndRank[0] + "#" + wordTypeMarker;
						
						// Rank defines how commonly used it is
						int synTermRank = Integer.parseInt(synTermAndRank[1]);
						
						// What we get here is a map of the type:
						// term -> {score of synset#1, score of synset#2...}

						// Add map to term if it doesn't have one 
						// tempDictionary -> HashMap<String, HashMap<Integer, Double>>
						if (!tempDictionary.containsKey(synTerm)) {
							tempDictionary.put(synTerm,
									new HashMap<Integer, Double>());
						}

						// Add synset link to synterm
						tempDictionary.get(synTerm).put(synTermRank,synsetScore);
					}
				}				
			}
			
			// Go through all the terms.
			for (Map.Entry<String, HashMap<Integer, Double>> entry : tempDictionary
					.entrySet()) {
				
				String word = entry.getKey();
				Map<Integer, Double> synSetScoreMap = entry.getValue(); //SymTermRank (int) and SymScore (double)

				// Calculate weighted average. Weigh the synsets according to
				// their rank.
				// Score= 1/2*first + 1/3*second + 1/4*third ..... etc.
				// Sum = 1/1 + 1/2 + 1/3 ...
				double score = 0.0;
				double sum = 0.0;
				
				for (Map.Entry<Integer, Double> setScore : synSetScoreMap.entrySet()) {
				
					score += setScore.getValue() / (double) setScore.getKey();
					sum += 1.0 / (double) setScore.getKey();
				}
				
				score /= sum;

				dictionary.put(word, score);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Double extract(String word)
	{
	    Double total = new Double(0);
	    
	    if(dictionary.get(word + "#n") != null)
	         total = dictionary.get(word + "#n") + total;
	    
	    if(dictionary.get(word + "#a") != null)
	        total = dictionary.get(word + "#a") + total;
	    
	    if(dictionary.get(word + "#r") != null)
	        total = dictionary.get(word + "#r") + total;
	    
	    if(dictionary.get(word + "#v") != null)
	        total = dictionary.get(word + "#v") + total;
	    
	    return total;
	}
	
	// Analyse every word in line of a tweet for a score
	public SENTIMENT analyze(String text) {
		
		SENTIMENT sentiment = SENTIMENT.NEUTRAL;
		String[] words = text.split("\\s+"); 
		
		double totalScore = 0, averageScore;
		
		for(String word : words) {
		
			word = word.replaceAll("([^a-zA-Z\\s])", "");
		    
			if (this.extract(word) == null)
		        continue;
		    totalScore += this.extract(word);
		}
		averageScore = totalScore;

		if(averageScore >= 0.6) {
			
			sentiment = SENTIMENT.HAPPY;
		}
		else if(averageScore > 0 && averageScore < 0.6) {
			
			sentiment = SENTIMENT.NEUTRAL;
		}
		else if(averageScore < 0 && averageScore >= -0.3) {
			
			sentiment = SENTIMENT.ANGRY;
		}
		else if(averageScore < 0.3) {
			
			sentiment = SENTIMENT.SAD;
		}
		
		return sentiment;
	}
	
}