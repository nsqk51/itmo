package Main;

public enum TopicToSpeek implements Name{
	BIRTHDAY("День рождения Малыша"),
	GRANDMA("Бабушка Карлсона"),
	ENYTHINGELSE("Что-то другое");
	
	private final String name;
	
	TopicToSpeek(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
