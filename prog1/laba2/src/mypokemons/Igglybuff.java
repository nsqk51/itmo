package mypokemons;


import mymoves.Confide;
import mymoves.ThunderWave;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Igglybuff extends Pokemon{
	public Igglybuff(String name, int level) {
		super (name, level);
		
		ThunderWave thunderwave = new ThunderWave(100, 90);
	    super.setMove(thunderwave);
	    Confide confide= new Confide(100, 100);
	    super.setMove(confide);
		
		super.setType(Type.NORMAL,Type.FAIRY);
		super.setStats(90, 30, 15, 40, 20, 15);
	}
}