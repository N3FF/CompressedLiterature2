// Justin Neff - TCSS 342 Compressed Literature 2

import java.util.PriorityQueue;
import java.util.Set;

public class CodingTree {

  MyHashTable<String, Integer> wordCount = new MyHashTable<String, Integer>(1 << 15);
  MyHashTable<String, String> wordToBinary = new MyHashTable<String, String>(1 << 15);
  TreeNode root = null;
  PriorityQueue<TreeNode> queue = new PriorityQueue<>();
  byte[] bytes;

  CodingTree(String fulltext) {
    for (int i = 0, start = 0; i < fulltext.length(); i++) {
      if (!isPartOfWord(fulltext.charAt(i))) {
        if (i > start) {
          String word = fulltext.substring(start, i);
          wordCount.put(word, (wordCount.containsKey(word) ? wordCount.get(word) + 1 : 1));
        }
        String delimiter = fulltext.substring(i, i + 1);
        wordCount.put(delimiter,
            (wordCount.containsKey(delimiter) ? wordCount.get(delimiter) + 1 : 1));
        start = i + 1;
      }
    }
    
    buildQueue();
    root = buildTree();
    buildTable(root, "");
    writeFile(fulltext);
    wordCount.stats();
  }

  private boolean isPartOfWord(char c) {
    return ((c >= '0' && '9' >= c) || (c >= 'A' && 'Z' >= c) || (c >= 'a' && 'z' >= c) || c == '\''
        || c == '-');
  }

  private void writeFile(String fulltext) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0, start = 0; i < fulltext.length(); i++) {
      if (!isPartOfWord(fulltext.charAt(i))) {
        if (i > start) {
          sb.append(wordToBinary.get(fulltext.substring(start, i)));
        }
        sb.append(wordToBinary.get(fulltext.substring(i, i + 1)));
        start = i + 1;
      }
    }
    int binLength = sb.length();
    bytes = new byte[binLength / 8];
    for (int i = 0; i < binLength / 8; i++) {
      bytes[i] = (byte) Integer.parseInt(sb.substring(i * 8, i * 8 + 8), 2);
    }
  }

  private void buildQueue() {
    Set<String> keys = wordCount.keySet();
    for (String s : keys) {
      queue.offer(new TreeNode(wordCount.get(s), s));
    }
  }

  private TreeNode buildTree() {
    TreeNode temp = null;
    if (queue.size() > 1) {
      while (!queue.isEmpty()) {
        TreeNode t = new TreeNode(queue.poll(), queue.poll());
        t.value = t.left.value + t.right.value;
        if (!queue.isEmpty()) {
          queue.offer(t);
        } else {
          temp = t;
        }
      }
    } else {
      temp = queue.poll();
    }
    return temp;
  }

  private void buildTable(TreeNode node, String str) {
    if (node.isLeaf) {
      wordToBinary.put(node.word, str);
    } else {
      buildTable(node.left, str + '0');
      buildTable(node.right, str + '1');
    }
  }

  private class TreeNode implements Comparable<TreeNode> {
    int value = 0;
    String word = null;
    boolean isLeaf = true;
    TreeNode left = null;
    TreeNode right = null;

    TreeNode(TreeNode left, TreeNode right) {
      this.left = left;
      this.right = right;
      isLeaf = false;
    }

    TreeNode(int value, String word) {
      this.value = value;
      this.word = word;
    }

    @Override
    public int compareTo(TreeNode t) {
      if (t.value == this.value)
        return 0;
      else
        return (t.value < this.value) ? 1 : -1;
    }

    @Override
    public String toString() {
      return "[leaf: " + isLeaf + "] [has left branch: " + (left != null) + "] [has right branch: "
          + (right != null) + "]";
    }
  }
}
