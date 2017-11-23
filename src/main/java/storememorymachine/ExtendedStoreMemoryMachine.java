package storememorymachine;

import com.sun.istack.internal.Nullable;
import grammar.model.Grammar;
import grammar.model.GrammarType;
import grammar.model.Rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ExtendedStoreMemoryMachine {

    private static final char UNIQUE_SYMBOL = '±';

    private Grammar grammar;

    public ExtendedStoreMemoryMachine(Grammar grammar) {
        if (grammar.getType().ordinal() <= GrammarType.TYPE_2.ordinal())
            throw new IllegalArgumentException("Grammar is not " + GrammarType.TYPE_2.getType());
        this.grammar = grammar;
        System.out.println("Original grammar");
        for (Rule rule : grammar.getRules())
            System.out.println(rule);
        System.out.println("Removing epsilon");
        deleteEpsilonRules(grammar);
        System.out.println("Grammar without epsilon");
        for (Rule rule : grammar.getRules())
            System.out.println(rule);
    }

    public void startRecognition(String stringToRecognition) {
        System.out.println("Start recognition");
        if (!recognitionStep(stringToRecognition, String.valueOf(UNIQUE_SYMBOL))) {
            System.out.println("Can't recognize");
        }
    }

    private boolean recognitionStep(String stringToRecognition, String memory) {
        if (stringToRecognition.isEmpty() && memory.isEmpty()) {
            printStep(stringToRecognition, memory);
            return true;
        } else if (!stringToRecognition.isEmpty() && memory.isEmpty()) {
            return false;
        } else if (stringToRecognition.isEmpty() && memory.equals(String.valueOf(UNIQUE_SYMBOL) + grammar.getS())) {
            if (recognitionStep(stringToRecognition, "")) {
                printStep(stringToRecognition, memory);
                return true;
            } else
                return false;
        } else {
            for (Rule rule : grammar.getRules()) {
                String ruleRight = rule.getRight();
                if (memory.contains(ruleRight) &&
                        memory.lastIndexOf(ruleRight) == memory.length() - ruleRight.length()) {
                    try {
                        if (recognitionStep(stringToRecognition, memory.substring(0, memory.length() - ruleRight.length()) + rule.getLeft())) {
                            printStep(stringToRecognition, memory);
                            return true;
                        }
                    } catch (StackOverflowError ignore) {
                    }
                }
            }
            if (stringToRecognition.isEmpty())
                return false;
            if (recognitionStep(stringToRecognition.substring(1), memory + stringToRecognition.substring(0, 1))) {
                printStep(stringToRecognition, memory);
                return true;
            } else
                return false;
        }
    }

    private void printStep(String stringToRecognition, String memory) {
        System.out.println("( " + stringToRecognition + " )" + " : " + "( " + memory + " )");
    }

    private void deleteEpsilonRules(Grammar grammar) {
        Set<Rule> rules = grammar.getRules();
        while (true) {
            Rule ruleWithEpsilon = findRuleWithEpsilon(rules);
            if (ruleWithEpsilon != null) {
                rules.remove(ruleWithEpsilon);
                List<Rule> newRules = new ArrayList<>();
                Character nonterminalWithEpsilon = ruleWithEpsilon.getLeft().charAt(0);
                for (Rule rule : rules) {
                    if (rule.getRight().indexOf(nonterminalWithEpsilon) != -1) {
                        int nonterminalsInRule = getCharactersCount(rule.getRight(), nonterminalWithEpsilon);
                        for (int i = 1; i <= nonterminalsInRule; i++) {
                            for (int j = 0; j <= nonterminalsInRule - i; j++) {
                                Rule newRule = new Rule(rule.getLeft(), removeCharacters(rule.getRight(), nonterminalWithEpsilon, j, i));
                                newRules.add(newRule);
                                System.out.println("New Rule: " + newRule);
                            }
                        }
                    }
                }
                rules.addAll(newRules);
            } else {
                break;
            }
        }
    }

    @Nullable
    private Rule findRuleWithEpsilon(Set<Rule> rules) {
        Rule ruleWithEpsilon = null;
        for (Rule rule : rules) {
            if (rule.getRight().isEmpty()) {
                ruleWithEpsilon = rule;
                System.out.println("Rule with epsilon: " + rule);
                break;
            }
        }
        return ruleWithEpsilon;
    }

    private String removeCharacters(String source, char remove, int position, int count) {
        StringBuilder resultBuilder = new StringBuilder(source);
        int i;
        for (i = 0; position > 0; i++) {
            if (resultBuilder.charAt(i) == remove)
                position--;
        }
        for (; count > 0; i++) {
            if (resultBuilder.charAt(i) == remove) {
                resultBuilder.deleteCharAt(i);
                count--;
            }
        }
        return resultBuilder.toString();
    }

    private int getCharactersCount(String source, char c) {
        int result = 0;
        for (int i = 0; i < source.length(); i++) {
            if (source.charAt(i) == c)
                result++;
        }
        return result;
    }
}
