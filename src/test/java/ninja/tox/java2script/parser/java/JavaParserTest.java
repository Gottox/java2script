package ninja.tox.java2script.parser.java;

import org.junit.Test;
import static org.junit.Assert.*;

public class JavaParserTest {
    @Test
    public void testEmptyClass() {
        JavaParser parser = new JavaParser("package test.package; class TestClass {}");
        assertEquals("DOCUMENT: null\n" +
                " PACKAGE: null\n" +
                "  TYPE_PATH: null\n" +
                "   TYPE_PATH_SEGMENT: test\n" +
                "   TYPE_PATH_SEGMENT: package\n" +
                " OPTION_DEFINITION: null\n" +
                "  CLASS: TestClass\n" +
                "   CLASS_BODY: null\n", parser.parseDocument().toString());
    }
}
