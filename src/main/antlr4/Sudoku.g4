grammar Sudoku;

result
    :   SAT LEFTPARENTHESIS MODEL value+ RIGHTPARENTHESIS
    ;

value
    :   LEFTPARENTHESIS FUNCTION field LEFTPARENTHESIS decleration* RIGHTPARENTHESIS INT expression RIGHTPARENTHESIS
    ;

expression
    :   LEFTPARENTHESIS AND expression+ RIGHTPARENTHESIS
    |   LEFTPARENTHESIS ITE expression expression expression RIGHTPARENTHESIS
    |   LEFTPARENTHESIS EQUALS expression expression RIGHTPARENTHESIS
    |   NUMBER
    |   value
    ;

decleration
    :   LEFTPARENTHESIS variable INT RIGHTPARENTHESIS
    ;

field
    :   (LETTER|NUMBER)+
    ;

variable
    :   'x!' NUMBER
    ;

EQUALS
    :   '='
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

AND
    :   'and'
    ;

ITE
    :   'ite'
    ;

FUNCTION
    :   'define-fun'
    ;

INT
    :   'Int'
    ;

LETTER
    :   [a-z]
    ;

NUMBER
    :   [1-9]
    ;

WHITESPACE
    :   [ \t\r\n]+ -> skip
    ;