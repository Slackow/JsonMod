grammar Json;

options {
    contextSuperClass = JsonParser.CustomContext;
}

@parser::members {
    public static final int EOF = -1;

    public static class CustomContext extends ParserRuleContext {
        public CustomContext() { super(); }
        public CustomContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode getToken(int ttype, int i) {
            if (children == null || i < 0 || i >= children.size()) return null;
            int j = -1;
            for (ParseTree o : children) {
                if (o instanceof TerminalNode t && t.getSymbol().getType() == ttype && ++j == i) {
                    return t;
                }
            }
            return null;
        }
    }
}

json_file: value EOF;

value: object | array | STRING | number | 'true' | 'false' | 'null';

object: '{' (member (',' member)*)? '}';
member: STRING ':' value;

array: '[' (value (',' value)*)? ']';

number: INTEGER FRACTION? EXPONENT?;

STRING: '"' CHARACTERS? '"';


fragment CHARACTER: ~[\u0000-\u001F\\"];

fragment ESCAPE: '\\' (["\\/bfnrt] | UNICODE);
fragment UNICODE: 'u' HEX HEX HEX HEX;
fragment HEX: [0-9A-Fa-f];

FRACTION: '.' [0-9]+;
INTEGER: '-'? ([0-9] | [1-9] [0-9]+);
EXPONENT: [eE] [+-]? [0-9]+;

WS: [ \r\n\t]+ -> skip;

fragment CHARACTERS: (CHARACTER | ESCAPE)+;