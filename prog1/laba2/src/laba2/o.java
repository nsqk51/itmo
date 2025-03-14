package laba2;
import mypokemons.*;
import ru.ifmo.se.pokemon.Battle;

public class o {
	
	public static void main(String[] args) {
		

		Battle b = new Battle();
		Pumpkaboo pumpkaboo = new Pumpkaboo("Pumpkin", 1);
		Seviper seviper = new Seviper("Snake",1);
		Wigglytuff wigglytuff = new Wigglytuff("Tuff",1);
		Gourgeist gourgeist= new Gourgeist("Jack",1);
		Jigglypuff jigglypuff = new Jigglypuff("Puff",1);
		Igglybuff igglybuff = new Igglybuff("Buff",1);
		b.addAlly(pumpkaboo);
		b.addAlly(wigglytuff);
		b.addAlly(jigglypuff);
		b.addFoe(seviper);
		b.addFoe(gourgeist);
		b.addFoe(igglybuff);
		b.go();
		
	}	
}