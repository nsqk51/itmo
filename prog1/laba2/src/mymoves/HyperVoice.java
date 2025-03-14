package mymoves;

import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Type;

public class HyperVoice extends SpecialMove{
	public HyperVoice(double pow, double acc) {
		super (Type.NORMAL, pow,acc);
	}
    
    @Override
    protected String describe() {
     return "Использует Hyper Voice";
	    
	}

}