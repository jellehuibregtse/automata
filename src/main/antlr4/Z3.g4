grammar Z3;

result
    :   SAT LEFTPARENTHESIS MODEL value+ RIGHTPARENTHESIS
    ;

value
    :   LEFTPARENTHESIS FUNCTION field LEFTPARENTHESIS declaration* RIGHTPARENTHESIS type expression RIGHTPARENTHESIS
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

or
    :   LEFTPARENTHESIS OR expression+ RIGHTPARENTHESIS
    ;

not
    :   LEFTPARENTHESIS NOT expression RIGHTPARENTHESIS
    ;

ite
    :   LEFTPARENTHESIS ITE expression expression expression RIGHTPARENTHESIS
    ;

equals
    :   LEFTPARENTHESIS EQUALS expression expression RIGHTPARENTHESIS
    ;

declaration
    :   LEFTPARENTHESIS variable type RIGHTPARENTHESIS
    ;

field
    :   (LETTER|NUMBER)+
    ;

variable
    :   'x!' NUMBER
    ;

type
    :   INT
    |   STRING
    |   BOOL
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

NOT
    :   'not'
    ;

OR
    :   'or'
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

STRING
    :   'String'
    ;

BOOL
    :   'Bool'
    ;

LETTER
    :   [A-Za-z]
    ;

NUMBER
    :   [0-9]
    ;

WHITESPACE
    :   [ \t\r\n]+ -> skip
    ;