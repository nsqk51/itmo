package mypokemons;

import mymoves.Thunder;
import ru.ifmo.se.pokemon.Type;

public final class Wigglytuff extends Jigglypuff{
	public Wigglytuff(String name, int level) {
		super (name, level);
		
		Thunder thunder = new Thunder(110, 70);
		super.setMove(thunder);
		
		super.setType(Type.NORMAL,Type.FAIRY);
		super.setStats(140, 70, 45, 80, 50, 45);
	}
}