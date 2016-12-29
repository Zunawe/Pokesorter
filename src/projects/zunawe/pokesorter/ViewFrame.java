package projects.zunawe.pokesorter;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.Comparator;
import java.util.Collections;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import projects.zunawe.pokesorter.pokemon.BasicPokemon;
import projects.zunawe.pokesorter.pokemon.Pokemon;

import projects.zunawe.util.JNumberTextField;

public class ViewFrame extends JFrame{
    private static final long serialVersionUID = -5749047949539597881L;
    private ArrayList<Pokemon> list = new ArrayList<Pokemon>();
    private JPanel panel;
    private final JFrame frame = this;
    private JComboBox<Pokemon> box;
    private JComboBox<String> altsBox;
    private BufferedImage image = null;
    private JLabel[] stats = new JLabel[6];
    private JLabel[] IVs = new JLabel[6];
    private JLabel[] EVs = new JLabel[6];
    private JLabel pokemonImage;
    private JLabel level;
    private JLabel nature;
    private JLabel statLabel = new JLabel("Stats:");
    private JLabel IVLabel = new JLabel("IVs:");
    private JLabel EVLabel = new JLabel("EVs:");
    private JLabel levelLabel = new JLabel("Level:");
    private JLabel natureLabel = new JLabel("Nature:");
    private JLabel[] statCategories = {new JLabel("Hit Points:"),
            new JLabel("Attack:"),
            new JLabel("Defense:"),
            new JLabel("Speed:"),
            new JLabel("Sp. Attack:"),
            new JLabel("Sp. Defense:")};
    private JLabel[] IVCategories = {new JLabel("Hit Points:"),
            new JLabel("Attack:"),
            new JLabel("Defense:"),
            new JLabel("Speed:"),
            new JLabel("Sp. Attack:"),
            new JLabel("Sp. Defense:")};
    private JLabel[] EVCategories = {new JLabel("Hit Points:"),
            new JLabel("Attack:"),
            new JLabel("Defense:"),
            new JLabel("Speed:"),
            new JLabel("Sp. Attack:"),
            new JLabel("Sp. Defense:")};
    private Pokemon selectedPokemon;

