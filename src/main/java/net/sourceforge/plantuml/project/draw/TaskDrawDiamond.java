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
package net.sourceforge.plantuml.project.draw;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.project.ToTaskDraw;
import net.sourceforge.plantuml.project.core.Task;
import net.sourceforge.plantuml.project.core.TaskAttribute;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;

public class TaskDrawDiamond extends AbstractTaskDraw {

	public TaskDrawDiamond(TimeScale timeScale, double y, String prettyDisplay, Day start, ISkinParam skinParam,
			Task task, ToTaskDraw toTaskDraw, StyleBuilder styleBuilder, HColorSet colorSet) {
		super(timeScale, y, prettyDisplay, start, skinParam, task, toTaskDraw, styleBuilder, colorSet);
	}

	@Override
	StyleSignature getStyleSignature() {
		return StyleSignature.of(SName.root, SName.element, SName.ganttDiagram, SName.milestone);
	}

	public double getHeightMax(StringBounder stringBounder) {
		return getFullHeightTask(stringBounder);
	}

	@Override
	protected double getShapeHeight(StringBounder stringBounder) {
//		final Style style = getStyle();
//		final ClockwiseTopRightBottomLeft padding = style.getPadding();
		int result = (int) getFontConfiguration().getFont().getSize2D();
		if (result % 2 == 1)
			result--;
		return result;

	}

	final public void drawTitle(UGraphic ug) {

		final Style style = getStyle();
		final ClockwiseTopRightBottomLeft margin = style.getMargin();
		final ClockwiseTopRightBottomLeft padding = style.getPadding();
		ug = ug.apply(UTranslate.dy(margin.getTop()));

		final TextBlock title = Display.getWithNewlines(prettyDisplay).create(getFontConfiguration(),
				HorizontalAlignment.LEFT, new SpriteContainerEmpty());
		final StringBounder stringBounder = ug.getStringBounder();
		final double titleHeight = title.calculateDimension(stringBounder).getHeight();
		final double h = (getShapeHeight(stringBounder) - titleHeight) / 2;

		final double x1 = timeScale.getStartingPosition(start);
		final double x2 = timeScale.getEndingPosition(start);
		final double width = getShapeHeight(ug.getStringBounder());
		final double delta = x2 - x1 - width;

		final double x = x2 - delta / 2 + padding.getLeft();
		title.drawU(ug.apply(new UTranslate(x, h)));
	}

	public void drawU(UGraphic ug) {

		final Style style = getStyle();
		final ClockwiseTopRightBottomLeft margin = style.getMargin();
		ug = ug.apply(UTranslate.dy(margin.getTop()));

		final double x1 = timeScale.getStartingPosition(start);
		final double x2 = timeScale.getEndingPosition(start);
		final double width = getShapeHeight(ug.getStringBounder());
		final double delta = x2 - x1 - width;
		drawShape(applyColors(ug).apply(UTranslate.dx(x1 + delta / 2)));
	}

	private UGraphic applyColors(UGraphic ug) {
		if (colors != null && colors.isOk()) {
			return colors.apply(ug);
		}
		return ug.apply(getLineColor()).apply(getBackgroundColor().bg());
	}

	private void drawShape(UGraphic ug) {
		ug.draw(getDiamond(ug.getStringBounder()));
	}

	public FingerPrint getFingerPrintNote(StringBounder stringBounder) {
		return null;
	}

	public FingerPrint getFingerPrint(StringBounder stringBounder) {
		final double h = getFullHeightTask(stringBounder);
		final double startPos = timeScale.getStartingPosition(start);
		return new FingerPrint(startPos, getY(stringBounder), startPos + h, getY(stringBounder) + h);
	}

	private UShape getDiamond(StringBounder stringBounder) {
		final double h = getShapeHeight(stringBounder);
		final UPolygon result = new UPolygon();
		result.addPoint(h / 2, 0);
		result.addPoint(h, h / 2);
		result.addPoint(h / 2, h);
		result.addPoint(0, h / 2);
		return result;
	}

	public double getX1(TaskAttribute taskAttribute) {
		final double x1 = timeScale.getStartingPosition(start);
		final double x2 = timeScale.getEndingPosition(start);
		final double width = getShapeHeight(null);
		final double delta = x2 - x1 - width;
		return x1 + delta;
	}

	public double getX2(TaskAttribute taskAttribute) {
		final double x1 = timeScale.getStartingPosition(start);
		final double x2 = timeScale.getEndingPosition(start);
		final double width = getShapeHeight(null);
		final double delta = x2 - x1 - width;
		return x2 - delta;
	}

}
