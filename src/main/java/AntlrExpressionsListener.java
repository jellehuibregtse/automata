import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Stack;

public class AntlrExpressionsListener extends ExpressionsBaseListener {
    private final Stack<Integer> stack = new Stack<>();
    private int result;

    @Override
    public void exitNumber(ExpressionsParser.NumberContext ctx) {
        var i = Integer.parseInt(ctx.NUMBER().getText());
        stack.push(i);
    }

    @Override
    public void exitExpression(ExpressionsParser.ExpressionContext ctx) {
        result = stack.pop();
        System.out.println("Result: " + result);
    }

    @Override
    public void exitMult(ExpressionsParser.MultContext ctx) {
        int a = stack.pop();
        int b = stack.pop();

        String operation = ctx.getChild(1).getText();

        if (operation.equals("*")) {
            stack.push(a * b);
        } else {
            stack.push(a / b);
        }
    }

    @Override
    public void exitAdd(ExpressionsParser.AddContext ctx) {
        int a = stack.pop();
        int b = stack.pop();

        String operation = ctx.getChild(1).getText();

        if (operation.equals("+")) {
            stack.push(a + b);
        } else {
            stack.push(a - b);
        }
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        System.out.println("Terminal node: " + node.getText());
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        System.out.println("Rule: " + ctx.getText());
    }

    public int getResult() {
        return result;
    }
}
