grammar Expressions;

expression
    :   (NEWLINE | WHITESPACE)* expr (NEWLINE | WHITESPACE)*
    |   ((NEWLINE | WHITESPACE)* statement (NEWLINE | WHITESPACE)*)*
    ;

expr
    :	expr WHITESPACE? ('*'|'/') WHITESPACE? expr                  # Mult
    |	expr WHITESPACE? ('+'|'-') WHITESPACE? expr                  # Add
    |	NUMBER                                                       # Number
    |	WHITESPACE? '(' WHITESPACE? expr WHITESPACE? ')' WHITESPACE? # Parenthesis
    ;

statement
    : 'var' WHITESPACE TEXT WHITESPACE? '=' WHITESPACE? expr WHITESPACE? NEWLINE # Var
    | TEXT WHITESPACE? '=' WHITESPACE? expr WHITESPACE? NEWLINE # Assign
    | 'print' WHITESPACE? '(' TEXT ')' NEWLINE # Print
    | 'return' WHITESPACE (expr | TEXT) (NEWLINE|WHITESPACE)* # Return
    ;

NEWLINE
    :   [\r\n]+ ;
WHITESPACE
    :   [\t ]+ ;
STRING
    :   ["]TEXT["] ;
TEXT
    :   (LETTER)(LETTER|DIGIT|[_])* ;
NUMBER
    :   DIGIT+ ;
DIGIT
    :   [0-9] ;
LETTER
    :   [A-Za-z] ;