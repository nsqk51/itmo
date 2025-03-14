package Main;

import java.util.List;

public class Karlson extends person{
	
	public Karlson(MoodImpl Pers) {
		super(Pers);
	}

	public void Say(String a) {
		System.out.println("Карлсон говорит о "+a);
	}

	@Override
	public void Sleep() {
		System.out.println("Карлсон пошел спать.");
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