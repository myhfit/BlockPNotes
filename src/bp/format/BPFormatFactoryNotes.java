package bp.format;

import java.util.function.Consumer;

public class BPFormatFactoryNotes implements BPFormatFactory
{
	public void register(Consumer<BPFormat> regfunc)
	{
		regfunc.accept(new BPFormatMarkdown());
		regfunc.accept(new BPFormatNotesJSON());
	}
}
