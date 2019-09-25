/**
 * The SrpnStack class for SRPN, which handles all of the functionality of the stack.
 * 
 * @author Thomas Vanner
 * @version 1.0
 * @release 25/11/2016
 * @See SrpnProcessInput.java
 */

import java.util.Stack;

public class SrpnStack{
	Stack<String> srpnStack;

	/**
	 * Constructor which initiates a new stack.
	 */
	public SrpnStack(){
		srpnStack = new Stack<String>();
	}

	/**
	 * Method used to push the user input onto the stack.
	 * 
	 * @param userInput
	 * 			
	 */
	public void push(String userInput){
		// Checks if there are less than 23 elements on the stack
		if(size() < 23){
			srpnStack.push(userInput);
		}
		else{
			System.err.println("Stack overflow.");
		}
	}

	/**
	 * Method used to push an integer onto the stack.
	 * 
	 * @param operationResult
	 * 		the result of the operation. See 'Operator' class.
	 */
	public void pushInt(int operationResult){
		String resultToString = Integer.toString(operationResult);
		push(resultToString);
	}

	/**
	 * Method pop an integer from the stack.
	 * 
	 * @return the popped integer.
	 */
	public int popInt(){
		int popInt = Integer.parseInt(srpnStack.pop());
		return popInt;
	}

	/**
	 * Method used to pop an item off the stack.
	 * 
	 * @return the popped string
	 */
	public String pop(){
		String popString = srpnStack.pop();
		return popString;
	}

	/**
	 * Method used to peek at the top item on the stack.
	 * 
	 * @return the top item on the stack.
	 */
	public String peek(){
		String topItem = srpnStack.peek();
		return topItem;
	}

	/**
	 * Method used to find the amount of element that are currently on the stack.
	 * 
	 * @return the size of the stack.
	 */
	public int size(){
		int srpnSize = srpnStack.size();
		return srpnSize;
	}

	/**
	 * Method used to print the conents of the stack.
	 * 
	 * @param operationResult
	 * 		the result of the operation. See 'Operator' class.
	 */
	public void printStack(){
		for(int i = 0; i < size(); i++){
			System.out.println(srpnStack.get(i));
		}	
	}
}
