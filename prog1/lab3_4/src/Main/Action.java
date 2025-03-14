package Main;

public record Action(String string) {
	public void Print() {
		System.out.println(string);
	}
}
