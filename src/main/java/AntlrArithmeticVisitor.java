import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.LongStream;

public class AntlrArithmeticVisitor extends ArithmeticBaseVisitor<AntlrArithmeticVisitor.Variable> {
    private final Map<String, Variable> variables = new HashMap<>();
    private final Map<String, Variable> local_variables = new HashMap<>();
    private final Map<String, ArithmeticParser.Function_definitionContext> functions = new HashMap<>();
    @Getter
    private String out = "";

    @Override
    public Variable visitAssignment(ArithmeticParser.AssignmentContext ctx) {
        String id = ctx.VALUE().getText();
        Variable value = null;
        if (ctx.children.get(1).getText().equals("+=")) {
            Variable old = local_variables.get(id);
            if (old == null)
                old = variables.get(id);
            if (old == null) {
                new TypeMismatchException("There is no variable with name: " + id).printStackTrace();
            } else {
                try {
                    Variable result = new Variable();
                    result.concatVariables(this.visit(ctx.expression()), old);
                    value = result;
                } catch (TypeMismatchException e) {
                    e.printStackTrace();
                }
            }
        } else if (ctx.children.get(1).getText().equals("-=")) {
            Variable old = local_variables.get(id);
            if (old == null)
                old = variables.get(id);
            if (old == null) {
                new TypeMismatchException("There is no variable with name: " + id).printStackTrace();
            } else {
                value = new Variable(old.getNumber() - this.visit(ctx.expression()).getNumber());
            }
        } else if (ctx.children.stream().anyMatch(e -> e.getText().equals("="))) {
            value = this.visit(ctx.expression());
        }
        if (local_variables.get(id) != null)
            return local_variables.put(id, value);
        return variables.put(id, value);
    }

    @Override
    public Variable visitPrint(ArithmeticParser.PrintContext ctx) {
        var variable = this.visit(ctx.expression());

        out += (!out.equals("") ? "\n" : "") + variable.getValue();
        System.out.println(variable.getValue());
        return variable;
    }

    @Override
    public Variable visitValueExpression(ArithmeticParser.ValueExpressionContext ctx) {
        // Retrieve value of variable from memory.
        String name = ctx.getText();
        Variable value = local_variables.get(name);
        if (value == null)
            value = variables.get(name);

        if (value == null) {
            new TypeMismatchException("There is no variable with name: " + name).printStackTrace();
        }

        return value;
    }

    @Override
    public Variable visitNumberExpression(ArithmeticParser.NumberExpressionContext ctx) {
        return new Variable(Integer.parseInt(ctx.getText()));
    }

    @Override
    public Variable visitStringExpression(ArithmeticParser.StringExpressionContext ctx) {
        return new Variable(ctx.getText().substring(1, ctx.getText().length() - 1));
    }

    @Override
    public Variable visitBooleanExpression(ArithmeticParser.BooleanExpressionContext ctx) {
        return new Variable(ctx.getText().equals("true"));
    }

    @Override
    public Variable visitPowerExpression(ArithmeticParser.PowerExpressionContext ctx) {
        var left = this.visit(ctx.expression(0)).getNumber();
        var right = this.visit(ctx.expression(1)).getNumber();
        return new Variable((int) Math.pow(left, right));
    }

    @Override
    public Variable visitModulusExpression(ArithmeticParser.ModulusExpressionContext ctx) {
        var left = this.visit(ctx.expression(0)).getNumber();
        var right = this.visit(ctx.expression(1)).getNumber();
        return new Variable(left % right);
    }

    @Override
    public Variable visitMultiplicationExpression(ArithmeticParser.MultiplicationExpressionContext ctx) {
        var left = this.visit(ctx.expression(0)).getNumber();
        var right = this.visit(ctx.expression(1)).getNumber();

        // Operation
        if (ctx.getChild(1).getText().equals("*")) {
            return new Variable(left * right);
        } else {
            return new Variable(left / right);
        }
    }

    @Override
    public Variable visitAdditionExpression(ArithmeticParser.AdditionExpressionContext ctx) {
        Variable left = this.visit(ctx.expression(0));
        Variable right = this.visit(ctx.expression(1));
        var result = new Variable();

        // Operation
        if (ctx.getChild(1).getText().equals("+")) {
            try {
                result.concatVariables(left, right);
            } catch (TypeMismatchException e) {
                e.printStackTrace();
            }
        } else {
            Integer subtract = left.getNumber() - right.getNumber();
            result.setNumber(subtract);
        }

        return result;
    }

    @Override
    public Variable visitFactorialExpression(ArithmeticParser.FactorialExpressionContext ctx) {
        Variable number = this.visit(ctx.expression());
        number.setNumber((int) LongStream.rangeClosed(1, number.getNumber())
                .reduce(1, (long x, long y) -> x * y));
        return number;
    }

    @Override
    public Variable visitComparisonExpression(ArithmeticParser.ComparisonExpressionContext ctx) {
        var left = this.visit(ctx.expression(0)).getNumber();
        var right = this.visit(ctx.expression(1)).getNumber();

        switch (ctx.getChild(1).getText()) {
            case ">":
                return new Variable(left > right);
            case "<":
                return new Variable(left < right);
            case "<=":
                return new Variable(left <= right);
            case ">=":
                return new Variable(left >= right);
            case "==":
                return new Variable(left.equals(right));
            case "!=":
                return new Variable(!left.equals(right));
            default:
                return new Variable();
        }
    }

    @Override
    public Variable visitAndExpression(ArithmeticParser.AndExpressionContext ctx) {
        return new Variable(this.visit(ctx.expression(0)).getBool() && this.visit(ctx.expression(1)).getBool());
    }

