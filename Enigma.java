import java.util.*;


/**
 * 
 * @author Alexander Belanger
 * This is the final file for the final ENIGMA project.
 * It also includes previous checkpoints.
 * BTW, the letter F has been giving me some trouble here and there when comes first, but it should be fine
 * 
 * This program should work most of the time so don't worry
 * This sadly doesn't have double kicking... I tried, and every time it screwed up my results, so i chose to leave it out.
 */

public class Enigma {
	static boolean w = true;
	static String plugboard = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static String reflector = "YRUHQSLDPXNGOKMIEBFZCWVJAT";
	static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public static void main(String[]args){
			System.out.println("ENIGMA");
			Scanner inputScanner = new Scanner(System.in);
			String inputString = inputScanner.nextLine();
			if(!inputString.contains("CRYPT")){
				
			Scanner input = new Scanner(inputString);
			
			Rotor leftRotor = new Rotor(input.next());
			Rotor middleRotor = new Rotor(input.next());
			Rotor rightRotor = new Rotor(input.next());
			
			String leftShift = input.next();
			String middleShift = input.next();
			String rightShift = input.next();
			
			String[] plugboardSettings = {input.next(),input.next(),input.next(),input.next(),input.next(),
					input.next(),input.next(),input.next(),input.next(),input.next()};
			
			String cipher = input.nextLine().substring(1);
			System.out.println(cipher);
			
			//Plugboard setup
			String newPlugboard = plugboard;
			char[] swap = newPlugboard.toCharArray();
			for(String setting : plugboardSettings){
				char target = setting.charAt(0);
				char change = setting.charAt(1);
				swap[newPlugboard.indexOf(target)] = change;
				swap[newPlugboard.indexOf(change)] = target;
			}
			newPlugboard = new String(swap);
			plugboard = newPlugboard;
			System.out.println("Plugboard Setting is: ABCDEFGHIJKLMNOPQRSTUVWXYZ\n"
	          +"                      ||||||||||||||||||||||||||\n"
	          +"                  To: "+plugboard);
			
			//Rotor position setup
			leftRotor.startPosition(leftShift);
			middleRotor.startPosition(middleShift);
			rightRotor.startPosition(rightShift);
			
			//Cipher LET'S DO THIS!!!
			char[] cipherArray = cipher.toCharArray();
			for(int i = 0; i < cipherArray.length; i++){
				if(cipherArray[i] >= 65 && cipherArray[i] <= 90){
					
					//lets kick this off
					rightRotor.kick();
					
				//Notch position check on run
					//left notch crossing? check
					if(rightRotor.kickMap.substring(0,1).equals(rightRotor.notchLetter.substring(1))){
						middleRotor.kick();
					}
					//middle notch crossing? check
					if(middleRotor.kickMap.substring(0,1).equals(middleRotor.notchLetter.substring(1))){
						leftRotor.kick();
					}
					
					//send letter through plugboard
					cipherArray[i] = plugboard.charAt(cipherArray[i]-65);
					
					//send through right rotor
					int indexOfLast = rightRotor.convertIn(alphabet.indexOf(cipherArray[i]));
					
					//send through middle rotor
					indexOfLast = middleRotor.convertIn(indexOfLast);
					
					//send through left rotor
					indexOfLast = leftRotor.convertIn(indexOfLast);

					//send through reflector
					cipherArray[i] = reflector.charAt(alphabet.charAt(indexOfLast)-65);
					indexOfLast = alphabet.indexOf(cipherArray[i]);
					
					//send back through left rotor
					indexOfLast = leftRotor.convertOut(indexOfLast);
					
					//send back through middle rotor
					indexOfLast = middleRotor.convertOut(indexOfLast);
					
					//send back through right rotor
					indexOfLast = rightRotor.convertOut(indexOfLast);
					
					//send back through plugboard
					cipherArray[i] = plugboard.charAt(alphabet.charAt(indexOfLast)-65);
					
				}
			}
			//After all the letters have been Encrypted Successfully, display the string!
			String s = new String(cipherArray);
			System.out.println();
			System.out.println(s);
		} else {
			while(w){
				String st = inputString;
				if(st.contains("quit")){
					System.out.println("EXIT");
					w = false;
				} else if(st.substring(0,8).equals("ENCRYPT:")){
					String input = st.substring(9, st.length());
					System.out.println(caesarIn(input, 1));
					quit();
				} else if(st.substring(0,8).equals("ENCRYPT ")){
					int shift = Integer.parseInt(st.substring(st.indexOf(" ")+1, st.indexOf(":")));
					String[] in = st.split(": ");
					System.out.println(caesarIn(in[1], shift));
					quit();
				} else if(st.substring(0,8).equals("DECRYPT:")){
					String input = st.substring(9, st.length());
					System.out.println(caesarOut(input, 1));
					quit();
				} else if(st.substring(0,8).equals("DECRYPT ")){
					int shift = Integer.parseInt(st.substring(st.indexOf(" ")+1, st.indexOf(":")));
					String[] in = st.split(": ");
					System.out.println(caesarOut(in[1], shift));
					quit();
				}else if(st.substring(0,14).equals("AFFINEENCRYPT:")){
					String input = st.substring(15, st.length());
					System.out.println(affineIn(input, 1));
					quit();
				}else if(st.substring(0,14).equals("AFFINEDECRYPT:")){
					String input = st.substring(15, st.length());
					System.out.println(affineOut(input, 1));
					quit();
				}else if(st.substring(0,13).equals("ROTORENCRYPT:")){
					String input = st.substring(14, st.length());
					System.out.println(affineIn(affineIn(affineIn(input,1),2),3));
					quit();
				}else if(st.substring(0,13).equals("ROTORDECRYPT:")){
					String input = st.substring(14, st.length());
					System.out.println(affineOut(affineOut(affineOut(input,3),2),1));
					quit();
				}
			}
	 	}
	}
	static String caesarIn(String original, int shift){
		char[] input = original.toCharArray();
		//Only possible options 1-26, all high values converted into within that range because of being repeated
		if(shift > 26){
			shift = 0 + (shift % 26);
		}
		for(int i = 0; i < original.length(); i++){
			if(input[i] != ' '){
				int temp = input[i];
				if(temp == 90){
					temp = 65 + (shift - 1);
					input[i] = (char)temp;
				} else {
					temp = temp + shift;
					if(temp > 90){
						temp = (temp - 90) + 64;
					}
					input[i] = (char)temp;
				}
			}
		}
		String output = new String(input);
		return output;
	}
	static String caesarOut(String encrypted, int shift){
		if(shift > 26){
			shift = 0 + (shift % 26);
		}
		char[] input = encrypted.toCharArray();
		for(int i = 0; i < encrypted.length(); i++){
			if(input[i] != ' '){
				int temp = input[i];
				temp = temp - shift;
				if(temp < 65){
					temp = (90 + temp) - 64;
				}
				input[i] = (char)temp;
			}
		}
		String output = new String(input);
		return output;
	}
	static String affineIn(String original, int map){
		String lettermap = "";
		if(map == 1){
			lettermap = "QWERTYUIOPASDFGHJKLZXCVBNM";
		} else if (map == 2){
			lettermap = "ZAQWSXCDERFVBGTYHNMJUIKLOP";
		} else if (map == 3){
			lettermap = "QPWOEIRUTYALSKDJFHGZMXNCBV";
		}
		char[] input = original.toCharArray();
		for(int i = 0; i < original.length(); i++){
			if(input[i] != ' '){
				if(lettermap.indexOf(input[i])+1 <= 25){
					char n = lettermap.charAt(lettermap.indexOf(input[i])+1);
					input[i] = n;
				} else {
					char n = lettermap.charAt(0);
					input[i] = n;
				}
			}
		}
		String output = new String(input);
		return output;
	}
	static String affineOut(String encrypted, int map){
		String lettermap = "";
		if(map == 1){
			lettermap = "QWERTYUIOPASDFGHJKLZXCVBNM";
		} else if (map == 2){
			lettermap = "ZAQWSXCDERFVBGTYHNMJUIKLOP";
		} else if (map == 3){
			lettermap = "QPWOEIRUTYALSKDJFHGZMXNCBV";
		}
		char[] input = encrypted.toCharArray();
		for(int i = 0; i < encrypted.length(); i++){
			if(input[i] != ' '){
				if(lettermap.indexOf(input[i])-1 >= 0){
					char n = lettermap.charAt(lettermap.indexOf(input[i])-1);
					input[i] = n;
				}else{
					char n = lettermap.charAt(25);
					input[i] = n;
				}
			}
		}
		String output = new String(input);
		return output;
	}
	static void quit(){
		w = false;
	}
}
