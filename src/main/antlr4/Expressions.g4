grammar Expressions;

expression
    :   NEWLINE* expr NEWLINE*
    |   NEWLINE* statement (NEWLINE+ statement)* NEWLINE*
    ;

expr
    :	expr ('*'|'/') expr         # Mult
    |	expr ('+'|'-') expr         # Add
    |	'('  expr ')'               # Parenthesis
    |	NUMBER                      # Number
    ;

statement
    :   'var'? VALUE '=' expr          # Var
    |   'print' '(' VALUE ')'          # Print
    |   'return' (expr | VALUE)        # Return
    ;
NEWLINE
    :   [\r\n] ;
WHITESPACE
    :   [\t ] -> skip ;
VALUE
    :   (LETTER)(LETTER|DIGIT|'_')* ;
NUMBER
    :   DIGIT+ ;
DIGIT
    :   [0-9] ;
LETTER
    :   [A-Za-z] ;