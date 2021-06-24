import java.util.ArrayList;
import java.util.List;

public class AntlrMockExamListener extends MockExamBaseListener {
    private final StringBuilder output = new StringBuilder();
    private final List<String> variables = new ArrayList<>();

    public String getOutput() {
        return output.toString();
    }

    @Override
    public void exitDeclare_function(MockExamParser.Declare_functionContext ctx) {
        output.append("FUNCTION: ")
                .append(ctx.variable().getText())
                .append(" ")
                .append("#par=")
                .append(ctx.type().size() - 1)
                .append("\n");
    }

    @Override
    public void enterDefine_constant(MockExamParser.Define_constantContext ctx) {
        output.append("INTEGER: ")
                .append("'")
                .append(ctx.variable().getText())
                .append("' = ")
                .append(ctx.value().getText())
                .append("\n");
    }

    @Override
    public void exitDefine_constant(MockExamParser.Define_constantContext ctx) {
        var variable = ctx.variable().getText();

        if (variables.contains(variable)) {
            output.append("*** ERROR in define-const: variable '")
                    .append(variable)
                    .append("' redefined ***")
                    .append("\n");
        } else {
            variables.add(variable);
        }
    }

    @Override
    public void enterAssertion(MockExamParser.AssertionContext ctx) {
        output.append("ASSERT: ").append("(");
    }

    @Override
    public void exitAssertion(MockExamParser.AssertionContext ctx) {
        output.append(")\n");
    }

    @Override
    public void enterAnd(MockExamParser.AndContext ctx) {
        for (var i = 0; i < ctx.expression().size(); i++) {
            var expression = ctx.expression(i);

            output.append(i != 0 ? " && " : "")
                    .append("(")
                    .append(expression.comparison().expression(0).variable().getText())
                    .append(" ")
                    .append(expression.comparison().equality().getText())
                    .append(" ")
                    .append(expression.comparison().expression(1).variable().getText())
                    .append(")");
        }
    }
}
