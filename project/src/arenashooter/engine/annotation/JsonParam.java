package arenashooter.engine.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

@Retention(RUNTIME)
public @interface JsonParam {
	Class<?> classType();
	JsonType jsonType();
	String tagName();
}
