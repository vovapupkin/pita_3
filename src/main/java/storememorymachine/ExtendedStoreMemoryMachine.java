package storememorymachine;

import grammar.model.Grammar;

public class ExtendedStoreMemoryMachine {

    private Grammar grammar;

    public ExtendedStoreMemoryMachine(Grammar grammar) {
        this.grammar = grammar;
    }

    public void startRecognition(String stringToRecognition) {
        recognitionStep(stringToRecognition, "#");
    }

    private boolean recognitionStep(String stringToRecognition, String memory) {
        if (stringToRecognition.isEmpty() && memory.isEmpty()) {
            printStep(stringToRecognition, memory);
            return true;
        } else if (!stringToRecognition.isEmpty() && memory.isEmpty()) {
            return false;
        } else if(stringToRecognition.isEmpty() && memory.equals("#" + grammar.getS())) {
            if(recognitionStep(stringToRecognition, "")) {
                printStep(stringToRecognition, memory);
                return true;
            } else
                return false;
        } else {
            for (int i = 0; i < grammar.getRules().size(); i++) {
                String ruleRight = grammar.getRules().get(i).getRight();
                int indexOfRule = memory.indexOf(ruleRight);
                if(indexOfRule == memory.length() - ruleRight.length() && indexOfRule >= 0) {
                    if( recognitionStep(stringToRecognition, memory.substring(0, memory.length() - ruleRight.length()) + grammar.getRules().get(i).getLeft())) {
                        printStep(stringToRecognition, memory);
                        return true;
                    } else {
                        return false;
                    }
                }
            }
            if(recognitionStep(stringToRecognition.substring(1),
                    memory + String.valueOf(stringToRecognition.charAt(0)))) {
                printStep(stringToRecognition, memory);
                return true;
            } else
                return false;
        }
    }

    private void printStep(String stringToRecognition, String memory) {
        System.out.println(stringToRecognition + ":" + memory);
    }

}
