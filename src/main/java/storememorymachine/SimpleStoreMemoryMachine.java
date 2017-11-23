package storememorymachine;

import com.sun.istack.internal.Nullable;
import finitestatemachine.util.RandomCharacterUtil;
import grammar.model.Grammar;
import grammar.model.Rule;
import javafx.util.Pair;

import java.util.HashSet;
import java.util.Set;

public class SimpleStoreMemoryMachine {

    private Grammar grammar;

    private static final char UNIQUE_SYMBOL = '±';

    public SimpleStoreMemoryMachine(Grammar grammar) {
        this.grammar = grammar;
        this.grammar = grammar;
        System.out.println("Original grammar");
        for (Rule rule : grammar.getRules())
            System.out.println(rule);
        System.out.println("Removing left lrec");
        removeLeftRecursive(grammar);
        System.out.println("Grammar without lrec");
        for (Rule rule : grammar.getRules())
            System.out.println(rule);
    }

    public void startRecognition(String stringToRecognition) {
        System.out.println("Start recognition");
        if (!recognitionStep(stringToRecognition, grammar.getS())) {
            System.out.println("Can't recognize");
        }
    }

    private boolean recognitionStep(String stringToRecognition, String memory) {
        if (stringToRecognition.isEmpty() && memory.isEmpty()) {
            printStep(stringToRecognition, memory);
            return true;
        } else if (stringToRecognition.isEmpty() != memory.isEmpty()) {
            return false;
        } else if (stringToRecognition.charAt(0) == memory.charAt(0)) {
            if (recognitionStep(stringToRecognition.substring(1), memory.substring(1))) {
                printStep(stringToRecognition, memory);
                return true;
            } else {
                return false;
            }
        } else {
            if (grammar.getN().contains(memory.charAt(0))) {
                for (Rule rule : grammar.getRules()) {
                    if (rule.getLeft().charAt(0) == memory.charAt(0)) {
                        String newMemoryState = rule.getRight() + memory.substring(1);
                        if (recognitionStep(stringToRecognition, newMemoryState)) {
                            printStep(stringToRecognition, memory);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private void printStep(String stringToRecognition, String memory) {
        System.out.println(stringToRecognition + ":" + memory);
    }

    private void removeLeftRecursive(Grammar grammar) {
        Set<Rule> rules = grammar.getRules();
        while (true) {
            Character recursiveNonterminal = findRecursiveNonterminal(grammar);
            if (recursiveNonterminal != null) {
                Pair<Set<Rule>, Set<Rule>> r_norRules = findRecursiveNorecursiveRules(grammar.getRules(), recursiveNonterminal);
                Character newNonterminal = RandomCharacterUtil.getRandom(grammar.getN());
                grammar.getN().add(newNonterminal);
                System.out.println("Adding new nonterminal: " + newNonterminal);
                for (Rule rule : r_norRules.getKey()) {
                    rules.add(new Rule(String.valueOf(newNonterminal),
                            rule.getRight().substring(1) + String.valueOf(newNonterminal)));
                    rules.add(new Rule(String.valueOf(newNonterminal),
                            rule.getRight().substring(1)));
                    rules.remove(rule);
                }
                for (Rule rule : r_norRules.getValue()) {
                    rules.add(new Rule(rule.getLeft(),
                            rule.getRight() + String.valueOf(newNonterminal)));
                }
            } else {
                return;
            }
        }
    }

    @Nullable
    private Character findRecursiveNonterminal(Grammar grammar) {
        for (Character nonterminal : grammar.getN()) {
            for (Rule rule : grammar.getRules()) {
                if (rule.getRight().indexOf(nonterminal) == 0 && rule.getLeft().indexOf(nonterminal) == 0)
                    return nonterminal;
            }
        }
        return null;
    }

    private Pair<Set<Rule>, Set<Rule>> findRecursiveNorecursiveRules(Set<Rule> rules, Character nonterminal) {
        Pair<Set<Rule>, Set<Rule>> result = new Pair<>(new HashSet<>(), new HashSet<>());
        for (Rule rule : grammar.getRules()) {
            if (rule.getLeft().indexOf(nonterminal) == 0) {
                if (rule.getRight().indexOf(nonterminal) == 0) {
                    result.getKey().add(rule);
                } else {
                    result.getValue().add(rule);
                }
            }

        }
        return result;
    }
}
