package Main;

import java.util.List;

abstract class person implements Name, MoodImpl{
	private String mood;
	private MoodImpl Pers;
	
	public person(MoodImpl Pers) {
		this.Pers = Pers;
		List<String> moodKarlson = Pers.getAvailableMoods();
		Mood mood = new Mood(moodKarlson);
		String a = mood.getMood();
		this.mood = a;
	}
	
	public String getName() {
		return mood;
	}
	
	public List<String> getList() {
		return Pers.getAvailableMoods();
	}
	
	public abstract void Say(String a);
	public abstract void Sleep();
}
