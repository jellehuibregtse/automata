grammar MockExam;

program
    :   line+
    ;

line
    :   LEFTPARENTHESIS (declare_function|define_constant|assertion) RIGHTPARENTHESIS
    ;

declare_function
    :   FUNCTION variable LEFTPARENTHESIS type* RIGHTPARENTHESIS type
    ;

define_constant
    :   CONST variable type value
    ;

assertion
    :   ASSRT expression+
    ;

expression
    :   and
    |   or
    |   not
    |   ite
    |   add
    |   subtract
    |   comparison
    |   variable
    |   bool
    |   number
    |   STR
    ;

value
    :   (number|bool|STR)
    ;

number
    :   NUMBER+
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

comparison
    :   LEFTPARENTHESIS equality expression expression+ RIGHTPARENTHESIS
    ;

add
    :   LEFTPARENTHESIS PLUS expression expression+ RIGHTPARENTHESIS
    ;

subtract
    :   LEFTPARENTHESIS MINUS expression+ RIGHTPARENTHESIS
    ;

equality
    :   EQUALS
    |   LESS_THAN
    |   LESS_THAN_EQUALS
    |   BIGGER_THAN
    |   BIGGER_THAN_EQUALS
    ;

variable
    :   LETTER+
    ;

type
    :   INT
    |   STRING
    |   BOOL
    ;

FUNCTION
    :   'declare-fun'
    ;

CONST
    :   'define-const'
    ;

ASSRT
    :   'assert'
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

LEFTPARENTHESIS
    :   '('
    ;

RIGHTPARENTHESIS
    :   ')'
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