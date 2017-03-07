package ninja.tox.java2script.parser.java;

public class JavaAstElement {
    /**
     * Definition of a document. That's just the .java file itself
     */
    public final static String TYPE_DOCUMENT = "DOCUMENT";
    /**
     * Defines a single line comment
     */
    public static final String TYPE_COMMENT = "COMMENT";
    /**
     * Defines a multi line comment
     */
    public static final String TYPE_MULTI_LINE_COMMENT = "MULTI_LINE_COMMENT";
    /**
     * Defines multiple options of a class/interface/enum/field
     */
    public static final String TYPE_OPTION_DEFINITION = "OPTION_DEFINITION";
    /**
     * Defines one option of a option definition
     */
    public static final String TYPE_OPTION = "OPTION";
    /**
     * defines the full qualified name of a class
     */
    public static final String TYPE_TYPE_PATH = "TYPE_PATH";
    public static final String TYPE_TYPE_PATH_SEGMENT = "TYPE_PATH_SEGMENT";
    /**
     * defines the body of a class
     */
    public static final String TYPE_CLASS = "CLASS";
    /**
     * defines the body of an interface
     */
    public static final String TYPE_CLASS_BODY = "CLASS_BODY";
    public static final String TYPE_ENUM = "ENUM";
    public static final String TYPE_ENUM_BODY = "ENUM_BODY";
    public static final String TYPE_INTERFACE = "INTERFACE";
    public static final String TYPE_PACKAGE = "PACKAGE";
    public static final String TYPE_MEMBER = "MEMBER";
    public static final String TYPE_MEMBER_NAME = "MEMBER_NAME";
    public static final String TYPE_METHOD = "METHOD";
    public static final String TYPE_METHOD_ARG_DEFINITION = "METHOD_ARG_DEFINITION";
    public static final String TYPE_CODE_BLOCK = "CODE_BLOCK";
    public static final String TYPE_ASSIGNMENT = "ASSIGNMENT";

    private String type;

    private JavaAstElement[] children = new JavaAstElement[0];

    private String value = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void addChild(JavaAstElement e) {
        int i = 0;
        JavaAstElement[] newChildren = new JavaAstElement[this.children.length+1];

        if(e == null)
            return;
        while(i < children.length) {
            newChildren[i] = children[i++];
        }
        this.children = newChildren;
        this.children[i] = e;
    }

    public JavaAstElement[] getChild(String type) {
        int i = 0, j = 0;
        JavaAstElement[] result = new JavaAstElement[this.children.length];

        while(i < children.length) {
            if(type == null || children[i].getType().equals(type)) {
                result[j++] = children[i];
            }
            i++;
        }
        return children;
    }

    public JavaAstElement(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.toString(0);
    }

    private String toString(int indent) {
        int i = 0;
        String result = "";
        while(i++ < indent) {
            result += " ";
        }

        result += this.getType() + ": " + this.getValue() + "\n";
        i = 0;
        while(i < this.children.length) {
            result += this.children[i].toString(indent + 1);
            i++;
        }
        return result;
    }
}
