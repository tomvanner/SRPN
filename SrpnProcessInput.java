/**
 * The class that handles all of the input processing, and error checking for SRPN.
 * 
 * @author Thomas Vanner
 * @version 1.0
 * @release 25/11/2016
 * @See SRPN.java
 */

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.lang.Math;

public class SrpnProcessInput {
	SrpnStack srpnStack = new SrpnStack();
	Operator srpnOperator = new Operator();

	// Creates an array list that will hold the numbers used in the 'r' command
	ArrayList<Integer> rNumberList = new ArrayList<Integer>();

	// Stores the current index for the 'r' command. See 'processNonNumerical' method
	private int currentRIndex = 0;

	/**
	 * Constructor which adds all of the values to the array list needed for the 'r' command.
	 *
	 */
	public SrpnProcessInput() {
		addRListValues();
	}

	/**
	 * Method used to split up user input into seperate strings whenever a space is encountered.
	 * 
	 * @param s
	 * 			the user input as recieved from SRPN.java
	 */
	public void processCommand(String s) {  
		// Split user input
		StringTokenizer st = new StringTokenizer(s, " ");
		boolean hasComment = false;
		// Loops through the user input string until there are no more tokens, and while there string has no comment
		while(st.hasMoreTokens() && !hasComment){
			// Holds the current token
			String nextElement = st.nextToken();
			hasComment = processToken(nextElement);
		}
	}

	/** 
	 * Method used to process the token passed from 'processCommand' method
	 * 
	 * @param nextElement
	 * 			the current part of the user input, which has been split by a space. See 'processCommand' method.
	 * 
	 * @return whether or not the string has a comment in
	 */
	private boolean processToken(String nextElement){
		boolean commentFound = false;
		String currentOperand = "";
		int j = nextElement.length();
		// Loops through each character in the element
		for(int i = 0; i < j; i++){
			// Gets the current character in the token
			char currentChar = nextElement.charAt(i);
			char nextChar = ' ';

			// If not last character in token, get next character - this prevents uknown index errors
			if(i != j - 1){
				nextChar = nextElement.charAt(i + 1);
			}

			// If character is '#', disregard any input after it - move onto the next token
			if(currentChar == '#'){
				commentFound = true;
				break;
			}

			// If input is an operand, add it to the current string
			else if(isOperand(currentChar)){
				currentOperand += currentChar;
				
				/* If last character in string, or the next character is not an operand
				 * process the input, as this indicates end of operand.
				 */
				if(i == j - 1 || !(isOperand(nextChar))){
					processOperand(currentOperand);
					// Resets the string/operand for next iteration
					currentOperand = "";
				}
			}

			// Checks if the current character is an non numeric input
			else if(isNonNumerical(currentChar)){
				
				/* If next character is an operand, and operation is '-' 
				 * input is a negative number, unless it's equal to 0
				 * srpn doesn't allow '-0'
				 */
				if(isOperand(nextChar) && currentChar == '-' && nextChar != 0){
					currentOperand += currentChar;
				}
				else{
					processNonNumerical(currentChar);
				}
			}

			// If none of above criteria are met, input is an unknown character
			else{
				System.err.println("Unrecognised operator or operand \"" + currentChar + "\".");
			}

		}
		return commentFound;
	}
	
	/** 
	 * Method which checks if input is non-numerical, i.e. it is an operator or a letter
	 * 
	 * @param currentCharacter
	 * 		the character which is currently being processed. See 'processToken' method.
	 * 
	 * @return whether or not the character is numeric or not
	 */
	private boolean isNonNumerical(char currentChar){
		if(currentChar == '+' || currentChar == '-' || currentChar == '*' || currentChar == '/' || currentChar == '%' || currentChar == '^' || currentChar == '=' || currentChar == 'd' || currentChar == 'r'){
			return true;
		}
		else{
			return false;
		}
	}
	
	/** 
	 * Method which checks if the operand is an octal, passes the saturation test and pushes it to the stack.
	 * 
	 * @param operand
	 * 		the entire operand to be processed. See 'processToken' method.
	 */
	private void processOperand(String operand){
		// Checks if the operand is octal
		if(!isOct(operand)){
			// Checks if oprand passes saturation test
			if(elementSaturationCheck(operand)){
				srpnStack.push(operand);	
			}
		}
		// Calculates octal value of operand
		else{
			String octValue = getOctValue(operand);
			calculateOct(octValue);
		}
	}
	
