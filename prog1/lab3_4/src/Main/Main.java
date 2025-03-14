package Main;

import java.util.ArrayList;
import java.util.List;

public class Main {
	public static void main(String args[]) {
		
		Main day = new Main();
		try {
			day.getDateBirthday();
		} catch (MyException e) {
			e.printStackTrace();
		}
		
		TimeOfDay time = new TimeOfDay();
		
		List<String> moodList = new ArrayList<>();
        moodList.add("побледнел");
        moodList.add("повеселел");
        moodList.add("разозлился");
        moodList.add("помрачнел");

        Mood moodProvider = new Mood(moodList);
		
		Karlson karlson = new Karlson(moodProvider);
		person malish = new Malish(moodProvider);
		
		Mood mood = new Mood(karlson.getList());
        try {
            mood.getMood();
        } catch (EmptyEmotionListException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
		
		TopicToSpeek birthday = TopicToSpeek.BIRTHDAY;
		TopicToSpeek grandma = TopicToSpeek.GRANDMA;
		TopicToSpeek enythingElse = TopicToSpeek.ENYTHINGELSE;
		
		String mood2 = karlson.getName();
		
		Action first = new Action("Карлсон сперва " + mood2);
		first.Print();
		
		boolean DayTrue = false;
		int BirthDay = (int) (Math.random()*31);
		if (BirthDay == 11) {
			DayTrue = true;
		}
		else {
			DayTrue = false;
		}
		
		if (time.IsNight() == true) {
			Action third = new Action("Уже стемнело");
			Action second = new Action("Малыш и Карлсон не успели поговорить ни о " + birthday.getName() + " ни о " + grandma.getName() + " ни о " + enythingElse.getName());
			third.Print();
			second.Print();
			if (DayTrue == true) {
				Action six = new Action("У Малыша завтра день рождения и он хочет лечь пораньше");
				six.Print();
				malish.Sleep();
			}
			else {
				Action seven = new Action("У Малыша завтра дня рождения нет и он решил перед сном обсудить что-то одно с Карлсоном");
				seven.Print();
				if (Math.random() > 0.5) {
					karlson.Say(birthday.getName());
				}
				else {
					karlson.Say(grandma.getName());
				}
				malish.Sleep();
			}
		}
		else {
			Action four = new Action("Ещё не стемнело");
			four.Print();
			Action five = new Action("Малыш и Карлсон успели поговорить и о " + birthday.getName() + " и о " + grandma.getName() + " и о " + enythingElse.getName());
		    five.Print();
		    malish.Sleep();
		}
	}
	
	public void getDateBirthday() throws MyException {
        
         int a = (int) (30*Math.random()+1);
            String input = "";

            if (input.trim().isEmpty()) {
                throw new MyException("Настроение не может быть пустым! Введенная строка: " + "\"" + input + "\"");
            }
            
            int moodValue;
            
            
        
        
    }
}