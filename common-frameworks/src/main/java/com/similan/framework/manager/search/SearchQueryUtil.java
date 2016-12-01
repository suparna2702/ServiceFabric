package com.similan.framework.manager.search;


public class SearchQueryUtil {
	
	public static final int PAD = 10;
	public static final String DEFAULT_RATE = "0";
	public static int PRECISION_PAD = 3;
	public static int SCALE_PAD = 2;
	public static int NO_MAXRETURN = -1;
	
	public static String BRIDGE_DILIMIT = "|";
	public static final String BLANK_CHARACTER=" ";
	public static final String DOUBLE_BLANK_CHARACTER="  ";
	public static final String WORD_DELIMIT=",";
	public static final char BOOST_SYMBOL = '^';
	public static final char DOUBLE_QUOTATION_MARKS = '"';
	public static final char FUZZY_SYMBOL = '~';
	public static final char WILDCARD = '*';
	public static final int DEFAULT_BOOST_FACTOR  = 4;
	public static final float NO_BOOST_WEIGHT  = 0.0f;
	public static final String TRUETONE = "TRUETONE";
	public static final String ALL = "All";
	public static final char DASH = '-';
	public static final String NUMBER_PREFIX = "9";
	
	private float similarity;
	
	public static final String[] SPECIALS =
		new String[]{
		"+", "-", "&&", "||", "!", "(", ")", "{", "}",
		"[", "]", "^", "\"", "~", "*", "?", ":", "\\",
	};
	
	public static final String[] SPECIALSTOREMOVE =
		new String[]{
		"+", "-", ".", "!", "(", ")", "{", "}",
		"[", "]", "^", "\"", "~", "*", "?", "_","/",","
	};
	
	public static String YES = "y";
	public static String NO = "n";
	
	public static String escapeSpecials(String clientQuery){
		String regexOr = "";
		for (String special : SPECIALS){
			regexOr += (special
			.equals(SPECIALS[0]) ? "" : "|") + "\\"
			+ special.substring(0, 1);
		}
		clientQuery = clientQuery
		.replaceAll("(?<!\\\\)(" + regexOr + ")",
				BLANK_CHARACTER);
		return clientQuery.trim();
	}
	
	public static String removeSpecials(String clientQuery){
		String regexOr = "";
		for (String special : SPECIALSTOREMOVE){
			regexOr += (special
			.equals(SPECIALSTOREMOVE[0]) ? "" : "|") + "\\"
			+ special.substring(0, 1);
		}
		clientQuery = clientQuery
		.replaceAll("(?<!\\\\)(" + regexOr + ")",
				"");
		return clientQuery.trim();
	}
	
	public static boolean isPhrase(String string) {
		return string == null ? false : (string.contains(BLANK_CHARACTER) );
	}
	
	public float getSimilarity() {
		return similarity;
	}

	public void setSimilarity(float similarity) {
		this.similarity = similarity;
	}

}
