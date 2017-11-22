package storememorymachine;

import grammar.model.Grammar;
import grammar.model.Rule;

import java.util.Comparator;

public class SimpleStoreMemoryMachine {

    private Grammar grammar;

    public SimpleStoreMemoryMachine(Grammar grammar) {
        this.grammar = grammar;
    }

    public void startRecognition(String stringToRecognition) {
        grammar.getRules().sort((o1, o2) -> {
            int o1N = 0;
            int o2N = 0;
            char[] o1rightChars = o1.getRight().toCharArray();
            char[] o2rightChars = o2.getRight().toCharArray();
            for (char o1rightChar : o1rightChars)
                if (grammar.getN().contains(o1rightChar))
                    o1N++;
            for (char o1rightChar : o2rightChars)
                if (grammar.getN().contains(o1rightChar))
                    o2N++;
            if(o1N == o2N)
                return 0;
            else if (o1N > o2N)
                return 1;
            else return -1;
        });
        recognitionStep(stringToRecognition, grammar.getS());
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
            int i = 0;
            if (grammar.getN().contains(memory.charAt(i))) {
                for (Rule rule : grammar.getRules()) {
                    if (rule.getLeft().charAt(0) == memory.charAt(i)) {
                        StringBuilder newMemoryStateBuilder = new StringBuilder(memory);
                        newMemoryStateBuilder.setCharAt(i, '*');
                        String newMemoryState = newMemoryStateBuilder.toString().replace("*", rule.getRight());
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
}
