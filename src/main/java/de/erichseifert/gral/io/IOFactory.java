/*
 * GRAL: GRAphing Library for Java(R)
 *
 * (C) Copyright 2009-2010 Erich Seifert <info[at]erichseifert.de>, Michael Seifert <michael.seifert[at]gmx.net>
 *
 * This file is part of GRAL.
 *
 * GRAL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GRAL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GRAL.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.erichseifert.gral.io;

import java.util.List;

/**
 * Interface for factories producing input or output classes.
 * This is be used to create a extensible plug-in system for reading or writing.
 * @param <T> Class of the objects produced by the factory.
 */
public interface IOFactory<T> {
	/**
	 * Returns an object for reading or writing the specified format.
	 * @param mimeType MIME type.
	 * @return Reader or writer for the specified MIME type.
	 */
	public abstract T get(String mimeType);

	/**
	 * Returns the capabilities for a specific format.
	 * @param mimeType MIME type of the format
	 * @return Capabilities for the specified format.
	 */
	public abstract IOCapabilities getCapabilities(String mimeType);

	/**
	 * Returns a list of capabilities for all supported formats.
	 * @return Supported capabilities.
	 */
	public abstract List<IOCapabilities> getCapabilities();

	/**
	 * Returns an array of Strings containing all supported formats.
	 * @return Supported formats.
	 */
	public abstract String[] getSupportedFormats();

	/**
	 * Returns whether the specified MIME type is supported.
	 * @param mimeType MIME type.
	 * @return <code>true</code> if supported, otherwise <code>false</code>.
	 */
	public abstract boolean isFormatSupported(String mimeType);
}
