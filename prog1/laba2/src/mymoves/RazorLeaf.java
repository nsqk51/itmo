package mymoves;


import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Type;

public class RazorLeaf extends PhysicalMove{
	public  RazorLeaf(double pow, double acc) {
		super (Type.GRASS, pow,acc);
	}
	
   
   @Override
   protected String describe() {
    return "Использует Razor Leaf";
   }
    
}