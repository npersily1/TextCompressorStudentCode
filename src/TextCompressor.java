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



/**
 * The {@code TextCompressor} class provides static methods for compressing
 * and expanding natural language through textfile input.
 *
 * @author Zach Blick, Noah Persily
 */
public class TextCompressor {
    public static final int BIT_COUNT = 12;
    public static final int EOF = 256;
    public static final int MAX = 1 << BIT_COUNT;

    private static void compress() {

        // Populate TST with ascii values
        TST tst = new TST();
        for (int i = 0; i < EOF; i++) {
            tst.insert("" + ((char) i), i);
        }

        int currentCode = EOF + 1;
        String s = BinaryStdIn.readString();
        int length = s.length();

        // For every letter
        for (int i = 0; i < length; i++) {
            // Find the longest prefix and write
            String prefix = tst.getLongestPrefix(s, i);
            BinaryStdOut.write(tst.lookup(prefix), BIT_COUNT);

            // Increment i depending on how long the prefix written out is
            i += prefix.length() - 1;

            // If you are not out of bounds add a new prefix
            if (currentCode < MAX && i + 1 < length) {

                tst.insert(prefix + s.charAt(1 + i), currentCode++);
            }
        }

        BinaryStdOut.write(EOF, BIT_COUNT);
        BinaryStdOut.close();
    }

    private static void expand() {
        // Populate map with ascii characters
        String[] prefixes = new String[MAX];

        for (int i = 0; i < EOF; i++) {
            prefixes[i] = "" + (char) i;
        }
        int codeCounter = EOF + 1;
        int code = BinaryStdIn.readInt(BIT_COUNT);

        while (true) {
            // Read the next code and write out the current code
            int nextCode = BinaryStdIn.readInt(BIT_COUNT);
            BinaryStdOut.write(prefixes[code]);

            // If done, break
            if (nextCode == EOF) {
                break;
            }

            // If in bounds
            if (codeCounter < MAX) {
                // If in edge case, use appropriate prefix
                if (prefixes[nextCode] == null) {
                    prefixes[codeCounter] = prefixes[code] + prefixes[code].charAt(0);

                } else {
                    prefixes[codeCounter] = prefixes[code] + prefixes[nextCode].charAt(0);
                }
                codeCounter++;
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
