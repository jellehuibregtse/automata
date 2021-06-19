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
    |   comparison
    |   str_method
    |   other
    |   value
    |   variable
    |   bool
    |   number
    |   STR
    ;

other
    :   LEFTPARENTHESIS field expression+ RIGHTPARENTHESIS
    ;

bool
    :   TRUE
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
    :   LEFTPARENTHESIS LET LEFTPARENTHESIS let_rule+ RIGHTPARENTHESIS expression* RIGHTPARENTHESIS
    ;

let_rule
    :   LEFTPARENTHESIS variable expression+ RIGHTPARENTHESIS
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

str_method
    :   str_len
    |   str_substr
    |   str_at
    |   str_concatation
    |   int_to_str
    ;

str_len
    :   LEFTPARENTHESIS 'str.len' variable RIGHTPARENTHESIS
    ;

str_substr
    :   LEFTPARENTHESIS 'str.substr' variable expression expression RIGHTPARENTHESIS
    ;

str_at
    :   LEFTPARENTHESIS 'str.at' variable expression RIGHTPARENTHESIS
    ;

str_concatation
    :   LEFTPARENTHESIS 'str.++' expression+ RIGHTPARENTHESIS
    ;

int_to_str
    :   LEFTPARENTHESIS 'int.to.str' expression+ RIGHTPARENTHESIS
    ;

number
    :   NUMBER+
    ;

field
    :   (LETTER|NUMBER)+
    |   variable
    ;

variable
    :   LETTER+ '!' NUMBER+
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