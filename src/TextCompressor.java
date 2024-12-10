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

    private static void compress() {

        String s = BinaryStdIn.readString();
        TST tst = new TST();
        int count = 128;
        int max = 1 << 12;
        for (int i = 0; i < s.length(); i++) {
            // find the longest prefix
            String prefix = tst.getLongestPrefix(s, i);
            // write that code
            int prefixCode = tst.lookup(prefix);
            if (prefixCode != -1) {
                BinaryStdOut.write(prefixCode);
            }
            if(max > count) {
                char next = s.charAt(i);

            }


            }


        BinaryStdOut.close();
    }

    private static void expand() {
        int length = BinaryStdIn.readInt(8);
        int lengthTwo = BinaryStdIn.readInt(8);
        String special = "";
        for (int i = 0; i < length; i++) {
            special += BinaryStdIn.readChar();
        }
        String specialtwo = " ";
        for (int i = 0; i < lengthTwo; i++) {
            specialtwo += BinaryStdIn.readChar();
        }
        String text = BinaryStdIn.readString();


        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '@') {
                BinaryStdOut.write(special);
            } else if (text.charAt(i) == '#') {
                BinaryStdOut.write(specialtwo);
            } else {
                BinaryStdOut.write(text.charAt(i));
            }

        }


        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
