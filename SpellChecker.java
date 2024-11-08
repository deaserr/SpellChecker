//Programmer: Dean Serrano
//Class: CS240
//Date: 05/09/2024
//MidTerm: SpellChecker
//Purpose: Takes in a given string to be compared to a dictionary for possible spelling corrections
//Issues in the code: suggestions are not 100% accurate

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Set;

public class SpellChecker {
    public static Hashtable<String, String> dictionary = new Hashtable<>(100);
    public static Scanner input = new Scanner(System.in);
    public static void main(String[] args) throws FileNotFoundException
    {
        //initializes the dictionary
        makeDictionary();
        intro();

        int programLoop = 0;
        while(programLoop == 0)
        {
            System.out.print("\nWould you like to add a word to the dictionary(1), or check the spelling in a sentence(2)? :");
            int usrCommand = input.nextInt();
            input.nextLine();
            System.out.println();
            

            switch (usrCommand) {
                case 1:
                    addWord();
                    break;
                case 2:
                    checkScentence();
                    programLoop++;
                    break;
                default:
                    System.out.println("Sorry that is an invalid command.");
                    break;
            }
        }
    }//end of main

    //gets dictionary file and stores in global hastable
    private static void makeDictionary() throws FileNotFoundException
    {
        File file  = new File("dictionary.txt");
        Scanner scan  = new Scanner(file);
        
        //loops through each word in file and adds to hashtable
        while(scan.hasNextLine())
        {
            String word = scan.nextLine();
            dictionary.put(word, word);
        }
    }//end of makeDictionary

    //checks given scentence for spelling errors
    public static void checkScentence()
    {
        System.out.print("Type here: ");
        String usrScentence = input.nextLine();

        String[] scentence = usrScentence.split(" ");

        //loops through each word in the given scentence
        for(String word : scentence)
        { 
            checkSpelling(word);
        }
    }//end of checkScentence

    //adds a word to the dictionary hastable
    public static void addWord()
    {
        String newWord;
        System.out.print("Please enter the word you would like to add: ");
        newWord = input.nextLine();
        dictionary.put(newWord, newWord);

        System.out.println(newWord + " has been added.");
    }//end of addWord

    //checks the spelling of a word against the words in the dictionary
    public static void checkSpelling(String word)
    {
        if(dictionary.containsValue(word))
        {
            System.out.println(word + ": correct");
        }
        else
        {
            Set<String> setOfKeys = dictionary.keySet();

            int lastEditNum = 0;
            int newEditNum = 0;

            String bestMatch = null;
            for(String key : setOfKeys)
            {
                if(lastEditNum == 0)
                {
                    lastEditNum = levDistance(word, key);
                    newEditNum = lastEditNum;
                    bestMatch = dictionary.get(key);
                }
                else
                {
                    newEditNum = levDistance(word, key);

                    if(newEditNum < lastEditNum)
                    {
                        bestMatch = dictionary.get(key);
                    }
                    else
                    {
                        break;
                    }
                }
            }
            System.out.println(word + ": Did you mean \"" + bestMatch +"\"");
        }
    }//end of checkSpelling

    static int levDistance(String str1, String str2)
    {
        int[][] matrix = new int[str1.length() + 1][str2.length() + 1];

        for(int i = 0; i <= str1.length(); i++)
        {
            for (int j = 0; j <= str2.length(); j++)
            {
                if(i == 0) //if str1 is empty
                {
                    matrix[i][j] = j;
                }
                else if(j ==0) //if str2 is empty
                {
                    matrix[i][j] = i;
                }
                else //finds the minumum between the three different types of edits
                {
                    matrix[i][j] = minEdits(matrix[i - 1][j - 1]
                        + editNum(str1.charAt(i - 1),str2.charAt(j - 1)), // replace
                        matrix[i - 1][j] + 1, // delete
                        matrix[i][j - 1] + 1); // insert
                }
            }
        }
        return matrix[str1.length()][str2.length()];
    }

    //checks the distance between two different characters
    static int editNum(char c1, char c2)
    {
        return c1 == c2 ? 0 : 1;
    }

    //takes the count of different operations done and returns the minimum value amoung them
    static int minEdits(int... nums)
    {
        return Arrays.stream(nums).min().orElse(Integer.MAX_VALUE);
    }

    public static void intro()
    {
        System.out.println("Welcome to the Spell Checker!");
        System.out.println("-----------------------------");
        System.out.println("\n- With this program you can compare a specified scentence to ");
        System.out.println("the system's dictionary to recieve spelling corrections.");
        System.out.println("- You also have to abliity to add your own words to the dictionary!");
    }
}
