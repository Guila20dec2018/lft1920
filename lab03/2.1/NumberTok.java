public class NumberTok extends Token {
	// ... completare ...
    public String lexeme = "";
    public NumberTok(int tag, String s) { super(tag); lexeme=s; }
    public String toString() { return "<" + tag + ", " + lexeme + ">"; }
    //public static final NumberTok
            //number = new NumberTok(Tag.NUM, "cond");

}
