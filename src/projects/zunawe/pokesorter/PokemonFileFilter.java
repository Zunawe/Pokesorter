package projects.zunawe.pokesorter;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class PokemonFileFilter extends FileFilter{
    private String extension = "pok";

    @Override
    public boolean accept(File file) {
        if(file.getName().toLowerCase().endsWith("." + extension) || file.isDirectory())
            return true;
        return false;
    }

    @Override
    public String getDescription() {
        return "Pokemon Files (*." + extension + ")";
    }
}