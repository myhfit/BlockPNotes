package bp.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bp.res.BPResourceDir;
import bp.util.ObjUtil;

public class BPProjectFactoryNotes implements BPProjectFactory
{
	private final static String S_PRJTYPE_NOTES = "notes";

	public BPResourceProject create(String prjtype, BPResourceDir dir, Map<String, String> prjdata)
	{
		BPResourceProjectNotes rc = null;
		if (S_PRJTYPE_NOTES.equals(prjtype))
		{
			rc = new BPResourceProjectNotes(dir, ObjUtil.toBool(prjdata.get("nocache"), false));
			if (prjdata.containsKey("name"))
				rc.setName(prjdata.get("name"));
			if (prjdata.containsKey("path"))
				rc.setPath(prjdata.get("path"));
		}
		return rc;
	}

	public Class<? extends BPResourceProject> getProjectClass()
	{
		return BPResourceProjectNotes.class;
	}

	public List<String> getProjectTypes()
	{
		List<String> rc = new ArrayList<String>();
		rc.add(S_PRJTYPE_NOTES);
		return rc;
	}

	public String getName()
	{
		return "Notes Project";
	}

}
