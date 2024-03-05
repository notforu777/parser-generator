package lab4gen.PySubset;

import java.text.ParseException;

public class PySubsetParser {
    private PySubsetLexer lex;

    public Tree parse(String str) throws ParseException {
        lex = new PySubsetLexer(str);
        lex.nextToken();
        return e(0);
    }

    private Tree s(Integer x) throws ParseException {
    Tree res = new Tree("s");
    Token token = lex.getCurToken().a;
    String str = lex.getCurToken().b;
    switch (token) {
        case NOT -> {
    Tree t = t(0);
res.children.add(t);


Tree sPrime = sPrime(0);
res.children.add(sPrime);


  ;
}

case VAR -> {
    Tree t = t(0);
res.children.add(t);


Tree sPrime = sPrime(0);
res.children.add(sPrime);


  ;
}

case LPAREN -> {
    Tree t = t(0);
res.children.add(t);


Tree sPrime = sPrime(0);
res.children.add(sPrime);


  ;
}


        case XOR, END, RPAREN -> {return res;}


        default -> throw new AssertionError();
    }

    return res;
}



private Tree t(Integer x) throws ParseException {
    Tree res = new Tree("t");
    Token token = lex.getCurToken().a;
    String str = lex.getCurToken().b;
    switch (token) {
        case NOT -> {
    Tree f = f(0);
res.children.add(f);


Tree tPrime = tPrime(0);
res.children.add(tPrime);


  ;
}

case VAR -> {
    Tree f = f(0);
res.children.add(f);


Tree tPrime = tPrime(0);
res.children.add(tPrime);


  ;
}

case LPAREN -> {
    Tree f = f(0);
res.children.add(f);


Tree tPrime = tPrime(0);
res.children.add(tPrime);


  ;
}


        case OR, XOR, END, RPAREN -> {return res;}


        default -> throw new AssertionError();
    }

    return res;
}



private Tree e(Integer x) throws ParseException {
    Tree res = new Tree("e");
    Token token = lex.getCurToken().a;
    String str = lex.getCurToken().b;
    switch (token) {
        case NOT -> {
    Tree s = s(0);
res.children.add(s);


Tree ePrime = ePrime(0);
res.children.add(ePrime);


  ;
}

case VAR -> {
    Tree s = s(0);
res.children.add(s);


Tree ePrime = ePrime(0);
res.children.add(ePrime);


  ;
}

case LPAREN -> {
    Tree s = s(0);
res.children.add(s);


Tree ePrime = ePrime(0);
res.children.add(ePrime);


  ;
}


        case END, RPAREN -> {return res;}


        default -> throw new AssertionError();
    }

    return res;
}



private Tree tPrime(Integer x) throws ParseException {
    Tree res = new Tree("tPrime");
    Token token = lex.getCurToken().a;
    String str = lex.getCurToken().b;
    switch (token) {
        case AND -> {
    Tree AND = new Tree("AND");
res.children.add(AND);

lex.nextToken();


Tree f = f(0);
res.children.add(f);


Tree tPrime = tPrime(0);
res.children.add(tPrime);


  ;
}


        case OR, XOR, END, RPAREN -> {return res;}


        default -> throw new AssertionError();
    }

    return res;
}



private Tree sPrime(Integer x) throws ParseException {
    Tree res = new Tree("sPrime");
    Token token = lex.getCurToken().a;
    String str = lex.getCurToken().b;
    switch (token) {
        case OR -> {
    Tree OR = new Tree("OR");
res.children.add(OR);

lex.nextToken();


Tree t = t(0);
res.children.add(t);


Tree sPrime = sPrime(0);
res.children.add(sPrime);


  ;
}


        case XOR, END, RPAREN -> {return res;}


        default -> throw new AssertionError();
    }

    return res;
}



private Tree f(Integer x) throws ParseException {
    Tree res = new Tree("f");
    Token token = lex.getCurToken().a;
    String str = lex.getCurToken().b;
    switch (token) {
        case NOT -> {
    Tree NOT = new Tree("NOT");
res.children.add(NOT);

lex.nextToken();


Tree f = f(0);
res.children.add(f);


  ;
}

case VAR -> {
    Tree VAR = new Tree("VAR");
res.children.add(VAR);

lex.nextToken();


  ;
}

case LPAREN -> {
    Tree LPAREN = new Tree("LPAREN");
res.children.add(LPAREN);

lex.nextToken();


Tree e = e(0);
res.children.add(e);


Tree RPAREN = new Tree("RPAREN");
res.children.add(RPAREN);

lex.nextToken();


  ;
}


        case OR, AND, XOR, END, RPAREN -> {return res;}


        default -> throw new AssertionError();
    }

    return res;
}



private Tree ePrime(Integer x) throws ParseException {
    Tree res = new Tree("ePrime");
    Token token = lex.getCurToken().a;
    String str = lex.getCurToken().b;
    switch (token) {
        case XOR -> {
    Tree XOR = new Tree("XOR");
res.children.add(XOR);

lex.nextToken();


Tree s = s(0);
res.children.add(s);


Tree ePrime = ePrime(0);
res.children.add(ePrime);


  ;
}


        case END, RPAREN -> {return res;}


        default -> throw new AssertionError();
    }

    return res;
}

}
