package arenashooter.engine.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(TYPE)
public @interface JsonRoot {
	/**
	 * Give the extension of the file in a Json transformation.</br>
	 * Example: <code>".obj"</code>
	 */
	String extension();
	
	/**
	 * Give the path of the parent directory for a Json transformation
	 */
	String directory();
}
