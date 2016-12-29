package projects.zunawe.pokesorter.pokemon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectOutputStream;

import java.util.Scanner;

public class ListMaker{
    public static void main(String[] args){
        try{
            File file = new File("data/database.bpok");
            if(file.exists())
                file.delete();
            file.createNewFile();

            Scanner scanner = new Scanner(System.in);
            FileReader fr = new FileReader("data/database.txt");
            BufferedReader bf = new BufferedReader(fr);
            FileOutputStream fout = new FileOutputStream("data/database.bpok");
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            String line = "";

            int[] baseList = new int[6];

            while(true){
                try{
                    for(int j = 0; j < baseList.length; j ++)
                        baseList[j] = 0;

                    line = bf.readLine();

                    //Removes extra spaces, and then splits it at each space.
                    line.replace(" {2,}", "");
                    String[] individual = line.split(" ");
                    //Takes the number out of the first three characters of the line
                    int number = Integer.parseInt(individual[0]);
                    //Looks for a parentheses in the case of alternate forms
                    //or a letter in the case of double-worded names. (i.e. Mr. Mime)
                    //If there is one, it adds it to the name and moves everything over
                    while(individual[2].contains("(") || individual[2].contains(")") || (int)individual[2].charAt(0) > 57){
                        individual[1] += " " + individual[2];
                        for(int j = 2; j < individual.length - 1; j ++)
                            individual[j] = individual[j + 1];
                    }
                    String name = individual[1];
                    for(int j = 0; j < baseList.length; j ++){
                        //Makes a temp string starting at the stats and ending before the total
                        baseList[j] += Integer.parseInt(individual[j + 2]);
                    }

                    BasicPokemon addPokemon = new BasicPokemon(name, number, baseList);

                    oos.writeObject(addPokemon);
                    System.out.println(addPokemon + " " + addPokemon.getBaseStat(0));
                }catch(Exception e){
                    break;
                }
            }

            bf.close();
            oos.close();
            scanner.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}