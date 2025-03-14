package mymoves;

import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.Type;

public class Psychic  extends SpecialMove{
	public  Psychic (double pow, double acc) {
		super (Type.PSYCHIC, pow,acc);
	}
	
	@Override
   protected void applyOppEffects(Pokemon p1) {
    super.applyOppEffects(p1);
    if (0.1 <= Math.random()) {
    	Effect mst = new Effect().stat(Stat.SPECIAL_DEFENSE, -1);
        p1.addEffect(mst);
    }
   }
   
   @Override
   protected String describe() {
    return "Использует Psychic";
   }
    
}