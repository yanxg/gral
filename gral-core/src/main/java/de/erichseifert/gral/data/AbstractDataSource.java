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
package de.erichseifert.gral.data;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import de.erichseifert.gral.data.statistics.Statistics;


/**
 * Abstract implementation of the <code>DataSource</code> interface.
 * This class provides access to statistical information,
 * administration and notification of listeners and supports
 * iteration of data values.
 */
public abstract class AbstractDataSource implements DataSource {
	/** Number of columns. */
	private int columnCount;
	/** Data types that are allowed in the respective columns. */
	private Class<? extends Number>[] types;
	/** Set of objects that will be notified of changes to the data values. */
	private final Set<DataListener> dataListeners;
	/** Statistical description of the data values. */
	private Statistics statistics;

	/**
	 * Iterator that returns each row of the DataSource.
	 */
	private class DataSourceIterator implements Iterator<Number> {
		/** Index of current column. */
		private int col;
		/** Index of current row. */
		private int row;

		/**
		 * Initializes a new iterator instance that starts at
		 * <code>(0, 0)</code>.
		 */
		public DataSourceIterator() {
			col = 0;
			row = 0;
		}

	    /**
	     * Returns <code>true</code> if the iteration has more elements.
	     * (In other words, returns <code>true</code> if <code>next</code>
	     * would return an element rather than throwing an exception.)
	     * @return <code>true</code> if the iterator has more elements.
	     */
		public boolean hasNext() {
			return (col < getColumnCount()) && (row < getRowCount());
		}

	    /**
	     * Returns the next element in the iteration.
	     * @return the next element in the iteration.
	     * @exception NoSuchElementException iteration has no more elements.
	     */
		public Number next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			Number value = get(col, row);
			if (++col >= getColumnCount()) {
				col = 0;
				++row;
			}
			return value;
		}

	    /**
	     * <code>remove</code> method to fulfill <code>Iterator</code>
	     * interface.
	     */
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * Initializes a new instance with the specified number of columns and
	 * column types.
	 * @param types type for each column
	 */
	public AbstractDataSource(Class<? extends Number>... types) {
		setColumnTypes(types);
		dataListeners = new LinkedHashSet<DataListener>();
	}

	/**
	 * Retrieves a object instance that contains various statistical
	 * information on the current data source.
	 * @return statistical information
	 */
	public Statistics getStatistics() {
		if (statistics == null) {
			statistics = new Statistics(this);
		}
		return statistics;
	}

	/**
	 * Adds the specified <code>DataListener</code> to this data source.
	 * @param dataListener listener to be added.
	 */
	public void addDataListener(DataListener dataListener) {
		dataListeners.add(dataListener);
	}

	/**
	 * Removes the specified <code>DataListener</code> from this data source.
	 * @param dataListener listener to be removed.
	 */
	public void removeDataListener(DataListener dataListener) {
		dataListeners.remove(dataListener);
	}

    /**
     * Returns an iterator over a set of elements of type T.
     *
     * @return an Iterator.
     */
	public Iterator<Number> iterator() {
		return new DataSourceIterator();
	}

	/**
	 * Notifies all registered listeners that data values have been added.
	 * @param events Event objects describing all values that have been added.
	 */
	protected void notifyDataAdded(DataChangeEvent... events) {
		for (DataListener dataListener : dataListeners) {
			dataListener.dataAdded(this, events);
		}
	}

	/**
	 * Notifies all registered listeners that data values have been removed.
	 * @param events Event objects describing all values that have been removed.
	 */
	protected void notifyDataRemoved(DataChangeEvent... events) {
		for (DataListener dataListener : dataListeners) {
			dataListener.dataRemoved(this, events);
		}
	}

	/**
	 * Notifies all registered listeners that data values have changed.
	 * @param events Event objects describing all values that have changed.
	 */
	protected void notifyDataUpdated(DataChangeEvent... events) {
		for (DataListener dataListener : dataListeners) {
			dataListener.dataUpdated(this, events);
		}
	}

	/**
	 * Returns the column with the specified index.
	 * @param col index of the column to return
	 * @return the specified column of the data source
	 */
	public Column getColumn(int col) {
		return new Column(this, col);
	}

	/**
	 * Returns the number of columns of the data source.
	 * @return number of columns in the data source.
	 */
	public int getColumnCount() {
		return columnCount;
	}


	/**
	 * Returns the data types of all columns.
	 * @return The data types of all column in the data source
	 */
	public Class<? extends Number>[] getColumnTypes() {
		Class<? extends Number>[] types = Arrays.copyOf(this.types, this.types.length);
		return types;
	}

	/**
	 * Sets the data types of all columns. This also changes the  number of
	 * columns.
	 * @param types Data types.
	 */
	protected void setColumnTypes(Class<? extends Number>... types) {
		this.types = Arrays.copyOf(types, types.length);
		columnCount = types.length;
	}

	/**
	 * Returns the row with the specified index.
	 * @param row index of the row to return
	 * @return the specified row of the data source
	 */
	public Row getRow(int row) {
		return new Row(this, row);
	}
}