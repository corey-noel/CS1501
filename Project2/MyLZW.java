public class MyLZW {

    private static final double resetRatio = 1.1;
    private static final int numChars = 256;
    private static final int maxWidth = 16;
    private static int codeWidth = 9;
    private static int maxCodes = 512;
    private static int codeMode = 0;
    // 0 = do nothing
    // 1 = reset
    // 2 = monitor

    public static void main(String[] args) {
        if (args[0].equals("-")) {
            if (args.length > 1) {
                if (args[1].equals("r"))                                    // reset mode
                    codeMode = 1;
                else if (args[1].equals("m"))                               // monitor mode
                    codeMode = 2;
            }

            compress();
        } else if (args[0].equals("+"))  {
            expand();
        } else 
            throw new IllegalArgumentException("Illegal command line argument");
    }


    public static void compress() {        
        System.err.printf("Mode %d\n", codeMode);
        System.err.println("Compressing...");
        BinaryStdOut.write(codeMode, 8);                                    // write codemode

        String input = BinaryStdIn.readString();                            // read full input
        TST<Integer> table = new TST<Integer>();                            // construct table
        int codeNum = 0;                                                    // next codeword
        String prefix = "";

        int amtIn = 0;
        int amtOut = 0;
        double maxRatio = -1;
        double minRatio = -1;
        double ratio;

        for (codeNum = 0; codeNum < numChars; codeNum++)                    // set all chars to themselves
            table.put("" + (char)codeNum, codeNum);

        codeNum = numChars + 1;                                             // skip EOF char (256)

        while (input.length() > 0) {
            prefix = table.longestPrefixOf(input);                          // get the longest next code

            BinaryStdOut.write(table.get(prefix), codeWidth);               // write its number code
            amtOut += codeWidth;

            if (prefix.length() < input.length()) {
                if (codeNum < maxCodes) {                                   // if book isn't full add it
                    table.put(input.substring(0, prefix.length() + 1), codeNum);
                    codeNum++;
                } else {                                                    // book is full
                    if (codeWidth < maxWidth) {                             // if book not max size
                        System.err.printf("Increasing size to %d on cycle %d\n", codeWidth + 1, codeNum);
                        codeWidth++;                                        // increase size
                        maxCodes *= 2;
                        table.put(input.substring(0, prefix.length() + 1), codeNum);
                        codeNum++;

                    } else if (codeMode == 1) {                             // reset mode
                        System.err.println("Clearing codebook...");
                        table = new TST<Integer>();                         // clear codebook
                        codeNum = 0; 

                        for (codeNum = 0; codeNum < numChars; codeNum++)    // set all chars to themselves
                            table.put("" + (char)codeNum, codeNum);

                        codeNum = numChars + 1;
                        prefix = table.longestPrefixOf(input);              // refind prefix

                        table.put(input.substring(0, prefix.length() + 1), codeNum);
                        codeNum++;

                    } else if (codeMode == 2) {                             // monitor mode
                        ratio = (double)amtOut / (double)amtIn;             // monitor ratios
                        if (ratio > maxRatio || maxRatio == -1)
                            maxRatio = ratio;
                        if (ratio < minRatio || minRatio == -1)
                            minRatio = ratio;

                        if (maxRatio / minRatio > resetRatio) {             // clear > resetRatio
                            System.err.println("Ratio exceeded, clearing codebook...");
                            table = new TST<Integer>();                            
                            codeNum = 0; 

                            for (codeNum = 0; codeNum < numChars; codeNum++)    
                                table.put("" + (char)codeNum, codeNum);

                            codeNum = numChars + 1;
                            prefix = table.longestPrefixOf(input);             

                            table.put(input.substring(0, prefix.length() + 1), codeNum);
                            codeNum++;
                        }
                    }
                }
            }

            input = input.substring(prefix.length());                       // shrink input
            amtIn += prefix.length() * 8;
        }
        BinaryStdOut.write(numChars, codeWidth);                            // write EOF
        BinaryStdOut.close();
    }



    public static void expand() {
        codeMode = BinaryStdIn.readInt(8);                                  // read in code mode
        System.err.printf("Mode %d\n", codeMode);
        System.err.println("Decompressing...");


        String[] table = new String[maxCodes - 1];                          // construct table
        int codeNum = 0;                                                    // next codeword

        int amtIn = 0;
        int amtOut = 0;
        double maxRatio = -1;
        double minRatio = -1;
        double ratio;


        for (codeNum = 0; codeNum < numChars; codeNum++)                    // set all chars to themselves
            table[codeNum] = "" + (char)codeNum;

        table[codeNum] = "";                                                // skip EOF char (256)
        codeNum++;

        int inputNum = BinaryStdIn.readInt(codeWidth);                      // read first char
        amtIn += codeWidth;
        if (inputNum == numChars) {                                         // file is blank
            BinaryStdOut.close();
            return;
        }

        String inputString = table[inputNum];         
        BinaryStdOut.write(inputString);                                    // output first char
        amtOut -= inputString.length() * 8;

        inputNum = BinaryStdIn.readInt(codeWidth);
        String lastInput = inputString;
        amtIn += codeWidth;

        while (inputNum != numChars) {                                      // while !EOF
            inputString = table[inputNum];
            if (inputNum == codeNum)                                        // code is code not yet input
                inputString = lastInput + lastInput.charAt(0);
            if (codeNum < maxCodes - 1) {                                   // if codebook not full
                table[codeNum] = lastInput + inputString.charAt(0);         // add code
                codeNum++;
            } else {                                                        // else codebook is full
                if (codeWidth < maxWidth) {                                 // if not max size
                    System.err.printf("Increasing size to %d on cycle %d\n", codeWidth + 1, codeNum);
                    codeWidth++;                                            // increase codebook size   
                    maxCodes *= 2;
                    String[] temp = new String[maxCodes];
                    for (int i = 0; i < table.length; i++)
                        temp[i] = table[i];
                    table = temp;
                    table[codeNum] = lastInput + inputString.charAt(0);
                    codeNum++;
                } else if (codeMode == 1) {                                 // reset mode
                    System.err.println("Clearing codebook...");
                    table = new String[maxCodes];                           // reset codebook
                    for (codeNum = 0; codeNum < numChars; codeNum++)
                        table[codeNum] = "" + (char)codeNum;

                    table[codeNum] = "";                                    // skip EOF char
                    codeNum++;                                              // NO OUTPUT TO CODEBOOK

                } else if (codeMode == 2) {                                 // monitor mode
                    ratio = (double)amtOut / (double)amtIn;                 // monitor ratios
                    if (ratio > maxRatio || maxRatio == -1)
                        maxRatio = ratio;
                    if (ratio < minRatio || minRatio == -1)
                        minRatio = ratio;

                    if (maxRatio / minRatio > resetRatio) {                 // clear > resetRatio
                        System.err.println("Ratio exceeded, clearing codebook...");
                        table = new String[maxCodes];                           
                        for (codeNum = 0; codeNum < numChars; codeNum++)
                            table[codeNum] = "" + (char)codeNum;

                        table[codeNum] = "";                                    
                        codeNum++;                                              
                    }
                }
            }
            BinaryStdOut.write(inputString);
            amtOut += inputString.length() * 8;

            lastInput = inputString;
            inputNum = BinaryStdIn.readInt(codeWidth);
            amtIn += codeWidth;
        }
        BinaryStdOut.close();
    }
}