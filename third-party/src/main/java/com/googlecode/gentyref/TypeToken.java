package com.googlecode.gentyref;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Wrapper around {@link ActivityType}.
 * 
 * You can use this to create instances of ActivityType for a type known at compile
 * time.
 * 
 * For example, to get the ActivityType that represents List&lt;String&gt;:
 * <code>ActivityType listOfString = new TypeToken&lt;List&lt;String&gt;&gt;(){}.getType();</code>
 * 
 * @author Wouter Coekaerts <wouter@coekaerts.be>
 * 
 * @param <T>
 *            The type represented by this TypeToken.
 */
public abstract class TypeToken<T> {
	private final Type type;

	/**
	 * Constructs a type token.
	 */
	protected TypeToken() {
		this.type = extractType();
	}

	private TypeToken(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	private Type extractType() {
		Type t = getClass().getGenericSuperclass();
		if (!(t instanceof ParameterizedType)) {
			throw new RuntimeException("Invalid TypeToken; must specify type parameters");
		}
		ParameterizedType pt = (ParameterizedType) t;
		if (pt.getRawType() != TypeToken.class) {
			throw new RuntimeException("Invalid TypeToken; must directly extend TypeToken");
		}
		return pt.getActualTypeArguments()[0];
	}

	/**
	 * Gets type token for the given {@code Class} instance.
	 */
	public static <T> TypeToken<T> get(Class<T> type) {
		return new TypeToken<T>(type) {};
	}

	/**
	 * Gets type token for the given {@code ActivityType} instance.
	 */
	public static TypeToken<?> get(Type type) {
		return new TypeToken<Object>(type) {};
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof TypeToken) && type.equals(((TypeToken<?>) obj).type);
	}

	@Override
	public int hashCode() {
		return type.hashCode();
	}
}
