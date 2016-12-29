package projects.zunawe.pokesorter;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class HelpDialog extends JDialog{
    private static final long serialVersionUID = 6831794530647817032L;

    private JLabel context;
    private String[] contextChoices = {"<html>The purpose of this program is to help you organize <br>the information of all the pokemon you choose <br>without having to create a custom flowchart. It <br>asks for the number of EVs, IVs, and levels your <br>pokemon has, and then stores the information in a <br>list. You can save this list in a file to access later. <br>Created pokemon are also editable for when you <br>have to update EVs or levels.</html>",
            "<html>Effort values (or EVs) are points given to pokemon <br>based on the pokemon they've battled throughout <br>their entire history. A pokemon can have a number <br>of EVs in each of the six stats with a minimum of 0 <br>and a maximum of 255. Any given pokemon cannot <br>have more than 510 total EVs. A pokemon gains <br>specific EVs from defeating specific pokemon. <br>After a pokemon gains four EVs in a stat, it will <br>gain one stat point either when leveling up <br>(generations I-IV) or immediately (generation V). <br>It is currently not possible to find out what your <br>pokemon's EVs are without exporting it to a <br>computer, using a cheating device, or using an EV <br>calculator (which is only reliably accurate at level <br>100, but can still deviate). Because of this, most <br>people keep track of their pokemon's EVs in a <br>chart, or, you can use this program with new <br>pokemon. I reccomend Serebii for more information <br>about EVs.</html>",
            "<html>Individual values (or IVs) are points that every <br>pokemon is given during it's creation. They can <br>never be changed during gameplay, and range from <br>0 to 31 in each stat. Though extremely rare <br>(1/1,073,741,824 found pokemon), it is possible to <br>find a pokemon with 31 in all stats. The higher an <br>IV is for a particular stat, the higher the stat will <br>be overall. I believe it is possible to find your IVs <br>in-game, but I'm not sure. The other way is to use <br>an online calculator. You will need to know your EVs <br>to use it effectively, and it increases in accuracy <br>the closer your pokemon is tovlevel 100. I <br>reccomend using Serebii to find out more about IVs.</html>",
            "<html>Shiny pokemon are pokemon with a slightly <br>differently-colored sprite and a short sparkling <br>animation when put into battle. There is a 1/8192 <br>chance of any given pokemon to be shiny. These <br>chances can increase when certain effects take <br>place. There is no added bonus to having a shiny <br>pokemon. They are just rare and generally <br>interesting to look at.</html>",
            "<html>Some pokemon have alternate forms that may <br>have different base stats, different colors, or <br>different types. In general, each pokemon's form <br>changes are unique. Pokemon with alternate forms <br>are not considered shiny, even if the only change is <br>coloration. All alternate forms of pokemon share a <br>pokedex number, but have a different sprite. When <br>choosing a pokemon with alternate forms in this <br>program, you will see the effects of the forms when <br>selecting the different forms in a second combo <br>box that appears. These changes will only reflect <br>the sprite and stats in the current version of this <br>program.</html>"};
    private int displayIndex = 0;

    private JPanel panel;

    public HelpDialog(JFrame owner){
        super(owner, "Help Contents");

        String[] listComponents = {"Purpose", "EVs", "IVs", "Shiny", "Alternate Forms"};
        JList<String> list = new JList<String>(listComponents);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setVisibleRowCount(-1);
        list.addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e){
                if (e.getValueIsAdjusting() == false){
                    int index = ((JList<String>)(e.getSource())).getSelectedIndex();
                    if(index == -1)
                        displayIndex = 0;
                    else
                        displayIndex = index;
                }
                resetDisplay();
            }
        });

        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(100, 353));

        SpringLayout layout = new SpringLayout();
        panel = new JPanel();
        panel.setLayout(layout);

        context = new JLabel(contextChoices[displayIndex]);

        panel.add(context);
        panel.add(listScroller);

        layout.putConstraint(SpringLayout.WEST, listScroller, 10, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, listScroller, 10, SpringLayout.NORTH, panel);

        layout.putConstraint(SpringLayout.WEST, context, 10, SpringLayout.EAST, listScroller);
        layout.putConstraint(SpringLayout.NORTH, context, 0, SpringLayout.NORTH, listScroller);

        setContentPane(panel);
        setSize(425, 400);
        setResizable(false);
        Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int)((resolution.getWidth() - getWidth()) / 2), (int)((resolution.getHeight() - getHeight()) / 2));
    }

    private void resetDisplay(){
        context.setText(contextChoices[displayIndex]);
    }
}