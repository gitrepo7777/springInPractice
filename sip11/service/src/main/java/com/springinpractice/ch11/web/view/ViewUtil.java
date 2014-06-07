/* 
 * Copyright 2011-2013 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springinpractice.ch11.web.view;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
public final class ViewUtil {
	
	/**
	 * @param list
	 * @param numCols
	 * @return
	 */
	public static <T> List<List<T>> toRows(List<T> list, int numCols) {
		int numElems = list.size();
		int numRows = (numElems + 2) / 3;
		
		List<List<T>> rows = new ArrayList<List<T>>();
		for (int i = 0; i < numRows; i++) {
			List<T> row = new ArrayList<T>();
			for (int j = 0; j < numCols; j++) {
				int listIndex = (j * numRows) + i;
				if (listIndex < numElems) {
					row.add(list.get(listIndex));
				}
			}
			rows.add(row);
		}
		
		return rows;
	}
	
	/**
	 * @param list
	 * @param numCols
	 * @return
	 */
	public static <T> List<List<T>> toColumns(List<T> list, int numCols) {
		int numElems = list.size();
		int numRows = (numElems + 2) / 3;
		
		List<List<T>> columns = new ArrayList<List<T>>();
		for (int i = 0; i < numCols; i++) {
			int fromIndex = i * numRows;
			int toIndex = Math.min((i + 1) * numRows, numElems);
			columns.add(list.subList(fromIndex, toIndex));
		}
		
		return columns;
	}
}
