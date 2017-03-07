package ninja.tox.java2script.parser.java;

import org.junit.Test;
import static org.junit.Assert.*;

public class JavaParserTest {
    @Test
    public void testEmptyClass() {
        JavaParser parser = new JavaParser("package test.package; class TestClass {}");
        assertEquals(
                "DOCUMENT: null\n" +
                " PACKAGE: null\n" +
                "  TYPE_PATH: null\n" +
                "   TYPE_PATH_SEGMENT: test\n" +
                "   TYPE_PATH_SEGMENT: package\n" +
                " OPTION_DEFINITION: null\n" +
                "  CLASS: TestClass\n" +
                "   CLASS_BODY: null\n", parser.parseDocument().toString());
    }

    @Test
    public void testUndefinedMember() {
        JavaParser parser = new JavaParser(
                "int testMember;\n");
        assertEquals(
                "MEMBER: void\n" +
                " MEMBER_NAME: testMember\n", parser.parseMember().toString());
    }

    @Test
    public void testEmptyMethod() {
        JavaParser parser = new JavaParser(
                "int testMethod() {\n" +
                "}\n");
        assertEquals(
                "MEMBER: int\n" +
                " MEMBER_NAME: testMethod\n" +
                " METHOD: null\n" +
                "  METHOD_ARG_DEFINITION: null\n" +
                "  CODE_BLOCK: null\n", parser.parseMember().toString());
    }

    @Test
    public void testAbstractMethod() {
        JavaParser parser = new JavaParser(
                "void testMethod();\n");
        assertEquals(
                "MEMBER: void\n" +
                " MEMBER_NAME: testMethod\n" +
                " METHOD: null\n" +
                "  METHOD_ARG_DEFINITION: null\n", parser.parseMember().toString());
    }
}
