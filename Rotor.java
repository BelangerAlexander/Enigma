
public class Rotor {
	
	static String rotorI = "EKMFLGDQVZNTOWYHXUSPAIBRCJ";
	static String rotorII = "AJDKSIRUXBLHWTMCQGZNPYFVOE";
	static String rotorIII = "BDFHJLCPRTXVZNYEIWGAKMUSQO";
	static String rotorIV = "ESOVPZJAYQUIRHXLNFTGKDCMWB";
	static String rotorV = "VZBRGITYUPSDNHLXAWMJQOFECK";
	
	static String baseMap = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public String kickMap = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public String rotorMap = "";
	
	public String mapLabel = "";
	public String notchLetter = "";
	
	public Rotor(String input){
		if(input.equals("I")){
			rotorMap = rotorI;
			mapLabel = "I";
			notchLetter = "QR";
		}else if(input.equals("II")){
			rotorMap = rotorII;
			mapLabel = "II";
			notchLetter = "EF";
		}else if(input.equals("III")){
			rotorMap = rotorIII;
			mapLabel = "III";
			notchLetter = "VW";
		}else if(input.equals("IV")){
			rotorMap = rotorIV;
			mapLabel = "IV";
			notchLetter = "JK";
		}else if(input.equals("V")){
			rotorMap = rotorV;
			mapLabel = "V";
			notchLetter = "ZA";
		}
	}
	
	public void kick(){
		String temp = kickMap.substring(1, kickMap.length());
		char last = kickMap.charAt(0);
		kickMap = temp+last;
		
	}
	public void startPosition(String startPosition){
		int kicks = kickMap.indexOf(startPosition);
		System.out.println();
		for(int i = 0; i < kicks; i++){
			this.kick();
		}
		System.out.println("Rotor "+mapLabel+" is set on: ["+kickMap.charAt(0)+"]"+kickMap.substring(1));
	}
	public int convertIn(int indexOfLast){
		int index = indexOfLast;
		char letter = kickMap.charAt(index);
		letter = rotorMap.charAt(baseMap.indexOf(letter));
		index = kickMap.indexOf(letter);
		return index;
	}
	
	public int convertOut(int indexOfLast){
		int index = indexOfLast;
		char letter = kickMap.charAt(index);
		letter = baseMap.charAt(rotorMap.indexOf(letter));
		index = kickMap.indexOf(letter);
		return index;
	}
}

