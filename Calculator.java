import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Calculator extends Application{

    private String strToken;                        // 用于存储输入数据的字符串
    private int i = 0;                              // 读取位置
    private char present;                           // 当前被读取的字符
    private int temp = 0;                           // 运算结果
    private String digitString = "";                // 数字串
    private final char[] operator = {'+','-','*','/','(',')'};
    private final char[] digit ={'1','2','3','4','5','6','7','8','9','0'};
    private final char[] sign ={'1','2','3','4','5','6','7','8','9','0','+','-','*','/','(',')',' '};
    private boolean isError = false;

    public char getChar() {                         // 取输入的字符串中的字符
        present = strToken.charAt( i );
        i++;
        return present;
    }

    public boolean preIsDigit(char ch){             //判断是否为数字
        for(int j = 0; j < digit.length; j++){
            if(ch == digit[j])
                return true;
        }
        return false;
    }

    public boolean isSign(char ch) {                //判断是否为简单算术运算的规定字符
        for(int j = 0; j < sign.length; j++){
            if(ch == sign[j]){
                return true;
            }
        }
        return false;
    }

    public void append(){                           //将连续的数字字符连接成数字串。便于运算
         digitString = digitString + present;
    }

    public void match(char ch) {                    //判断字符是运算符，并且进行匹配
        for (int j = 0; j < operator.length; j++) {
            if (ch == operator[j]) {
                getChar();
            }
        }
    }

    public String error(){                          // 错误提示语句，在计算框中进行输出
        return "Your expression is illegal";
    }

    public int exp(){                               // 匹配运算符‘+’‘-’
        temp = term();
        while((present == '+')||(present == '-')){
            switch(present){
                case '+':
                    match( '+' );
                    temp += term();
                    break;
                case '-':
                    match( '-' );
                    temp -= term();
                    break;
            }
        }
        return temp;
    }

    public int term(){                              //匹配运算符‘*’‘/’
        temp = factor();
        while(present == '*'|| present == '/'){
            switch (present){
                case '*':
                    match( '*' );
                    temp *= factor();
                    break;
                case '/':
                    match( '/' );
                    temp /= factor();
                    break;
            }
        }
        return temp;
    }

    public int factor(){                            //匹配括号
        if(present == '('){
            match( '(' );
            temp = exp();
            match( ')' );
        }else if(Character.isDigit( present )){
                append();
                getChar();
                while(preIsDigit(present)){
                    append();
                    getChar();
                }
                temp = Integer.parseInt(digitString);
                digitString = "";
            }
        return temp;
    }
    //图形界面显示
    public void start(Stage stage){
        Scene scene = new Scene(new Group(),450, 250);
        javafx.scene.control.TextField express = new javafx.scene.control.TextField();
        TextField tfResult = new TextField();
        Button btn = new Button("计算");
        Button clr = new Button( "清空" );
        GridPane grid = new GridPane();
        tfResult.setEditable(false);
        grid.setVgap(4);
        grid.setHgap(10);
        grid.setPadding(new Insets(5,5,5,5));
        grid.add(new javafx.scene.control.Label("请输入一个表达式:"),0,0);
        grid.add( express, 1,0 );
        grid.add(btn, 2, 0);
        grid.add( new Label("结果"),0,1 );
        grid.add( tfResult, 1,1 );
        grid.add(clr,2,1);
        Group root = (Group)scene.getRoot();
        root.getChildren().add(grid);
        stage.setTitle("超级简易计算器");
        stage.setScene(scene);
        stage.show();

        //按钮触发，进行运算
        btn.setOnAction( new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                strToken = express.getText() + " ";                       //将输入的字符串取出放在strToken中
                for(int m = 0; m < strToken.length(); m++){               //判断输入的字符串是否均为合法字符
                    if(isSign(strToken.charAt(m))){
                        isError = false;
                    }
                    else{
                        isError = true;
                        break;
                    }
                }
                if(!isError){                                           //无非法字符就进行运算
                    getChar();
                    int result = exp();
                    tfResult.setText("" + result);
                    i = 0;
                    temp = 0;
                }else{                                                  //有非法字符就输出错误
                    tfResult.setText(error());
                    i = 0;
                    temp = 0;
                }
        }
        });

        clr.setOnAction( new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tfResult.setText("");
                express.setText("");
            }
        } );
    }
    public static void main(String[] args){
        Application.launch( args );
    }
}
