package mypokemons;


import mymoves.FocusBlast;
import ru.ifmo.se.pokemon.Type;

public final class Gourgeist extends Pumpkaboo{
	public Gourgeist(String name, int level) {
		super (name, level);
		
		FocusBlast focusblast = new FocusBlast(120, 70);
	    super.setMove(focusblast);
		
		super.setType(Type.GHOST,Type.GRASS);
		super.setStats(65, 90, 122, 58, 75, 84);
		
		
	}
}