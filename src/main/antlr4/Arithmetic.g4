grammar Arithmetic;

program
    :   NEWLINE* statement (NEWLINE+ statement)* NEWLINE*
    ;

statement
    :   print
    |   assignment
    |   if_statement
    |   while_statement
    |   for_statement
    |   function_definition
    |   function_call
    |   return_statement
    |   NEWLINE
    ;

function_definition
    :   'func' TYPE VALUE LEFTPARENTHESIS arguments RIGHTPARENTHESIS code_block
    ;

function_call
    :   VALUE LEFTPARENTHESIS arguments RIGHTPARENTHESIS
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
    :   'for' LEFTPARENTHESIS (assignment (',' assignment)*) ';' expression ';' assignment  RIGHTPARENTHESIS code_block
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
    :   'var' VALUE ':' TYPE
    |   'var' VALUE ':' TYPE '=' expression
    |   VALUE ('='|'+='|'-=') expression
    |   VALUE ('++'|'--')
    ;

expression
    :   function_call                                                   # FunctionCall
    |   expression '**' expression										# PowerExpression
	|   expression '%' expression										# ModulusExpression
	|   expression ('*' | '/') expression								# MultiplicationExpression
	|   expression ('+' | '-') expression								# AdditionExpression
	|   expression '!'											        # FactorialExpression
	|   expression ('<=' | '>=' | '<' | '>' | '==' | '!=') expression	# ComparisonExpression
	|   expression '&&' expression									    # AndExpression
	|   expression '||' expression									    # OrExpression
	|   LEFTPARENTHESIS expression RIGHTPARENTHESIS										        # ParenthesisExpression
	|   NUMBER					                                        # NumberExpression
    |   (TRUE | FALSE)	                                                # BooleanExpression
    |   VALUE				                                            # ValueExpression
    |   STRING			                                                # StringExpression
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
    :   'number' | 'boolean' | 'string'
    ;

VALUE
    :   ([a-zA-Z])([a-zA-Z]|[0-9]|[_])*
    ;

LEFTPARENTHESIS
    :   '('
    ;

RIGHTPARENTHESIS
    :   ')'
    ;

WHITESPACE
    :   [\t ]+ -> skip
    ;

NUMBER
    :   [0-9]+
    ;