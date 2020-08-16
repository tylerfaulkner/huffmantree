/*
 * Course: CS2852 - 072
 * Spring 2020
 * Lab 9 - Huffman Tree
 * Name: Tyler Faulkner
 * Created: 05/12/2020
 */
package faulknert;

/**
 * An abstract class that defines a HuffmanTree
 */
public abstract class HuffmanTree implements Comparable<HuffmanTree> {
    /**
     * An integer that contains the frequency of the character that may
     * be stored in a leaf node of the HuffmanTree
     */
    final int frequency; // the frequency of this tree

    /**
     * Constructor that takes a starting frequency
     * @param frequency The default starting frequency of a node (usually 0 or 1)
     */
    HuffmanTree(int frequency) {
        this.frequency = frequency;
    }

    /**
     * Method required by Comparable to compare two HuffmanTrees. Implemented by
     * comparing their frequencies
     * @param tree The other HuffmanTree being compared
     * @return 0 if the tree contain the same frequency, a positive integer if the current node's
     * frequency is greater than the other node, a negative integer otherwise.
     *
     */
    @Override
    public int compareTo(HuffmanTree tree) {
        return frequency - tree.frequency;
    }
}

