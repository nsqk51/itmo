package mypokemons;

import mymoves.Psychic;
import mymoves.RazorLeaf;
import mymoves.ScaryFace;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Pumpkaboo extends Pokemon{
	
	public Pumpkaboo(String name, int level ) {
		super (name, level);
		
		ScaryFace scaryface = new ScaryFace(100, 100);
		super.setMove(scaryface);
		Psychic psychic = new Psychic(90, 100);
		super.setMove(psychic);
		RazorLeaf razorleaf = new RazorLeaf(55, 95);
	    super.setMove(razorleaf);
	
		super.setType(Type.GHOST,Type.GRASS);
		super.setStats(49, 66, 70, 44, 55, 51);
		
		
	}
}
