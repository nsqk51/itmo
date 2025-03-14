package mypokemons;



import mymoves.PoisonTail;
import mymoves.Screech;
import mymoves.SwordsDance;
import mymoves.VenomDrench;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public final class Seviper extends Pokemon{
	
	public Seviper(String name, int level ) {
		super (name, level);
		
		SwordsDance swordsDance = new SwordsDance(100,100);
		super.setMove(swordsDance);
		VenomDrench venomdrench = new VenomDrench(100,100);
		super.setMove(venomdrench);
		Screech screech = new Screech(100,85);
		super.setMove(screech);
		PoisonTail poisontail = new PoisonTail(50, 100);
		super.setMove(poisontail);
		
		super.setType(Type.POISON);
		super.setStats(73, 100, 60, 100, 60, 65);
		
		
	}
}
