grammar Arithmetic;

program
    :   statement+
    ;

statement
    :   print
    |   assignment
    |   if_statement
    |   while_statement
    |   WHITESPACE
    ;

if_statement
    :   'if' condition_block ('else if' condition_block)* ('else' code_block)?
    ;

condition_block
    :   expression code_block
    ;

code_block
    :   '{' statement* '}'
    |   statement
    ;

while_statement
    :   'while' expression code_block
    ;

print
    :   'print' '(' expression ')'
    ;

assignment
    :   'var' VALUE ':' TYPE
    |   'var' VALUE ':' TYPE '=' expression
    |   VALUE '=' expression
    ;

expression
    :   expression '**' expression										# PowerExpression
	|   expression ('*' | '/') expression								# MultiplicationExpression
	|   expression ('+' | '-') expression								# AdditionExpression
	|   expression '!'											        # FactorialExpression
	|   expression ('<=' | '>=' | '<' | '>' | '==' | '!=') expression	# ComparisonExpression
	|   expression '&&' expression									    # AndExpression
	|   expression '||' expression									    # OrExpression
	|   '(' expression ')'										        # ParenthesisExpression
	|   NUMBER					                                        # NumberExpression
    |   (TRUE | FALSE)	                                                # BooleanExpression
    |   VALUE				                                            # ValueExpression
    |   STRING			                                                # StringExpression
    ;

TRUE
    :   'true'
    ;

FALSE
    :   'false'
    ;

STRING
    :   ((["]('\\"'|~["])*["])|([']('\\\''|~['])*[']))
    ;

TYPE
    :   'number' | 'boolean' | 'string'
    ;

VALUE
    :   ([a-zA-Z])([a-zA-Z]|[0-9]|[_])*
    ;

WHITESPACE
    :   [ \t\r\n]+ -> skip
    ;

NUMBER
    :   [0-9]+
    ;