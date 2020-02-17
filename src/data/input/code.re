%%
ws -> [ \t\n]
delimiter -> (, | ; | \{ | \} | \( | \))

digit -> [0-9]
number -> [+-]? {digit}+ (\.{digit}+)? ([Ee][+-]?{digit}+)?

letter_ul -> [A-Za-z_]
id -> {letter_ul} ({letter_ul} | {digit})*

int -> int
float -> float
double -> double
String -> String

text -> \".*\"

type -> ({int}|{float}|{double}|{String})
array -> {type}\[\]

if -> if
else -> else
relop -> (<|<=|=|!=|>|>=)

%%
ws -> WS
delimiter -> DELIMITER

digit -> NUMBER
letter_ul -> ID

number -> NUMBER
id -> ID

int -> INT
float -> FLOAT
double -> DOUBLE
String -> STRING
text -> TEXT

type -> TYPE
array -> ARRAY
if -> IF
else -> ELSE
relop -> RELOP
