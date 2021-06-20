public class AntlrMockExamListener extends MockExamBaseListener {
    @Override
    public void exitDeclare_function(MockExamParser.Declare_functionContext ctx) {
        System.out.println(ctx.getText());
    }
}
