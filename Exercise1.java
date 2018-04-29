import java.io.*;
import java.util.*;

public class Exercise1 {
    private StringBuffer buffer = new StringBuffer();//缓冲区，用来存放临时字母
    private String strToken;   //字符数组，用来存放构成单词符号的字符串
    private int i = 0;  //扫描的光标位置
    private char ch;
    //保留字、关键字数组
    private final String[] ReserveWords = {"void","char","int","float","double","short","long","signed","unsigned","struct",
            "union","enum","typedef","sizeof","auto","static","register","extern","const","volatile","return",
            "continue","break","goto","if","else","switch","case","default","for","do","while"};

    //无参构造函数
    public int getI(){
        return i + 1;
    }

    public Exercise1(){
    }
    //构造函数
    public Exercise1(String fileSrc){
        readFile(buffer, fileSrc);
    }
    //判断是否是保留字
    public boolean isReserveWord(String s) {
        for (int i = 0; i < ReserveWords.length; i++) {
            if (ReserveWords[i].equals(s))
                return true;
        }
        return false;
    }
    //判断是否是字符"/"
    public boolean isNote1(char c) {
        if(c == '/'){
            return true;
        }
        return false;
    }
    //判断是否是字符"*"
    public boolean isNote2(char c) {
        if(c == '*'){
            return true;
        }
        return false;
    }
    //判断是否是字母
    public boolean isLetter(char ch){
        return Character.isLetter(ch);
    }

    //读文件
    int rows= 1;

    public boolean readFile(StringBuffer buffer, String fileSrc) {
        try {
            FileReader fileReader = new FileReader(fileSrc);
            BufferedReader br = new BufferedReader(fileReader);
            String tmp = null;
            while((tmp = br.readLine())!= null) {
                buffer.append(tmp);
                buffer.append("\r\n");
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //写文件
    public boolean writeFile(String out) throws IOException {
       try{
           File file = new File("./src/output.txt");
           if(!file.exists()){
               file.createNewFile();
           }
           FileWriter fout = new FileWriter(file.getAbsoluteFile(),true);
           BufferedWriter bw = new BufferedWriter(fout);
           bw.write(out);
           bw.close();
           return true;
       }catch(IOException e){
           e.printStackTrace();
           return true;
       }
    }
    //清空输出文件内容
    public boolean clearFile() throws IOException {
        try{
            File file = new File("./src/output.txt");
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fout = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fout);
            bw.write("");
            bw.close();
            return true;
        }catch(IOException e){
            e.printStackTrace();
            return true;
        }
    }

    //将下一个字符读到ch中，光标随之向后移动一个字符
    public void getChar(){
        ch = buffer.charAt(i);
        i++;
    }
    //将字母添加到strToken之后
    public void append(){
        strToken = strToken + ch;
    }

    //回退操作，保证光标的正确位置
    public void rollback(){
        i--;
        ch = ' ';
    }
    //将Token清空，以便放下一个字符串
    public void clearToken(){
        strToken = "";
    }

    public void transfer() throws IOException {
        clearToken();
        clearFile();
        while(i < buffer.length()){
            getChar();
            //代码中保留字与标识符识别
            if(isLetter(ch)){
                while(isLetter(ch)) {    //如果为字母，则将它添加到strToken中
                    append();
                    getChar();
                }
                rollback();
                if(isReserveWord(strToken)){
                    writeFile(strToken.toUpperCase());
                }
                else{
                    writeFile(strToken.toLowerCase());
                }
                clearToken();    //将strToken清空
            }
            else if(isNote1(ch)) {         //注释中字母大写
                append();
                getChar();
                //多行注释
                if (isNote2(ch)) {
                    append();
                    getChar();
                    while (!isNote2(ch)) {
                        append();
                        getChar();
                        if (isNote2(ch)) {
                            append();
                            getChar();
                            if (isNote1(ch)) {
                                append();
                                getChar();
                                break;
                            }
                        }
                    }
                    writeFile(strToken.toUpperCase());
                    clearToken();
                }
                //单行注释
                if (isNote1(ch)) {
                    append();
                    getChar();
                    while (ch != '\n') {
                        append();
                        getChar();
                    }
                    writeFile(strToken.toUpperCase());
                    clearToken();
                }
                rollback();
            }    //非 "/"、 "*"、字母
            else {
                append();
                writeFile(strToken);
                clearToken();
            }
        }
    }
    public static void main(String[] args) throws IOException {
        Exercise1 test = new Exercise1("./src/input.txt");
        test.transfer();
    }
}

