import finitestatemachine.model.FiniteStateMachine;
import finitestatemachine.util.FSMBuilder;
import finitestatemachine.util.FSMMinimizeUtil;
import finitestatemachine.util.GraphDrawerUtil;
import grammar.model.Grammar;
import storememorymachine.ExtendedStoreMemoryMachine;
import storememorymachine.SimpleStoreMemoryMachine;

public class Main {

    public static void main(String[] args) {
//        String filePath = "rules";
        //1
        //2
        //3
        //4
        String filePath = "rules_lab3";

        Grammar grammar = Grammar.fromFile(filePath);

        FiniteStateMachine finiteStateMachine = FSMBuilder.buildFromGrammar(grammar);

        GraphDrawerUtil.drawGraph(finiteStateMachine);

        FiniteStateMachine minimized = FSMMinimizeUtil.minimize(finiteStateMachine);

        GraphDrawerUtil.drawGraph(minimized);
        //5
        filePath = "rules_5";
        grammar = Grammar.fromFile(filePath);
        ExtendedStoreMemoryMachine simpleStoreMemoryMachine = new ExtendedStoreMemoryMachine(grammar);
        simpleStoreMemoryMachine.startRecognition("add⊥");
    }
}
