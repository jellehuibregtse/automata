grammar Arithmetic;

program
    :   NEWLINE* statement (NEWLINE+ statement)* NEWLINE*
    ;

statement
    :   print
    |   for_statement
    |   assignment
    |   if_statement
    |   while_statement
    |   function_definition
    |   function_call
    |   return_statement
    |   NEWLINE
    ;

function_definition
    :   'func' TYPE VALUE '(' arguments ')' code_block
    ;

function_call
    :   VALUE '(' arguments ')'
    ;

arguments
    :   (expression (',' expression)*)?
    ;

return_statement
    :   'return' expression
    ;

if_statement
    :   'if' condition_block ('else if' condition_block)* ('else' code_block)?
    ;

while_statement
    :   'while' condition_block
    ;

for_statement
    :   'for' '('? assignment ';' expression ';' assignment ';'? ')'? code_block
    ;

condition_block
    :   expression code_block
    ;

code_block
    :   NEWLINE* '{' (NEWLINE*|statement*) '}'
    |   NEWLINE? statement
    ;

print
    :   'print' expression
    ;

assignment
    :   'var' (array_assignment|VALUE) (':' TYPE)?
    |   'var' (array_assignment|VALUE) (':' TYPE)? '=' expression
    |   (array_assignment|VALUE) ('='|'+='|'-=') expression
    ;

expression
    :   function_call                                                   # FunctionCall
    |   (array|array_assignment)                                        # ArrayExpression
    |   expression '**' expression										# PowerExpression
	|   expression '%' expression										# ModulusExpression
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

array_assignment
    :   VALUE array
    ;

array
    :   '[' ((NUMBER|array) (',' (NUMBER|array))*)? ']'
    ;

NEWLINE
    :   ('\r'? '\n')
    ;

TRUE
    :   'true'
    ;

FALSE
    :   'false'
    ;

STRING
    :   ((["]~["]*["])|([']~[']*[']))
    ;

TYPE
    :   'number' | 'boolean' | 'string' | 'array'
    ;

VALUE
    :   ([a-zA-Z])([a-zA-Z]|[0-9]|[_])*
    ;

WHITESPACE
    :   [\t ]+ -> skip
    ;

NUMBER
    :   [0-9]+
    ;