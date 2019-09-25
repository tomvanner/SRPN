/**
 * The operator class for SRPN, which deals with calculating the results for each operation.
 * 
 * @author Thomas Vanner
 * @version 1.0
 * @release 25/11/2016
 * @See SrpnProcessInput.java
 */

public class Operator{
	SrpnStack srpnStack;

	/**
	 * Method used to get the contents of the current stack.
	 * 
	 * @param stack
	 * 			the stack variable
	 */
	public void setStack(SrpnStack stack){
		srpnStack = stack;
	}
	
	/**
	 * Method used to add two operands together, and push the result to the stack.
	 * 
	 * @param operandOne
	 * 			the first operand that the operation is going to be performed on
	 * @param operandTwo
	 * 			the second operand that the operation is going to be performed on
	 */
	public void addOperands(int operandOne, int operandTwo){
		int calculationResult = operandTwo + operandOne;
		srpnStack.pushInt(calculationResult);
	}

	/**
	 * Method used to subtract two operands from each other, and push the result to the stack.
	 * 
	 * @param operandOne
	 * 			the first operand that the operation is going to be performed on
	 * @param operandTwo
	 * 			the second operand that the operation is going to be performed on
	 */
	public void subtractOperands(int operandOne, int operandTwo){
		int calculationResult = operandTwo - operandOne;
		srpnStack.pushInt(calculationResult);	
	}

	/**
	 * Method used to multiply two operands together, and push the result to the stack.
	 * 
	 * @param operandOne
	 * 			the first operand that the operation is going to be performed on
	 * @param operandTwo
	 * 			the second operand that the operation is going to be performed on
	 */
	public void multiplyOperands(int operandOne, int operandTwo){
		int calculationResult = operandTwo * operandOne;
		srpnStack.pushInt(calculationResult);
	}

	/**
	 * Method used to raise one operand to the power of another, and push the result to the stack.
	 * 
	 * @param operandOne
	 * 			the first operand that the operation is going to be performed on
	 * @param operandTwo
	 * 			the second operand that the operation is going to be performed on
	 */
	public void exponentOperands(int operandOne, int operandTwo){
		int calculationResult = (int) Math.pow(operandTwo, operandOne);
		srpnStack.pushInt(calculationResult);
	}

	/**
	 * Method used to divide one operand by the other, and push the result to the stack.
	 * 
	 * @param operandOne
	 * 			the first operand that the operation is going to be performed on
	 * @param operandTwo
	 * 			the second operand that the operation is going to be performed on
	 */
	public void divideOperands(int operandOne, int operandTwo){
		int calculationResult = operandTwo / operandOne;
		srpnStack.pushInt(calculationResult);
	}	

	/**
	 * Method used to perform the modulus of one operand on the other, and push the result to the stack.
	 * 
	 * @param operandOne
	 * 			the first operand that the operation is going to be performed on
	 * @param operandTwo
	 * 			the second operand that the operation is going to be performed on
	 */
	public void modulusOperands(int operandOne, int operandTwo){
		int calculationResult = operandTwo % operandOne;
		srpnStack.pushInt(calculationResult);
	}
}
