/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.mobile.sdk;

import com.liferay.mobile.sdk.annotation.Param;
import com.liferay.mobile.sdk.annotation.Path;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Bruno Farache
 */
public class OrderByComparatorTest extends BaseTest {

	public OrderByComparatorTest() throws IOException {
		super();
	}

	@Test
	public void getEntriesDescending() throws Exception {
		BookmarksEntryService service = ServiceBuilder.build(
			BookmarksEntryService.class);

		long groupId = props.getGroupId();

		Call<JSONArray> call = service.getEntries(
			groupId, ServiceContextTest.PARENT_FOLDER_ID, -1, -1,
			new JSONObject());

		JSONArray entries = call.execute();
		assertEquals(2, entries.length());

		JSONObject first = entries.getJSONObject(0);
		JSONObject second = entries.getJSONObject(1);

		assertEquals("Z", first.getString("name"));
		assertEquals("A", second.getString("name"));
	}

	@Test
	public void nullOrderByComparator() throws Exception {
		BookmarksEntryService service = ServiceBuilder.build(
			BookmarksEntryService.class);

		long groupId = props.getGroupId();

		Call<JSONArray> call = service.getEntries(
			groupId, ServiceContextTest.PARENT_FOLDER_ID, -1, -1, null);

		JSONArray entries = call.execute();
		assertEquals(2, entries.length());

		JSONObject first = entries.getJSONObject(0);
		JSONObject second = entries.getJSONObject(1);

		assertEquals("A", first.getString("name"));
		assertEquals("Z", second.getString("name"));
	}

	@Before
	public void setUp() throws Exception {
		_serviceContextTest = new ServiceContextTest();
		_entryA = _serviceContextTest.addBookmarkEntry("A", null);
		_entryZ = _serviceContextTest.addBookmarkEntry("Z", null);
	}

	@After
	public void tearDown() throws Exception {
		_serviceContextTest.deleteBookmarkEntry(_entryA);
		_serviceContextTest.deleteBookmarkEntry(_entryZ);
	}

	@Path("/bookmarks.bookmarksentry")
	public interface BookmarksEntryService {

		@Path("/get-entries")
		Call<JSONArray> getEntries(
			@Param(name = "groupId") long groupId,
			@Param(name = "folderId") long folderId,
			@Param(name = "start") int start, @Param(name = "end") int end,
			@Param(
				name = "orderByComparator",
				className = "com.liferay.bookmarks.util.comparator." +
					"EntryNameComparator")
				JSONObject orderByComparator);

	}

	private JSONObject _entryA;
	private JSONObject _entryZ;
	private ServiceContextTest _serviceContextTest;

}