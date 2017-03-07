package ninja.tox.java2script.parser.java;

public class JavaParser {
    final JavaLexer lexer;
    private String error = null;

    public JavaParser(String javaSource) {
        this.lexer = new JavaLexer(javaSource);
    }

    public JavaAstElement parseDocument() {
        String token;
        JavaAstElement element = new JavaAstElement(JavaAstElement.TYPE_DOCUMENT);

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
            case "abstract":
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

    public JavaAstElement parsePackage() {
        JavaAstElement element = new JavaAstElement(JavaAstElement.TYPE_PACKAGE);

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

    public JavaAstElement parseTypePath() {
        JavaAstElement element = new JavaAstElement(JavaAstElement.TYPE_TYPE_PATH);
        JavaAstElement child;
        String token;
        while((token = this.lexer.nextToken()) != null) {
            child = new JavaAstElement(JavaAstElement.TYPE_TYPE_PATH_SEGMENT);
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

    public JavaAstElement parseImport() {
        JavaAstElement element = new JavaAstElement(JavaAstElement.TYPE_PACKAGE);

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

    public JavaAstElement parseClass() {
        JavaAstElement element = new JavaAstElement(JavaAstElement.TYPE_CLASS);
        JavaAstElement child;
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

    public JavaAstElement parseClassBody() {
        JavaAstElement element = new JavaAstElement(JavaAstElement.TYPE_CLASS_BODY);
        String token;
        while((token = this.lexer.nextToken()) != null && !"}".equals(token)) {
            parseOptionDefinition();
        }
        return element;
    }

    public JavaAstElement parseOptionDefinition() {
        JavaAstElement element = new JavaAstElement(JavaAstElement.TYPE_OPTION_DEFINITION);
        JavaAstElement child;
        String token;
        boolean run = true;

        while(run && (token = this.lexer.nextToken()) != null) {
            run = false;
            switch(token) {
            case "private":
            case "public":
            case "static":
            case "abstract":
            case "final":
                run = true;
                child = new JavaAstElement(JavaAstElement.TYPE_OPTION);
                child.setValue(token);
                break;
            case "class":
                this.lexer.back();
                child = parseClass();
                break;
            case "interface":
                this.lexer.back();
                child = parseInterface();
                break;
            case "enum":
                this.lexer.back();
                child = parseEnum();
                break;
            default:
                this.lexer.back();
                child = parseMember();
                break;
            }
            element.addChild(child);
        }
        return element;
    }

    public JavaAstElement parseMember() {
        String token = this.lexer.nextToken();
        JavaAstElement element = new JavaAstElement(JavaAstElement.TYPE_MEMBER);
        JavaAstElement child;
        element.setValue(token);

        child = new JavaAstElement(JavaAstElement.TYPE_MEMBER_NAME);
        if((token = this.lexer.nextToken()) == null) {
            this.error = "Error";
            return null;
        }
        child.setValue(token);
        element.addChild(child);

        if((token = this.lexer.nextToken()) != null && "(".equals(token)) {
            this.lexer.back();
            element.addChild(parseMethod());
        }


        return element;
    }

    public JavaAstElement parseMethod() {
        JavaAstElement element = new JavaAstElement(JavaAstElement.TYPE_METHOD);
        JavaAstElement child = new JavaAstElement(JavaAstElement.TYPE_METHOD_ARG_DEFINITION);
        String token = this.lexer.nextToken();

        if("(".equals(token) == false) {
            this.error = "Expected '('";
            return null;
        }

        while((token = this.lexer.nextToken()) != null && !")".equals(token)) {
            // TODO parse args
        }

        element.addChild(child);

        token = this.lexer.nextToken();

        if("{".equals(token)) {
            this.lexer.back();
            element.addChild(this.parseCodeBlock());
        }
        return element;
    }

    private JavaAstElement parseCodeBlock() {
        // TODO
        return new JavaAstElement(JavaAstElement.TYPE_CODE_BLOCK);
    }

    public JavaAstElement parseEnum() {
        return new JavaAstElement(JavaAstElement.TYPE_ENUM);
    }

    public JavaAstElement parseInterface() {
        return new JavaAstElement(JavaAstElement.TYPE_INTERFACE);
    }

    public JavaAstElement parseMultiLineComment() {
        String token;
        JavaAstElement element = new JavaAstElement(JavaAstElement.TYPE_MULTI_LINE_COMMENT);
        this.lexer.startSpan();
        while((token = this.lexer.nextToken()) != null && !"*/".equals(token)) { }
        if(token == null)
            this.error = "Unterminated Comment";
        element.setValue(this.lexer.flushSpan());

        return element;
    }

    public JavaAstElement parseComment() {
        char ws;
        JavaAstElement element = new JavaAstElement(JavaAstElement.TYPE_COMMENT);
        this.lexer.startSpan();
        while((ws = this.lexer.nextWhiteSpace()) != '\n' && ws != '\0') { }

        element.setValue(this.lexer.flushSpan());

        return element;
    }
}
