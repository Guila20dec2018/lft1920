import java.io.*; 
import java.util.*;

public class Lexer {

    public static int line = 1;
    private char peek = ' ';
    
    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }

    private Token matchKeyWord(String s) {
        return switch (s) {
            case "cond" -> Word.cond;
            case "when" -> Word.when;
            case "then" -> Word.then;
            case "else" -> Word.elsetok;
            case "while" -> Word.whiletok;
            case "do" -> Word.dotok;
            case "seq" -> Word.seq;
            case "print" -> Word.print;
            case "read" -> Word.read;
            default -> null;
        };
    }

    public Token lexical_scan(BufferedReader br) {
        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r') {
            if (peek == '\n') line++;
            readch(br);
        }
        
        switch (peek) {
            case '!':
                peek = ' ';
                return Token.not;

	// ... gestire i casi di (, ), {, }, +, -, *, /, ; ... //

            case '(':
                peek = ' ';
                return Token.lpt;

            case ')':
                peek = ' ';
                return Token.rpt;

            case '{':
                peek = ' ';
                return Token.lpg;

            case '}':
                peek = ' ';
                return Token.rpg;

            case '+':
                peek = ' ';
                return Token.plus;

            case '-':
                peek = ' ';
                return Token.minus;

            case '*':
                peek = ' ';
                return Token.mult;

            case '/':
                peek = ' ';
                return Token.div;

            case ';':
                peek = ' ';
                return Token.semicolon;

            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character"
                            + " after & : "  + peek );
                    return null;
                }

	        // ... gestire i casi di ||, <, >, <=, >=, ==, <>, = ... //
            case '|':
                readch(br);
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    System.err.println("Erroneous character"
                            + " after | : "  + peek );
                    return null;
                }

            case '<':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.le;
                } else if (peek == '>'){
                    peek = ' ';
                    return Word.ne;
                } else {
                    //peek = ' ';//non sono sicuro se devo mettere anche qua, forse salta un carattere quello che h appena letto
                    //tolto perche voglio annalisare anche il nuovo carattere che ho letto
                    return Word.lt;
                }

            case '>':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.ge;
                } else {
                    return Word.gt;
                }

            case '=':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.eq;
                } else {
                    return Token.assign;
                }
          
            case (char)-1:
                return new Token(Tag.EOF);

            default:
                // se una parola chiave inizia subito dopo un identificatore non lo prendo!!!
                if (Character.isLetter(peek) ) {
                    // ... gestire il caso degli identificatori e delle parole chiave //
                    //System.out.println("Character: " + peek);
                    StringBuilder word = new StringBuilder(String.valueOf(peek));
                    //confrontare con le parole chiavi
                    Token word1 = matchKeyWord(String.valueOf(word));
                    if (word1 != null) {
                        peek = ' ';
                        return word1;
                    }
                    readch(br);
                    while (Character.isLetter(peek) || Character.isDigit(peek) || peek == '_') {
                        word.append(peek);
                        word1 = matchKeyWord(String.valueOf(word));
                        if (word1 != null) {
                            peek = ' ';
                            return word1;
                        }
                        readch(br);
                    }
                    return new Word(Tag.ID, String.valueOf(word));

                } else if (peek == '_') {
                    StringBuilder word = new StringBuilder(String.valueOf(peek));
                    readch(br);
                    while (peek == '_') {
                        word.append(peek);
                        readch(br);
                    }
                    if (Character.isLetter(peek) || Character.isDigit(peek)) {
                        word.append(peek);
                        readch(br);
                        while (Character.isLetter(peek) || Character.isDigit(peek) || peek == '_') {
                            word.append(peek);
                            readch(br);
                        }
                        return new Word(Tag.ID, String.valueOf(word));
                    }
                    else {
                        System.err.println("NOT valid identifier: "
                                + word  + peek );
                        return null;
                    }

                } else if (Character.isDigit(peek)) {
	                // ... gestire il caso dei numeri ... //
                    StringBuilder number = new StringBuilder(String.valueOf(peek));
                    readch(br);
                    while (Character.isDigit(peek)) {
                        number.append(peek);
                        readch(br);
                    }
                    //System.out.println("Number: " + number);
                    //peek = ' ';// se il prossimo character e' diverso di numero lo sovrascrivo con questa operazione
                    return new NumberTok(Tag.NUM, String.valueOf(number));

                } else {
                        System.err.println("Erroneous character: " 
                                + peek );
                        return null;
                }
        }
    }
		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "C:\\Users\\guila\\Documents\\LFT1920\\lablft1920\\lab03\\2.1\\input.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do {
                tok = lex.lexical_scan(br);
                if (tok != null)
                    System.out.println("Scan: " + tok);
                else
                    System.out.println("Error!");

            } while (tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {e.printStackTrace();}    
    }

}
