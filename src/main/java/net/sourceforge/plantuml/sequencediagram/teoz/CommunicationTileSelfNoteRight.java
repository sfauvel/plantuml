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
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.Note;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class CommunicationTileSelfNoteRight extends AbstractTile {

	private final CommunicationTileSelf tile;
	private final Message message;
	private final Rose skin;
	private final ISkinParam skinParam;
	private final Note noteOnMessage;

	public Event getEvent() {
		return message;
	}

	@Override
	public double getContactPointRelative() {
		return tile.getContactPointRelative();
	}

	public CommunicationTileSelfNoteRight(CommunicationTileSelf tile, Message message, Rose skin, ISkinParam skinParam,
			Note noteOnMessage) {
		super(((AbstractTile) tile).getStringBounder());
		this.tile = tile;
		this.message = message;
		this.skin = skin;
		this.skinParam = skinParam;
		this.noteOnMessage = noteOnMessage;
	}

	@Override
	final protected void callbackY_internal(double y) {
		tile.callbackY(y);
	}

	private Component getComponent(StringBounder stringBounder) {
		final Component comp = skin.createComponent(null, ComponentType.NOTE, null,
				noteOnMessage.getSkinParamBackcolored(skinParam), noteOnMessage.getStrings());
		return comp;
	}

	private Real getNotePosition(StringBounder stringBounder) {
		return tile.getMaxX();
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Component comp = getComponent(stringBounder);
		final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		final Area area = new Area(dim.getWidth(), dim.getHeight());
		tile.drawU(ug);
		final Real p = getNotePosition(stringBounder);

		comp.drawU(ug.apply(UTranslate.dx(p.getCurrentValue())), area, (Context2D) ug);
	}

	public double getPreferredHeight() {
		final Component comp = getComponent(getStringBounder());
		final Dimension2D dim = comp.getPreferredDimension(getStringBounder());
		return Math.max(tile.getPreferredHeight(), dim.getHeight());
	}

	public void addConstraints() {
		tile.addConstraints();
	}

	public Real getMinX() {
		return tile.getMinX();
	}

	public Real getMaxX() {
		final Component comp = getComponent(getStringBounder());
		final Dimension2D dim = comp.getPreferredDimension(getStringBounder());
		return getNotePosition(getStringBounder()).addFixed(dim.getWidth());
	}

}
