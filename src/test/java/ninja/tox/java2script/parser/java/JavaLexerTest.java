package ninja.tox.java2script.parser.java;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Created by tox on 4/21/15.
 */
public class JavaLexerTest {
    @Test
    public void testNextToken() {
        JavaLexer lexer = new JavaLexer("class Foo {}");
        assertEquals("class", lexer.nextToken());
        assertEquals("Foo", lexer.nextToken());
        assertEquals("{", lexer.nextToken());
        assertEquals("}", lexer.nextToken());
        assertNull(lexer.nextToken());
    }

    @Test
    public void testPostfix() {
        JavaLexer lexer = new JavaLexer("postfix \texpr++ expr--\n");

        assertEquals("postfix", lexer.nextToken());
        assertEquals("expr", lexer.nextToken());
        assertEquals("++", lexer.nextToken());
        assertEquals("expr", lexer.nextToken());
        assertEquals("--", lexer.nextToken());
        assertNull(lexer.nextToken());
    }

    @Test
    public void testUnary() {
        JavaLexer lexer = new JavaLexer("unary ++expr --expr +expr -expr ~ !");

        assertEquals("unary", lexer.nextToken());
        assertEquals("++", lexer.nextToken());
        assertEquals("expr", lexer.nextToken());
        assertEquals("--", lexer.nextToken());
        assertEquals("expr", lexer.nextToken());
        assertEquals("+", lexer.nextToken());
        assertEquals("expr", lexer.nextToken());
        assertEquals("-", lexer.nextToken());
        assertEquals("expr", lexer.nextToken());
        assertEquals("~", lexer.nextToken());
        assertEquals("!", lexer.nextToken());
        assertNull(lexer.nextToken());
    }

    public void testMultiplicative() {
        JavaLexer lexer = new JavaLexer("a*b/c%d");
        assertEquals("a", lexer.nextToken());
        assertEquals("*", lexer.nextToken());
        assertEquals("b", lexer.nextToken());
        assertEquals("/", lexer.nextToken());
        assertEquals("c", lexer.nextToken());
        assertEquals("%", lexer.nextToken());
        assertEquals("d", lexer.nextToken());
        assertNull(lexer.nextToken());
    }

    public void testAdditive() {
        JavaLexer lexer = new JavaLexer("a+b-c");
        assertEquals("a", lexer.nextToken());
        assertEquals("+", lexer.nextToken());
        assertEquals("b", lexer.nextToken());
        assertEquals("-", lexer.nextToken());
        assertEquals("c", lexer.nextToken());
        assertNull(lexer.nextToken());
    }

    @Test
    public void testShift() {
        JavaLexer lexer = new JavaLexer("a<<b>>c>>>d");
        assertEquals("a", lexer.nextToken());
        assertEquals("<<", lexer.nextToken());
        assertEquals("b", lexer.nextToken());
        assertEquals(">>", lexer.nextToken());
        assertEquals("c", lexer.nextToken());
        assertEquals(">>>", lexer.nextToken());
        assertEquals("d", lexer.nextToken());
        assertNull(lexer.nextToken());
    }

    @Test
    public void testRelational() {
        JavaLexer lexer = new JavaLexer("a<b>c>=d<=e instanceof f");
        assertEquals("a", lexer.nextToken());
        assertEquals("<", lexer.nextToken());
        assertEquals("b", lexer.nextToken());
        assertEquals(">", lexer.nextToken());
        assertEquals("c", lexer.nextToken());
        assertEquals(">=", lexer.nextToken());
        assertEquals("d", lexer.nextToken());
        assertEquals("<=", lexer.nextToken());
        assertEquals("e", lexer.nextToken());
        assertEquals("instanceof", lexer.nextToken());
        assertEquals("f", lexer.nextToken());
        assertNull(lexer.nextToken());
    }

    @Test
    public void testEquality() {
        JavaLexer lexer = new JavaLexer("a == b != c");
        assertEquals("a", lexer.nextToken());
        assertEquals("==", lexer.nextToken());
        assertEquals("b", lexer.nextToken());
        assertEquals("!=", lexer.nextToken());
        assertEquals("c", lexer.nextToken());
        assertNull(lexer.nextToken());
    }

    @Test
    public void testBitwise() {
        JavaLexer lexer = new JavaLexer("a & b ^ c | d");
        assertEquals("a", lexer.nextToken());
        assertEquals("&", lexer.nextToken());
        assertEquals("b", lexer.nextToken());
        assertEquals("^", lexer.nextToken());
        assertEquals("c", lexer.nextToken());
        assertEquals("|", lexer.nextToken());
        assertEquals("d", lexer.nextToken());
        assertNull(lexer.nextToken());
    }

    @Test
    public void testLogical() {
        JavaLexer lexer = new JavaLexer("a && b || c");
        assertEquals("a", lexer.nextToken());
        assertEquals("&&", lexer.nextToken());
        assertEquals("b", lexer.nextToken());
        assertEquals("||", lexer.nextToken());
        assertEquals("c", lexer.nextToken());
        assertNull(lexer.nextToken());
    }

    @Test
    public void testTernary() {
        JavaLexer lexer = new JavaLexer("a ? b : c");
        assertEquals("a", lexer.nextToken());
        assertEquals("?", lexer.nextToken());
        assertEquals("b", lexer.nextToken());
        assertEquals(":", lexer.nextToken());
        assertEquals("c", lexer.nextToken());
        assertNull(lexer.nextToken());
    }

    @Test
    public void testAssignment() {
        JavaLexer lexer = new JavaLexer("a = b += c -= d *= e /= f %= g &= h ^= i |= j <<= k >>= l >>>= m");
        assertEquals("a", lexer.nextToken());
        assertEquals("=", lexer.nextToken());
        assertEquals("b", lexer.nextToken());
        assertEquals("+=", lexer.nextToken());
        assertEquals("c", lexer.nextToken());
        assertEquals("-=", lexer.nextToken());
        assertEquals("d", lexer.nextToken());
        assertEquals("*=", lexer.nextToken());
        assertEquals("e", lexer.nextToken());
        assertEquals("/=", lexer.nextToken());
        assertEquals("f", lexer.nextToken());
        assertEquals("%=", lexer.nextToken());
        assertEquals("g", lexer.nextToken());
        assertEquals("&=", lexer.nextToken());
        assertEquals("h", lexer.nextToken());
        assertEquals("^=", lexer.nextToken());
        assertEquals("i", lexer.nextToken());
        assertEquals("|=", lexer.nextToken());
        assertEquals("j", lexer.nextToken());
        assertEquals("<<=", lexer.nextToken());
        assertEquals("k", lexer.nextToken());
        assertEquals(">>=", lexer.nextToken());
        assertEquals("l", lexer.nextToken());
        assertEquals(">>>=", lexer.nextToken());
        assertEquals("m", lexer.nextToken());
        assertNull(lexer.nextToken());
    }

    // assignment 	= += -= *= /= %= &= ^= |= <<= >>= >>>=
}
