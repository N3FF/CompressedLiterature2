// Justin Neff - TCSS 342 Compressed Literature 2

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public class Main {

  public static void main(String[] args) {
    compressFile();
    // testMyHashTable();
    // testCodingTree();
  }

  private static void compressFile() {

    Long start = System.currentTimeMillis();
    byte bytes[] = null;
    String fileLoc = "./src/WarAndPeace.txt";
    //String fileLoc = "./src/Dracula.txt";
    CodingTree tree = null;

    // READ TEXT TO STRING
    Path path = Paths.get(fileLoc);
    try {
      bytes = Files.readAllBytes(path);
      BufferedReader br = new BufferedReader(new FileReader(fileLoc));
      StringBuilder sb = new StringBuilder();
      for (int c = br.read(); c >= 0; c = br.read()) {
        sb.append((char)c);
      }
      br.close();
      tree = new CodingTree(sb.toString());
    } catch (IOException e) {
    }

    // WRITE COMPRESSED TO FILE
    File file = new File("./src/compressed.txt");
    try {
      if (file.exists()) {
        file.delete();
      }
      file.createNewFile();
      DataOutputStream os = new DataOutputStream(new FileOutputStream(file));
      os.write(tree.bytes);
      os.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    // WRITE HUFFMAN CODES TO FILE
    file = new File("./src/codes.txt");
    try {
      PrintWriter pw = new PrintWriter(new FileOutputStream(file));
      pw.write(tree.wordToBinary.toString());
      pw.close();
    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
    }

    System.out.println("--------------------------------------------------");
    System.out.println("Execution Time: " + ((new Date()).getTime() - start) + " ms");
    System.out.println("Original Size: " + bytes.length + " bytes");
    System.out.println("Compressed Size: " + tree.bytes.length + " bytes");
    System.out
        .println("Compression: " + (100 - (float) 100 * tree.bytes.length / bytes.length) + "%");
  }

  private static void testCodingTree() {
    CodingTree tree = new CodingTree("Test Tickle The Tree To Ten Times The Tempo");
    System.out.println("Test Binary: " + tree.wordToBinary.get(" "));
    System.out.println("Tree Root: " + tree.root);
    System.out.println("Tree Access: " + tree.wordCount.size());
    System.out.println("Byte Test: " + tree.bytes[1]);
    // System.out.println(tree.wordCount);
  }

  private static void testMyHashTable() {
    MyHashTable table = new MyHashTable<>(1 << 2);
    table.put("This", "That");
    table.put("1", "2");
    table.put("Queue", "Five");
    System.out.println(table.toString());
    System.out.println(table.get("1"));
    System.out.println(table.containsKey("1"));
    table.stats();
  }
}
