package sample;

import bool_expressions.Converter;
import bool_expressions.Expression;
import bool_expressions.Validator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;

public class Controller {

    private final ObservableList<String> logicalTypes = FXCollections.observableArrayList("true", "false", "NA");

    @FXML
    private Button btnA;
    @FXML
    private Button btnB;
    @FXML
    private Button btnC;
    @FXML
    private Button btnInversion;
    @FXML
    private Button btnShefferStroke;
    @FXML
    private Button btnPierceArrow;
    @FXML
    private Button btnAnd;
    @FXML
    private Button btnOr;
    @FXML
    private Button btnXor;
    @FXML
    private Button btnDirectImplication;
    @FXML
    private Button btnConverseImplication;
    @FXML
    private Button btnEquivalence;
    @FXML
    private Button btnLeftBrace;
    @FXML
    private Button btnRightBrace;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnResult;
    @FXML
    private Button btnBackspace;
    @FXML
    private TextField field;
    @FXML
    private TextArea area;
    @FXML
    private Label label;
    @FXML
    private ChoiceBox<String> choiceBoxA;
    @FXML
    private ChoiceBox<String> choiceBoxB;
    @FXML
    private ChoiceBox<String> choiceBoxC;

    private void printSymbol(String text, char symbol) {
        String ch = String.valueOf(symbol == '>' ? "->" : String.valueOf(symbol == '<' ? "<-" : symbol));
        if (Validator.isValid(text, symbol)) {
            field.appendText(String.valueOf(ch));
            Main.log.info("Успішна валідація " + ch);
        } else Main.log.info("Не успішна валідація " + ch);
    }

    @FXML
    void initialize() {
        Main.log.info("Початок роботи програми");

        choiceBoxA.setItems(logicalTypes);
        choiceBoxB.setItems(logicalTypes);
        choiceBoxC.setItems(logicalTypes);
        choiceBoxA.setValue("true");
        choiceBoxB.setValue("false");
        choiceBoxC.setValue("NA");

        label.setOnMouseClicked(event2 -> {
            Main.formInfo.show();
            Main.log.info("Відкриття таблиці істинності");
        });

        btnA.setOnAction(event -> printSymbol(field.getText(), 'A'));

        btnB.setOnAction(event -> printSymbol(field.getText(), 'B'));

        btnC.setOnAction(event -> printSymbol(field.getText(), 'C'));

        btnInversion.setOnAction(event1 -> printSymbol(field.getText(), '!'));

        btnShefferStroke.setOnAction(event1 -> printSymbol(field.getText(), '/'));

        btnPierceArrow.setOnAction(event1 -> printSymbol(field.getText(), (char) 8595));

        btnAnd.setOnAction(event -> printSymbol(field.getText(), '&'));

        btnOr.setOnAction(event -> printSymbol(field.getText(), '|'));

        btnXor.setOnAction(event -> printSymbol(field.getText(), '^'));

        btnDirectImplication.setOnAction(event -> printSymbol(field.getText(), '>'));

        btnConverseImplication.setOnAction(event -> printSymbol(field.getText(), '<'));

        btnEquivalence.setOnAction(event -> printSymbol(field.getText(), '~'));

        btnLeftBrace.setOnAction(event -> printSymbol(field.getText(), '('));

        btnRightBrace.setOnAction(event -> printSymbol(field.getText(), ')'));

        btnBackspace.setOnAction(event1 -> {
            Main.log.info("Натискання Backspace");
            field.setText(field.getText().isEmpty() ? "" : field.getText().substring(0, field.getText().length() - 1));
        });

        btnResult.setOnAction(event -> {
            Main.log.info("Натискання Result");
            ArrayList<Converter> list = new ArrayList<>();
            list.add(new Converter("A", choiceBoxA.getValue()));
            list.add(new Converter("B", choiceBoxB.getValue()));
            list.add(new Converter("C", choiceBoxC.getValue()));

            if (!field.getText().isEmpty())
                if (Validator.isValidExpression(field.getText()))
                    area.appendText(Expression.getResultOfExpression(Expression.replaceToBoolExpression(list, field.getText())) + "\n");
                else area.appendText("Помилка" + "\n");
        });

        btnClear.setOnAction(event -> {
            Main.log.info("Натискання Clear");
            field.clear();
            area.clear();
        });


    }
}
