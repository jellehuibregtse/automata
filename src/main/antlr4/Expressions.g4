grammar Expressions;

expression
    :   (NEWLINE)* expr (NEWLINE)*
    |   (NEWLINE)* statement (NEWLINE)* ((NEWLINE)* NEWLINE+ (NEWLINE)* statement (NEWLINE)*)*
    ;

expr
    :	expr ('*'|'/') expr         # Mult
    |	expr ('+'|'-') expr         # Add
    |	NUMBER                      # Number
    |	 '('  expr ')'              # Parenthesis
    ;

statement
    : 'var' TEXT '=' expr           # Var
    | TEXT '=' expr                 # Assign
    | 'print' '(' TEXT ')'          # Print
    | 'return' (expr | TEXT)        # Return
    ;

NEWLINE
    :   [\r\n]+ ;
WHITESPACE
    :   [\t\r\n ]+ -> skip ;
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