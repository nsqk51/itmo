package mymoves;

import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.StatusMove;
import ru.ifmo.se.pokemon.Type;

public class ScaryFace extends StatusMove{
	public ScaryFace(double pow, double acc) {
		super (Type.NORMAL, pow,acc);
	}
	
	@Override
    protected void applyOppEffects(Pokemon p1) {
     super.applyOppEffects(p1);
	 Effect mst = new Effect().stat(Stat.SPEED, -2);
     p1.addEffect(mst);
    }
    
    @Override
    protected String describe() {
     return "Использует Scary Face ";
	    
	}

}