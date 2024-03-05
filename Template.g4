grammar Template;

template: grammarName attrs? rules ;
grammarName: 'grammar' ID ;
attrs: 'attributes' attr+;

rules: grammarRule+ ;
grammarRule: termRule | nonTermRule ;
termRule: TERM ':' REGEX ';' ;
nonTermRule: ID attr? ':' case ('|' case)* ';' ;
case: prod+ code? ;
prod: TERM | ID inherAttr? ;
code: CODE ;
attr: '[' ID ':' ID ']' ;
inherAttr: TRIANGLE_CODE ;

TERM: [A-Z_]+ ;
ID: [a-zA-Z]+ ;
CODE: '{' .+? '}' ;
TRIANGLE_CODE: '<' .+? '>' ;
REGEX: '"'.*?'"' ;
WS: [ \n\r\t] -> skip ;