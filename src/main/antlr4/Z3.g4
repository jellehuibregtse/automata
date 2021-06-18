grammar Z3;

result
    :   SAT LEFTPARENTHESIS MODEL value+ RIGHTPARENTHESIS
    ;

value
    :   LEFTPARENTHESIS FUNCTION field LEFTPARENTHESIS declaration* RIGHTPARENTHESIS type expression RIGHTPARENTHESIS
    ;

expression
    :   and
    |   or
    |   not
    |   ite
    |   add
    |   subtract
    |   equals
    |   less_than
    |   less_than_equals
    |   bigger_than
    |   bigger_than_equals
    |   NUMBER+
    |   value
    |   variable
    |   STR
    |   TRUE
    |   FALSE
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
    :   LEFTPARENTHESIS EQUALS expression expression+ RIGHTPARENTHESIS
    ;

less_than
    :   LEFTPARENTHESIS LESS_THAN expression expression+ RIGHTPARENTHESIS
    ;

less_than_equals
    :   LEFTPARENTHESIS LESS_THAN_EQUALS expression expression+ RIGHTPARENTHESIS
    ;

bigger_than
    :   LEFTPARENTHESIS BIGGER_THAN expression expression+ RIGHTPARENTHESIS
    ;

bigger_than_equals
    :   LEFTPARENTHESIS BIGGER_THAN_EQUALS expression expression+ RIGHTPARENTHESIS
    ;

add
    :   LEFTPARENTHESIS PLUS expression expression+ RIGHTPARENTHESIS
    ;

subtract
    :   LEFTPARENTHESIS MINUS expression+ RIGHTPARENTHESIS
    ;

declaration
    :   LEFTPARENTHESIS variable type RIGHTPARENTHESIS
    ;

field
    :   (LETTER|NUMBER)+
    ;

variable
    :   'x!' NUMBER+
    ;

type
    :   INT
    |   STRING
    |   BOOL
    ;

EQUALS
    :   '='
    ;

LESS_THAN
    :   '<'
    ;

LESS_THAN_EQUALS
    :   '<='
    ;

BIGGER_THAN
    :   '>'
    ;

BIGGER_THAN_EQUALS
    :   '>='
    ;

PLUS
    :   '+'
    ;

MINUS
    :   '-'
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

TRUE
    :   'true'
    ;

FALSE
    :   'false'
    ;

STR
    :   ((["]~["]*["])|([']~[']*[']))
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