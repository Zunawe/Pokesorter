package projects.zunawe.pokesorter.pokemon;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Pokemon implements Serializable{

    private static final long serialVersionUID = 6424846917859792485L;

    private String[] natures = {"Hardy", "Lonely", "Brave", "Adamant", "Naughty",
            "Bold", "Docile", "Relaxed", "Impish", "Lax",
            "Timid", "Hasty", "Serious", "Jolly", "Naive",
            "Modest", "Mild", "Quiet", "Bashful", "Rash",
            "Calm", "Gentle", "Sassy", "Careful", "Quirky"};

    private String displayName;
    private int number;
    private ArrayList<String> names = new ArrayList<String>();
    private String picturePath;
    private int level;
    private int natureIndex;
    private int[] IVList = new int[6];
    private int[] EVList = new int[6];
    private ArrayList<Integer[]> bases = new ArrayList<Integer[]>();
    private int currentFormIndex;
    private boolean hasAlternates;
    private boolean shiny;
    private int[] stats = new int[6];
    private double[] natureMods = new double[25];

    public static final int HP = 0;
    public static final int ATTACK = 1;
    public static final int DEFENSE = 2;
    public static final int SPEED = 3;
    public static final int SPATTACK = 4;
    public static final int SPDEFENSE = 5;

    public Pokemon(){
        number = 0;
        level = 1;
        names.add("");
        displayName = "";
        shiny = false;

        bases.add(new Integer[6]);
        for(int i = 0; i < IVList.length; i ++){
            IVList[i] = 0;
            EVList[i] = 0;
            bases.get(0)[i] = 0;
            stats[i] = 0;
            natureMods[i] = 1;
        }

        picturePath = "/data/sprites/0.png";

        hasAlternates = false;
    }

    public Pokemon(int num, int lev, int[] IVs, int[] EVs, int nature, boolean isShiny) throws IOException{
        level = lev;
        shiny = isShiny;

        readDatabase(num);
        natureIndex = nature;
        setNatureMods();
        for(int i = 0; i < IVs.length; i ++)
            setIV(i, IVs[i]);
        for(int i = 0; i < EVs.length; i ++)
            setEV(i, EVs[i]);
        calculateStats();

        picturePath = "data/sprites/";
        if(shiny)
            picturePath += "shiny/";
        picturePath += getNumber();
        if(hasAlternates)
            picturePath += "" + (char)(currentFormIndex + 97);
        if(shiny)
            picturePath += "s.png";
        else
            picturePath += ".png";
    }

    public void setIV(int index, int value){
        IVList[index] = value;
    }

    public void setEV(int index, int value){
        EVList[index] = value;
    }

    public void setNatureMods(){
        for(int i = 0; i < natureMods.length; i ++)
            natureMods[i] = 1;

        switch(natureIndex){

            case 2:
                natureMods[ATTACK] = 1.1;
                natureMods[DEFENSE] = 0.9;
                break;
            case 3:
                natureMods[ATTACK] = 1.1;
                natureMods[SPEED] = 0.9;
                break;
            case 4:
                natureMods[ATTACK] = 1.1;
                natureMods[SPATTACK] = 0.9;
                break;
            case 5:
                natureMods[ATTACK] = 1.1;
                natureMods[SPDEFENSE] = 0.9;
                break;
            case 6:
                natureMods[DEFENSE] = 1.1;
                natureMods[ATTACK] = 0.9;
                break;

            case 8:
                natureMods[DEFENSE] = 1.1;
                natureMods[SPEED] = 0.9;
                break;
            case 9:
                natureMods[DEFENSE] = 1.1;
                natureMods[SPATTACK] = 0.9;
                break;
            case 10:
                natureMods[DEFENSE] = 1.1;
                natureMods[SPDEFENSE] = 0.9;
                break;
            case 11:
                natureMods[SPEED] = 1.1;
                natureMods[ATTACK] = 0.9;
                break;
            case 12:
                natureMods[SPEED] = 1.1;
                natureMods[DEFENSE] = 0.9;
                break;

            case 14:
                natureMods[SPEED] = 1.1;
                natureMods[SPATTACK] = 0.9;
                break;
            case 15:
                natureMods[SPEED] = 1.1;
                natureMods[SPDEFENSE] = 0.9;
                break;
            case 16:
                natureMods[SPATTACK] = 1.1;
                natureMods[ATTACK] = 0.9;
                break;
            case 17:
                natureMods[SPATTACK] = 1.1;
                natureMods[DEFENSE] = 0.9;
                break;
            case 18:
                natureMods[SPATTACK] = 1.1;
                natureMods[SPEED] = 0.9;
                break;

            case 20:
                natureMods[SPATTACK] = 1.1;
                natureMods[SPDEFENSE] = 0.9;
                break;
            case 21:
                natureMods[SPDEFENSE] = 1.1;
                natureMods[ATTACK] = 0.9;
                break;
            case 22:
                natureMods[SPDEFENSE] = 1.1;
                natureMods[DEFENSE] = 0.9;
                break;
            case 23:
                natureMods[SPDEFENSE] = 1.1;
                natureMods[SPEED] = 0.9;
                break;
            case 24:
                natureMods[SPDEFENSE] = 1.1;
                natureMods[SPATTACK] = 0.9;
                break;
        }
    }

    private void calculateStats(){
        stats[HP] = (int)Math.floor(((IVList[HP] + (2 * bases.get(currentFormIndex)[HP]) + ((double)EVList[HP] / 4)) * ((double)level / 100)) + 10 + level);

        for(int i = 1; i < stats.length; i ++){
            stats[i] = (int)Math.floor((((IVList[i] + (2 * bases.get(currentFormIndex)[i]) + ((double)EVList[i] / 4)) * ((double)level / 100)) + 5) * natureMods[i]);
        }
    }

    public String getName(){
        return names.get(currentFormIndex);
    }

    public String getDisplayName(){
        return displayName;
    }

    public String[] getAltNames(){
        String[] returnNames = new String[names.size()];

        for(int i = 0; i < names.size(); i ++){
            returnNames[i] = names.get(i);
            returnNames[i] = returnNames[i].substring(returnNames[i].indexOf('(') + 1, returnNames[i].length() - 1);
            System.out.println(returnNames[i].substring(0, returnNames[i].indexOf('(') + 1));
        }

        return returnNames;
    }

    public int getNumber(){
        return number;
    }

    public int getLevel(){
        return level;
    }

    public int getIV(int index){
        return IVList[index];
    }

    public int getEV(int index){
        return EVList[index];
    }

    public int getStat(int index){
        return stats[index];
    }

    public int getBaseStat(int index){
        return bases.get(currentFormIndex)[index];
    }

    public String getNature(){
        return natures[natureIndex];
    }

    public String getPictureFilePath(){
        return picturePath;
    }

    public boolean hasAlts(){
        return hasAlternates;
    }

    public boolean isShiny(){
        return shiny;
    }

    public void setCurrentForm(int index){
        if(!hasAlternates)
            return;

        currentFormIndex = index;
        calculateStats();
        if(currentFormIndex + 97 > (int)'z'){
            picturePath = "data/sprites/";
            if(shiny)
                picturePath += "shiny/";
            picturePath += getNumber();
            if(hasAlternates)
                picturePath += "z" + (char)(currentFormIndex - 26 + 97);
            if(shiny)
                picturePath += "s.png";
            else
                picturePath += ".png";
        }
        else{
            picturePath = "data/sprites/";
            if(shiny)
                picturePath += "shiny/";
            picturePath += getNumber();
            if(hasAlternates)
                picturePath += "" + (char)(currentFormIndex + 97);
            if(shiny)
                picturePath += "s.png";
            else
                picturePath += ".png";
        }
    }

    public String toString(){
        return getNumber() + " " + getDisplayName();
    }

    private void readDatabase(int num) throws IOException{
        BasicPokemon bp = new BasicPokemon();

        try{
            FileInputStream in = new FileInputStream("data/database.bpok");
            ObjectInputStream ois = new ObjectInputStream(in);
            ArrayList<BasicPokemon> alts = new ArrayList<BasicPokemon>();

            boolean gotAlts = false;
            while(true){
                try{
                    bp = (BasicPokemon)ois.readObject();
                    if(bp.getNumber() == num){
                        alts.add(bp);
                        gotAlts = true;
                    }
                    else if(gotAlts)
                        break;
                }catch(Exception e){
                    break;
                }

            }

            for(int i = 0; i < alts.size(); i ++){
                bases.add(new Integer[6]);
                names.add(alts.get(i).getName());
            }

            displayName = names.get(0).replaceAll("\\([^\\(]*\\)", "");

            number = num;

            for(int i = 0; i < bases.size(); i ++){
                for(int j = 0; j < bases.get(i).length; j ++){
                    bases.get(i)[j] = alts.get(i).getBaseStat(j);
                }
            }

            if(alts.size() > 1)
                hasAlternates = true;

            ois.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static boolean isInt(String str){
        try{
            Integer.parseInt(str);
        }
        catch(NumberFormatException nfe){
            return false;
        }
        return true;
    }
}