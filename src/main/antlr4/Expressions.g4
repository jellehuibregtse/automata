grammar Expressions;

expression
    :   ((expr | ('var' WHITESPACE)? STRING WHITESPACE? '=' WHITESPACE? expr | 'print' WHITESPACE? '(' (expr | STRING) ')') NEWLINE)* ;

expr
    :	'(' expr ')'        # Parenthesis
    |	expr ('*'|'/') expr # Mult
    |	expr ('+'|'-') expr # Add
    |	NUMBER              # Number
    |	WHITESPACE          # Whitespace
    ;

NEWLINE
    :   [\r\n]+ ;
WHITESPACE
    :   [\t ]+ ;
DIGIT
    :   [0-9] ;
NUMBER
    :   DIGIT+ ;
LETTER
    :   [A-Za-z] ;
STRING
    :   (LETTER)(LETTER|DIGIT|[_])* ;