    @Override
    public Variable visitOrExpression(ArithmeticParser.OrExpressionContext ctx) {
        return new Variable(this.visit(ctx.expression(0)).getBool() || this.visit(ctx.expression(1)).getBool());
    }

    @Override
    public Variable visitParenthesisExpression(ArithmeticParser.ParenthesisExpressionContext ctx) {
        return this.visit(ctx.expression());
    }

    @Override
    public Variable visitIf_statement(ArithmeticParser.If_statementContext ctx) {
        List<ArithmeticParser.Condition_blockContext> conditions = ctx.condition_block();

        var evaluatedBlock = false;

        for (ArithmeticParser.Condition_blockContext condition : conditions) {

            Variable evaluated = this.visit(condition.expression());

            if (Boolean.TRUE.equals(evaluated.getBool())) {
                evaluatedBlock = true;

                this.visit(condition.code_block());
                break;
            }
        }

        if (!evaluatedBlock && ctx.code_block() != null) {
            // Evaluate the else code block if it is present
            this.visit(ctx.code_block());
        }

        return new Variable();
    }

    @Override
    public Variable visitWhile_statement(ArithmeticParser.While_statementContext ctx) {
        ArithmeticParser.Condition_blockContext condition = ctx.condition_block();

        Variable value = this.visit(condition.expression());

        while (Boolean.TRUE.equals(value.getBool())) {
            // Visit code block
            this.visit(condition.code_block());

            // Evaluate expression
            value = this.visit(condition.expression());
        }
        return new Variable();
    }

    @Override
    public Variable visitFor_statement(ArithmeticParser.For_statementContext ctx) {
        // Visit all the assignments of the first part of the for loop.
        for (var i = 0; i < ctx.assignment().size() - 1; i++) {
            this.visit(ctx.assignment(i));
        }

        this.visit(ctx.expression());

        do {
            // Visit the code block
            this.visit(ctx.code_block());

            this.visit(ctx.assignment(ctx.assignment().size() - 1));
        } while (Boolean.TRUE.equals(this.visit(ctx.expression()).getBool()));

        return new Variable();
    }

    @Override
    public Variable visitFunction_definition(ArithmeticParser.Function_definitionContext ctx) {
        functions.put(ctx.VALUE().getText(), ctx);
        return new Variable();
    }

    @Override
    public Variable visitFunction_call(ArithmeticParser.Function_callContext ctx) {
        // Get the function from memory (functions map).
        var function = functions.get(ctx.VALUE().getText());
        // Get all the statements from the function.
        List<ArithmeticParser.StatementContext> statements = function.code_block().statement();

        Map<String, Variable> local = new HashMap<>();

        // Check if the call is correct in number of arguments / parameters.
        if (function.arguments().getChildCount() != ctx.arguments().getChildCount()) {
            new ArgumentMismatchException("Number of parameters does not match.").printStackTrace();
        }

        // Go through all arguments and add them to memory.
        for (var i = 0; i < function.arguments().getChildCount(); i++) {
            var variable = this.visit(ctx.arguments().getChild(i));
            // Insert variable at start of list
            local.put(function.arguments().getChild(i).getText(), variable);
        }

        local_variables.putAll(local);

        var result = new Variable();

        // Visit all statements in the code block.
        for (ArithmeticParser.StatementContext statement : statements) {
            var s = this.visit(statement);
            // If the statement is a return statement, set result to statement result, and break the function
            if (statement.return_statement() != null) {
                result = s;
                break;
            }
        }

        local_variables.clear();

        return result;
    }

    @Override
    public Variable visitReturn_statement(ArithmeticParser.Return_statementContext ctx) {
        return this.visit(ctx.expression());
    }

    private enum TYPE {
        NUMBER,
        STRING,
        BOOL
    }

    protected static class TypeMismatchException extends Exception {
        public TypeMismatchException(String errorMessage) {
            super(errorMessage);
        }
    }

    protected static class ArgumentMismatchException extends Exception {
        public ArgumentMismatchException(String errorMessage) {
            super(errorMessage);
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    protected static class Variable {
        private String string;
        private Integer number;
        private Boolean bool;
        private TYPE type;

        public Variable(Boolean bool) {
            this.bool = bool;
            this.type = TYPE.BOOL;
        }

        public Variable(Integer number) {
            this.number = number;
            this.type = TYPE.NUMBER;
        }

        public Variable(String string) {
            this.string = string;
            this.type = TYPE.STRING;
        }

        public Object getValue() {
            switch (type) {
                case STRING:
                    return this.string;
                case BOOL:
                    return this.bool;
                default:
                    return this.number;
            }
        }

        public void setBool(Boolean bool) {
            this.bool = bool;
            this.type = TYPE.BOOL;
        }

        public void setNumber(Integer number) {
            this.number = number;
            this.type = TYPE.NUMBER;
        }

        public void setString(String string) {
            this.string = string;
            this.type = TYPE.STRING;
        }

        public void concatVariables(Variable a, Variable b) throws TypeMismatchException {
            var aType = a.getType();
            var bType = b.getType();

            if (!((aType.equals(bType) && a.type == TYPE.NUMBER) || a.type == TYPE.STRING || b.type == TYPE.STRING)) {
                throw new TypeMismatchException("Type error: Type mismatch");
            }

            if (a.type == TYPE.STRING || b.type == TYPE.STRING) {
                var x1 = a.getValue().toString();
                var x2 = b.getValue().toString();

                this.string = x1 + x2;
                this.type = TYPE.STRING;
            } else {
                this.number = a.getNumber() + b.getNumber();
                this.type = TYPE.NUMBER;
            }
        }
    }
}