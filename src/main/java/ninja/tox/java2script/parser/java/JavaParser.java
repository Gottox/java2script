package ninja.tox.java2script.parser.java;

public class JavaParser {
    final JavaLexer lexer;
    private String error = null;

    public JavaParser(String javaSource) {
        this.lexer = new JavaLexer(javaSource);
    }

    public JavaElement parseDocument() {
        String token;
        JavaElement element = new JavaElement(JavaElement.TYPE_DOCUMENT);

        while((token = this.lexer.nextToken()) != null) {
            this.lexer.back();
            switch(token) {
            case "//":
                element.addChild(parseComment());
                break;
            case "/*":
                element.addChild(parseMultiLineComment());
                break;
            case "static":
            case "public":
            case "private":
            case "class":
            case "interface":
            case "enum":
                element.addChild(parseOptionDefinition());
                break;
            case "import":
                element.addChild(parseImport());
                break;
            case "package":
                element.addChild(parsePackage());
                break;
            default:
                this.error = "Unknown token '"+this.lexer.nextToken()+"'";
            }
        }
        return element;
    }

    public JavaElement parsePackage() {
        JavaElement element = new JavaElement(JavaElement.TYPE_PACKAGE);

        if(!"package".equals(this.lexer.nextToken())) {
            error = "keyword 'package' expected";
            return null;
        }

        element.addChild(this.parseTypePath());

        if(!";".equals(this.lexer.nextToken())) {
            error = "';' expected";
            return null;
        }
        return element;
    }

    public JavaElement parseTypePath() {
        JavaElement element = new JavaElement(JavaElement.TYPE_TYPE_PATH);
        JavaElement child;
        String token;
        while((token = this.lexer.nextToken()) != null) {
            child = new JavaElement(JavaElement.TYPE_TYPE_PATH_SEGMENT);
            child.setValue(token);
            element.addChild(child);
            token = this.lexer.nextToken();
            if(";".equals(token)) {
                this.lexer.back();
                break;
            } else if(!".".equals(token)) {
                error = "unexpected token '"+token+"'";
                return null;
            }
        }
        return element;
    }

    public JavaElement parseImport() {
        JavaElement element = new JavaElement(JavaElement.TYPE_PACKAGE);

        if(!"import".equals(this.lexer.nextToken())) {
            error = "keyword 'package' expected";
            return null;
        }

        element.addChild(this.parseTypePath());

        if(!";".equals(this.lexer.nextToken())) {
            error = "';' expected";
            return null;
        }
        return element;
    }

    public JavaElement parseClass() {
        JavaElement element = new JavaElement(JavaElement.TYPE_CLASS);
        JavaElement child;
        String token;

        if(!"class".equals(this.lexer.nextToken())) {
            error = "'class' keyword expected";
            return null;
        };

        element.setValue(this.lexer.nextToken());

        // TODO add extends / implements

        if(!"{".equals(this.lexer.nextToken())) {
            error = "'{' expected";
            return null;
        };

        this.lexer.back();
        element.addChild(parseClassBody());
        return element;
    }

    public JavaElement parseClassBody() {
        JavaElement element = new JavaElement(JavaElement.TYPE_CLASS_BODY);
        String token;
        while((token = this.lexer.nextToken()) != null && !"}".equals(token)) {}
        return element;
    }

    public JavaElement parseOptionDefinition() {
        JavaElement element = new JavaElement(JavaElement.TYPE_OPTION_DEFINITION);
        JavaElement child = null;
        String token;
        boolean run = true;

        while(run && (token = this.lexer.nextToken()) != null) {
            switch(token) {
            case "private":
            case "public":
            case "static":
            case "final":
                child = new JavaElement(JavaElement.TYPE_OPTION);
                child.setValue(token);
                break;
            case "class":
                run = false;
                this.lexer.back();
                child = parseClass();
                break;
            case "interface":
                run = false;
                this.lexer.back();
                child = parseInterface();
                break;
            case "enum":
                run = false;
                this.lexer.back();
                child = parseEnum();
                break;
            }
            element.addChild(child);
        }
        return element;
    }

    public JavaElement parseEnum() {
        return new JavaElement(JavaElement.TYPE_ENUM);
    }

    public JavaElement parseInterface() {
        return new JavaElement(JavaElement.TYPE_INTERFACE);
    }

    public JavaElement parseMultiLineComment() {
        String token;
        JavaElement element = new JavaElement(JavaElement.TYPE_MULTI_LINE_COMMENT);
        this.lexer.startSpan();
        while((token = this.lexer.nextToken()) != null && "*/".equals(token) == false) { }
        if(token == null)
            this.error = "Unterminated Comment";
        element.setValue(this.lexer.flushSpan());

        return element;
    }

    public JavaElement parseComment() {
        char ws;
        JavaElement element = new JavaElement(JavaElement.TYPE_COMMENT);
        this.lexer.startSpan();
        while((ws = this.lexer.nextWhiteSpace()) != '\n' && ws != '\0') { }

        element.setValue(this.lexer.flushSpan());

        return element;
    }
}
