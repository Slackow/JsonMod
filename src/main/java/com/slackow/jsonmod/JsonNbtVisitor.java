package com.slackow.jsonmod;

import net.minecraft.nbt.*;

import java.math.BigInteger;

public class JsonNbtVisitor {
    public Tag visitObject(JsonParser.ObjectContext ctx) {
        CompoundTag compoundTag = new CompoundTag();
        for (JsonParser.MemberContext memberContext : ctx.member()) {
            compoundTag.put(parseString(memberContext.STRING().getText()), visitValue(memberContext.value()));
        }
        return compoundTag;
    }

    public Tag visitArray(JsonParser.ArrayContext ctx) {
        ListTag tagList = new ListTag();
        for (JsonParser.ValueContext valueContext : ctx.value()) {
            tagList.add(visitValue(valueContext));
        }
        return tagList;
    }

    public Tag visitNumber(JsonParser.NumberContext ctx) {
        if (ctx.FRACTION() != null || ctx.EXPONENT() != null) {
            return DoubleTag.valueOf(Double.parseDouble(ctx.getText()));
        } else {
            BigInteger number = new BigInteger(ctx.getText());
            int bitLength = number.bitLength();
            if (bitLength < 32) {
                return IntTag.valueOf(number.intValue());
            } else {
                return LongTag.valueOf(number.longValueExact());
            }
        }
    }

    public Tag visitJson_file(JsonParser.Json_fileContext ctx) {
        return visitValue(ctx.value());
    }

    public String parseString(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < s.length() - 1; i++) {
            if (s.charAt(i) == '\\') {
                i++;
                if (s.charAt(i) == 'u') {
                    sb.append(Integer.parseUnsignedInt(s.substring(i, i + 4), 16));
                    i += 4;
                    continue;
                }
                switch (s.charAt(i)) {
                    case '\\' -> sb.append('\\');
                    case '"' -> sb.append('"');
                    case '/' -> sb.append('/');
                    case 'f' -> sb.append('\f');
                    case 'r' -> sb.append('\r');
                    case 'n' -> sb.append('\n');
                    case 't' -> sb.append('\t');
                    case 'b' -> sb.append('\b');
                }
            } else {
                sb.append(s.charAt(i));
            }
        }
        return sb.toString();
    }

    public Tag visitValue(JsonParser.ValueContext ctx) {
        return switch (ctx.getText()) {
            case "null" -> throw new IllegalArgumentException("No null allowed!");
            case "true" -> ByteTag.valueOf(true);
            case "false" -> ByteTag.valueOf(false);
            case String s when s.charAt(0) == '"' -> StringTag.valueOf((parseString(s)));
            default -> {
                if (ctx.array() != null) {
                    yield visitArray(ctx.array());
                } else if (ctx.object() != null) {
                    yield visitObject(ctx.object());
                } else {
                    yield visitNumber(ctx.number());
                }
            }
        };
    }
}
