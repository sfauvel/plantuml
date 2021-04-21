/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.dedication;

import java.math.BigInteger;

public class RBlock {

	private final byte[] buffer;

	private RBlock(final byte[] init) {
		this.buffer = new byte[init.length + 1];
		System.arraycopy(init, 0, buffer, 1, init.length);
	}

	public RBlock(final byte[] init, int start, int size) {
		this.buffer = new byte[size + 1];
		if (start + size < init.length)
			System.arraycopy(init, start, buffer, 1, size);
		else
			System.arraycopy(init, start, buffer, 1, init.length - start);
	}

	public RBlock change(BigInteger E, BigInteger N) {
		final BigInteger big = new BigInteger(buffer);
		final BigInteger changed = big.modPow(E, N);
		return new RBlock(changed.toByteArray());
	}

	public byte[] getData(int size) {
		if (buffer.length == size) {
			return buffer;
		}
		final byte[] result = new byte[size];
		System.arraycopy(buffer, buffer.length - size, result, 0, size);
		return result;
	}

}
