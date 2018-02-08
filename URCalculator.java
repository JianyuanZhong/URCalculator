import java.text.NumberFormat;
import java.util.*;
import java.util.Map.Entry;
public class URCalculator {
	private Stack<String> stack =new Stack<>();
	private HashMap<String,String> map= new HashMap<>();

	public URCalculator(){
		run();
	}
	
	public ArrayList<String> Tokenizer(String input){
		input=input.replaceAll("\\s+", "");
		String[] buffer=input.split("");
		ArrayList<String> tokens=new ArrayList<>();
		String temp = "";
		int count=0;
		int pointer=0;
		for (int i=0;i<buffer.length; i++){
			if(count==1) pointer=tokens.size()-3;
			if(buffer[i].equals("+")||buffer[i].equals("-")){
				if(i==0) 
					tokens.add("0");
				
				else if(buffer[i-1].equals("+")||buffer[i-1].equals("-")||buffer[i-1].equals("=")
						||buffer[i-1].equals("(")||buffer[i-1].equals("[")||buffer[i-1].equals("{")){
					tokens.add("0");
					if(buffer[i-1].equals("-")) 
						count++;
//					if(buffer[i-1].equals("+")&&buffer[i].equals("-")) 
//						buffer[i]="+";
				}
			}else if(count>0){
				if(buffer[i-1].equals("-")) 
					count++;
				if(count%2==0) {
					if(tokens.get(pointer).equals("+"))
						tokens.set(tokens.size()-1, "+");
					else
						tokens.set(tokens.size()-1, "+");
				}
				else {
					if(tokens.get(pointer).equals("+"))
						tokens.set(tokens.size()-1, "-");
					else
						tokens.set(tokens.size()-1, "-");
				
				}
				count=0;
			}
			
			if(! isOperator(buffer[i].charAt(0))){
				if(!buffer[i].equals(" "))
					temp+=buffer[i];

			}else{
				if(!temp.equals("")){
					tokens.add(temp);
				}
				tokens.add(buffer[i]);
				temp="";
			}
		}
		if(!temp.equals("")) tokens.add(temp);

		return tokens;
	}

	public boolean isOperator(char c){
		return c=='+'||c=='-'||c=='*'||c=='/'||c=='^'||c=='('||c==')'||
				c=='{'||c=='}'||c=='[' ||c==']'|| c=='=';
	}

	public boolean isHigherPrecedence(String curr,String prev){
		if(prev.equals("=")) 
			return true;
		if((curr.equals("+")||curr.equals("-"))&&(prev.equals("+")||prev.equals("-"))) 
			return false;
		else if((curr.equals("*")||curr.equals("/"))&&(prev.equals("+")||prev.equals("-"))) 
			return true;
		else if(curr.equals("^")&&(prev.equals("+")||prev.equals("-")||prev.equals("*")||prev.equals("/"))) 
			return true;
		else if(curr.equals("(")||curr.equals("[")||curr.equals("{")||curr.equals("=")) 
			return true;
		else if(prev.equals("(")||prev.equals("[")||prev.equals("{")) 
			return true;

		return false;
	}

	public ArrayList<String> toPostfix(ArrayList<String> tokens){
		ArrayList<String> postfix=new ArrayList<>();

		for(String i: tokens){
			if(!i.equals("")){
				if(isOperator(i.charAt(0))){
					if(stack.empty()||isHigherPrecedence(i,stack.peek())){
						stack.push(i);
					}else if(i.equals("}")){
						while(!stack.peek().equals("{")) 
							postfix.add(stack.pop());
						stack.pop();
					}else if(i.equals("]")){
						while(!stack.peek().equals("[")) 
							postfix.add(stack.pop());
						stack.pop();
					}else if(i.equals(")")){
						while(!stack.peek().equals("(")) 
							postfix.add(stack.pop());
						stack.pop();
					}else {

						while(!stack.empty()) {
							if(isHigherPrecedence(i,stack.peek())) 
								break;
							else 
								if(!i.equals("(")&&!i.equals(")")&&!i.equals("{")&&!i.equals("}")&&!i.equals("[")&&!i.equals("]"))
									postfix.add(stack.pop());
						}

						stack.push(i);
					}
				}else{
					postfix.add(i);

				}
			}
		}
		while (!stack.empty())
			postfix.add(stack.pop());
		return postfix;
	}

