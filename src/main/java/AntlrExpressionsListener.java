import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Stack;

public class AntlrExpressionsListener extends ExpressionsBaseListener {
    private final Stack<Variable> stack = new Stack<>();
    private Integer result;
    private boolean printSteps;

    AntlrExpressionsListener(boolean printSteps) {
        this.printSteps = printSteps;
    }

    @Override
    public void exitExpression(ExpressionsParser.ExpressionContext ctx) {
        if (result == null) {
            result = stack.pop().value;
            System.out.println("Result: " + result);
        }
    }

    @Override
    public void exitReturn(ExpressionsParser.ReturnContext ctx) {
        if (result == null) {
            if (ctx.TEXT() != null) {
                stack.forEach(var -> {
                    if (var.name.equals(ctx.TEXT().getText())) {
                        result = var.value;
                    }
                });
                if (result == null) {
                    throw new NullPointerException("Cannot resolve '" + ctx.TEXT().getText() + "'");
                }
            } else {
                result = stack.pop().value;
            }
            System.out.println("Result: " + result);
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
        if (stack.stream().anyMatch((var) -> var.name != null && var.name.equals(ctx.TEXT().getText())))
            throw new IllegalArgumentException("Variable '" + ctx.TEXT().getText() + "' is already defined in the scope.");
        stack.push(new Variable(ctx.TEXT().getText(), stack.pop().value));
    }

    @Override
    public void exitPrint(ExpressionsParser.PrintContext ctx) {
        if (ctx.TEXT() != null) {
            Variable variable = stack.stream().filter((var) -> var.name.equals(ctx.TEXT().getText())).findFirst().orElse(null);
            if(variable == null)
                throw new NullPointerException("Cannot resolve '" + ctx.TEXT().getText() + "'");
            System.out.println("Out: " + variable.value);
        } else
            System.out.println("Out: " + stack.pop().value);
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        if (printSteps)
            System.out.println("Terminal node: " + node.getText());
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        if (printSteps)
            System.out.println("Rule: " + ctx.getText());
    }

    public int getResult() {
        return result;
    }

    private class Variable {
        String name;
        Integer value;


        public Variable(String name, Integer value) {
            this.name = name;
            this.value = value;
        }
    }
}