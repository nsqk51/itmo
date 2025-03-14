package mymoves;

import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.Status;
import ru.ifmo.se.pokemon.StatusMove;
import ru.ifmo.se.pokemon.Type;

public class VenomDrench extends StatusMove{
	public VenomDrench(double pow, double acc) {
		super (Type.POISON, pow,acc);
	}
	@Override
    protected void applyOppEffects(Pokemon p1) {
     super.applyOppEffects(p1);
     Effect effect = new Effect();
     if (effect.condition() == Status.POISON) {
    	 Effect m1st = new Effect().stat(Stat.SPECIAL_ATTACK, -1);
    	 Effect m2st = new Effect().stat(Stat.ATTACK, -1);
    	 Effect m3st = new Effect().stat(Stat.SPEED, -1);
         p1.addEffect(m1st);
         p1.addEffect(m2st);
         p1.addEffect(m3st);
     }
    }
    
    @Override
    protected String describe() {
     return "Использует Venom Drench";
    }
}

