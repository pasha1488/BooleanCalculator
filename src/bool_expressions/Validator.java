package bool_expressions;

import java.util.ArrayList;

public class Validator {

    private static ArrayList<Character> brackets = new ArrayList<>();
    private static ArrayList<Character> validChars = new ArrayList<>();
    private static ArrayList<Character> variables = new ArrayList<>();

    static {
        brackets.add('(');
        brackets.add(')');
        addVariable('A');
        addVariable('B');
        addVariable('C');
        addValidChar('!');
        addValidChar('&');
        addValidChar('/');
        addValidChar((char) 8595);
        addValidChar('|');
        addValidChar('^');
        addValidChar('-');
        addValidChar('>');
        addValidChar('<');
        addValidChar('~');
    }

    private static boolean isVariable(char symbol) {
        return variables.contains(symbol);
    }

    private static boolean isChar(char symbol) {
        return validChars.contains(symbol);
    }

    private static boolean isBracket(char symbol) {
        return brackets.contains(symbol);
    }

    private static boolean isValidBracket(String text, char symbol) {
        if (isBracket(symbol)) {
            if (symbol == ')') {
                int left = 0;
                int right = 0;
                for (int i = 0; i < text.length(); i++) {
                    if (text.charAt(i) == '(') left++;
                    if (text.charAt(i) == ')') right++;
                }
                if (left <= right) return false;
            }
            if (!text.isEmpty()) {
                char prevChar = text.charAt(text.length() - 1);
                return isChar(prevChar) && symbol != ')' || prevChar == '(' || prevChar == symbol || isVariable(prevChar) && symbol == ')';
            }
            return symbol == '(';
        }
        return false;
    }

    private static boolean isValidChar(String text, char symbol) {
        if (isChar(symbol)) {
            if (symbol == '!') {
                if (!text.isEmpty()) {
                    char prevChar = text.charAt(text.length() - 1);
                    return !isVariable(prevChar) && (isChar(prevChar) || prevChar == '(');
                }
                return (text.isEmpty());
            }
            if (!text.isEmpty()) {
                char prevChar = text.charAt(text.length() - 1);
                return isVariable(prevChar) || prevChar == ')';
            }
        }
        return false;
    }

    private static boolean isValidVariable(String text, char symbol) {
        if (isVariable(symbol)) {
            if (!text.isEmpty()) {
                char prevChar = text.charAt(text.length() - 1);
                return prevChar == '(' || isChar(prevChar) || prevChar == '↓' || prevChar == (char) 8595;

            }
            return (text.isEmpty());
        }
        return false;
    }

    //..................................................................................................................

    public static void addVariable(char variable) {
        Validator.variables.add(variable);
    }

    public static void addValidChar(char ch) {
        Validator.validChars.add(ch);
    }

    public static boolean isValid(String text, char symbol) {
        return isValidChar(text, symbol) || isValidBracket(text, symbol) || isValidVariable(text, symbol);
    }

    public static boolean isValidExpression(String expression) {
        if (expression.isEmpty()) return false;
        if (isChar(expression.charAt(expression.length() - 1))) return false;
        if (expression.length() == 1 && (expression.charAt(0) == '!' || expression.charAt(0) == '(')) return false;
        for (int i = 0; i < expression.length(); i++) {
            if (!isBracket(expression.charAt(i)) && !isChar(expression.charAt(i)) && !isVariable(expression.charAt(i)))
                return false;
        }
        try {
            int a = numberOfPairsOfBrackets(expression);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static int numberOfPairsOfBrackets(String expression) throws Exception {
        int left = 0;
        int right = 0;
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '(') {
                left++;
                if (i + 1 < expression.length() && expression.charAt(i + 1) == ')')
                    throw new Exception();
            }
            if (expression.charAt(i) == ')') right++;
        }
        if (left != right)
            throw new Exception();

        return left;
    }

}
