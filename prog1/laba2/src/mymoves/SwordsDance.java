package mymoves;

import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.StatusMove;
import ru.ifmo.se.pokemon.Type;

public class SwordsDance extends StatusMove{
	public SwordsDance(double pow, double acc) {
		super (Type.NORMAL, pow,acc);
	}
	
	@Override
    protected void applySelfEffects(Pokemon p1) {
     super.applySelfEffects(p1);
	 Effect pst = new Effect().stat(Stat.ATTACK, 2);
     p1.addEffect(pst);
    }
    
    @Override
    protected String describe() {
     return "Использует Swords Dance";
	    
	}

}
