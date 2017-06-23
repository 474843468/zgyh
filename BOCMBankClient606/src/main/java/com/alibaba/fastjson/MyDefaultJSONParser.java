package com.alibaba.fastjson;

import static com.alibaba.fastjson.parser.JSONScanner.EOI;
import static com.alibaba.fastjson.parser.JSONToken.EOF;
import static com.alibaba.fastjson.parser.JSONToken.ERROR;
import static com.alibaba.fastjson.parser.JSONToken.FALSE;
import static com.alibaba.fastjson.parser.JSONToken.LBRACE;
import static com.alibaba.fastjson.parser.JSONToken.LBRACKET;
import static com.alibaba.fastjson.parser.JSONToken.LITERAL_FLOAT;
import static com.alibaba.fastjson.parser.JSONToken.LITERAL_INT;
import static com.alibaba.fastjson.parser.JSONToken.LITERAL_STRING;
import static com.alibaba.fastjson.parser.JSONToken.NEW;
import static com.alibaba.fastjson.parser.JSONToken.NULL;
import static com.alibaba.fastjson.parser.JSONToken.RBRACKET;
import static com.alibaba.fastjson.parser.JSONToken.SET;
import static com.alibaba.fastjson.parser.JSONToken.TREE_SET;
import static com.alibaba.fastjson.parser.JSONToken.TRUE;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeSet;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.TypeUtils;

public class MyDefaultJSONParser extends com.alibaba.fastjson.parser.DefaultJSONParser {
	public MyDefaultJSONParser(String input, ParserConfig config, int features) {
		super(input, config, features);
	}
	public MyDefaultJSONParser(char[] input, int length, ParserConfig globalInstance, int features) {
		super(input, length, globalInstance, features);
	}
	public MyDefaultJSONParser(String text, ParserConfig globalInstance) {
		super(text, globalInstance);
	}
	//@Override
	public final void parseArrayNew(final Collection array) {
        parseArrayNew(array, null);
    }
	//@Override
	public final void parseArrayNew(final Collection array, Object fieldName) {
		final JSONLexer lexer = getLexer();

        if (lexer.token() == JSONToken.SET || lexer.token() == JSONToken.TREE_SET) {
            lexer.nextToken();
        }

        if (lexer.token() != JSONToken.LBRACKET) {
            throw new JSONException("syntax error, expect [, actual " + JSONToken.name(lexer.token()));
        }

        lexer.nextToken(JSONToken.LITERAL_STRING);

        ParseContext context = this.getContext();
        this.setContext(array, fieldName);
        try {
            for (int i = 0;; ++i) {
                if (isEnabled(Feature.AllowArbitraryCommas)) {
                    while (lexer.token() == JSONToken.COMMA) {
                        lexer.nextToken();
                        continue;
                    }
                }

                Object value;
                switch (lexer.token()) {
                    case LITERAL_INT:
                        value = lexer.integerValue();
                        value=myValueToString(value);//syl--add
                        lexer.nextToken(JSONToken.COMMA);
                        break;
                    case LITERAL_FLOAT:
                        if (lexer.isEnabled(Feature.UseBigDecimal)) {
                            value = lexer.decimalValue(true);
                        } else {
                            value = lexer.decimalValue(false);
                        }
                        value=myValueToString(value);//syl--add
                        lexer.nextToken(JSONToken.COMMA);
                        break;
                    case LITERAL_STRING:
                        String stringLiteral = lexer.stringVal();
                        lexer.nextToken(JSONToken.COMMA);

                        if (lexer.isEnabled(Feature.AllowISO8601DateFormat)) {
                            JSONScanner iso8601Lexer = new JSONScanner(stringLiteral);
                            if (iso8601Lexer.scanISO8601DateIfMatch()) {
                                value = iso8601Lexer.getCalendar().getTime();
                                value=myValueToString(value);//syl--add
                            } else {
                                value = stringLiteral;
                            }
                        } else {
                            value = stringLiteral;
                        }

                        break;
                    case TRUE:
                        value = Boolean.TRUE;
                        value=myValueToString(value);//syl--add
                        lexer.nextToken(JSONToken.COMMA);
                        break;
                    case FALSE:
                        value = Boolean.FALSE;
                        value=myValueToString(value);//syl--add
                        lexer.nextToken(JSONToken.COMMA);
                        break;
                    case LBRACE:
                        JSONObject object = new JSONObject();
                        value = parseObjectNew(object, i);//syl--upd
                        break;
                    case LBRACKET:
                        Collection items = new JSONArray();
                        //parseArray(items, i);//syl--del
                        parseArrayNew(items, i);//syl--add
                        value = items;
                        break;
                    case NULL:
                        value = null;
                        lexer.nextToken(JSONToken.LITERAL_STRING);
                        break;
                    case RBRACKET:
                        lexer.nextToken(JSONToken.COMMA);
                        return;
                    default:
                        value = parse();
                        break;
                }

                array.add(value);
                checkListResolve(array);

                if (lexer.token() == JSONToken.COMMA) {
                    lexer.nextToken(JSONToken.LITERAL_STRING);
                    continue;
                }
            }
        } finally {
            this.setContext(context);
        }
	}
	
