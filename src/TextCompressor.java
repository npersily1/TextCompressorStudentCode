/******************************************************************************
 *  Compilation:  javac TextCompressor.java
 *  Execution:    java TextCompressor - < input.txt   (compress)
 *  Execution:    java TextCompressor + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *  Data files:   abra.txt
 *                jabberwocky.txt
 *                shakespeare.txt
 *                virus.txt
 *
 *  % java DumpBinary 0 < abra.txt
 *  136 bits
 *
 *  % java TextCompressor - < abra.txt | java DumpBinary 0
 *  104 bits    (when using 8-bit codes)
 *
 *  % java DumpBinary 0 < alice.txt
 *  1104064 bits
 *  % java TextCompressor - < alice.txt | java DumpBinary 0
 *  480760 bits
 *  = 43.54% compression ratio!
 ******************************************************************************/

import java.util.Hashtable;
import java.util.Map;

/**
 * The {@code TextCompressor} class provides static methods for compressing
 * and expanding natural language through textfile input.
 *
 * @author Zach Blick, Noah Persily
 */
public class TextCompressor {
    public static final int BIT_COUNT = 12;
    public static final int START = 257;
    public static final int MAX = 1 << BIT_COUNT;

    private static void compress() {

        String s = BinaryStdIn.readString();
        TST tst = new TST();
        for (int i = 0; i < 256; i++) {
            tst.insert("" + ((char) i), i);
        }

        int currentCode = START;
        int length = s.length();
        for (int i = 0; i < length; i++) {
            // find the longest prefix
            String prefix = tst.getLongestPrefix(s, i);
            BinaryStdOut.write(tst.lookup(prefix), BIT_COUNT);

            if (currentCode < MAX && i + prefix.length() < length) {
                String lookAhead = prefix + s.charAt(i + prefix.length());
                tst.insert(lookAhead, currentCode++);
            }

        }

        BinaryStdOut.write(START - 1, BIT_COUNT);
        BinaryStdOut.close();
    }

    private static void expand() {

        String[] prefixes = new String[MAX];

        for (int i = 0; i < START - 1; i++) {
            prefixes[i] = "" + (char) i;
        }
        int codeCounter = START;

        int code = BinaryStdIn.readInt(12);
        while (code != START - 1) {

            BinaryStdOut.write(prefixes[code]);

            int nextCode = BinaryStdIn.readInt(BIT_COUNT);

            if(prefixes[nextCode] == null) {
                prefixes[codeCounter] = prefixes[code] + prefixes[code].charAt(0);
            } else {
                prefixes[codeCounter] = prefixes[code] + prefixes[nextCode].charAt(0);
            }

            code = nextCode;

        }



        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
