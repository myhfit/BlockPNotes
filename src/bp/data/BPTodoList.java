package bp.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bp.util.ObjUtil;

public class BPTodoList implements BPSLData
{
	protected String m_name;
	protected List<BPTodoItem> m_items;

	public BPTodoList()
	{
		m_items = new ArrayList<BPTodoItem>();
	}

	public String getName()
	{
		return m_name;
	}

	public void setName(String name)
	{
		m_name = name;
	}

	public List<BPTodoItem> getItems()
	{
		return m_items;
	}

	public Map<String, Object> getMappedData()
	{
		Map<String, Object> rc = new HashMap<String, Object>();
		rc.put("name", m_name);
		List<Map<String, Object>> itemmos = new ArrayList<Map<String, Object>>();
		List<BPTodoItem> items = m_items;
		for (BPTodoItem item : items)
		{
			itemmos.add(item.getSaveData());
		}
		rc.put("items", itemmos);
		return rc;
	}

	@SuppressWarnings("unchecked")
	public void setMappedData(Map<String, Object> data)
	{
		m_name = ObjUtil.toString(data.get("name"));
		List<Map<String, Object>> itemmos = (List<Map<String, Object>>) data.get("items");
		List<BPTodoItem> items = new ArrayList<BPTodoItem>();
		for (Map<String, Object> itemmo : itemmos)
		{
			BPTodoItem item = ObjUtil.mapToObj2(itemmo, false);
			items.add(item);
		}
		m_items.clear();
		m_items.addAll(items);
	}

	public enum BPTodoItemType
	{
		NORMAL, END_TIME, DEADLINE, START_TIME, WISH_LIST
	}

	public static class BPTodoItem implements BPSLData
	{
		public String name;
		public String content;
		public String contenttype;
		public boolean done;
		public Object userdata;
		public BPTodoItemType eletype;

		public BPTodoItem()
		{
			eletype = BPTodoItemType.NORMAL;
		}

		public Map<String, Object> getMappedData()
		{
			Map<String, Object> rc = new HashMap<String, Object>();
			rc.put("name", name);
			rc.put("content", content);
			rc.put("contenttype", contenttype);
			rc.put("userdata", ObjUtil.objToMap(userdata));
			rc.put("done", done);
			rc.put("eletype", eletype.ordinal());
			return rc;
		}

		@SuppressWarnings("unchecked")
		public void setMappedData(Map<String, Object> data)
		{
			name = ObjUtil.toString(data.get("name"));
			content = ObjUtil.toString(data.get("content"));
			contenttype = ObjUtil.toString(data.get("contenttype"));
			userdata = ObjUtil.mapToObj2((Map<String, ?>) data.get("userdata"), true);
			done = ObjUtil.toBool(data.get("done"), false);
			eletype = ObjUtil.enumFromOrdinal(BPTodoItemType.class, ObjUtil.toInt(data.get("eletype"), 0));
		}
	}
}
