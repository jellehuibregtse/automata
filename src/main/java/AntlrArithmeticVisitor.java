import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.LongStream;

public class AntlrArithmeticVisitor extends ArithmeticBaseVisitor<AntlrArithmeticVisitor.Variable> {
    private final Map<String, Variable> memory = new HashMap<>();

    @Override
    public Variable visitAssignment(ArithmeticParser.AssignmentContext ctx) {
        String id = ctx.VALUE().getText();
        Variable value = null;
        if (ctx.children.stream().anyMatch(e -> e.getText().equals("=")))
            value = this.visit(ctx.expression());
        return memory.put(id, value);
    }

    @Override
    public Variable visitPrint(ArithmeticParser.PrintContext ctx) {
        var variable = this.visit(ctx.expression());
        var varType = variable.getType();

        switch (varType) {
            case NUMBER:
                System.out.println(variable.getNumber());
                return variable;
            case STRING:
                System.out.println(variable.getString());
                return variable;
            case BOOL:
                System.out.println(variable.getBool());
                return variable;
            default:
                return variable;
        }
    }

    @Override
    public Variable visitValueExpression(ArithmeticParser.ValueExpressionContext ctx) {
        // Retrieve value of variable from memory.
        String name = ctx.getText();
        Variable value = memory.get(name);

        if (value == null) {
            try {
                throw new TypeMisMatchException("There is no variable with name: " + name);
            } catch (TypeMisMatchException e) {
                e.printStackTrace();
            }
        }

        return value;
    }

    @Override
    public Variable visitNumberExpression(ArithmeticParser.NumberExpressionContext ctx) {
        return new Variable(Integer.parseInt(ctx.getText()));
    }

    @Override
    public Variable visitStringExpression(ArithmeticParser.StringExpressionContext ctx) {
        return new Variable(ctx.getText());
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
            } catch (TypeMisMatchException e) {
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
        Variable value = this.visit(ctx.expression());

        while (Boolean.TRUE.equals(value.getBool())) {
            // Visit code block
            this.visit(ctx.code_block());

            // Evaluate expression
            value = this.visit(ctx.expression());
        }
        return new Variable();
    }

    private enum TYPE {
        NUMBER,
        STRING,
        BOOL
    }

    protected static class TypeMisMatchException extends Exception {
        public TypeMisMatchException(String errorMessage) {
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

        public void concatVariables(Variable a, Variable b) throws TypeMisMatchException {
            var aType = a.getType();
            var bType = b.getType();

            if (!aType.equals(bType)) {
                throw new TypeMisMatchException("Type error: Type mismatch");
            }

            if (a.getString() != null && b.getString() != null) {
                var x1 = a.getString().substring(1, a.getString().length() - 1);
                var x2 = b.getString().substring(1, b.getString().length() - 1);

                String result = x1 + x2;

                this.type = TYPE.STRING;
                this.string = result;
            }

            this.number = a.getNumber() + b.getNumber();
            this.type = TYPE.NUMBER;
        }
    }
}