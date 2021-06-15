grammar Sudoku;

result
    :   SAT LEFTPARENTHESIS MODEL value+ RIGHTPARENTHESIS
    ;

value
    :   LEFTPARENTHESIS FUNCTION field LEFTPARENTHESIS declaration* RIGHTPARENTHESIS INT expression RIGHTPARENTHESIS
    ;

expression
    :   and
    |   ite
    |   equals+
    |   NUMBER
    |   value
    |   variable
    ;

and
    :   LEFTPARENTHESIS AND expression+ RIGHTPARENTHESIS
    ;

ite
    :   LEFTPARENTHESIS ITE expression expression expression RIGHTPARENTHESIS
    ;

equals
    :   LEFTPARENTHESIS EQUALS expression expression RIGHTPARENTHESIS
    ;

declaration
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
    :   [0-9]
    ;

WHITESPACE
    :   [ \t\r\n]+ -> skip
    ;