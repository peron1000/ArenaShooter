package arenashooter.engine.xmlReaders;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import arenashooter.engine.animation.AnimationData;
import arenashooter.engine.animation.animevents.AnimEvent;
import arenashooter.engine.animation.tracks.AnimTrackDouble;
import arenashooter.engine.animation.tracks.AnimTrackTexture;
import arenashooter.engine.animation.tracks.AnimTrackVec2f;
import arenashooter.engine.animation.tracks.AnimTrackVec3f;
import arenashooter.engine.animation.tracks.EventTrack;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;

public class AnimationXmlReader extends XmlReader {

	private static double length;

	private static boolean loop;
	
	private static Map<Double, AnimEvent> events;
	
	private static Map<String, AnimTrackDouble>  tracksD;
	private static Map<String, AnimTrackTexture> tracksT;
	private static Map<String, AnimTrackVec2f>   tracksVec2f;
	private static Map<String, AnimTrackVec3f>   tracksVec3f;
	
	private AnimationXmlReader() { }

	public static AnimationData read(String path) {
		parse(path);
		
		try {
			length = Double.parseDouble(root.getAttribute("length"));
		} catch(Exception e) {
			e.printStackTrace();
			length = 1.0;
		}
		
		loop = false;
		if(root.hasAttribute("loop")) {
			try {
				loop = Boolean.parseBoolean(root.getAttribute("loop"));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		events = new HashMap<>();
		tracksD = new HashMap<>();
		tracksT = new HashMap<>();
		tracksVec2f = new HashMap<>();
		tracksVec3f = new HashMap<>();
		
		NodeList tracks = root.getChildNodes();
		
		Node current = null;
		for(int i=0; i<tracks.getLength(); i++) {
			if(tracks.item(i).getNodeType() != Node.ELEMENT_NODE) continue;
			
			current = tracks.item(i);
			switch( current.getNodeName() ) {
			case "trackEvent":
				readTrackEvents(current);
				break;
			case "trackD":
				readTrackD(current);
				break;
			case "trackT":
				readTrackT(current);
				break;
			case "trackVec2f":
				readTrackVec2f(current);
				break;
			case "trackVec3f":
				readTrackVec3f(current);
				break;
			default:
				System.out.println("Animation - Unknown element "+current.getNodeName()+" in "+path);
			}
		}
		
		return new AnimationData(length, loop, new EventTrack(events), tracksD, tracksT, tracksVec2f, tracksVec3f);
	}
	
	private static void readTrackD(Node node) {
		Map<Double, Double> keyframes = new HashMap<>();
		
		NodeList children = node.getChildNodes();
		Element current;
		for( int i=0; i<children.getLength(); i++ ) {
			if( children.item(i).getNodeType() != Node.ELEMENT_NODE ) continue;
			current = (Element)children.item(i);

			double time = Double.parseDouble(current.getAttribute("time"));
			double value = Double.parseDouble(current.getAttribute("value"));
			keyframes.put(time, value);
		}
		
		tracksD.put(node.getNodeName(), new AnimTrackDouble(keyframes));
	}
	
	private static void readTrackT(Node node) {
		Map<Double, Texture> keyframes = new HashMap<>();
		
		NodeList children = node.getChildNodes();
		Element current;
		for( int i=0; i<children.getLength(); i++ ) {
			if( children.item(i).getNodeType() != Node.ELEMENT_NODE ) continue;
			current = (Element)children.item(i);

			double time = Double.parseDouble(current.getAttribute("time"));
			Texture value = Texture.loadTexture(current.getAttribute("value"));
			keyframes.put(time, value);
		}
		
		tracksT.put(node.getNodeName(), new AnimTrackTexture(keyframes));
	}
	
	private static void readTrackVec2f(Node node) {
		Map<Double, Vec2f> keyframes = new HashMap<>();
		
		NodeList children = node.getChildNodes();
		Element current;
		for( int i=0; i<children.getLength(); i++ ) {
			if( children.item(i).getNodeType() != Node.ELEMENT_NODE ) continue;
			current = (Element)children.item(i);

			double time = Double.parseDouble(current.getAttribute("time"));
			double x = Double.parseDouble(current.getAttribute("x"));
			double y = Double.parseDouble(current.getAttribute("y"));
			Vec2f value = new Vec2f(x, y);
			keyframes.put(time, value);
		}

		tracksVec2f.put(node.getNodeName(), new AnimTrackVec2f(keyframes));
	}
	
	private static void readTrackVec3f(Node node) {
		Map<Double, Vec3f> keyframes = new HashMap<>();
		
		NodeList children = node.getChildNodes();
		Element current;
		for( int i=0; i<children.getLength(); i++ ) {
			if( children.item(i).getNodeType() != Node.ELEMENT_NODE ) continue;
			current = (Element)children.item(i);

			double time = Double.parseDouble(current.getAttribute("time"));
			double x = Double.parseDouble(current.getAttribute("x"));
			double y = Double.parseDouble(current.getAttribute("y"));
			double z = Double.parseDouble(current.getAttribute("z"));
			Vec3f value = new Vec3f(x, y, z);
			keyframes.put(time, value);
		}
		
		tracksVec3f.put(node.getNodeName(), new AnimTrackVec3f(keyframes));
	}
	
	private static void readTrackEvents(Node node) {
		//TODO: load events
		System.out.println("Animation - Loading anim events is not supported yet!");
	}
}
