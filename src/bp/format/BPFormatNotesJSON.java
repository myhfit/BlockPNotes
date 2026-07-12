package bp.format;

public class BPFormatNotesJSON extends BPFormatBase
{
	public final static String FORMAT_NOTES_JSON = "NotesJSON";

	public String getName()
	{
		return FORMAT_NOTES_JSON;
	}

	public String[] getExts()
	{
		return new String[] { ".bpnj" };
	}
}
