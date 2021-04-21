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

import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.project.core.Task;
import net.sourceforge.plantuml.project.core.TaskAttribute;
import net.sourceforge.plantuml.project.lang.CenterBorderColor;
import net.sourceforge.plantuml.project.time.Day;
import net.sourceforge.plantuml.project.timescale.TimeScale;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class TaskDrawSeparator implements TaskDraw {

	private final TimeScale timeScale;
	private double y;
	private final Day min;
	private final Day max;
	private final String name;
	private final StyleBuilder styleBuilder;
	private final HColorSet colorSet;

	public TaskDrawSeparator(String name, TimeScale timeScale, double y, Day min, Day max, StyleBuilder styleBuilder,
			HColorSet colorSet) {
		this.styleBuilder = styleBuilder;
		this.colorSet = colorSet;
		this.name = name;
		this.y = y;
		this.timeScale = timeScale;
		this.min = min;
		this.max = max;
	}

	public void drawTitle(UGraphic ug) {
		final ClockwiseTopRightBottomLeft padding = getStyle().getPadding();
		final ClockwiseTopRightBottomLeft margin = getStyle().getMargin();
		final double dx = margin.getLeft() + padding.getLeft();
		final double dy = margin.getTop() + padding.getTop();
		getTitle().drawU(ug.apply(new UTranslate(dx, dy)));
	}

	private StyleSignature getStyleSignature() {
		return StyleSignature.of(SName.root, SName.element, SName.ganttDiagram, SName.separator);
	}

	private Style getStyle() {
		return getStyleSignature().getMergedStyle(styleBuilder);
	}

	private TextBlock getTitle() {
		if (name == null) {
			return TextBlockUtils.empty(0, 0);
		}
		return Display.getWithNewlines(this.name).create(getFontConfiguration(), HorizontalAlignment.LEFT,
				new SpriteContainerEmpty());
	}

	private FontConfiguration getFontConfiguration() {
		return getStyle().getFontConfiguration(colorSet);
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final double widthTitle = getTitle().calculateDimension(stringBounder).getWidth();
		final double start = timeScale.getStartingPosition(min);
		// final double start2 = start1 + widthTitle;
		final double end = timeScale.getEndingPosition(max);

		final ClockwiseTopRightBottomLeft padding = getStyle().getPadding();
		final ClockwiseTopRightBottomLeft margin = getStyle().getMargin();
		ug = ug.apply(new UTranslate(0, margin.getTop()));

		final HColor backColor = getStyle().value(PName.BackGroundColor).asColor(colorSet);

		if (HColorUtils.isTransparent(backColor) == false) {
			final double height = padding.getTop() + getTextHeight(stringBounder) + padding.getBottom();
			if (height > 0) {
				final URectangle rect = new URectangle(end - start, height);
				ug.apply(backColor.bg()).draw(rect);
			}
		}

		final HColor lineColor = getStyle().value(PName.LineColor).asColor(colorSet);
		ug = ug.apply(lineColor);
		ug = ug.apply(UTranslate.dy(padding.getTop() + getTextHeight(stringBounder) / 2));

		if (widthTitle == 0) {
			final ULine line = ULine.hline(end - start);
			ug.draw(line);
		} else {
			if (padding.getLeft() > 1) {
				final ULine line1 = ULine.hline(padding.getLeft());
				ug.draw(line1);
			}
			final double x1 = padding.getLeft() + margin.getLeft() + widthTitle + margin.getRight();
			final double x2 = end - 1;
			final ULine line2 = ULine.hline(x2 - x1);
			ug.apply(UTranslate.dx(x1)).draw(line2);
		}
	}

	public FingerPrint getFingerPrint(StringBounder stringBounder) {
		final double h = getFullHeightTask(stringBounder);
		final double end = timeScale.getEndingPosition(max);
		return new FingerPrint(0, y, end, y + h);
	}

	public double getFullHeightTask(StringBounder stringBounder) {
		final ClockwiseTopRightBottomLeft padding = getStyle().getPadding();
		final ClockwiseTopRightBottomLeft margin = getStyle().getMargin();
		return margin.getTop() + padding.getTop() + getTextHeight(stringBounder) + padding.getBottom()
				+ margin.getBottom();
	}

	private double getTextHeight(StringBounder stringBounder) {
		return getTitle().calculateDimension(stringBounder).getHeight();
	}

	public double getY(StringBounder stringBounder) {
		return y;
	}

	public void pushMe(double deltaY) {
		this.y += deltaY;
	}

	public TaskDraw getTrueRow() {
		return null;
	}

	public void setColorsAndCompletion(CenterBorderColor colors, int completion, Url url, Display note) {
	}

	public Task getTask() {
		throw new UnsupportedOperationException();
	}

	public double getY(StringBounder stringBounder, Direction direction) {
		throw new UnsupportedOperationException();
	}

	public FingerPrint getFingerPrintNote(StringBounder stringBounder) {
		return null;
	}

	public double getHeightMax(StringBounder stringBounder) {
		return getFullHeightTask(stringBounder);
	}

	public double getX1(TaskAttribute taskAttribute) {
		throw new UnsupportedOperationException();
	}

	public double getX2(TaskAttribute taskAttribute) {
		throw new UnsupportedOperationException();
	}

}