	/** 
	 * Method which processes input which is not a numerical value.
	 * 
	 * @param nonNumericInput
	 * 		the character which is non numeric
	 */
	private void processNonNumerical(char nonNumericInput){
		if(nonNumericInput == '='){
			System.out.println(srpnStack.peek());
		}
		else if(nonNumericInput == 'd'){
			srpnStack.printStack();
		}
		
		/*
		 *  If r is entered, prints the first item of the 'rNumberList' array list
		 *  and increment 'currentRIndex' so if 'r' is entered again
		 *  the second item is printed etc.
		 */
		else if(nonNumericInput == 'r'){
			srpnStack.pushInt(rNumberList.get(currentRIndex));
			currentRIndex ++;
		}
		else{
			performOperation(nonNumericInput);
		}
	}
	
	/** 
	 * Method which checks if an operand is an octal.
	 * 
	 * @param operand
	 * 		the operand to be checked
	 * 
	 * @return whether or not the operand is an octal
	 */
	private boolean isOct(String operand){
		// If string starts with '0' or starts with '-0' which would still be an octal value, but negative.
		if(((operand.charAt(0) == '0') || (operand.charAt(0) == '-' && operand.charAt(1) == '0')) && operand.length() > 1){
			return true;
		}
		else{
			return false;
		}
	}
	
	/** 
	 * Method which calculates the decimal value of an operand which is an octal, and pushes it to the stack.
	 * 
	 * @param octValue
	 * 		the operand in octal number e.g. '010'
	 */
	private void calculateOct(String octValue){
		boolean isNegative = false;
		int decimalValue = 0;
		int exponent = 0;
		// Loops through the octValue from least significant digit to most significant digit
		for(int i = octValue.length() - 1; i >= 0; i--, exponent++){
			char currentOctValue = octValue.charAt(i);
			if(currentOctValue == '-'){
				isNegative = true;
				continue;
			}
			// Multiplies the current digit by the value in that digit's 'column', and adds it to the decimal value
			decimalValue += Character.getNumericValue(currentOctValue) * Math.pow(8, exponent);
		}
		
		// Negates the decimal value if a minus sign was found
		if(isNegative){
			decimalValue *= -1;
		}
		if(elementSaturationCheck(Integer.toString(decimalValue))){
			srpnStack.pushInt(decimalValue);
		}

	}
	
	/** 
	 * Method which extracts the octal value from the raw operand. 
	 * This is needed due to the fact SRPN disregards any input after 
	 * an '8' or a '9' is encountered, and uses the preceeding numbers 
	 * 
	 * @param octOperand
	 * 		the operand in 'raw' form. See 'processOperand' method.
	 * 
	 * @return the octal value of the given operand
	 */
	private String getOctValue(String octOperand){
		String octValue = "";
		for(int i = 0; i < octOperand.length(); i++){
			char currentCharacter = octOperand.charAt(i);
			if(currentCharacter == '8' || currentCharacter == '9'){
				break;
			}
			else if(currentCharacter != '0' || octValue != ""){
				octValue += octOperand.charAt(i);

			}
			else{
				continue;
			}
		}
		return octValue;
	}

	/** 
	 * Method which checks if a given character is an operand or not.
	 * 
	 * @param currentChar
	 * 		the character to be tested
	 * 
	 * @return whether or not the given character is an operand.
	 */
	private boolean isOperand(char currentChar){
		try{
			Integer.parseInt(Character.toString(currentChar));
			return true;
		}
		catch(NumberFormatException e){
			return false;
		}
	}    
	
