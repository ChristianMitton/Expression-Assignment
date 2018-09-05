package expr_eval;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structure.Stack;

public class Expression {

	/**
	 * Expression to be evaluated
	 */
	String expr;                
    
	/**
	 * Populates the scalars list with simple scalar variables
	 * Scalar characters in the expression 
	 */
	ArrayList<ScalarVariable> scalars;   
	
	/**
	 * Populates the arrays list with simple array variables
	 * Array characters in the expression
	 */
	ArrayList<ArrayVariable> arrays;
    
    /**
     * String containing all delimiters (characters other than variables and constants), 
     * to be used with StringTokenizer
     */
    public static final String delims = " \t*+-/()[]";
    
    /**
     * Initializes this Expression object with an input expression. Sets all other
     * fields to null.
     * 
     * @param expr Expression
     */
    public Expression(String expr) {
        this.expr = expr;
    }
    
    private int numberOfOpenBracketsTheTokenContains(String token) {
		int numOfOpenBrackets = 0;
		for(int indexPtr = 0; indexPtr < token.length(); indexPtr++) {
			if(token.charAt(indexPtr) == '[') {
				numOfOpenBrackets++;
			}
		}
		return numOfOpenBrackets;
	}

    /**
     * Populates the scalars and arrays lists with characters for scalar and array
     * variables in the expression. For every variable, a SINGLE character is created and stored,
     * even if it appears more than once in the expression.
     * At this time, values for all variables are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     */
    public void buildVariable() {
    		/** COMPLETE THIS METHOD **/
    		/** DO NOT create new scalars and arrays **/
    		arrays = new ArrayList<ArrayVariable>();
    		scalars = new ArrayList<ScalarVariable>();
		
		String delimsNoOpenBrackets = " \t*+-/()]";		
		String lettersToBecomeArrayVar = "";
		
		int openBracketsPassed = 0;
		
		StringTokenizer tokens = new StringTokenizer(expr, delimsNoOpenBrackets);
		
		/*---------------------------
		  find arrayVariables first 
		  ---------------------------*/
		while(tokens.hasMoreTokens()) {
			//keep track of currentToken
			String token = tokens.nextToken();
			
			//inspect token to see if it contains only 1 open bracket.
			//if it contains only 1 open bracket, all letters from start of token to before open bracket makeup an arrayVariable
			//if the arrayVariable isn't in the list already, add it to the list
			if(numberOfOpenBracketsTheTokenContains(token) == 1) {
				for(int indexPtr = 0; token.charAt(indexPtr) != '['; indexPtr++) {
					lettersToBecomeArrayVar += token.charAt(indexPtr);
				}
				if(!arrays.contains(new ArrayVariable(lettersToBecomeArrayVar))) {
					arrays.add(new ArrayVariable(lettersToBecomeArrayVar));	
				}
				lettersToBecomeArrayVar = "";
				
			} 
			
			//inspect token to see if it contains multiple open brackets.
			if(numberOfOpenBracketsTheTokenContains(token) > 1) {
				//if it contains multiple brackets, all letters from start of token to before 1st open bracket makeup an arrayVariable
				int indexPtr = 0;
				for(indexPtr = 0; token.charAt(indexPtr) != '['; indexPtr++) {
					lettersToBecomeArrayVar += token.charAt(indexPtr);
				}
				if(!arrays.contains(new ArrayVariable(lettersToBecomeArrayVar))) {
					arrays.add(new ArrayVariable(lettersToBecomeArrayVar));	
				}
				lettersToBecomeArrayVar = "";
				//Now the first arrayVariable has been gotten
				//indexPtr's value is the first open bracket
				
				//Mark 1 bracket to have been passed thus far
				openBracketsPassed++;
				
				//when you get to the case where you encounter an open bracket, letters, and then another open bracket,
				//the letters inbetween brackets makeup an arrayVariable
				for(indexPtr = indexPtr+1; indexPtr < token.length() ; indexPtr++) {
					//on first iteration, indexPtr points to letter after first open bracket. Don't have to worry about operators or spaces cause of tokenizer					
					if(Character.isLetter(token.charAt(indexPtr))) {
						lettersToBecomeArrayVar += token.charAt(indexPtr);
					}
					if(token.charAt(indexPtr) == '[') {
						//if the ptr points to another open bracket, add letters before it to array varaibles
						if(!arrays.contains(new ArrayVariable(lettersToBecomeArrayVar))) {
							arrays.add(new ArrayVariable(lettersToBecomeArrayVar));	
						}
						lettersToBecomeArrayVar = "";
						openBracketsPassed++;
					}
					//if all open brackets have been passed, exit loop
					if(openBracketsPassed == numberOfOpenBracketsTheTokenContains(token)) {
						break;
					}
	
				}
			}
		}
		
		/*---------------------------
	 	  find scalarVariables Second 
	      ---------------------------*/
		tokens = new StringTokenizer(expr, delims);
		
		while(tokens.hasMoreTokens()) {
			String token = tokens.nextToken();
			
			//if arrayVariables don't contain the token and it's not a number, add it to arrayScalars
			if(!arrays.contains(new ArrayVariable(token)) && !token.matches("[0-9]+") && !scalars.contains(new ScalarVariable(token)) && !token.contains(".")) {
				scalars.add(new ScalarVariable(token));
			}
		}
				
    }
    
