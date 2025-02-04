package bp.ext;

public class BPExtensionLoaderNotes implements BPExtensionLoader
{
	public String getName()
	{
		return "Notes";
	}

	public boolean isUI()
	{
		return false;
	}

	public String getUIType()
	{
		return null;
	}

	public String[] getParentExts()
	{
		return new String[] { "WebCommonFormats" };
	}

	public String[] getDependencies()
	{
		return null;
	}
}