	/** 
	 * Method which performs the given operation on the first two operands on the stack. 
	 * Also checks if the operation result is over or under saturated.
	 * 
	 * @param operation
	 * 		the operation which is to be performed on the operands. See 'processToken' method.
	 */
	private void performOperation(char operation){
		// Sets the stack in Operator class
		srpnOperator.setStack(srpnStack);
		// Checks for stack underflow
		if(underflowCheck()){
			int firstOperand = srpnStack.popInt();
			int secondOperand = srpnStack.popInt();
			long operationResult = 0;
			
			// Checks the operation entered, and pushes result to stack if it passes saturation test
			switch(operation){
			case '+':
				operationResult = ((long) secondOperand) + firstOperand;
				if(operationSaturationCheck(operationResult)){
					srpnOperator.addOperands(firstOperand, secondOperand);
				}
				break;
			case '-':
				operationResult = ((long) secondOperand) - firstOperand;
				if(operationSaturationCheck(operationResult)){
					srpnOperator.subtractOperands(firstOperand, secondOperand);
				}
				break;
			case '*':
				operationResult = ((long) secondOperand) * firstOperand;
				if(operationSaturationCheck(operationResult)){
					srpnOperator.multiplyOperands(firstOperand, secondOperand);
				}
				break;
			case '/':
				// Checks for divide by zero error
				if(divideZero(firstOperand, secondOperand)){
					operationResult = ((long) secondOperand) / firstOperand;
					if(operationSaturationCheck(operationResult)){
						srpnOperator.divideOperands(firstOperand, secondOperand);
					}
				}
				break;
			case '^':
				// Checks for negative power
				if(negativePower(firstOperand, secondOperand)){
					operationResult += (long) Math.pow(secondOperand, firstOperand);
					if(operationSaturationCheck(operationResult)){
						srpnOperator.exponentOperands(firstOperand, secondOperand);
					}
				}
				break;
			case '%':
				if(divideZero(firstOperand, secondOperand)){
					operationResult = ((long) secondOperand) % firstOperand;
					if(operationSaturationCheck(operationResult)){
						srpnOperator.modulusOperands(firstOperand, secondOperand);
					}
				}
				break;
			}
		}
	}

	/** 
	 * Method which checks if an element which has yet to be pushed to the stack is over or under saturated. 
	 * 
	 * @param elementToPush
	 * 		the operand to be pushed to the stack
	 * 
	 * @return whether or not the operand was over/under saturated or was in the correct range.
	 */
	private boolean elementSaturationCheck(String elementToPush){
		// If element is over 10 digits long, then it is certainly over/under saturated
		if(elementToPush.length() > 10){
			if(elementToPush.contains("-")){
				srpnStack.push("-2147483648");
				return false;
			}
			else{
				srpnStack.push("2147483647");
				return false;
			}
		}
		
		/* If not, it could still be over/under saturated, but will be 
		 * short enough to be stored in a long in order to check.
		 */
		else{
			long elementHolder = Long.parseLong(elementToPush);
			if(elementHolder > 2147483647){
				srpnStack.push("2147483647");
				return false;
			}
			else if(elementHolder < -2147483648){
				srpnStack.push("-2147483648");
				return false;
			}
		}
		return true;
	}

	/** 
	 * Method which checks if a divide, or modulus operation would result in a division by 0. See 'performOperation' method. 
	 * 
	 * @param firstOperand
	 * 		the first operand which if equal to zero, will return true, as this would not result in a division by 0.
	 * @param secondOperand
	 * 		the second operand, which is pushed to the stack if there is a division by 0
	 * 
	 * @return whether or not a division by 0 will occur.
	 */
	private boolean divideZero(int firstOperand, int secondOperand){
		if(firstOperand != 0){
			return true;
		}
		else{
			// Push user input on stack regardless
			srpnStack.pushInt(secondOperand);
			srpnStack.pushInt(firstOperand);
			System.err.println("Divide by 0.");
			return false;
		}
	}

	/** 
	 * Method which checks if an exponent operation would result in a operand being raised to a negative power.
	 * 
	 * @param firstOperand
	 * 		the first operand which if equal to or greater than 0, will return true, as this would not result in a negative power
	 * @param secondOperand
	 * 		the second operand, which is pushed to the stack if there is a negative power.
	 * 
	 * @return whether or not an exponent operation will result in an operand being raised to a negative power
	 */
	private boolean negativePower(int firstOperand, int secondOperand){
		if(firstOperand >= 0){
			return true;
		}
		else{
			srpnStack.pushInt(secondOperand);
			srpnStack.pushInt(firstOperand);
			System.err.println("Negative power.");
			return false;
		}
	}

	/** 
	 * Method which checks if a stack underflow will occur. See 'performOperation' method.
	 * 
	 * @return whether or not a stack underflow will occur.
	 */
	private boolean underflowCheck(){
		if(srpnStack.size() < 2){
			System.err.println("Stack underflow.");
			return false;
		}
		else{
			return true;
		}
	}

