package org.benhur.utility.database.dependencies;

public interface IColumn
{
  String getId();

  String getName();

  void setForeignColumn(IColumn foreignColumn);

  IColumn getForeignColumn();
}
