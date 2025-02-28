/*
 *  Copyright (C) 2021 the original author or authors.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package we.fizz.component;

/**
 * Component Type
 * 
 * @author Francis Dong
 *
 */
public enum ComponentTypeEnum {

	CONDITION("condition"), CIRCLE("circle");

	private String code;

	private ComponentTypeEnum(String code) {
		this.code = code;
	}

	public static ComponentTypeEnum getEnumByCode(String code) {
		for (ComponentTypeEnum item : ComponentTypeEnum.values()) {
			if (item.getCode().equals(code)) {
				return item;
			}
		}

		return null;
	}

	public String getCode() {
		return code;
	}

}
