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
    |   let
    |   ite
    |   add
    |   subtract
    |   comparison+
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

let
    :   LEFTPARENTHESIS LET LEFTPARENTHESIS expression expression expression RIGHTPARENTHESIS RIGHTPARENTHESIS
    ;

comparison
    :   LEFTPARENTHESIS equality expression expression+ RIGHTPARENTHESIS
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

equality
    :   EQUALS
    |   LESS_THAN
    |   LESS_THAN_EQUALS
    |   BIGGER_THAN
    |   BIGGER_THAN_EQUALS
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

LET
    :   'let'
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