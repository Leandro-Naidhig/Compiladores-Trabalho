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

Program ::= Func {F unc}<br/>
Func ::= "function" Id [ "(" ParamList ")" ] ["->" Type ] StatList<br/>
ParamList ::= ParamDec {”, ”P aramDec}<br/>
ParamDec ::= Id ":" Type<br/>
Type ::= "Int" | "Boolean" | "String"<br/>
StatList ::= "{” {Stat} ”}"<br/>
Stat ::= AssignExprStat | ReturnStat | VarDecStat | IfStat | WhileStat<br/>
AssignExprStat ::= Expr [ "=" Expr ] ";"<br/>
ReturnStat ::= "return" Expr ";"<br/>
VarDecStat ::= "var" Id ":" Type ";"<br/>
IfStat ::= "if" Expr StatList [ "else" StatList ]<br/>
WhileStat ::= "while" Expr StatList<br/>
Expr ::= ExprAnd {”or”ExprAnd}<br/>
ExprAnd ::= ExprRel {”and”ExprRel}<br/>
ExprRel ::= ExprAdd [ RelOp ExprAdd ]<br/>
RelOp ::= "<" | "<=" | ">" | ">=" | "==" | "!="<br/>
ExprAdd ::= ExprMult {(” + ” | ” − ”)ExprM ult}<br/>
ExprMult ::= ExprUnary {(” ∗ ” | ”/”)ExprU nary}<br/>
ExprUnary ::= [ ( "+" | "-" ) ] ExprPrimary<br/>
ExprPrimary ::= Id | FuncCall | ExprLiteral<br/>
ExprLiteral ::= LiteralInt | LiteralBoolean | LiteralString<br/>
LiteralBoolean ::= "true" | "false"<br/>
FuncCall ::= Id "(" [ Expr {”, ”Expr} ] ")"<br/>

----

## Integrantes

- Nome: [Chady Chaito](https://github.com/chadychaito) RA: 613697

- Nome: [Gustavo Buoro Branco de Souza](https://github.com/Gustavobbs/) RA: 726533

- Nome: [José Gabriel Oliveira Santana](https://github.com/Eetrexx/) RA: 620459

- Nome: [Leandro Naidhig](https://github.com/Leandro-Naidhig/) RA: 726555
