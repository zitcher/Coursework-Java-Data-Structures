package portfolio;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;

/**
 * Represents a trie a.k.a a prefix tree of strings.
 *
 * @author Zachary Hoffman
 */
public class Trie {
  private TrieNode root;
  private HashMap<String, Integer> wordHash;

  /**
   * Initializes the trie with a list of words to fill it with.
   * @param words The words to fill the trie with.
   */
  public Trie(List<String> words) {
    wordHash = new HashMap<String, Integer>();
    root = new TrieNode(null, new ArrayList<>(), "");
    addToTrie(words);
  }

  /**
   * Adds all words in the given list to the tree.
   * @param words A list of words to add to the tree.
   */
  public void addToTrie(List<String> words) {
    for (String word : words) {
      //add the word to the map
      if (wordHash.containsKey(word)) {
        wordHash.put(word, wordHash.get(word) + 1);
      } else {
        wordHash.put(word, 1);
      }

      //then add it to the tree
      add(word, root);
    }
  }

  //recursivly adds a word to the prefix tree.
  private void add(String word, TrieNode node) {
    //make sure to not add empty strings
    if (!word.equals("")) {
      //otherwise grab the first letter and search the nodes children to
      //see if one matches it
      String firstLetter =  word.substring(0, 1);
      String restOfWord = word.substring(1);
      for (TrieNode child : node.getChildren()) {
        //if we find a matching child add it and recur down that child.
        if (child.getValue().equals(firstLetter)) {
          add(restOfWord, child);
          return; //end the function because we finished adding!
        }
      }

      //if first letter not found in children, add it and recur down tree.
      add(restOfWord,
          node.addChild(new TrieNode(node, new ArrayList<>(), firstLetter)));
    }
  }

  /**
   * Finds all words that begin with the given word.
   * @param prefix Word to search with.
   * @return A list of words that begin with prefix.
   */
  public List<String> potentialWords(String prefix) {
    String[] prefixArray = prefix.split("");
    List<String> mutablePrefixList =
        new ArrayList<>(Arrays.asList(prefixArray));

    if (!prefix.equals("")) {
      return potentialWordsHelper(mutablePrefixList, root, prefix);
    } else {
      return potentialWordsHelper(new ArrayList<>(), root, prefix);
    }
  }

  private List<String> potentialWordsHelper(List<String> sList,
                                            TrieNode node,
                                            String prefix) {
    //when sList is empty, that means we are at the node in the tree that
    //represents the final letter of prefix.
    if (sList.size() <= 0) {
      List<String> words = new ArrayList<>();
      toList(node, words, prefix);
      return words;
    } else {
      //otherwise recur through the tree to find the node or the final letter
      //of the given prefix.
      String firstLetter = sList.remove(0);
      for (TrieNode child : node.getChildren()) {
        if (child.getValue().equals(firstLetter)) {
          return potentialWordsHelper(sList, child, prefix);
        }
      }
    }

    //otherwise the are no suffixes so
    return new ArrayList<>();
  }

  //adds all all words that start with prefix and are children of node
  // to the list of words.
  private void toList(TrieNode node,
                      List<String> words,
                      String prefix) {
    //first check if the current prefix is a word
    if (wordHash.containsKey(prefix)) {
      words.add(prefix);
    }

    //loop through children
    for (TrieNode child : node.getChildren()) {
      toList(child, words, prefix + child.getValue());
    }
  }

  /** Retrurns the hash of all the words this trie holds.
    @return The hash of all the words this trie holds.
  */
  public HashMap<String, Integer> getWordHash() {
    return wordHash;
  }
}
