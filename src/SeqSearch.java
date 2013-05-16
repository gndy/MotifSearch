package src;

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
	public static void main(String[] args) {
		BufferedReader br = null;
		SimpleNamespace ns = null;
		String workingsequence;
		String searchingsequence;
		String genomename;

		try {
			br = new BufferedReader(new FileReader(args[0]));
			ns = new SimpleNamespace("biojava");
			searchingsequence = args[1].toLowerCase();
			
			if (args[0].toLowerCase().endsWith("gb")) {
				RichSequenceIterator rsi = RichSequence.IOTools.readGenbankDNA(
						br, ns);
				
				while (rsi.hasNext()) {
					RichSequence rs = rsi.nextRichSequence();

					genomename = rs.getDescription();

					workingsequence = rs.getInternalSymbolList().seqString().toLowerCase();
					//System.out.println(genomename+workingsequence);
					MotifSearch(workingsequence, searchingsequence, genomename);

				}

			} else if (args[0].toLowerCase().endsWith("fasta")) {
				RichSequenceIterator rsi = RichSequence.IOTools.readFastaDNA(
						br, ns);
				while (rsi.hasNext()) {
					RichSequence rs = rsi.nextRichSequence();

					genomename = rs.getDescription();

					workingsequence = rs.seqString().toLowerCase();
					MotifSearch(workingsequence, searchingsequence, genomename);

				}

			} else {
				System.out.println("Can not read this file!");
				System.exit(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Since a single file can contain more than a sequence, you need to
	// iterate over
	// rsi to get the information.

	public static void MotifSearch(String workingsequence,
			String searchingsequence, String genomename) {
		try {
			// Variables needed...
			Matcher occurences;
			FiniteAlphabet IUPAC = DNATools.getDNA();
			SymbolList WorkingSequence = DNATools.createDNA(workingsequence);

			// Create pattern using pattern factory.
			Pattern pattern;
			PatternFactory FACTORY = PatternFactory.makeFactory(IUPAC);
			try {
				pattern = FACTORY.compile(searchingsequence);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			System.out.println("Searching for: " + pattern.patternAsString()
					+ " in " + genomename);

			// Obtain iterator of matches.
			try {
				occurences = pattern.matcher(WorkingSequence);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}

			// Foreach match
			while (occurences.find()) {
				System.out.println("Match: " + "\t" + occurences.start() + "\t"
						+ occurences.group().seqString());
			}
		}

		catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
}
