package bp.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bp.cache.BPCacheDataFileSystem;
import bp.cache.BPTreeCacheNode;
import bp.data.BPTodoList;
import bp.data.BPTodoList.BPTodoItem;
import bp.res.BPResource;
import bp.res.BPResourceDir;
import bp.res.BPResourceFileLocal;
import bp.util.DateUtil;
import bp.util.IOUtil;
import bp.util.JSONUtil;
import bp.util.ObjUtil;
import bp.util.Std;
import bp.util.TextUtil;

public class BPResourceProjectNotes extends BPResourceProjectFile
{
	public final static String PRJTYPE_NOTES = "notes";

	protected volatile Map<String, Object> m_stats = null;

	protected volatile List<BPTodoList> m_todos = null;

	public BPResourceProjectNotes(BPResourceDir dir, boolean nocache)
	{
		super(dir, nocache);
	}

	public String getResType()
	{
		return "notes project";
	}

	public BPResource wrapResource(BPResource res)
	{
		BPResource rc = null;
		if (res.isFileSystem())
		{
		}
		if (rc == null)
		{
			rc = super.wrapResource(res);
		}
		return rc;
	}

	public String getProjectTypeName()
	{
		return PRJTYPE_NOTES;
	}

	public BPResource createNote(String name, String ext)
	{
		return getResourceByPrefix(name, ext);
	}

	protected BPResource getResourceByPrefix(String resname, String ext)
	{
		String t = DateUtil.formatTime(System.currentTimeMillis(), "yyyy-MM");
		BPResourceDir dir = (BPResourceDir) getChild(t, true);
		if (dir == null)
		{
			dir = (BPResourceDir) createChild(t, false);
		}
		String tname = resname;
		BPResource sub = dir.getChild(tname + ext, true);
		while (sub != null)
		{
			tname = resname + randomSuffix();
			sub = dir.getChild(tname + ext, true);
		}
		return dir.getChild(tname + ext, false);
	}

	protected String randomSuffix()
	{
		String ct = (System.currentTimeMillis() + "");
		ct = ct.substring(ct.length() - 3);
		return "_" + ct + "_" + ((int) (100d * Math.random()));
	}

	public Map<String, Object> getOverview()
	{
		Map<String, Object> rc = super.getOverview();
		Map<String, Object> stats = m_stats;
		if (stats != null)
			rc.putAll(stats);
		return rc;
	}

	public Map<String, Object> getStatistics()
	{
		Map<String, Object> rc = m_stats;
		if (rc == null)
		{
			rc = makeStatistics();
			m_stats = rc;
		}
		return rc;
	}

	protected Map<String, Object> makeStatistics()
	{
		Map<String, Object> rc = new HashMap<String, Object>();

		List<BPTodoList> todolists = m_todos;
		List<Map<String, Object>> tdljson = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> todoitems = new ArrayList<Map<String, Object>>();
		rc.put("todoitems", todoitems);
		rc.put("todolists", tdljson);

		int todolistcount = 0;
		int todoitemcount = 0;

		if (todolists != null)
		{
			todolistcount = todolists.size();
			for (BPTodoList todolist : todolists)
			{
				if (todolist == null)
					continue;
				tdljson.add(todolist.getMappedData());
				List<BPTodoItem> items = todolist.getItems();
				for (BPTodoItem item : items)
				{
					if (!item.done)
					{
						todoitems.add(item.getMappedData());
						todoitemcount++;
					}
				}
			}
		}

		rc.put("count_todolist", todolistcount);
		rc.put("count_todoitem", todoitemcount);
		return rc;
	}

	public void refreshNote(BPTodoList todolist, BPResource res)
	{
		makeStatistics();
	}

	public void refreshByCache(BPTreeCacheNode<BPCacheDataFileSystem> root)
	{
		super.refreshByCache(root);
		long ct = System.currentTimeMillis();
		List<BPTreeCacheNode<?>> nodes = new ArrayList<BPTreeCacheNode<?>>();
		root.filter(node -> node.getKey().toLowerCase().endsWith(".todo.bpnj"), nodes);
		List<BPTodoList> todos = new ArrayList<BPTodoList>();
		for (BPTreeCacheNode<?> node : nodes)
		{
			BPCacheDataFileSystem f = (BPCacheDataFileSystem) node.getValue();
			BPResourceFileLocal fres = new BPResourceFileLocal(f.getFullName());
			try
			{
				String text = fres.useInputStream(in -> TextUtil.toString(IOUtil.read(in), "utf-8"));
				if (text != null)
				{
					Map<String, Object> mapdata = JSONUtil.decode(text);
					BPTodoList todo = ObjUtil.mapToObj2(mapdata, false);
					todos.add(todo);
				}
			}
			catch (Exception e)
			{
				Std.err(e);
			}
		}
		m_todos = todos;
		long ct2 = System.currentTimeMillis();
		Std.debug("TodoList Cache:" + todos.size() + " Loaded in " + (ct2 - ct) + "ms");
	}

	public void refreshStatistics()
	{
		m_stats = makeStatistics();
	}
}