    public ViewFrame(String frameName){
        super(frameName);

        panel = new JPanel();
        SpringLayout layout = new SpringLayout();
        panel.setLayout(layout);

        String[] pokeNames = new String[list.size()];
        for(int i = 0; i < list.size(); i ++){
            Pokemon current = list.get(i);
            pokeNames[i] = current.getNumber() + " " + current.getName();
        }

        Pokemon[] pokemonArray = new Pokemon[list.size()];
        for(int i = 0; i < list.size(); i ++)
            pokemonArray[i] = list.get(i);
        box = new JComboBox<Pokemon>(pokemonArray);
        box.setEditable(false);
        box.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){

                JComboBox<Pokemon> cb = (JComboBox<Pokemon>)e.getSource();
                int index = cb.getSelectedIndex();
                if(index == -1)
                    selectedPokemon = null;
                else
                    selectedPokemon = (Pokemon)cb.getSelectedItem();

                updateAltsBox();
                updateLabels();

            }
        });

        altsBox = new JComboBox<String>();
        altsBox.setEditable(false);
        altsBox.setVisible(false);
        altsBox.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JComboBox<String> cb = (JComboBox<String>)e.getSource();
                int index = cb.getSelectedIndex();
                if(index == -1)
                    selectedPokemon.setCurrentForm(0);
                else
                    selectedPokemon.setCurrentForm(index);

                updateLabels();
            }
        });

        int index = box.getSelectedIndex();
        if(index == -1)
            selectedPokemon = null;
        else
            selectedPokemon = list.get(index);

        if(selectedPokemon != null){
            level = new JLabel("" + selectedPokemon.getLevel());
            nature = new JLabel(selectedPokemon.getNature());
        }
        else{
            level = new JLabel("0");
            nature = new JLabel("N/A");
        }
        try{
            if(selectedPokemon != null){
                try{
                    image = ImageIO.read(new File(selectedPokemon.getPictureFilePath()));
                }
                catch(Exception e){
                    image = ImageIO.read(new File("data/sprites/0.png"));
                }
                for(int i = 0; i < stats.length; i ++)
                    stats[i] = new JLabel("" + selectedPokemon.getStat(i));
                for(int i = 0; i < IVs.length; i ++)
                    IVs[i] = new JLabel("" + selectedPokemon.getIV(i));
                for(int i = 0; i < EVs.length; i ++)
                    EVs[i] = new JLabel("" + selectedPokemon.getEV(i));
            }
            else{
                image = ImageIO.read(new File("data/sprites/0.png"));
                for(int i = 0; i < stats.length; i ++){
                    stats[i] = new JLabel("" + 0);
                    IVs[i] = new JLabel("" + 0);
                    EVs[i] = new JLabel("" + 0);
                }
            }
            pokemonImage = new JLabel(new ImageIcon(image));
        }
        catch(IOException ioe){
            JOptionPane.showMessageDialog(null, "There was an error loading the pokemon's image.", null, JOptionPane.ERROR_MESSAGE);
        }

        JButton addButton = new JButton("Add Pokemon");
        addButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                AddDialog dialog = new AddDialog(frame);
                dialog.setVisible(true);
            }
        });

        JButton editButton = new JButton("Modify Pokemon");
        editButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(list.size() == 0){
                    JOptionPane.showMessageDialog(null, "There are no pokemon to modify!", null, JOptionPane.WARNING_MESSAGE);
                    return;
                }
                EditDialog dialog = new EditDialog(frame);
                dialog.setVisible(true);
            }
        });

        JButton removeButton = new JButton("Remove Pokemon");
        removeButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(list.size() == 0){
                    JOptionPane.showMessageDialog(null, "There are no pokemon to remove!", null, JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int decider = JOptionPane.showConfirmDialog(null, "Are you sure you wish to remove the currently displayed pokemon?", null, JOptionPane.YES_NO_OPTION);
                if(decider == JOptionPane.NO_OPTION)
                    return;
                list.remove(selectedPokemon);
                updateComboBox();
            }
        });

        editButton.setPreferredSize(removeButton.getPreferredSize());
        addButton.setPreferredSize(removeButton.getPreferredSize());

        panel.add(box);
        panel.add(altsBox);
        panel.add(levelLabel);
        panel.add(level);
        panel.add(natureLabel);
        panel.add(nature);
        panel.add(statLabel);
        panel.add(IVLabel);
        panel.add(EVLabel);
        for(int i = 0; i < stats.length; i ++){
            panel.add(stats[i]);
            panel.add(IVs[i]);
            panel.add(EVs[i]);
            panel.add(statCategories[i]);
            panel.add(IVCategories[i]);
            panel.add(EVCategories[i]);
        }
        panel.add(addButton);
        panel.add(editButton);
        panel.add(removeButton);
        panel.add(pokemonImage);


        //Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu helpMenu = new JMenu("Help");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem open = new JMenuItem("Open File...");
        JMenuItem clear = new JMenuItem("Clear List");
        JMenuItem append = new JMenuItem("Append List");
        JMenuItem contents = new JMenuItem("Contents");
        JMenuItem version = new JMenuItem("Version 1.2");
        save.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                JFileChooser fc = new JFileChooser();
                PokemonFileFilter filter = new PokemonFileFilter();
                fc.addChoosableFileFilter(filter);
                fc.setFileFilter(filter);
                fc.setAcceptAllFileFilterUsed(false);

                int result = fc.showSaveDialog(new JFrame("Save"));

                if(result == JFileChooser.APPROVE_OPTION){
                    File file = fc.getSelectedFile();
                    String filePath = file.getPath();
                    if(!filePath.endsWith(".pok"))
                        file = new File(filePath + ".pok");

                    if(file.exists()){
                        int reply = JOptionPane.showConfirmDialog(null, "That file already exists.\nWould you like to overwrite?", "Select an Option", JOptionPane.YES_NO_OPTION);
                        if(reply == JOptionPane.YES_OPTION){
                            file.delete();
                            try{
                                file.createNewFile();
                            }catch(IOException ioe){
                                JOptionPane.showMessageDialog(null, "There was a problem overwriting the file.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                    else{
                        try {
                            file.createNewFile();
                        } catch (IOException ioe){
                            JOptionPane.showMessageDialog(null, "There was a problem creating the file.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    try{
                        FileOutputStream fout = new FileOutputStream(file);
                        ObjectOutputStream oos = new ObjectOutputStream(fout);

                        for(Pokemon p: list)
                            oos.writeObject(p);

                        oos.close();
                    }catch(Exception ex){
                        JOptionPane.showMessageDialog(null, "There was a problem overwriting the file.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        open.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                JFileChooser fc = new JFileChooser();
                PokemonFileFilter filter = new PokemonFileFilter();
                fc.addChoosableFileFilter(filter);
                fc.setFileFilter(filter);
                fc.setAcceptAllFileFilterUsed(false);

                int result = fc.showOpenDialog(new JFrame("Open"));

                if(result == JFileChooser.APPROVE_OPTION){

                    File file = fc.getSelectedFile();
                    String filePath = file.getPath();
                    if(!filePath.endsWith(".pok")){
                        JOptionPane.showMessageDialog(null, "You cannot choose a directory.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if(!file.exists()){
                        JOptionPane.showMessageDialog(null, "There was a problem opening the file.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    try{
                        list.clear();
                        FileInputStream fin = new FileInputStream(file);
                        ObjectInputStream ois = new ObjectInputStream(fin);
                        while(true){
                            try{
                                list.add((Pokemon)ois.readObject());
                            }catch(Exception e1){
                                break;
                            }
                        }
                        ois.close();
                    }catch(Exception ex){
                        JOptionPane.showMessageDialog(null, "There was a problem reading the file.", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    updateComboBox();
                    sortList();
                }
            }
        });
        clear.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                int decider = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove all pokemon from the current list?", null, JOptionPane.YES_NO_OPTION);
                if(decider == JOptionPane.NO_OPTION)
                    return;
                list.clear();
                updateComboBox();
            }
        });
        append.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                JFileChooser fc = new JFileChooser();
                PokemonFileFilter filter = new PokemonFileFilter();
                fc.addChoosableFileFilter(filter);
                fc.setFileFilter(filter);
                fc.setAcceptAllFileFilterUsed(false);

                int result = fc.showOpenDialog(new JFrame("Append"));
                fc.setApproveButtonText("Append");

                if(result == JFileChooser.APPROVE_OPTION){
                    File file = fc.getSelectedFile();
                    String filePath = file.getPath();
                    if(!filePath.endsWith(".pok")){
                        JOptionPane.showMessageDialog(null, "You cannot choose a directory.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if(!file.exists()){
                        JOptionPane.showMessageDialog(null, "There was a problem opening the file.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    try{
                        FileInputStream fin = new FileInputStream(file);
                        ObjectInputStream ois = new ObjectInputStream(fin);
                        while(true){
                            try{
                                list.add((Pokemon)ois.readObject());
                            }
                            catch(Exception e1){
                                break;
                            }
                        }
                        ois.close();
                    }catch(Exception ex){
                        JOptionPane.showMessageDialog(null, "There was a problem reading the file.", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    updateComboBox();
                    sortList();
                }
            }
        });
        contents.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                HelpDialog help = new HelpDialog(frame);
                help.setVisible(true);
            }
        });
        fileMenu.add(save);
        fileMenu.add(open);
        editMenu.add(clear);
        editMenu.add(append);
        helpMenu.add(contents);
        helpMenu.addSeparator();
        helpMenu.add(version);
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);

        //Setting the size and overall view
        setContentPane(panel);
        setJMenuBar(menuBar);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 440);
        ImageIcon img = new ImageIcon("CherishBall.png");
        setIconImage(img.getImage());
        Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int)((resolution.getWidth() - getWidth()) / 2), (int)((resolution.getHeight() - getHeight()) / 2));
        setResizable(false);

        //Setting the layout of the panel
        //Combo Box
        layout.putConstraint(SpringLayout.WEST, box, 10, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, box, 10, SpringLayout.NORTH, panel);

        //Alts Combo Box
        layout.putConstraint(SpringLayout.WEST, altsBox, 10, SpringLayout.EAST, box);
        layout.putConstraint(SpringLayout.NORTH, altsBox, 0, SpringLayout.NORTH, box);

        //Level Label
        layout.putConstraint(SpringLayout.WEST, levelLabel, 0, SpringLayout.WEST, box);
        layout.putConstraint(SpringLayout.NORTH, levelLabel, 10, SpringLayout.SOUTH, box);

        //Level
        layout.putConstraint(SpringLayout.WEST, level, 2, SpringLayout.EAST, levelLabel);
        layout.putConstraint(SpringLayout.NORTH, level, 0, SpringLayout.NORTH, levelLabel);

        //Nature Label
        layout.putConstraint(SpringLayout.WEST, natureLabel, 0, SpringLayout.WEST, levelLabel);
        layout.putConstraint(SpringLayout.NORTH, natureLabel, 15, SpringLayout.SOUTH, levelLabel);

        //Nature
        layout.putConstraint(SpringLayout.WEST, nature, 2, SpringLayout.EAST, natureLabel);
        layout.putConstraint(SpringLayout.NORTH, nature, 0, SpringLayout.NORTH, natureLabel);

        //Stats Label
        layout.putConstraint(SpringLayout.WEST, statLabel, 0, SpringLayout.WEST, natureLabel);
        layout.putConstraint(SpringLayout.NORTH, statLabel, 15, SpringLayout.SOUTH, natureLabel);

        //Stats
        layout.putConstraint(SpringLayout.WEST, stats[0], 100, SpringLayout.WEST, statLabel);
        layout.putConstraint(SpringLayout.NORTH, stats[0], 2, SpringLayout.SOUTH, statLabel);
        for(int i = 1; i < stats.length; i ++){
            layout.putConstraint(SpringLayout.WEST, stats[i], 100, SpringLayout.WEST, statLabel);
            layout.putConstraint(SpringLayout.NORTH, stats[i], 2, SpringLayout.SOUTH, stats[i - 1]);
        }

        //Stat Labels
        layout.putConstraint(SpringLayout.EAST, statCategories[0], -10, SpringLayout.WEST, stats[0]);
        layout.putConstraint(SpringLayout.NORTH, statCategories[0], 2, SpringLayout.SOUTH, statLabel);
        for(int i = 1; i < stats.length; i ++){
            layout.putConstraint(SpringLayout.EAST, statCategories[i], -10, SpringLayout.WEST, stats[i]);
            layout.putConstraint(SpringLayout.NORTH, statCategories[i], 2, SpringLayout.SOUTH, stats[i - 1]);
        }

        //IVs Label
        layout.putConstraint(SpringLayout.WEST, IVLabel, 0, SpringLayout.WEST, statLabel);
        layout.putConstraint(SpringLayout.NORTH, IVLabel, 20, SpringLayout.SOUTH, stats[stats.length - 1]);

        //IVs
        layout.putConstraint(SpringLayout.WEST, IVs[0], 0, SpringLayout.WEST, stats[0]);
        layout.putConstraint(SpringLayout.NORTH, IVs[0], 2, SpringLayout.SOUTH, IVLabel);
        for(int i = 1; i < IVs.length; i ++){
            layout.putConstraint(SpringLayout.WEST, IVs[i], 0, SpringLayout.WEST, stats[i]);
            layout.putConstraint(SpringLayout.NORTH, IVs[i], 2, SpringLayout.SOUTH, IVs[i - 1]);
        }

        //IV Labels
        layout.putConstraint(SpringLayout.EAST, IVCategories[0], -10, SpringLayout.WEST, IVs[0]);
        layout.putConstraint(SpringLayout.NORTH, IVCategories[0], 2, SpringLayout.SOUTH, IVLabel);
        for(int i = 1; i < IVs.length; i ++){
            layout.putConstraint(SpringLayout.EAST, IVCategories[i], -10, SpringLayout.WEST, IVs[i]);
            layout.putConstraint(SpringLayout.NORTH, IVCategories[i], 2, SpringLayout.SOUTH, IVs[i - 1]);
        }

        //EVs Label
        layout.putConstraint(SpringLayout.WEST, EVLabel, 200, SpringLayout.WEST, IVLabel);
        layout.putConstraint(SpringLayout.NORTH, EVLabel, 0, SpringLayout.NORTH, IVLabel);

        //EVs
        layout.putConstraint(SpringLayout.WEST, EVs[0], 100, SpringLayout.WEST, EVLabel);
        layout.putConstraint(SpringLayout.NORTH, EVs[0], 2, SpringLayout.SOUTH, EVLabel);
        for(int i = 1; i < EVs.length; i ++){
            layout.putConstraint(SpringLayout.WEST, EVs[i], 0, SpringLayout.WEST, EVs[i - 1]);
            layout.putConstraint(SpringLayout.NORTH, EVs[i], 2, SpringLayout.SOUTH, EVs[i - 1]);
        }

        //EV Labels
        layout.putConstraint(SpringLayout.EAST, EVCategories[0], -10, SpringLayout.WEST, EVs[0]);
        layout.putConstraint(SpringLayout.NORTH, EVCategories[0], 2, SpringLayout.SOUTH, EVLabel);
        for(int i = 1; i < EVs.length; i ++){
            layout.putConstraint(SpringLayout.EAST, EVCategories[i], -10, SpringLayout.WEST, EVs[i]);
            layout.putConstraint(SpringLayout.NORTH, EVCategories[i], 2, SpringLayout.SOUTH, EVs[i - 1]);
        }

        //Pokemon Picture
        layout.putConstraint(SpringLayout.EAST, pokemonImage, -10, SpringLayout.EAST, panel);
        layout.putConstraint(SpringLayout.NORTH, pokemonImage, 10, SpringLayout.NORTH, panel);

        //Add Pokemon Button
        layout.putConstraint(SpringLayout.EAST, addButton, -10, SpringLayout.EAST, panel);
        layout.putConstraint(SpringLayout.NORTH, addButton, 20, SpringLayout.SOUTH, pokemonImage);

        //Edit Pokemon Button
        layout.putConstraint(SpringLayout.EAST, editButton, 0, SpringLayout.EAST, addButton);
        layout.putConstraint(SpringLayout.NORTH, editButton, 10, SpringLayout.SOUTH, addButton);

        //Remove Pokemon Button
        layout.putConstraint(SpringLayout.EAST, removeButton, 0, SpringLayout.EAST, editButton);
        layout.putConstraint(SpringLayout.NORTH, removeButton, 10, SpringLayout.SOUTH, editButton);

        updateComboBox();
    }

    public void updateComboBox(){
        sortList();

        if(list.size() >= 0)
            box.removeAllItems();
        for(Pokemon item: list)
            box.addItem(item);
        if(list.size() == 0)
            box.setPreferredSize(new Dimension(110, 25));
        else
            box.setPreferredSize(null);

        updateAltsBox();

        updateLabels();
    }

    public void updateAltsBox(){
        if(selectedPokemon == null){
            altsBox.setVisible(false);
            return;
        }

        if(selectedPokemon.hasAlts()){
            altsBox.removeAllItems();
            for(String item: selectedPokemon.getAltNames())
                altsBox.addItem(item);
            altsBox.setVisible(true);
        }
        else{
            altsBox.setVisible(false);
        }

        updateLabels();
    }

    public void updateLabels(){
        try{
            if(selectedPokemon != null){
                try{
                    image = ImageIO.read(new File(selectedPokemon.getPictureFilePath()));
                }
                catch(Exception e){
                    image = ImageIO.read(new File("data/sprites/0.png"));
                }
                nature.setText(selectedPokemon.getNature());
                level.setText("" + selectedPokemon.getLevel());
                for(int i = 0; i < stats.length; i ++)
                    stats[i].setText("" + selectedPokemon.getStat(i));
                for(int i = 0; i < IVs.length; i ++)
                    IVs[i].setText("" + selectedPokemon.getIV(i));
                for(int i = 0; i < EVs.length; i ++)
                    EVs[i].setText("" + selectedPokemon.getEV(i));
            }
            else{
                image = ImageIO.read(new File("data/sprites/0.png"));
                nature.setText("N/A");
                level.setText("" + 0);
                for(int i = 0; i < stats.length; i ++){
                    stats[i].setText("" + 0);
                    IVs[i].setText("" + 0);
                    EVs[i].setText("" + 0);
                }
            }
            pokemonImage.setIcon(new ImageIcon(image));
        }
        catch(IOException ioe){
            JOptionPane.showMessageDialog(null, "There was an error loading the pokemon's image.", null, JOptionPane.ERROR_MESSAGE);
        }
    }

    public void sortList(){
        Comparator<Pokemon> comparator = new Comparator<Pokemon>(){
            @Override
            public int compare(Pokemon poke1, Pokemon poke2){
                int temp = poke1.getNumber() - poke2.getNumber();
                return temp;
            }
        };

        Collections.sort(list, comparator);
    }

    public class AddDialog extends JDialog{
        private static final long serialVersionUID = 9203359372448814899L;

        private JPanel panel;
        private JCheckBox shinyCheck;
        private JComboBox addBox;
        private JComboBox<String> natureBox;
        private String[] natures = {"Hardy", "Lonely", "Brave", "Adamant", "Naughty",
                "Bold", "Docile", "Relaxed", "Impish", "Lax",
                "Timid", "Hasty", "Serious", "Jolly", "Naive",
                "Modest", "Mild", "Quiet", "Bashful", "Rash",
                "Calm", "Gentle", "Sassy", "Careful", "Quirky"};
        private ArrayList<BasicPokemon> allPokes;
        private BasicPokemon selectedBasicPokemon;
        private int natureIndex;
        private boolean shiny;
        private JLabel[] baseStats = new JLabel[6];
        private JNumberTextField[] IVFields = new JNumberTextField[6];
        private JNumberTextField[] EVFields = new JNumberTextField[6];
        private JNumberTextField levelField;
        private JLabel baseStatLabel = new JLabel("Base Stats:");
        private JLabel IVLabel = new JLabel("IVs:");
        private JLabel EVLabel = new JLabel("EVs:");
        private JLabel levelLabel = new JLabel("Level:");
        private JLabel[] categories = {new JLabel("HP"),
                new JLabel("Att"),
                new JLabel("Def"),
                new JLabel("Speed"),
                new JLabel("Sp. Att"),
                new JLabel("Sp. Def")};

        public AddDialog(JFrame owner){
            super(owner, "Add Pokemon");

            SpringLayout layout = new SpringLayout();
            panel = new JPanel();
            panel.setLayout(layout);

            setContentPane(panel);
            setSize(425, 240);
            setResizable(false);
            Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
            setLocation((int)((resolution.getWidth() - getWidth()) / 2), (int)((resolution.getHeight() - getHeight()) / 2));

            allPokes = getAllPokemon();

            String[] pokeNames = new String[allPokes.size()];
            for(int i = 0; i < allPokes.size(); i ++){
                BasicPokemon current = allPokes.get(i);
                pokeNames[i] = current.getNumber() + " " + current.getName();
            }

            addBox = new JComboBox(allPokes.toArray());
            addBox.setEditable(false);
            addBox.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    JComboBox cb = (JComboBox)e.getSource();
                    selectedBasicPokemon = allPokes.get(cb.getSelectedIndex());

                    updateBaseStatLabels();
                }
            });

            shinyCheck = new JCheckBox("Shiny");
            shinyCheck.addItemListener(new ItemListener(){
                public void itemStateChanged(ItemEvent e){
                    if(e.getStateChange() == ItemEvent.DESELECTED)
                        shiny = false;
                    else
                        shiny = true;
                }
            });

            natureBox = new JComboBox<String>(natures);
            natureBox.setEditable(false);
            natureBox.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    JComboBox<String> cb = (JComboBox<String>)e.getSource();
                    natureIndex = cb.getSelectedIndex();
                }
            });

            selectedBasicPokemon = allPokes.get(addBox.getSelectedIndex());

            for(int i = 0; i < baseStats.length; i ++)
                baseStats[i] = new JLabel("" + selectedBasicPokemon.getBaseStat(i));

            for(int i = 0; i < IVFields.length; i ++){
                IVFields[i] = new JNumberTextField(2);
                IVFields[i].setColumns(3);
                IVFields[i].setAllowNegative(false);
                EVFields[i] = new JNumberTextField(3);
                EVFields[i].setColumns(3);
                EVFields[i].setAllowNegative(false);
            }
            levelField = new JNumberTextField(3);
            levelField.setColumns(3);

            JButton finishedButton = new JButton("Add");
            finishedButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    try{
                        int[] IVs = new int[IVFields.length];
                        int[] EVs = new int[EVFields.length];
                        for(int i = 0; i < IVFields.length; i ++){
                            if(IVFields[i].getText().length() == 0){
                                JOptionPane.showMessageDialog(null, "One of the IV fields is empty.", null, JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                            IVs[i] = IVFields[i].getInt();
                        }
                        for(int i = 0; i < EVFields.length; i ++){
                            if(EVFields[i].getText().length() == 0){
                                JOptionPane.showMessageDialog(null, "One of the EV fields is empty.", null, JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                            EVs[i] = EVFields[i].getInt();
                        }
                        if(levelField.getText().length() == 0){
                            JOptionPane.showMessageDialog(null, "The level field is empty.", null, JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        if(levelField.getInt() > 100){
                            JOptionPane.showMessageDialog(null, "You can't have more than 100 levels.", null, JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        for(int i = 0; i < IVFields.length; i ++){
                            if(IVFields[i].getInt() > 31 || IVFields[i].getInt() < 0){
                                JOptionPane.showMessageDialog(null, "You can't have more than 31 in an IV.", null, JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                        }
                        int totalEVs = 0;
                        for(int i = 0; i < EVFields.length; i ++){
                            totalEVs += EVFields[i].getInt();
                            if(EVFields[i].getInt() > 255 || EVFields[i].getInt() < 0){
                                JOptionPane.showMessageDialog(null, "You can't have more than 255 in an EV.", null, JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                        }
                        if(totalEVs > 510){
                            JOptionPane.showMessageDialog(null, "You can't have more than 510 total EVs.", null, JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        int level = levelField.getInt();
                        list.add(new Pokemon(selectedBasicPokemon.getNumber(), level, IVs, EVs, natureIndex, shiny));

                        updateComboBox();
                        dispose();
                    }catch (IOException ioe){
                        JOptionPane.showMessageDialog(null, "There was an error loading one of the fields.", null, JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    dispose();
                }
            });

            for(JLabel label: baseStats)
                add(label);
            for(JNumberTextField field: IVFields)
                add(field);
            for(JNumberTextField field: EVFields)
                add(field);

            add(addBox);
            add(natureBox);
            add(shinyCheck);

            add(levelField);
            add(baseStatLabel);
            add(IVLabel);
            add(EVLabel);
            add(levelLabel);
            for(JLabel label: categories)
                add(label);

            add(finishedButton);
            add(cancelButton);

            //Combo addBox
            layout.putConstraint(SpringLayout.WEST, addBox, 10, SpringLayout.WEST, panel);
            layout.putConstraint(SpringLayout.NORTH, addBox, 10, SpringLayout.NORTH, panel);

            //Nature Combo addBox
            layout.putConstraint(SpringLayout.WEST, natureBox, 10, SpringLayout.EAST, addBox);
            layout.putConstraint(SpringLayout.NORTH, natureBox, 0, SpringLayout.NORTH, addBox);

            //Shiny Checkbox
            layout.putConstraint(SpringLayout.WEST, shinyCheck, 10, SpringLayout.EAST, natureBox);
            layout.putConstraint(SpringLayout.NORTH, shinyCheck, 0, SpringLayout.NORTH, natureBox);

            //Level Label
            layout.putConstraint(SpringLayout.WEST, levelLabel, 0, SpringLayout.WEST, addBox);
            layout.putConstraint(SpringLayout.NORTH, levelLabel, 10, SpringLayout.SOUTH, addBox);

            //Level Field
            layout.putConstraint(SpringLayout.WEST, levelField, 5, SpringLayout.EAST, levelLabel);
            layout.putConstraint(SpringLayout.NORTH, levelField, 0, SpringLayout.NORTH, levelLabel);

            //Categories
            layout.putConstraint(SpringLayout.WEST, categories[0], 20, SpringLayout.EAST, baseStatLabel);
            layout.putConstraint(SpringLayout.NORTH, categories[0], 0, SpringLayout.NORTH, levelLabel);
            for(int i = 1; i < categories.length; i ++){
                layout.putConstraint(SpringLayout.WEST, categories[i], 50 * i, SpringLayout.WEST, categories[0]);
                layout.putConstraint(SpringLayout.NORTH, categories[i], 0, SpringLayout.NORTH, categories[i - 1]);
            }

            //Stats Label
            layout.putConstraint(SpringLayout.WEST, baseStatLabel, 0, SpringLayout.WEST, levelLabel);
            layout.putConstraint(SpringLayout.NORTH, baseStatLabel, 10, SpringLayout.SOUTH, categories[0]);

            //Stats
            layout.putConstraint(SpringLayout.WEST, baseStats[0], 0, SpringLayout.WEST, categories[0]);
            layout.putConstraint(SpringLayout.NORTH, baseStats[0], 0, SpringLayout.NORTH, baseStatLabel);
            for(int i = 1; i < baseStats.length; i ++){
                layout.putConstraint(SpringLayout.WEST, baseStats[i], 0, SpringLayout.WEST, categories[i]);
                layout.putConstraint(SpringLayout.NORTH, baseStats[i], 0, SpringLayout.NORTH, baseStatLabel);
            }

            //IVs Label
            layout.putConstraint(SpringLayout.WEST, IVLabel, 0, SpringLayout.WEST, baseStatLabel);
            layout.putConstraint(SpringLayout.NORTH, IVLabel, 10, SpringLayout.SOUTH, baseStatLabel);

            //IV Fields
            layout.putConstraint(SpringLayout.WEST, IVFields[0], 0, SpringLayout.WEST, categories[0]);
            layout.putConstraint(SpringLayout.NORTH, IVFields[0], 0, SpringLayout.NORTH, IVLabel);
            for(int i = 1; i < IVFields.length; i ++){
                layout.putConstraint(SpringLayout.WEST, IVFields[i], 0, SpringLayout.WEST, baseStats[i]);
                layout.putConstraint(SpringLayout.NORTH, IVFields[i], 0, SpringLayout.NORTH, IVFields[i - 1]);
            }

            //EVs Label
            layout.putConstraint(SpringLayout.WEST, EVLabel, 0, SpringLayout.WEST, IVLabel);
            layout.putConstraint(SpringLayout.NORTH, EVLabel, 10, SpringLayout.SOUTH, IVLabel);

            //EV Fields
            layout.putConstraint(SpringLayout.WEST, EVFields[0], 0, SpringLayout.WEST, categories[0]);
            layout.putConstraint(SpringLayout.NORTH, EVFields[0], 0, SpringLayout.NORTH, EVLabel);
            for(int i = 1; i < EVFields.length; i ++){
                layout.putConstraint(SpringLayout.WEST, EVFields[i], 0, SpringLayout.WEST, IVFields[i]);
                layout.putConstraint(SpringLayout.NORTH, EVFields[i], 0, SpringLayout.NORTH, EVFields[i - 1]);
            }

            //Button
            layout.putConstraint(SpringLayout.WEST, finishedButton, 0, SpringLayout.WEST, levelLabel);
            layout.putConstraint(SpringLayout.NORTH, finishedButton, 20, SpringLayout.SOUTH, EVLabel);

            //Cancel Button
            layout.putConstraint(SpringLayout.WEST, cancelButton, 10, SpringLayout.EAST, finishedButton);
            layout.putConstraint(SpringLayout.NORTH, cancelButton, 0, SpringLayout.NORTH, finishedButton);

        }

        public void updateBaseStatLabels(){
            if(selectedBasicPokemon != null){
                for(int i = 0; i < baseStats.length; i ++)
                    baseStats[i].setText("" + selectedBasicPokemon.getBaseStat(i));
            }
            else{
                for(int i = 0; i < baseStats.length; i ++)
                    baseStats[i].setText("" + 0);
            }
        }

        public ArrayList<BasicPokemon> getAllPokemon(){
            ArrayList<BasicPokemon> pokes = new ArrayList<BasicPokemon>();

            try{
                FileInputStream in = new FileInputStream("data/database.bpok");
                ObjectInputStream ois = new ObjectInputStream(in);

                while(true){
                    try{
                        pokes.add((BasicPokemon)ois.readObject());
                    }
                    catch(Exception e1){
                        break;
                    }
                }

                for(int i = 0; i < pokes.size() - 1; i ++)
                    if(pokes.get(i).getNumber() == pokes.get(i + 1).getNumber()){
                        pokes.remove(i + 1);
                        i --;
                    }

                ois.close();
            }catch(Exception e){
                e.printStackTrace();
            }

            return pokes;
        }

    }

    public class EditDialog extends JDialog{
        private static final long serialVersionUID = 5903170603141690106L;

        private JPanel panel;
        private JCheckBox shinyCheck;
        private JComboBox editBox;
        private JComboBox<String> natureBox;
        private String[] natures = {"Hardy", "Lonely", "Brave", "Adamant", "Naughty",
                "Bold", "Docile", "Relaxed", "Impish", "Lax",
                "Timid", "Hasty", "Serious", "Jolly", "Naive",
                "Modest", "Mild", "Quiet", "Bashful", "Rash",
                "Calm", "Gentle", "Sassy", "Careful", "Quirky"};
        private Pokemon selectedEditPokemon;
        private int natureIndex;
        private boolean shiny;
        private JLabel[] baseStats = new JLabel[6];
        private JNumberTextField[] IVFields = new JNumberTextField[6];
        private JNumberTextField[] EVFields = new JNumberTextField[6];
        private JNumberTextField levelField;
        private JLabel baseStatLabel = new JLabel("Base Stats:");
        private JLabel IVLabel = new JLabel("IVs:");
        private JLabel EVLabel = new JLabel("EVs:");
        private JLabel levelLabel = new JLabel("Level:");
        private JLabel[] categories = {new JLabel("HP"),
                new JLabel("Att"),
                new JLabel("Def"),
                new JLabel("Speed"),
                new JLabel("Sp. Att"),
                new JLabel("Sp. Def")};

        public EditDialog(JFrame owner){
            super(owner, "Modify Pokemon");

            SpringLayout layout = new SpringLayout();
            panel = new JPanel();
            panel.setLayout(layout);

            setContentPane(panel);
            setSize(425, 240);
            setResizable(false);
            Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
            setLocation((int)((resolution.getWidth() - getWidth()) / 2), (int)((resolution.getHeight() - getHeight()) / 2));

            editBox = new JComboBox(list.toArray());
            editBox.setEditable(false);
            editBox.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    JComboBox cb = (JComboBox)e.getSource();
                    selectedEditPokemon = list.get(cb.getSelectedIndex());

                    updateNumbers();
                }
            });

            shinyCheck = new JCheckBox("Shiny");
            shinyCheck.addItemListener(new ItemListener(){
                public void itemStateChanged(ItemEvent e){
                    if(e.getStateChange() == ItemEvent.DESELECTED)
                        shiny = false;
                    else if(e.getStateChange() == ItemEvent.SELECTED)
                        shiny = true;
                }
            });

            natureBox = new JComboBox<String>(natures);
            natureBox.setEditable(false);
            natureBox.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    JComboBox<String> cb = (JComboBox<String>)e.getSource();
                    natureIndex = cb.getSelectedIndex();
                }
            });

            selectedEditPokemon = (Pokemon)editBox.getSelectedItem();

            shinyCheck.setSelected(selectedEditPokemon.isShiny());

            for(int i = 0; i < baseStats.length; i ++)
                baseStats[i] = new JLabel("" + selectedEditPokemon.getBaseStat(i));

            for(int i = 0; i < IVFields.length; i ++){
                IVFields[i] = new JNumberTextField(2);
                IVFields[i].setColumns(3);
                IVFields[i].setAllowNegative(false);
                EVFields[i] = new JNumberTextField(3);
                EVFields[i].setColumns(3);
                EVFields[i].setAllowNegative(false);
            }
            levelField = new JNumberTextField(3);
            levelField.setColumns(3);

            JButton finishedButton = new JButton("Done");
            finishedButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    try{
                        list.remove(selectedEditPokemon);

                        int[] IVs = new int[IVFields.length];
                        int[] EVs = new int[EVFields.length];
                        for(int i = 0; i < IVFields.length; i ++){
                            if(IVFields[i].getText().length() == 0){
                                JOptionPane.showMessageDialog(null, "One of the IV fields is empty.", null, JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                            IVs[i] = IVFields[i].getInt();
                        }
                        for(int i = 0; i < EVFields.length; i ++){
                            if(EVFields[i].getText().length() == 0){
                                JOptionPane.showMessageDialog(null, "One of the EV fields is empty.", null, JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                            EVs[i] = EVFields[i].getInt();
                        }
                        if(levelField.getText().length() == 0){
                            JOptionPane.showMessageDialog(null, "The level field is empty.", null, JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        if(levelField.getInt() > 100){
                            JOptionPane.showMessageDialog(null, "You can't have more than 100 levels.", null, JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        for(int i = 0; i < IVFields.length; i ++){
                            if(IVFields[i].getInt() > 31 || IVFields[i].getInt() < 0){
                                JOptionPane.showMessageDialog(null, "You can't have more than 31 in an IV.", null, JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                        }
                        int totalEVs = 0;
                        for(int i = 0; i < EVFields.length; i ++){
                            totalEVs += EVFields[i].getInt();
                            if(EVFields[i].getInt() > 255 || EVFields[i].getInt() < 0){
                                JOptionPane.showMessageDialog(null, "You can't have more than 255 in an EV.", null, JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                        }
                        if(totalEVs > 510){
                            JOptionPane.showMessageDialog(null, "You can't have more than 510 total EVs.", null, JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        int level = levelField.getInt();
                        list.add(new Pokemon(selectedEditPokemon.getNumber(), level, IVs, EVs, natureIndex, shiny));

                        updateComboBox();
                        dispose();
                    }catch (IOException ioe){
                        JOptionPane.showMessageDialog(null, "There was an error loading one of the fields.", null, JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    dispose();
                }
            });

            updateNumbers();

            for(JLabel label: baseStats)
                add(label);
            for(JNumberTextField field: IVFields)
                add(field);
            for(JNumberTextField field: EVFields)
                add(field);

            add(editBox);
            add(natureBox);
            add(shinyCheck);

            add(levelField);
            add(baseStatLabel);
            add(IVLabel);
            add(EVLabel);
            add(levelLabel);
            for(JLabel label: categories)
                add(label);

            add(finishedButton);
            add(cancelButton);

            //Combo addBox
            layout.putConstraint(SpringLayout.WEST, editBox, 10, SpringLayout.WEST, panel);
            layout.putConstraint(SpringLayout.NORTH, editBox, 10, SpringLayout.NORTH, panel);

            //Nature Combo addBox
            layout.putConstraint(SpringLayout.WEST, natureBox, 10, SpringLayout.EAST, editBox);
            layout.putConstraint(SpringLayout.NORTH, natureBox, 0, SpringLayout.NORTH, editBox);

            //Shiny Checkbox
            layout.putConstraint(SpringLayout.WEST, shinyCheck, 10, SpringLayout.EAST, natureBox);
            layout.putConstraint(SpringLayout.NORTH, shinyCheck, 0, SpringLayout.NORTH, natureBox);

            //Level Label
            layout.putConstraint(SpringLayout.WEST, levelLabel, 0, SpringLayout.WEST, editBox);
            layout.putConstraint(SpringLayout.NORTH, levelLabel, 10, SpringLayout.SOUTH, editBox);

            //Level Field
            layout.putConstraint(SpringLayout.WEST, levelField, 5, SpringLayout.EAST, levelLabel);
            layout.putConstraint(SpringLayout.NORTH, levelField, 0, SpringLayout.NORTH, levelLabel);

            //Categories
            layout.putConstraint(SpringLayout.WEST, categories[0], 20, SpringLayout.EAST, baseStatLabel);
            layout.putConstraint(SpringLayout.NORTH, categories[0], 0, SpringLayout.NORTH, levelLabel);
            for(int i = 1; i < categories.length; i ++){
                layout.putConstraint(SpringLayout.WEST, categories[i], 50 * i, SpringLayout.WEST, categories[0]);
                layout.putConstraint(SpringLayout.NORTH, categories[i], 0, SpringLayout.NORTH, categories[i - 1]);
            }

            //Stats Label
            layout.putConstraint(SpringLayout.WEST, baseStatLabel, 0, SpringLayout.WEST, levelLabel);
            layout.putConstraint(SpringLayout.NORTH, baseStatLabel, 10, SpringLayout.SOUTH, categories[0]);

            //Stats
            layout.putConstraint(SpringLayout.WEST, baseStats[0], 0, SpringLayout.WEST, categories[0]);
            layout.putConstraint(SpringLayout.NORTH, baseStats[0], 0, SpringLayout.NORTH, baseStatLabel);
            for(int i = 1; i < baseStats.length; i ++){
                layout.putConstraint(SpringLayout.WEST, baseStats[i], 0, SpringLayout.WEST, categories[i]);
                layout.putConstraint(SpringLayout.NORTH, baseStats[i], 0, SpringLayout.NORTH, baseStatLabel);
            }

            //IVs Label
            layout.putConstraint(SpringLayout.WEST, IVLabel, 0, SpringLayout.WEST, baseStatLabel);
            layout.putConstraint(SpringLayout.NORTH, IVLabel, 10, SpringLayout.SOUTH, baseStatLabel);

            //IV Fields
            layout.putConstraint(SpringLayout.WEST, IVFields[0], 0, SpringLayout.WEST, categories[0]);
            layout.putConstraint(SpringLayout.NORTH, IVFields[0], 0, SpringLayout.NORTH, IVLabel);
            for(int i = 1; i < IVFields.length; i ++){
                layout.putConstraint(SpringLayout.WEST, IVFields[i], 0, SpringLayout.WEST, baseStats[i]);
                layout.putConstraint(SpringLayout.NORTH, IVFields[i], 0, SpringLayout.NORTH, IVFields[i - 1]);
            }

            //EVs Label
            layout.putConstraint(SpringLayout.WEST, EVLabel, 0, SpringLayout.WEST, IVLabel);
            layout.putConstraint(SpringLayout.NORTH, EVLabel, 10, SpringLayout.SOUTH, IVLabel);

            //EV Fields
            layout.putConstraint(SpringLayout.WEST, EVFields[0], 0, SpringLayout.WEST, categories[0]);
            layout.putConstraint(SpringLayout.NORTH, EVFields[0], 0, SpringLayout.NORTH, EVLabel);
            for(int i = 1; i < EVFields.length; i ++){
                layout.putConstraint(SpringLayout.WEST, EVFields[i], 0, SpringLayout.WEST, IVFields[i]);
                layout.putConstraint(SpringLayout.NORTH, EVFields[i], 0, SpringLayout.NORTH, EVFields[i - 1]);
            }

            //Button
            layout.putConstraint(SpringLayout.WEST, finishedButton, 0, SpringLayout.WEST, levelLabel);
            layout.putConstraint(SpringLayout.NORTH, finishedButton, 20, SpringLayout.SOUTH, EVLabel);

            //Cancel Button
            layout.putConstraint(SpringLayout.WEST, cancelButton, 10, SpringLayout.EAST, finishedButton);
            layout.putConstraint(SpringLayout.NORTH, cancelButton, 0, SpringLayout.NORTH, finishedButton);

        }

        public void updateNumbers(){
            if(selectedEditPokemon != null){
                natureBox.setSelectedItem(selectedEditPokemon.getNature());
                levelField.setNumber(selectedEditPokemon.getLevel());
                for(int i = 0; i < IVFields.length; i ++){
                    IVFields[i].setNumber(selectedEditPokemon.getIV(i));
                    EVFields[i].setNumber(selectedEditPokemon.getEV(i));
                }

                for(int i = 0; i < baseStats.length; i ++)
                    baseStats[i].setText("" + selectedEditPokemon.getBaseStat(i));
            }
            else{
                for(int i = 0; i < baseStats.length; i ++)
                    baseStats[i].setText("" + 0);
            }
        }

        public ArrayList<BasicPokemon> getAllPokemon(){
            ArrayList<BasicPokemon> pokes = new ArrayList<BasicPokemon>();
            try{
                FileInputStream in = new FileInputStream("data/database.bpok");
                ObjectInputStream ois = new ObjectInputStream(in);

                while(true){
                    try{
                        pokes.add((BasicPokemon)ois.readObject());
                    }
                    catch(Exception e1){
                        break;
                    }
                }

                ois.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            return pokes;
        }
    }
}