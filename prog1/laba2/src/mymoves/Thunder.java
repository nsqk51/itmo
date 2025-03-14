package mymoves;

import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Type;

public class Thunder extends SpecialMove{
 	public  Thunder(double pow, double acc) {
 		super (Type.ELECTRIC, pow,acc);
 	}
 	
 	@Override
    protected void applyOppEffects(Pokemon p1) {
     super.applyOppEffects(p1);
     if (0.3 <= Math.random()) {
         Effect.paralyze(p1);
     }
    }
    
    @Override
    protected String describe() {
     return "Использует Thunder";
    }
     
 }