    /**
     * Loads values for scalars and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     */
    public void loadVariableValues(Scanner sc) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String varl = st.nextToken();
            ScalarVariable scal = new ScalarVariable(varl);
            ArrayVariable arr = new ArrayVariable(varl);
            int scali = scalars.indexOf(scal);
            int arri = arrays.indexOf(arr);
            if (scali == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar character
                scalars.get(scali).value = num;
            } else { // array character
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,value) pairs
                while (st.hasMoreTokens()) {
                    String tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    private Stack<String> convertStringToStack(String expression) {		
			Stack<String> stack = new Stack<String>();
			String exprNoSpaces = expression.replace(" ", "");						
		
			StringTokenizer tokens = new StringTokenizer(exprNoSpaces, delims, true);
			while(tokens.hasMoreTokens()) {
				String currentToken = tokens.nextToken();
				stack.push(currentToken);
			}

			return stack;
	} 
   	private String convertStackToString(Stack<String> stack){
   		ArrayList<String> list = new ArrayList<String>();
   		while(!stack.isEmpty()) {
   			list.add(stack.pop());
   		}
   		list = reverse(list);
   		String result = "";
   		for(String item: list) {
   			result += item;
   		}
   		return result;
   	}
   
   	private boolean isAnOperator(String piece) {
   		if(piece.equals("+") ||
			   	piece.equals("-") ||
			   	piece.equals("*") ||
			   	piece.equals("/")) {
		   	return true;
	   	}
	   	return false;	   
   	}   
   	private boolean isAPlainNumber(String str){
   		for (char c : str.toCharArray()) {
   			if (!Character.isDigit(c) && c != '.') {
        	   		return false;
   			}
   		}
   		return true;
   	}   		
   	
   	private String exprWithVariablesReplaced(String expression) {
   		StringTokenizer tokens = new StringTokenizer(expression.replace(" ", ""), delims, true);
		String output = "";
		while(tokens.hasMoreTokens()) {
			String currentToken = tokens.nextToken();
			if(scalars.contains(new ScalarVariable(currentToken))) {
				output += scalars.get(scalars.indexOf(new ScalarVariable(currentToken))).value;
			} else {								
				output += currentToken;
			}
			
		}
		return output;
   	} 	
   	private String exprWithArrayVariablesReplaced(String exprssion) {
   		Stack<String> stack = new Stack<String>();
   		
   		int arrayVariableValue = 0;
	
		StringTokenizer tokens = new StringTokenizer(exprssion, delims, true);
		
		while(tokens.hasMoreTokens()) {
			String currentToken = tokens.nextToken();			
			if(currentToken.equals("]")) {
				String closingBracket = currentToken;
				String number = stack.pop();
				String openingBracket = stack.pop();
				String variable = stack.pop();						
				
				//convert arrayVariable to proper form
				if(arrays.contains(new ArrayVariable(variable))) {
					ArrayVariable arrayVar = arrays.get(arrays.indexOf(new ArrayVariable(variable)));
					//------------
					
					if(number.contains(".")) {

						String numbersAfterPeriod = number.substring(number.indexOf('.')+1, number.length());
//						System.out.println("Numbers after period: "+numbersAfterPeriod);
						boolean numbersAfterPeriodAreAllZero = true;
						for(char c : numbersAfterPeriod.toCharArray()) {
							if(c != '0') {
								numbersAfterPeriodAreAllZero = false;
								break;
							}
						}
//						System.out.println("numbersAfterPeriodAreAllZero: " +numbersAfterPeriodAreAllZero);
						if(numbersAfterPeriodAreAllZero == true) {
							number = number.substring(0, number.indexOf("."));
//							System.out.println("number: " +number);
						}
					}
					//--------------
					arrayVariableValue = arrayVar.values[Integer.parseInt(number)];
					//push this back into stack
					stack.push(arrayVariableValue+"");
				} else {
					stack.push(variable);
					stack.push(openingBracket);
					stack.push(number);
					stack.push(closingBracket);
				}
				
				
			} else {
				stack.push(currentToken);
			} 
		}
		
		String newExpr = convertStackToString(stack);
		return newExpr;
		
   	}
   	
   	private String reversedLetters(String expression) {
   		String reverse = "";
   		for(int indexPtr = expression.length()-1; indexPtr > -1; indexPtr--) {
			reverse += expression.charAt(indexPtr);
		}
   		return reverse;
   	}
   	private ArrayList<String> reverse(ArrayList<String> list) {
   		for(int i = 0, j = list.size() - 1; i < j; i++) {
   	        list.add(i, list.remove(j));
   	    }
   	    return list;
   	}
   	       
    private boolean isAnOpenParenthesisOrBracket(String peice) {
  	   if(peice.equals("(") || peice.equals("[")) {
  		   return true;
  	   }
  	   return false;
     }
    private boolean isSomeKindOfBracket(String letter) {
   		if(letter.equals("(") || letter.equals("[") || letter.equals("]") || letter.equals(")")) {
   			return true;
   		} else {
   			return false;
   		}
   	}
  
	private String evaluateSimpliestExpressionsInParenthesis(String simplifiedExpr) {
//		System.out.println("BEEP: " + simplifiedExpr);
		simplifiedExpr = exprWithVariablesReplaced(simplifiedExpr);
//		System.out.println("BEEP: " + simplifiedExpr);
		Stack<String> stack = new Stack<String>();

		/*---------------------------------------------------------
		 evaluate the simlpiest expressions in parenthesis first: +-/*
		 ---------------------------------------------------------*/
		StringTokenizer tokens = new StringTokenizer(simplifiedExpr, delims, true);
		while (tokens.hasMoreTokens()) {
			String currentToken = tokens.nextToken();

			if (isAnOperator(currentToken) || isAPlainNumber(currentToken) || isAnOpenParenthesisOrBracket(currentToken)
					|| currentToken.equals("]")) {
				stack.push(currentToken);
			} else if (currentToken.equals(")")) {
				if (isAPlainNumber(stack.peek())) {
					String secondNumber = stack.pop();
					String operator = "";
					if(stack.peek().equals("(")) {
						stack.push(secondNumber);
						stack.push(currentToken);
						continue;
					} else {
						operator = stack.pop();
					}
					if(stack.peek().equals("(") || stack.peek().equals(")") || stack.peek().equals("[") || stack.peek().equals("]") || isAnOperator(stack.peek())) {
						stack.push(operator);
						stack.push(secondNumber);
						stack.push(currentToken);
						continue;
					}
					String firstNumber = "";
					if (isAPlainNumber(stack.peek())) {
						firstNumber = stack.pop();
						if(stack.peek().equals("(")) {
							String result = solve(firstNumber, operator, secondNumber);
							// remove trailing open parenthesis
							if(stack.peek().equals("(")) {
								stack.pop();
							}
							stack.push(result);
						} else {
							stack.push(firstNumber);
							stack.push(operator);
							stack.push(secondNumber);
							stack.push(currentToken);
						}
					} else {
						stack.push(operator);
						stack.push(secondNumber);
						stack.push(currentToken);
						continue;
					}
				} else {
					stack.push(currentToken);
				}
			} else {
				// Only added so array tokens would be pushed onto stack
				stack.push(currentToken);
			}
			// System.out.println("Token: " + currentToken);
		}
		return convertStackToString(stack);
	}	
	private String newEvaluateSimpliestExpressionsInParenthesis(String simplifiedExpr) {
		Stack<String> stack = new Stack<String>();
		StringTokenizer tokens = new StringTokenizer(simplifiedExpr, delims, true);

		while (tokens.hasMoreTokens()) {
			String currentToken = tokens.nextToken();
			
			if(currentToken.equals(")")) {
				//check if next token is number
				if(!stack.isEmpty() && isAPlainNumber(stack.peek())) {
					//if it is a number, create refrence
					String secondNumber = stack.pop();
					String operator = "";
					//check if next item in stack is an operator
					if(isAnOperator(stack.peek())) {
						operator = stack.pop();
					} else {
						stack.push(secondNumber);
						stack.push(currentToken);
						continue;
					}
					String firstNumber = "";
					//check if next item in stack is a number
					if(isAPlainNumber(stack.peek())) {
						firstNumber = stack.pop();
					} else {
						stack.push(operator);
						stack.push(secondNumber);
						stack.push(currentToken);
						continue;
					}
					if(stack.peek().equals("(")) {
						String result = solve(firstNumber, operator, secondNumber);
						stack.push(result);
						//stack.push(currentToken);
					} else {
						stack.push(firstNumber);
						stack.push(operator);
						stack.push(secondNumber);
						stack.push(currentToken);
						continue;
					}
					
				} else {
					stack.push(currentToken);
					continue;
				}
			}
			
			stack.push(currentToken);
		}
		return convertStackToString(stack);
	}
	private String evaluateSimplestArrayExpressions(String simplifiedExpr){
		Stack<String> stack = new Stack<String>();
		StringTokenizer tokens = new StringTokenizer(simplifiedExpr, delims, true);
		while(tokens.hasMoreTokens()) {
			String currentToken = tokens.nextToken();
			
			if(isAnOperator(currentToken) || isAPlainNumber(currentToken) || isAnOpenParenthesisOrBracket(currentToken) || currentToken.equals(")")) {
				stack.push(currentToken);
			}
			else if(currentToken.equals("]")) {
				if(isAPlainNumber(stack.peek())) {
					String secondNumber = stack.pop();
					//if there's only 1 number in brackets
					if(stack.peek().equals("[")) {
						stack.push(secondNumber);
						//push closing bracket
						stack.push(currentToken);
						continue;
					}
					String operator = stack.pop();
					String firstNumber = "";
					if(isAPlainNumber(stack.peek())) {
						firstNumber = stack.pop();
						if (stack.peek().equals("[")) {
							String result = solve(firstNumber, operator, secondNumber);
							//Don't remove trailing open bracket, it's neededd						
							stack.push(result);
							//push closing bracket to close arrayVariable
							stack.push(currentToken);
						} else {
							stack.push(firstNumber);
							stack.push(operator);
							stack.push(secondNumber);
							stack.push(currentToken);
							continue;
						}
					} else {
						stack.push(operator);
						stack.push(secondNumber);
						stack.push(currentToken);
						continue;
					}
				} else {
					stack.push(currentToken);
				}
			} else {
				//Only added so array tokens would be pushed onto stack 
				stack.push(currentToken);
			}
		}
		return convertStackToString(stack);
	}	
   
	private String solve(String firstConstant, String operator, String secondConstant) {
		   double first = Double.parseDouble(firstConstant);
		   double second = Double.parseDouble(secondConstant);
		   String result = "";
		   if(operator.equals("*")) {
			   result = (first * second)+"";
		   } else if(operator.equals("/")) {
			   result = (first / second)+"";
		   } else if(operator.equals("+")) {
			   result = (first + second)+"";
		   } else if(operator.equals("-")) {
			   result = (first - second)+"";
		   }
		   return result;
	   }  	
	private String evaluateMultiplication(String expression) {
		Stack<String> stack = new Stack<String>();
		String result = "";
		StringTokenizer tokens = new StringTokenizer(expression,delims,true);
		while(tokens.hasMoreTokens()) {
			String currentToken = tokens.nextToken();
			if(!stack.isEmpty() && (stack.peek().equals("*")) && !isSomeKindOfBracket(currentToken)) {				
//				String secondConstant = currentToken;
				String secondConstant = "";
				//if second number is negative
				if(currentToken.equals("-")) {
					currentToken = tokens.nextToken();
					currentToken = "-"+currentToken;
					secondConstant = currentToken; 
				} else {
					secondConstant = currentToken;
				}
				String operator = stack.pop();
				String firstConstant = "";
				if(!isSomeKindOfBracket(stack.peek())) {
					firstConstant = stack.pop();
					//if first number is negative
					if(!stack.isEmpty() && stack.peek().equals("-")) {
						String extraSubtractionSymbol = stack.pop();		
						if(stack.isEmpty() || isAnOperator(stack.peek()) || isAnOpenParenthesisOrBracket(stack.peek())) {
							firstConstant = "-"+firstConstant;
						} else {
							stack.push(extraSubtractionSymbol);
							result = solve(firstConstant, operator, secondConstant);
							stack.push(result);
							continue;
						}
					}
				} else {
					stack.push(operator);
					stack.push(currentToken);
					continue;
				}
				
				result = solve(firstConstant, operator, secondConstant);
				stack.push(result);
				
			} else {
				stack.push(currentToken);
			}
		}
		return convertStackToString(stack);
	}
	private String evaluateDivision(String expression) {
		Stack<String> stack = new Stack<String>();
		String result = "";
		StringTokenizer tokens = new StringTokenizer(expression,delims,true);
		while(tokens.hasMoreTokens()) {
			String currentToken = tokens.nextToken();
			if(!stack.isEmpty() && (stack.peek().equals("/")) && !isSomeKindOfBracket(currentToken)) {				
				String secondConstant = "";
				//if second number is negative
				if(currentToken.equals("-")) {
					currentToken = tokens.nextToken();
					currentToken = "-"+currentToken;
					secondConstant = currentToken; 
				} else {
					secondConstant = currentToken;
				}
				String operator = stack.pop();
				String firstConstant = "";
				if(!isSomeKindOfBracket(stack.peek())) {
					firstConstant = stack.pop();
					//if first number is negative
					if(!stack.isEmpty() && stack.peek().equals("-")) {
						String extraSubtractionSymbol = stack.pop();
						if(stack.isEmpty() || isAnOperator(stack.peek()) || isAnOpenParenthesisOrBracket(stack.peek())) {
							firstConstant = "-"+firstConstant;
						} else {
							stack.push(extraSubtractionSymbol);
							result = solve(firstConstant, operator, secondConstant);
							stack.push(result);
							continue;
						}
					}
				} else {
					stack.push(operator);
					stack.push(currentToken);
					continue;
				}	
				result = solve(firstConstant, operator, secondConstant);
				stack.push(result);
				
			} else {
				stack.push(currentToken);
			}
		}
		return convertStackToString(stack);
	}		
	private String evaluateSubtractionAndAddition(String expression) {
		Stack<String> stack = new Stack<String>();
		String result = "";
		StringTokenizer tokens = new StringTokenizer(expression,delims,true);
		while(tokens.hasMoreTokens()) {
			String currentToken = tokens.nextToken();
			if(!stack.isEmpty() && (stack.peek().equals("+") || stack.peek().equals("-"))) {				
				String secondConstant = "";
				if(currentToken.equals("-")) {
					currentToken = tokens.nextToken();
					currentToken = "-"+currentToken;
					secondConstant = currentToken; 
				} else {
					secondConstant = currentToken;
				}
				String operator = stack.pop();				
				String firstConstant = "";
				if(!stack.isEmpty() && isAPlainNumber(stack.peek())) {
					firstConstant = stack.pop();
					if(!stack.isEmpty() && stack.peek().equals("-")) {
						String extraSubtractionSymbol = stack.pop();
						if(stack.isEmpty()) {
							firstConstant = "-"+firstConstant;		
						} else if(isAnOperator(stack.peek())) {
							firstConstant = "-"+firstConstant;
						} else {
							stack.push(extraSubtractionSymbol);
						}
					}
					result = solve(firstConstant, operator, secondConstant);
					stack.push(result);
				} else {
					stack.push(operator);
					stack.push(secondConstant);
					continue;
				}				
				
			} else {
				stack.push(currentToken);
			}
		}		
		return convertStackToString(stack);
	}
		
	private String newCleanNumbersWithParenthesisAroundThem(String expression) {
		String delimsNoParen = " \t*+-/[]";
		expression = expression.replace(" ", "");
		Stack<String> stack = new Stack<String>();
		//
		StringTokenizer tokens = new StringTokenizer(expression, delimsNoParen, true);
		while (tokens.hasMoreTokens()) {
			String currentToken = tokens.nextToken();
			if (currentToken.startsWith("(") && currentToken.contains(")")) {
				String openBrackets = currentToken.substring(0, currentToken.lastIndexOf('('));
				// Write code to get closed parenthesis
				if ((currentToken.indexOf(")") - currentToken.lastIndexOf(")")) < 0) {
					String closedBrackets = currentToken.substring(currentToken.indexOf(')'), currentToken.lastIndexOf(')'));
					// Write code to get closed parenthesis
					String corrected = currentToken.substring(currentToken.lastIndexOf('(') + 1, currentToken.indexOf(')')) + closedBrackets;
					stack.push(corrected);
				} else {
					String corrected = openBrackets + currentToken.substring(currentToken.lastIndexOf('(') + 1, currentToken.indexOf(')'));
					stack.push(corrected);
				}
			} else {
				stack.push(currentToken);
			}
		}
		return convertStackToString(stack);
	}
	private String cleanParenthesisAroundNegatives(String expression) {
		String delimsNoNegOrParens = " \t*+/[]()";
		expression = expression.replace(" ", "");
		Stack<String> stack = new Stack<String>();
		StringTokenizer tokens = new StringTokenizer(expression, delimsNoNegOrParens, true);
		while(tokens.hasMoreTokens()) {
			String currentToken = tokens.nextToken();
			if(currentToken.equals(")")){
				if(!stack.isEmpty() && stack.peek().charAt(0) == '-') {
					boolean restIsANumber = true;
					for(int indexPtr = 1; indexPtr <currentToken.length()-1; indexPtr++) {
						if(!Character.isDigit(currentToken.charAt(indexPtr))) {
							restIsANumber = false;
						}
					}
					if(restIsANumber == true) {
						//pop neg number
						String negNumber = stack.pop();
						if(!stack.isEmpty() && stack.peek().equals("(")) {
							stack.pop();
							stack.push(negNumber);
							continue;
						} else {
							stack.push(negNumber);
							stack.push(currentToken);
							continue;
						}
					}
				} else {
					stack.push(currentToken);
				}
			} else {
				stack.push(currentToken);
			}
		}
		return convertStackToString(stack);
	}
	
	private boolean isANegativeNumber(String expression) {
		boolean negInBeginning = false;
		boolean onlyHasNegInBeginning = true;
		
		for(int indexPtr = 1; indexPtr < expression.length(); indexPtr++) {
			if(expression.startsWith("-")) {
				negInBeginning = true;
			}
			if(expression.charAt(indexPtr) == '+' || expression.charAt(indexPtr) == '-' || expression.charAt(indexPtr) == '/' || expression.charAt(indexPtr) == '*') {
				onlyHasNegInBeginning = false;
			}
		}
		if(negInBeginning == true && onlyHasNegInBeginning == true) {
			return true;
		} else {
			return false;
		}
	}
	
    /**
     * Evaluates the expression, and can use RECURSION to evaluate subexpressions and to evaluate array 
     * subscript expressions.
     * 
     * @param scalars The scalar array list, with values for all scalar items
     * @param arrays The array array list, with values for all array items
     * 
     * @return Result of evaluation
     */
    public double evaluate() {
    		/** COMPLETE THIS METHOD **/		
//    		System.out.println("---Expression with variables included: " + expr);
//    		System.out.println("---Expression with variables replaced: " + exprWithVariablesReplaced(expr));
    		
    		String simplifiedExpr = exprWithVariablesReplaced(expr);
    		Stack<String> stack = new Stack<String>();    				    		
    		
    		simplifiedExpr = evaluateSimpliestExpressionsInParenthesis(simplifiedExpr); //***********
    		
//		System.out.println("---evaluateSimpliestExpressionsInParenthesis: " + simplifiedExpr);		
		
		simplifiedExpr = evaluateSimplestArrayExpressions(simplifiedExpr);				
//		System.out.println("---**evaluateSimplestArrayExpressions: " + simplifiedExpr);
		
		simplifiedExpr = evaluateSimpliestExpressionsInParenthesis(simplifiedExpr);
//		simplifiedExpr = evaluateSimplestArrayExpressions(simplifiedExpr);
		simplifiedExpr = exprWithArrayVariablesReplaced(simplifiedExpr);			
		

		simplifiedExpr = newCleanNumbersWithParenthesisAroundThem(simplifiedExpr);
		
//		System.out.println("---Simplified expression: " + simplifiedExpr);

		while(!isANegativeNumber(simplifiedExpr) && (simplifiedExpr.contains("+") || simplifiedExpr.contains("-") || simplifiedExpr.contains("*") || simplifiedExpr.contains("/"))) {
			simplifiedExpr = exprWithArrayVariablesReplaced(simplifiedExpr);
//			System.out.println("exprWithArrayVariablesReplaced: " + simplifiedExpr);
			
			simplifiedExpr = newCleanNumbersWithParenthesisAroundThem(simplifiedExpr);
//			System.out.println("cleanNumbersWithParenthesisAroundThem: " + simplifiedExpr);
			
			simplifiedExpr = newEvaluateSimpliestExpressionsInParenthesis(simplifiedExpr);
//			System.out.println("evaluateSimpliestExpressions: " + simplifiedExpr);
			 
			simplifiedExpr = newCleanNumbersWithParenthesisAroundThem(simplifiedExpr);
//			System.out.println("cleanNumbersWithParenthesisAroundThem: " + simplifiedExpr);
			simplifiedExpr = exprWithArrayVariablesReplaced(simplifiedExpr);
			 
			simplifiedExpr = evaluateMultiplication(simplifiedExpr);
//			System.out.println("evaluateMultiplication: " + simplifiedExpr);
			 
			simplifiedExpr = newCleanNumbersWithParenthesisAroundThem(simplifiedExpr);
//			System.out.println("cleanNumbersWithParenthesisAroundThem: " + simplifiedExpr);
			 
			simplifiedExpr = cleanParenthesisAroundNegatives(simplifiedExpr);
//			System.out.println("cleanParenthesisAroundNegatives: " + simplifiedExpr);
			 
			simplifiedExpr = evaluateMultiplication(simplifiedExpr);
//			System.out.println("evaluateMultiplication: " + simplifiedExpr);
			 
			simplifiedExpr = evaluateDivision(simplifiedExpr);
//			System.out.println("evaluateDivision: " + simplifiedExpr);
			 
			simplifiedExpr = evaluateMultiplication(simplifiedExpr);
//			System.out.println("evaluateMultiplication: " + simplifiedExpr);
			 
			simplifiedExpr = newEvaluateSimpliestExpressionsInParenthesis(simplifiedExpr);
//			System.out.println("***evaluateSimpliestExpressions: " + simplifiedExpr);
			 
			simplifiedExpr = newCleanNumbersWithParenthesisAroundThem(simplifiedExpr);
//			System.out.println("cleanNumbersWithParenthesisAroundThem: " + simplifiedExpr);
			 
			simplifiedExpr = cleanParenthesisAroundNegatives(simplifiedExpr);
//			System.out.println("cleanParenthesisAroundNegatives: " + simplifiedExpr);
			 
			simplifiedExpr = evaluateDivision(simplifiedExpr);
//			System.out.println("evaluateDivision: " + simplifiedExpr);
			 
			simplifiedExpr = evaluateMultiplication(simplifiedExpr);
//			System.out.println("evaluateMultiplication: " + simplifiedExpr);
			 
			simplifiedExpr = evaluateSubtractionAndAddition(simplifiedExpr);
//			System.out.println("**evaluateSubtractionAndAddition: " + simplifiedExpr);
			 
			simplifiedExpr = newCleanNumbersWithParenthesisAroundThem(simplifiedExpr);
//			System.out.println("cleanNumbersWithParenthesisAroundThem: " + simplifiedExpr);
			 
			simplifiedExpr = evaluateMultiplication(simplifiedExpr);
//			System.out.println("evaluateMultiplication: " + simplifiedExpr);
			 
			simplifiedExpr = evaluateDivision(simplifiedExpr);
//			System.out.println("evaluateDivision: " + simplifiedExpr);
			 
			simplifiedExpr = cleanParenthesisAroundNegatives(simplifiedExpr);
			//System.out.println("cleanParenthesisAroundNegatives: " + simplifiedExpr);
			 
			simplifiedExpr = evaluateSubtractionAndAddition(simplifiedExpr);
//			System.out.println("evaluateSubtractionAndAddition: " + simplifiedExpr);
			 
			simplifiedExpr = newCleanNumbersWithParenthesisAroundThem(simplifiedExpr);
//			System.out.println("cleanNumbersWithParenthesisAroundThem: " + simplifiedExpr);
			 
			simplifiedExpr = cleanParenthesisAroundNegatives(simplifiedExpr);
//			System.out.println("cleanParenthesisAroundNegatives: " + simplifiedExpr);
				
			simplifiedExpr = exprWithArrayVariablesReplaced(simplifiedExpr);
//			System.out.println("exprWithArrayVariablesReplaced: " + simplifiedExpr);

//			 break;
		}
				
//		System.out.println("Final simplified expression: " + simplifiedExpr);
//		System.out.println();
		
		String finalExpression = simplifiedExpr;
		Double finalExpressionConvertedToDouble = Double.parseDouble(finalExpression);
		
    		/*----------------
  		  Testing     		  
  		 -----------------*/
//		while(!stack.isEmpty()) {
//			String popped = stack.pop();
//			System.out.println("In Stack: " + popped);
//		}
//    		
//        System.out.println("Scalars: \n" + scalars.toString() + "\n");
//    		System.out.println("Arrays: \n" + arrays.toString());
    		
    		return finalExpressionConvertedToDouble;
    }

    /**
     * Utility method, prints the characters in the scalars list
     */
    public void printScalars() {
        for (ScalarVariable ss: scalars) {
            System.out.println(ss);
        }
    }
    
    /**
     * Utility method, prints the characters in the arrays list
     */
    public void printArrays() {
    		for (ArrayVariable as: arrays) {
    			System.out.println(as);
    		}
    }

}

