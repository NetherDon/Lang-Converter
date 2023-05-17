package ConvF;

public class Parser {
	public static final int _EOF = 0;
	public static final int _ident = 1;
	public static final int _number = 2;
	public static final int maxT = 13;

	static final boolean _T = true;
	static final boolean _x = false;
	static final int minErrDist = 2;

	public Token t;    // last recognized token
	public Token la;   // lookahead token
	int errDist = minErrDist;
	
	public Scanner scanner;
	public Errors errors;

	class U {
	public static void W(String x) { 
		System.out.println(x);
	}
}


	public Parser(Scanner scanner) {
		this.scanner = scanner;
		errors = new Errors();
	}

	void SynErr (int n) {
		if (errDist >= minErrDist) errors.SynErr(la.line, la.col, n);
		errDist = 0;
	}

	public void SemErr (String msg) {
		if (errDist >= minErrDist) errors.SemErr(t.line, t.col, msg);
		errDist = 0;
	}
	
	void Get () {
		for (;;) {
			t = la;
			la = scanner.Scan();
			if (la.kind <= maxT) {
				++errDist;
				break;
			}

			la = t;
		}
	}
	
	void Expect (int n) {
		if (la.kind==n) Get(); else { SynErr(n); }
	}
	
	boolean StartOf (int s) {
		return set[s][la.kind];
	}
	
	void ExpectWeak (int n, int follow) {
		if (la.kind == n) Get();
		else {
			SynErr(n);
			while (!StartOf(follow)) Get();
		}
	}
	
	boolean WeakSeparator (int n, int syFol, int repFol) {
		int kind = la.kind;
		if (kind == n) { Get(); return true; }
		else if (StartOf(repFol)) return false;
		else {
			SynErr(n);
			while (!(set[syFol][kind] || set[repFol][kind] || set[0][kind])) {
				Get();
				kind = la.kind;
			}
			return StartOf(syFol);
		}
	}
	
	void Calc0_F() {
		VarDecl();
		StatSeq();
	}

	void VarDecl() {
		Expect(3);
		Ident();
		U.W("variable "+t.val); 
		while (la.kind == 4) {
			Get();
			Ident();
			U.W("variable "+t.val); 
		}
	}

	void StatSeq() {
		Stat();
		while (la.kind == 1 || la.kind == 5) {
			Stat();
		}
	}

	void Ident() {
		Expect(1);
	}

	void Stat() {
		String e,v; 
		if (la.kind == 5) {
			Get();
			Ident();
			U.W(t.val+" @ .");   
		} else if (la.kind == 1) {
			Ident();
			v = t.val;    		
			Expect(6);
			e = Expr();
			U.W(e + " "+v+" !"); 
		} else SynErr(14);
	}

	String  Expr() {
		String  e;
		String e1,e2,op; e="";
		e1 = Term();
		e = e1;            	
		while (la.kind == 9 || la.kind == 10) {
			op = AddOp();
			e2 = Term();
			e += (" "+e2 +" "+op);  
		}
		return e;
	}

	String  Term() {
		String  e;
		String e1,e2,op; 
		e1 = Factor();
		e = e1;          
		while (la.kind == 11 || la.kind == 12) {
			op = MulOp();
			e2 = Factor();
			e += (" "+e2+" "+op);    
		}
		return e;
	}

	String  AddOp() {
		String  e;
		e="";            
		if (la.kind == 9) {
			Get();
		} else if (la.kind == 10) {
			Get();
		} else SynErr(15);
		e = t.val;       
		return e;
	}

	String  Factor() {
		String  e;
		e="";             
		if (la.kind == 1) {
			Ident();
			e = t.val+" @";   
		} else if (la.kind == 2) {
			Get();
			e = t.val;        
		} else if (la.kind == 7) {
			Get();
			e = Expr();
			Expect(8);
		} else SynErr(16);
		return e;
	}

	String  MulOp() {
		String  e;
		e="";                
		if (la.kind == 11) {
			Get();
			e = t.val;           
		} else if (la.kind == 12) {
			Get();
			e = " /";         
		} else SynErr(17);
		return e;
	}



	public void Parse() {
		la = new Token();
		la.val = "";		
		Get();
		Calc0_F();
		Expect(0);

	}

	private static final boolean[][] set = {
		{_T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x}

	};
} // end Parser


class Errors {
	public int count = 0;                                    // number of errors detected
	public java.io.PrintStream errorStream = System.out;     // error messages go to this stream
	public String errMsgFormat = "-- line {0} col {1}: {2}"; // 0=line, 1=column, 2=text
	
	protected void printMsg(int line, int column, String msg) {
		StringBuffer b = new StringBuffer(errMsgFormat);
		int pos = b.indexOf("{0}");
		if (pos >= 0) { b.delete(pos, pos+3); b.insert(pos, line); }
		pos = b.indexOf("{1}");
		if (pos >= 0) { b.delete(pos, pos+3); b.insert(pos, column); }
		pos = b.indexOf("{2}");
		if (pos >= 0) b.replace(pos, pos+3, msg);
		errorStream.println(b.toString());
	}
	
	public void SynErr (int line, int col, int n) {
		String s;
		switch (n) {
			case 0: s = "EOF expected"; break;
			case 1: s = "ident expected"; break;
			case 2: s = "number expected"; break;
			case 3: s = "\"var\" expected"; break;
			case 4: s = "\",\" expected"; break;
			case 5: s = "\"print\" expected"; break;
			case 6: s = "\"=\" expected"; break;
			case 7: s = "\"(\" expected"; break;
			case 8: s = "\")\" expected"; break;
			case 9: s = "\"+\" expected"; break;
			case 10: s = "\"-\" expected"; break;
			case 11: s = "\"*\" expected"; break;
			case 12: s = "\"/\" expected"; break;
			case 13: s = "??? expected"; break;
			case 14: s = "invalid Stat"; break;
			case 15: s = "invalid AddOp"; break;
			case 16: s = "invalid Factor"; break;
			case 17: s = "invalid MulOp"; break;
			default: s = "error " + n; break;
		}
		printMsg(line, col, s);
		count++;
	}

	public void SemErr (int line, int col, String s) {	
		printMsg(line, col, s);
		count++;
	}
	
	public void SemErr (String s) {
		errorStream.println(s);
		count++;
	}
	
	public void Warning (int line, int col, String s) {	
		printMsg(line, col, s);
	}
	
	public void Warning (String s) {
		errorStream.println(s);
	}
} // Errors


class FatalError extends RuntimeException {
	public static final long serialVersionUID = 1L;
	public FatalError(String s) { super(s); }
}
