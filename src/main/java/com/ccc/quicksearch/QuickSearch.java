package com.ccc.quicksearch;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.SortedMap;

import org.apache.commons.collections.Trie;
import org.apache.commons.collections.trie.PatriciaTrie;
import org.apache.commons.collections.trie.StringKeyAnalyzer;

import com.ccc.quicksearch.util.CounterMap;

/**
 * http://wwwconference.org/proceedings/www2010/www/p1249.pdf
 * @author Adam Gibson
 *
 */
public class QuickSearch {

	public final static String chars="A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z";
	public final static String[] charSplit=chars.split(",");
	private PatriciaTrie<String,Integer> trie = new PatriciaTrie<String,Integer>(new StringKeyAnalyzer());

	/**
	 * Instantiate this search with a pre created tree.
	 * @param trie2 the trie to use with this search.
	 */
	public QuickSearch(Trie<String, Integer> trie2) {
		super();
		this.trie = (PatriciaTrie<String, Integer>) trie2;
	}

	/**
	 * Instantiate a search with the given file
	 * @param f the file to load from
	 * @return an instantiated search with a trie based on the 
	 * data in the given file.
	 * @throws IOException
	 */
	public static QuickSearch newSearch(File f) throws IOException  {
		return new QuickSearch(QuickSearch.load(f));
	}
	/**
	 * Load the file in to a trie such that each line in the file is
	 * word SPACE number
	 * @param f the file to load from
	 * @return an initialized trie based on the words and counts in the file
	 * @throws IOException
	 */
	public static  Trie<String,Integer> load(File f) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String line;
		PatriciaTrie<String,Integer> trie= new PatriciaTrie<String,Integer>(new  StringKeyAnalyzer());
		/*
		 * Read line by line the format of:
		 * word  wordcount
		 */
		while((line=reader.readLine())!=null) {
			String[] split=line.split(" ");
			trie.put(split[0],Integer.parseInt(split[1]));
		}
		return trie;
	}

	/**
	 * Returns the most likely word of m length
	 * based on the current context
	 * @param context the context for the most likely next word
	 * @return the most likely next word based on the context.
	 */
	public String otherMostLikely(String context) {
		CounterMap<String,String> counter = new CounterMap<String,String>();
		/*
		 * Iterate through each character, appending
		 * the character to the string to search for all
		 * words of the given prefix.
		 */
		for(String s : charSplit) {
			String test=context + s;
			SortedMap<String,Integer> sorted=trie.getPrefixedBy(test);
			int size=sorted==null ? 0 : sorted.size();
			if(size > 0) {
				counter.incrementCount(s, s, sorted.size());
			}	
		}
		return counter.argMax() != null ?counter.argMax().getFirst() : "";
	}//end otherMostLikely


	/**
	 * Return the probability of a word of length m
	 * @param word the word to get the probability for
	 * @param m the length to get word prefixes for
	 * @return the probability of the given word based on 
	 * length m
	 */
	public double probabilityOfWord(String word,int m) {
		CounterMap<String,String> counter = new CounterMap<String,String>();
		for(int i=0;i<m;i++) {
			SortedMap<String,Integer> sorted=trie.getPrefixedBy(word.substring(0,i), i);
			if(i >= word.length())
				continue;
			Character c=word.charAt(i);
			for(String s : sorted.keySet()) {
				counter.incrementCount(String.valueOf(c), s, sorted.get(s));
			}
		}

		return counter.getCount(counter.argMax().getFirst())/counter.totalCount();
	}


	

}
