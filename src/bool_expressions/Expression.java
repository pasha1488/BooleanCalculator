package bool_expressions;

import java.util.ArrayList;

public class Expression {

    //Private Methods

    private static boolean isOneOperand(String expression) {
        return expression.length() <= 5;                    //max length of operand is 5 (false)
    }

    private static boolean getFirstOperand(String expression) {
        return expression.charAt(0) == 't';                    // if first char is t (true)
    }

    private static boolean isFirstTrue(String expression) {
        return expression.charAt(0) == 't';
    }

    private static boolean isFirstFalse(String expression) {
        return expression.charAt(0) == 'f';
    }

    private static boolean isNextChar(String expression, Character character, boolean value) {
        return value ? expression.charAt(4) == character : expression.charAt(5) == character;
    }

    private static boolean parseBoolWithoutNA(String expression) {

        if (isOneOperand(expression)) return getFirstOperand(expression);
        else {
            if (isFirstTrue(expression)) {
                if (isNextChar(expression, '&', true)) return true & parseBoolWithoutNA(expression.substring(5));
                if (isNextChar(expression, '|', true)) return true | parseBoolWithoutNA(expression.substring(5));
                if (isNextChar(expression, '^', true)) return true ^ parseBoolWithoutNA(expression.substring(5));
            }
            if (isFirstFalse(expression)) {
                if (isNextChar(expression, '&', false)) return false & parseBoolWithoutNA(expression.substring(6));
                if (isNextChar(expression, '|', false)) return false | parseBoolWithoutNA(expression.substring(6));
                if (isNextChar(expression, '^', false)) return false ^ parseBoolWithoutNA(expression.substring(6));
            }
        }
        return false;
    }

    private static String parseBoolWithNA(String expression) {
        boolean t = Expression.parseBoolWithoutNA(expression.replace("NA", "true"));
        boolean f = Expression.parseBoolWithoutNA(expression.replace("NA", "false"));
        return (String) (t == f ? t : "NA");
    }

    // Public Methods

    public static String parseBool(boolean a, boolean b, char operation) {
        return String.valueOf
                ((operation == '&') ? a & b :
                        (operation == '|') ? a | b :
                                (operation == '^') ? a ^ b :
                                        (operation == '>' ? Expression.directImplication(a, b) :
                                                (operation == '<' ? Expression.converseImplication(a, b) :
                                                        (operation == '~' ? Expression.equivalence(a, b) :
                                                                (operation == 8595 ? Expression.pierceArrow(a, b) :
                                                                        (operation == '/' && Expression.shefferStroke(a, b)))))));
    }

    public static String groupParser(String expression, char operation) {

        if (operation == '!') {
            while (expression.contains("!")) {
                expression = expression.replace("!true", "false");
                expression = expression.replace("!false", "true");
            }
            return expression;
        }

        //.....................................................

        if (operation == '>') {
            int i = 0;
            int length = expression.length();

            boolean prev = false;
            boolean next = false;

            while (i < length - 2) {
                if (expression.charAt(i) == 'f') prev = false;
                if (expression.charAt(i) == 't') prev = true;

                if (expression.charAt(i) == '-' && expression.charAt(i + 1) == operation) {
                    if (expression.charAt(i + 2) == 'f') next = false;
                    if (expression.charAt(i + 2) == 't') next = true;
                    expression = expression.replace(String.valueOf(prev) + "->" + String.valueOf(next), parseBool(prev, next, operation));
                    length = expression.length();
                    i -= String.valueOf(prev).length();
                    continue;
                }
                i++;
            }
            return expression;
        }

        if (operation == '<') {
            int i = 0;
            int length = expression.length();

            boolean prev = false;
            boolean next = false;

            while (i < length - 2) {
                if (expression.charAt(i) == 'f') prev = false;
                if (expression.charAt(i) == 't') prev = true;

                if (expression.charAt(i) == operation && expression.charAt(i + 1) == '-') {
                    if (expression.charAt(i + 2) == 'f') next = false;
                    if (expression.charAt(i + 2) == 't') next = true;
                    expression = expression.replace(String.valueOf(prev) + "<-" + String.valueOf(next), parseBool(prev, next, operation));
                    length = expression.length();
                    i -= String.valueOf(prev).length();
                    continue;
                }
                i++;
            }
            return expression;
        }

        int i = 0;
        int length = expression.length();

        boolean prev = false;
        boolean next = false;

        while (i < length - 1) {
            if (expression.charAt(i) == 'f') prev = false;
            if (expression.charAt(i) == 't') prev = true;

            if (expression.charAt(i) == operation) {
                if (expression.charAt(i + 1) == 'f') next = false;
                if (expression.charAt(i + 1) == 't') next = true;
                expression = expression.replace(String.valueOf(prev) + operation + String.valueOf(next), parseBool(prev, next, operation));
                length = expression.length();
                i -= String.valueOf(prev).length();
                continue;
            }
            i++;
        }
        return expression;
    }

