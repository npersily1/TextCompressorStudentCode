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
        String[] words = s.split(" ");
        Hashtable<String, Integer> map = new Hashtable<>();

        for (int i = 0; i < words.length; i++) {
            if (map.containsKey(words[i])) {
                map.replace(words[i], 1 + map.get(words[i]));
            } else {
                map.put(words[i], 1);
            }
        }
        // Put hash map into pair objects ArrayList
        // Then Find the greatest and add it your array of thirty while the array isn't filled
        // Then when I have the 30 things assign them to characters 200-230 and then do the same compression as before


        int max = 0;
        int secondMax = 0;
        for (int i : map.values()) {
            if (i > max) {
                secondMax = max;
                max = i;
            }
        }
        String mostCommon = "";
        String secondCommon = "";
        for (String k : map.keySet()) {
            if (map.get(k) == max) {
                mostCommon = k;
                continue;
            }
            if (map.get(k) == secondMax) {
                secondCommon = k;
            }

        }

        BinaryStdOut.write(mostCommon.length(), 8);
        BinaryStdOut.write(mostCommon);
        BinaryStdOut.write(secondCommon.length(), 8);
        BinaryStdOut.write(secondCommon);

        for (int i = 0; i < words.length; i++) {
            if (words[i].equals(mostCommon)) {
                BinaryStdOut.write('@', 8);
            } else if (words[i].equals(secondCommon)) {
                BinaryStdOut.write('#', 8);
            } else {
                BinaryStdOut.write(words[i]);
                BinaryStdOut.write(' ', 8);
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