	@Override
	public Object parseObject(final Map object) {
        return parseObjectNew(object, null);
    }

	@Override
	public Object parse(Object fieldName) {
		final JSONLexer lexer = getLexer();
        switch (lexer.token()) {
            case SET:
                lexer.nextToken();
                HashSet<Object> set = new HashSet<Object>();
                parseArrayNew(set, fieldName);//syl--upd
                return set;
            case TREE_SET:
                lexer.nextToken();
                TreeSet<Object> treeSet = new TreeSet<Object>();
                parseArrayNew(treeSet, fieldName);//syl--upd
                return treeSet;
            case LBRACKET:
                JSONArray array = new JSONArray();
                parseArrayNew(array, fieldName);//syl--upd
                return array;
            case LBRACE:
                JSONObject object = new JSONObject();
                return parseObjectNew(object, fieldName);//syl--upd
            case LITERAL_INT:
                Number intValue = lexer.integerValue();
                lexer.nextToken();
                return intValue+"";//syl--upd
            case LITERAL_FLOAT:
                Object value = lexer.decimalValue(isEnabled(Feature.UseBigDecimal));
                lexer.nextToken();
                return value+"";//syl--upd
            case LITERAL_STRING:
                String stringLiteral = lexer.stringVal();
                lexer.nextToken(JSONToken.COMMA);

                if (lexer.isEnabled(Feature.AllowISO8601DateFormat)) {
                    JSONScanner iso8601Lexer = new JSONScanner(stringLiteral);
                    if (iso8601Lexer.scanISO8601DateIfMatch()) {
                        return iso8601Lexer.getCalendar().getTime()+"";
                    }
                }

                return stringLiteral;
            case NULL:
                lexer.nextToken();
                return null;
            case TRUE:
                lexer.nextToken();
                return Boolean.TRUE+"";//syl--upd
            case FALSE:
                lexer.nextToken();
                return Boolean.FALSE+"";//syl--upd
            case NEW:
                lexer.nextToken(JSONToken.IDENTIFIER);

                if (lexer.token() != JSONToken.IDENTIFIER) {
                    throw new JSONException("syntax error");
                }
                lexer.nextToken(JSONToken.LPAREN);

                accept(JSONToken.LPAREN);
                long time = ((Number) lexer.integerValue()).longValue();
                accept(JSONToken.LITERAL_INT);

                accept(JSONToken.RPAREN);

                return new Date(time)+"";//syl--toChekc
            case EOF:
                if (lexer.isBlankInput()) {
                    return null;
                }
                throw new JSONException("unterminated json string, pos " + lexer.getBufferPosition());
            case ERROR:
            default:
                throw new JSONException("syntax error, pos " + lexer.getBufferPosition());
        }
		
	}
	
