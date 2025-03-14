package Main;

import java.util.List;

public class Malish extends person{
	
	public Malish(MoodImpl Pers) {
		super(Pers);
	}

	public void Say(String a) {
		System.out.print("Малыш говорит о "+a);
	}

	@Override
	public void Sleep() {
		System.out.print("Малыш пошел спать.");
	}

	@Override
	public String getMood() {
		return this.getMood();
	}

	@Override
	public List<String> getAvailableMoods() {
		return this.getAvailableMoods();
	}
}