    public static String getResultOfExpression(String expression) {

        int numberOfPairsOfBrackets = 0;
        try {
            numberOfPairsOfBrackets = Validator.numberOfPairsOfBrackets(expression);
        } catch (Exception e) {
            return "Помилка";
        }

        if (numberOfPairsOfBrackets != 0) {
            if (numberOfPairsOfBrackets == 1 && expression.charAt(0) == '(' && expression.charAt(expression.length() - 1) == ')')
                return getResultOfExpression(expression.substring(1, expression.length() - 1));
            int lastLeft = 0;
            int i = 0;
            int length = expression.length();
            String cutExp;
            while (i < length) {
                if (expression.charAt(i) == '(') lastLeft = i;
                if (expression.charAt(i) == ')') {
                    cutExp = expression.substring(lastLeft, i + 1);
                    expression = expression.replace(cutExp, Expression.getResultOfExpression(cutExp));
                    i = 0;
                    length = expression.length();
                    continue;
                }
                i++;
            }
            return Expression.getResultOfExpression(expression);
        }

        if (expression.contains("NA")) {
            if (expression.length() <= 2) return "NA";
            else {
                boolean t = Boolean.parseBoolean(Expression.getResultOfExpression(expression.replace("NA", "true")));
                boolean f = Boolean.parseBoolean(Expression.getResultOfExpression(expression.replace("NA", "false")));
                return (t == f ? String.valueOf(t) : "NA");
            }
        }

        if (expression.length() <= 5) {
            return String.valueOf(expression.charAt(0) == 't');
        }

        expression = groupParser(expression, '!');
        expression = groupParser(expression, '/');
        expression = groupParser(expression, (char) 8595);
        expression = groupParser(expression, '&');
        expression = groupParser(expression, '|');
        expression = groupParser(expression, '^');
        expression = groupParser(expression, '>');
        expression = groupParser(expression, '<');
        expression = groupParser(expression, '~');

        return expression;

//        s = s.replaceAll("!true", "false");
//        s = s.replaceAll("!false", "true");
//
//        s = s.replaceAll("true&true", "true");
//        s = s.replaceAll("true&false", "false");
//        s = s.replaceAll("false&true", "false");
//        s = s.replaceAll("false&false", "false");
//
//        s = s.replaceAll("true|true", "true");
//        s = s.replaceAll("true|false", "true");
//        s = s.replaceAll("false|true", "true");
//        s = s.replaceAll("false|false", "false");
//
//        s = s.replaceAll("true^true", "false");
//        s = s.replaceAll("true^false", "true");
//        s = s.replaceAll("false^true", "true");
//        s = s.replaceAll("false^false", "false");
//
//        s = s.replaceAll("true~true", "true");
//        s = s.replaceAll("true~false", "false");
//        s = s.replaceAll("false~true", "false");
//        s = s.replaceAll("false~false", "true");
//
//        s = s.replaceAll("true->true", "true");
//        s = s.replaceAll("true->false", "true");
//        s = s.replaceAll("false->true", "false");
//        s = s.replaceAll("false->false", "true");
//
//        s = s.replaceAll("true<-true", "true");
//        s = s.replaceAll("true<-false", "false");
//        s = s.replaceAll("false<-true", "true");
//        s = s.replaceAll("false<-false", "true");
//
//        return s;

    }

    public static String parseBool(String expression) {

        ArrayList<Character> list = new ArrayList<>();
        list.add('f');
        list.add('a');
        list.add('l');
        list.add('s');
        list.add('e');
        list.add('t');
        list.add('r');
        list.add('u');
        list.add('&');
        list.add('|');
        list.add('^');

        for (int i = 0; i < expression.length(); i++) {
            if (!list.contains(expression.charAt(i))) {
                try {
                    throw new Exception();
                } catch (Exception e) {
                    System.err.println("The method only supports operations such as &, |, ^");
                }
                return null;
            }
        }

        if (!expression.isEmpty()) {
            if (expression.contains("NA")) return parseBoolWithNA(expression);
            else return String.valueOf(parseBoolWithoutNA(expression));
        }
        return "false";
    }

    public static String replaceToBoolExpression(ArrayList<Converter> list, String expression) {
        String res = expression;

        for (Converter converter : list) {
            res = res.replaceAll(converter.getVariable(), converter.getReplacer());
        }

        return res;
    }

    // Boolean Methods

    public static boolean directImplication(boolean a, boolean b) {
        return !(a && !b);
    }

    public static boolean converseImplication(boolean a, boolean b) {
        return !(!a && b);
    }

    public static boolean equivalence(boolean a, boolean b) {
        return a == b;
    }

    public static boolean pierceArrow(boolean a, boolean b) {
        return (!a && !b);
    }

    public static boolean shefferStroke(boolean a, boolean b) {
        return !(a && b);
    }

}
