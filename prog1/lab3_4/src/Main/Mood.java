package Main;

import java.util.List;


public class Mood implements MoodImpl{
	private List<String> emotions;
	
	public Mood(List<String> a) {
		this.emotions = a;
	}
	
	public String getMood() {
		if (emotions.isEmpty()) {
	        throw new EmptyEmotionListException("Список эмоций пуст.");
	    }
		
		int len = emotions.size();
		int a = (int) ((12*Math.random()) % len);
		String b = emotions.get(a);
		return b;
	}
	
	public List<String> getAvailableMoods(){
		return emotions;
	}
}