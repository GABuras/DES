// George Adler Buras

package com.mycompany.des;

import java.util.*;

public class DES {
    
    // Initial permutation table
    private final static int[] initialPermutationTable = 
        {58, 50, 42, 34, 26, 18, 10, 2,
        60, 52, 44, 36, 28, 20, 12, 4,
        62, 54, 46, 38, 30, 22, 14, 6,
        64, 56, 48, 40, 32, 24, 16, 8,
        57, 49, 41, 33, 25, 17, 9, 1,
        59, 51, 43, 35, 27, 19, 11, 3,
        61, 53, 45, 37, 29, 21, 13, 5,
        63, 55, 47, 39, 31, 23, 15, 7};
    
    // Final permutation table
    private final static int[] finalPermutationTable = 
        {40, 8, 48, 16, 56, 24, 64, 32,
        39, 7, 47, 15, 55, 23, 63, 31,
        38, 6, 46, 14, 54, 22, 62, 30,
        37, 5, 45, 13, 53, 21, 61, 29,
        36, 4, 44, 12, 52, 20, 60, 28,
        35, 3, 43, 11, 51, 19, 59, 27,
        34, 2, 42, 10, 50, 18, 58, 26,
        33, 1, 41, 9, 49, 17, 57, 25};
    
    // Expansion permutation table
    private final static int[] expansionPermutationTable = 
        {32, 1, 2, 3, 4, 5, 4, 5,
         6, 7, 8, 9, 8, 9, 10, 11,
         12, 13, 12, 13, 14, 15, 16, 17,
         16, 17, 18, 19, 20, 21, 20, 21,
         22, 23, 24, 25, 24, 25, 26, 27,
         28, 29, 28, 29, 30, 31, 32, 1};
    
    //Straight permutation table
    private final static int[] straightPermutationTable = 
     {16,  7, 20, 21,
       29, 12, 28, 17,
       1, 15, 23, 26,
       5, 18, 31, 10,
       2,  8, 24, 14,
       32, 27,  3,  9,
       19, 13, 30,  6,
       22, 11,  4, 25};
    
