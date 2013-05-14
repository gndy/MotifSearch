package seqSearch;

import org.biojava.bio.seq.DNATools;
import org.biojava.bio.symbol.FiniteAlphabet;
import org.biojava.bio.symbol.SymbolList;
import org.biojava.utils.regex.Matcher;
import org.biojava.utils.regex.Pattern;
import org.biojava.utils.regex.PatternFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import org.biojavax.SimpleNamespace;
import org.biojavax.bio.seq.RichSequence;
import org.biojavax.bio.seq.RichSequenceIterator;
 

public class SeqSearch {
		public static void main(String[] args){
			BufferedReader br = null;
			SimpleNamespace ns = null;
			SymbolList workingsequence;
			String searchingsequence;
			String genomename;
	 
			try{
				br = new BufferedReader(new FileReader(args[0]));
				ns = new SimpleNamespace("biojava");
				searchingsequence=args[1].toLowerCase();
				// You can use any of the convenience methods found in the BioJava 1.6 API
				RichSequenceIterator rsi = RichSequence.IOTools.readGenbankDNA(br, ns);
	 
				// Since a single file can contain more than a sequence, you need to iterate over
				// rsi to get the information.
				while(rsi.hasNext()){
					RichSequence rs = rsi.nextRichSequence();
					genomename = rs.getDescription();
					workingsequence = rs.getInternalSymbolList(); 
					MotifSearch(workingsequence, searchingsequence, genomename);
					
				}
			}
			catch(Exception be){
				be.printStackTrace();
				System.exit(-1);
			}
		}
		
		
		public static void MotifSearch(SymbolList workingsequence ,String searchingsequence,String genomename) {
		   try {
			// Variables needed...
			Matcher occurences;
			FiniteAlphabet IUPAC = DNATools.getDNA();
			//SymbolList WorkingSequence = DNATools.createDNA("tagagatagacgatagc");
	 
			// Create pattern using pattern factory.
			Pattern pattern;
			PatternFactory FACTORY = PatternFactory.makeFactory(IUPAC);
			try{
				pattern = FACTORY.compile(searchingsequence);
			} catch(Exception e) {e.printStackTrace(); return;}
				System.out.println("Searching for: "+pattern.patternAsString()+" in "+genomename);
	 
			// Obtain iterator of matches.
			try {
				occurences = pattern.matcher( workingsequence );
			} catch(Exception e) {e.printStackTrace(); return;}
	 
			// Foreach match
			while( occurences.find() ) {
				System.out.println("Match: " +"\t"+ occurences.start() +"\t"+ occurences.group().seqString());
			}
			}
	 
			catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}

