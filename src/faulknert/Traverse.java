/*
 * Course: CS2852 - 072
 * Spring 2020
 * Lab 9 - Huffman Tree
 * Name: Tyler Faulkner
 * Created: 05/12/2020
 */
package faulknert;

/**
 * A Functional Interface that implements a specific action taken when doing
 * a traversal of a Huffman Tree and visiting a leaf node.
 */
@FunctionalInterface
interface Traverse {
    /**
     * Generic method that performs an operation on a HuffmanLeaf object
     * @param leaf - HuffmanLeaf object to visit
     * @param sb - StringBuffer to store the result of the visit
     */
    void process(HuffmanTreeUtilities.HuffmanLeaf leaf, StringBuffer sb);
}
