package mymoves;

import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.Status;
import ru.ifmo.se.pokemon.StatusMove;
import ru.ifmo.se.pokemon.Type;

public class Confide extends StatusMove{
	public Confide(double pow, double acc) {
		super (Type.NORMAL, pow,acc);
	}
	@Override
    protected void applyOppEffects(Pokemon p1) {
     super.applyOppEffects(p1);
     Effect effect = new Effect();
     if (effect.condition() == Status.POISON) {
    	 Effect mst = new Effect().stat(Stat.SPECIAL_ATTACK, -1);
         p1.addEffect(mst);
     }
    }
    
    @Override
    protected String describe() {
     return "Использует Confide";
    }
}
