package Main;

public class TimeOfDay {
	private boolean time;
	
	public TimeOfDay() {
		if (Math.random() < 0.5) {
			this.time = true;
		}
		else {
			this.time = false;
		}
	}
	
	public boolean IsNight() {
		return time;
	}
}
