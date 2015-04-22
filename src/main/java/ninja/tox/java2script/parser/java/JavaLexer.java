package ninja.tox.java2script.parser.java;

public class JavaLexer {
    private final String javaSource;
    private int index = 0;
    private String lastToken = null;
    private int spanIndex;

    public JavaLexer(String javaSource) {
        this.javaSource = javaSource;
    }

    public String nextToken() {
        int startIndex;
        char c;
        String tmp;

        // Drop whitespaces
        while(!isEnd() && Character.isWhitespace(this.curChar())) {
            this.index++;
        }
        if(isEnd())
            return null;
        startIndex = this.index;
        if(Character.isJavaIdentifierStart(this.curChar())) {
            this.index++;
            while(!isEnd() && Character.isJavaIdentifierPart(this.curChar())) {
                this.index++;
            }
        }
        else if(Character.isDigit(this.curChar())) {
            this.index++;
            while(!isEnd() && Character.isDigit(this.curChar())) {
                this.index++;
            }
        }
        else if(this.curChar() == '+' || this.curChar() == '-') {
            this.index++;
            if(doubleOperator()) {
            } else if(assignOperator()) {
            }
        } else if(this.curChar() == '=' || this.curChar() == '!') {
            this.index++;
            assignOperator();
        } else if(this.curChar() == '<' || this.curChar() == '>') {
            this.index++;
            if (doubleOperator()) {
                if (this.curChar() == '>') {
                    doubleOperator();
                }
            }
            assignOperator();
        } else if(this.curChar() == '/') {
            this.index++;
            if(assignOperator()) {
            } else if(this.curChar() == '/' || this.curChar() == '*') {
                this.index++;
            }
        } else if(this.curChar() == '*') {
            this.index++;
            if(assignOperator()) {
            } if(this.curChar() == '/') {
                this.index++;
            }
        } else if(this.curChar() == '%' || this.curChar() == '^') {
            this.index++;
            assignOperator();
        } else if(this.curChar() == '|' || this.curChar() == '&') {
            this.index++;
            if(assignOperator()) {
            } else if(doubleOperator()) {
            }
        } else {
            this.index++;
        }
        return this.lastToken = this.javaSource.substring(startIndex, this.index);
    }

    public char nextWhiteSpace() {
        while(!isEnd() && !Character.isWhitespace(this.curChar())) {
            this.index++;
        }
        if(isEnd())
            return '\0';
        return this.curChar();
    }

    public void startSpan() {
        this.spanIndex = this.index;
    }

    public String flushSpan() {
        return this.javaSource.substring(this.spanIndex, this.index);
    }

    public void back() {
        this.index -= lastToken.length();
        lastToken = null;
    }

    private boolean doubleOperator() {
        if(!isEnd() && this.curChar() == this.prevChar()) {
            this.index++;
            return true;
        }
        return false;
    }

    private boolean assignOperator() {
        if(!isEnd() && this.curChar() == '=') {
            this.index++;
            return true;
        }
        return false;
    }

    private boolean isEnd() {
        return this.index >= this.javaSource.length();
    }

    private char curChar() {
        return this.javaSource.charAt(this.index);
    }

    private char prevChar() {
        return this.javaSource.charAt(this.index-1);
    }
}
