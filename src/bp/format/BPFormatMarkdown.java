package bp.format;

public class BPFormatMarkdown implements BPFormat
{
	public final static String FORMAT_MARKDOWN = "MarkDown";

	public String getName()
	{
		return FORMAT_MARKDOWN;
	}

	public String[] getExts()
	{
		return new String[] { ".md", "text/markdown" };
	}
}