	public final Object parseObjectNew(final Map object, Object fieldName) {
		JSONScanner lexer = (JSONScanner) this.lexer;

        if (lexer.token() != JSONToken.LBRACE && lexer.token() != JSONToken.COMMA) {
            throw new JSONException("syntax error, expect {, actual " + lexer.tokenName());
        }

        ParseContext context = this.getContext();
        try {
            boolean setContextFlag = false;
            for (;;) {
                lexer.skipWhitespace();
                char ch = lexer.getCurrent();
                if (isEnabled(Feature.AllowArbitraryCommas)) {
                    while (ch == ',') {
                        lexer.incrementBufferPosition();
                        lexer.skipWhitespace();
                        ch = lexer.getCurrent();
                    }
                }

                boolean isObjectKey = false;
                Object key;
                if (ch == '"') {
                    key = lexer.scanSymbol(symbolTable, '"');
                    lexer.skipWhitespace();
                    ch = lexer.getCurrent();
                    if (ch != ':') {
                        throw new JSONException("expect ':' at " + lexer.pos() + ", name " + key);
                    }
                } else if (ch == '}') {
                    lexer.incrementBufferPosition();
                    lexer.resetStringPosition();
                    lexer.nextToken();
                    return object;
                } else if (ch == '\'') {
                    if (!isEnabled(Feature.AllowSingleQuotes)) {
                        throw new JSONException("syntax error");
                    }

                    key = lexer.scanSymbol(symbolTable, '\'');
                    lexer.skipWhitespace();
                    ch = lexer.getCurrent();
                    if (ch != ':') {
                        throw new JSONException("expect ':' at " + lexer.pos());
                    }
                } else if (ch == EOI) {
                    throw new JSONException("syntax error");
                } else if (ch == ',') {
                    throw new JSONException("syntax error");
                } else if ((ch >= '0' && ch <= '9') || ch == '-') {
                    lexer.resetStringPosition();
                    lexer.scanNumber();
                    if (lexer.token() == JSONToken.LITERAL_INT) {
                        key = lexer.integerValue();
                    } else {
                        key = lexer.decimalValue(true);
                    }
                    ch = lexer.getCurrent();
                    if (ch != ':') {
                        throw new JSONException("expect ':' at " + lexer.pos() + ", name " + key);
                    }
                } else if (ch == '{' || ch == '[') {
                    lexer.nextToken();
                    key = parse();
                    isObjectKey = true;
                } else {
                    if (!isEnabled(Feature.AllowUnQuotedFieldNames)) {
                        throw new JSONException("syntax error");
                    }

                    key = lexer.scanSymbolUnQuoted(symbolTable);
                    lexer.skipWhitespace();
                    ch = lexer.getCurrent();
                    if (ch != ':') {
                        throw new JSONException("expect ':' at " + lexer.pos() + ", actual " + ch);
                    }
                }

                if (!isObjectKey) {
                    lexer.incrementBufferPosition();
                    lexer.skipWhitespace();
                }

                ch = lexer.getCurrent();

                lexer.resetStringPosition();

                if (key == "@type") {
                    String typeName = lexer.scanSymbol(symbolTable, '"');
                    Class<?> clazz = TypeUtils.loadClass(typeName);

                    if (clazz == null) {
                        object.put("@type", typeName);
                        continue;
                    }

                    lexer.nextToken(JSONToken.COMMA);
                    if (lexer.token() == JSONToken.RBRACE) {
                        lexer.nextToken(JSONToken.COMMA);
                        try {
                            return clazz.newInstance();
                        } catch (Exception e) {
                            throw new JSONException("create instance error", e);
                        }
                    }

                    this.setResolveStatus(TypeNameRedirect);

                    if (this.context != null && !(fieldName instanceof Integer)) {
                        this.popContext();
                    }

                    ObjectDeserializer deserializer = config.getDeserializer(clazz);
                    return deserializer.deserialze(this, clazz, fieldName);
                }
                
                if (key == "$ref") {
                    lexer.nextToken(JSONToken.LITERAL_STRING);
                    if (lexer.token() == JSONToken.LITERAL_STRING) {
                        String ref = lexer.stringVal();
                        lexer.nextToken(JSONToken.RBRACE);

                        Object refValue = null;
                        if ("@".equals(ref)) {
                            if (this.getContext() != null) {
                                refValue = this.getContext().getObject();
                            }
                        } else if ("..".equals(ref)) {
                            ParseContext parentContext = context.getParentContext();
                            if (parentContext.getObject() != null) {
                                refValue = this.getContext().getObject();
                            } else {
                                addResolveTask(new ResolveTask(parentContext, ref));
                                setResolveStatus(DefaultJSONParser.NeedToResolve);
                            }
                        } else if ("$".equals(ref)) {
                            ParseContext rootContext = context;
                            while (rootContext.getParentContext() != null) {
                                rootContext = rootContext.getParentContext();
                            }

                            if (rootContext.getObject() != null) {
                                refValue = rootContext.getObject();
                            } else {
                                addResolveTask(new ResolveTask(rootContext, ref));
                                setResolveStatus(DefaultJSONParser.NeedToResolve);
                            }
                        } else {
                            addResolveTask(new ResolveTask(context, ref));
                            setResolveStatus(DefaultJSONParser.NeedToResolve);
                        }

                        if (lexer.token() != JSONToken.RBRACE) {
                            throw new JSONException("syntax error");
                        }
                        lexer.nextToken(JSONToken.COMMA);

                        return refValue;
                    } else {
                        throw new JSONException("illegal ref, " + JSONToken.name(lexer.token()));
                    }
                }
                
                if (!setContextFlag) {
                    setContext(object, fieldName);
                    setContextFlag = true;
                }

                Object value;
                if (ch == '"') {
                    lexer.scanString();
                    String strValue = lexer.stringVal();
                    value = strValue;

                    if (lexer.isEnabled(Feature.AllowISO8601DateFormat)) {
                        JSONScanner iso8601Lexer = new JSONScanner(strValue);
                        if (iso8601Lexer.scanISO8601DateIfMatch()) {
                            value = iso8601Lexer.getCalendar().getTime();
                        }
                    }

                    if (object.getClass() == JSONObject.class) {
                        object.put(key.toString(), value);
                    } else {
                        object.put(key, value);
                    }
                } else if (ch >= '0' && ch <= '9' || ch == '-') {
                    lexer.scanNumber();
                    if (lexer.token() == JSONToken.LITERAL_INT) {
                        value = lexer.integerValue();
                    } else {
                        value = lexer.decimalValue();
                    }
                    value=value+"";//syl--add
                    object.put(key, value);
                } else if (ch == '[') { // 减少潜套，兼容android
                    lexer.nextToken();
                    JSONArray list = new JSONArray();
                    this.parseArrayNew(list, key);//syl--upd
                    value = list;
                    object.put(key, value);

                    if (lexer.token() == JSONToken.RBRACE) {
                        lexer.nextToken();
                        return object;
                    } else if (lexer.token() == JSONToken.COMMA) {
                        continue;
                    } else {
                        throw new JSONException("syntax error");
                    }
                } else if (ch == '{') { // 减少潜套，兼容android
                    lexer.nextToken();
                    Object obj = this.parseObjectNew(new JSONObject(), key);//syl--upd
                    checkMapResolve(object, key.toString());

                    if (object.getClass() == JSONObject.class) {
                        object.put(key.toString(), obj);
                    } else {
                        object.put(key, obj);
                    }

                    setContext(context, obj, key);

                    if (lexer.token() == JSONToken.RBRACE) {
                        lexer.nextToken();

                        setContext(context);
                        return object;
                    } else if (lexer.token() == JSONToken.COMMA) {
                        continue;
                    } else {
                        throw new JSONException("syntax error, " + lexer.tokenName());
                    }
                } else {
                    lexer.nextToken();
                    value = parse();
                    object.put(key, value);

                    if (lexer.token() == JSONToken.RBRACE) {
                        lexer.nextToken();
                        return object;
                    } else if (lexer.token() == JSONToken.COMMA) {
                        continue;
                    } else {
                        throw new JSONException("syntax error, position at " + lexer.pos() + ", name " + key);
                    }
                }

                lexer.skipWhitespace();
                ch = lexer.getCurrent();
                if (ch == ',') {
                    lexer.incrementBufferPosition();
                    continue;
                } else if (ch == '}') {
                    lexer.incrementBufferPosition();
                    lexer.resetStringPosition();
                    lexer.nextToken();

                    this.setContext(object, fieldName);

                    return object;
                } else {
                    throw new JSONException("syntax error, position at " + lexer.pos() + ", name " + key);
                }

            }
        } finally {
            this.setContext(context);
        }
		
	}
	
	public static Object myValueToString(Object value){
		if(value!=null){
			value=value+"";
		}
		//System.out.println(value);
		return value;
	}
}
