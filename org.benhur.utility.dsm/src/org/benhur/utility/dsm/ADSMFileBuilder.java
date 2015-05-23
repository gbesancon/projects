package org.benhur.utility.dsm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.benhur.utility.text.TextUtility;

public abstract class ADSMFileBuilder<TInput>
{
  protected static char COLUMN_CHAR = '|';
  protected static char ROW_CHAR = '-';
  protected static int ID_LENGTH = 2;

  public void createDSMFile(TInput input, String dsmFilepath) throws IOException
  {
    FileWriter fileWriter = new FileWriter(new File(dsmFilepath));

    buildDSMFile(fileWriter, input);

    fileWriter.close();
  }

  protected abstract void buildDSMFile(FileWriter fileWriter, TInput input) throws IOException;

  protected String GetRowTitle(int nameMaxLength, int nbItems)
  {
    StringBuilder builder = new StringBuilder();
    builder.append(COLUMN_CHAR);
    builder.append(" ");
    builder.append(TextUtility.PadText("", nameMaxLength, ' '));
    builder.append(" ");
    builder.append(COLUMN_CHAR);
    builder.append(GetCellText(""));
    for (int iItem = 0; iItem < nbItems; iItem++)
    {
      builder.append(GetCellText(GetHexa(iItem + 1)));
    }
    builder.append("\n");
    return builder.toString();
  }

  protected String GetRowSeparator(int nameMaxLength, int nbItems)
  {
    StringBuilder builder = new StringBuilder();
    builder.append(TextUtility.PadText("", 1 + 1 + nameMaxLength + 1 + 1 + ID_LENGTH + 1 + GetCellText("").length()
        * nbItems, ROW_CHAR));
    builder.append("\n");
    return builder.toString();
  }

  protected String GetRowEmpty(int nameMaxLength, int nbItems)
  {
    StringBuilder builder = new StringBuilder();
    builder.append(TextUtility.PadText("", 1 + 1 + nameMaxLength + 1 + ID_LENGTH + 1, ' '));
    builder.append(COLUMN_CHAR);
    for (int iItem = 0; iItem < nbItems; iItem++)
    {
      builder.append(GetCellText(""));
    }
    builder.append("\n");
    return builder.toString();
  }

  protected String GetCellText(String value)
  {
    StringBuilder builder = new StringBuilder();
    builder.append(TextUtility.PadText(value, ID_LENGTH, ' '));
    builder.append(COLUMN_CHAR);
    return builder.toString();
  }

  protected String GetHexa(int value)
  {
    final StringBuilder builder = new StringBuilder();

    int number = value - 1;
    while (number >= 0)
    {
      int numChar = (number % 26) + 65;
      builder.append((char) numChar);
      number = (number / 26) - 1;
    }
    return builder.reverse().toString();
  }
}
