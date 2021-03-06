import lombok.Getter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayDeque;
import java.util.Deque;

public class AntlrExpressionsListener extends ExpressionsBaseListener {
    private final Deque<Variable> stack = new ArrayDeque<>();
    private final boolean printSteps;
    @Getter private Integer result;
    @Getter private String out = "";

    AntlrExpressionsListener(boolean printSteps) {
        this.printSteps = printSteps;
    }

    @Override
    public void exitExpression(ExpressionsParser.ExpressionContext ctx) {
        if (result == null) {
            result = stack.pop().value;
            printValue("Result: " + result);
        }
    }

    @Override
    public void exitReturn(ExpressionsParser.ReturnContext ctx) {
        if (result == null) {
            if (ctx.VALUE() != null) {
                stack.forEach(v -> {
                    if (v.name.equals(ctx.VALUE().getText())) {
                        result = v.value;
                    }
                });
                if (result == null) {
                    throw new NullPointerException("Cannot resolve '" + ctx.VALUE().getText() + "'");
                }
            } else {
                result = stack.pop().value;
            }
            printValue("Result: " + result);
        }
    }

    @Override
    public void exitNumber(ExpressionsParser.NumberContext ctx) {
        var i = Integer.parseInt(ctx.NUMBER().getText());
        stack.push(new Variable(null, i));
    }

    @Override
    public void exitMult(ExpressionsParser.MultContext ctx) {
        int a = stack.pop().value;
        int b = stack.pop().value;

        String operation = ctx.children.stream().filter(t -> t.getText().equals("*") || t.getText().equals("/")).findFirst().orElseThrow(NullPointerException::new).getText();

        if (operation.equals("*")) {
            stack.push(new Variable(null, a * b));
        } else {
            stack.push(new Variable(null, a / b));
        }
    }

    @Override
    public void exitAdd(ExpressionsParser.AddContext ctx) {
        int a = stack.pop().value;
        int b = stack.pop().value;

        String operation = ctx.children.stream().filter(t -> t.getText().equals("+") || t.getText().equals("-")).findFirst().orElseThrow(NullPointerException::new).getText();

        if (operation.equals("+")) {
            stack.push(new Variable(null, a + b));
        } else {
            stack.push(new Variable(null, a - b));
        }
    }

    @Override
    public void exitVar(ExpressionsParser.VarContext ctx) {
        if (ctx.children.get(0).getText().equals("var")) {
            if (stack.stream().anyMatch(v -> v.name != null && v.name.equals(ctx.VALUE().getText()))) {
                throw new IllegalArgumentException("Variable '" + ctx.VALUE().getText() + "' is already defined in the scope.");
            }
            stack.push(new Variable(ctx.VALUE().getText(), stack.pop().value));
        } else {
            Variable variable = stack.stream().filter(v -> v.name != null && v.name.equals(ctx.VALUE().getText())).findFirst().orElseThrow(() -> new NullPointerException("Cannot resolve '" + ctx.VALUE().getText() + "'"));
            variable.value = stack.pop().value;
        }
    }

    @Override
    public void exitPrint(ExpressionsParser.PrintContext ctx) {
        if (ctx.VALUE() != null) {
            var variable = stack.stream().filter(v -> v.name.equals(ctx.VALUE().getText())).findFirst().orElse(null);
            if (variable == null) {
                throw new NullPointerException("Cannot resolve '" + ctx.VALUE().getText() + "'");
            }
            printValue("Out: " + variable.value);
        } else {
            printValue("Out: " + stack.pop().value);
        }
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        if (printSteps) {
            printValue("Terminal node: " + node.getText());
        }
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        if (printSteps) {
            printValue("Rule: " + ctx.getText());
        }
    }

    private void printValue(String message) {
        out += (!out.equals("")? "\n" : "") + message;
        System.out.println(message);
    }

    private static class Variable {
        String name;
        Integer value;


        public Variable(String name, Integer value) {
            this.name = name;
            this.value = value;
        }
    }
}