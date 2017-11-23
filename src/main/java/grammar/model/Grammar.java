package grammar.model;

import grammar.util.FileUtil;
import grammar.util.RuleParser;
import grammar.util.StringUtil;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Grammar {
    private String S;
    private Set<Rule> rules;
    private Set<Character> T;
    private Set<Character> N;

    public Grammar(Set<Rule> rules, Set<Character> T, Set<Character> N) {
        this.rules = rules;
        this.T = T;
        this.N = N;
        this.S = "S";
    }

    public Set<Rule> getRules() {
        return rules;
    }

    public void setRules(Set<Rule> rules) {
        this.rules = rules;
    }

    public Set<Character> getT() {
        return T;
    }

    public void setT(Set<Character> t) {
        T = t;
    }

    public Set<Character> getN() {
        return N;
    }

    public void setN(Set<Character> n) {
        N = n;
    }

    public String getS() {
        return S;
    }

    public void setS(String s) {
        S = s;
    }

    public GrammarType getType() {
        if (!isType1()) return GrammarType.TYPE_0;
        if (!isType2()) return GrammarType.TYPE_1;
        if (!isType3()) return GrammarType.TYPE_2;
        return GrammarType.TYPE_3;
    }

    private boolean isType1() {
        for (Rule rule : getRules()) {
            if (rule.getLeft().length() > rule.getRight().length())
                return false;
        }

        return true;
    }

    private boolean isType2() {
        for (Rule rule : getRules()) {
            List<Character> leftChars = StringUtil.splitToChars(rule.getLeft());
            if (leftChars.size() > 1)
                return false;
            if (getT().contains(leftChars.get(0))) {
                return false;
            }
        }

        return true;
    }

    // for state machines
    private boolean isType3() {
        for (Rule rule : getRules()) {
            List<Character> rightChars = StringUtil.splitToChars(rule.getRight());
            if (rightChars.size() > 2)
                return false;
            int nonterminalsCount = 0;
            for (Character rightChar : rightChars)
                if (getN().contains(rightChar))
                    nonterminalsCount++;

            if (nonterminalsCount != 0) {
                if (rightChars.size() == 1)
                    return false;
                if (nonterminalsCount > 1)
                    return false;
            }
        }
        return true;
    }

    public static Grammar fromFile(String filename) {
        List<String> rulesStrings;

        try {
            rulesStrings = FileUtil.readFile(filename);
        } catch (FileNotFoundException e) {
            System.out.println("Wrong file name");
            throw new IllegalArgumentException("Wrong file name");
        }

        List<Rule> rules = RuleParser.parseAll(rulesStrings);

        Set<Character> terminals = Rule.getTerminals(rules);
        Set<Character> nonTerminals = Rule.getNonterminals(rules);

        return new Grammar(new HashSet<>(rules), terminals, nonTerminals);
    }


}
