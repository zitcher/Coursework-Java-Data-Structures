package portfolio;

import java.util.List;
import java.util.ArrayList;

import com.google.common.base.Objects;

/**
 * Represents a trie a.k.a a prefix tree of strings.
 *
 * @author Zachary Hoffman
 */
public class TrieNode {
  private TrieNode parent;
  private List<TrieNode> children;
  private String value;

  /** Constructs a Trie.
   * @param parent The parent of the current trie in its tree.
   * @param children A list of TrieNodes representing the Nodes in a
   * Trie that this node is linked to.
   * @param value A string represeting what this TrieNode holds.
   */
  public TrieNode(TrieNode parent, List<TrieNode> children, String value) {
    this.parent = parent;
    this.children = new ArrayList<>(children);
    this.value = value;
  }

  /**
    * Returns the node's parent.
    * @return The parent of the node the method is called on.
    */
  public TrieNode getParent() {
    return parent;
  }

  /**
    * Returns a list of the node's children.
    * @return A list containing chilren of the node the method is called on.
    */
  public List<TrieNode> getChildren() {
    return new ArrayList<>(children);
  }

  /**
    * Returns the node's value.
    * @return The value of the node the method is called on.
    */
  public String getValue() {
    return value;
  }

  /**
    * Adds a TrieNode of the same type to the current TrieNode's child list.
    * @param child The node to add.
    * @return The added node.
    */
  public TrieNode addChild(TrieNode child) {
    children.add(child);
    return child;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }

  //how to check for equality
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof TrieNode)) {
      return false;
    }

    TrieNode aTrieNode = (TrieNode) obj;

    //Checks if value matches
    return aTrieNode.getValue().equals(this.getValue());
  }

  //Return's a string describing the trie node's value
  @Override
  public String toString() {
    return value;
  }
}
