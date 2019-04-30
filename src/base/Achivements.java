package base;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Achivements {
	
	
	
	
	public static Set<String> loadAchivements(){
		try{
			List<String> lines = Files.readAllLines(Paths.get("achivements.inf"), Charset.forName("UTF-8"));
			HashSet<String> set= new HashSet<>();
			set.addAll(lines);
			return set;
		} catch (Exception e) {
			return new HashSet<>();
		}
	}
	public static Set<String> loadEndings(){
		try{
		List<String> lines = Files.readAllLines(Paths.get("ending.inf"), Charset.forName("UTF-8"));
		HashSet<String> set= new HashSet<>();
		set.addAll(lines);
		return set;
		} catch (Exception e) {
			return new HashSet<>();
		}
	}
	public static void addAchivement(String achivement){
		try {
			Set<String> set = loadAchivements();
			set.add(achivement);
			Files.write(Paths.get("achivements.inf"), set);
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}
	public static void addEnding(String achivement){
		
		try {
			Set<String> set = loadEndings();
			set.add(achivement);
			Files.write(Paths.get("ending.inf"), set);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
