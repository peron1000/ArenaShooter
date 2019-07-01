package arenashooter.engine.json;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Entity;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.Barrel;
import arenashooter.entities.spatials.Character;
import arenashooter.entities.spatials.Spatial3;
import arenashooter.entities.spatials.items.Gun;
import arenashooter.game.CharacterClass;
import arenashooter.game.CharacterInfo;

public class TextJson {
	public static void main(String[] args) throws IOException {
		Entity e = new Entity();
		Gson gson = new Gson();
		String json = gson.toJson(e);
		int indent = 0;
		String jsonIndented = "";
		for (int i = 0; i < json.length(); i++) {
			if (json.charAt(i) == '{') {
				indent++;
				String indentation = "\n";
				for (int j = 0; j < indent; j++) {
					indentation += "\t";
				}
				jsonIndented += json.charAt(i) + indentation;
			} else if (json.charAt(i) == ',') {
				String indentation = "\n";
				for (int j = 0; j < indent; j++) {
					indentation += "\t";
				}
				jsonIndented += json.charAt(i) + indentation;
			} else if (json.charAt(i) == '}') {
				indent--;
				jsonIndented += json.charAt(i);
			} else {
				jsonIndented += json.charAt(i);
			}
		}
		File parent = new File("data/mapJSON");
		if(!parent.exists()) {
			parent.mkdir();
		}
		File file = new File(parent , "test.json");
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter writer = new FileWriter(file);
		writer.write(jsonIndented);
		writer.close();
	}
}