    // S-box tables: Substitute and Shrink
    private final static int[][][] sBoxTables = 
        {{{14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
         {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
         {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
         {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}},
 
        {{15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
         {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
         {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
         {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}},
 
        {{10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
         {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
         {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
         {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}},
 
        {{7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
         {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
         {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
         {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}},
 
        {{2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
         {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
         {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
         {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}},
 
        {{12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
         {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
         {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
         {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}},
 
        {{4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
         {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
         {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
         {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}},
 
        {{13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
         {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
         {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
         {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}}};
    
    // Size of circular shift to the left by round
    private final static int[] keyShiftTable = 
        {1, 1, 2, 2,
        2, 2, 2, 2,
        1, 2, 2, 2,
        2, 2, 2, 1};
    
    // Key compression table
    private final static int[] keyCompressionTable = 
        {14, 17, 11, 24, 1, 5,
        3, 28, 15, 6, 21, 10,
        23, 19, 12, 4, 26, 8,
        16, 7, 27, 20, 13, 2,
        41, 52, 31, 37, 47, 55,
        30, 40, 51, 45, 33, 48,
        44, 49, 39, 56, 34, 53,
        46, 42, 50, 36, 29, 32};
    
    // Converts a string of text into its eqivalent binary representation (8-bits per char)
    public static String textToBinary(String text) {
        char[] characters = text.toCharArray();
        StringBuilder binary = new StringBuilder();
        
        for (int i = 0; i < characters.length; i++) {
            binary.append(String.format("%8s",Integer.toBinaryString(characters[i])).replaceAll(" ", "0"));
        }
        
        return binary.toString();
    }
    
    // Converts an integer, num, into its eqivalent [len]-bit binary representation
    public static String intToBinary(int num, int len) {
        StringBuilder binary = new StringBuilder(Integer.toBinaryString(num));
        
        while (binary.length() < len) 
            binary.insert(0, '0');
        
        return binary.toString();
    }
    
    // Converts a binary stream into its equivalent human readable text
    public static String binaryToText(String binary) {
        StringBuilder text = new StringBuilder();
        
        Arrays.stream(binary.split("(?<=\\G.{8})")).forEach(s -> text.append((char) Integer.parseInt(s, 2)));
        
        return text.toString();
    }
    
    // Converts a String array into a string
    public static String stringArrayToString (String[] stringArray) {
        StringBuilder str = new StringBuilder();
        
        for (int i = 0; i < stringArray.length; i++)
            str.append(stringArray[i]);
        
        return str.toString();
    }
    
    // XORs the bits of two binary strings of equal length
    public static String xorStrings(String binary1, String binary2) {
        StringBuilder result = new StringBuilder();
        
        if (binary1.length() != binary2.length()) {
            System.out.println("ERROR: Binary strings passed to xorStrings are not the same length.");
            System.out.printf("Binary 1 length: %d\nBinary 2 length: %d\n", binary1.length(), binary2.length());
        }
        else {
            for (int i = 0; i < binary1.length(); i++) {
                if (binary1.charAt(i) == binary2.charAt(i))
                    result.append("0");
                else
                    result.append("1");
            }
        }            
        
        return result.toString();
    }
    
    // returns '1' if there is an even number of 1s in the binary
    // returns '0' if there is an odd number of 1s in the binary
    public static char calculateParity(String binary) {
        char parity;
        // the number of 1s in the binary number
        int count = 0;
        char c;
        
        for (int i = 0; i < binary.length(); i++) {
            c = binary.charAt(i);
            if (c == '1') 
                count++;
            else if (c != '0') 
                System.out.println("ERROR: String passed to calculate Parity is not a binary number");
        }
        
        if (count % 2 == 0)
            parity = '1';
        else
            parity = '0';
        
        return parity;
    }
    
    // Checks that the parity bits of a 64-bit key are correct
    public static String checkParity64(String binary) {
        int index = 0;
        StringBuilder temp = new StringBuilder();
        
        for (int i = 0; i < 8; i++) {
            temp.append(binary.substring(index, index+7));
            if (calculateParity(temp.toString()) != binary.charAt(index+7))
                return "ERROR: 64-bit key failed parity integrity check\n";
            
            temp.setLength(0);
            index += 8;
        }
        
        return "64-bit key passed parity integrity check\n";
    }
    
    // Generates the 64-bit symmetric key used for DES
    public static String generateKey64() {
        StringBuilder key64 = new StringBuilder();
        Random rand = new Random();
        int i, j, r;
        
        for (i = 0; i < 8; i++) {
            StringBuilder temp = new StringBuilder();
            
            for (j = 0; j < 7; j++) {
                r = rand.nextInt(2);
                temp.append(String.valueOf(r));
            }
            
            temp.append(calculateParity(temp.toString()));
            
            key64.append(temp);
        }
        
        return key64.toString();
    }
    
    // Removes parity bits from 64-bit DES key to get 56-bit key
    public static String getKey56(String key64) {
        if (key64.length() != 64)
            System.out.println("ERROR: Key passed to getKey56 does not have 64-bits");
        
        StringBuilder key56 = new StringBuilder(key64);
        
        for (int i = 63; i >= 7; i-=8)
            key56.deleteCharAt(i);
        
        if (key56.length() != 56)
            System.out.println("ERROR: Key generated by getKey56 is not 56-bits long");
                
        return key56.toString();
    }
    
    // Generates 48-bit round keys from the 56-bit key
    public static String[] generateRoundKeys48(String key56) {
        String[] roundKeys48 = new String[16];
        // Split key
        StringBuilder leftHalf = new StringBuilder(key56.substring(0, 28));
//        System.out.printf("leftHalf = %s\n", leftHalf);
        StringBuilder rightHalf = new StringBuilder(key56.substring(28,56));
//        System.out.printf("rightHalf = %s\n", rightHalf);
        StringBuilder combinedKey = new StringBuilder();

        for (int i = 0; i < 16; i++) {
            // Circularly shift to the left according to the key shift table
            leftHalf.replace(0, leftHalf.length(), circularLeftShift(leftHalf.toString(), keyShiftTable[i]));
            rightHalf.replace(0, rightHalf.length(), circularLeftShift(rightHalf.toString(), keyShiftTable[i]));

            // combine key
            combinedKey.append(leftHalf);
            combinedKey.append(rightHalf);
            
            // compress key
            combinedKey.replace(0, combinedKey.length(), permute(combinedKey.toString(), keyCompressionTable, 48)); 
            if (combinedKey.length() != 48)
                System.out.printf("ERROR: Combined key improperly compressed. Length should be 48, but is %d.\n", combinedKey.length());
            
            roundKeys48[i] = combinedKey.toString();
            
            combinedKey.setLength(0);
        }
        
        return roundKeys48;
    }
    
    // Rearranges the bits of the text according to the table
    public static String permute(String text, int[] permutationTable, int size) {
        StringBuilder permutation = new StringBuilder();
        
        for(int i = 0; i < size; i++)
            permutation.append(text.charAt(permutationTable[i]-1));
        
        return permutation.toString();
    }
    
    // Circularly shifts n bits to the left
    public static String circularLeftShift(String key, int numOfShifts) {
        StringBuilder shiftedKey = new StringBuilder();
        
        // for each shift
        for (int i = 0; i < numOfShifts; i++) {
            // shift all the bits to the left by one
            for (int j = 1; j < key.length(); j++) 
                shiftedKey.append(key.charAt(j));
            shiftedKey.append(key.charAt(0));
            key = shiftedKey.toString();
            shiftedKey.setLength(0);
        }
        
        return key;
    }
    
    // Splits textBinary into 64-bit blocks, padding at end
    public static String[] blockTextPad64(String textBinary) {
        int numOfBlocks = textBinary.length()/64 + 1; // +1 to account for remainder
        String[] textBinaryBlocks = new String[numOfBlocks];
        StringBuilder textBinaryBlock = new StringBuilder();
        
        for (int i = 0; i < numOfBlocks-1; i++) {
            textBinaryBlock.append(textBinary.substring(i*64, (i+1)*64));
            textBinaryBlocks[i] = textBinaryBlock.toString();
            textBinaryBlock.setLength(0);
        }
        
        if (textBinary.length() % 64 == 0)
            textBinaryBlock.append("1000000000000000000000000000000000000000000000000000000000000000");
        else {
            String temp = textBinary.substring((numOfBlocks-1)*64);
            textBinaryBlock.append(temp);
            textBinaryBlock.append("1");
            int numOf0s = 64 - temp.length() - 1;
            for (int i = 0; i < numOf0s; i++)
                textBinaryBlock.append("0");
        }
        textBinaryBlocks[numOfBlocks-1] = textBinaryBlock.toString();
        
        return textBinaryBlocks;
    }
    
    // Splits textBinary into 64-bit blocks, assumes input is already padded to be divisible by 64
    public static String[] blockText64(String textBinary) {
        int numOfBlocks = textBinary.length()/64; 
        String[] textBinaryBlocks = new String[numOfBlocks];
        StringBuilder textBinaryBlock = new StringBuilder();
        
        for (int i = 0; i < numOfBlocks; i++) {
            textBinaryBlock.append(textBinary.substring(i*64, (i+1)*64));
            textBinaryBlocks[i] = textBinaryBlock.toString();
            textBinaryBlock.setLength(0);
        }
        
        return textBinaryBlocks;
    }
    
    // Removes the padding at the end of a string
    public static String removePad(String textBinary) {
        boolean padStartFound = false;
        int index = textBinary.length() - 1;
        
        while(!padStartFound) {
            if (textBinary.charAt(index) == '1')
                padStartFound = true;
            else if (textBinary.charAt(index) == '0')
                index--;
            else {
                System.out.println("ERROR: Text binary passed to removePad is not a binary.");
                padStartFound = true;
            }
        }
        
        textBinary = textBinary.substring(0, index);
        
        return textBinary;
    }
    
    // Encrypts a 64-bit plaintext block using the 16 48-bit round keys generated from the 64-bit key
    // Providng the the 16 48-bit round keys in reverse order decrypts the 64-bit ciphertext block
    public static String encryptBlock(String plainBinaryBlockString, String[] roundKey48) {
        StringBuilder plainBinaryBlock = new StringBuilder(plainBinaryBlockString);
        System.out.printf("Input binary block: %s\n", plainBinaryBlock);
        
        // Initial permutation
        plainBinaryBlock.replace(0, plainBinaryBlock.length(), permute(plainBinaryBlock.toString(), initialPermutationTable, 64));
        System.out.printf("\tInitial permutation: %s\n", plainBinaryBlock);

        // Split plaintext binary into left and right halves
        // Left Plaintext
        StringBuilder LPT = new StringBuilder(plainBinaryBlock.substring(0, 32));
        // Right Plaintext
        StringBuilder RPT = new StringBuilder(plainBinaryBlock.substring(32, 64));
        
        System.out.printf("\tLeft half: %s\n", LPT);
        System.out.printf("\tRight half: %s\n", RPT);
        
        // Mangled text (temporary storage)
        StringBuilder mangled = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        StringBuilder rowBinary = new StringBuilder();
        StringBuilder colBinary = new StringBuilder();
        StringBuilder valBinary = new StringBuilder();
        int row, col, val;

        
        // 16 rounds of encryption
        for (int i = 0; i < 16; i++) {
            // Expand the right plaintext. Expansion called mangled text
            mangled.append(permute(RPT.toString(), expansionPermutationTable, 48));
            
            // XOR the mangled text with the 48-bit round key
            mangled.replace(0, mangled.length(), xorStrings( mangled.toString(), roundKey48[i]));
            
            // Substitute and shrink the mangled text
            temp.setLength(0);
            for (int j = 0; j < 8; j++) {
                rowBinary.append(mangled.charAt(j * 6));
                rowBinary.append(mangled.charAt(j * 6 + 5));
                row = Integer.parseInt(rowBinary.toString(), 2);
                
                colBinary.append(mangled.charAt(j * 6 + 1));
                colBinary.append(mangled.charAt(j * 6 + 2));
                colBinary.append(mangled.charAt(j * 6 + 3));
                colBinary.append(mangled.charAt(j * 6 + 4));
                col = Integer.parseInt(colBinary.toString(), 2);
                
                val = sBoxTables[j][row][col];
                valBinary.append(intToBinary(val, 4));
                
                temp.append(valBinary);
                
                rowBinary.setLength(0);
                colBinary.setLength(0);
                valBinary.setLength(0);
            }
                        
            // Permutate the mangled text
            mangled.replace(0, mangled.length(), permute(temp.toString(), straightPermutationTable, 32));
            
            // XOR the mangled text with the left plaintext
            mangled.replace(0, mangled.length(), xorStrings( mangled.toString(), LPT.toString()));
            
            
            // Swap if not last round
            if (i != 15) {
                LPT.replace(0, LPT.length(), RPT.toString());
                RPT.replace(0, RPT.length(), mangled.toString());
            }
            else
                LPT.replace(0, LPT.length(), mangled.toString());
            
            // Reset mangledtext
            mangled.setLength(0);
            
            System.out.printf("Round %d: \n", i + 1);

            System.out.printf("\tLeft half: %s\n", LPT);
            System.out.printf("\tRight half: %s\n", RPT);
        }

        // Rejoin the left ciphertext binary and the right ciphertext binary
        StringBuilder cipherBinaryBlock = new StringBuilder();
        cipherBinaryBlock.append(LPT);
        cipherBinaryBlock.append(RPT);
        System.out.printf("Output binary block: %s\n", cipherBinaryBlock);

        // Final permutation
        cipherBinaryBlock.replace(0, cipherBinaryBlock.length(), permute(cipherBinaryBlock.toString(), finalPermutationTable, 64));
        System.out.printf("Final permutation: %s\n\n", cipherBinaryBlock);

        return cipherBinaryBlock.toString();
    }
    
    // Encrypts the plaintext with the key to produce a ciphertext
    public static String encrypt(String plaintext, String key64) {
        // Convert plaintext to its binary representation
        StringBuilder plainBinary = new StringBuilder(textToBinary(plaintext));
        System.out.printf("\tPlaintext binary: %s\n", plainBinary);
        
        // Check the integrity of the 64-bit key
        String integrityCheck = checkParity64(key64);
        System.out.printf("\t%s", integrityCheck);
        if (integrityCheck.equals("ERROR: 64-bit key failed parity integrity check\n"))
            return "Encryption stopped because the 64-bit key is bad.";
        
        // Derive the 56-bit key from the 64-bit key, removing parity bits
        String key56 = getKey56(key64);
        System.out.printf("\tDerived 56-bit key: %s\n", key56);
        
        // Generate 48-bit round keys
        String[] roundKeys48 = generateRoundKeys48(key56);
        System.out.println("\t48-bit Round Keys: ");
        for (int i = 0; i < roundKeys48.length; i++) 
            System.out.printf("\t\t %d: %s\n", i+1, roundKeys48[i]);
        System.out.println("");
        
        // Split the plaintext binary into plaintext binary blocks
        String[] plainBinaryBlocks = blockTextPad64(plainBinary.toString());
        
        // Encrypt each plaintext binary block
        String[] cipherBinaryBlocks = new String[plainBinaryBlocks.length];
        for (int i = 0; i < plainBinaryBlocks.length; i++) {
            System.out.printf("Encrypting block %d\n", i+1);
            cipherBinaryBlocks[i] = encryptBlock(plainBinaryBlocks[i], roundKeys48);
        }

        // Put ciphertext binary blocks together
        String cipherBinary = stringArrayToString(cipherBinaryBlocks);
        System.out.printf("Ciphertext binary: %s\n", cipherBinary);
        
        // Convert ciphertext binary into ciphertext
        String ciphertext = binaryToText(cipherBinary);
        return ciphertext;
    }
    
    // Decrypts the ciphertext with the key to produce a plaintext
    public static String decrypt(String ciphertext, String key64) {
        
        // Convert ciphertext to its binary representation
        StringBuilder cipherBinary = new StringBuilder(textToBinary(ciphertext));
        System.out.printf("\tCiphertext binary: %s\n", cipherBinary);
        
        // Check the integrity of the 64-bit key
        String integrityCheck = checkParity64(key64);
        System.out.printf("\t%s", integrityCheck);
        if (integrityCheck.equals("ERROR: 64-bit key failed parity integrity check\n"))
            return "Decryption stopped because the 64-bit key is bad.";
        
        // Derive the 56-bit key from the 64-bit key, removing parity bits
        String key56 = getKey56(key64);
        System.out.printf("\tDerived 56-bit key: %s\n", key56);
        
        // Generate 48-bit round keys
        String[] roundKeys48 = generateRoundKeys48(key56);
        
        // Reverse order of 48-bit round keys
        String[] reverseRoundKeys48 = new String[roundKeys48.length];
        int f, b;
        b = roundKeys48.length - 1;
        for (f = 0; f < reverseRoundKeys48.length; f++) {
            reverseRoundKeys48[b] = roundKeys48[f];
            b -= 1;
        }
        
        System.out.println("\tReverse order 48-bit Round Keys: ");
        for (int i = 0; i < reverseRoundKeys48.length; i++) 
            System.out.printf("\t\t %d: %s\n", i+1, reverseRoundKeys48[i]);
        System.out.println("");
        
        // Split the ciphertext binary into ciphertext binary blocks
        String[] cipherBinaryBlocks = blockText64(cipherBinary.toString());
        
        // Decrypt each ciphertext binary block
        String[] plainBinaryBlocks = new String[cipherBinaryBlocks.length];
        for (int i = 0; i < cipherBinaryBlocks.length; i++) {
            System.out.printf("Decrypting block %d\n", i+1);
            plainBinaryBlocks[i] = encryptBlock(cipherBinaryBlocks[i], reverseRoundKeys48);
        }

        // Put plaintext binary blocks together
        String plainBinary = stringArrayToString(plainBinaryBlocks);
        System.out.printf("Plaintext binary with padding: %s\n", plainBinary);

        
        // Remove padding
        plainBinary = removePad(plainBinary);
        System.out.printf("Plaintext binary: %s\n", plainBinary);

        
        // Convert plaintext binary into plaintext
        String plaintext = binaryToText(plainBinary);
        return plaintext;
    }

    public static void main(String[] args) {
        System.out.println("DES implementation demonstration: \n");
        
        String plaintext = "Hello World!";
        System.out.printf("Plaintext: %s\n\n", plaintext);
        
        String key64 = generateKey64();
        System.out.printf("New 64-bit key: %s\n\n", key64);
        
        System.out.println("Begin encryption: ");
        String ciphertext = encrypt(plaintext, key64);
        System.out.printf("Ciphertext: %s\n\n", ciphertext);

        System.out.println("Begin decryption: ");
        String decryptedtext = decrypt(ciphertext, key64);
        System.out.printf("Decrypted text = %s\n\n", decryptedtext);
        
        // Test to see if it worked
        if (plaintext.equals(decryptedtext))
            System.out.println("Success!\n");
        else
            System.out.println("Failure.\n");
    }
}
