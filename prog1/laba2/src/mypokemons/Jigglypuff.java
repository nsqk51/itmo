package mypokemons;

import mymoves.HyperVoice;
import ru.ifmo.se.pokemon.Type;

public class Jigglypuff extends Igglybuff{
	public Jigglypuff(String name, int level) {
		super (name, level);
		
		HyperVoice hypervoice = new HyperVoice(90, 100);
		super.setMove(hypervoice);
		
		super.setType(Type.NORMAL,Type.FAIRY);
		super.setStats(115, 45, 20, 45, 25, 20);
	}
}