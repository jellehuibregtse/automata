import lombok.Getter;

public class AntlrMockExamListener extends MockExamBaseListener {
    private final StringBuilder output = new StringBuilder();

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
    public void exitDefine_constant(MockExamParser.Define_constantContext ctx) {
        output.append("INTEGER: ")
                .append("'")
                .append(ctx.variable().getText())
                .append("' = ")
                .append(ctx.value().getText())
                .append("\n");
    }

    @Override
    public void enterAssertion(MockExamParser.AssertionContext ctx) {
        output.append("ASSERT: ").append("(");
    }

    @Override
    public void exitAssertion(MockExamParser.AssertionContext ctx) {
        output.append(")\n");
    }
}
