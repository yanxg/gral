/*
 * GRAL: GRAphing Library for Java(R)
 *
 * (C) Copyright 2009-2011 Erich Seifert <dev[at]erichseifert.de>,
 * Michael Seifert <michael.seifert[at]gmx.net>
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
package de.erichseifert.gral.plots.points;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import org.junit.BeforeClass;
import org.junit.Test;

import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.data.Row;

public class SizeablePointsRendererTest {
	private static DataTable table;
	private static Shape shape;

	@BeforeClass
	public static void setUpBeforeClass() {
		table = new DataTable(Integer.class, Integer.class, Integer.class);
		table.add(1, 3,  1); // 0
		table.add(2, 1,  2); // 1
		table.add(3, 2, -1); // 2

		shape = new Rectangle2D.Double(-5.0, -5.0, 10.0, 10.0);
	}

	@Test
	public void testPointPath() {
		// Unsized shape
		PointRenderer unsized = new SizeablePointRenderer();
		unsized.setSetting(PointRenderer.SHAPE, shape);
		Shape unsizedExpected = shape;
		Shape unsizedPath = unsized.getPointPath(new Row(table, 0));
		assertEquals(unsizedExpected.getBounds2D(), unsizedPath.getBounds2D());

		// Unsized shape
		PointRenderer sized = new SizeablePointRenderer();
		sized.setSetting(PointRenderer.SHAPE, shape);
		Shape sizedExpected = AffineTransform.getScaleInstance(2.0, 2.0)
				.createTransformedShape(shape);
		Shape sizedPath = sized.getPointPath(new Row(table, 1));
		assertEquals(sizedExpected.getBounds2D(), sizedPath.getBounds2D());

		// Invalid size
		PointRenderer invalid = new SizeablePointRenderer();
		invalid.setSetting(PointRenderer.SHAPE, shape);
		Shape invalidPath = invalid.getPointPath(new Row(table, 2));
		assertNull(invalidPath);
	}

}