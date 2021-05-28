grammar Expressions;

expression
    :   (NEWLINE | WHITESPACE)* expr (NEWLINE | WHITESPACE)*
    |   (var_st | assign_st | print_st)* return_st
    ;

expr
    :	expr ('*'|'/') expr # Mult
    |	expr ('+'|'-') expr # Add
    |	NUMBER              # Number
    |	'(' expr ')'        # Parenthesis
    ;

var_st
    : 'var' WHITESPACE STRING WHITESPACE? '=' WHITESPACE? expr WHITESPACE? NEWLINE ;

assign_st
    : STRING WHITESPACE? '=' WHITESPACE? expr WHITESPACE? NEWLINE ;

print_st
    : 'print' WHITESPACE? '(' (expr | STRING) ')' NEWLINE ;

return_st
    : 'return' WHITESPACE (expr | STRING) (NEWLINE|WHITESPACE)* ;

NEWLINE
    :   [\r\n]+ ;
WHITESPACE
    :   [\t ]+ ;
STRING
    :   (LETTER)(LETTER|DIGIT|[_])* ;
NUMBER
    :   DIGIT+ ;
DIGIT
    :   [0-9] ;
LETTER
    :   [A-Za-z] ;