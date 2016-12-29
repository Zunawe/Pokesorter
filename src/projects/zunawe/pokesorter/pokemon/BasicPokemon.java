package projects.zunawe.pokesorter.pokemon;

import java.io.Serializable;

public class BasicPokemon implements Serializable{
    private static final long serialVersionUID = 4197811934748758518L;

    private int[] baseStats = new int[6];
    private String name;
    private int number;

    public BasicPokemon(String nam, int num, int[] bases){
        name = nam;
        number = num;

        for(int i = 0; i < bases.length; i ++){
            baseStats[i] = bases[i];
        }
    }

    public BasicPokemon(){
        name = "";
        number = 0;
    }

    public String getName(){
        return name;
    }

    public int getNumber(){
        return number;
    }

    public int getBaseStat(int index){
        return baseStats[index];
    }

    public String toString(){
        return getNumber() + " " + getName().replaceAll("\\([^\\(]*\\)", "");
    }
}