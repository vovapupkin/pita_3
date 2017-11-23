package grammar.model;

import grammar.util.StringUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Rule {
    private String left;
    private String right;

    public Rule() {
    }

    public Rule(String left, String right) {
        this.left = left;
        this.right = right;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public Set<Character> getTerminals() {
        Set<Character> terminals = new HashSet<>();

        Set<Character> characters = new HashSet<>(getCharsFromRule());

        for (Character c : characters) {
            if (!Character.isUpperCase(c))
                terminals.add(c);
        }

        return terminals;
    }

    public Set<Character> getNonterminals() {
        Set<Character> terminals = new HashSet<>();

        Set<Character> characters = new HashSet<>(getCharsFromRule());

        for (Character c : characters) {
            if (Character.isUpperCase(c))
                terminals.add(c);
        }

        return terminals;
    }

    private List<Character> getCharsFromRule() {
        List<Character> characters = new ArrayList<>();
        characters.addAll(StringUtil.splitToChars(getLeft()));
        characters.addAll(StringUtil.splitToChars(getRight()));
        return characters;
    }

    public static Set<Character> getTerminals(List<Rule> rules) {
        Set<Character> terminals = new HashSet<>();

        for (Rule rule : rules)
            terminals.addAll(rule.getTerminals());

        return terminals;
    }

    public static Set<Character> getNonterminals(List<Rule> rules) {
        Set<Character> terminals = new HashSet<>();

        for (Rule rule : rules)
            terminals.addAll(rule.getNonterminals());

        return terminals;
    }

    @Override
    public String toString() {
        return "{" + left + "->" + right + "}";
    }

    @Override
    public int hashCode() {
        int result = left.hashCode();
        result = 31 * result + right.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rule rule = (Rule) o;

        if (left != null ? !left.equals(rule.left) : rule.left != null) return false;
        return right != null ? right.equals(rule.right) : rule.right == null;
    }
}
