package stages.lexer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CharStream {
    private final String codeFile;
    private int position;
    private int column;
    private int linesCount;


    public CharStream(String filePath){
        try {
            this.codeFile = Files.readString(Paths.get(filePath));
            this.position = 0;
            this.linesCount = 1;
            this.column = 1;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void consumeNextChar(){
        //Check if the next char is a new Line mark
        if(codeFile.charAt(this.position) == '\n') {
            column = 1;
            linesCount++;
        }else {
            column++;
        }
        //Here we consume the next char
        this.position++;
    }

    public char peekNextChar() {
        return this.position >= this.codeFile.length() ? (char) -1 : this.codeFile.charAt(position);
    }

    public int getLinesCount() {
        return this.linesCount;
    }

    public String getText(int start, int stop) {
        return codeFile.substring(start, stop + 1);
    }

    public int getPosition(){
        return this.position;
    }

    public int getColumn(){
        return this.column;
    }
}
