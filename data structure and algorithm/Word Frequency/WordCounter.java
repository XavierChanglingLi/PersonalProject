/**
*Author: Changling Li
*Date: 10/26/19
*Name: WordCounter.java
*loop through the input file and count the word frequency
*/

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.ArrayList;

public class WordCounter{
	//creates and manages a BSTMap of word-count pairs
	private int totalcount;
	private BSTMap<String, Integer> bst;

	public WordCounter(){
		//constructor
		StringAscending comp = new StringAscending();
		bst = new BSTMap<String, Integer>(comp);
		totalcount = 0;
	}

	public void analyze(String filename){
		try{
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);

			String line = br.readLine();
			//split line into words. 
			//as split on anything that is not (^)
			while(line!=null){
				String[] words = line.split("[^a-zA-Z0-9]");
				for(int i=0;i<words.length;i++){
					String word = words[i].trim().toLowerCase();
					if(word.length()==0){continue;}
					else{
						totalcount++;
						if(!bst.containsKey(word)){bst.put(word, 1);}
						else{
							int v = this.getCount(word);
							bst.put(word, v+1);
						}
					}
				}
				line = br.readLine();
			}
			br.close();
		}
		catch(FileNotFoundException ex){
			System.out.println("Board.read()::unable to open file"+filename);
		}
		catch(IOException ex){
			System.out.println("Board.read():: error reading file"+filename);
		}
	}

	public int getTotalWordCount(){
		//returns the total word count
		return totalcount;
	}

	public int getUniqueWordCount(){
		//return the number of unique words which should be the size of the BSTMap
		return bst.size();
	}

	public int getCount(String word){
		//returns the frequency value associated with this word
		if(!bst.containsKey(word)){
			return 0;
		}
		else{
			return bst.get(word);
		}
	}

	public double getFrequency(String word){
		//returns the value associated with this word divided by the total word count
		double c = this.getCount(word);
		double t = this.getTotalWordCount();
		double frequency = c/t;
		return frequency;
	}

	public void writeWordCountFile(String filename){
		try{
			FileWriter fw = new FileWriter(filename);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.append("totalWordCount "+ String.valueOf(getTotalWordCount())+"\n");
			ArrayList<KeyValuePair<String,Integer>> kvp = bst.entrySet();
			for(KeyValuePair<String, Integer> pair: kvp){
				bw.append(pair.getKey()+" "+pair.getValue()+"\n");
			}
			bw.close();
		}
		catch(IOException ex){
			System.out.println("Board.read():: error reading file"+filename);
		}
	}

	public void readWordCountFile(String filename){
		try{
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			line = br.readLine();
			while(line!=null){
				String[] words = line.split(" ");
				for(int i=0;i<words.length;i+=2){
					String word = words[i];
					Integer v = Integer.valueOf(words[i+1]);
					bst.put(word, v);
				}
				line=br.readLine();
			}
			br.close();
		}
		catch(FileNotFoundException ex){
			System.out.println("Board.read()::unable to open file"+filename);
		}
		catch(IOException ex){
			System.out.println("Board.read():: error reading file"+filename);
		}
	}

	public static void main(String[] args){
		WordCounter wc = new WordCounter();
		if(args.length<1){
			System.out.println("specify input file name");
			System.exit(1);}
		for(int i=0; i<args.length; i++){
			long begin = System.currentTimeMills();
			wc.analyze(args[i]+".txt");
			wc.writeWordCountFile(args[i]+"_wordCount.txt");
			long end = System.currentTimeMills();
			long difference = end-begin;
			System.out.println("The time for processing" + args[i] + "is" + difference);
		}
		wc.analyze(args[0]);

		//System.out.println(wc.getTotalWordCount());
		//System.out.println(wc.getUniqueWordCount());
		//System.out.println(wc.getCount("best"));
		//System.out.println(wc.getFrequency("best"));
		wc.writeWordCountFile(args[1]);
	}

}
