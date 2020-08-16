/*
 * Course: CS2852 - 072
 * Spring 2020
 * Lab 9 - Huffman Tree
 * Name: Tyler Faulkner
 * Created: 05/12/2020
 */
package faulknert;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Scanner;


/**
 * Driver class for the Huffman Encoding/Decoding Lab
 */
public class HuffmanCode {
    private static final int DECIMAL_TO_PERCENT = 100;
    private static String originalFile = "";

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String input = loadFile(in);

        // build the Huffman Tree
        HuffmanTree tree = HuffmanTreeUtilities.
                buildTree(HuffmanTreeUtilities.calculateFrequencies(input));

        // Print out the resulting Huffman Code
        System.out.println("SYMBOL\tWEIGHT\tHUFFMAN CODE");
        HuffmanTreeUtilities.printCodes(tree, new StringBuilder());

        // Get the output filename from the user
        String filename = saveFile(in);
        // Compress the file
        HuffmanTreeUtilities.compress(tree, filename, input);

        // Report compression savings
        double ogSize = new File(originalFile).length();
        double newSize = new File(filename).length();
        System.out.println("Compression Complete.");
        System.out.println("Old file size: " + (int) ogSize);
        System.out.println("Compression size: " + (int) newSize);
        System.out.printf("Compression savings: %.2f%%\n",
                ((ogSize-newSize)/ogSize)* DECIMAL_TO_PERCENT);
        System.out.println("Press enter to decompress file.");
        in.nextLine();

        // Uncompress the file and print it to the screen
        System.out.println(HuffmanTreeUtilities.decompress(tree, new File(filename)));

    }

    /**
     * Prompts the user for a file to read and verifies it is a .txt file. This method will
     * call the readFile method to read the contents of the text file to a String.
     * @param in Scanner for user input
     * @return A String containing the contents of the file read in.
     */
    private static String loadFile(Scanner in) {
        System.out.print("Please enter file name (exclude .txt): ");
        String fileName = in.nextLine() + ".txt";
        File file = Paths.get(fileName).toFile();
        String filePath = file.toURI().toString();
        System.out.println(filePath);
        String ext = file.toURI().toString().substring(filePath.lastIndexOf('.'));
        while(!ext.equals(".txt")){
            System.out.println("File selected is not a text file.");
            System.out.print("Please enter file name (exclude .txt): ");
            fileName = in.nextLine() + ".txt";
            file = Paths.get(fileName).toFile();
            filePath = file.toURI().toString();
            ext = file.toURI().toString().substring(filePath.lastIndexOf('.'));
        }
        try {
            Scanner fileIn = new Scanner(file);
            originalFile = fileName;
            return readFile(fileIn);
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
        }
        return null;
    }

    /**
     * Reads a file, line by line, adding each line to a String
     * @param fileIn A Scanner that is connected to the file to read
     * @return A String containing tbe contents of the given file.
     */
    private static String readFile(Scanner fileIn) {
        StringBuilder sb = new StringBuilder();
        while(fileIn.hasNextLine()){
            sb.append(fileIn.nextLine());
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Prompts the user for a filename to save the compressed text file to.
     * @param in Scanner for user input
     * @return A String containing the filename. The extension .bin will be attached
     */
    private static String saveFile(Scanner in) {
        System.out.print("Please enter a filename to save " +
                "the compressed file: ");
        return in.nextLine();
    }
}