	public String evaluate(ArrayList<String> posfix){
		Stack<String> st=new Stack<>();
		Iterator<String> itr= posfix.iterator();
		String result="";
		while(itr.hasNext()){
			String temp=itr.next();
			if(!temp.equals("(")&&!temp.equals(")")&&!temp.equals("[")&&!temp.equals("]")&&!temp.equals("{")&&!temp.equals("}")){
				if(!isOperator(temp.charAt(0))){
					st.push(temp);
				}else if(temp.equals("=")){
					String val1=st.pop();
					String val2=st.peek();
					try{
						Double.parseDouble(val2);
						System.out.print("Do not assign value to numbers!");
						map.put(val1, val1);
					}catch(NumberFormatException e){
						map.put(val2, val1);
					}
				}else{
					Double val1=0.0;
					Double val2=0.0;
					String a="";

					if(st.peek().charAt(0)=='0'||st.peek().charAt(0)=='1'||st.peek().charAt(0)=='2'||st.peek().charAt(0)=='3'||st.peek().charAt(0)=='4'||st.peek().charAt(0)=='5'||st.peek().charAt(0)=='6'||st.peek().charAt(0)=='7'||st.peek().charAt(0)=='8'||st.peek().charAt(0)=='9'){
						val1=Double.parseDouble(st.pop());

					}else{
						a=st.pop();
						while(map.get(a)!=null){
							a=map.get(a);
						}
						val1=Double.parseDouble(a);
					}

					if(st.peek().charAt(0)=='0'||st.peek().charAt(0)=='1'||st.peek().charAt(0)=='2'||st.peek().charAt(0)=='3'||st.peek().charAt(0)=='4'||st.peek().charAt(0)=='5'||st.peek().charAt(0)=='6'||st.peek().charAt(0)=='7'||st.peek().charAt(0)=='8'||st.peek().charAt(0)=='9'){
						val2=Double.parseDouble(st.pop());

					}else{
						a=st.pop();
						while(map.get(a)!=null){
							a=map.get(a);
						}
						val2=Double.parseDouble(a);
					}

					if(temp.equals("+")) st.push(val2+val1+"");
					if(temp.equals("-")) st.push(val2-val1+"");
					if(temp.equals("*")) st.push(val2*val1+"");
					if(temp.equals("/")) st.push(val2/val1+"");
					if(temp.equals("^")) st.push(Math.pow(val2, val1)+"");

				}
			}
		}
		return st.pop();
	}

	public void run(){
		System.out.println("::::::::::::::::::::::::::URCalculator::::::::::::::::::::::::::");
		System.out.print("\nOperators: =,+,-,*,/,^ \n\n'=' will asign whatever on the right to the left charactor at all time."+"\n\n\n>>>> ");
		try{
		Scanner sc= new Scanner(System.in);
		String input="0";
		
		input=sc.nextLine();
		while(!input.equals("exit")){
			
			if(input.split("\\s+")[0].equals("clear")){
				
				if(input.split("\\s+")[1].equals("all")) {
					map.clear();
					System.out.println("\n\n\nAll assigned value has been cleared!");
				}else {
					map.remove(input.split("\\s+")[1]);
					System.out.println("\n\n\n"+input.split("\\s+")[1]+"'s value has been cleared.");
				}
				System.out.print(">>>> ");
				input=sc.nextLine();
			}else{
				
				
				String temp=evaluate(toPostfix(Tokenizer(input)));
				while(map.get(temp)!=null){
					temp=map.get(temp);
				}
			Double.parseDouble(temp);
			System.out.println("\n\n\nans: "+ temp+"\n\n\n");
			System.out.print(">>>> ");
			input=sc.nextLine();
			}
			
		}
		System.out.println("\n\n\nQuited!");
		}catch(NumberFormatException e){
			System.out.println("\n\n\nUnknow Charactor！ ");
			run();
		}catch(EmptyStackException e){
			System.out.println("Please check your Syntx!");
			run();
		}
	}

	public static void main(String[] args){
		URCalculator cal= new URCalculator();
	}



}
