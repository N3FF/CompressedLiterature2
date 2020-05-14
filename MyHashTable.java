// Justin Neff - TCSS 342 Compressed Literature 2

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MyHashTable<K, V> {

  private int capacity = 0;
  private int size = 0;
  private int maxProbes = 0;
  private int[] probes = new int[1 << 15];
  private V[] values;
  private K[] entries;

  MyHashTable(int capacity) {
    this.capacity = capacity;
    values = (V[]) new Object[capacity];
    entries = (K[]) new Object[capacity];
    for (int i = 0; i < probes.length; i++) {
      probes[i] = 0;
    }
  }

  public void put(K searchKey, V newValue) {
    int hash = hash(searchKey);
    int probe = 0;
    for (int i = hash; i < capacity; i = (i + 1) % capacity) {
      if (Objects.isNull(entries[i])) {
        entries[i] = searchKey;
        values[i] = newValue;
        size++;
        probes[probe]++;
        maxProbes = (probe > maxProbes) ? probe : maxProbes;
        break;
      } else if (entries[i].equals(searchKey)) {
        values[i] = newValue;
        break;
      } else {
        probe++;
      }
    }
  }

  public V get(K searchKey) {
    int hash = hash(searchKey);
    for (int i = hash; i < capacity; i = (i + 1) % capacity) {
      if (Objects.isNull(entries[i]) || entries[i].equals(searchKey)) {
        return (V) values[i];
      }
    }
    return null;
  }


  public Set<K> keySet() {
    K[] keys = (K[]) new Object[size];
    for (int i = 0, j = 0; i < entries.length; i++) {
      if (Objects.nonNull(entries[i])) {
        keys[j] = entries[i];
        j++;
      }
    }
    return new HashSet<K>(Arrays.asList(keys));
  }

  public boolean containsKey(K searchKey) {
    int hash = hash(searchKey);
    for (int i = hash; i < capacity; i = (i + 1) % capacity) {
      if (Objects.isNull(entries[i])) {
        return false;
      } else if (entries[i].equals(searchKey)) {
        return true;
      }
    }
    return false;
  }

  public void stats() {
    // StringBuilder sb = new StringBuilder();
    System.out.println("Hash Table Stats");
    System.out.println("================");
    System.out.println("Number of Entries: " + size);
    System.out.println("Number of Buckets: " + capacity);
    System.out.println("Histogram of Probes: ");
    System.out.print("[");
    for (int i = 0; i < maxProbes; i++) {
      System.out.print(probes[i] + ", ");
      if (i % 20 == 12) {
        System.out.println("");
      }
    }
    System.out.print(probes[maxProbes] + "]\n");
    System.out.println("Fill Percentage: " + ((float) size / capacity) * 100 + "%");
    System.out.println("Max Linear Prob: " + maxProbes);
    float avg = 0;
    for (int i = 0; i <= maxProbes; i++) {
      avg += probes[i] * i;
    }
    System.out.println("Average Linear Prob: " + avg / size);
  }

  private int hash(K key) {
    return Math.abs(key.hashCode() % capacity);
  }

  public int size() {
    return size;
  }

  public String toString() {
    Set<K> set = keySet();
    StringBuilder sb = new StringBuilder();
    sb.append('{');
    for (K s : set) {
      sb.append(s);
      sb.append('=');
      sb.append(get(s));
      sb.append(", ");
    }
    sb.setLength(sb.length() - 2);
    sb.append('}');
    return sb.toString();
  }
}
