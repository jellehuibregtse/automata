grammar Expressions;

expression
    :   (expr NEWLINE)* ;

expr
    :	expr ('*'|'/') expr # Mult
    |	expr ('+'|'-') expr # Add
    |	NUMBER              # Number
    |	'(' expr ')'        # Parenthesis
    ;

NEWLINE
    :   [\r\n]+ ;
NUMBER
    :   [0-9]+ ;