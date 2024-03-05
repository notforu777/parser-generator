package lab4gen.Calc;

import java.text.ParseException;

public class CalcParser {
    private CalcLexer lex;

    public Tree parse(String str) throws ParseException {
        lex = new CalcLexer(str);
        lex.nextToken();
        return e(0);
    }

    private Tree p(Integer x) throws ParseException {
    Tree res = new Tree("p");
    Token token = lex.getCurToken().a;
    String str = lex.getCurToken().b;
    switch (token) {
        case UNARY_MINUS -> {
    Tree f = f(0);
res.children.add(f);


Tree pPrime = pPrime(f.v);
res.children.add(pPrime);


  res.v = pPrime.v;
}

case NUM -> {
    Tree f = f(0);
res.children.add(f);


Tree pPrime = pPrime(f.v);
res.children.add(pPrime);


  res.v = pPrime.v;
}

case LPAREN -> {
    Tree f = f(0);
res.children.add(f);


Tree pPrime = pPrime(f.v);
res.children.add(pPrime);


  res.v = pPrime.v;
}


        case MUL, END, RPAREN, PLUS, MINUS -> {return res;}


        default -> throw new AssertionError();
    }

    return res;
}



private Tree t(Integer x) throws ParseException {
    Tree res = new Tree("t");
    Token token = lex.getCurToken().a;
    String str = lex.getCurToken().b;
    switch (token) {
        case UNARY_MINUS -> {
    Tree p = p(0);
res.children.add(p);


Tree tPrime = tPrime(p.v);
res.children.add(tPrime);


  res.v = tPrime.v;
}

case NUM -> {
    Tree p = p(0);
res.children.add(p);


Tree tPrime = tPrime(p.v);
res.children.add(tPrime);


  res.v = tPrime.v;
}

case LPAREN -> {
    Tree p = p(0);
res.children.add(p);


Tree tPrime = tPrime(p.v);
res.children.add(tPrime);


  res.v = tPrime.v;
}


        case END, RPAREN, PLUS, MINUS -> {return res;}


        default -> throw new AssertionError();
    }

    return res;
}



private Tree pPrime(Integer acc) throws ParseException {
    Tree res = new Tree("pPrime");
    Token token = lex.getCurToken().a;
    String str = lex.getCurToken().b;
    switch (token) {
        case POW -> {
    Tree POW = new Tree("POW");
res.children.add(POW);

lex.nextToken();


Tree p = p(0);
res.children.add(p);


  res.v = (int) Math.pow(acc, p.v);
}


        case MUL, END, RPAREN, PLUS, MINUS -> res.v = acc;


        default -> throw new AssertionError();
    }

    return res;
}



private Tree e(Integer x) throws ParseException {
    Tree res = new Tree("e");
    Token token = lex.getCurToken().a;
    String str = lex.getCurToken().b;
    switch (token) {
        case UNARY_MINUS -> {
    Tree t = t(0);
res.children.add(t);


Tree ePrime = ePrime(t.v);
res.children.add(ePrime);


  res.v = ePrime.v;
}

case NUM -> {
    Tree t = t(0);
res.children.add(t);


Tree ePrime = ePrime(t.v);
res.children.add(ePrime);


  res.v = ePrime.v;
}

case LPAREN -> {
    Tree t = t(0);
res.children.add(t);


Tree ePrime = ePrime(t.v);
res.children.add(ePrime);


  res.v = ePrime.v;
}


        case END, RPAREN -> {return res;}


        default -> throw new AssertionError();
    }

    return res;
}



private Tree tPrime(Integer acc) throws ParseException {
    Tree res = new Tree("tPrime");
    Token token = lex.getCurToken().a;
    String str = lex.getCurToken().b;
    switch (token) {
        case MUL -> {
    Tree MUL = new Tree("MUL");
res.children.add(MUL);

lex.nextToken();


Tree p = p(0);
res.children.add(p);


Tree tPrime = tPrime(acc * p.v);
res.children.add(tPrime);


  res.v = tPrime.v;
}


        case END, RPAREN, PLUS, MINUS -> res.v = acc;


        default -> throw new AssertionError();
    }

    return res;
}



private Tree f(Integer x) throws ParseException {
    Tree res = new Tree("f");
    Token token = lex.getCurToken().a;
    String str = lex.getCurToken().b;
    switch (token) {
        case UNARY_MINUS -> {
    Tree UNARY_MINUS = new Tree("UNARY_MINUS");
res.children.add(UNARY_MINUS);

lex.nextToken();


Tree f = f(0);
res.children.add(f);


  res.v = -f.v;
}

case NUM -> {
    Tree NUM = new Tree("NUM");
res.children.add(NUM);

lex.nextToken();


  res.v = Integer.parseInt(str);
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


  res.v = e.v;
}


        case MUL, POW, END, RPAREN, PLUS, MINUS -> {return res;}


        default -> throw new AssertionError();
    }

    return res;
}



private Tree ePrime(Integer acc) throws ParseException {
    Tree res = new Tree("ePrime");
    Token token = lex.getCurToken().a;
    String str = lex.getCurToken().b;
    switch (token) {
        case PLUS -> {
    Tree PLUS = new Tree("PLUS");
res.children.add(PLUS);

lex.nextToken();


Tree t = t(0);
res.children.add(t);


Tree ePrime = ePrime(acc + t.v);
res.children.add(ePrime);


  res.v = ePrime.v;
}

case MINUS -> {
    Tree MINUS = new Tree("MINUS");
res.children.add(MINUS);

lex.nextToken();


Tree t = t(0);
res.children.add(t);


Tree ePrime = ePrime(acc - t.v);
res.children.add(ePrime);


  res.v = ePrime.v;
}


        case END, RPAREN -> res.v = acc;


        default -> throw new AssertionError();
    }

    return res;
}

}
