package mymoves;

import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class PoisonTail  extends PhysicalMove{
 	public  PoisonTail (double pow, double acc) {
 		super (Type.POISON, pow,acc);
 	}
 	
 	@Override
    protected void applyOppEffects(Pokemon p1) {
     super.applyOppEffects(p1);
     if (0.1 <= Math.random()) {
         Effect.poison(p1);
     }
    }
    
    @Override
    protected String describe() {
     return "Использует Poison Tail";
    }
     
 }
