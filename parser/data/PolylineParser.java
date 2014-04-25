package org.philhosoft.mif.parser.data;


import org.philhosoft.mif.model.data.MifData;
import org.philhosoft.mif.model.data.Polyline;
import org.philhosoft.mif.model.parameter.CoordinatePair;
import org.philhosoft.mif.model.parameter.Pen;
import org.philhosoft.mif.parser.DefaultParser;
import org.philhosoft.mif.parser.ParsingContext;
import org.philhosoft.mif.parser.parameter.CoordinatePairParser;
import org.philhosoft.mif.parser.parameter.PenParser;


/*
 PLINE [ MULTIPLE numsections ]
   numpts1
 x1 y1
 x2 y2
 :
 [  numpts2
 x1 y1
 x2 y2  ]
 :
 [ PEN (width, pattern, color) ]
 [ SMOOTH ]
 */
public class PolylineParser extends DefaultParser implements MifDataParser
{
	public static final String KEYWORD = "PLINE";

	private CoordinatePairParser coordinatesParser = new CoordinatePairParser();
	private PenParser penParser = new PenParser();

	@Override
	public String getKeyword()
	{
		return KEYWORD;
	}

	@Override
	public MifData parseData(ParsingContext context)
	{
		Polyline polyline = new Polyline();

		String line = context.getCurrentLine();
		if (line == null)
			throw new IllegalStateException();

		int sectionNumber = 1;
		if (line.length() > getKeyword().length())
		{
			String parameter = line.substring(getKeyword().length() + 1).trim();
			String[] parts = parameter.split("\\s+");
			if (parts.length == 2 && parts[0].equals("MULTIPLE"))
			{
				try
				{
					sectionNumber = Integer.valueOf(parts[1]);
				}
				catch (NumberFormatException e)
				{
					context.addError("Invalid number of sections for " + getKeyword());
				}
			}
		}

		for (int i = 0; i < sectionNumber; i++)
		{
			readSection(polyline, context);
		}

		// Read options
		while (context.readNextLine() && parseOption(polyline, context))
		{}

		return polyline;
	}

	private void readSection(Polyline polyline, ParsingContext context)
	{
		context.readNextLine();
		String line = context.getCurrentLine();
		int coordinateNb = 0;
		try
		{
			coordinateNb = Integer.valueOf(line);
		}
		catch (NumberFormatException e)
		{
			context.addError("Invalid number of coordinates for " + getKeyword());
			return;
		}
		polyline.addSection();
		for (int i = 0; i < coordinateNb; i++)
		{
			context.readNextLine();
			CoordinatePair coordinates = (CoordinatePair) coordinatesParser.parseParameter(context);
			polyline.addCoordinates(coordinates);
		}
	}

	private boolean parseOption(Polyline polyligne, ParsingContext context)
	{
		if (penParser.canParse(context))
		{
			Pen pen = penParser.parseParameter(context);
			polyligne.setPen(pen);
			return true;
		}
		if (context.getCurrentLine().equals("SMOOTH"))
		{
			polyligne.setSmooth(true);
			return true;
		}
		context.pushBackLine();
		return false;
	}
}
