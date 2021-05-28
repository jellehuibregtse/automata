grammar Expressions;

expression
    :   (NEWLINE | WHITESPACE)* expr (NEWLINE | WHITESPACE)*
    |   (NEWLINE | WHITESPACE)* statement (NEWLINE | WHITESPACE)* ((NEWLINE | WHITESPACE)* NEWLINE+ (NEWLINE | WHITESPACE)* statement (NEWLINE | WHITESPACE)*)*
    ;

expr
    :	expr WHITESPACE? ('*'|'/') WHITESPACE? expr                  # Mult
    |	expr WHITESPACE? ('+'|'-') WHITESPACE? expr                  # Add
    |	NUMBER                                                       # Number
    |	WHITESPACE? '(' WHITESPACE? expr WHITESPACE? ')' WHITESPACE? # Parenthesis
    ;

statement
    : 'var' WHITESPACE TEXT WHITESPACE? '=' WHITESPACE? expr WHITESPACE? # Var
    | TEXT WHITESPACE? '=' WHITESPACE? expr WHITESPACE? # Assign
    | 'print' WHITESPACE? '(' TEXT ')' # Print
    | 'return' WHITESPACE (expr | TEXT) # Return
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