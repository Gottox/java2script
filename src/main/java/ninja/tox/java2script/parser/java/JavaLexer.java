package ninja.tox.java2script.parser.java;

public class JavaLexer {
    private final String javaSource;
    private int index = 0;

    public JavaLexer(String javaSource) {
        this.javaSource = javaSource;
    }

    public String nextToken() {
        int startIndex;
        char c;
        // Drop whitespaces
        while(!isEnd() && Character.isWhitespace(this.curChar())) {
            index++;
        }
        if(isEnd())
            return null;
        startIndex = index;
        if(Character.isJavaIdentifierStart(this.curChar())) {
            index++;
            while(!isEnd() && Character.isJavaIdentifierPart(this.curChar())) {
                index++;
            }
        }
        else if(Character.isDigit(this.curChar())) {
            index++;
            while(!isEnd() && Character.isDigit(this.curChar())) {
                index++;
            }
        }
        else if(this.curChar() == '+' || this.curChar() == '-') {
            index++;
            if(doubleOperator()) {
            } else if(assignOperator()) {
            }
        } else if(this.curChar() == '=' || this.curChar() == '!') {
            index++;
            assignOperator();
        } else if(this.curChar() == '<' || this.curChar() == '>') {
            index++;
            if(doubleOperator()) {
                if(this.curChar() == '>') {
                    doubleOperator();
                }
            }
            assignOperator();
        } else if(this.curChar() == '*' || this.curChar() == '/' || this.curChar() == '%' || this.curChar() == '^') {
            index++;
            assignOperator();
        } else if(this.curChar() == '|' || this.curChar() == '&') {
            index++;
            if(assignOperator()) {
            } else if(doubleOperator()) {
            }
        } else {
            index++;
        }
        return this.javaSource.substring(startIndex, index);
    }

    private boolean doubleOperator() {
        if(!isEnd() && this.curChar() == this.prevChar()) {
            index++;
            return true;
        }
        return false;
    }

    private boolean assignOperator() {
        if(!isEnd() && this.curChar() == '=') {
            index++;
            return true;
        }
        return false;
    }

    private boolean isEnd() {
        return index >= this.javaSource.length();
    }

    private char curChar() {
        return this.javaSource.charAt(index);
    }

    private char prevChar() {
        return this.javaSource.charAt(index-1);
    }
}
