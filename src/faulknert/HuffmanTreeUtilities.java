/*
 * Course: CS2852 - 072
 * Spring 2020
 * Lab 9 - Huffman Tree
 * Name: Tyler Faulkner
 * Created: 05/12/2020
 */
package faulknert;

import edu.msoe.jones.bitstream.BitInputStream;
import edu.msoe.jones.bitstream.BitOutputStream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Contains necessary classes and methods for a huffman tree.
 */
public class HuffmanTreeUtilities {
    /**
     * A Leaf in a huffman tree
     */
    public static class HuffmanLeaf extends HuffmanTree {
        private final char value;
        /**
         * Constructor that takes a starting frequency
         *
         * @param frequency The default starting frequency of a node (usually 0 or 1)
         * @param val value character of the leaf node
         */
        private HuffmanLeaf(int frequency, char val) {
            super(frequency);
            value = val;
        }
    }

    /**
     * A node class that has children nodes
     */
    public static class HuffmanNode extends HuffmanTree {
        private final HuffmanTree left;
        private final HuffmanTree right;

        /**
         * Constructor that takes a left and right class of a huffman tree.
         * @param left left node
         * @param right right node
         */
        private HuffmanNode(HuffmanTree left, HuffmanTree right){
            super(left.frequency + right.frequency);
            this.left = left;
            this.right = right;
        }
    }

    private static final int ASCII_NEW_LINE = 10;
    private static final int ASCII_VALUES = 256;
    private static final HashMap<Character, String> CODE_TABLE = new HashMap<>();

    /**
     * Creates a frequency table for the characters in the inputted string
     * @param input the string to create the frequency table off of.
     * @return an integer array of each characters frequency
     */
    public static int[] calculateFrequencies(String input) {
        int[] frequency = new int[ASCII_VALUES];
        for(int i = 0; i < input.length(); ++i){
            char currentChar = input.charAt(i);
            frequency[currentChar]++;
        }
        return frequency;
    }

    /**
     * Creates a huffman tree based of a character frequency array.
     * @param characterFrequencies integer array of character frequencies
     * @return a complete HuffmanTree
     */
    public static HuffmanTree buildTree(int[] characterFrequencies) {
        PriorityQueue<HuffmanTree> queue = new PriorityQueue<>();
        for(int i = 0; i < ASCII_VALUES; ++i) {
            if (characterFrequencies[i] != 0) {
                queue.offer(new HuffmanLeaf(characterFrequencies[i], (char) i));
            }
        }
        while (queue.size() > 1){
            queue.offer(new HuffmanNode(queue.poll(), queue.poll()));
        }
        HuffmanTree tree = queue.poll();
        return tree;
    }

    /**
     * Prints each character it's weight and code on each line
     * @param tree the tree to print
     * @param prefix prefix to add to each bit value
     */
    public static void printCodes(HuffmanTree tree, StringBuilder prefix) {
        Traverse traverse = (leaf, sb) -> System.out.println(leaf.value
                + "     \t" + leaf.frequency
                + "   \t" + sb.toString());
        preorderTraversal(tree, prefix, traverse);
    }

    /**
     * Constructs a hash table based on the given tree
     * @param tree the tree to create the hash table off of
     * @param prefix prefix for each bit value
     */
    public static void buildTable(HuffmanTree tree, StringBuilder prefix) {
        CODE_TABLE.clear();
        Traverse traverse = (leaf, sb) -> CODE_TABLE.put(leaf.value, sb.toString());
        preorderTraversal(tree, prefix, traverse);
    }

    /**
     * Compresses a given text string into a bit file based on the given tree
     * @param tree tree to create hash table from
     * @param filename the file name to save the compressed file
     * @param input the text to convert to bits
     */
    public static void compress(HuffmanTree tree, String filename, String input){
        StringBuilder sb = new StringBuilder();
        buildTable(tree, sb);
        try (Scanner in = new Scanner(input)) {
            try (BitOutputStream out = new BitOutputStream(
                    new FileOutputStream(new File(filename)))) {
                while (in.hasNextLine()) {
                    String line = in.nextLine();
                    for (int i = 0; i < line.length(); ++i) {
                        String bits = CODE_TABLE.get(line.charAt(i));
                        for(int j = 0; j< bits.length(); ++j) {
                            out.write(Integer.parseInt(bits.charAt(j) + ""));
                        }
                    }
                    String newLine = CODE_TABLE.get('\n');
                    for(int i = 0; i < newLine.length(); ++i) {
                        out.write(Integer.parseInt(newLine.charAt(i) + ""));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not Found.");
        } catch (IOException e){
            System.out.println("Error: Critical");
        }
    }

    /**
     * Decompresses a compressed bit file back into text form
     * @param tree the tree with characters
     * @param file the file to decompress
     * @return the text version of the binary file
     */
    public static String decompress(HuffmanTree tree, File file) {
        StringBuilder decompressed = new StringBuilder();
        try(BitInputStream in = new BitInputStream(new FileInputStream(file))) {
            StringBuilder code = new StringBuilder();
            int bit = in.read();
            while(bit != -1){
                code.append(bit);
                String character = decompressTraversal(tree, code.toString());
                if(character != null){
                    decompressed.append(character);
                    code.delete(0, code.length());
                }
                bit = in.read();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File Not Found");
        } catch (IOException e) {
            System.out.println("Error: Critical");
        }
        return decompressed.toString();
    }

    private static void preorderTraversal(HuffmanTree tree,
                                          StringBuilder prefix, Traverse traverse) {
        if(tree instanceof HuffmanLeaf){
            StringBuffer sb = new StringBuffer(prefix);
            traverse.process((HuffmanLeaf) tree, sb);
        } else {
            HuffmanNode node = (HuffmanNode) tree;
            StringBuilder left = new StringBuilder(prefix);
            StringBuilder right = new StringBuilder(prefix);
            preorderTraversal(node.left, left.append("0"), traverse);
            preorderTraversal(node.right, right.append("1"), traverse);
        }
    }

    private static String decompressTraversal(HuffmanTree tree, String code){
        if(code.equals("") && tree instanceof HuffmanLeaf){
            return (((HuffmanLeaf) tree).value + "");
        } else if (code.equals("") && tree instanceof HuffmanNode){
            return null;
        } else {
            HuffmanNode node = (HuffmanNode) tree;
            if (code.charAt(0) == '0') {
                return decompressTraversal(node.left, code.substring(1));
            } else {
                return decompressTraversal(node.right, code.substring(1));
            }
        }
    }
}
