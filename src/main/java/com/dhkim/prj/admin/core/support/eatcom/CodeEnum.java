package com.dhkim.prj.admin.core.support.eatcom;

import java.util.EnumSet;

/**
 * 별도의 코드값을 가지는 enum
 * <pre>
 * (사용 예시)
 * &#64;lombok.Getter
 * &#64;lombok.AllArgsConstructor
 * public enum CommonStatus implements CodeEnum {
 *   ENABLED("01", "정상"), 
 *   DISABLED("02", "중지");
 *	
 *   private String code;
 *   private String description;
 * }
 * </pre>
 */
public interface CodeEnum {
	
	String getCode();
	String getDescription();
	
	static <E extends Enum<E> & CodeEnum> E valueOfCode(Class<E> enumClass, String code) {
		for (E e : EnumSet.allOf(enumClass)) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
}
