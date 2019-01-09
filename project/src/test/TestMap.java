package test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.junit.Test;

import arenashooter.engine.MapXMLTranslator;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Entity;
import arenashooter.entities.Map;
import arenashooter.entities.spatials.Plateform;

public class TestMap {
	
	@Test
	public void testPlateform1() {
		Map map = MapXMLTranslator.getMap("res/data/mapXML/mapXML.xml");
		HashMap<String, Entity> mapChildren = map.children;
		Collection<Entity> entities = mapChildren.values();
		boolean find = false;
		for (Entity entity : entities) {
			if(entity instanceof Plateform) {
				Plateform p = (Plateform) entity;
				if(p.position.x == 0 && p.position.y == 510 && p.getExtendX() == 1500 && p.getExtendY() == 100) {
					find = true;
				}
			}
		}
		assertTrue(find);
	}
	
	@Test
	public void testPlateform2() {
		Map map = MapXMLTranslator.getMap("res/data/mapXML/mapXML.xml");
		HashMap<String, Entity> mapChildren = map.children;
		Collection<Entity> entities = mapChildren.values();
		boolean find = false;
		for (Entity entity : entities) {
			if(entity instanceof Plateform) {
				Plateform p = (Plateform) entity;
				if(p.position.x == -800 && p.position.y == 210 && p.getExtendX() == 300 && p.getExtendY() == 300) {
					find = true;
				}
			}
		}
		assertTrue(find);
	}
	
	@Test
	public void testSpawn1() {
		Map map = MapXMLTranslator.getMap("res/data/mapXML/mapXML.xml");
		ArrayList<Vec2f> spawn = map.spawn;
		boolean find = false;
		for (Vec2f vec2f : spawn) {
			if(vec2f.x == -300 && vec2f.y ==0) {
				find = true;
			}
		}
		assertTrue(find);
	}
	
	@Test
	public void testSpawn2() {
		Map map = MapXMLTranslator.getMap("res/data/mapXML/mapXML.xml");
		ArrayList<Vec2f> spawn = map.spawn;
		boolean find = false;
		for (Vec2f vec2f : spawn) {
			if(vec2f.x == 300 && vec2f.y ==0) {
				find = true;
			}
		}
		assertTrue(find);
	}
}
