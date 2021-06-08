grammar Sudoku;

result
    :   SAT LEFTPARENTHESIS MODEL value+ RIGHTPARENTHESIS
    ;

value
    :   LEFTPARENTHESIS FUNCTION field LEFTPARENTHESIS RIGHTPARENTHESIS INT NUMBER RIGHTPARENTHESIS
    ;

field
    :   FIELD NUMBER NUMBER
    ;

SAT
    :   'sat'
    ;

LEFTPARENTHESIS
    :   '('
    ;

RIGHTPARENTHESIS
    :   ')'
    ;

MODEL
    :   'model'
    ;

FUNCTION
    :   'define-fun'
    ;

FIELD
    :   'a'
    ;

INT
    :   'Int'
    ;

NUMBER
    :   [0-9]+
    ;

WHITESPACE
    :   [ \t\r\n]+ -> skip
    ;