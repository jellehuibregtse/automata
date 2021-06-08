grammar Expressions;

expression
    :   (NEWLINE)* expr (NEWLINE)*
    |   (NEWLINE)* statement (NEWLINE)* (NEWLINE+ statement (NEWLINE)*)*
    ;

expr
    :	expr ('*'|'/') expr         # Mult
    |	expr ('+'|'-') expr         # Add
    |	NUMBER                      # Number
    |	 '('  expr ')'              # Parenthesis
    ;

statement
    :   'var'? VALUE '=' expr          # Var
    |   'print' '(' VALUE ')'          # Print
    |   'return' (expr | VALUE)        # Return
    ;

NEWLINE
    :   [\r\n] ;
WHITESPACE
    :   [\t\r\n ] -> skip ;
VALUE
    :   (LETTER)(LETTER|DIGIT|[_])* ;
NUMBER
    :   DIGIT+ ;
DIGIT
    :   [0-9] ;
LETTER
    :   [A-Za-z] ;