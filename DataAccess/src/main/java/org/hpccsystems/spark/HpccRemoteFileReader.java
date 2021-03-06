/*******************************************************************************
 *     HPCC SYSTEMS software Copyright (C) 2018 HPCC Systems®.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *******************************************************************************/
package org.hpccsystems.spark;

import org.apache.spark.sql.Row;
import org.hpccsystems.spark.thor.BinaryRecordReader;
import org.hpccsystems.spark.thor.DataPartition;


/**
 * Remote file reader used by the HpccRDD.
 */
public class HpccRemoteFileReader {
  private RecordDef def;
  private DataPartition fp;
  private BinaryRecordReader brr;
  /**
   * A remote file reader that reads the part identified by the
   * HpccPart object using the record definition provided.
   * @param def the definition of the data
   * @param fp the part of the file, name and location
   */
  public HpccRemoteFileReader(DataPartition fp, RecordDef rd) {
    this.def = rd;
    this.fp = fp;
    this.brr = new BinaryRecordReader(fp, def);
  }
  /**
   * Is there more data
   * @return true if there is a next record
   */
  public boolean hasNext() {
    boolean rslt;
    try {
      rslt = brr.hasNext();
    } catch (HpccFileException e) {
      rslt = false;
      System.err.println("Read failure for " + fp.toString());
      e.printStackTrace(System.err);
    }
    return rslt;
  }
  /**
   * Return next record
   * @return the record
   */
  public Row next() {
    Row rslt = null;
    try {
      rslt = brr.getNext();
    } catch (HpccFileException e) {
      System.err.println("Read failure for " + fp.toString());
      e.printStackTrace(System.err);
      throw new java.util.NoSuchElementException("Fatal read error");
    }
    return rslt;
  }
}
