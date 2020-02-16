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