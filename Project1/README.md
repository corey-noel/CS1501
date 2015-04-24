# CS/COE 1501 Assignment 1

Posted:  Jan. 16, 2015

##Goal:
To demonstrate knowledge of both exhaustive search of a problem space and lookup search through the implementation of a modified version of the game Boggle.

##Background:
Given a 4x4 grid of letters, Boggle is played by having users identify as many valid English words of at least three characters that can be made by joining adjacent letters on the boggle board.
Adjacent letters can be found horizontally, vertically, or diagonally next to one another.
Note that the same space the grid cannot be used twice in a given word.

Consider the following board:

| | | | |
|---|---|---|---|
|F|R|O|O|
|Y|I|E|S|
|L|D|N|T|
|A|E|R|E|

FRIEND, ROSTER, and FROST are all valid words.
DEAD is not a valid word as you would need to use the same D twice to construct it.

For this assignment, we will consider a modified version of Boggle where wildcard characters are allowed.
The "*" character will be considered a wildcard and can be considered any letter of the alphabet when constructing words.
For example, in the following board:

| | | | |
|---|---|---|---|
|F|R|O|O|
|Y|I|E|S|
|L|*|N|T|
|A|E|R|E|

RIVER, RING, and TRAIL are all valid words where they would not have been in the previous puzzle.

##Specifications:
* Your Boggle implementation should present the user with a board and prompt the user to enter as many words as they can find in that board. For each word entered, you should inform the user whether or not the word they entered was valid. Once the user indicates that they have finished entering words, your game should do the following:
	* Print out all of the possible words for the board in alphabetical order.
	* Show the user the list of valid words he/she correctly guessed.
	* Show the user the number of valid words that he/she correctly guessed.
	* Show the user the number of possible words for the board.
	* Show the user the percentage of possible words that he/she correctly guessed.
* The flow of your program should be as follows:
	* When the program is run, any needed command line arguments should be parsed and interpereted, setting flags for the running of the game as needed.
	* Your program should load the appropriate board from a file.
	* Your program should load a cannonical dictionary for the game that will be used to determine valid words.
	* Your program should then compile a list of all valid words in the current board.
	* Your program should allow the user to play the game as described above.
* To compile the list of valid words on the board, you must design and implement and algorithm to exhaustively find all of the words in the board using **recursion** and **backtracking**. Your algorithm must find all possible words for each board while not wasting time construcing invalid words (i.e., prune search paths that are not the prefix of a valid word as discussed in lecture). This algorithm is a key portion of the project, and its overall efficiency will constribute to your grade.
* Use the dictionary provided as the cannonical dictionary for determining valid words/prefixes. Each word in this dictionary is stored on a separate line. Your game must parse this file and load it into a dictionary class that implements DictionaryInterface.java. An simple implementation is provided for you as SimpleDictionary.java.
* As each word is discovered by your exhaustive algorithm, you must insert it into a second instantiation of a class implementing DictionaryInterface (e.g., another SimpleDictionary instantiation) so that when the user begins guessing words, this second object can be queried to determine whether or not the guessed word is valid.
* You must write a second DictionaryInterface implementation that stores the list of words using the De La Briandais trie structure described in lecture.
* The use of either the SimpleDictionary implementation of the De La Briandais trie should be determined through a command line argument to your program. If the argument "-d simple" is passed to your program (i.e., "java MyBoggle -d simple"), it should use the SimpleDictionary implementation for storing words from the provided cannonical dictionary as well as the words that will be found in exploring the board. If the argument "-d dlb" is passed to your program (i.e., "java MyBoggle -d dlb"), it should use your De La Briandais trie implementation for storing words from the provided cannonical dictionary as well as the words that will be found in exploring the board. Your program should default to the use of the the SimpleDictionary impelementation if no argument is given.
* The filename storing the board to be played should also gleaned via command line argument. If the argument "-b board3.txt" is passed to your program (i.e., "java MyBoggle -b board3.txt"), it should open the file board3.txt stored in the same directory as your program and display the board stored within. Several sample boards are provided for you (board1.txt, board2.txt, board3.txt, board4.txt, board5.txt, and board6.txt). Each of these file constains a single line with 16 characters on it. The first 4 characters represent the first row of the board, the next 4 the second row of the board and so on. board1.txt, for example, should display the first example board shown on this page, while board2.txt should display the second.
* Both a dictionary and a board should be able to be specified via the command line for a single run of your program, (e.g., "java MyBoggle -d dlb -b board2.txt"). The relative order of the two arguments should not matter, (i.e., "java MyBoggle -d dlb -b board2.txt" and "java MyBoggle -b board2.txt -d dlb" should both present the board stored in board2.txt to the user and use a De La Briandais trie implemention of DictionaryInterface for word storage).

***Due:  11:59 PM Feb. 1, 2015***

##Submission Guidelines:
* **DO NOT SUBMIT** any IDE package files or the the dictionary world list file (dictionary.txt).  You should only submit your .java files (including DictionaryInterface.java and SimpleDictionary.java) and the board .txt files.
* You must name the primary driver for your game MyBoggle.java.
* You must be able to compile your game by running "javac MyBoggle.java".
* You must be able to run your game by running "java MyBoggle".
* You must fill out info_sheet.txt.
* Be sure to remember to push the lastest copy of your code back to your GitHub repository before the the assignment is due.  At 12:00 AM Feb 2, the repositories will automatically be copied for grading.  Whatever is present in your GitHub repository at that time will be considered your submission for this assignment.

##Additional Notes:
* Note that the boards are specified with upper-case letters while the dictionary is made up of lower case words. Your submission should operate in a case-insensitive manner.
* A driver program is provided to test your dictionary (DictTest.java) as well as sample output (dictTestOut.txt). You can use them to test your implementation of DictionaryInterface.
* Example output files have been provided (out1.txt and out2.txt) to help you with debugging.
* Do not modify the board files, DictionaryInterface.java, or SimpleDictionary.java.
* Your program should read in a single line of 16 characters for the board (all boards will be 4x4) and then split that line into the four rows of the board.
* Your program should use DictionaryInterface to implement a DLB trie class, and then allow either the DLB class or SimpleDictionary class to be used during the game (depending on the command line arguments given).
* Your DLB trie implementation should **not** use any Java builtin datastructures (e.g., ArrayList, LinkedList, etc.). Your DLB should be all your own work.
