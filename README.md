## COMPILADORES - FASE 3

Bacharelado em Ciência da Computação

Universidade Federal de São Carlos

Campus Sorocaba

Trabalho Prático da disciplina de Compiladores

----

## Descrição do Trabalho

#### Objetivos

Realização da Análise Léxica, Sintática, Semântica e Geração do Código na Linguagem C.

#### Gramática

Program ::= Func {F unc}
Func ::= "function" Id [ "(" ParamList ")" ] ["->" Type ] StatList
ParamList ::= ParamDec {”, ”P aramDec}
ParamDec ::= Id ":" Type
Type ::= "Int" | "Boolean" | "String"
StatList ::= "{” {Stat} ”}"
Stat ::= AssignExprStat | ReturnStat | VarDecStat | IfStat | WhileStat
AssignExprStat ::= Expr [ "=" Expr ] ";"
ReturnStat ::= "return" Expr ";"
VarDecStat ::= "var" Id ":" Type ";"
IfStat ::= "if" Expr StatList [ "else" StatList ]
WhileStat ::= "while" Expr StatList
Expr ::= ExprAnd {”or”ExprAnd}
ExprAnd ::= ExprRel {”and”ExprRel}
ExprRel ::= ExprAdd [ RelOp ExprAdd ]
RelOp ::= "<" | "<=" | ">" | ">=" | "==" | "!="
ExprAdd ::= ExprMult {(” + ” | ” − ”)ExprM ult}
ExprMult ::= ExprUnary {(” ∗ ” | ”/”)ExprU nary}
ExprUnary ::= [ ( "+" | "-" ) ] ExprPrimary
ExprPrimary ::= Id | FuncCall | ExprLiteral
ExprLiteral ::= LiteralInt | LiteralBoolean | LiteralString
LiteralBoolean ::= "true" | "false"
FuncCall ::= Id "(" [ Expr {”, ”Expr} ] ")"

----

## Integrantes

- Nome: [Chady Chaito](https://github.com/chadychaito) RA: 613697

- Nome: [Gustavo Buoro Branco de Souza](https://github.com/Gustavobbs/) RA: 726533

- Nome: [José Gabriel Oliveira Santana](https://github.com/Eetrexx/) RA: 620459

- Nome: [Leandro Naidhig](https://github.com/Leandro-Naidhig/) RA: 726555
