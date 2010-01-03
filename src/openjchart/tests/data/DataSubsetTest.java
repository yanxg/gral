/* OpenJChart : a free plotting library for the Java(tm) platform
 *
 * (C) Copyright 2009, by Erich Seifert and Michael Seifert.
 *
 * This file is part of OpenJChart.
 *
 * OpenJChart is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenJChart is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenJChart.  If not, see <http://www.gnu.org/licenses/>.
 */

package openjchart.tests.data;

import static org.junit.Assert.assertEquals;
import openjchart.data.DataSubset;
import openjchart.data.DataTable;

import org.junit.BeforeClass;
import org.junit.Test;

public class DataSubsetTest {
	private static final double DELTA = 1e-15;
	private static DataTable table;

	@BeforeClass
	public static void setUpBeforeClass() {
		table = new DataTable(Integer.class, Integer.class);
		table.add(1, 1); // 0
		table.add(2, 3); // 1
		table.add(3, 2); // 2
		table.add(4, 6); // 3
		table.add(5, 4); // 4
		table.add(6, 8); // 5
		table.add(7, 9); // 6
		table.add(8, 11); // 7
	}

	@Test
	public void testCreation() {
		DataSubset data = new DataSubset(table) {
			@Override
			public boolean accept(Number[] row) {
				return (row[0].doubleValue() % 2.0) == 0.0;
			}
		};
		assertEquals(table.getColumnCount(), data.getColumnCount());
		assertEquals(table.getRowCount()/2, data.getRowCount());
		assertEquals( 2.0, data.get(0, 0).doubleValue(), DELTA);
		assertEquals( 4.0, data.get(0, 1).doubleValue(), DELTA);
		assertEquals( 6.0, data.get(0, 2).doubleValue(), DELTA);
		assertEquals( 8.0, data.get(0, 3).doubleValue(), DELTA);
		assertEquals( 3.0, data.get(1, 0).doubleValue(), DELTA);
		assertEquals( 6.0, data.get(1, 1).doubleValue(), DELTA);
		assertEquals( 8.0, data.get(1, 2).doubleValue(), DELTA);
		assertEquals(11.0, data.get(1, 3).doubleValue(), DELTA);
	}

}
