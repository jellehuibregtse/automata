import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class AntlrZ3Listener extends Z3BaseListener {
    List<Variable> variables = new ArrayList<>();
    Function currentFunction;

    // Dot language
    private static final String EMPTY_CIRCLE = "\t\"\" [shape=none]";

    private static String getCircle(String name, boolean isFinalState) {
        return "\t\"" + name + "\" [shape=" + (isFinalState ? "double" : "") + "circle]";
    }

    private static String getTransition(String a, String b, String label) {
        return "\t\"" + a + "\" -> \"" + b + "\"" + (label == null ? "" : " [label=" + label + "]");
    }


    // The size of the sudoku.
    private static final int SUDOKU_SIZE = 9;
    @Getter
    private final int[][] sudokuGrid = new int[SUDOKU_SIZE][SUDOKU_SIZE];
    @Getter
    private String out = "";


    public boolean sudokuIsEmpty() {
        int values = 0;
        for (var i = 0; i < SUDOKU_SIZE; i++) {
            for (var j = 0; j < SUDOKU_SIZE; j++) {
                if (sudokuGrid[i][j] != 0) {
                    values++;
                }
            }
        }
        return values == 0;
    }

    public void print() {
        if (!sudokuIsEmpty()) {
            printLn("┌───────┬───────┬───────┐");
            for (var i = 0; i < SUDOKU_SIZE; i++) {
                print("│");
                for (var j = 0; j < SUDOKU_SIZE; j++) {
                    if (j % Math.sqrt(SUDOKU_SIZE) == 0) {
                        print(" ");
                    }
                    print(sudokuGrid[i][j] == 0 ? "  " : sudokuGrid[i][j] + " ");
                    if ((j + 1) % Math.sqrt(SUDOKU_SIZE) == 0) {
                        print("│");
                    }
                }
                if ((i - 2) % 3 == 0 && (i + 1) != SUDOKU_SIZE) {
                    print("\n├───────┼───────┼───────┤");
                }
                print("\n");
            }
            printLn("└───────┴───────┴───────┘");
        } else {
            printLn("}");
            System.out.println(out);
        }
    }

    private void print(String message) {
        out += message;
        System.out.print(message);
    }

    private void printLn(String message) {
        out += message + "\n";
        System.out.println(message);
    }

    @Override
    public void exitValue(Z3Parser.ValueContext ctx) {
        for (var i = 0; i < ctx.declaration().size(); i++) {
            variables.add(new Variable(ctx.declaration(i).variable().getText(), ctx.declaration(i).type(), null));
        }

        currentFunction = new Function(ctx.field().getText(),
                                       (ArrayList<Variable>) variables,
                                       ctx.type(),
                                       ctx.expression());

        if (!ctx.field().LETTER().isEmpty() && ctx.field().LETTER(0).getText().equals("a")) {
            // Sudoku A.
            var x = Integer.parseInt(ctx.field().NUMBER(0).getText()) - 1;
            var y = Integer.parseInt(ctx.field().NUMBER(1).getText()) - 1;
            var value = (int) handleExpression(ctx.expression());

            sudokuGrid[x][y] = value;
        } else if (ctx.field().getText().equals("b")) {
            // Sudoku B.
            for (var x = 0; x < SUDOKU_SIZE; x++) {
                for (var y = 0; y < SUDOKU_SIZE; y++) {
                    variables.get(0).setValue(x + 1);
                    variables.get(1).setValue(y + 1);
                    sudokuGrid[x][y] = (int) handleExpression(ctx.expression());
                }
            }
        } else {
            // NFA
            if (out.isEmpty()) {
                printLn("digraph automaton {");
                printLn("\trankdir=LR;");
                printLn("");
                printLn(EMPTY_CIRCLE);
                for (int i = 0; i <= 7; i++) {
                    printLn(getCircle("100" + i, i == 1 || i == 7));
                }
            }

            if (currentFunction.getName().equals("FinalStates")) {
                var result = (String) handleExpression(ctx.expression());
                assert result != null;
                var states = result.split(":");
                System.out.println("TODO HANDLE FINAL STATES: " + states[0] + ", " + states[1]);
            }

            if (currentFunction.getName().equals("InitState")) {
                var result = handleExpression(ctx.expression());
                assert result != null;
                var initState = result.toString();
                System.out.println(getTransition("", initState, null));
            }

            if (currentFunction.getName().equals("A")) {
                getTransitionFromIte(ctx.expression().ite());
            }
        }

        variables.clear();
    }

    private void getTransitionFromIte(Z3Parser.IteContext ite) {
        if (ite == null) {
            return;
        }

        var and = ite.expression(0).and();
        var a = and.expression(0).comparison().expression(1).number();
        var b = and.expression(1).comparison().expression(1).number();
        var c = and.expression(2).comparison().expression(1);
        // For some reason only getting last char of number, there for the "100" +.
        printLn(getTransition("100" + a.getText(), "100" + b.getText(), c.getText()));

        getTransitionFromIte(ite.expression(2).ite());
    }

    private Object handleExpression(Z3Parser.ExpressionContext expression) {
        if (expression.comparison() != null) {
            return handleComparison(expression.comparison());
        } else if (expression.ite() != null) {
            return handleIte(expression.ite());
        } else if (expression.and() != null) {
            return handleAnd(expression.and());
        } else if (expression.number() != null) {
            return Integer.parseInt(expression.number().getText());
        } else if (expression.STR() != null) {
            return expression.STR().getText().substring(1, expression.STR().getText().length() - 1);
        } else if (expression.bool() != null) {
            return expression.bool().TRUE() != null;
        } else if (expression.variable() != null) {
            return handleVariable(expression.variable());
        }

        return null;
    }

    private Object handleVariable(Z3Parser.VariableContext variable) {
        for (Variable v : variables) {
            if (v.getName().equals(variable.getText()) && v.getValue() != null) {
                return v.getValue();
            }
        }

        return null;
    }

    private boolean handleComparison(Z3Parser.ComparisonContext comparison) {
        if (comparison.equality().EQUALS() != null) {
            Object first = handleExpression(comparison.expression(0));
            for (var i = 1; i < comparison.expression().size(); i++) {
                var value = handleExpression(comparison.expression(i));
                if (first instanceof Boolean && (!(value instanceof Boolean) || (boolean) first != (boolean) value)) {
                    return false;
                } else if (first instanceof Integer && (!(value instanceof Integer) || !(first).equals(value))) {
                    return false;
                } else if (first instanceof String && (!(value instanceof String) || !(first).equals(value))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean handleAnd(Z3Parser.AndContext and) {
        for (var i = 0; i < and.expression().size(); i++) {
            Object result = handleExpression(and.expression(i));
            if (!(result instanceof Boolean) || !(boolean) result) {
                return false;
            }
        }
        return true;
    }

    private Object handleIte(Z3Parser.IteContext ite) {
        var value = handleExpression(ite.expression(0));
        if (value instanceof Boolean && (boolean) value) {
            return handleExpression(ite.expression(1));
        } else {
            return handleExpression(ite.expression(2));
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class Variable {
        private final String name;
        private final Z3Parser.TypeContext type;
        private Object value;

        @Override
        public String toString() {
            return String.format("Name: %s, type: %s, value: %s",
                                 name,
                                 type.getText(),
                                 value == null ? "null" : value.toString());
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class Function {
        private final String name;
        private final ArrayList<Variable> variables;
        private final Z3Parser.TypeContext type;
        private final Z3Parser.ExpressionContext expression;

        @Override
        public String toString() {
            return String.format("Name: %s, type: %s, expression: %s", name, type.getText(), expression.getText());
        }
    }
}