	/** 
	 * Method which checks if the result of an operation is under/over saturated.
	 * @param operationResult
	 * 		the result of the operation. 'See processOperation' method.
	 * 
	 * @return whether or not the result was under/over saturated or not
	 */
	private boolean operationSaturationCheck(long operationResult){
		// If the operand exceeds the max integer value, push the max value to the stack to prevent rollover
		if(operationResult > 2147483647){
			srpnStack.pushInt(2147483647);
			return false;
		}
		// Same as above, but for minimum value
		else if(operationResult < -2147483648){
			srpnStack.pushInt(-2147483648);
			return false;
		}
		else{
			return true;
		}
	}
	
	/** 
	 * Method which adds all the required values to the 'rNumberList' array list, defined at the top of this class.
	 * These values are used when the 'r' command is entered. See constructor.
	 */
	private void addRListValues(){
		rNumberList.add(1804289383);
		rNumberList.add(846930886);
		rNumberList.add(1681692777);
		rNumberList.add(1714636915);
		rNumberList.add(1957747793);
		rNumberList.add(424238335);
		rNumberList.add(719885386);
		rNumberList.add(1649760492);
		rNumberList.add(596516649);
		rNumberList.add(1189641421);
		rNumberList.add(1025202362);
		rNumberList.add(1350490027);
		rNumberList.add(783368690);
		rNumberList.add(1102520059);
		rNumberList.add(2044897763);
		rNumberList.add(1967513926);
		rNumberList.add(1365180540);
		rNumberList.add(1540383426);
		rNumberList.add(304089172);
		rNumberList.add(1303455736);
		rNumberList.add(35005211);
		rNumberList.add(521595368);
		rNumberList.add(294702567);
		rNumberList.add(1726956429);
		rNumberList.add(336465782);
		rNumberList.add(861021530);
		rNumberList.add(278722862);
		rNumberList.add(233665123);
		rNumberList.add(2145174067);
		rNumberList.add(468703135);
		rNumberList.add(1101513929);
		rNumberList.add(1801979802);
		rNumberList.add(1315634022);
		rNumberList.add(635723058);
		rNumberList.add(1369133069);
		rNumberList.add(1125898167);
		rNumberList.add(1059961393);
		rNumberList.add(2089018456);
		rNumberList.add(628175011);
		rNumberList.add(1656478042);
		rNumberList.add(1131176229);
		rNumberList.add(1653377373);
		rNumberList.add(859484421);
		rNumberList.add(1914544919);
		rNumberList.add(608413784);
		rNumberList.add(756898537);
		rNumberList.add(1734575198);
		rNumberList.add(1973594324);
		rNumberList.add(149798315);
		rNumberList.add(2038664370);
		rNumberList.add(1129566413);
		rNumberList.add(184803526);
		rNumberList.add(412776091);
		rNumberList.add(1424268980);
		rNumberList.add(1911759956);
		rNumberList.add(749241873);
		rNumberList.add(137806862);
		rNumberList.add(42999170);
		rNumberList.add(982906996);
		rNumberList.add(135497281);
		rNumberList.add(511702305);
		rNumberList.add(2084420925);
		rNumberList.add(1937477084);
		rNumberList.add(1827336327);
		rNumberList.add(572660336);
		rNumberList.add(1159126505);
		rNumberList.add(805750846);
		rNumberList.add(1632621729);
		rNumberList.add(1100661313);
		rNumberList.add(1433925857);
		rNumberList.add(1141616124);
		rNumberList.add(84353895);
		rNumberList.add(939819582);
		rNumberList.add(2001100545);
		rNumberList.add(1998898814);
		rNumberList.add(1548233367);
		rNumberList.add(610515434);
		rNumberList.add(1585990364);
		rNumberList.add(1374344043);
		rNumberList.add(760313750);
		rNumberList.add(1477171087);
		rNumberList.add(356426808);
		rNumberList.add(945117276);
		rNumberList.add(1889947178);
		rNumberList.add(1780695788);
		rNumberList.add(709393584);
		rNumberList.add(491705403);
		rNumberList.add(1918502651);
		rNumberList.add(752392754);
		rNumberList.add(1474612399);
		rNumberList.add(2053999932);
		rNumberList.add(1264095060);
		rNumberList.add(1411549676);
		rNumberList.add(1843993368);
		rNumberList.add(943947739);
		rNumberList.add(1984210012);
		rNumberList.add(855636226);
		rNumberList.add(1749698586);
		rNumberList.add(1469348094);
		rNumberList.add(1956297539);
	}
}
