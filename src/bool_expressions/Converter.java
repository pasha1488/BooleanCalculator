package bool_expressions;

public class Converter {

    private String variable;
    private String replacer;

    public Converter(String variable, String replacer) {
        this.variable = variable;
        this.replacer = replacer;
    }

    public String  getVariable() {
        return variable;
    }

    public String getReplacer() {
        return replacer;
    }
}
